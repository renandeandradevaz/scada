package renan.controller;

import java.util.List;

import renan.anotacoes.Funcionalidade;
import renan.modelo.TipoArmamento;
import renan.hibernate.HibernateUtil;
import renan.sessao.SessaoGeral;
import renan.util.Util;
import renan.util.UtilController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class TipoArmamentoController {

	private final Result result;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public TipoArmamentoController(Result result, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoGeral = sessaoGeral;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(filhaDe = "criarEditarTipoArmamento")
	public void criarTipoArmamento() {

		sessaoGeral.adicionar("idTipoArmamento", null);
		result.forwardTo(this).criarEditarTipoArmamento();
	}

	@Path("/tipoArmamento/editarTipoArmamento/{tipoArmamento.id}")
	@Funcionalidade(filhaDe = "criarEditarTipoArmamento")
	public void editarTipoArmamento(TipoArmamento tipoArmamento) {

		tipoArmamento = hibernateUtil.selecionar(tipoArmamento);

		sessaoGeral.adicionar("idTipoArmamento", tipoArmamento.getId());
		result.include(tipoArmamento);
		result.forwardTo(this).criarEditarTipoArmamento();
	}

	@Funcionalidade(nome = "Criar e editar tipos de armamentos")
	public void criarEditarTipoArmamento() {
	}

	@Path("/tipoArmamento/excluirTipoArmamento/{tipoArmamento.id}")
	@Funcionalidade(nome = "Excluir tipos de armamentos")
	public void excluirTipoArmamento(TipoArmamento tipoArmamento) {

		hibernateUtil.deletar(tipoArmamento);
		result.include("sucesso", "Tipo de armamento excluído(a) com sucesso");
		result.forwardTo(this).listarTipoArmamentos(null, null);
	}

	@Funcionalidade(filhaDe = "criarEditarTipoArmamento")
	public void salvarTipoArmamento(TipoArmamento tipoArmamento) {

		if (Util.preenchido(sessaoGeral.getValor("idTipoArmamento"))) {

			tipoArmamento.setId((Integer) sessaoGeral.getValor("idTipoArmamento"));
		}

		hibernateUtil.salvarOuAtualizar(tipoArmamento);
		result.include("sucesso", "Tipo de armamento salvo(a) com sucesso");
		result.forwardTo(this).listarTipoArmamentos(new TipoArmamento(), null);
	}

	@Funcionalidade(nome = "Tipos de armamento", modulo = "New")
	public void listarTipoArmamentos(TipoArmamento tipoArmamento, Integer pagina) {

		tipoArmamento = (TipoArmamento) UtilController.preencherFiltros(tipoArmamento, "tipoArmamento", sessaoGeral);
		if (Util.vazio(tipoArmamento)) {
			tipoArmamento = new TipoArmamento();
		}

		List<TipoArmamento> tipoArmamentos = hibernateUtil.buscar(tipoArmamento, pagina);
		result.include("tipoArmamentos", tipoArmamentos);

	}
}
