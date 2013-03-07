<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/grupoOperador/salvarVinculosFuncionalidades"/>" method="post">
  <fieldset>
    <legend>Vincular funcionalidades ao grupo de operador</legend>
    	<table class="table table-striped" style="width: 500px; border-bottom: 2px; border-bottom-style: solid;">
		  <tbody>
		  	<c:forEach items="${funcionalidades}" var="item">
				<tr <c:if test="${!empty item.modulo}"> style="border-top: 2px; border-top-style: solid;" </c:if>  >
				  <td> <input type="checkbox" name="funcionalidadesSelecionadas" value="${item.codigo}" <c:if test="${funcionalidadesDoGrupoSelecionado.contains(item.codigo)}"> checked="checked" </c:if> /> </td>
				  <td> ${item.nomeFuncionalidade} </td>
				</tr>
		  	</c:forEach>  	
		  </tbody>
		</table>
    <button type="submit" class="btn">Salvar</button>
  </fieldset>
</form>
