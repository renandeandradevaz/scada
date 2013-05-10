package scada.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import scada.anotacoes.Funcionalidade;
import scada.hibernate.HibernateUtil;
import scada.modelo.Armamento;
import scada.modelo.TipoArmamento;
import scada.sessao.SessaoGeral;
import scada.util.Util;
import scada.util.UtilController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class ArmamentoController {

	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public ArmamentoController(Result result, Validator validator, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {
		this.result = result;
		this.validator = validator;
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

		this.validarArmamento(armamento);

		hibernateUtil.salvarOuAtualizar(armamento);
		result.include("sucesso", "Armamento salvo com sucesso");
		result.redirectTo(this).listarArmamentos(new Armamento(), null);
	}

	private void validarArmamento(Armamento armamento) {

		Armamento armamentoFiltro = new Armamento();

		armamentoFiltro.setNumeracao(armamento.getNumeracao());
		armamentoFiltro.setTipoArmamento(armamento.getTipoArmamento());

		List<Armamento> armamentos = hibernateUtil.buscar(armamentoFiltro, MatchMode.EXACT);

		if (Util.preenchido(armamentos)) {

			if (Util.vazio(armamento.getId())) {

				validarArmamento();
			}

			else {

				if (!armamento.getId().equals(armamentos.get(0).getId())) {

					validarArmamento();
				}
			}
		}
	}

	private void validarArmamento() {

		validator.add(new ValidationMessage("Já existe um armamento deste tipo com esta numeração", "Erro"));
		validator.onErrorForwardTo(this).criarEditarArmamento();
	}

	@Funcionalidade(nome = "Armamentos", modulo = "Material bélico")
	public void listarArmamentos(Armamento armamento, Integer pagina) {

		armamento = (Armamento) UtilController.preencherFiltros(armamento, "armamento", sessaoGeral);
		if (Util.vazio(armamento)) {
			armamento = new Armamento();
		}

		List<Criterion> restricoes = new ArrayList<Criterion>();

		if (Util.preenchido(armamento.getStatus())) {

			restricoes.add(Restrictions.eq("status", armamento.getStatus()));
		}

		List<Armamento> armamentos = hibernateUtil.buscar(armamento, pagina, restricoes);
		result.include("armamentos", armamentos);

		listarStatusArmamento();
	}
}
