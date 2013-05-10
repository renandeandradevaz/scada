package scada.sessao;

import scada.modelo.Operador;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoOperador {

	private Operador operador;

	public Operador getOperador() {
		return operador;
	}

	public void login(Operador operador) {
		this.operador = operador;
	}

	public void logout() {
		this.operador = null;
	}

}
