<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/login/salvarTrocarPropriaSenha"/>" method="post">
  <fieldset>
    <legend>Troca de senha</legend>
    
    <div class="control-group warning">
    	<label class="control-label">Senha antiga</label>
    	<div class="controls">
    		<input type="password" class="input-xlarge required" name="senhaAntiga"  >
		</div>
    </div>
    
    <div class="control-group warning">
    	<label class="control-label">Nova senha</label>
    	<div class="controls">
    		<input type="password" class="input-xlarge required" name="senhaNova"  >
		</div>
    </div>
            
    <button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-danger" href="<c:url value="/home/home"/>" > Cancelar </a>
  </fieldset>
</form>