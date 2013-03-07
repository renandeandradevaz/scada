<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/usuario/salvarUsuario"/>" method="post">
  <fieldset>
    <legend>Criar usuário</legend>
    <div class="control-group warning">
      <label class="control-label">Nome</label>
      <div class="controls">
        <input type="text" class="input-xlarge required" name="usuario.login" value="${usuario.login}">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Senha</label>
      <div class="controls">
        <input type="password" class="input-xlarge required"  name="usuario.senha" value="" >
      </div>
    </div>    
    <div class="control-group">
        <label class="control-label">Grupo de usuário</label>
        <div class="controls">
          <select name="usuario.grupoUsuario.id" >
			<c:forEach items="${gruposUsuario}" var="item">
				<option <c:if test="${usuario.grupoUsuario.id == item.id}"> selected="selected" </c:if> value="${item.id}"> ${item.nome} </option>
			</c:forEach>
		  </select>
        </div>
    </div>
    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/usuario/listarUsuarios"/>" > Cancelar </a>
  </fieldset>
</form>