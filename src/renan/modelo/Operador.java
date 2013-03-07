package renan.modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import renan.hibernate.Entidade;

@Entity
public class Operador extends Militar implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String login;
	private String senha;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private GrupoOperador grupoOperador;

	public Operador() {
	}

	public Operador(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public GrupoOperador getGrupoOperador() {
		return grupoOperador;
	}

	public void setGrupoOperador(GrupoOperador grupoOperador) {
		this.grupoOperador = grupoOperador;
	}

}
