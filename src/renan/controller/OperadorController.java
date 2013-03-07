package renan.controller;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.GrupoOperador;
import renan.modelo.Operador;
import renan.sessao.SessaoGeral;
import renan.util.GeradorDeMd5;
import renan.util.Util;
import renan.util.UtilController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class OperadorController {

	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public OperadorController(Result result, Validator validator, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {

		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);

	}

	@Funcionalidade(filhaDe = "criarEditarOperador")
	public void criarOperador() {

		sessaoGeral.adicionar("idOperador", null);
		result.forwardTo(this).criarEditarOperador();
	}

	@Path("/operador/editarOperador/{operador.id}")
	@Funcionalidade(filhaDe = "criarEditarOperador")
	public void editarOperador(Operador operador) {

		operador = hibernateUtil.selecionar(operador);

		if (operador.getLogin().equals("administrador")) {

			validator.add(new ValidationMessage("Não é possível editar o operador administrador", "Erro"));

			validator.onErrorForwardTo(this).listarOperadors(null, null);
		}

		sessaoGeral.adicionar("idOperador", operador.getId());
		result.include(operador);
		result.forwardTo(this).criarEditarOperador();
	}

	@Funcionalidade(nome = "Criar e editar operadors")
	public void criarEditarOperador() {

		List<GrupoOperador> gruposOperador = hibernateUtil.buscar(new GrupoOperador());
		result.include("gruposOperador", gruposOperador);
	}

	@Path("/operador/excluirOperador/{operador.id}")
	@Funcionalidade(nome = "Excluir operador")
	public void excluirOperador(Operador operador) {

		Operador operadorSelecionado = hibernateUtil.selecionar(operador);

		if (operadorSelecionado.getLogin().equals("administrador")) {

			validator.add(new ValidationMessage("Não é possível excluir o operador administrador", "Erro"));

			validator.onErrorForwardTo(this).listarOperadors(null, null);
		}

		hibernateUtil.deletar(operadorSelecionado);
		result.include("sucesso", "Operador excluído com sucesso");
		result.forwardTo(this).listarOperadors(null, null);
	}

	@Funcionalidade(filhaDe = "criarEditarOperador")
	public void salvarOperador(Operador operador) {

		if (Util.vazio(sessaoGeral.getValor("idOperador"))) {

			validarNomesRepetidos(operador);
		}

		else {

			Operador operadorSelecionado = hibernateUtil.selecionar(new Operador((Integer) sessaoGeral.getValor("idOperador")));

			if (!operador.getLogin().equals(operadorSelecionado.getLogin())) {

				validarNomesRepetidos(operador);
			}

			operador.setId((Integer) sessaoGeral.getValor("idOperador"));
		}

		operador.setSenha(GeradorDeMd5.converter(operador.getSenha()));
		hibernateUtil.salvarOuAtualizar(operador);
		result.include("sucesso", "Operador salvo com sucesso");
		result.forwardTo(this).listarOperadors(new Operador(), null);
	}

	private void validarNomesRepetidos(Operador operador) {

		Operador operadorFiltro = new Operador();
		operadorFiltro.setLogin(operador.getLogin());

		if (Util.preenchido(hibernateUtil.buscar(operadorFiltro, MatchMode.EXACT))) {
			validator.add(new ValidationMessage("Já existe um operador com este nome", "Erro"));
		}
		validator.onErrorForwardTo(this).criarEditarOperador();
	}

	@Funcionalidade(nome = "Operadors", modulo = "Controle de acesso")
	public void listarOperadors(Operador operador, Integer pagina) {

		operador = (Operador) UtilController.preencherFiltros(operador, "operador", sessaoGeral);
		if (Util.vazio(operador)) {
			operador = new Operador();
		}

		List<Operador> operadors = hibernateUtil.buscar(operador, pagina);

		result.include("operadors", operadors);

	}
}
