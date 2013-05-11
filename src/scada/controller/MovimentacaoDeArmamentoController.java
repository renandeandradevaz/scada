package scada.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import scada.anotacoes.Funcionalidade;
import scada.filtros.FiltrosMovimentacaoDeArmamento;
import scada.hibernate.AliasHibernateUtil;
import scada.hibernate.HibernateUtil;
import scada.modelo.Armamento;
import scada.modelo.Cliente;
import scada.modelo.MovimentacaoDeArmamento;
import scada.modelo.Operador;
import scada.sessao.SessaoGeral;
import scada.sessao.SessaoMovimentacao;
import scada.sessao.SessaoOperador;
import scada.util.Util;
import scada.util.UtilController;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.view.Results;

@Resource
public class MovimentacaoDeArmamentoController {

	private final Result result;
	private SessaoGeral sessaoGeral;
	private SessaoOperador sessaoOperador;
	private SessaoMovimentacao sessaoMovimentacao;
	private HibernateUtil hibernateUtil;
	private Validator validator;

	public MovimentacaoDeArmamentoController(Result result, SessaoGeral sessaoGeral, SessaoOperador sessaoOperador, HibernateUtil hibernateUtil, Validator validator, SessaoMovimentacao sessaoMovimentacao) {
		this.result = result;
		this.sessaoGeral = sessaoGeral;
		this.sessaoOperador = sessaoOperador;
		this.sessaoMovimentacao = sessaoMovimentacao;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
		this.validator = validator;
	}

	@Funcionalidade(nome = "Acautelar armamentos")
	public void acautelarArmamentos() {

		this.sessaoMovimentacao = null;
	}

	@Funcionalidade(filhaDe = "acautelarArmamentos")
	public void autoCompleteClientes(String nomeCliente) {

		Cliente cliente = new Cliente();
		cliente.setNome(nomeCliente);

		List<Cliente> clientes = hibernateUtil.buscar(cliente, 1);

		result.use(Results.json()).from(clientes).serialize();

	}

	@Funcionalidade(filhaDe = "acautelarArmamentos")
	public void autoCompleteArmamentos(String numeracaoArmamento) {

		Armamento armamento = new Armamento();
		armamento.setNumeracao(numeracaoArmamento);

		List<String> status = new ArrayList<String>();
		status.add(Armamento.ARMAMENTO_DISPONIVEL_NÃO_ACAUTELADO);
		status.add(Armamento.ARMAMENTO_INDISPONIVEL_NÃO_ACAUTELADO);

		List<Criterion> restricoes = new ArrayList<Criterion>();
		restricoes.add(Restrictions.in("status", status));

		List<Armamento> armamentos = hibernateUtil.buscar(armamento, 1, restricoes);

		result.use(Results.json()).from(armamentos).include("tipoArmamento").serialize();

	}

