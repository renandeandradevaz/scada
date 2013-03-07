<%@ include file="/base.jsp" %> 

<ul id="operador" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/operador/editarOperador"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/operador/excluirOperador"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/operador/criarOperador"/>" > Criar operador </a>

<br><br>

<form class="well form-inline" action="<c:url value="/operador/listarOperadores"/>" method="post" >
	<input type="text" class="input-small" name="operador.login" value="${sessaoGeral.valor.get('operador').login}" placeholder="Login">
	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Operadores </h3>

<c:set var="link" value="operador/listarOperadores" scope="request" />
<%@ include file="/paginacao.jsp" %> 

<table class="table table-striped table-bordered tablesorter">
  <thead>
    <tr>
      <th>Id</th>
      <th>Login</th>
      <th>Grupo de operadores</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${operadores}" var="item">
		<tr id="operador_${item.id}">
		  <td> ${item.id} </td>
		  <td> ${item.login} </td>
		  <td> ${item.grupoOperador.nome} </td>
		</tr>
  	</c:forEach>  	
  </tbody>
</table>
