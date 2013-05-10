<%@ include file="/base.jsp" %> 
<%@ taglib uri="/tags/tags" prefix="tags"%>

<ul id="mohoidfgoih98745oihdog" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/mohoidfgoih98745oihdog/editarOIAHPSODFIH349823OISHFD"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/mohoidfgoih98745oihdog/excluirOIAHPSODFIH349823OISHFD"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/mohoidfgoih98745oihdog/criarOIAHPSODFIH349823OISHFD"/>" > Criar mohoidfgoih98745oihdog </a>

<br><br>

<form class="well form-inline" action="<c:url value="/mohoidfgoih98745oihdog/listarOIAHPSODFIH349823OISHFDs"/>" method="post" >
<!-- Campos de pesquisa aqui -->
	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> OIAHPSODFIH349823OISHFDs </h3>

<c:choose>
	<c:when test="${!empty mohoidfgoih98745oihdogs}">
		
		<c:set var="link" value="mohoidfgoih98745oihdog/listarOIAHPSODFIH349823OISHFDs" scope="request" />
		<%@ include file="/paginacao.jsp" %> 
		
		<table class="table table-striped table-bordered tablesorter">
			<thead>
		    	<tr>
 <!-- Th label aqui -->
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${mohoidfgoih98745oihdogs}" var="item">
					<tr id="mohoidfgoih98745oihdog_${item.id}">
 <!-- Td valor aqui -->
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<br>  <br>  <h4> Nenhum registro foi encontrado </h4>
	</c:otherwise>
</c:choose>