package renan.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import renan.hibernate.Entidade;

@Entity
public class GrupoUsuario implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String nome;

	@OneToMany(mappedBy = "grupoUsuario")
	private List<Usuario> usuarios;

	@OneToMany(mappedBy = "grupoUsuario")
	private List<FuncionalidadeGrupoUsuario> funcionalidades;

	public GrupoUsuario() {

	}

	public GrupoUsuario(Integer id) {
		this.id = id;
	}

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

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<FuncionalidadeGrupoUsuario> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<FuncionalidadeGrupoUsuario> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

}
