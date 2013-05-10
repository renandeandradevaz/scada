package scada.auxiliar;

public class RelatorioAcautelamentosAuxiliar {

	private Integer quantidadeAcautelamentos;
	private String armamento;

	public RelatorioAcautelamentosAuxiliar(Integer quantidadeAcautelamentos, String armamento) {

		this.quantidadeAcautelamentos = quantidadeAcautelamentos;
		this.armamento = armamento;
	}

	public Integer getQuantidadeAcautelamentos() {
		return quantidadeAcautelamentos;
	}

	public void setQuantidadeAcautelamentos(Integer quantidadeAcautelamentos) {
		this.quantidadeAcautelamentos = quantidadeAcautelamentos;
	}

	public String getArmamento() {
		return armamento;
	}

	public void setArmamento(String armamento) {
		this.armamento = armamento;
	}

}
