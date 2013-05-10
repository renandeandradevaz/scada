package scada.modelo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import scada.hibernate.Entidade;

@MappedSuperclass
public abstract class Militar implements Entidade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;
	private String postoGraduacao;
	private Integer identidade;
	private String subUnidade;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPostoGraduacao() {
		return postoGraduacao;
	}

	public void setPostoGraduacao(String postoGraduacao) {
		this.postoGraduacao = postoGraduacao;
	}

	public Integer getIdentidade() {
		return identidade;
	}

	public void setIdentidade(Integer identidade) {
		this.identidade = identidade;
	}

	public String getSubUnidade() {
		return subUnidade;
	}

	public void setSubUnidade(String subUnidade) {
		this.subUnidade = subUnidade;
	}

}
