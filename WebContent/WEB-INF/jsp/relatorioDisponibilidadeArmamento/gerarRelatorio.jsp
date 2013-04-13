<%@ include file="/base.jsp" %> 


<script type="text/javascript">
jQuery(function () {
	jQuery('#container').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: 'Disponibilidade de armamentos do tipo: ${tipoArmamentoSelecionado}' 
            },
            tooltip: {
        	    pointFormat: '{series.name}: <b>{point.y}</b>',
            	percentageDecimals: 1
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
                            return '<b>'+ this.point.name +'</b>: '+ this.y;
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
    });
    
</script>


<script type="text/javascript" src="<c:url value="/js/highcharts.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/exporting.js"/>"></script>

<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
