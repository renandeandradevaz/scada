
jQuery(document).ready(function() {
	
	jQuery(".tablesorter").tablesorter();
	
	jQuery(".data").datepicker({
		
		dateFormat: 'dd/mm/yy',
		dayNames: [
		'Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado','Domingo'
		],
		dayNamesMin: [
		'D','S','T','Q','Q','S','S','D'
		],
		dayNamesShort: [
		'Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'
		],
		monthNames: [
		'Janeiro','Fevereiro','Marco','Abril','Maio','Junho','Julho','Agosto','Setembro',
		'Outubro','Novembro','Dezembro'
		],
		monthNamesShort: [
		'Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set',
		'Out','Nov','Dez'
		],
		nextText: 'Próximo',
		prevText: 'Anterior'

	});
	
	jQuery("#impressao").hover(
		function () {
			jQuery("#texto-imprimir").show();
         }, 
         function () {
        	 jQuery("#texto-imprimir").hide();
         }
     );	
	
	jQuery("#esconder-menu").click(function() {
		
		jQuery("#fixed-top").fadeOut("slow");
		jQuery("#mostrar-menu").show();
	});
	
	jQuery("#mostrar-menu").click(function() {
		
		jQuery("#fixed-top").fadeIn("slow");
		jQuery("#mostrar-menu").hide();
	});
	
	jQuery("#impressao").click(function() {
		
		jQuery("#fixed-top").hide();
		jQuery(".paginacao").hide();
		jQuery("form.well").hide();
		jQuery("form.form-inline").hide();
		jQuery("a.btn").hide();
		window.print();
		jQuery("#fixed-top").show();
		jQuery(".paginacao").show();
		jQuery("form.well").show();
		jQuery("form.form-inline").show();
		jQuery("a.btn").show();
	});

	jQuery(".numero-decimal").keypress(function(e) {

		var tecla = (window.event) ? event.keyCode : e.which;

		if ((tecla > 47 && tecla < 58)){

			return true;
		}
		
		if(tecla == 8 || tecla == 0){
			
			return true;
		}
		
		if(tecla == 44){
			
			if(this.value.indexOf(",") == -1){
				
				return true;					
			}
			else{
				return false;
			}				
		}
		
		else{
			return false;
		}
	});

	jQuery(".numero-inteiro").keypress(function(e) {

		var tecla = (window.event) ? event.keyCode : e.which;

		if ((tecla > 47 && tecla < 58))
			return true;
		else {
			if (tecla == 8 || tecla == 0)
				return true;

			else
				return false;
		}
	});
	
	jQuery(".data").keypress(function(e) {
		
		var tecla = (window.event) ? event.keyCode : e.which;
		
		if(tecla == 8){
			
			return true;			
		}

		return false;
	});
	
	jQuery(".tradutorSimNao").each(function() {
		
		var valor = jQuery(this).text();
		
		while(valor.indexOf(" ") != -1){

			valor = valor.replace(" ", "");
		}
		
		jQuery(this).empty();
		
		if(valor == "false" || valor == ""){
			
			jQuery(this).append("Não");
		}
		else{
			
			jQuery(this).append("Sim");
		}
	});
	
});

function deletar(link) {  
	
	if (confirm('Tem certeza que deseja excluir?')) {  
		gerarLinkCompleto(link);
	}  
}  