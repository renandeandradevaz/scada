<%@ include file="/base.jsp" %> 
<%@ include file="/layoutRelatorios.jsp" %>

<form style="width: 1300px;" class="form-inline" action="<c:url value="/relatorioAcautelamentos/gerarRelatorioAcautelamentos"/>" method="post">
  <fieldset>
    <legend>Acautelamentos por tipo de armamento</legend>
    
    <label> Tipo de armamento </label>
    
     <select name="tipoArmamentoSelecionado" >
		<c:forEach items="${tiposDeArmamento}" var="item">
			<option value="${item.id}"> ${item.descricao} - <fmt:formatNumber value="${item.calibre}" /></option>
		</c:forEach>
	</select>
	
	<label style="margin-left: 50px;" > Quantidade de meses </label>
	
	<input class="input-mini" type="number" name="quantidadeMeses" min="1" max="12" value="6"  >
	
	<label style="margin-left: 50px;" > Quantidade de armamentos mais utilizados </label>
	
	<input class="input-mini" type="number" name="quantidadeArmamentosMaisUtilizados" min="1" max="10" value="4"  >

    <button style="margin-left: 30px;" type="submit" class="btn btn-primary">Gerar relatório</button>

  </fieldset>
</form>
<br>

<script type="text/javascript">
jQuery(function () {
    jQuery('#divrelatorio').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: 'Acautelamentos por tipo de armamento'
        },
        subtitle: {
            text: 'Tipo de armamento selecionado: ${tipoArmamentoSelecionado} '
        },
        xAxis: {
            categories: [${mesesEAnos}]
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Quantidade de acautelamentos'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [${armamentosEQuantidades}]
    });
    
    jQuery("tspan:last").hide();
});
    
</script>

<script type="text/javascript" charset="utf-8" src="<c:url value="/js/highcharts.js"/>"></script>

<div style="width: 1200px;" >
	<div id="divrelatorio" style="min-width: 1200px; height: 600px;"></div>
</div>
