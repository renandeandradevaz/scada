package scada.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.MatchMode;

import scada.anotacoes.Funcionalidade;
import scada.hibernate.HibernateUtil;
import scada.modelo.Configuracao;
import scada.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ConfiguracaoController {

	private final Result result;
	private HibernateUtil hibernateUtil;

	public ConfiguracaoController(Result result, HibernateUtil hibernateUtil) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(nome = "Configurações gerais", modulo = "Configurações")
	public void configuracoes() {

		List<Configuracao> configuracoes = hibernateUtil.buscar(new Configuracao());

		HashMap<String, String> hashConfiguracoes = new HashMap<String, String>();

		for (Configuracao configuracao : configuracoes) {

			hashConfiguracoes.put(configuracao.getChave(), configuracao.getValor());

		}

		HashMap<String, String> configuracoesPadroes = Configuracao.configuracoesPadroes();

		for (Entry<String, String> configuracaoPadrao : configuracoesPadroes.entrySet()) {

			if (!hashConfiguracoes.containsKey(configuracaoPadrao.getKey())) {

				hashConfiguracoes.put(configuracaoPadrao.getKey(), configuracaoPadrao.getValue());
			}
		}

		result.include("configuracoes", hashConfiguracoes);
	}

	@Funcionalidade(filhaDe = "configuracoes")
	public void salvarConfiguracoes(HashMap<String, String> configuracoes) {

		for (Entry<String, String> configuracaoEntrySet : configuracoes.entrySet()) {

			Configuracao configuracaoFiltro = new Configuracao();
			configuracaoFiltro.setChave(configuracaoEntrySet.getKey());

			Configuracao configuracao = hibernateUtil.selecionar(configuracaoFiltro, MatchMode.EXACT);

			if (Util.vazio(configuracao)) {

				configuracao = new Configuracao();

			}

			configuracao.setChave(configuracaoEntrySet.getKey());
			configuracao.setValor(configuracaoEntrySet.getValue());
			hibernateUtil.salvarOuAtualizar(configuracao);
		}

		result.include("sucesso", "Configurações salvas com sucesso");

		result.forwardTo(this).configuracoes();
	}

}
