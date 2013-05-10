package scada.filtros;

import java.util.GregorianCalendar;

public class FiltrosMovimentacaoDeArmamento {

	private GregorianCalendar dataInicial;
	private GregorianCalendar dataFinal;

	public GregorianCalendar getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(GregorianCalendar dataInicial) {
		this.dataInicial = dataInicial;
	}

	public GregorianCalendar getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(GregorianCalendar dataFinal) {
		this.dataFinal = dataFinal;
	}

}
