<%@ include file="/base.jsp" %> 
<%@ include file="/layoutRelatorios.jsp" %>

<form class="form-inline" action="<c:url value="/relatorioDisponibilidadeArmamento/gerarRelatorioDisponibilidadeArmamento"/>" method="post">
  <fieldset>
    <legend>Selecione o tipo de armamento para gerar o relatório</legend>
    
     <select name="tipoArmamentoSelecionado" >
     	<option value=""> Todos </option>
		<c:forEach items="${tiposDeArmamento}" var="item">
			<option value="${item.id}"> ${item.descricao} - <fmt:formatNumber value="${item.calibre}" /> </option>
		</c:forEach>
	</select>

    <button style="margin-left: 30px;" type="submit" class="btn btn-primary">Gerar relatório</button>

  </fieldset>
</form>
<br>

<script type="text/javascript">
jQuery(function () {
	jQuery('#divrelatorio').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: 'Disponibilidade de armamentos do tipo: ${tipoArmamentoSelecionado}' 
            },
                       
            tooltip: {
            	formatter: function(){
            		
            		return '<b>'+ this.point.name +'</b>: ' + this.point.y + ' (' + Highcharts.numberFormat(this.percentage, 1) +' %)';
            	}
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function() {
                            return '<b>'+ this.point.name +'</b>: '+ this.y + ' (' + this.percentage.toFixed(1) + '%)';
                        }
                    }
                }
            },
            series: [{
                type: 'pie',
                name: 'Quantidade',
                data: [${resultadoRelatorioDisponibilidade}]
            }]
        });	
	
		jQuery("tspan:last").hide();
    });
    
</script>

<script type="text/javascript" charset="utf-8" src="<c:url value="/js/highcharts.js"/>"></script>

<div style="width: 1000px;" >
	<div id="divrelatorio" style="min-width: 500px; height: 500px;"></div>
</div>
