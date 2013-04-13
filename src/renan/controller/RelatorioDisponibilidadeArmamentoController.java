package renan.controller;

import org.hibernate.criterion.MatchMode;

import renan.anotacoes.Funcionalidade;
import renan.hibernate.HibernateUtil;
import renan.modelo.Armamento;
import renan.modelo.TipoArmamento;
import renan.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioDisponibilidadeArmamentoController {

	private Result result;
	private HibernateUtil hibernateUtil;

	public RelatorioDisponibilidadeArmamentoController(Result result, HibernateUtil hibernateUtil) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(nome = "Disponibilidade", modulo = "Relatórios")
	public void selecionarTipoArmamento() {

		result.include("tiposDeArmamento", hibernateUtil.buscar(new TipoArmamento()));
	}

	@Funcionalidade(filhaDe = "selecionarTipoArmamento")
	public void gerarRelatorio(Integer tipoArmamentoSelecionado) {

		TipoArmamento tipoArmamento = new TipoArmamento(tipoArmamentoSelecionado);

		Armamento armamento = new Armamento();
		armamento.setTipoArmamento(tipoArmamento);

		StringBuffer resultadoRelatorioDisponibilidade = new StringBuffer();

		armamento.setStatus(Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO);
		resultadoRelatorioDisponibilidade.append("[\'" + Armamento.ARMAMENTO_DISPONIVEL_ACAUTELADO + "\', " + String.valueOf(hibernateUtil.contar(armamento, MatchMode.EXACT)) + "],");

		armamento.setStatus(Armamento.ARMAMENTO_DISPONIVEL_NÃO_ACAUTELADO);
		resultadoRelatorioDisponibilidade.append("[\'" + Armamento.ARMAMENTO_DISPONIVEL_NÃO_ACAUTELADO + "\', " + String.valueOf(hibernateUtil.contar(armamento, MatchMode.EXACT)) + "],");

		armamento.setStatus(Armamento.ARMAMENTO_INDISPONIVEL_ACAUTELADO);
		resultadoRelatorioDisponibilidade.append("[\'" + Armamento.ARMAMENTO_INDISPONIVEL_ACAUTELADO + "\', " + String.valueOf(hibernateUtil.contar(armamento, MatchMode.EXACT)) + "],");

		armamento.setStatus(Armamento.ARMAMENTO_INDISPONIVEL_NÃO_ACAUTELADO);
		resultadoRelatorioDisponibilidade.append("[\'" + Armamento.ARMAMENTO_INDISPONIVEL_NÃO_ACAUTELADO + "\', " + String.valueOf(hibernateUtil.contar(armamento, MatchMode.EXACT)) + "],");

		result.include("resultadoRelatorioDisponibilidade", resultadoRelatorioDisponibilidade.toString());

		if (Util.preenchido(tipoArmamentoSelecionado)) {

			tipoArmamento = hibernateUtil.selecionar(tipoArmamento);

			result.include("tipoArmamentoSelecionado", tipoArmamento.getDescricao());
		}

		else {

			result.include("tipoArmamentoSelecionado", "Todos");
		}

	}
}
