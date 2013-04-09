<%@ include file="/base.jsp" %> 

<ul id="tipoArmamento" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/tipoArmamento/editarTipoArmamento"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/tipoArmamento/excluirTipoArmamento"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/tipoArmamento/criarTipoArmamento"/>" > Criar tipo de armamento </a>

<br><br>

<form class="well form-inline" action="<c:url value="/tipoArmamento/listarTipoArmamentos"/>" method="post" >
    <input type="text" class="input-small" name="tipoArmamento.descricao" value="${sessaoGeral.valor.get('tipoArmamento').descricao}" placeholder="Descrição">
    <input type="text" class="input-small numero-decimal" name="tipoArmamento.calibre" value="<fmt:formatNumber value="${sessaoGeral.valor.get('tipoArmamento').calibre}" />" placeholder="Calibre">

	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Tipos de armamentos </h3>

<c:choose>
	<c:when test="${!empty tipoArmamentos}">
		
		<c:set var="link" value="tipoArmamento/listarTipoArmamentos" scope="request" />
		<%@ include file="/paginacao.jsp" %> 
		
		<table class="table table-striped table-bordered tablesorter">
			<thead>
		    	<tr>
                    <th> Descrição </th>
                    <th> Calibre </th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tipoArmamentos}" var="item">
					<tr id="tipoArmamento_${item.id}">
                        <td> ${item.descricao} </td>
                        <td> <fmt:formatNumber value="${item.calibre}" /> </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<br>  <br>  <h4> Nenhum registro foi encontrado </h4>
	</c:otherwise>
</c:choose>
