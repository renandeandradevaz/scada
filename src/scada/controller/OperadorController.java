package scada.controller;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import scada.anotacoes.Funcionalidade;
import scada.hibernate.HibernateUtil;
import scada.modelo.GrupoOperador;
import scada.modelo.Operador;
import scada.sessao.SessaoGeral;
import scada.util.GeradorDeMd5;
import scada.util.Util;
import scada.util.UtilController;
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

			validator.onErrorForwardTo(this).listarOperadores(null, null);
		}

		sessaoGeral.adicionar("idOperador", operador.getId());
		result.include(operador);
		result.forwardTo(this).criarEditarOperador();
	}

	@Funcionalidade(nome = "Criar e editar operadores")
	public void criarEditarOperador() {

		List<GrupoOperador> gruposOperador = hibernateUtil.buscar(new GrupoOperador());
		result.include("gruposOperador", gruposOperador);

		listarGraduacoes();
	}

	private void listarGraduacoes() {
		List<String> graduacoes = Operador.listarGraduacoes();
		result.include("graduacoes", graduacoes);
	}

	@Path("/operador/excluirOperador/{operador.id}")
	@Funcionalidade(nome = "Excluir operador")
	public void excluirOperador(Operador operador) {

		Operador operadoreselecionado = hibernateUtil.selecionar(operador);

		if (operadoreselecionado.getLogin().equals("administrador")) {

			validator.add(new ValidationMessage("Não é possível excluir o operador administrador", "Erro"));

			validator.onErrorForwardTo(this).listarOperadores(null, null);
		}

		hibernateUtil.deletar(operadoreselecionado);
		result.include("sucesso", "Operador excluído com sucesso");
		result.forwardTo(this).listarOperadores(null, null);
	}

	@Funcionalidade(filhaDe = "criarEditarOperador")
	public void salvarOperador(Operador operador) {

		if (Util.vazio(sessaoGeral.getValor("idOperador"))) {

			validarNomesRepetidos(operador);
			validarIdentidadesRepetidas(operador);

			operador.setSenha(GeradorDeMd5.converter(operador.getSenha()));
		}

		else {

			Operador operadoreselecionado = hibernateUtil.selecionar(new Operador((Integer) sessaoGeral.getValor("idOperador")));

			if (!operador.getLogin().equals(operadoreselecionado.getLogin())) {

				validarNomesRepetidos(operador);
			}
			if (!operador.getIdentidade().equals(operadoreselecionado.getIdentidade())) {

				validarIdentidadesRepetidas(operador);
			}

			operador.setId((Integer) sessaoGeral.getValor("idOperador"));
			operador.setSenha(operadoreselecionado.getSenha());
		}

		hibernateUtil.salvarOuAtualizar(operador);
		result.include("sucesso", "Operador salvo com sucesso");
		result.redirectTo(this).listarOperadores(new Operador(), null);
	}

	private void validarNomesRepetidos(Operador operador) {

		Operador operadorFiltro = new Operador();
		operadorFiltro.setLogin(operador.getLogin());

		if (Util.preenchido(hibernateUtil.buscar(operadorFiltro, MatchMode.EXACT))) {
			validator.add(new ValidationMessage("Já existe um operador com este login", "Erro"));
		}
		validator.onErrorForwardTo(this).criarEditarOperador();
	}

	private void validarIdentidadesRepetidas(Operador operador) {

		Operador operadorFiltro = new Operador();
		operadorFiltro.setIdentidade(operador.getIdentidade());

		if (Util.preenchido(hibernateUtil.buscar(operadorFiltro, MatchMode.EXACT))) {
			validator.add(new ValidationMessage("Já existe um operador com esta identidade", "Erro"));
		}
		validator.onErrorForwardTo(this).criarEditarOperador();
	}

	@Funcionalidade(nome = "Operadores", modulo = "Controle de acesso")
	public void listarOperadores(Operador operador, Integer pagina) {

		operador = (Operador) UtilController.preencherFiltros(operador, "operador", sessaoGeral);
		if (Util.vazio(operador)) {
			operador = new Operador();
		}

		List<Operador> operadores = hibernateUtil.buscar(operador, pagina);

		result.include("operadores", operadores);

		listarGraduacoes();

	}

	@Path("/operador/trocarSenha/{operador.id}")
	@Funcionalidade(nome = "Trocar senha")
	public void trocarSenha(Operador operador) {

		operador = hibernateUtil.selecionar(operador);

		if (operador.getLogin().equals("administrador")) {

			validator.add(new ValidationMessage("Não é possível trocar a senha do administrador", "Erro"));

			validator.onErrorForwardTo(this).listarOperadores(null, null);
		}

		sessaoGeral.adicionar("idOperador", operador.getId());
	}

	@Funcionalidade(filhaDe = "trocarSenha")
	public void salvarTrocaSenha(String senha) {

		Operador operador = hibernateUtil.selecionar(new Operador((Integer) this.sessaoGeral.getValor("idOperador")));

		operador.setSenha(GeradorDeMd5.converter(senha));

		hibernateUtil.salvarOuAtualizar(operador);

		result.include("sucesso", "Senha trocada com sucesso");

		result.forwardTo(this).listarOperadores(null, null);
	}
}
