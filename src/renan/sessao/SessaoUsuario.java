package renan.sessao;

import renan.modelo.Usuario;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoUsuario {

	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void login(Usuario usuario) {
		this.usuario = usuario;
	}

	public void logout() {
		this.usuario = null;
	}

}
