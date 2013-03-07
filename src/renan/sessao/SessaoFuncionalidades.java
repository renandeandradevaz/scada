package renan.sessao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import renan.anotacoes.Funcionalidade;
import renan.modelo.FuncionalidadeGrupoUsuario;
import renan.util.Util;
import renan.util.UtilReflection;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoFuncionalidades {

	private List<String> codigosFuncionalidades;
	private String codigosFuncionalidadesSeparadosPorVirgula;
	private TreeMap<String, List<FuncionalidadeGrupoUsuario>> modulos;

	public void adicionarCodigoFuncionalidade(String codigo) {

		if (Util.vazio(codigosFuncionalidades)) {
			codigosFuncionalidades = new ArrayList<String>();
			codigosFuncionalidadesSeparadosPorVirgula = "";
		}

		codigosFuncionalidades.add(codigo);
		codigosFuncionalidadesSeparadosPorVirgula += codigo + ",";
		adicionarFuncionalidadesFilhas(codigo);
	}

	private void adicionarFuncionalidadesFilhas(String codigo) {

		String nomeClasse = codigo.split("/")[0];

		String nomeCompletoClasse = nomeClasse.substring(0, 1).toUpperCase() + nomeClasse.substring(1) + "Controller";

		String nomeMetodo = codigo.split("/")[1];

		Class<?> classe = null;
		try {
			classe = Class.forName(new UtilReflection().obterPacotePrincipal() + ".controller." + nomeCompletoClasse);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Method metodo : classe.getMethods()) {

			if (metodo.isAnnotationPresent(Funcionalidade.class)) {

				Funcionalidade anotacao = metodo.getAnnotation(Funcionalidade.class);

				if (Util.preenchido(anotacao.filhaDe())) {

					String nomeFuncionalidadePai = anotacao.filhaDe();
					String nomeFuncionalidadeFilha = metodo.getName();

					if (nomeFuncionalidadePai.equals(nomeMetodo)) {

						String nomeFuncionalidadeFilhaCompleto = nomeClasse + "/" + nomeFuncionalidadeFilha;

						if (!codigosFuncionalidadesSeparadosPorVirgula.contains(nomeFuncionalidadeFilhaCompleto)) {

							codigosFuncionalidadesSeparadosPorVirgula += nomeFuncionalidadeFilhaCompleto + ",";
						}
					}
				}
			}
		}
	}

	public void adicionarFuncionalidadeAoModulo(String modulo, FuncionalidadeGrupoUsuario funcionalidade) {

		if (Util.preenchido(modulo)) {

			if (Util.vazio(modulos)) {
				modulos = new TreeMap<String, List<FuncionalidadeGrupoUsuario>>();
			}

			String nomeFuncionalidade = funcionalidade.getNomeFuncionalidade().replace("Acessar ", "");
			nomeFuncionalidade = nomeFuncionalidade.substring(0, 1).toUpperCase() + nomeFuncionalidade.substring(1);
			funcionalidade.setNomeFuncionalidade(nomeFuncionalidade);

			if (!modulos.containsKey(modulo)) {

				List<FuncionalidadeGrupoUsuario> funcionalidades = new ArrayList<FuncionalidadeGrupoUsuario>();
				funcionalidades.add(funcionalidade);

				modulos.put(modulo, funcionalidades);
			}

			else {
				modulos.get(modulo).add(funcionalidade);
			}
		}

	}

	public List<String> getCodigosFuncionalidades() {
		return codigosFuncionalidades;
	}

	public void setCodigosFuncionalidades(List<String> codigosFuncionalidades) {
		this.codigosFuncionalidades = codigosFuncionalidades;
	}

	public String getCodigosFuncionalidadesSeparadosPorVirgula() {
		return codigosFuncionalidadesSeparadosPorVirgula;
	}

	public void setCodigosFuncionalidadesSeparadosPorVirgula(String codigosFuncionalidadesSeparadosPorVirgula) {
		this.codigosFuncionalidadesSeparadosPorVirgula = codigosFuncionalidadesSeparadosPorVirgula;
	}

	public TreeMap<String, List<FuncionalidadeGrupoUsuario>> getModulos() {
		return modulos;
	}

	public void setModulos(TreeMap<String, List<FuncionalidadeGrupoUsuario>> modulos) {
		this.modulos = modulos;
	}

}
