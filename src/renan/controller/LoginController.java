package renan.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.MatchMode;

import renan.anotacoes.Public;
import renan.hibernate.HibernateUtil;
import renan.modelo.FuncionalidadeGrupoUsuario;
import renan.modelo.GrupoUsuario;
import renan.modelo.Usuario;
import renan.sessao.SessaoFuncionalidades;
import renan.sessao.SessaoUsuario;
import renan.util.GeradorDeMd5;
import renan.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class LoginController {

	private static final String SENHA_ADMINISTRADOR = "1234";

	private final Result result;
	private SessaoUsuario sessaoUsuario;
	private SessaoFuncionalidades sessaoFuncionalidades;
	private Validator validator;
	private HibernateUtil hibernateUtil;

	public LoginController(Result result, SessaoUsuario sessaoUsuario, SessaoFuncionalidades sessaoFuncionalidades, Validator validator, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoUsuario = sessaoUsuario;
		this.validator = validator;
		this.sessaoFuncionalidades = sessaoFuncionalidades;
		this.hibernateUtil = hibernateUtil;
	}

	@Public
	@Path("/")
	public void telaLogin() {

	}

	@Public
	public void efetuarLogin(Usuario usuario) {

		verificaExistenciaAdministrador(usuario);
		tentarEfetuarLogin(usuario);
		colocarUsuarioNaSessao(usuario);
		colocarFuncionalidadesNaSessao(usuario);
		result.redirectTo(HomeController.class).home();
	}

	private void verificaExistenciaAdministrador(Usuario usuario) {

		if (usuario.getLogin().equals("administrador") && usuario.getSenha().equals(SENHA_ADMINISTRADOR)) {

			Usuario usuarioFiltro = new Usuario();
			usuarioFiltro.setLogin("administrador");

			Usuario usuarioBanco = hibernateUtil.selecionar(usuarioFiltro, MatchMode.EXACT);

			if (Util.vazio(usuarioBanco)) {

				GrupoUsuario grupoUsuario = new GrupoUsuario();
				grupoUsuario.setNome("Administradores");
				hibernateUtil.salvarOuAtualizar(grupoUsuario);

				usuario.setGrupoUsuario(grupoUsuario);
				usuario.setSenha(GeradorDeMd5.converter(SENHA_ADMINISTRADOR));
				hibernateUtil.salvarOuAtualizar(usuario);
			}
		}
	}

	private void tentarEfetuarLogin(Usuario usuario) {

		usuario.setSenha(GeradorDeMd5.converter(usuario.getSenha()));

		Usuario usuarioBanco = null;

		if (Util.preenchido(usuario.getLogin())) {

			Usuario usuarioFiltro = new Usuario();
			usuarioFiltro.setLogin(usuario.getLogin());

			usuarioBanco = hibernateUtil.selecionar(usuarioFiltro, MatchMode.EXACT);
		}

		if (Util.vazio(usuarioBanco) || !usuarioBanco.getSenha().equals(usuario.getSenha())) {

			validator.add(new ValidationMessage("Login ou senha incorretos", "Erro"));
		}

		validator.onErrorRedirectTo(this).telaLogin();
	}

	private void colocarUsuarioNaSessao(Usuario usuario) {

		this.sessaoUsuario.login(usuario);
	}

	private void colocarFuncionalidadesNaSessao(Usuario usuario) {

		usuario = hibernateUtil.selecionar(usuario);

		sessaoFuncionalidades.setCodigosFuncionalidades(null);
		sessaoFuncionalidades.setModulos(null);

		HashMap<String, FuncionalidadeGrupoUsuario> funcionalidadesHash = GrupoUsuarioController.obterHashFuncionalidades();

		if (usuario.getLogin().equals("administrador")) {

			Collection<FuncionalidadeGrupoUsuario> funcionalidadesLista = funcionalidadesHash.values();

			for (FuncionalidadeGrupoUsuario funcionalidade : funcionalidadesLista) {

				preencherSessaoFuncionalidades(funcionalidade);
			}
		}

		else {

			for (FuncionalidadeGrupoUsuario funcionalidade : usuario.getGrupoUsuario().getFuncionalidades()) {

				funcionalidade = funcionalidadesHash.get(funcionalidade.getCodigo());

				preencherSessaoFuncionalidades(funcionalidade);
			}
		}

		organizarFuncionalidadesAlfabeticamente();

	}

	private void organizarFuncionalidadesAlfabeticamente() {

		if (Util.preenchido(sessaoFuncionalidades.getModulos())) {

			for (Entry<String, List<FuncionalidadeGrupoUsuario>> entry : sessaoFuncionalidades.getModulos().entrySet()) {

				Collections.sort(entry.getValue(), new Comparator<FuncionalidadeGrupoUsuario>() {

					public int compare(FuncionalidadeGrupoUsuario fg1, FuncionalidadeGrupoUsuario fg2) {

						return fg1.getNomeFuncionalidade().toUpperCase().compareTo(fg2.getNomeFuncionalidade().toUpperCase());
					}

				});
			}
		}

	}

	private void preencherSessaoFuncionalidades(FuncionalidadeGrupoUsuario funcionalidade) {

		sessaoFuncionalidades.adicionarCodigoFuncionalidade(funcionalidade.getCodigo());
		sessaoFuncionalidades.adicionarFuncionalidadeAoModulo(funcionalidade.getModulo(), funcionalidade);
	}

	@Public
	public void permissaoNegada() {

	}

	@Public
	public void logout() {

		sessaoUsuario.logout();

		result.redirectTo(this).telaLogin();
	}
}
