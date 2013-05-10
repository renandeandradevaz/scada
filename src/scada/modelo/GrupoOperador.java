package scada.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import scada.hibernate.Entidade;

@Entity
public class GrupoOperador implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String nome;

	@OneToMany(mappedBy = "grupoOperador")
	private List<Operador> operadores;

	@OneToMany(mappedBy = "grupoOperador")
	private List<FuncionalidadeGrupoOperador> funcionalidades;

	public GrupoOperador() {

	}

	public GrupoOperador(Integer id) {
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

	public List<Operador> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<Operador> operadores) {
		this.operadores = operadores;
	}

	public List<FuncionalidadeGrupoOperador> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<FuncionalidadeGrupoOperador> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

}
