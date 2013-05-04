<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML>
<html lang="pt-br">
	<head>
		<title> SCADA - Sistema de controle de acautelamentos e disponibilidade de armamentos </title>
		<link type="text/css" href="<c:url value="/css/bootstrap.css"/>" rel="stylesheet" />
		<link type="text/css" href="<c:url value="/css/bootstrap-alterado.css"/>" rel="stylesheet" />
		<link type="text/css" href="<c:url value="/css/bootstrap-responsive.css"/>" rel="stylesheet" />
		<link type="text/css" href="<c:url value="/css/estilo.css"/>" rel="stylesheet" />
		<link type="text/css" href="<c:url value="/css/menu.css"/>" rel="stylesheet" />
		<link type="text/css" href="<c:url value="/css/jquery-ui-1.9.2.custom.css"/>" rel="stylesheet" />
		<script type="text/javascript" src="<c:url value="/js/jquery-1.7.2.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.9.2.custom.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/jquery.validate.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/jquery.tablesorter.min.js"/>"></script>
		<script type="text/javascript" charset="utf-8" src="<c:url value="/js/scripts.gerais.js"/>"></script>			
		<script type="text/javascript" src="<c:url value="/js/menu.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/submenu.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/botao.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/form.requerido.js"/>"></script>	
				
	</head>
	
	<noscript>
		<meta http-equiv="Refresh" content="0;url=<c:url value="/javascriptDesabilitado.jsp"/>">
	</noscript>

	 <body data-spy="scroll" data-target=".subnav" data-offset="50" style="position: relative;">
	 
	 	<div id="mostrar-menu" class="esconder-mostrar-menu">
	 		<p> Exibir </p> 
	 	</div>

		<div id="fixed-top" class="navbar navbar-inverse navbar-fixed-top" >
			<div style="background: black; box-shadow: 5px 5px 10px grey;" >
				<div id="menu">
				    <ul class="menu">
				        <li><a href="" class="parent"><span>Menu</span></a>
				            <ul>
				            	<c:forEach var="modulo" items="${sessaoFuncionalidades.modulos}">
						        	<li><a href="" class="parent"><span>${modulo.key}</span></a>
			                            <ul>
			                            	<c:forEach var="item" items="${modulo.value}">
			                                	<li><a href="<c:url value="/${item.codigo}"/>"><span>${item.nomeFuncionalidade}</span></a></li>
			                            	</c:forEach>
			                            </ul>
				                	</li>
				            	</c:forEach>
				            </ul>
				        </li>
				        <li><a href="<c:url value="/home/sobre"/>" class="last"><span>Sobre</span></a>	        
				    </ul>
				    <ul class="menu-operador" >
				    	<li><span> Logado como: ${sessaoOperador.operador.postoGraduacao} ${sessaoOperador.operador.nome} &nbsp; </span> </li>
				    </ul>
				</div>
			</div>
			
			<div id="esconder-menu" class="esconder-mostrar-menu">
				<p> Esconder </p> 
			</div>
			
			<div id="impressao">
				<div id="icone-impressao">
					<i class="icon-print"></i>
				</div>
				<div id="texto-imprimir">
					<p> Imprimir </p>
				</div>
			</div>
			
			<a id="sair" style="float: right; padding-right: 15px; font-weight: bold; margin-top: 5px; font-size: 12px; cursor: pointer; " href="<c:url value="/login/logout"/>"> Sair </a>
			<a style="float: right; padding-right: 15px; font-weight: bold; margin-top: 5px; font-size: 12px; cursor: pointer; " href="<c:url value="/login/trocarPropriaSenha"/>"> Trocar senha </a>
		</div>		
		
		<!--
		 Input hidden para guardar as informações das funcionalidades que o operador logado pode acessar.
		Será usado para montar o submenu somente com as opções que o operador pode enxergar
		 -->
		<input id="codigosFuncionalidadesPermitidas" type="hidden" value="${sessaoFuncionalidades.codigosFuncionalidadesSeparadosPorVirgula}" >

		<a style="display: none;" href="http://apycom.com/"></a>
		
		<div id="divconteudo" style="margin-left: 30px; margin-right: 30px; margin-bottom: 100px; margin-top: 75px;" >
		
		<c:if test="${not empty sucesso}">
			<div class="alert alert-success">
				${sucesso}
			</div>
		</c:if>
		
		<c:if test="${not empty errors}">
			<div class="alert alert-error">
				<c:forEach items="${errors }" var="error">
					<strong>${error.category }</strong> - ${error.message } <br>
				</c:forEach>
			</div>
		</c:if>
		
 		<br>