package renan.controller;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.GrupoUsuario;
import renan.modelo.Usuario;
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
public class UsuarioController {

	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public UsuarioController(Result result, Validator validator, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {

		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);

	}

	@Funcionalidade(filhaDe = "criarEditarUsuario")
	public void criarUsuario() {

		sessaoGeral.adicionar("idUsuario", null);
		result.forwardTo(this).criarEditarUsuario();
	}

	@Path("/usuario/editarUsuario/{usuario.id}")
	@Funcionalidade(filhaDe = "criarEditarUsuario")
	public void editarUsuario(Usuario usuario) {

		usuario = hibernateUtil.selecionar(usuario);

		if (usuario.getLogin().equals("administrador")) {

			validator.add(new ValidationMessage("Não é possível editar o usuário administrador", "Erro"));

			validator.onErrorForwardTo(this).listarUsuarios(null, null);
		}

		sessaoGeral.adicionar("idUsuario", usuario.getId());
		result.include(usuario);
		result.forwardTo(this).criarEditarUsuario();
	}

	@Funcionalidade(nome = "Criar e editar usuários")
	public void criarEditarUsuario() {

		List<GrupoUsuario> gruposUsuario = hibernateUtil.buscar(new GrupoUsuario());
		result.include("gruposUsuario", gruposUsuario);
	}

	@Path("/usuario/excluirUsuario/{usuario.id}")
	@Funcionalidade(nome = "Excluir usuário")
	public void excluirUsuario(Usuario usuario) {

		Usuario usuarioSelecionado = hibernateUtil.selecionar(usuario);

		if (usuarioSelecionado.getLogin().equals("administrador")) {

			validator.add(new ValidationMessage("Não é possível excluir o usuário administrador", "Erro"));

			validator.onErrorForwardTo(this).listarUsuarios(null, null);
		}

		hibernateUtil.deletar(usuarioSelecionado);
		result.include("sucesso", "Usuário excluído com sucesso");
		result.forwardTo(this).listarUsuarios(null, null);
	}

	@Funcionalidade(filhaDe = "criarEditarUsuario")
	public void salvarUsuario(Usuario usuario) {

		if (Util.vazio(sessaoGeral.getValor("idUsuario"))) {

			validarNomesRepetidos(usuario);
		}

		else {

			Usuario usuarioSelecionado = hibernateUtil.selecionar(new Usuario((Integer) sessaoGeral.getValor("idUsuario")));

			if (!usuario.getLogin().equals(usuarioSelecionado.getLogin())) {

				validarNomesRepetidos(usuario);
			}

			usuario.setId((Integer) sessaoGeral.getValor("idUsuario"));
		}

		usuario.setSenha(GeradorDeMd5.converter(usuario.getSenha()));
		hibernateUtil.salvarOuAtualizar(usuario);
		result.include("sucesso", "Usuário salvo com sucesso");
		result.forwardTo(this).listarUsuarios(new Usuario(), null);
	}

	private void validarNomesRepetidos(Usuario usuario) {

		Usuario usuarioFiltro = new Usuario();
		usuarioFiltro.setLogin(usuario.getLogin());

		if (Util.preenchido(hibernateUtil.buscar(usuarioFiltro, MatchMode.EXACT))) {
			validator.add(new ValidationMessage("Já existe um usuário com este nome", "Erro"));
		}
		validator.onErrorForwardTo(this).criarEditarUsuario();
	}

	@Funcionalidade(nome = "Usuários", modulo = "Controle de acesso")
	public void listarUsuarios(Usuario usuario, Integer pagina) {

		usuario = (Usuario) UtilController.preencherFiltros(usuario, "usuario", sessaoGeral);
		if (Util.vazio(usuario)) {
			usuario = new Usuario();
		}

		List<Usuario> usuarios = hibernateUtil.buscar(usuario, pagina);

		result.include("usuarios", usuarios);

	}
}
