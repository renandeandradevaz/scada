package renan.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import renan.hibernate.Entidade;

@Entity
public class Cliente extends Militar implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	public Cliente() {
	}

	public Cliente(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
