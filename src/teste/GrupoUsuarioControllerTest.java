package teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import renan.controller.GrupoUsuarioController;
import renan.hibernate.HibernateUtil;
import renan.modelo.FuncionalidadeGrupoUsuario;
import renan.modelo.GrupoUsuario;
import renan.modelo.Usuario;
import renan.sessao.SessaoGeral;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;

public class GrupoUsuarioControllerTest {

	MockResult result;
	MockValidator validator;
	SessaoGeral sessaoGeral;
	HibernateUtil hibernateUtil;

	@Before
	public void limpaEPopulaBanco() {

		if (hibernateUtil == null) {

			hibernateUtil = new HibernateUtil();
		}

		result = new MockResult();
		validator = new MockValidator();
		sessaoGeral = new SessaoGeral();

		limparBanco();
		popularBanco();
	}

	@After
	public void fecharSessao() {

		hibernateUtil.fecharSessao();
	}

	private void limparBanco() {

		hibernateUtil.deletar(hibernateUtil.buscar(new FuncionalidadeGrupoUsuario()));
		hibernateUtil.deletar(hibernateUtil.buscar(new Usuario()));
		hibernateUtil.deletar(hibernateUtil.buscar(new GrupoUsuario()));
	}

	private void popularBanco() {

		GrupoUsuario grupoUsuario = new GrupoUsuario();
		grupoUsuario.setNome("teste");

		hibernateUtil.salvarOuAtualizar(grupoUsuario);

	}

	@Test
	public void testaSalvar() {

		assertEquals(new Integer(1), hibernateUtil.contar(new GrupoUsuario()));

		GrupoUsuarioController controller = new GrupoUsuarioController(new MockResult(), new MockValidator(), new SessaoGeral(), hibernateUtil);

		GrupoUsuario grupoUsuario = new GrupoUsuario();
		grupoUsuario.setNome("Renan");

		controller.salvarGrupoUsuario(grupoUsuario);

		assertEquals(new Integer(2), hibernateUtil.contar(new GrupoUsuario()));
	}

	@Test
	public void validarNomesRepetidos() {

		assertEquals(new Integer(1), hibernateUtil.contar(new GrupoUsuario()));

		try {

			GrupoUsuarioController controller = new GrupoUsuarioController(result, validator, new SessaoGeral(), hibernateUtil);

			GrupoUsuario grupoUsuario = new GrupoUsuario();
			grupoUsuario.setNome("teste");

			controller.salvarGrupoUsuario(grupoUsuario);

			fail("Não deveria ter salvo");
		} catch (Exception e) {

			assertTrue(validator.getErrors().get(0).toString().contains("Já existe um grupo de usuário com este nome"));

		}

		assertEquals(new Integer(1), hibernateUtil.contar(new GrupoUsuario()));
	}

	@Test
	public void salvarVinculosFuncionalidades() {

		GrupoUsuario grupoUsuario = new GrupoUsuario();
		grupoUsuario.setNome("teste");
		grupoUsuario = hibernateUtil.selecionar(grupoUsuario);

		FuncionalidadeGrupoUsuario funcionalidadeGrupoUsuario = new FuncionalidadeGrupoUsuario();
		funcionalidadeGrupoUsuario.setCodigo("funcionalidade");
		funcionalidadeGrupoUsuario.setGrupoUsuario(grupoUsuario);
		hibernateUtil.salvarOuAtualizar(funcionalidadeGrupoUsuario);

		grupoUsuario = hibernateUtil.selecionar(grupoUsuario);

		grupoUsuario.setFuncionalidades(new ArrayList<FuncionalidadeGrupoUsuario>());
		grupoUsuario.getFuncionalidades().add(funcionalidadeGrupoUsuario);

		sessaoGeral.adicionar("grupoUsuario", grupoUsuario);

		GrupoUsuarioController controller = new GrupoUsuarioController(result, validator, sessaoGeral, hibernateUtil);

		List<String> funcionalidades = new ArrayList<String>();
		funcionalidades.add("salvarUsuario");
		funcionalidades.add("excluirQualquerCoisa");

		controller.salvarVinculosFuncionalidades(funcionalidades);

		assertEquals(new Integer(2), hibernateUtil.contar(new FuncionalidadeGrupoUsuario()));
	}

}
