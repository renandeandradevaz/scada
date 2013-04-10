package renan.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import renan.util.Util;
import renan.util.UtilReflection;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Component
@RequestScoped
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private Session session;
	private Result result;

	public HibernateUtil(Session session, Result result) {

		this.result = result;
		this.session = session;

	}

	public HibernateUtil() {

	}

	@SuppressWarnings("deprecation")
	private void iniciarSessionFactory() {

		if (sessionFactory == null) {

			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
	}

	public void fecharSessao() {

		if (session != null && session.isOpen()) {

			session.close();
		}

		// gerarEstatisticas();
	}

	public void salvarOuAtualizar(Entidade entidade) {

		try {

			iniciarSessionFactory();

			abrirSessao();
			session.beginTransaction();

			if (entidade.getId() != null) {

				entidade = (Entidade) session.merge(entidade);
			}

			session.saveOrUpdate(entidade);
			session.getTransaction().commit();
		}

		catch (RuntimeException e) {

			session.getTransaction().rollback();
			fecharSessao();
			throw e;
		}

	}

	private void abrirSessao() {

		if (session == null || !session.isOpen()) {

			session = sessionFactory.openSession();
		}
	}

	public <E extends Entidade> void salvarOuAtualizar(List<E> entidades) {

		try {

			iniciarSessionFactory();

			abrirSessao();
			session.beginTransaction();

			for (Entidade entidade : entidades) {

				if (entidade.getId() != null) {

					entidade = (Entidade) session.merge(entidade);
				}

				session.saveOrUpdate(entidade);
			}

			session.getTransaction().commit();
		}

		catch (RuntimeException e) {

			session.getTransaction().rollback();
			fecharSessao();
			throw e;
		}

	}

	public void deletar(Entidade entidade) {

		try {

			iniciarSessionFactory();

			abrirSessao();
			session.beginTransaction();

			entidade = (Entidade) session.merge(entidade);
			session.delete(entidade);
			session.getTransaction().commit();
		}

		catch (RuntimeException e) {

			session.getTransaction().rollback();
			fecharSessao();
			throw e;
		}
	}

	public <E extends Entidade> void deletar(List<E> entidades) {

		try {

			iniciarSessionFactory();

			abrirSessao();
			session.beginTransaction();

			for (Entidade entidade : entidades) {

				entidade = (Entidade) session.merge(entidade);
				session.delete(entidade);
			}

			session.getTransaction().commit();
		}

		catch (RuntimeException e) {

			session.getTransaction().rollback();
			fecharSessao();
			throw e;
		}

	}

	private void gerarEstatisticas() {

		if (!sessionFactory.getStatistics().isStatisticsEnabled()) {
			sessionFactory.getStatistics().setStatisticsEnabled(true);
		}
		System.out.println("Quantidade de sessões abertas: " + sessionFactory.getStatistics().getSessionOpenCount());
		System.out.println("Quantidade de sessões fechadas: " + sessionFactory.getStatistics().getSessionCloseCount());
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro) {

		return buscar(filtro, null, null, MatchMode.ANYWHERE);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina) {

		return buscar(filtro, pagina, null, null, null);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes) {

		return buscar(filtro, restricoes, null, null);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, String alias) {

		return buscar(filtro, restricoes, null, null, alias);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, Order ordenacao) {

		return buscar(filtro, null, ordenacao, null);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, MatchMode matchMode) {

		return buscar(filtro, null, null, matchMode);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, List<Criterion> restricoes) {

		return buscar(filtro, pagina, restricoes, null, null);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, Order ordenacao) {

		return buscar(filtro, pagina, null, ordenacao, null);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, MatchMode matchMode) {

		return buscar(filtro, pagina, null, null, matchMode);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, Order ordenacao) {

		return buscar(filtro, restricoes, ordenacao, null);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, MatchMode matchMode) {

		return buscar(filtro, restricoes, null, matchMode);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, Order ordenacao, MatchMode matchMode) {

		return buscar(filtro, null, ordenacao, matchMode);
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, List<Criterion> restricoes, Order ordenacao) {

		return buscar(filtro, pagina, restricoes, ordenacao, null);
	}

	public Integer contar(Entidade filtro) {

		return contar(filtro, null, null);
	}

	public Integer contar(Entidade filtro, List<Criterion> restricoes) {

		return contar(filtro, restricoes, null);
	}

	public Integer contar(Entidade filtro, MatchMode matchMode) {

		return contar(filtro, null, matchMode);
	}

	public Integer contar(Entidade filtro, List<Criterion> restricoes, MatchMode matchMode) {

		Criteria criteria = gerarFiltros(filtro, matchMode);
		adicionarOrdenacaoERestricoes(restricoes, null, criteria, false);
		criteria.setProjection(Projections.rowCount());
		Long quantidadeRegistros = (Long) criteria.uniqueResult();

		return quantidadeRegistros.intValue();
	}

	public <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, Order ordenacao, MatchMode matchMode) {

		return buscar(filtro, restricoes, ordenacao, matchMode, null);
	}

	@SuppressWarnings("unchecked")
	public <E extends Entidade> List<E> buscar(Entidade filtro, List<Criterion> restricoes, Order ordenacao, MatchMode matchMode, String alias) {

		Criteria criteria = gerarFiltros(filtro, matchMode);

		if (Util.preenchido(alias)) {

			criteria.createAlias(alias, alias);
		}

		adicionarOrdenacaoERestricoes(restricoes, ordenacao, criteria, false);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public <E extends Entidade> List<E> buscar(Entidade filtro, Integer pagina, List<Criterion> restricoes, Order ordenacao, MatchMode matchMode) {

		Criteria criteria = adicionarPaginacao(filtro, pagina, matchMode, restricoes, ordenacao);

		adicionarOrdenacaoERestricoes(restricoes, ordenacao, criteria, true);

		return criteria.list();
	}

	private Criteria adicionarPaginacao(Entidade filtro, Integer pagina, MatchMode matchMode, List<Criterion> restricoes, Order ordenacao) {

		Criteria criteria = gerarFiltros(filtro, matchMode);
		adicionarOrdenacaoERestricoes(restricoes, ordenacao, criteria, false);
		criteria.setProjection(Projections.rowCount());
		Long quantidadeRegistros = (Long) criteria.uniqueResult();

		criteria = gerarFiltros(filtro, matchMode);
		Integer quantidadeDeRegistrosPorPagina = 10;
		if (pagina == null) {
			pagina = 1;
		}
		pagina = pagina - 1;
		Integer primeiroRegistroPagina = pagina * quantidadeDeRegistrosPorPagina;
		criteria.setFirstResult(primeiroRegistroPagina);
		criteria.setMaxResults(quantidadeDeRegistrosPorPagina);

		if (Util.preenchido(result)) {
			result.include("quantidadeRegistros", quantidadeRegistros);
			result.include("primeiroRegistroPagina", primeiroRegistroPagina + 1);

			Integer ultimoRegistroPagina = primeiroRegistroPagina + quantidadeDeRegistrosPorPagina;
			if (ultimoRegistroPagina > quantidadeRegistros) {
				ultimoRegistroPagina = quantidadeRegistros.intValue();
			}
			result.include("ultimoRegistroPagina", ultimoRegistroPagina);

			result.include("pagina", pagina + 1);
			result.include("quantidadePaginas", quantidadeRegistros / quantidadeDeRegistrosPorPagina + 1);
		}

		return criteria;
	}

	private void adicionarOrdenacaoERestricoes(List<Criterion> restricoes, Order ordenacao, Criteria criteria, Boolean ordenacaoDecrescente) {

		if (Util.preenchido(ordenacao)) {

			criteria.addOrder(ordenacao);
		} else {

			if (ordenacaoDecrescente) {

				criteria.addOrder(Order.desc("id"));
			} else {

				criteria.addOrder(Order.asc("id"));
			}
		}

		if (Util.preenchido(restricoes)) {

			for (Criterion restricao : restricoes) {

				criteria.add(restricao);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends Entidade> E selecionar(Entidade filtro) {

		Criteria criteria = gerarFiltros(filtro, null);

		return (E) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <E extends Entidade> E selecionar(Entidade filtro, MatchMode matchMode) {

		Criteria criteria = gerarFiltros(filtro, matchMode);

		return (E) criteria.uniqueResult();
	}

	private Criteria gerarFiltros(Entidade filtro, MatchMode matchMode) {

		iniciarSessionFactory();

		abrirSessao();

		Criteria criteria = session.createCriteria(filtro.getClass());

		if (Util.vazio(matchMode)) {
			matchMode = MatchMode.ANYWHERE;
		}

		UtilReflection.nullifyStrings(filtro);

		criteria.add(Example.create(filtro).enableLike(matchMode).ignoreCase());
		if (filtro.getId() != null && filtro.getId() != 0) {

			criteria.add(Restrictions.eq("id", filtro.getId()));
		}

		gerarFiltroParaAssociacoes(criteria, filtro);

		return criteria;
	}

	private void gerarFiltroParaAssociacoes(Criteria criteria, Entidade entidadePrimeiroNivel) {

		Method[] metodos = entidadePrimeiroNivel.getClass().getMethods();

		for (Method metodo : metodos) {

			if (metodo.getName().startsWith("get")) {
				try {
					Object objeto = metodo.invoke(entidadePrimeiroNivel, new Object[0]);

					if (objeto instanceof Entidade) {

						Entidade entidadeAssociada = (Entidade) objeto;

						String nomeEntidadeAssociada = metodo.getName().substring(3);
						String nomeEntidadeAssociadaDiminutivo = nomeEntidadeAssociada.substring(0, 1).toLowerCase() + nomeEntidadeAssociada.substring(1);

						if (entidadeAssociada.getId() != null && entidadeAssociada.getId() != 0) {

							criteria.add(Restrictions.eq(nomeEntidadeAssociadaDiminutivo + ".id", entidadeAssociada.getId()));
						}

						UtilReflection.nullifyStrings(entidadeAssociada);

						criteria.createCriteria(nomeEntidadeAssociadaDiminutivo).add(Example.create(entidadeAssociada).enableLike(MatchMode.ANYWHERE).ignoreCase());

					}
				} catch (IllegalArgumentException e) {

				} catch (IllegalAccessException e) {

				} catch (InvocationTargetException e) {

				}
			}
		}
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
