<%@ include file="/base.jsp" %> 

<ul id="grupoUsuario" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/grupoUsuario/vincularFuncionalidades"/>')">Vincular funcionalidades</a></li>
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/grupoUsuario/editarGrupoUsuario"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/grupoUsuario/excluirGrupoUsuario"/>')">Excluir</a></li>
</ul>

<a class="btn" href="<c:url value="/grupoUsuario/criarGrupoUsuario"/>" > Criar grupo de usuário </a>

<br><br>

<form class="well form-inline" action="<c:url value="/grupoUsuario/listarGruposUsuario"/>" method="post" >
	<input type="text" class="input-small" name="grupoUsuario.nome" value="${sessaoGeral.valor.get('grupoUsuario').nome}" placeholder="Nome">
	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Grupos de usuários </h3>

<c:set var="link" value="grupoUsuario/listarGruposUsuario" scope="request" />
<%@ include file="/paginacao.jsp" %> 

<table class="table table-striped table-bordered tablesorter">
  <thead>
    <tr>
      <th>Id</th>
      <th>Nome</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${gruposUsuario}" var="item">
		<tr id="grupoUsuario_${item.id}">
		  <td> ${item.id} </td>
		  <td> ${item.nome} </td>
		</tr>
  	</c:forEach>  	
  </tbody>
</table>
