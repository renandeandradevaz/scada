package scada.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import scada.hibernate.HibernateUtil;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;

@RequestScoped
@Intercepts(after = InterceptadorDeAutorizacao.class)
public class HibernateInterceptor implements Interceptor {

	private Result result;
	private HibernateUtil hibernateUtil;

	public HibernateInterceptor(Result result, HibernateUtil hibernateUtil) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method, Object instance) throws InterceptionException {

		try {

			stack.next(method, instance);

			hibernateUtil.fecharSessao();
		}

		catch (RuntimeException e) {

			hibernateUtil.fecharSessao();
			
			StringWriter writerStack = new StringWriter();
			PrintWriter printWriterStack = new PrintWriter(writerStack);
			e.printStackTrace(printWriterStack);

			result.include("exception", writerStack.toString());

			throw e;
		}

	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}