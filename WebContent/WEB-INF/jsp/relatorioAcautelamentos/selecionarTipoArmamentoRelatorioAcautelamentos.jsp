<%@ include file="/base.jsp" %> 

<form style="width: 1300px;" class="form-inline" action="<c:url value="/relatorioAcautelamentos/gerarRelatorioAcautelamentos"/>" method="post">
  <fieldset>
    <legend>Acautelamentos por tipo de armamento</legend>
    
    <label> Tipo de armamento </label>
    
     <select name="tipoArmamentoSelecionado" >
		<c:forEach items="${tiposDeArmamento}" var="item">
			<option value="${item.id}"> ${item.descricao} - <fmt:formatNumber value="${item.calibre}" /></option>
		</c:forEach>
	</select>
	
	<label style="margin-left: 50px;" > Quantidade de meses </label>
	
	<input class="input-mini numero-inteiro" type="number" name="quantidadeMeses" min="1" max="12" value="6"  >
	
	<label style="margin-left: 50px;" > Quantidade de armamentos mais utilizados </label>
	
	<input class="input-mini numero-inteiro" type="number" name="quantidadeArmamentosMaisUtilizados" min="1" max="10" value="4"  >

    <button style="margin-left: 30px;" type="submit" class="btn btn-primary">Gerar relatório</button>

  </fieldset>
</form>
