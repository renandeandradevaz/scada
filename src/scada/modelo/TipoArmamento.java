package scada.modelo;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import scada.hibernate.Entidade;

@Entity
public class TipoArmamento implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String descricao;
	private BigDecimal calibre;
	
	@OneToMany(mappedBy = "tipoArmamento")
	private List<Armamento> armamentos;

	public TipoArmamento() {
		
	}

	public TipoArmamento(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getCalibre() {
		return calibre;
	}

	public void setCalibre(BigDecimal calibre) {
		this.calibre = calibre;
	}
	
	public List<Armamento> getArmamentos() {
		return armamentos;
	}
	
	public void setOperadores(List<Armamento> armamentos) {
		this.armamentos = armamentos;
	}

}
