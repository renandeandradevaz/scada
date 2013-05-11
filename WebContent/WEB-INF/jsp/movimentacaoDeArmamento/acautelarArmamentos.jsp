<%@ include file="/base.jsp" %> 

<script >

function montarAutoCompleteArmamento(indice){
	  
	  jQuery( "#armamento_" + indice ).autocomplete({
	      source: function( request, response ) {
	        jQuery.ajax({
	          url: "<c:url value="/movimentacaoDeArmamento/autoCompleteArmamentos"/>",
	          dataType: "json",
	          data: {
	        	  numeracaoArmamento: request.term
	          },
	          success: function( data ) {
	        	  
	            response( jQuery.map( data.list, function( item ) {
	            	
	              return {
	                label: item.numeracao + " - " + item.tipoArmamento.descricao + " " + item.tipoArmamento.calibre,
	                value: item.numeracao
	              }
	            }));
	          }
	        });
	      },
	      minLength: 3,
	      select: function() {
	    	  
	    	  if(jQuery("#armamentos input:last").val() != ""){
	    		  
		    	  var quantidadesInputs = jQuery("#armamentos input").size();

		    	  jQuery("#armamentos").append("<BR> <BR> <label class='control-label'> </label> <input type='text' class='input-xlarge' name='armamentosSelecionados' autofocus='autofocus' >");
		    	  
		    	  jQuery("#armamentos input:last").attr("id", "armamento_" + quantidadesInputs);
		    	  
		    	  montarAutoCompleteArmamento(quantidadesInputs);	    	  
	    	  }
	      }
	 });
}
</script>

<form class="form-horizontal" action="<c:url value="/movimentacaoDeArmamento/salvarAcautelamentos"/>" method="post">
  <fieldset>
    <legend>Acautelar armamentos</legend>
    <div class="control-group warning">
		<label class="control-label">Destino </label>
		<div class="controls">
		  <input type="text" class="input-xlarge" required name="sessaoMovimentacao.destino" value="${sessaoMovimentacao.destino}" autofocus="autofocus"  >
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Observações </label>
		<div class="controls">
			<textarea rows="3" style="width: 270px;" name="sessaoMovimentacao.observacoes" > ${sessaoMovimentacao.observacoes} </textarea>
		</div>
	</div>
    <div class="control-group warning">
		<label class="control-label">Cliente </label>
		<div class="controls">
		  <input type="text" class="input-xxlarge required" id="cliente" name="sessaoMovimentacao.nomeCliente" value="${sessaoMovimentacao.nomeCliente}"  >
		</div>

        <c:set var="display" value="none" />
        <c:if test="${!empty sessaoMovimentacao.armamentosSelecionados}">
        	<c:set var="display" value="block" />
        </c:if>
        
        <div id="armamentos" style="display: ${display}">
        	<br>
			<label class="control-label">Armamentos: </label>
			
			<c:forEach items="${sessaoMovimentacao.armamentosSelecionados}" var="item" varStatus="i">
				
				<BR> <BR> <label class='control-label'> </label> <input type='text' id="armamento_${i.index}" class='input-xlarge' name='armamentosSelecionados' value="${item}" autofocus='autofocus' >
				
				<script>
					montarAutoCompleteArmamento(${i.index});	
				</script>
				
			</c:forEach>
		</div>
		
		<div id="botoes" style="display: ${display}">
			<br>
			<button type="submit" class="btn btn-primary"> Salvar </button>
			<a class="btn btn-danger" href="<c:url value="/movimentacaoDeArmamento/listarMovimentacaoDeArmamentos"/>" > Cancelar </a>
		</div>
    </div>
  </fieldset>
</form>

<script>
  jQuery(function() {
 
    jQuery( "#cliente" ).autocomplete({
      source: function( request, response ) {
        jQuery.ajax({
          url: "<c:url value="/movimentacaoDeArmamento/autoCompleteClientes"/>",
          dataType: "json",
          data: {
            nomeCliente: utf8_encode(request.term)
          },
          success: function( data ) {
        	  
            response( jQuery.map( data.list, function( item ) {
            	
              return {
                label: item.nome,
                value: item.nome
              }
            }));
          }
        });
      },
      minLength: 3,
      select: function() {
    	  
    	 if(jQuery("#armamentos input").size() == 0){
    		 
    	  jQuery("#armamentos").fadeIn();
    	  jQuery("#botoes").fadeIn();
    	  
    	  jQuery("#armamentos").append("<BR> <BR> <label class='control-label'> </label> <input id='armamento_0' type='text' class='input-xlarge' name='armamentosSelecionados' autofocus='autofocus' >");
    	  
    	  montarAutoCompleteArmamento(0);
    	 }
      }
    });
  });
</script>

