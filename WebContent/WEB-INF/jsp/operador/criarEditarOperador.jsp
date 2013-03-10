<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/operador/salvarOperador"/>" method="post">
  <fieldset>
    <legend>Criar/editar operador</legend>
    <div class="control-group warning">
      <label class="control-label">Nome</label>
      <div class="controls">
        <input type="text" class="input-xlarge required" name="operador.login" value="${operador.login}">
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Senha</label>
      <div class="controls">
        <input type="password" class="input-xlarge required"  name="operador.senha" value="" >
      </div>
    </div>    
    <div class="control-group">
        <label class="control-label">Grupo de operador</label>
        <div class="controls">
          <select name="operador.grupoOperador.id" >
			<c:forEach items="${gruposOperador}" var="item">
				<option <c:if test="${operador.grupoOperador.id == item.id}"> selected="selected" </c:if> value="${item.id}"> ${item.nome} </option>
			</c:forEach>
		  </select>
        </div>
    </div>
    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/operador/listarOperadores"/>" > Cancelar </a>
  </fieldset>
</form>