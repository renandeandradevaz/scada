/* Js que servirá para esconder os botões que o operador não tem permissão de ver. */

jQuery(document).ready(function( jQuery ){
	
	var codigosFuncionalidadesPermitidas = jQuery('#codigosFuncionalidadesPermitidas').val();	
	codigosFuncionalidadesPermitidas = codigosFuncionalidadesPermitidas.split(",");
	
	jQuery(".btn").each(function() {
			
		var codigoFuncionalidade = jQuery(this).attr("href");
		
		if(codigoFuncionalidade != null){
		
			codigoFuncionalidade = codigoFuncionalidade.split("/");
			
			codigoFuncionalidade = codigoFuncionalidade[2] + "/" + codigoFuncionalidade[3].split("'")[0];
	
			if(jQuery.inArray(codigoFuncionalidade, codigosFuncionalidadesPermitidas) == -1){
				
				jQuery(this).hide();
			}
		}
	});
});