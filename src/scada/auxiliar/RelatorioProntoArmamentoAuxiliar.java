package scada.auxiliar;

import java.math.BigDecimal;

import scada.modelo.TipoArmamento;

public class RelatorioProntoArmamentoAuxiliar {

	private TipoArmamento tipoArmamento;
	private Integer quantidadePrevista;
	private Integer quantidadeAcautelada;
	private Integer quantidadeExistente;
	private BigDecimal porcentagemAcautelada;

	public TipoArmamento getTipoArmamento() {
		return tipoArmamento;
	}

	public void setTipoArmamento(TipoArmamento tipoArmamento) {
		this.tipoArmamento = tipoArmamento;
	}

	public Integer getQuantidadePrevista() {
		return quantidadePrevista;
	}

	public void setQuantidadePrevista(Integer quantidadePrevista) {
		this.quantidadePrevista = quantidadePrevista;
	}

	public Integer getQuantidadeAcautelada() {
		return quantidadeAcautelada;
	}

	public void setQuantidadeAcautelada(Integer quantidadeAcautelada) {
		this.quantidadeAcautelada = quantidadeAcautelada;
	}

	public Integer getQuantidadeExistente() {
		return quantidadeExistente;
	}

	public void setQuantidadeExistente(Integer quantidadeExistente) {
		this.quantidadeExistente = quantidadeExistente;
	}

	public BigDecimal getPorcentagemAcautelada() {
		return porcentagemAcautelada;
	}

	public void setPorcentagemAcautelada(BigDecimal porcentagemAcautelada) {
		this.porcentagemAcautelada = porcentagemAcautelada;
	}

}
