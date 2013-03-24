package renan.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import renan.hibernate.Entidade;

@Entity
public class Armamento implements Entidade {
	
	//Isto È um coment·rio

	@Id
	@GeneratedValue
	private Integer id;

	private String numeracao;
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	private TipoArmamento tipoArmamento;
	
	public static final String ARMAMENTO_DISPONIVEL = "Dispon√≠vel";
	public static final String ARMAMENTO_INDISPONIVEL = "Indispon√≠vel";
	
	public static List<String> listarStatus(){
		
		List<String> status = new ArrayList<String>();
		
		status.add(ARMAMENTO_DISPONIVEL);
		status.add(ARMAMENTO_INDISPONIVEL);
		
		return status;
	}

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

	public TipoArmamento getTipoArmamento() {
		return tipoArmamento;
	}

	public void setTipoArmamento(TipoArmamento tipoArmamento) {
		this.tipoArmamento = tipoArmamento;
	}
}
