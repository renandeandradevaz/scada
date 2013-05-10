package scada.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;

import scada.anotacoes.Funcionalidade;
import scada.anotacoes.Public;
import scada.controller.GrupoOperadorController;
import scada.controller.LoginController;
import scada.sessao.SessaoFuncionalidades;
import scada.sessao.SessaoOperador;
import scada.util.Util;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Intercepts
public class InterceptadorDeAutorizacao implements Interceptor {

	private final SessaoOperador sessaoOperador;
	private SessaoFuncionalidades sessaoFuncionalidades;
	private Result result;

	public InterceptadorDeAutorizacao(SessaoOperador sessaoOperador, Result result, SessaoFuncionalidades sessaoFuncionalidades) {
		this.sessaoOperador = sessaoOperador;
		this.result = result;
		this.sessaoFuncionalidades = sessaoFuncionalidades;
	}

	public boolean accepts(ResourceMethod method) {

		boolean contemAnotacaoPublic = !method.containsAnnotation(Public.class);

		if (Util.preenchido(sessaoOperador.getOperador())) {

			if (sessaoOperador.getOperador().getLogin().equals("administrador")) {

				return false;
			}
		}

		return contemAnotacaoPublic;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {

		if (Util.vazio(sessaoOperador.getOperador())) {

			result.include("errors", Arrays.asList(new ValidationMessage("O operador não está logado no sistema", "Erro")));
			result.redirectTo(LoginController.class).telaLogin();
		}
		if (Util.preenchido(sessaoOperador.getOperador())) {

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

		String codigo = GrupoOperadorController.montarCodigoFuncionalidade(nomeClasse, nomeMetodo);

		if (!sessaoFuncionalidades.getCodigosFuncionalidades().contains(codigo)) {

			return false;
		}
		return true;
	}

}
