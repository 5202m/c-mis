
var ZxDataRep = {
    repChart : null,

    init : function(){
        this.initGrid();
        this.setEvent();
    },
    /**
     * 设置事件
     */
    setEvent:function(){
        // 列表查询
        $("#zxData_queryRepForm_search").on("click",function(){
            ZxDataRep.initGrid($('#basicIndexId').val());
        });

        // 重置
        $("#zxData_queryRepForm_reset").on("click",function(){
            $("#zxData_queryRepForm")[0].reset();
        });
    },
    /**
     * 功能：dataGrid初始化
     */
    initGrid : function(value){
        value = isBlank(value)?$('#basicIndexId').val():value;
        if(isValid(value)) {
            $.getJSON(basePath + '/zxDataController/repDatagrid.do', {basicIndexId: value}, function (data) {
                if (data) {
                    ZxDataRep.showChart(data.data);
                }
            });
        }
    },
    /**
     * 初始化图表
     */
    initChart : function(){
        ZxDataRep.repChart = new Highcharts.Chart({
            chart: {
                renderTo : 'zxData_repChart',
                type: 'spline',
                borderWidth: 2,
                plotBackgroundColor: 'rgba(255, 255, 255, .9)',
                plotShadow: true,
                plotBorderWidth: 1
            },
            legend : {
                enabled : true
            },
            xAxis: {
                /*type: 'datetime',
                dateTimeLabelFormats: {
                    month: '%e. %b',
                    year: '%b'
                },*/
                title: {
                    text: '日期'
                },
                categories:[]
            },
            yAxis: {
                title: {
                    text: '值'
                }
            },
            credits : { //水印
                enabled: false
            },
            title: {
                text: '财经日历数据统计',
                style : {fontWeight: 'bold', "fontSize": "22px"}
            },
            tooltip: {
                headerFormat: '<b>{series.name}</b><br>',
                pointFormat: '{point.y}% '
                //pointFormat: '失业率：{point.y}</b>'
            },
            plotOptions: {
                spline: {
                    marker: {
                        enabled: true
                    }
                }
            },
            series: []
        });
    },

    /**
     * 重绘图表
     */
    showChart : function(data){
        if(data){
            $("#zxData_repChart").show();
            if(!ZxDataRep.repChart){
                ZxDataRep.initChart();
            }
            var predictValueObj = {name:'预期值',data:[]},
                lastValueObj = {name:'前值',data:[]},
                valueObj = {name:'公布值',data:[]},
                xValue = [];
            $.each(data, function(i, row){
                var predictValue = isBlank(row.predictValue)?0:parseFloat(row.predictValue.replace('%',''));
                var lastValue = isBlank(row.lastValue)?0:parseFloat(row.lastValue.replace('%',''));
                var value = isBlank(row.value)?0:parseFloat(row.value.replace('%',''));
                predictValueObj.data.push([row.date, predictValue]);
                lastValueObj.data.push([row.date, lastValue]);
                valueObj.data.push([row.date, value]);
                xValue.push(row.date);
            });
            var loc_title = {text : "财经日历数据统计——失业率"};
            ZxDataRep.repChart.setTitle(loc_title, null, false);
            ZxDataRep.repChart.addAxis(xValue, true);
            ZxDataRep.repChart.addSeries(predictValueObj, false);
            ZxDataRep.repChart.addSeries(lastValueObj, false);
            ZxDataRep.repChart.addSeries(valueObj, false);
            ZxDataRep.repChart.redraw();
        }else{
            $("#zxData_repChart").hide();
        }
    }
};

$(function(){
    ZxDataRep.init();
});