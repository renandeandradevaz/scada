<%@ include file="/base.jsp" %> 

<ul id="cliente" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/cliente/editarCliente"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/cliente/excluirCliente"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/cliente/criarCliente"/>" > Criar cliente </a>

<br><br>

<form class="well form-inline" action="<c:url value="/cliente/listarClientes"/>" method="post" >
    <input type="text" class="input-small" name="cliente.asfasf" value="${sessaoGeral.valor.get('cliente').asfasf}" placeholder="Asfasf">

	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Clientes </h3>

<c:choose>
	<c:when test="${!empty clientes}">
		
		<c:set var="link" value="cliente/listarClientes" scope="request" />
		<%@ include file="/paginacao.jsp" %> 
		
		<table class="table table-striped table-bordered tablesorter">
			<thead>
		    	<tr>
                    <th> Asfasf </th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${clientes}" var="item">
					<tr id="cliente_${item.id}">
                        <td> ${item.asfasf} </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<br>  <br>  <h4> Nenhum registro foi encontrado </h4>
	</c:otherwise>
</c:choose>
