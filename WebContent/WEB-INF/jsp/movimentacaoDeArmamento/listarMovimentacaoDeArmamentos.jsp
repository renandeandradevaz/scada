<%@ include file="/base.jsp" %> 
<%@ taglib uri="/tags/tags" prefix="tags"%>

<a class="btn" href="<c:url value="/movimentacaoDeArmamento/acautelarArmamentos"/>" > Acautelar armamentos </a>
<a class="btn" href="<c:url value="/movimentacaoDeArmamento/devolverArmamentos"/>" > Devolver armamentos </a>

<br><br>

<form class="well form-inline" action="<c:url value="/movimentacaoDeArmamento/listarMovimentacaoDeArmamentos"/>" method="post" >
	<select name="movimentacaoDeArmamento.tipoMovimentacao" >
		<option value="" style='display:none;' > Tipo de movimentação </option>
		<option value="" > Todos </option>
		<c:forEach items="${tiposMovimentacoes}" var="item">
			<option <c:if test="${sessaoGeral.valor.get('movimentacaoDeArmamento').tipoMovimentacao == item}"> selected="selected" </c:if> value="${item}"> ${item} </option>
		</c:forEach>
	</select>
    <input type="text" class="input-small" name="movimentacaoDeArmamento.armamento.numeracao" value="${sessaoGeral.valor.get('movimentacaoDeArmamento').armamento.numeracao}" placeholder="Armamento">
    <input type="text" class="input-small" name="movimentacaoDeArmamento.operador.nome" value="${sessaoGeral.valor.get('movimentacaoDeArmamento').operador.nome}" placeholder="Operador">
    <input type="text" class="input-small" name="movimentacaoDeArmamento.cliente.nome" value="${sessaoGeral.valor.get('movimentacaoDeArmamento').cliente.nome}" placeholder="Cliente">
    <input type="text" class="input-small data" name="filtrosMovimentacaoDeArmamento.dataInicial" value="<fmt:formatDate value="${sessaoGeral.valor.get('filtrosMovimentacaoDeArmamento').dataInicial.time}" />" placeholder="Data inicial">
    <input type="text" class="input-small data" name="filtrosMovimentacaoDeArmamento.dataFinal" value="<fmt:formatDate value="${sessaoGeral.valor.get('filtrosMovimentacaoDeArmamento').dataFinal.time}" />" placeholder="Data Final">

	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Movimentações de armamentos </h3>

<c:choose>
	<c:when test="${!empty movimentacaoDeArmamentos}">
		
		<c:set var="link" value="movimentacaoDeArmamento/listarMovimentacaoDeArmamentos" scope="request" />
		<%@ include file="/paginacao.jsp" %> 
		
		<table class="table table-striped table-bordered tablesorter">
			<thead>
		    	<tr>
                    <th> Tipo de movimentação </th>
                    <th> Armamento </th>
                    <th> Operador </th>
                    <th> Cliente </th>
                    <th> Data/Hora </th>
                    <th> Destino </th>
                    <th> Observações </th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${movimentacaoDeArmamentos}" var="item">
					<tr id="movimentacaoDeArmamento_${item.id}">
                        <td> ${item.tipoMovimentacao} </td>
                        <td> ${item.armamento.numeracao} </td>
                        <td> ${item.operador.postoGraduacao} ${item.operador.nome} </td>
                        <td> ${item.cliente.postoGraduacao} ${item.cliente.nome} </td>
                        <td> <fmt:formatDate value="${item.dataHora.time}" type="BOTH" /> </td>
                        <td> ${item.destino} </td>
                        <td> ${item.observacoes} </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<br>  <br>  <h4> Nenhum registro foi encontrado </h4>
	</c:otherwise>
</c:choose>
