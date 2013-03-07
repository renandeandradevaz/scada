<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/tipoArmamento/salvarTipoArmamento"/>" method="post">
  <fieldset>
    <legend>Criar tipoArmamento</legend>
    <div class="control-group">
      <label class="control-label">Descricao</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="tipoArmamento.descricao" value="${tipoArmamento.descricao}">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Calibre</label>
      <div class="controls">
        <input type="text" class="input-xlarge data" name="tipoArmamento.calibre" value="<fmt:formatDate value="${tipoArmamento.calibre.time}"/>">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/tipoArmamento/listarTipoArmamentos"/>" > Cancelar </a>
  </fieldset>
</form>
