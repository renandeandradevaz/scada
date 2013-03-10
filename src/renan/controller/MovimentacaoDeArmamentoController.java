package renan.controller;

import java.util.List;

import renan.anotacoes.Funcionalidade;
import renan.modelo.MovimentacaoDeArmamento;
import renan.hibernate.HibernateUtil;
import renan.sessao.SessaoGeral;
import renan.util.Util;
import renan.util.UtilController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

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

	@Funcionalidade(filhaDe = "criarEditarMovimentacaoDeArmamento")
	public void criarMovimentacaoDeArmamento() {

		sessaoGeral.adicionar("idMovimentacaoDeArmamento", null);
		result.forwardTo(this).criarEditarMovimentacaoDeArmamento();
	}

	@Path("/movimentacaoDeArmamento/editarMovimentacaoDeArmamento/{movimentacaoDeArmamento.id}")
	@Funcionalidade(filhaDe = "criarEditarMovimentacaoDeArmamento")
	public void editarMovimentacaoDeArmamento(MovimentacaoDeArmamento movimentacaoDeArmamento) {

		movimentacaoDeArmamento = hibernateUtil.selecionar(movimentacaoDeArmamento);

		sessaoGeral.adicionar("idMovimentacaoDeArmamento", movimentacaoDeArmamento.getId());
		result.include(movimentacaoDeArmamento);
		result.forwardTo(this).criarEditarMovimentacaoDeArmamento();
	}

	@Funcionalidade(nome = "Criar e editar movimentacaoDeArmamentos")
	public void criarEditarMovimentacaoDeArmamento() {
	}

	@Path("/movimentacaoDeArmamento/excluirMovimentacaoDeArmamento/{movimentacaoDeArmamento.id}")
	@Funcionalidade(nome = "Excluir movimentacaoDeArmamento")
	public void excluirMovimentacaoDeArmamento(MovimentacaoDeArmamento movimentacaoDeArmamento) {

		hibernateUtil.deletar(movimentacaoDeArmamento);
		result.include("sucesso", "MovimentacaoDeArmamento excluído(a) com sucesso");
		result.forwardTo(this).listarMovimentacaoDeArmamentos(null, null);
	}

	@Funcionalidade(filhaDe = "criarEditarMovimentacaoDeArmamento")
	public void salvarMovimentacaoDeArmamento(MovimentacaoDeArmamento movimentacaoDeArmamento) {

		if (Util.preenchido(sessaoGeral.getValor("idMovimentacaoDeArmamento"))) {

			movimentacaoDeArmamento.setId((Integer) sessaoGeral.getValor("idMovimentacaoDeArmamento"));
		}

		hibernateUtil.salvarOuAtualizar(movimentacaoDeArmamento);
		result.include("sucesso", "MovimentacaoDeArmamento salvo(a) com sucesso");
		result.forwardTo(this).listarMovimentacaoDeArmamentos(new MovimentacaoDeArmamento(), null);
	}

	@Funcionalidade(nome = "MovimentacaoDeArmamentos", modulo = "Em construção")
	public void listarMovimentacaoDeArmamentos(MovimentacaoDeArmamento movimentacaoDeArmamento, Integer pagina) {

		movimentacaoDeArmamento = (MovimentacaoDeArmamento) UtilController.preencherFiltros(movimentacaoDeArmamento, "movimentacaoDeArmamento", sessaoGeral);
		if (Util.vazio(movimentacaoDeArmamento)) {
			movimentacaoDeArmamento = new MovimentacaoDeArmamento();
		}

		List<MovimentacaoDeArmamento> movimentacaoDeArmamentos = hibernateUtil.buscar(movimentacaoDeArmamento, pagina);
		result.include("movimentacaoDeArmamentos", movimentacaoDeArmamentos);

	}
}
