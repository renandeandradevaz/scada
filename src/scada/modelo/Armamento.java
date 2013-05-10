package scada.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import scada.hibernate.Entidade;

@Entity
public class Armamento implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String numeracao;
	private String status;
	private String subUnidade;

	@ManyToOne(fetch = FetchType.LAZY)
	private TipoArmamento tipoArmamento;
	
	public static final String ARMAMENTO_DISPONIVEL_ACAUTELADO = "Disponível Acautelado";
	public static final String ARMAMENTO_DISPONIVEL_NÃO_ACAUTELADO = "Disponível Não Acautelado";
	public static final String ARMAMENTO_INDISPONIVEL_ACAUTELADO = "Indisponível Acautelado";
	public static final String ARMAMENTO_INDISPONIVEL_NÃO_ACAUTELADO = "Indisponível Não Acautelado";
	
	public static List<String> listarStatus(){
		
		List<String> status = new ArrayList<String>();
		
		status.add(ARMAMENTO_DISPONIVEL_NÃO_ACAUTELADO);
		status.add(ARMAMENTO_INDISPONIVEL_NÃO_ACAUTELADO);
		status.add(ARMAMENTO_DISPONIVEL_ACAUTELADO);
		status.add(ARMAMENTO_INDISPONIVEL_ACAUTELADO);
		
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

	public String getSubUnidade() {
		return subUnidade;
	}

	public void setSubUnidade(String subUnidade) {
		this.subUnidade = subUnidade;
	}
}
