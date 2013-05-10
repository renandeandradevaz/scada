package scada.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.MatchMode;

import scada.anotacoes.Public;
import scada.hibernate.HibernateUtil;
import scada.modelo.FuncionalidadeGrupoOperador;
import scada.modelo.GrupoOperador;
import scada.modelo.Operador;
import scada.sessao.SessaoFuncionalidades;
import scada.sessao.SessaoOperador;
import scada.util.GeradorDeMd5;
import scada.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class LoginController {

	private static final String HASH_SENHA_ADMINISTRADOR = "b9bde749700bd25648b0a3f2ecaa81c2";

	private final Result result;
	private SessaoOperador sessaoOperador;
	private SessaoFuncionalidades sessaoFuncionalidades;
	private Validator validator;
	private HibernateUtil hibernateUtil;

	public LoginController(Result result, SessaoOperador sessaoOperador, SessaoFuncionalidades sessaoFuncionalidades, Validator validator, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoOperador = sessaoOperador;
		this.validator = validator;
		this.sessaoFuncionalidades = sessaoFuncionalidades;
		this.hibernateUtil = hibernateUtil;
	}

	@Public
	@Path("/")
	public void telaLogin() {

	}

	@Public
	public void efetuarLogin(Operador operador) {

		verificaExistenciaAdministrador(operador);
		tentarEfetuarLogin(operador);
		colocarOperadorNaSessao(operador);
		colocarFuncionalidadesNaSessao(operador);
		result.redirectTo(HomeController.class).home();
	}

	private void verificaExistenciaAdministrador(Operador operador) {

		if (operador.getLogin().equals("administrador") && GeradorDeMd5.converter(operador.getSenha()).equals(HASH_SENHA_ADMINISTRADOR)) {

			Operador operadorFiltro = new Operador();
			operadorFiltro.setLogin("administrador");

			Operador operadorBanco = hibernateUtil.selecionar(operadorFiltro, MatchMode.EXACT);

			if (Util.vazio(operadorBanco)) {

				GrupoOperador grupoOperador = new GrupoOperador();
				grupoOperador.setNome("Administradores");
				hibernateUtil.salvarOuAtualizar(grupoOperador);

				operador.setGrupoOperador(grupoOperador);
				operador.setSenha(HASH_SENHA_ADMINISTRADOR);
				hibernateUtil.salvarOuAtualizar(operador);
			}
		}
	}

	private void tentarEfetuarLogin(Operador operador) {

		operador.setSenha(GeradorDeMd5.converter(operador.getSenha()));

		Operador operadorBanco = null;

		if (Util.preenchido(operador.getLogin())) {

			Operador operadorFiltro = new Operador();
			operadorFiltro.setLogin(operador.getLogin());

			operadorBanco = hibernateUtil.selecionar(operadorFiltro, MatchMode.EXACT);
		}

		if (Util.vazio(operadorBanco) || !operadorBanco.getSenha().equals(operador.getSenha())) {

			validator.add(new ValidationMessage("Login ou senha incorretos", "Erro"));
		}

		validator.onErrorRedirectTo(this).telaLogin();
	}

	private void colocarOperadorNaSessao(Operador operador) {

		this.sessaoOperador.login((Operador) this.hibernateUtil.selecionar(operador, MatchMode.EXACT));
	}

	private void colocarFuncionalidadesNaSessao(Operador operador) {

		operador = hibernateUtil.selecionar(operador);

		sessaoFuncionalidades.setCodigosFuncionalidades(null);
		sessaoFuncionalidades.setModulos(null);

		HashMap<String, FuncionalidadeGrupoOperador> funcionalidadesHash = GrupoOperadorController.obterHashFuncionalidades();

		if (operador.getLogin().equals("administrador")) {

			Collection<FuncionalidadeGrupoOperador> funcionalidadesLista = funcionalidadesHash.values();

			for (FuncionalidadeGrupoOperador funcionalidade : funcionalidadesLista) {

				preencherSessaoFuncionalidades(funcionalidade);
			}
		}

		else {

			for (FuncionalidadeGrupoOperador funcionalidade : operador.getGrupoOperador().getFuncionalidades()) {

				funcionalidade = funcionalidadesHash.get(funcionalidade.getCodigo());

				preencherSessaoFuncionalidades(funcionalidade);
			}
		}

		organizarFuncionalidadesAlfabeticamente();

	}

	private void organizarFuncionalidadesAlfabeticamente() {

		if (Util.preenchido(sessaoFuncionalidades.getModulos())) {

			for (Entry<String, List<FuncionalidadeGrupoOperador>> entry : sessaoFuncionalidades.getModulos().entrySet()) {

				Collections.sort(entry.getValue(), new Comparator<FuncionalidadeGrupoOperador>() {

					public int compare(FuncionalidadeGrupoOperador fg1, FuncionalidadeGrupoOperador fg2) {

						return fg1.getNomeFuncionalidade().toUpperCase().compareTo(fg2.getNomeFuncionalidade().toUpperCase());
					}

				});
			}
		}

	}

	private void preencherSessaoFuncionalidades(FuncionalidadeGrupoOperador funcionalidade) {

		sessaoFuncionalidades.adicionarCodigoFuncionalidade(funcionalidade.getCodigo());
		sessaoFuncionalidades.adicionarFuncionalidadeAoModulo(funcionalidade.getModulo(), funcionalidade);
	}

	@Public
	public void permissaoNegada() {

	}

	@Public
	public void logout() {

		sessaoOperador.logout();

		result.redirectTo(this).telaLogin();
	}

	@Public
	public void trocarPropriaSenha() {

		verificarUsuarioLogado();
	}

	@Public
	public void salvarTrocarPropriaSenha(String senhaAntiga, String senhaNova) {

		if (!verificarUsuarioLogado()) {

			return;
		}

		Operador operador = hibernateUtil.selecionar(new Operador(sessaoOperador.getOperador().getId()));

		if (!GeradorDeMd5.converter(senhaAntiga).equals(operador.getSenha())) {

			validator.add(new ValidationMessage("Senha antiga incorreta", "Erro"));

			validator.onErrorRedirectTo(this).trocarPropriaSenha();
		}

		operador.setSenha(GeradorDeMd5.converter(senhaNova));

		hibernateUtil.salvarOuAtualizar(operador);

		result.include("sucesso", "Senha trocada com sucesso");

		result.redirectTo(HomeController.class).home();
	}

	private boolean verificarUsuarioLogado() {

		if (sessaoOperador.getOperador() == null) {

			result.redirectTo(this).telaLogin();
			return false;
		}

		return true;
	}
}
