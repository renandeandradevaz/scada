
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
		prevText: 'Anterior',
		changeMonth: true,
		changeYear: true

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
		jQuery("form.form-horizontal").hide();
		jQuery(".highcharts-tooltip").hide();
		jQuery("a.btn").hide();
		jQuery("#divCabecalhoRelatorios").show();
		jQuery(".visto").show();
		window.print();
		jQuery("#fixed-top").show();
		jQuery(".paginacao").show();
		jQuery("form.well").show();
		jQuery("form.form-inline").show();
		jQuery("form.form-horizontal").show();
		jQuery(".highcharts-tooltip").show();
		jQuery("a.btn").show();
		jQuery("#divCabecalhoRelatorios").hide();
		jQuery(".visto").hide();
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
	
	jQuery("#marcarDesmarcar").click(function() {
		
		if(jQuery("#marcarDesmarcar").attr("checked") === undefined){

			jQuery(".chekboxTabela").each( function() {
				
				jQuery(this).removeAttr("checked");
			});
		}
		
		else{
			
			jQuery(".chekboxTabela").each( function() {

				jQuery(this).attr("checked", "checked");
			});
		}
	});
	
});

function deletar(link) {  
	
	if (confirm('Tem certeza que deseja excluir?')) {  
		gerarLinkCompleto(link);
	}  
}  

function utf8_encode(string) { 
	 
    var
    str = string,
    specialChars = [
        {val:'a', let:'áàãâä'},
	{val:'e', let:'éèêë'},
	{val:'i', let:'íìîï'},
	{val:'o', let:'óòõôö'},
	{val:'u', let:'úùûü'},
	{val:'c', let:'ç'},
    ],
    regex;
 
    for (var i in  specialChars) {
	regex = new RegExp('[' + specialChars[i].let + ']', 'g');
	str = str.replace(regex, specialChars[i].val);
	regex = null;
    }
 
    return str;
};