	@Funcionalidade(filhaDe = "acautelarArmamentos")
	public void salvarAcautelamentos(SessaoMovimentacao sessaoMovimentacao, List<String> armamentosSelecionados) {

		this.sessaoMovimentacao = sessaoMovimentacao;
		this.sessaoMovimentacao.setArmamentosSelecionados(armamentosSelecionados);

		String nomeCliente = this.sessaoMovimentacao.getNomeCliente();

		validarCliente(nomeCliente);

		List<String> armamentosNaoRepetidos = new ArrayList<String>();

		if (Util.preenchido(armamentosSelecionados)) {

			for (String armamento : armamentosSelecionados) {

				if (Util.preenchido(armamento)) {

					if (!armamentosNaoRepetidos.contains(armamento)) {

						armamentosNaoRepetidos.add(armamento);
					}
				}
			}
		}

		if (Util.vazio(armamentosNaoRepetidos)) {

			validator.add(new ValidationMessage("Não foi selecionado nenhum armamento", "Erro"));

			validator.onErrorForwardTo(this).acautelarArmamentos();
		}

		else {

			for (String numeracaoArmamento : armamentosNaoRepetidos) {

				Armamento armamento = new Armamento();
				armamento.setNumeracao(numeracaoArmamento);

				try {

					armamento = hibernateUtil.selecionar(armamento, MatchMode.EXACT);
				}

				catch (Exception e) {

					validarArmamento(numeracaoArmamento);
				}

				if (Util.vazio(armamento)) {

					validarArmamento(numeracaoArmamento);
				}

				if (armamento.getStatus().equals(Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO) || armamento.getStatus().equals(Armamento.ARMAMENTO_INDISPONIVEL_ACAUTELADO)) {
					validator.add(new ValidationMessage("Armamento " + numeracaoArmamento + " já encontra-se acautelado.", "Erro"));

					validator.onErrorForwardTo(this).acautelarArmamentos();
				}

				if (armamento.getStatus().equals(Armamento.ARMAMENTO_DISPONIVEL_NÃO_ACAUTELADO)) {

					armamento.setStatus(Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO);
				} else {

					armamento.setStatus(Armamento.ARMAMENTO_INDISPONIVEL_ACAUTELADO);
				}

				Cliente cliente = new Cliente();
				cliente.setNome(nomeCliente);
				cliente = hibernateUtil.selecionar(cliente, MatchMode.EXACT);

				Operador operador = new Operador();
				operador.setLogin(sessaoOperador.getOperador().getLogin());
				operador = hibernateUtil.selecionar(operador, MatchMode.EXACT);

				MovimentacaoDeArmamento movimentacaoDeArmamento = new MovimentacaoDeArmamento();
				movimentacaoDeArmamento.setArmamento(armamento);
				movimentacaoDeArmamento.setCliente(cliente);
				movimentacaoDeArmamento.setOperador(operador);
				movimentacaoDeArmamento.setDataHora(new GregorianCalendar());
				movimentacaoDeArmamento.setTipoMovimentacao(MovimentacaoDeArmamento.TIPO_MOVIMENTACAO_ACAUTELAMENTO);
				movimentacaoDeArmamento.setDevolvido(false);
				movimentacaoDeArmamento.setDestino(this.sessaoMovimentacao.getDestino());
				movimentacaoDeArmamento.setObservacoes(this.sessaoMovimentacao.getObservacoes());

				hibernateUtil.salvarOuAtualizar(movimentacaoDeArmamento);
				hibernateUtil.salvarOuAtualizar(armamento);
			}
		}

		result.include("sucesso", "Acautelamentos realizados com sucesso");

		result.redirectTo(this).listarMovimentacaoDeArmamentos(new MovimentacaoDeArmamento(), new FiltrosMovimentacaoDeArmamento(), null);
	}

	private void validarArmamento(String numeracaoArmamento) {
		validator.add(new ValidationMessage("Armamento " + numeracaoArmamento + " não encontrado. Por favor, informe o código correto do armamento", "Erro"));

		validator.onErrorForwardTo(this).acautelarArmamentos();
	}

	private void validarCliente(String nomeCliente) {
		Cliente clienteFiltro = new Cliente();
		clienteFiltro.setNome(nomeCliente);

		if (hibernateUtil.contar(clienteFiltro, MatchMode.EXACT) != 1) {

			validator.add(new ValidationMessage("Não existe nenhum cliente com o nome informado. Por favor, informe o nome completo do cliente", "Erro"));
		}

		else {

			MovimentacaoDeArmamento movimentacaoDeArmamento = new MovimentacaoDeArmamento();
			movimentacaoDeArmamento.setCliente(clienteFiltro);
			movimentacaoDeArmamento.setDevolvido(false);

			if (hibernateUtil.contar(movimentacaoDeArmamento, MatchMode.EXACT) != 0) {

				validator.add(new ValidationMessage("O cliente " + nomeCliente + " possui acautelamentos em aberto que ainda não foram devolvidos. Para acautelar novos armamentos, devolva os antigos primeiro.", "Erro"));
			}
		}

		validator.onErrorForwardTo(this).acautelarArmamentos();
	}

