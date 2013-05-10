package scada.sessao;

import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoMovimentacao {

	private String nomeCliente;
	private String destino;
	private String observacoes;
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

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

}
