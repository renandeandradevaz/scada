<%@ include file="/base.jsp" %> 
<%@ include file="/layoutRelatorios.jsp" %>

<style>

.visto{
	border: 2px solid;
	width: 150px;
	height: 100px;
	position: absolute;
	display: none;
	margin-top: -150px;
}
.visto p{
	text-align: center;
	padding: 10px;
	font-size: 10px;
}
.visto hr{
	color: black;
	background-color: black;
	height: 1px;
	margin: 20px;
	margin-top: 35px;
}
	
.prontoArmamento{
	width: 100%;
	margin: auto;
}
h5, h6{
	text-align: center;
}
p{
	font-size: 13px;
	text-align: center;
}
table{
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

<div class="visto" >
	<p>Visto:</p>
	
	<hr>
	
	<p style="padding: 0px; margin-top: -20px;" >Cmt Cia</p>
	
</div>
	
<div class="prontoArmamento" >

	<h5>  PRONTO DO ARMAMENTO  </h5>
	
	<br>
	
	<p> Emitido em <fmt:formatDate value="${dataHoraAtual.time}" type="BOTH" /> por ${sessaoOperador.operador.postoGraduacao} ${sessaoOperador.operador.nome}  </p>
	
	<br>
	
	<h6> ACAUTELAMENTOS EM ABERTO </h6>
	
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
					<td><b>${acautelamentoAberto.quantidadeExistente}</b></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<br>
	
	<h6> ARMAMENTOS ACAUTELADOS </h6>
	
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
	                <td> ${item.cliente.postoGraduacao} ${item.cliente.nome} </td>
	                <td> ${item.armamento.subUnidade} </td>
	                <td> ${item.destino} </td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<br>
	
	<h6> RESUMO DAS MOVIMENTAÇÕES </h6>
	
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
