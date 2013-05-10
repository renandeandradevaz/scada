package scada.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Operador extends Militar {

	private String login;
	private String senha;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private GrupoOperador grupoOperador;

	public Operador() {

	}

	public Operador(Integer id) {

		this.setId(id);
	}

	public static List<String> listarGraduacoes() {

		List<String> graduacoes = new ArrayList<String>();

		graduacoes.add("Coronel");
		graduacoes.add("Tenente-Coronel");
		graduacoes.add("Major");
		graduacoes.add("Capitão");
		graduacoes.add("1º Tenente");
		graduacoes.add("2º Tenente");
		graduacoes.add("Subtenente");
		graduacoes.add("1º Sargento");
		graduacoes.add("2º Sargento");
		graduacoes.add("3º Sargento");
		graduacoes.add("Cabo");
		graduacoes.add("Soldado");

		return graduacoes;

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
