<%@ include file="/base.jsp" %> 

<form class="form-inline" action="<c:url value="/relatorioDisponibilidadeArmamento/gerarRelatorio"/>" method="post">
  <fieldset>
    <legend>Selecione o tipo de armamento para gerar o relatório</legend>
    
     <select name="tipoArmamentoSelecionado" >
     	<option value=""> Todos </option>
		<c:forEach items="${tiposDeArmamento}" var="item">
			<option value="${item.id}"> ${item.descricao} </option>
		</c:forEach>
	</select>

    <button style="margin-left: 30px;" type="submit" class="btn btn-primary">Gerar relatório</button>

  </fieldset>
</form>
