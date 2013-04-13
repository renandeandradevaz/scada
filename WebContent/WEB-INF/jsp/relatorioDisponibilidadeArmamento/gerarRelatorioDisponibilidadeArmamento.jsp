<%@ include file="/base.jsp" %> 


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
<script type="text/javascript" charset="utf-8" src="<c:url value="/js/exporting.js"/>"></script>

<div id="divrelatorio" style="min-width: 500px; height: 500px; margin: 0 auto"></div>


