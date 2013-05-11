<%@ include file="/base.jsp" %> 
<%@ include file="/layoutRelatorios.jsp" %>

<form style="margin-top: -30px;" class="form-horizontal" action="<c:url value="/relatorioAcautelamentos/gerarRelatorioAcautelamentos"/>" method="post">
  <fieldset>
    <legend style="font-size: 13px; cursor: pointer;" >Clique para exibir os filtros</legend>
    
    <div id="filtrosRelatorio" style="display: none;" >
    
	    <div class="control-group">
	      <label class="control-label">Tipo de armamento</label>
	      <div class="controls">
	        <select name="tipoArmamentoSelecionado" >
				<c:forEach items="${tiposDeArmamento}" var="item">
					<option value="${item.id}"> ${item.descricao} - <fmt:formatNumber value="${item.calibre}" /></option>
				</c:forEach>
			</select>
	      </div>
	    </div>
	    
	    <div class="control-group">
	      <label class="control-label">Quantidade de meses</label>
	      <div class="controls">
	        <input class="input-mini numero-inteiro" type="number" name="quantidadeMeses" min="1" max="12" value="6"  >
	      </div>
	    </div>
	    
	    <div class="control-group">
	      <label class="control-label">Quantidade de armamentos mais utilizados</label>
	      <div class="controls">
			<input class="input-mini numero-inteiro" type="number" name="quantidadeArmamentosMaisUtilizados" min="1" max="10" value="4"  >
	      </div>
	    </div>
	    
	    <button type="submit" class="btn btn-primary">Gerar relatório</button>
	    
	 </div>

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
                '<td style="padding:0"><b>{point.y:.0f}</b></td></tr>',
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
    
	jQuery("legend").click(function() {
		
		jQuery("#filtrosRelatorio").fadeIn("slow");
		jQuery("legend").text("Filtros");
	});
});
    
</script>

<script type="text/javascript" charset="utf-8" src="<c:url value="/js/highcharts.js"/>"></script>

<div id="divrelatorio"></div>
