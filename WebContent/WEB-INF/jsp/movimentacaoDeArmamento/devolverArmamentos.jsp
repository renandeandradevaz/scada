<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/movimentacaoDeArmamento/salvarDevolucoes"/>" method="post">
  <fieldset>
    <legend>Devolu��o de armamentos acautelados</legend>
	<c:choose>
		<c:when test="${!empty movimentacoes}">
			
			<table class="table table-striped table-bordered">
				<thead>
			    	<tr>
	                    <th> <input type="checkbox" id="marcarDesmarcar" class="chekboxTabela" /> Armamento </th>
	                    <th> Status </th>
	                    <th> Tipo de armamento </th>
	                    <th> Operador  </th>
	                    <th> Cliente </th>
	                    <th> Data/Hora </th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${movimentacoes}" var="item">
						<tr>
	                        <td> <input type="checkbox" name="idsAcautelamentos" value="${item.id}" class="chekboxTabela" >  ${item.armamento.numeracao} </td>
	                        <td> ${item.armamento.status} </td>
	                        <td> ${item.armamento.tipoArmamento.descricao} - <fmt:formatNumber value="${item.armamento.tipoArmamento.calibre}" /> </td>
	                        <td> ${item.operador.nome} </td>
	                        <td> ${item.cliente.nome} </td>
	                        <td> <fmt:formatDate value="${item.dataHora.time}" type="BOTH" /> </td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<br>  <br>  <h4> Nenhum registro foi encontrado </h4>
		</c:otherwise>
	</c:choose>

    <button type="submit" class="btn">Devolver</button>
  </fieldset>
</form>


