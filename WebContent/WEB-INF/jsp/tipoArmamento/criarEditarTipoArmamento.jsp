<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/tipoArmamento/salvarTipoArmamento"/>" method="post">
  <fieldset>
    <legend>Criar/editar tipo de armamento</legend>
    <div class="control-group warning">
      <label class="control-label">Descrição</label>
      <div class="controls">
        <input type="text" class="input-xlarge required" name="tipoArmamento.descricao" value="${tipoArmamento.descricao}">
      </div>
    </div>
    <div class="control-group warning">
      <label class="control-label">Calibre</label>
      <div class="controls">
        <input type="text" class="input-xlarge numero-decimal required" name="tipoArmamento.calibre" value="<fmt:formatNumber value="${tipoArmamento.calibre}"/>">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/tipoArmamento/listarTipoArmamentos"/>" > Cancelar </a>
  </fieldset>
</form>
