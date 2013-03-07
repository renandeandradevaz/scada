package teste;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import renan.hibernate.HibernateUtil;
import renan.modelo.FuncionalidadeGrupoUsuario;
import renan.modelo.GrupoUsuario;
import renan.modelo.Usuario;

public class HibernateUtilTest {

	HibernateUtil hibernateUtil;

	@Before
	public void limpaEPopulaBanco() {

		if (hibernateUtil == null) {

			hibernateUtil = new HibernateUtil();
		}

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

		for (int i = 0; i < 7; i++) {

			GrupoUsuario grupoUsuario = new GrupoUsuario();
			grupoUsuario.setNome("grupo " + i);

			hibernateUtil.salvarOuAtualizar(grupoUsuario);

			Usuario usuario1 = new Usuario();
			usuario1.setGrupoUsuario(grupoUsuario);
			usuario1.setLogin("usuario1." + i);
			hibernateUtil.salvarOuAtualizar(usuario1);

			Usuario usuario2 = new Usuario();
			usuario2.setGrupoUsuario(grupoUsuario);
			usuario2.setLogin("usuario2." + i);
			hibernateUtil.salvarOuAtualizar(usuario2);
		}
	}

	@Test
	public void testaSalvar() {

		assertEquals(new Integer(7), hibernateUtil.contar(new GrupoUsuario()));
		GrupoUsuario grupoUsuario = new GrupoUsuario();
		hibernateUtil.salvarOuAtualizar(grupoUsuario);
		assertEquals(new Integer(8), hibernateUtil.contar(new GrupoUsuario()));

		assertEquals(new Integer(14), hibernateUtil.contar(new Usuario()));
		Usuario usuario = new Usuario();
		usuario.setGrupoUsuario(grupoUsuario);
		hibernateUtil.salvarOuAtualizar(usuario);
		assertEquals(new Integer(15), hibernateUtil.contar(new Usuario()));

	}

	@Test
	public void testaExcluir() {

		assertEquals(new Integer(14), hibernateUtil.contar(new Usuario()));
		Usuario usuario = new Usuario();
		usuario.setLogin("usuario1.0");
		hibernateUtil.deletar(hibernateUtil.selecionar(usuario));
		assertEquals(new Integer(13), hibernateUtil.contar(new Usuario()));

	}

	@Test
	public void pesquisaComFiltros() {

		Usuario usuario = new Usuario();
		usuario.setLogin("usuario1.");
		assertEquals(7, hibernateUtil.buscar(usuario).size());

		GrupoUsuario grupoUsuario = new GrupoUsuario();
		grupoUsuario.setNome("Grupo 2");
		usuario = new Usuario();
		usuario.setGrupoUsuario(grupoUsuario);
		assertEquals(2, hibernateUtil.buscar(usuario).size());

		grupoUsuario = new GrupoUsuario();
		grupoUsuario.setNome("Grupo 2");
		usuario = new Usuario();
		usuario.setGrupoUsuario(new GrupoUsuario(hibernateUtil.selecionar(grupoUsuario).getId()));
		assertEquals(2, hibernateUtil.buscar(usuario).size());

	}

	@Test
	public void testaPaginacao() {

		popularBanco();
		popularBanco();
		popularBanco();

		List<Usuario> usuarios = hibernateUtil.buscar(new Usuario(), 3);
		assertEquals(10, usuarios.size());
		assertEquals("usuario2.3", usuarios.get(0).getLogin());
	}

	@Test
	public void testaOrdenacao() {

		popularBanco();
		popularBanco();
		popularBanco();

		List<Usuario> usuarios = hibernateUtil.buscar(new Usuario(), 2, Order.asc("login"));
		assertEquals("usuario1.2", usuarios.get(0).getLogin());
		assertEquals("usuario1.2", usuarios.get(1).getLogin());
		assertEquals("usuario1.3", usuarios.get(2).getLogin());
		assertEquals("usuario1.3", usuarios.get(3).getLogin());
		assertEquals("usuario1.3", usuarios.get(4).getLogin());
		assertEquals("usuario1.3", usuarios.get(5).getLogin());
		assertEquals("usuario1.4", usuarios.get(6).getLogin());
		assertEquals("usuario1.4", usuarios.get(7).getLogin());
		assertEquals("usuario1.4", usuarios.get(8).getLogin());

	}

	@Test
	public void testaRestricoes() {

		List<SimpleExpression> restricoes = new ArrayList<SimpleExpression>();
		restricoes.add(Restrictions.eq("login", "usuario1.4"));
		List<Usuario> usuarios = hibernateUtil.buscar(new Usuario(), restricoes);
		assertEquals(1, usuarios.size());
	}

	@Test
	public void testaMatchMode() {

		Usuario usuario = new Usuario();
		usuario.setLogin(".0");
		assertEquals(new Integer(2), hibernateUtil.contar(usuario, MatchMode.END));
	}
}
