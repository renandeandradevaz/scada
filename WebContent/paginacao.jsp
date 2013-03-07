<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value="/js/jquery.paginate.js"/>"></script>
<link type="text/css" href="<c:url value="/css/paginacao.css"/>" rel="stylesheet" />

<style>
   .paginacao {
      font-family: verdana;
      padding:10px 0px 0px 0px;
      margin:0px;
      letter-spacing:1px;
    }
    #info-registros{
    	font-family: serif;
    	font-size: 11px;
    	font-weight: bold;
    }
</style>

<c:set var="link" value="${requestScope.link}" />

<form name="form" action="<c:url value="/${link}"/>" method="post">
	<input id="pagina" type="hidden" name="pagina">
</form>

<div class="paginacao">
	<div id="paginacao"></div> <p id="info-registros"> Exibindo ${primeiroRegistroPagina} a ${ultimoRegistroPagina} de ${quantidadeRegistros} </p>
</div>

<br>

<script type="text/javascript">
$(function() {
	
	quantidadePaginas = ${quantidadePaginas};
	paginaAtual = ${pagina};
	
	$("#paginacao").paginate({
		count 		: quantidadePaginas,
		start 		: paginaAtual,
		border					: false,
		text_color  			: '#888',
		background_color    	: '#EEE',	
		text_hover_color  		: 'black',
		background_hover_color	: '#CFCFCF',
		mouse					: 'press',
		onChange     			: 
			
		function(page){
			
			$('#pagina').val(page);
			document.form.submit();
		}
	});
});
</script>