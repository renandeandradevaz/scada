<%@ include file="/base.jsp" %> 


<form class="form-horizontal" action="<c:url value="/movimentacaoDeArmamento/salvarAcautelamentos"/>" method="post">
  <fieldset>
    <legend>Acautelar armamentos</legend>
    <div class="control-group warning">
		<label class="control-label">Cliente: </label>
		<div class="controls">
		  <input type="text" class="input-xxlarge required" id="cliente" name="nomeCliente" autofocus="autofocus"  >
		</div>
        
        <div id="armamentos" style="display: none">
        	<br>
			<label class="control-label">Armamentos: </label>
		</div>
		
		<div id="botoes" style="display: none">
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
            nomeCliente: request.term
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
	                label: item.numeracao,
	                value: item.numeracao
	              }
	            }));
	          }
	        });
	      },
	      minLength: 3,
	      select: function() {
	    	  
	    	  var quantidadesInputs = jQuery("#armamentos input").size();
	    	  
	    	  jQuery("#armamentos").append("<BR> <BR> <label class='control-label'> </label> <input type='text' class='input-xlarge' name='armamentosSelecionados' autofocus='autofocus' >");
	    	  
	    	  jQuery("#armamentos input:last").attr("id", "armamento_" + quantidadesInputs);
	    	  
	    	  montarAutoCompleteArmamento(quantidadesInputs);	    	  
	      }
	    });
  }
  
  </script>

