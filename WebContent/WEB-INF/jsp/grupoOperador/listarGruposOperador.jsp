<%@ include file="/base.jsp" %> 

<ul id="grupoOperador" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/grupoOperador/vincularFuncionalidades"/>')">Vincular funcionalidades</a></li>
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/grupoOperador/editarGrupoOperador"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/grupoOperador/excluirGrupoOperador"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/grupoOperador/criarGrupoOperador"/>" > Criar grupo de operador </a>

<br><br>

<form class="well form-inline" action="<c:url value="/grupoOperador/listarGruposOperador"/>" method="post" >
	<input type="text" class="input-small" name="grupoOperador.nome" value="${sessaoGeral.valor.get('grupoOperador').nome}" placeholder="Nome">
	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Grupos de operadores </h3>

<c:set var="link" value="grupoOperador/listarGruposOperador" scope="request" />
<%@ include file="/paginacao.jsp" %> 

<table class="table table-striped table-bordered tablesorter">
  <thead>
    <tr>
      <th>Nome</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${gruposOperador}" var="item">
		<tr id="grupoOperador_${item.id}">
		  <td> ${item.nome} </td>
		</tr>
  	</c:forEach>  	
  </tbody>
</table>
