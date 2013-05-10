package scada.modelo;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import scada.hibernate.Entidade;
import scada.hibernate.HibernateUtil;
import scada.util.Util;
import br.com.caelum.vraptor.Resource;

@Resource
@Entity
public class Configuracao implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String chave;
	private String valor;

	public static HashMap<String, String> configuracoesPadroes() {

		HashMap<String, String> configuracoesPadroes = new HashMap<String, String>();

		configuracoesPadroes.put("quantidadeRegistrosPorPagina", "10");

		return configuracoesPadroes;
	}

	public String retornarConfiguracao(String chave, HibernateUtil hibernateUtil) {

		Configuracao configuracao = new Configuracao();
		configuracao.setChave(chave);

		configuracao = hibernateUtil.selecionar(configuracao);

		if (Util.vazio(configuracao) || Util.vazio(configuracao.getValor())) {

			return configuracoesPadroes().get(chave);
		}

		return configuracao.getValor();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
