<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/armamento/salvarArmamento"/>" method="post">
  <fieldset>
    <legend>Criar armamento</legend>
    <div class="control-group">
      <label class="control-label">Numeracao</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="armamento.numeracao" value="${armamento.numeracao}">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Status</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="armamento.status" value="${armamento.status}">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/armamento/listarArmamentos"/>" > Cancelar </a>
  </fieldset>
</form>
