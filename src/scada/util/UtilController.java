package scada.util;

import scada.sessao.SessaoGeral;

public class UtilController {

	public static Object preencherFiltros(Object object, String chave, SessaoGeral sessaoGeral) {

		if (Util.vazio(object) && Util.preenchido(sessaoGeral.getValor(chave))) {
			object = sessaoGeral.getValor(chave);
		}

		sessaoGeral.adicionar(chave, object);

		return object;
	}

}