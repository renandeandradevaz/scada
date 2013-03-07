package renan.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;

import renan.anotacoes.Funcionalidade;
import renan.anotacoes.Public;
import renan.controller.GrupoUsuarioController;
import renan.controller.LoginController;
import renan.sessao.SessaoFuncionalidades;
import renan.sessao.SessaoUsuario;
import renan.util.Util;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Intercepts
public class InterceptadorDeAutorizacao implements Interceptor {

	private final SessaoUsuario sessaoUsuario;
	private SessaoFuncionalidades sessaoFuncionalidades;
	private Result result;

	public InterceptadorDeAutorizacao(SessaoUsuario sessaoUsuario, Result result, SessaoFuncionalidades sessaoFuncionalidades) {
		this.sessaoUsuario = sessaoUsuario;
		this.result = result;
		this.sessaoFuncionalidades = sessaoFuncionalidades;
	}

	public boolean accepts(ResourceMethod method) {

		boolean contemAnotacaoPublic = !method.containsAnnotation(Public.class);

		if (Util.preenchido(sessaoUsuario.getUsuario())) {

			if (sessaoUsuario.getUsuario().getLogin().equals("administrador")) {

				return false;
			}
		}

		return contemAnotacaoPublic;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {

		if (Util.vazio(sessaoUsuario.getUsuario())) {

			result.include("errors", Arrays.asList(new ValidationMessage("O usuário não está logado no sistema", "Erro")));
			result.redirectTo(LoginController.class).telaLogin();
		}
		if (Util.preenchido(sessaoUsuario.getUsuario())) {

			Boolean possuiPermissao = null;

			Method metodo = method.getMethod();

			if (metodo.isAnnotationPresent(Funcionalidade.class)) {

				Funcionalidade anotacao = metodo.getAnnotation(Funcionalidade.class);

				if (Util.preenchido(anotacao.nome())) {

					String nomeMetodo = metodo.getName();
					possuiPermissao = verificarPermissao(metodo, nomeMetodo);
				}

				if (Util.preenchido(anotacao.filhaDe())) {

					String nomeMetodo = anotacao.filhaDe();
					possuiPermissao = verificarPermissao(metodo, nomeMetodo);
				}

				if (!possuiPermissao) {

					result.redirectTo(LoginController.class).permissaoNegada();
					return;
				}

				stack.next(method, resourceInstance);
			}
		}

	}

	private Boolean verificarPermissao(Method metodo, String nomeMetodo) {

		String nomeClasse = metodo.getDeclaringClass().getSimpleName();

		String codigo = GrupoUsuarioController.montarCodigoFuncionalidade(nomeClasse, nomeMetodo);

		if (!sessaoFuncionalidades.getCodigosFuncionalidades().contains(codigo)) {

			return false;
		}
		return true;
	}

}
