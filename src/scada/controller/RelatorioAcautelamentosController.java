package scada.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import scada.anotacoes.Funcionalidade;
import scada.auxiliar.RelatorioAcautelamentosAuxiliar;
import scada.hibernate.HibernateUtil;
import scada.modelo.Armamento;
import scada.modelo.MovimentacaoDeArmamento;
import scada.modelo.TipoArmamento;
import scada.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioAcautelamentosController {

	private Result result;
	private HibernateUtil hibernateUtil;

	public RelatorioAcautelamentosController(Result result, HibernateUtil hibernateUtil) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(nome = "Acautelamentos", modulo = "Relatórios")
	public void selecionarTipoArmamentoRelatorioAcautelamentos() {

		buscarTiposDeArmamento();
	}

	private void buscarTiposDeArmamento() {

		result.include("tiposDeArmamento", hibernateUtil.buscar(new TipoArmamento()));
	}

	@Funcionalidade(filhaDe = "selecionarTipoArmamentoRelatorioAcautelamentos")
	public void gerarRelatorioAcautelamentos(Integer tipoArmamentoSelecionado, Integer quantidadeMeses, Integer quantidadeArmamentosMaisUtilizados) {

		buscarTiposDeArmamento();

		TipoArmamento tipoArmamento = hibernateUtil.selecionar(new TipoArmamento(tipoArmamentoSelecionado));
		result.include("tipoArmamentoSelecionado", tipoArmamento.getDescricao() + " - " + tipoArmamento.getCalibre());

		Armamento armamentoFiltro = new Armamento();
		armamentoFiltro.setTipoArmamento(tipoArmamento);

		List<Armamento> armamentos = hibernateUtil.buscar(armamentoFiltro);

		HashMap<String, List<Integer>> armamentosEQuantidades = new HashMap<String, List<Integer>>();

		List<String> mesesEAnos = new ArrayList<String>();
		DateTime hoje = new DateTime();

		List<RelatorioAcautelamentosAuxiliar> armamentosMaisAcautelados = new ArrayList<RelatorioAcautelamentosAuxiliar>();

		for (Armamento armamento : armamentos) {

			Integer totalAcautelamentosArmamento = 0;

			armamentosEQuantidades.put(armamento.getNumeracao(), new ArrayList<Integer>());

			MovimentacaoDeArmamento movimentacaoDeArmamento = new MovimentacaoDeArmamento();
			movimentacaoDeArmamento.setArmamento(armamento);
			movimentacaoDeArmamento.setTipoMovimentacao(MovimentacaoDeArmamento.TIPO_MOVIMENTACAO_ACAUTELAMENTO);

			for (int i = quantidadeMeses - 1; i >= 0; i--) {

				DateTime mesPassado = hoje.minusMonths(i);
				DateTime primeiroDiaMesPassado = new DateTime(mesPassado.get(DateTimeFieldType.year()), mesPassado.get(DateTimeFieldType.monthOfYear()), 1, 0, 0, 0);
				DateTime primeiroDiaMesSeguinte = primeiroDiaMesPassado.plusMonths(1);

				List<Criterion> restricoes = new ArrayList<Criterion>();

				restricoes.add(Restrictions.between("dataHora", primeiroDiaMesPassado.toGregorianCalendar(), primeiroDiaMesSeguinte.toGregorianCalendar()));

				Integer quantidadeAcautelamentos = hibernateUtil.contar(movimentacaoDeArmamento, restricoes, MatchMode.EXACT);

				armamentosEQuantidades.get(armamento.getNumeracao()).add(quantidadeAcautelamentos);
				totalAcautelamentosArmamento += quantidadeAcautelamentos;

				String mesEAno = Util.mesAbreviado(mesPassado.get(DateTimeFieldType.monthOfYear())) + " " + mesPassado.get(DateTimeFieldType.year());

				if (!mesesEAnos.contains(mesEAno)) {

					mesesEAnos.add(mesEAno);
				}
			}

			armamentosMaisAcautelados.add(new RelatorioAcautelamentosAuxiliar(totalAcautelamentosArmamento, armamento.getNumeracao()));
		}

		if (Util.preenchido(armamentosMaisAcautelados)) {

			Collections.sort(armamentosMaisAcautelados, new Comparator<RelatorioAcautelamentosAuxiliar>() {

				public int compare(RelatorioAcautelamentosAuxiliar arg0, RelatorioAcautelamentosAuxiliar arg1) {

					Integer quantidade1 = arg0.getQuantidadeAcautelamentos();
					Integer quantidade2 = arg1.getQuantidadeAcautelamentos();

					return quantidade2.compareTo(quantidade1);
				}
			});
		}

		adicionarMesesEAnosNoResult(mesesEAnos);

		adicionarArmamentosEQuantidadesNoResult(armamentosEQuantidades, armamentosMaisAcautelados, quantidadeArmamentosMaisUtilizados);
	}

	private void adicionarArmamentosEQuantidadesNoResult(HashMap<String, List<Integer>> armamentosEQuantidades, List<RelatorioAcautelamentosAuxiliar> armamentosMaisAcautelados, Integer quantidadeArmamentosMaisUtilizados) {

		List<String> armamentosConsiderados = new ArrayList<String>();

		for (RelatorioAcautelamentosAuxiliar armamentoMaisAcautelado : armamentosMaisAcautelados) {

			if (armamentosConsiderados.size() < quantidadeArmamentosMaisUtilizados) {

				armamentosConsiderados.add(armamentoMaisAcautelado.getArmamento());
			}
		}

		StringBuffer armamentosEQuantidadesStringBuffer = new StringBuffer();

		for (Entry<String, List<Integer>> armamentoEQuantidades : armamentosEQuantidades.entrySet()) {

			if (armamentosConsiderados.contains(armamentoEQuantidades.getKey())) {

				armamentosEQuantidadesStringBuffer.append("{name:" + "'" + armamentoEQuantidades.getKey() + "',");
				armamentosEQuantidadesStringBuffer.append("data:[");

				for (Integer quantidade : armamentoEQuantidades.getValue()) {

					armamentosEQuantidadesStringBuffer.append(quantidade + ",");
				}

				armamentosEQuantidadesStringBuffer.append("]},");
			}
		}

		result.include("armamentosEQuantidades", armamentosEQuantidadesStringBuffer.toString());
	}

	private void adicionarMesesEAnosNoResult(List<String> mesesEAnos) {

		StringBuffer mesesEAnosStringBuffer = new StringBuffer();

		for (String mesEAno : mesesEAnos) {

			mesesEAnosStringBuffer.append("'" + mesEAno + "',");
		}

		result.include("mesesEAnos", mesesEAnosStringBuffer.toString());
	}
}