	@Funcionalidade(nome = "Movimentações", modulo = "Controle de armamento")
	public void listarMovimentacaoDeArmamentos(MovimentacaoDeArmamento movimentacaoDeArmamento, FiltrosMovimentacaoDeArmamento filtrosMovimentacaoDeArmamento, Integer pagina) {

		movimentacaoDeArmamento = (MovimentacaoDeArmamento) UtilController.preencherFiltros(movimentacaoDeArmamento, "movimentacaoDeArmamento", sessaoGeral);
		filtrosMovimentacaoDeArmamento = (FiltrosMovimentacaoDeArmamento) UtilController.preencherFiltros(filtrosMovimentacaoDeArmamento, "filtrosMovimentacaoDeArmamento", sessaoGeral);
		if (Util.vazio(movimentacaoDeArmamento)) {
			movimentacaoDeArmamento = new MovimentacaoDeArmamento();
		}

		List<Criterion> restricoes = new ArrayList<Criterion>();

		if (Util.preenchido(movimentacaoDeArmamento.getTipoMovimentacao())) {

			restricoes.add(Restrictions.eq("tipoMovimentacao", movimentacaoDeArmamento.getTipoMovimentacao()));
		}

		if (Util.preenchido(filtrosMovimentacaoDeArmamento)) {

			GregorianCalendar dataInicial = null;
			GregorianCalendar dataFinal = null;

			if (Util.preenchido(filtrosMovimentacaoDeArmamento.getDataInicial())) {

				dataInicial = Util.copiaGregorianCalendar(filtrosMovimentacaoDeArmamento.getDataInicial());
			} else {
				dataInicial = new GregorianCalendar(1990, 1, 1);
			}

			if (Util.preenchido(filtrosMovimentacaoDeArmamento.getDataFinal())) {

				dataFinal = Util.copiaGregorianCalendar(filtrosMovimentacaoDeArmamento.getDataFinal());
				dataFinal.add(Calendar.DAY_OF_MONTH, +1);
			} else {
				dataFinal = new GregorianCalendar(2099, 1, 1);
			}

			restricoes.add(Restrictions.between("dataHora", dataInicial, dataFinal));

		}

		List<MovimentacaoDeArmamento> movimentacaoDeArmamentos = hibernateUtil.buscar(movimentacaoDeArmamento, pagina, restricoes);
		result.include("movimentacaoDeArmamentos", movimentacaoDeArmamentos);

		List<String> tiposMovimentacoes = MovimentacaoDeArmamento.listarTiposDeMovimentacoes();
		result.include("tiposMovimentacoes", tiposMovimentacoes);

	}

	@Funcionalidade(nome = "Devolver armamentos")
	public void devolverArmamentos() {

		List<String> status = new ArrayList<String>();
		status.add(Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO);
		status.add(Armamento.ARMAMENTO_INDISPONIVEL_ACAUTELADO);

		List<Criterion> restricoes = new ArrayList<Criterion>();
		restricoes.add(Restrictions.in("armamento.status", status));

		MovimentacaoDeArmamento movimentacaoDeArmamento = new MovimentacaoDeArmamento();
		movimentacaoDeArmamento.setDevolvido(false);

		List<AliasHibernateUtil> alias = new ArrayList<AliasHibernateUtil>();
		alias.add(new AliasHibernateUtil("armamento", "armamento"));

		List<MovimentacaoDeArmamento> movimentacoes = hibernateUtil.buscar(movimentacaoDeArmamento, restricoes, alias);

		result.include("movimentacoes", movimentacoes);
	}

	@Funcionalidade(filhaDe = "devolverArmamentos")
	public void salvarDevolucoes(List<Integer> idsAcautelamentos) {

		if (Util.preenchido(idsAcautelamentos)) {

			for (Integer idAcautelamento : idsAcautelamentos) {

				MovimentacaoDeArmamento acautelamento = hibernateUtil.selecionar(new MovimentacaoDeArmamento(idAcautelamento));

				Operador operador = new Operador();
				operador.setLogin(sessaoOperador.getOperador().getLogin());
				operador = hibernateUtil.selecionar(operador, MatchMode.EXACT);

				Armamento armamento = acautelamento.getArmamento();

				MovimentacaoDeArmamento devolucao = new MovimentacaoDeArmamento();
				devolucao.setArmamento(armamento);
				devolucao.setCliente(acautelamento.getCliente());
				devolucao.setOperador(operador);
				devolucao.setDataHora(new GregorianCalendar());
				devolucao.setTipoMovimentacao(MovimentacaoDeArmamento.TIPO_MOVIMENTACAO_DEVOLUCAO_ACAUTELAMENTO);

				if (armamento.getStatus().equals(Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO)) {

					armamento.setStatus(Armamento.ARMAMENTO_DISPONIVEL_NÃO_ACAUTELADO);
				} else {

					armamento.setStatus(Armamento.ARMAMENTO_INDISPONIVEL_NÃO_ACAUTELADO);
				}

				acautelamento.setDevolvido(true);

				hibernateUtil.salvarOuAtualizar(acautelamento);
				hibernateUtil.salvarOuAtualizar(devolucao);
				hibernateUtil.salvarOuAtualizar(armamento);

			}
			result.include("sucesso", "Armamento(s) devolvido(s) com sucesso.");
		}

		result.redirectTo(this).listarMovimentacaoDeArmamentos(new MovimentacaoDeArmamento(), new FiltrosMovimentacaoDeArmamento(), null);

	}
}
