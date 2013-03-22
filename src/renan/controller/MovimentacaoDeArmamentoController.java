package renan.controller;

import java.util.List;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.Cliente;
import renan.modelo.MovimentacaoDeArmamento;
import renan.sessao.SessaoGeral;
import renan.util.Util;
import renan.util.UtilController;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class MovimentacaoDeArmamentoController {

	private final Result result;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public MovimentacaoDeArmamentoController(Result result, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoGeral = sessaoGeral;
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
