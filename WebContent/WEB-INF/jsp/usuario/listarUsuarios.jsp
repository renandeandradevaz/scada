<%@ include file="/base.jsp" %> 

<ul id="usuario" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/usuario/editarUsuario"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/usuario/excluirUsuario"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/usuario/criarUsuario"/>" > Criar usuário </a>

<br><br>

<form class="well form-inline" action="<c:url value="/usuario/listarUsuarios"/>" method="post" >
	<input type="text" class="input-small" name="usuario.login" value="${sessaoGeral.valor.get('usuario').login}" placeholder="Login">
	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Usuários </h3>

<c:set var="link" value="usuario/listarUsuarios" scope="request" />
<%@ include file="/paginacao.jsp" %> 

<table class="table table-striped table-bordered tablesorter">
  <thead>
    <tr>
      <th>Id</th>
      <th>Login</th>
      <th>Grupo de usuários</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${usuarios}" var="item">
		<tr id="usuario_${item.id}">
		  <td> ${item.id} </td>
		  <td> ${item.login} </td>
		  <td> ${item.grupoUsuario.nome} </td>
		</tr>
  	</c:forEach>  	
  </tbody>
</table>
