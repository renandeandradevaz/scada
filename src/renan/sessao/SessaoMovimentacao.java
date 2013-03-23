package renan.sessao;

import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoMovimentacao {

	private String nomeCliente;
	private List<String> armamentosSelecionados;

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public List<String> getArmamentosSelecionados() {
		return armamentosSelecionados;
	}

	public void setArmamentosSelecionados(List<String> armamentosSelecionados) {
		this.armamentosSelecionados = armamentosSelecionados;
	}

}
