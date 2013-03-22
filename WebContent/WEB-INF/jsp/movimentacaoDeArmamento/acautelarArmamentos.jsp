<%@ include file="/base.jsp" %> 


<form class="form-horizontal" action="<c:url value="/armamento/salvarArmamento"/>" method="post">
  <fieldset>
    <legend>Acautelar armamentos</legend>
    <div class="control-group warning">
      <label class="control-label">Cliente: </label>
      <div class="controls">
        <input type="text" class="input-xlarge required" id="cliente" >
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
      minLength: 3
    });
  });
  </script>

