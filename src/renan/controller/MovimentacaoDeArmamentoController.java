package renan.controller;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.Armamento;
import renan.modelo.Cliente;
import renan.modelo.MovimentacaoDeArmamento;
import renan.modelo.Operador;
import renan.sessao.SessaoGeral;
import renan.sessao.SessaoMovimentacao;
import renan.sessao.SessaoOperador;
import renan.util.Util;
import renan.util.UtilController;
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

		List<SimpleExpression> restricoes = new ArrayList<SimpleExpression>();
		restricoes.add((SimpleExpression) Restrictions.in("status", status));

		List<Armamento> armamentos = hibernateUtil.buscar(armamento, 1, restricoes);

		result.use(Results.json()).from(armamentos).serialize();

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

					validator.add(new ValidationMessage("Armamento " + numeracaoArmamento + " não encontrado. Por favor, informe o código correto do armamento", "Erro"));

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

				hibernateUtil.salvarOuAtualizar(movimentacaoDeArmamento);
				hibernateUtil.salvarOuAtualizar(armamento);
			}
		}

		result.redirectTo(this).listarMovimentacaoDeArmamentos(new MovimentacaoDeArmamento(), null);
	}

	private void validarCliente(String nomeCliente) {
		Cliente clienteFiltro = new Cliente();
		clienteFiltro.setNome(nomeCliente);

		if (hibernateUtil.contar(clienteFiltro, MatchMode.EXACT) != 1) {

			validator.add(new ValidationMessage("Não existe nenhum cliente com o nome informado. Por favor, informe o nome completo do cliente", "Erro"));

			validator.onErrorForwardTo(this).acautelarArmamentos();
		}
	}

	@Funcionalidade(nome = "Movimentações", modulo = "Em construção")
	public void listarMovimentacaoDeArmamentos(MovimentacaoDeArmamento movimentacaoDeArmamento, Integer pagina) {

		movimentacaoDeArmamento = (MovimentacaoDeArmamento) UtilController.preencherFiltros(movimentacaoDeArmamento, "movimentacaoDeArmamento", sessaoGeral);
		if (Util.vazio(movimentacaoDeArmamento)) {
			movimentacaoDeArmamento = new MovimentacaoDeArmamento();
		}

		List<MovimentacaoDeArmamento> movimentacaoDeArmamentos = hibernateUtil.buscar(movimentacaoDeArmamento, pagina);
		result.include("movimentacaoDeArmamentos", movimentacaoDeArmamentos);

	}
}
