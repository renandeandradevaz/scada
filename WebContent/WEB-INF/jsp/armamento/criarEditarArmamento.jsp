<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/armamento/salvarArmamento"/>" method="post">
  <fieldset>
    <legend>Criar/editar armamento</legend>
    <div class="control-group warning">
      <label class="control-label">Numeração</label>
      <div class="controls">
        <input type="text" class="input-xlarge required" name="armamento.numeracao" value="${armamento.numeracao}">
      </div>
    </div>
    <div class="control-group warning">
        <label class="control-label">Status</label>
        <div class="controls">
          <select name="armamento.status" >
			<c:forEach items="${statusArmamento}" var="item">
				<option <c:if test="${armamento.status == item}"> selected="selected" </c:if> value="${item}"> ${item} </option>
			</c:forEach>
		  </select>
        </div>
    </div>
    <div class="control-group warning">
        <label class="control-label">Tipo de armamento</label>
        <div class="controls">
          <select name="armamento.tipoArmamento.id" >
			<c:forEach items="${tiposDeArmamento}" var="item">
				<option <c:if test="${armamento.tipoArmamento.id == item.id}"> selected="selected" </c:if> value="${item.id}"> ${item.descricao} - <fmt:formatNumber value="${item.calibre}" /> </option>
			</c:forEach>
		  </select>
        </div>
    </div>
    
    
    <div class="control-group warning">
      <label class="control-label">Subunidade</label>
      <div class="controls">
        <input type="text" class="input-xlarge required" name="armamento.subUnidade" value="${armamento.subUnidade}">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/armamento/listarArmamentos"/>" > Cancelar </a>
  </fieldset>
</form>
