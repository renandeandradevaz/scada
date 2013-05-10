package scada.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import scada.hibernate.Entidade;

@Entity
public class FuncionalidadeGrupoOperador implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String codigo;

	@ManyToOne
	private GrupoOperador grupoOperador;

	@Transient
	private String nomeFuncionalidade;

	@Transient
	private String modulo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public GrupoOperador getGrupoOperador() {
		return grupoOperador;
	}

	public void setGrupoOperador(GrupoOperador grupoOperador) {
		this.grupoOperador = grupoOperador;
	}

	public String getNomeFuncionalidade() {
		return nomeFuncionalidade;
	}

	public void setNomeFuncionalidade(String nomeFuncionalidade) {
		this.nomeFuncionalidade = nomeFuncionalidade;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

}
