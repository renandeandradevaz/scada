<%@ include file="/base.jsp" %> 

<ul id="armamento" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/armamento/editarArmamento"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/armamento/excluirArmamento"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/armamento/criarArmamento"/>" > Criar armamento </a>

<br><br>

<form class="well form-inline" action="<c:url value="/armamento/listarArmamentos"/>" method="post" >
    <input type="text" class="input-small" name="armamento.numeracao" value="${sessaoGeral.valor.get('armamento').numeracao}" placeholder="Numeração">
    <input type="text" class="input-small" name="armamento.tipoArmamento.descricao" value="${sessaoGeral.valor.get('armamento').tipoArmamento.descricao}" placeholder="Tipo de armamento" style="width: 130px;"> 
    <select name="armamento.status" >
		<option value="" style='display:none;' > Status </option>
		<option value="" > Todos </option>
		<c:forEach items="${statusArmamento}" var="item">
			<option <c:if test="${sessaoGeral.valor.get('armamento').status == item}"> selected="selected" </c:if> value="${item}"> ${item} </option>
		</c:forEach>
	</select>

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
                    <th> Tipo de armamento </th>
                    <th> Numeração </th>
                    <th> Subunidade </th>
                    <th> Status </th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${armamentos}" var="item">
					<tr id="armamento_${item.id}">
                        <td> ${item.tipoArmamento.descricao} - <fmt:formatNumber value="${item.tipoArmamento.calibre}" /> </td>
                        <td> ${item.numeracao} </td>
                        <td> ${item.subUnidade} </td>
                        <td> ${item.status} </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<br>  <br>  <h4> Nenhum registro foi encontrado </h4>
	</c:otherwise>
</c:choose>
