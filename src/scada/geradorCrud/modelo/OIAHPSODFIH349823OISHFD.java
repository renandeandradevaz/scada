package scada.geradorCrud.modelo;
// Imports aqui
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import scada.hibernate.Entidade;

@Entity
public class OIAHPSODFIH349823OISHFD implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

// Atributos aqui
	public OIAHPSODFIH349823OISHFD() {
	}

	public OIAHPSODFIH349823OISHFD(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
// Getters and setters aqui
}
