package renan.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.FuncionalidadeGrupoUsuario;
import renan.modelo.GrupoUsuario;
import renan.sessao.SessaoGeral;
import renan.util.Util;
import renan.util.UtilController;
import renan.util.UtilReflection;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class GrupoUsuarioController {

	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public GrupoUsuarioController(Result result, Validator validator, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {
		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(filhaDe = "criarEditarGrupoUsuario")
	public void criarGrupoUsuario() {

		sessaoGeral.adicionar("idGrupoUsuario", null);
		result.forwardTo(this).criarEditarGrupoUsuario();
	}

	@Path("/grupoUsuario/editarGrupoUsuario/{grupoUsuario.id}")
	@Funcionalidade(filhaDe = "criarEditarGrupoUsuario")
	public void editarGrupoUsuario(GrupoUsuario grupoUsuario) {

		grupoUsuario = hibernateUtil.selecionar(grupoUsuario);

		sessaoGeral.adicionar("idGrupoUsuario", grupoUsuario.getId());
		result.include(grupoUsuario);
		result.forwardTo(this).criarEditarGrupoUsuario();
	}

	@Funcionalidade(nome = "Criar e editar grupo de usuários")
	public void criarEditarGrupoUsuario() {

	}

	@Path("/grupoUsuario/excluirGrupoUsuario/{grupoUsuario.id}")
	@Funcionalidade(nome = "Excluir grupo de usuários")
	public void excluirGrupoUsuario(GrupoUsuario grupoUsuario) {

		GrupoUsuario grupoUsuarioSelecionado = hibernateUtil.selecionar(grupoUsuario);

		if (grupoUsuarioSelecionado.getUsuarios().size() > 0) {

			validator.add(new ValidationMessage("Existem usuários vinculados a este grupo de usuário. Se quiser remover este grupo de usuário, por favor, exclua os usuários deste grupo ou vincule-os a outro grupo de usuário", "Erro"));

			validator.onErrorForwardTo(this).listarGruposUsuario(null, null);
		}

		hibernateUtil.deletar(grupoUsuario);
		result.include("sucesso", "Grupo de usuários excluído com sucesso");
		result.forwardTo(this).listarGruposUsuario(null, null);
	}

	@Funcionalidade(nome = "Grupos de usuários", modulo = "Controle de acesso")
	public void listarGruposUsuario(GrupoUsuario grupoUsuario, Integer pagina) {

		grupoUsuario = (GrupoUsuario) UtilController.preencherFiltros(grupoUsuario, "grupoUsuario", sessaoGeral);
		if (Util.vazio(grupoUsuario)) {
			grupoUsuario = new GrupoUsuario();
		}

		List<GrupoUsuario> gruposUsuario = hibernateUtil.buscar(grupoUsuario, pagina);
		result.include("gruposUsuario", gruposUsuario);
	}

	@Funcionalidade(filhaDe = "criarEditarGrupoUsuario")
	public void salvarGrupoUsuario(GrupoUsuario grupoUsuario) {

		if (Util.vazio(sessaoGeral.getValor("idGrupoUsuario"))) {

			validarNomesRepetidos(grupoUsuario);
		}

		else {

			GrupoUsuario grupoUsuarioSelecionado = hibernateUtil.selecionar(new GrupoUsuario((Integer) sessaoGeral.getValor("idGrupoUsuario")));

			if (!grupoUsuario.getNome().equals(grupoUsuarioSelecionado.getNome())) {

				validarNomesRepetidos(grupoUsuario);
			}

			grupoUsuario.setId((Integer) sessaoGeral.getValor("idGrupoUsuario"));
		}

		hibernateUtil.salvarOuAtualizar(grupoUsuario);
		result.include("sucesso", "Grupo de usuário salvo com sucesso");
		result.forwardTo(this).listarGruposUsuario(new GrupoUsuario(), null);
	}

	private void validarNomesRepetidos(GrupoUsuario grupoUsuario) {

		GrupoUsuario grupoUsuarioFiltro = new GrupoUsuario();
		grupoUsuarioFiltro.setNome(grupoUsuario.getNome());

		if (Util.preenchido(hibernateUtil.buscar(grupoUsuarioFiltro, MatchMode.EXACT))) {
			validator.add(new ValidationMessage("Já existe um grupo de usuário com este nome", "Erro"));
		}
		validator.onErrorForwardTo(this).criarEditarGrupoUsuario();
	}

	@Path("/grupoUsuario/vincularFuncionalidades/{grupoUsuario.id}")
	@Funcionalidade(nome = "Vincular funcionalidades ao grupo de usuários")
	public void vincularFuncionalidades(GrupoUsuario grupoUsuario) {

		grupoUsuario = hibernateUtil.selecionar(grupoUsuario);
		sessaoGeral.adicionar("grupoUsuario", grupoUsuario);

		List<String> funcionalidadesDoGrupoSelecionado = obterCodigosFuncionalidades(grupoUsuario.getFuncionalidades());
		result.include("funcionalidadesDoGrupoSelecionado", funcionalidadesDoGrupoSelecionado);

		result.include("funcionalidades", obterFuncionalidades());
	}

	private List<String> obterCodigosFuncionalidades(List<FuncionalidadeGrupoUsuario> funcionalidades) {

		List<String> codigosFuncionalidades = new ArrayList<String>();

		for (FuncionalidadeGrupoUsuario funcionalidade : funcionalidades) {

			codigosFuncionalidades.add(funcionalidade.getCodigo());
		}

		return codigosFuncionalidades;
	}

	@Funcionalidade(filhaDe = "vincularFuncionalidades")
	public void salvarVinculosFuncionalidades(List<String> funcionalidadesSelecionadas) {

		GrupoUsuario grupoUsuario = (GrupoUsuario) sessaoGeral.getValor("grupoUsuario");

		deletarTodasFuncionalidadesDoGrupoDeUsuario(grupoUsuario);
		adicionarFuncionalidadesGrupoUsuario(funcionalidadesSelecionadas, grupoUsuario);

		result.include("sucesso", "Funcionalidades vinculadas ao grupo de usuário");
		result.forwardTo(this).listarGruposUsuario(new GrupoUsuario(), null);
	}

	private void deletarTodasFuncionalidadesDoGrupoDeUsuario(GrupoUsuario grupoUsuario) {

		List<FuncionalidadeGrupoUsuario> funcionalidadesQueSeraoRemovidas = new ArrayList<FuncionalidadeGrupoUsuario>();

		for (FuncionalidadeGrupoUsuario funcionalidade : grupoUsuario.getFuncionalidades()) {

			funcionalidadesQueSeraoRemovidas.add(funcionalidade);
		}

		hibernateUtil.deletar(funcionalidadesQueSeraoRemovidas);
	}

	private void adicionarFuncionalidadesGrupoUsuario(List<String> funcionalidadesSelecionadas, GrupoUsuario grupoUsuario) {

		if (Util.preenchido(funcionalidadesSelecionadas)) {

			List<FuncionalidadeGrupoUsuario> funcionalidadesQueSeraoAdicionadas = new ArrayList<FuncionalidadeGrupoUsuario>();

			for (String funcionalidadeSelecionada : funcionalidadesSelecionadas) {

				FuncionalidadeGrupoUsuario funcionalidade = new FuncionalidadeGrupoUsuario();
				funcionalidade.setCodigo(funcionalidadeSelecionada);
				funcionalidade.setGrupoUsuario(grupoUsuario);

				funcionalidadesQueSeraoAdicionadas.add(funcionalidade);
			}

			hibernateUtil.salvarOuAtualizar(funcionalidadesQueSeraoAdicionadas);
		}
	}

	public static HashMap<String, FuncionalidadeGrupoUsuario> obterHashFuncionalidades() {

		HashMap<String, FuncionalidadeGrupoUsuario> funcionalidadesHash = new HashMap<String, FuncionalidadeGrupoUsuario>();

		List<FuncionalidadeGrupoUsuario> funcionalidadesLista = obterFuncionalidades();

		for (FuncionalidadeGrupoUsuario funcionalidade : funcionalidadesLista) {

			funcionalidadesHash.put(funcionalidade.getCodigo(), funcionalidade);
		}

		return funcionalidadesHash;
	}

	@SuppressWarnings("rawtypes")
	public static List<FuncionalidadeGrupoUsuario> obterFuncionalidades() {

		List<FuncionalidadeGrupoUsuario> funcionalidadesGrupoUsuario = new ArrayList<FuncionalidadeGrupoUsuario>();

		try {
			Iterable<Class> classes = new UtilReflection().obterClassesDoPacote(new UtilReflection().obterPacotePrincipal() + ".controller");

			for (Class classe : classes) {

				List<FuncionalidadeGrupoUsuario> funcionalidadesDaClasse = new ArrayList<FuncionalidadeGrupoUsuario>();

				for (Method metodo : classe.getMethods()) {

					if (metodo.isAnnotationPresent(Funcionalidade.class)) {

						Funcionalidade anotacao = metodo.getAnnotation(Funcionalidade.class);

						if (Util.preenchido(anotacao.nome())) {

							String nomeClasse = classe.getSimpleName();
							String nomeMetodo = metodo.getName();

							String codigo = montarCodigoFuncionalidade(nomeClasse, nomeMetodo);

							FuncionalidadeGrupoUsuario funcionalidadeGrupoUsuario = new FuncionalidadeGrupoUsuario();
							funcionalidadeGrupoUsuario.setCodigo(codigo);

							if (Util.preenchido(anotacao.modulo())) {

								String nomeFuncionalidade = anotacao.nome().substring(0, 1).toLowerCase() + anotacao.nome().substring(1);
								nomeFuncionalidade = "Acessar " + nomeFuncionalidade;

								funcionalidadeGrupoUsuario.setNomeFuncionalidade(nomeFuncionalidade);
								funcionalidadeGrupoUsuario.setModulo(anotacao.modulo());
							} else {
								funcionalidadeGrupoUsuario.setNomeFuncionalidade(anotacao.nome());
							}

							funcionalidadesDaClasse.add(funcionalidadeGrupoUsuario);
						}
					}
				}

				funcionalidadesGrupoUsuario.addAll(organizarFuncionalidades(funcionalidadesDaClasse));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return funcionalidadesGrupoUsuario;
	}

	public static String montarCodigoFuncionalidade(String nomeClasse, String nomeMetodo) {

		String nomeClasseSemController = nomeClasse.replace("Controller", "");
		nomeClasseSemController = nomeClasseSemController.substring(0, 1).toLowerCase() + nomeClasseSemController.substring(1);

		String codigo = nomeClasseSemController + "/" + nomeMetodo;

		return codigo;
	}

	private static List<FuncionalidadeGrupoUsuario> organizarFuncionalidades(List<FuncionalidadeGrupoUsuario> funcionalidadesDaClasse) {

		Collections.sort(funcionalidadesDaClasse, new Comparator<FuncionalidadeGrupoUsuario>() {

			public int compare(FuncionalidadeGrupoUsuario fg1, FuncionalidadeGrupoUsuario fg2) {

				return fg1.getNomeFuncionalidade().toUpperCase().compareTo(fg2.getNomeFuncionalidade().toUpperCase());
			}
		});

		List<FuncionalidadeGrupoUsuario> funcionalidadesOrganizadas = new ArrayList<FuncionalidadeGrupoUsuario>();

		for (FuncionalidadeGrupoUsuario funcionalidade : funcionalidadesDaClasse) {

			if (Util.preenchido(funcionalidade.getModulo())) {

				funcionalidadesOrganizadas.add(funcionalidade);
			}
		}

		for (FuncionalidadeGrupoUsuario funcionalidade : funcionalidadesDaClasse) {

			if (!funcionalidadesOrganizadas.contains(funcionalidade)) {

				funcionalidadesOrganizadas.add(funcionalidade);
			}
		}

		return funcionalidadesOrganizadas;
	}
}
