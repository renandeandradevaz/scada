/* Variável global que guardará o id do elemento clicado. */
id = 0;

/*
 * Gera o link completo concatenando o link recebido 
 * como parâmetro com o id do elemento selecionado.
 */
function gerarLinkCompleto(link){
	document.location.href= link + "/" + id; 
}

/*
 * Pega do input hidden que está na base.jsp as permissões que o operador logado tem e esconde do submenu as que ele não pode ver.
 */
jQuery(document).ready(function( $ ){
	
	var codigosFuncionalidadesPermitidas = $('#codigosFuncionalidadesPermitidas').val();	
	codigosFuncionalidadesPermitidas = codigosFuncionalidadesPermitidas.split(",");
	
	for(var i = 0 ; i < $('.dropdown-menu li').length ; i++){
		
		var codigoFuncionalidade = $('.dropdown-menu').children('li').eq(i).children('a').attr('href');
		
		codigoFuncionalidade = codigoFuncionalidade.split("/");
		
		codigoFuncionalidade = codigoFuncionalidade[2] + "/" + codigoFuncionalidade[3].split("'")[0];
		
		$('.dropdown-menu').children('li').eq(i).attr("id", codigoFuncionalidade);

		if(jQuery.inArray(codigoFuncionalidade, codigosFuncionalidadesPermitidas) == -1){
			
			codigoFuncionalidade = codigoFuncionalidade.split("/");
			var codigoAntesDaBarra = codigoFuncionalidade[0];
			var codigoDepoisDaBarra = codigoFuncionalidade[1];
			
			$('li[id^=' + codigoAntesDaBarra + ']li[id$=' + codigoDepoisDaBarra + ']').hide();
		}
	}
	
});

jQuery(document).ready(function( $ ){
	
	/* Seleciona todos os submenus da tela e os esconde */
	var subMenus = $('.dropdown-menu');
	subMenus.hide();

	/* Esconde todos os submenus quando é clicado qualquer elemento que não seja
	 * um elemento que deva mostrar um submenu. 
	*/
	$('body').click(function(){
		subMenus.hide();
	});
	
	$(function(){
	    $('tr').click(function(e){
	    	
	    	/* Pego o valor do id do tr clicado */
	        id = $(this).attr('id');
	        e.stopPropagation();
	        subMenus.hide();
	        
	       if(id != null){

	    	   /* Pego o id da entidade e também o id do submenu */
	    	   var posicaoUnderline = id.indexOf("_");
	    	   var idSubMenu = '#' + id.substring(0, posicaoUnderline);
	    	   id = id.substring(posicaoUnderline + 1);
	    	   
	    	   /* Atribuo propriedades css ao submenu para aparecer ao lado do ponteiro do mouse */
	    	   $(idSubMenu).css("left", e.pageX);
	    	   $(idSubMenu).css("top", e.pageY - 55);
	    	   $(idSubMenu).css("display", "block");
	    	   
	    	   /* Mostro o submenu */
	    	   $(idSubMenu).show();
	    	   
	    	   /* Se o submenu não tiver nenhum li visível, eu o escondo */
	    	   if ($(idSubMenu + ' li:visible').length == 0)
	    	   {
	    		   $(idSubMenu).hide();
	    	   }
	       }
	    });
	});	
});