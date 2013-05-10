package teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scada.controller.GrupoOperadorController;
import scada.hibernate.HibernateUtil;
import scada.modelo.FuncionalidadeGrupoOperador;
import scada.modelo.GrupoOperador;
import scada.modelo.Operador;
import scada.sessao.SessaoGeral;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;

public class GrupoOperadorControllerTest {

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

		hibernateUtil.deletar(hibernateUtil.buscar(new FuncionalidadeGrupoOperador()));
		hibernateUtil.deletar(hibernateUtil.buscar(new Operador()));
		hibernateUtil.deletar(hibernateUtil.buscar(new GrupoOperador()));
	}

	private void popularBanco() {

		GrupoOperador grupoOperador = new GrupoOperador();
		grupoOperador.setNome("teste");

		hibernateUtil.salvarOuAtualizar(grupoOperador);

	}

	@Test
	public void testaSalvar() {

		assertEquals(new Integer(1), hibernateUtil.contar(new GrupoOperador()));

		GrupoOperadorController controller = new GrupoOperadorController(new MockResult(), new MockValidator(), new SessaoGeral(), hibernateUtil);

		GrupoOperador grupoOperador = new GrupoOperador();
		grupoOperador.setNome("scada");

		controller.salvarGrupoOperador(grupoOperador);

		assertEquals(new Integer(2), hibernateUtil.contar(new GrupoOperador()));
	}

	@Test
	public void validarNomesRepetidos() {

		assertEquals(new Integer(1), hibernateUtil.contar(new GrupoOperador()));

		try {

			GrupoOperadorController controller = new GrupoOperadorController(result, validator, new SessaoGeral(), hibernateUtil);

			GrupoOperador grupoOperador = new GrupoOperador();
			grupoOperador.setNome("teste");

			controller.salvarGrupoOperador(grupoOperador);

			fail("Não deveria ter salvo");
		} catch (Exception e) {

			assertTrue(validator.getErrors().get(0).toString().contains("Já existe um grupo de operador com este nome"));

		}

		assertEquals(new Integer(1), hibernateUtil.contar(new GrupoOperador()));
	}

	@Test
	public void salvarVinculosFuncionalidades() {

		GrupoOperador grupoOperador = new GrupoOperador();
		grupoOperador.setNome("teste");
		grupoOperador = hibernateUtil.selecionar(grupoOperador);

		FuncionalidadeGrupoOperador funcionalidadeGrupoOperador = new FuncionalidadeGrupoOperador();
		funcionalidadeGrupoOperador.setCodigo("funcionalidade");
		funcionalidadeGrupoOperador.setGrupoOperador(grupoOperador);
		hibernateUtil.salvarOuAtualizar(funcionalidadeGrupoOperador);

		grupoOperador = hibernateUtil.selecionar(grupoOperador);

		grupoOperador.setFuncionalidades(new ArrayList<FuncionalidadeGrupoOperador>());
		grupoOperador.getFuncionalidades().add(funcionalidadeGrupoOperador);

		sessaoGeral.adicionar("grupoOperador", grupoOperador);

		GrupoOperadorController controller = new GrupoOperadorController(result, validator, sessaoGeral, hibernateUtil);

		List<String> funcionalidades = new ArrayList<String>();
		funcionalidades.add("salvarOperador");
		funcionalidades.add("excluirQualquerCoisa");

		controller.salvarVinculosFuncionalidades(funcionalidades);

		assertEquals(new Integer(2), hibernateUtil.contar(new FuncionalidadeGrupoOperador()));
	}

}
