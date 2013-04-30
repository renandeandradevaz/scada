<%@ include file="/base.jsp" %> 
<%@ include file="/layoutRelatorios.jsp" %>

<style>
	
.prontoArmamento{
	width: 1200px;
	margin: auto;
}
h4, h5{
	text-align: center;
}
p{
	font-size: 15px;
	text-align: center;
}
table{
	width: 1000px !important;
	margin: auto;
}
table:hover {
	cursor: default;
}
.table tbody tr:hover td, .table tbody tr:hover th {
	background-color: white;
}
table.table-bordered{
	border-collapse: collapse;
}
td, th {
	background-color: white !important;
	text-align: center !important;
}

</style>

<div class="prontoArmamento" >

	<h4>  PRONTO DO ARMAMENTO  </h4>
	
	<br>
	
	<p> Emitido em <fmt:formatDate value="${dataHoraAtual.time}" type="BOTH" />  </p>
	
	<br>
	
	<h5> ACAUTELAMENTOS EM ABERTO </h5>
	
	<table class="table table-bordered">
		<thead>
			<tr>
				<th> Tipo de armamento </th>
				<th> Quantidade prevista </th>
				<th> Quantidade acautelada </th>
				<th> Quantidade existente </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="acautelamentoAberto" items="${acautelamentosAbertos}" >
				<tr>
					<td>${acautelamentoAberto.tipoArmamento.descricao} - <fmt:formatNumber value="${acautelamentoAberto.tipoArmamento.calibre}" /> </td>
					<td>${acautelamentoAberto.quantidadePrevista}</td>
					<td>${acautelamentoAberto.quantidadeAcautelada}</td>
					<td>${acautelamentoAberto.quantidadeExistente}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<br>
	
	<h5> ARMAMENTOS ACAUTELADOS </h5>
	
	<table class="table table-bordered">
		<thead>
	    	<tr>
	            <th> Tipo de armamento </th>
	            <th> Armamento </th>
	            <th> Cliente </th>
	            <th> SU </th>
	            <th> Destino </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${armamentosAcautelados}" var="item">
				<tr>
	                <td> ${item.armamento.tipoArmamento.descricao} - <fmt:formatNumber value="${item.armamento.tipoArmamento.calibre}" /> </td>
	                <td> ${item.armamento.numeracao} </td>
	                <td> ${item.cliente.nome} </td>
	                <td> ${item.armamento.subUnidade} </td>
	                <td> ${item.destino} </td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<br>
	
	<h5> RESUMO DAS MOVIMENTAÇÕES </h5>
	
	<table class="table table-bordered">
		<thead>
	    	<tr>
	            <th> Tipo de armamento </th>
	            <th> Porcentagem acautelada </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${resumoMovimentacoes}" var="item">
				<tr>
	                <td> ${item.tipoArmamento.descricao} - <fmt:formatNumber value="${item.tipoArmamento.calibre}" /> </td>
	                <td> <fmt:formatNumber value="${item.porcentagemAcautelada}" />%</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</div>
