<%@ include file="/base.jsp" %> 

<form class="form-horizontal" action="<c:url value="/configuracao/salvarConfiguracoes"/>" method="post">
  <fieldset>
    <legend>Configurações gerais</legend>
    
    <div class="control-group warning">
      <label class="control-label configuracao">Quantidade de registros por página</label>
      <div class="controls">
        <input type="text" class="input-medium required numero-inteiro" name="configuracoes(quantidadeRegistrosPorPagina)" value="${configuracoes.get('quantidadeRegistrosPorPagina')}">
      </div>
    </div>

	<br><br>
    <button type="submit" class="btn btn-primary">Salvar</button>
  </fieldset>
</form>
