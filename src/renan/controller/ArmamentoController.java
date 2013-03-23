package renan.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.Armamento;
import renan.modelo.TipoArmamento;
import renan.sessao.SessaoGeral;
import renan.util.Util;
import renan.util.UtilController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ArmamentoController {

	private final Result result;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public ArmamentoController(Result result, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoGeral = sessaoGeral;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(filhaDe = "criarEditarArmamento")
	public void criarArmamento() {

		sessaoGeral.adicionar("idArmamento", null);
		result.forwardTo(this).criarEditarArmamento();
	}

	@Path("/armamento/editarArmamento/{armamento.id}")
	@Funcionalidade(filhaDe = "criarEditarArmamento")
	public void editarArmamento(Armamento armamento) {

		armamento = hibernateUtil.selecionar(armamento);

		sessaoGeral.adicionar("idArmamento", armamento.getId());
		result.include(armamento);
		result.forwardTo(this).criarEditarArmamento();
	}

	@Funcionalidade(nome = "Criar e editar armamentos")
	public void criarEditarArmamento() {

		List<TipoArmamento> tiposDeArmamento = hibernateUtil.buscar(new TipoArmamento());
		result.include("tiposDeArmamento", tiposDeArmamento);

		listarStatusArmamento();
	}

	private void listarStatusArmamento() {

		List<String> status = Armamento.listarStatus();
		result.include("statusArmamento", status);
	}

	@Path("/armamento/excluirArmamento/{armamento.id}")
	@Funcionalidade(nome = "Excluir armamento")
	public void excluirArmamento(Armamento armamento) {

		hibernateUtil.deletar(armamento);
		result.include("sucesso", "Armamento excluído(a) com sucesso");
		result.forwardTo(this).listarArmamentos(null, null);
	}

	@Funcionalidade(filhaDe = "criarEditarArmamento")
	public void salvarArmamento(Armamento armamento) {

		if (Util.preenchido(sessaoGeral.getValor("idArmamento"))) {

			armamento.setId((Integer) sessaoGeral.getValor("idArmamento"));
		}

		hibernateUtil.salvarOuAtualizar(armamento);
		result.include("sucesso", "Armamento salvo(a) com sucesso");
		result.redirectTo(this).listarArmamentos(new Armamento(), null);
	}

	@Funcionalidade(nome = "Armamentos", modulo = "Material bélico")
	public void listarArmamentos(Armamento armamento, Integer pagina) {

		armamento = (Armamento) UtilController.preencherFiltros(armamento, "armamento", sessaoGeral);
		if (Util.vazio(armamento)) {
			armamento = new Armamento();
		}

		List<SimpleExpression> restricoes = new ArrayList<SimpleExpression>();

		if (Util.preenchido(armamento.getStatus())) {

			restricoes.add(Restrictions.eq("status", armamento.getStatus()));
		}

		List<Armamento> armamentos = hibernateUtil.buscar(armamento, pagina, restricoes);
		result.include("armamentos", armamentos);

		listarStatusArmamento();
	}
}
