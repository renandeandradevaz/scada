package scada.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import scada.anotacoes.Funcionalidade;
import scada.auxiliar.RelatorioProntoArmamentoAuxiliar;
import scada.hibernate.AliasHibernateUtil;
import scada.hibernate.HibernateUtil;
import scada.modelo.Armamento;
import scada.modelo.MovimentacaoDeArmamento;
import scada.modelo.TipoArmamento;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioProntoArmamentoController {

	private Result result;
	private HibernateUtil hibernateUtil;

	public RelatorioProntoArmamentoController(Result result, HibernateUtil hibernateUtil) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(nome = "Pronto do armamento", modulo = "Relatórios")
	public void gerarRelatorioProntoArmamento() {

		result.include("dataHoraAtual", new GregorianCalendar());

		List<TipoArmamento> tiposArmamento = hibernateUtil.buscar(new TipoArmamento());

		List<RelatorioProntoArmamentoAuxiliar> acautelamentosAbertos = new ArrayList<RelatorioProntoArmamentoAuxiliar>();
		List<RelatorioProntoArmamentoAuxiliar> resumoMovimentacoes = new ArrayList<RelatorioProntoArmamentoAuxiliar>();

		for (TipoArmamento tipoArmamento : tiposArmamento) {

			RelatorioProntoArmamentoAuxiliar acautelamentoAberto = new RelatorioProntoArmamentoAuxiliar();

			acautelamentoAberto.setTipoArmamento(tipoArmamento);

			Armamento armamento = new Armamento();
			armamento.setTipoArmamento(tipoArmamento);

			Integer quantidadePrevista = hibernateUtil.contar(armamento);
			acautelamentoAberto.setQuantidadePrevista(quantidadePrevista);

			List<String> status = new ArrayList<String>();
			status.add(Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO);
			status.add(Armamento.ARMAMENTO_INDISPONIVEL_ACAUTELADO);

			List<Criterion> restricoes = new ArrayList<Criterion>();
			restricoes.add(Restrictions.in("status", status));

			Integer quantidadeAcautelada = hibernateUtil.contar(armamento, restricoes);
			acautelamentoAberto.setQuantidadeAcautelada(quantidadeAcautelada);

			acautelamentoAberto.setQuantidadeExistente(quantidadePrevista - quantidadeAcautelada);

			acautelamentosAbertos.add(acautelamentoAberto);

			if (quantidadeAcautelada > 0) {

				resumoMovimentacoes.add(acautelamentoAberto);
			}

		}

		result.include("acautelamentosAbertos", acautelamentosAbertos);

		List<String> status = new ArrayList<String>();
		status.add(Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO);
		status.add(Armamento.ARMAMENTO_INDISPONIVEL_ACAUTELADO);

		List<Criterion> restricoes = new ArrayList<Criterion>();
		restricoes.add(Restrictions.in("armamento.status", status));

		MovimentacaoDeArmamento movimentacaoDeArmamento = new MovimentacaoDeArmamento();
		movimentacaoDeArmamento.setDevolvido(false);

		List<AliasHibernateUtil> alias = new ArrayList<AliasHibernateUtil>();
		alias.add(new AliasHibernateUtil("armamento", "armamento"));
		alias.add(new AliasHibernateUtil("armamento.tipoArmamento", "tipoArmamento"));

		List<MovimentacaoDeArmamento> armamentosAcautelados = hibernateUtil.buscar(movimentacaoDeArmamento, restricoes, Order.asc("tipoArmamento.descricao"), null, alias);

		result.include("armamentosAcautelados", armamentosAcautelados);

		for (RelatorioProntoArmamentoAuxiliar resumo : resumoMovimentacoes) {

			BigDecimal quantidadeAcautelada = new BigDecimal(resumo.getQuantidadeAcautelada());
			BigDecimal quantidadePrevista = new BigDecimal(resumo.getQuantidadePrevista());

			BigDecimal porcentagemAcautelada = quantidadeAcautelada.divide(quantidadePrevista, 3, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

			resumo.setPorcentagemAcautelada(porcentagemAcautelada);
		}

		result.include("resumoMovimentacoes", resumoMovimentacoes);

	}
}
