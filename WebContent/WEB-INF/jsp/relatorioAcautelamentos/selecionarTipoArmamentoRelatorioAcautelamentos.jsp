<%@ include file="/base.jsp" %> 

<form style="margin-top: -30px;" class="form-horizontal" action="<c:url value="/relatorioAcautelamentos/gerarRelatorioAcautelamentos"/>" method="post">
  <fieldset>
    <legend>Acautelamentos por tipo de armamento</legend>
    
    <div class="control-group">
      <label class="control-label">Tipo de armamento</label>
      <div class="controls">
        <select name="tipoArmamentoSelecionado" >
			<c:forEach items="${tiposDeArmamento}" var="item">
				<option value="${item.id}"> ${item.descricao} - <fmt:formatNumber value="${item.calibre}" /></option>
			</c:forEach>
		</select>
      </div>
    </div>
    
    <div class="control-group">
      <label class="control-label">Quantidade de meses</label>
      <div class="controls">
        <input class="input-mini numero-inteiro" type="number" name="quantidadeMeses" min="1" max="12" value="6"  >
      </div>
    </div>
    
    <div class="control-group">
      <label class="control-label">Quantidade de armamentos mais utilizados</label>
      <div class="controls">
		<input class="input-mini numero-inteiro" type="number" name="quantidadeArmamentosMaisUtilizados" min="1" max="10" value="4"  >
      </div>
    </div>
    
    <button type="submit" class="btn btn-primary">Gerar relatório</button>

  </fieldset>
</form>
