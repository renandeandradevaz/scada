<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/cliente/salvarCliente"/>" method="post">
  <fieldset>
    <legend>Criar cliente</legend>
    <div class="control-group">
      <label class="control-label">Asfasf</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="cliente.asfasf" value="${cliente.asfasf}">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/cliente/listarClientes"/>" > Cancelar </a>
  </fieldset>
</form>
