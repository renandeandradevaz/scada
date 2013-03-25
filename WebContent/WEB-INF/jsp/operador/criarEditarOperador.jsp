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
    
	<div class="control-group warning">
        <label class="control-label">Posto/Graduação</label>
        <div class="controls">
          <select>
          		<option> Coronel </option>
          		<option> Tenente-Coronel </option>
          		<option> Major </option>
          		<option> Capitão </option>
				<option> 1º Tenente</option>
				<option> 2º Tenente</option>
				<option> Subtenente </option>
				<option> 1º Sargento </option>
				<option> 2º Sargento </option>
				<option> 3º Sargento </option>
				<option> Cabo </option>
				<option> Soldado </option>
		  </select>
       </div>
     </div>
    
    <div class="control-group">
      <label class="control-label">Identidade</label>
      <div class="controls">
        <input type="text" class="input-xlarge required"  name="operador.identidade" value="${operador.identidade}" >
      </div>
    </div>
    
    <div class="control-group">
      <label class="control-label">Subunidade</label>
      <div class="controls">
        <input type="text" class="input-xlarge required"  name="operador.subUnidade" value="${operador.subUnidade}" >
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