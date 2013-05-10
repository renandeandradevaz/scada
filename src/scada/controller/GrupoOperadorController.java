package scada.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import scada.anotacoes.Funcionalidade;
import scada.hibernate.HibernateUtil;
import scada.modelo.FuncionalidadeGrupoOperador;
import scada.modelo.GrupoOperador;
import scada.sessao.SessaoGeral;
import scada.util.Util;
import scada.util.UtilController;
import scada.util.UtilReflection;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class GrupoOperadorController {

	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public GrupoOperadorController(Result result, Validator validator, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {
		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(filhaDe = "criarEditarGrupoOperador")
	public void criarGrupoOperador() {

		sessaoGeral.adicionar("idGrupoOperador", null);
		result.forwardTo(this).criarEditarGrupoOperador();
	}

	@Path("/grupoOperador/editarGrupoOperador/{grupoOperador.id}")
	@Funcionalidade(filhaDe = "criarEditarGrupoOperador")
	public void editarGrupoOperador(GrupoOperador grupoOperador) {

		grupoOperador = hibernateUtil.selecionar(grupoOperador);

		sessaoGeral.adicionar("idGrupoOperador", grupoOperador.getId());
		result.include(grupoOperador);
		result.forwardTo(this).criarEditarGrupoOperador();
	}

	@Funcionalidade(nome = "Criar e editar grupo de operadores")
	public void criarEditarGrupoOperador() {

	}

	@Path("/grupoOperador/excluirGrupoOperador/{grupoOperador.id}")
	@Funcionalidade(nome = "Excluir grupo de operadores")
	public void excluirGrupoOperador(GrupoOperador grupoOperador) {

		GrupoOperador grupoOperadoreselecionado = hibernateUtil.selecionar(grupoOperador);

		if (grupoOperadoreselecionado.getOperadores().size() > 0) {

			validator.add(new ValidationMessage("Existem operadores vinculados a este grupo de operador. Se quiser remover este grupo de operador, por favor, exclua os operadores deste grupo ou vincule-os a outro grupo de operador", "Erro"));

			validator.onErrorForwardTo(this).listarGruposOperador(null, null);
		}

		hibernateUtil.deletar(grupoOperador);
		result.include("sucesso", "Grupo de operadores excluído com sucesso");
		result.forwardTo(this).listarGruposOperador(null, null);
	}

	@Funcionalidade(nome = "Grupos de operadores", modulo = "Controle de acesso")
	public void listarGruposOperador(GrupoOperador grupoOperador, Integer pagina) {

		grupoOperador = (GrupoOperador) UtilController.preencherFiltros(grupoOperador, "grupoOperador", sessaoGeral);
		if (Util.vazio(grupoOperador)) {
			grupoOperador = new GrupoOperador();
		}

		List<GrupoOperador> gruposOperador = hibernateUtil.buscar(grupoOperador, pagina);
		result.include("gruposOperador", gruposOperador);
	}

	@Funcionalidade(filhaDe = "criarEditarGrupoOperador")
	public void salvarGrupoOperador(GrupoOperador grupoOperador) {

		if (Util.vazio(sessaoGeral.getValor("idGrupoOperador"))) {

			validarNomesRepetidos(grupoOperador);
		}

		else {

			GrupoOperador grupoOperadoreselecionado = hibernateUtil.selecionar(new GrupoOperador((Integer) sessaoGeral.getValor("idGrupoOperador")));

			if (!grupoOperador.getNome().equals(grupoOperadoreselecionado.getNome())) {

				validarNomesRepetidos(grupoOperador);
			}

			grupoOperador.setId((Integer) sessaoGeral.getValor("idGrupoOperador"));
		}

		hibernateUtil.salvarOuAtualizar(grupoOperador);
		result.include("sucesso", "Grupo de operador salvo com sucesso");
		result.redirectTo(this).listarGruposOperador(new GrupoOperador(), null);
	}

	private void validarNomesRepetidos(GrupoOperador grupoOperador) {

		GrupoOperador grupoOperadorFiltro = new GrupoOperador();
		grupoOperadorFiltro.setNome(grupoOperador.getNome());

		if (Util.preenchido(hibernateUtil.buscar(grupoOperadorFiltro, MatchMode.EXACT))) {
			validator.add(new ValidationMessage("Já existe um grupo de operador com este nome", "Erro"));
		}
		validator.onErrorForwardTo(this).criarEditarGrupoOperador();
	}

	@Path("/grupoOperador/vincularFuncionalidades/{grupoOperador.id}")
	@Funcionalidade(nome = "Vincular funcionalidades ao grupo de operadores")
	public void vincularFuncionalidades(GrupoOperador grupoOperador) {

		grupoOperador = hibernateUtil.selecionar(grupoOperador);
		sessaoGeral.adicionar("grupoOperador", grupoOperador);

		List<String> funcionalidadesDoGrupoSelecionado = obterCodigosFuncionalidades(grupoOperador.getFuncionalidades());
		result.include("funcionalidadesDoGrupoSelecionado", funcionalidadesDoGrupoSelecionado);

		result.include("funcionalidades", obterFuncionalidades());
	}

	private List<String> obterCodigosFuncionalidades(List<FuncionalidadeGrupoOperador> funcionalidades) {

		List<String> codigosFuncionalidades = new ArrayList<String>();

		for (FuncionalidadeGrupoOperador funcionalidade : funcionalidades) {

			codigosFuncionalidades.add(funcionalidade.getCodigo());
		}

		return codigosFuncionalidades;
	}

	@Funcionalidade(filhaDe = "vincularFuncionalidades")
	public void salvarVinculosFuncionalidades(List<String> funcionalidadesSelecionadas) {

		GrupoOperador grupoOperador = (GrupoOperador) sessaoGeral.getValor("grupoOperador");

		deletarTodasFuncionalidadesDoGrupoDeOperador(grupoOperador);
		adicionarFuncionalidadesGrupoOperador(funcionalidadesSelecionadas, grupoOperador);

		result.include("sucesso", "Funcionalidades vinculadas ao grupo de operador");
		result.forwardTo(this).listarGruposOperador(new GrupoOperador(), null);
	}

	private void deletarTodasFuncionalidadesDoGrupoDeOperador(GrupoOperador grupoOperador) {

		List<FuncionalidadeGrupoOperador> funcionalidadesQueSeraoRemovidas = new ArrayList<FuncionalidadeGrupoOperador>();

		for (FuncionalidadeGrupoOperador funcionalidade : grupoOperador.getFuncionalidades()) {

			funcionalidadesQueSeraoRemovidas.add(funcionalidade);
		}

		hibernateUtil.deletar(funcionalidadesQueSeraoRemovidas);
	}

	private void adicionarFuncionalidadesGrupoOperador(List<String> funcionalidadesSelecionadas, GrupoOperador grupoOperador) {

		if (Util.preenchido(funcionalidadesSelecionadas)) {

			List<FuncionalidadeGrupoOperador> funcionalidadesQueSeraoAdicionadas = new ArrayList<FuncionalidadeGrupoOperador>();

			for (String funcionalidadeSelecionada : funcionalidadesSelecionadas) {

				FuncionalidadeGrupoOperador funcionalidade = new FuncionalidadeGrupoOperador();
				funcionalidade.setCodigo(funcionalidadeSelecionada);
				funcionalidade.setGrupoOperador(grupoOperador);

				funcionalidadesQueSeraoAdicionadas.add(funcionalidade);
			}

			hibernateUtil.salvarOuAtualizar(funcionalidadesQueSeraoAdicionadas);
		}
	}

	public static HashMap<String, FuncionalidadeGrupoOperador> obterHashFuncionalidades() {

		HashMap<String, FuncionalidadeGrupoOperador> funcionalidadesHash = new HashMap<String, FuncionalidadeGrupoOperador>();

		List<FuncionalidadeGrupoOperador> funcionalidadesLista = obterFuncionalidades();

		for (FuncionalidadeGrupoOperador funcionalidade : funcionalidadesLista) {

			funcionalidadesHash.put(funcionalidade.getCodigo(), funcionalidade);
		}

		return funcionalidadesHash;
	}

	@SuppressWarnings("rawtypes")
	public static List<FuncionalidadeGrupoOperador> obterFuncionalidades() {

		List<FuncionalidadeGrupoOperador> funcionalidadesGrupoOperador = new ArrayList<FuncionalidadeGrupoOperador>();

		try {
			Iterable<Class> classes = new UtilReflection().obterClassesDoPacote(new UtilReflection().obterPacotePrincipal() + ".controller");

			for (Class classe : classes) {

				List<FuncionalidadeGrupoOperador> funcionalidadesDaClasse = new ArrayList<FuncionalidadeGrupoOperador>();

				for (Method metodo : classe.getMethods()) {

					if (metodo.isAnnotationPresent(Funcionalidade.class)) {

						Funcionalidade anotacao = metodo.getAnnotation(Funcionalidade.class);

						if (Util.preenchido(anotacao.nome())) {

							String nomeClasse = classe.getSimpleName();
							String nomeMetodo = metodo.getName();

							String codigo = montarCodigoFuncionalidade(nomeClasse, nomeMetodo);

							FuncionalidadeGrupoOperador funcionalidadeGrupoOperador = new FuncionalidadeGrupoOperador();
							funcionalidadeGrupoOperador.setCodigo(codigo);

							if (Util.preenchido(anotacao.modulo())) {

								String nomeFuncionalidade = anotacao.nome().substring(0, 1).toLowerCase() + anotacao.nome().substring(1);
								nomeFuncionalidade = "Acessar " + nomeFuncionalidade;

								funcionalidadeGrupoOperador.setNomeFuncionalidade(nomeFuncionalidade);
								funcionalidadeGrupoOperador.setModulo(anotacao.modulo());
							} else {
								funcionalidadeGrupoOperador.setNomeFuncionalidade(anotacao.nome());
							}

							funcionalidadesDaClasse.add(funcionalidadeGrupoOperador);
						}
					}
				}

				funcionalidadesGrupoOperador.addAll(organizarFuncionalidades(funcionalidadesDaClasse));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return funcionalidadesGrupoOperador;
	}

	public static String montarCodigoFuncionalidade(String nomeClasse, String nomeMetodo) {

		String nomeClasseSemController = nomeClasse.replace("Controller", "");
		nomeClasseSemController = nomeClasseSemController.substring(0, 1).toLowerCase() + nomeClasseSemController.substring(1);

		String codigo = nomeClasseSemController + "/" + nomeMetodo;

		return codigo;
	}

	private static List<FuncionalidadeGrupoOperador> organizarFuncionalidades(List<FuncionalidadeGrupoOperador> funcionalidadesDaClasse) {

		Collections.sort(funcionalidadesDaClasse, new Comparator<FuncionalidadeGrupoOperador>() {

			public int compare(FuncionalidadeGrupoOperador fg1, FuncionalidadeGrupoOperador fg2) {

				return fg1.getNomeFuncionalidade().toUpperCase().compareTo(fg2.getNomeFuncionalidade().toUpperCase());
			}
		});

		List<FuncionalidadeGrupoOperador> funcionalidadesOrganizadas = new ArrayList<FuncionalidadeGrupoOperador>();

		for (FuncionalidadeGrupoOperador funcionalidade : funcionalidadesDaClasse) {

			if (Util.preenchido(funcionalidade.getModulo())) {

				funcionalidadesOrganizadas.add(funcionalidade);
			}
		}

		for (FuncionalidadeGrupoOperador funcionalidade : funcionalidadesDaClasse) {

			if (!funcionalidadesOrganizadas.contains(funcionalidade)) {

				funcionalidadesOrganizadas.add(funcionalidade);
			}
		}

		return funcionalidadesOrganizadas;
	}
}
