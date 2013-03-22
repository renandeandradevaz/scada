package renan.controller;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.Armamento;
import renan.modelo.Cliente;
import renan.modelo.MovimentacaoDeArmamento;
import renan.modelo.Operador;
import renan.sessao.SessaoGeral;
import renan.sessao.SessaoOperador;
import renan.util.Util;
import renan.util.UtilController;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class MovimentacaoDeArmamentoController {

	private final Result result;
	private SessaoGeral sessaoGeral;
	private SessaoOperador sessaoOperador;
	private HibernateUtil hibernateUtil;

	public MovimentacaoDeArmamentoController(Result result, SessaoGeral sessaoGeral, SessaoOperador sessaoOperador, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoGeral = sessaoGeral;
		this.sessaoOperador = sessaoOperador;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(nome = "Acautelar armamentos")
	public void acautelarArmamentos() {

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

		List<Armamento> armamentos = hibernateUtil.buscar(armamento, 1);

		result.use(Results.json()).from(armamentos).serialize();

	}

	@Funcionalidade(filhaDe = "acautelarArmamentos")
	public void salvarAcautelamentos(String nomeCliente, List<String> armamentosSelecionados) {

		// Fazer validações com nome do cliente utilizando o match mode exact

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

			// Fazer validações se a lista estiver vazia
		}

		else {

			for (String numeracaoArmamento : armamentosNaoRepetidos) {

				// Fazer validaçoes com o codigo do armamento utilizando o match
				// mode exact

				Armamento armamento = new Armamento();
				armamento.setNumeracao(numeracaoArmamento);
				armamento = hibernateUtil.selecionar(armamento, MatchMode.EXACT);

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
			}
		}

		result.redirectTo(this).listarMovimentacaoDeArmamentos(new MovimentacaoDeArmamento(), null);
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
