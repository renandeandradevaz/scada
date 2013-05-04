<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/operador/salvarTrocaSenha"/>" method="post">
  <fieldset>
    <legend>Informe a nova senha</legend>
    
    <div class="control-group warning">
    	<label class="control-label">Senha</label>
    	<div class="controls">
    		<input type="password" class="input-xlarge required"  name="senha" value="" >
		</div>
    </div>
            
    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/operador/listarOperadores"/>" > Cancelar </a>
  </fieldset>
</form>