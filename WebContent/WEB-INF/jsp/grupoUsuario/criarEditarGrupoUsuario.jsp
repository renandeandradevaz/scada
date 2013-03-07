<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/grupoUsuario/salvarGrupoUsuario"/>" method="post">
  <fieldset>
    <legend>Criar grupo de usuários</legend>
    <div class="control-group warning">
      <label class="control-label">Nome</label>
      <div class="controls">
        <input type="text" class="input-xlarge required" name="grupoUsuario.nome" value="${grupoUsuario.nome}">
      </div>
    </div>
    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/grupoUsuario/listarGruposUsuario"/>" > Cancelar </a>
  </fieldset>
</form>
