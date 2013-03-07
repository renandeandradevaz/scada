package renan.modelo;

import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import renan.hibernate.Entidade;

@Entity
public class MovimentacaoDeArmamento implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String tipoMovimentacao;
	private GregorianCalendar dataHora;
	private String destino;
	private String observacoes;
	private Boolean validado;

	@ManyToOne(fetch = FetchType.LAZY)
	private Operador operador;

	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY)
	private Armamento armamento;

	public MovimentacaoDeArmamento() {
	}

	public MovimentacaoDeArmamento(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(String tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public GregorianCalendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(GregorianCalendar dataHora) {
		this.dataHora = dataHora;
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

	public Boolean getValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Armamento getArmamento() {
		return armamento;
	}

	public void setArmamento(Armamento armamento) {
		this.armamento = armamento;
	}

}
