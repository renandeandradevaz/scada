<%@ include file="/base.jsp" %> 

<ul id="armamento" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/armamento/editarArmamento"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/armamento/excluirArmamento"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/armamento/criarArmamento"/>" > Criar armamento </a>

<br><br>

<form class="well form-inline" action="<c:url value="/armamento/listarArmamentos"/>" method="post" >
    <input type="text" class="input-small" name="armamento.numeracao" value="${sessaoGeral.valor.get('armamento').numeracao}" placeholder="Numeracao">
    <input type="text" class="input-small" name="armamento.status" value="${sessaoGeral.valor.get('armamento').status}" placeholder="Status">
    <input type="text" class="input-small" name="armamento.tipoArmamento.descricao" value="${sessaoGeral.valor.get('armamento').tipoArmamento.descricao}" placeholder="Tipo de armamento" style="width: 130px;"> 

	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Armamentos </h3>

<c:choose>
	<c:when test="${!empty armamentos}">
		
		<c:set var="link" value="armamento/listarArmamentos" scope="request" />
		<%@ include file="/paginacao.jsp" %> 
		
		<table class="table table-striped table-bordered tablesorter">
			<thead>
		    	<tr>
                    <th> Numeracao </th>
                    <th> Status </th>
                    <th> Tipo de armamento </th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${armamentos}" var="item">
					<tr id="armamento_${item.id}">
                        <td> ${item.numeracao} </td>
                        <td> ${item.status} </td>
                        <td> ${item.tipoArmamento.descricao} </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<br>  <br>  <h4> Nenhum registro foi encontrado </h4>
	</c:otherwise>
</c:choose>
