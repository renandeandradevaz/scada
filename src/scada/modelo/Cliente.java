package scada.modelo;

import javax.persistence.Entity;

@Entity
public class Cliente extends Militar {

	public Cliente() {

	}

	public Cliente(Integer id) {

		this.setId(id);
	}

}
