<%@ include file="/base.jsp" %> 

<ul id="operador" class="dropdown-menu">
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/operador/editarOperador"/>')">Editar</a></li>
	<li><a href="javascript:deletar('<c:url value="/operador/excluirOperador"/>')">Excluir</a></li>
	<li><a href="javascript:gerarLinkCompleto('<c:url value="/operador/trocarSenha"/>')">Trocar a senha</a></li>
</ul>

<a class="btn" href="<c:url value="/operador/criarOperador"/>" > Criar operador </a>

<br><br>

<form class="well form-inline" action="<c:url value="/operador/listarOperadores"/>" method="post" >
	
	<select name="operador.postoGraduacao" >
		<option value="" style='display:none;' >Posto/Graduacao</option>
		<option value="" > Todos </option>
		<c:forEach items="${graduacoes}" var="item">
			<option <c:if test="${sessaoGeral.valor.get('operador').postoGraduacao == item}"> selected="selected" </c:if> value="${item}"> ${item} </option>
		</c:forEach>
	</select>
	<input type="text" class="input-small" name="operador.nome" value="${sessaoGeral.valor.get('operador').nome}" placeholder="Nome">
	<input type="text" class="input-small" name="operador.identidade" value="${sessaoGeral.valor.get('operador').identidade}" placeholder="Identidade">
	<input type="text" class="input-small" name="operador.subUnidade" value="${sessaoGeral.valor.get('operador').subUnidade}" placeholder="Subunidade">
	<input type="text" class="input-medium" name="operador.grupoOperador.nome" value="${sessaoGeral.valor.get('operador').grupoOperador.nome}" placeholder="Grupo de Operadores">
			
	<button type="submit" class="btn btn-info">Pesquisar</button>
</form>

<h3> Operadores </h3>

<c:set var="link" value="operador/listarOperadores" scope="request" />
<%@ include file="/paginacao.jsp" %> 

<table class="table table-striped table-bordered tablesorter">
  <thead>
    <tr>
      <th>Nome</th>
      <th>Post/Grad</th>
      <th>Identidade</th>
      <th>Subunidade</th>
      <th>Grupo de operadores</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${operadores}" var="item">
		<tr id="operador_${item.id}">
		  <td> ${item.nome} </td>
		  <td> ${item.postoGraduacao} </td>
		  <td> ${item.identidade} </td>
		  <td> ${item.subUnidade} </td>
		  <td> ${item.grupoOperador.nome} </td>
		</tr>
  	</c:forEach>  	
  </tbody>
</table>
