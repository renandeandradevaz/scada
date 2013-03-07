<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/movimentacaoDeArmamento/salvarMovimentacaoDeArmamento"/>" method="post">
  <fieldset>
    <legend>Criar movimentacaoDeArmamento</legend>
    <div class="control-group">
      <label class="control-label">TipoMovimentacao</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="movimentacaoDeArmamento.tipoMovimentacao" value="${movimentacaoDeArmamento.tipoMovimentacao}">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">DataHora</label>
      <div class="controls">
        <input type="text" class="input-xlarge data" name="movimentacaoDeArmamento.dataHora" value="<fmt:formatDate value="${movimentacaoDeArmamento.dataHora.time}"/>">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Destino</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="movimentacaoDeArmamento.destino" value="${movimentacaoDeArmamento.destino}">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Observacoes</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="movimentacaoDeArmamento.observacoes" value="${movimentacaoDeArmamento.observacoes}">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Validado</label>
      <div class="controls">
        <input type="checkbox" <c:if test="${movimentacaoDeArmamento.validado}"> checked="checked" </c:if> class="input-xlarge" name="movimentacaoDeArmamento.validado">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/movimentacaoDeArmamento/listarMovimentacaoDeArmamentos"/>" > Cancelar </a>
  </fieldset>
</form>
