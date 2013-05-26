<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/grupoOperador/salvarGrupoOperador"/>" method="post">
  <fieldset>
    <legend>Criar/editar grupo de operadores</legend>
    <div class="control-group warning">
      <label class="control-label">Nome</label>
      <div class="controls">
        <input type="text" class="input-xlarge required" name="grupoOperador.nome" value="${grupoOperador.nome}">
      </div>
    </div>
    
    <div class="control-group warning">
      <label class="control-label">Permissão especial</label>
        <div class="controls">
          <select name="grupoOperador.permissaoEspecial" >
			<option <c:if test="${grupoOperador.permissaoEspecial == true}"> selected="selected" </c:if> value="true"> Sim </option>
			<option <c:if test="${grupoOperador.permissaoEspecial == false}"> selected="selected" </c:if> value="false"> Não </option>
		  </select>
    	</div>
    </div>
    
    
    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/grupoOperador/listarGruposOperador"/>" > Cancelar </a>
  </fieldset>
</form>
