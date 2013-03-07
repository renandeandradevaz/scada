package renan.modelo;

import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import renan.hibernate.Entidade;

@Entity
public class TipoArmamento implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

    private String descricao;
    private GregorianCalendar calibre;

	public TipoArmamento() {
	}

	public TipoArmamento(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public GregorianCalendar getCalibre() {
        return calibre;
    }

    public void setCalibre(GregorianCalendar calibre){
        this.calibre = calibre;
    }
}
