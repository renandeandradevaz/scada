package renan.controller;

import java.util.List;

import renan.anotacoes.Funcionalidade;
import renan.modelo.Cliente;
import renan.hibernate.HibernateUtil;
import renan.sessao.SessaoGeral;
import renan.util.Util;
import renan.util.UtilController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ClienteController {

	private final Result result;
	private SessaoGeral sessaoGeral;
	private HibernateUtil hibernateUtil;

	public ClienteController(Result result, SessaoGeral sessaoGeral, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoGeral = sessaoGeral;
		this.hibernateUtil = hibernateUtil;
		this.hibernateUtil.setResult(result);
	}

	@Funcionalidade(filhaDe = "criarEditarCliente")
	public void criarCliente() {

		sessaoGeral.adicionar("idCliente", null);
		result.forwardTo(this).criarEditarCliente();
	}

	@Path("/cliente/editarCliente/{cliente.id}")
	@Funcionalidade(filhaDe = "criarEditarCliente")
	public void editarCliente(Cliente cliente) {

		cliente = hibernateUtil.selecionar(cliente);

		sessaoGeral.adicionar("idCliente", cliente.getId());
		result.include(cliente);
		result.forwardTo(this).criarEditarCliente();
	}

	@Funcionalidade(nome = "Criar e editar clientes")
	public void criarEditarCliente() {
	}

	@Path("/cliente/excluirCliente/{cliente.id}")
	@Funcionalidade(nome = "Excluir cliente")
	public void excluirCliente(Cliente cliente) {

		hibernateUtil.deletar(cliente);
		result.include("sucesso", "Cliente excluído(a) com sucesso");
		result.forwardTo(this).listarClientes(null, null);
	}

	@Funcionalidade(filhaDe = "criarEditarCliente")
	public void salvarCliente(Cliente cliente) {

		if (Util.preenchido(sessaoGeral.getValor("idCliente"))) {

			cliente.setId((Integer) sessaoGeral.getValor("idCliente"));
		}

		hibernateUtil.salvarOuAtualizar(cliente);
		result.include("sucesso", "Cliente salvo(a) com sucesso");
		result.forwardTo(this).listarClientes(new Cliente(), null);
	}

	@Funcionalidade(nome = "Clientes", modulo = "New")
	public void listarClientes(Cliente cliente, Integer pagina) {

		cliente = (Cliente) UtilController.preencherFiltros(cliente, "cliente", sessaoGeral);
		if (Util.vazio(cliente)) {
			cliente = new Cliente();
		}

		List<Cliente> clientes = hibernateUtil.buscar(cliente, pagina);
		result.include("clientes", clientes);

	}
}
