package teste;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scada.hibernate.HibernateUtil;
import scada.modelo.Armamento;
import scada.modelo.Cliente;
import scada.modelo.FuncionalidadeGrupoOperador;
import scada.modelo.GrupoOperador;
import scada.modelo.MovimentacaoDeArmamento;
import scada.modelo.Operador;
import scada.modelo.TipoArmamento;

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

		hibernateUtil.deletar(hibernateUtil.buscar(new MovimentacaoDeArmamento()));
		hibernateUtil.deletar(hibernateUtil.buscar(new FuncionalidadeGrupoOperador()));
		hibernateUtil.deletar(hibernateUtil.buscar(new Operador()));
		hibernateUtil.deletar(hibernateUtil.buscar(new GrupoOperador()));
		hibernateUtil.deletar(hibernateUtil.buscar(new Armamento()));
		hibernateUtil.deletar(hibernateUtil.buscar(new TipoArmamento()));
		hibernateUtil.deletar(hibernateUtil.buscar(new Cliente()));
	}

	private void popularBanco() {

		for (int i = 0; i < 7; i++) {

			GrupoOperador grupoOperador = new GrupoOperador();
			grupoOperador.setNome("grupo " + i);
			hibernateUtil.salvarOuAtualizar(grupoOperador);

			Operador operador1 = new Operador();
			operador1.setGrupoOperador(grupoOperador);
			operador1.setLogin("operador1." + i);
			hibernateUtil.salvarOuAtualizar(operador1);

			Operador operador2 = new Operador();
			operador2.setGrupoOperador(grupoOperador);
			operador2.setLogin("operador2." + i);
			hibernateUtil.salvarOuAtualizar(operador2);

			Cliente cliente = new Cliente();
			cliente.setNome("Teste");
			hibernateUtil.salvarOuAtualizar(cliente);

			TipoArmamento tipoArmamento = new TipoArmamento();
			tipoArmamento.setDescricao("tipoArmamento." + i);
			hibernateUtil.salvarOuAtualizar(tipoArmamento);

			Armamento armamento = new Armamento();
			armamento.setTipoArmamento(tipoArmamento);
			hibernateUtil.salvarOuAtualizar(armamento);

			MovimentacaoDeArmamento movimentacaoDeArmamento = new MovimentacaoDeArmamento();
			movimentacaoDeArmamento.setArmamento(armamento);
			hibernateUtil.salvarOuAtualizar(movimentacaoDeArmamento);
		}
	}

	@Test
	public void testaSalvar() {

		assertEquals(new Integer(7), hibernateUtil.contar(new GrupoOperador()));
		GrupoOperador grupoOperador = new GrupoOperador();
		hibernateUtil.salvarOuAtualizar(grupoOperador);
		assertEquals(new Integer(8), hibernateUtil.contar(new GrupoOperador()));

		assertEquals(new Integer(14), hibernateUtil.contar(new Operador()));
		Operador operador = new Operador();
		operador.setGrupoOperador(grupoOperador);
		hibernateUtil.salvarOuAtualizar(operador);
		assertEquals(new Integer(15), hibernateUtil.contar(new Operador()));

	}

	@Test
	public void testaExcluir() {

		assertEquals(new Integer(14), hibernateUtil.contar(new Operador()));
		Operador operador = new Operador();
		operador.setLogin("operador1.0");
		hibernateUtil.deletar(hibernateUtil.selecionar(operador));
		assertEquals(new Integer(13), hibernateUtil.contar(new Operador()));

	}

	@Test
	public void pesquisaComFiltros() {

		Operador operador = new Operador();
		operador.setLogin("operador1.");
		assertEquals(7, hibernateUtil.buscar(operador).size());

		GrupoOperador grupoOperador = new GrupoOperador();
		grupoOperador.setNome("Grupo 2");
		operador = new Operador();
		operador.setGrupoOperador(grupoOperador);
		assertEquals(2, hibernateUtil.buscar(operador).size());

		grupoOperador = new GrupoOperador();
		grupoOperador.setNome("Grupo 2");
		operador = new Operador();
		operador.setGrupoOperador(new GrupoOperador(hibernateUtil.selecionar(grupoOperador).getId()));
		assertEquals(2, hibernateUtil.buscar(operador).size());

	}

	@Test
	public void testaPaginacao() {

		popularBanco();
		popularBanco();
		popularBanco();

		List<Operador> operadores = hibernateUtil.buscar(new Operador(), 3);
		assertEquals(10, operadores.size());
		assertEquals("operador2.3", operadores.get(0).getLogin());
	}

	@Test
	public void testaOrdenacao() {

		popularBanco();
		popularBanco();
		popularBanco();

		List<Operador> operadores = hibernateUtil.buscar(new Operador(), 2, Order.asc("login"));
		assertEquals("operador1.2", operadores.get(0).getLogin());
		assertEquals("operador1.2", operadores.get(1).getLogin());
		assertEquals("operador1.3", operadores.get(2).getLogin());
		assertEquals("operador1.3", operadores.get(3).getLogin());
		assertEquals("operador1.3", operadores.get(4).getLogin());
		assertEquals("operador1.3", operadores.get(5).getLogin());
		assertEquals("operador1.4", operadores.get(6).getLogin());
		assertEquals("operador1.4", operadores.get(7).getLogin());
		assertEquals("operador1.4", operadores.get(8).getLogin());

	}

	@Test
	public void testaRestricoes() {

		List<Criterion> restricoes = new ArrayList<Criterion>();
		restricoes.add(Restrictions.eq("login", "operador1.4"));
		List<Operador> operadores = hibernateUtil.buscar(new Operador(), restricoes);
		assertEquals(1, operadores.size());
	}

	@Test
	public void testaMatchMode() {

		Operador operador = new Operador();
		operador.setLogin(".0");
		assertEquals(new Integer(2), hibernateUtil.contar(operador, MatchMode.END));
	}

	@Test
	public void testaPesquisaNivel2() {

		TipoArmamento tipoArmamento = new TipoArmamento();
		tipoArmamento.setDescricao("tipoArmamento.1");

		Armamento armamento = new Armamento();
		armamento.setTipoArmamento(tipoArmamento);

		MovimentacaoDeArmamento movimentacaoDeArmamento = new MovimentacaoDeArmamento();
		movimentacaoDeArmamento.setArmamento(armamento);

		assertEquals(new Integer(1), hibernateUtil.contar(movimentacaoDeArmamento));

		tipoArmamento = new TipoArmamento(hibernateUtil.selecionar(tipoArmamento).getId());

		armamento = new Armamento();
		armamento.setTipoArmamento(tipoArmamento);

		movimentacaoDeArmamento = new MovimentacaoDeArmamento();
		movimentacaoDeArmamento.setArmamento(armamento);

		assertEquals(new Integer(1), hibernateUtil.contar(movimentacaoDeArmamento));
	}
}
