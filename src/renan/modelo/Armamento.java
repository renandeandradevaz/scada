package renan.modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import renan.hibernate.Entidade;

@Entity
public class Armamento implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String numeracao;
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	private TipoArmamento tipoArmamento;

	public Armamento() {
	}

	public Armamento(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeracao() {
		return numeracao;
	}

	public void setNumeracao(String numeracao) {
		this.numeracao = numeracao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
