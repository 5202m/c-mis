/**
 * 聊天室访客记录报表（整点在线人数）
 * @author dick.guo
 * @date   2016/01/21
 */
var chatVisitorRepT = {
	gridId : 'chatVisitorRepT_datagrid',
	groupName : null,
	repChart : null,
	
	init : function(){
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		var loc_columns = [
            {title : '序号',checkbox : true},
			{title : '数据日期', field : 'dataDate'}
		];
		var loc_timeStr = null;
		for(var i = 0; i < 24; i++){
			loc_timeStr = (i < 10 ? "0" : "") + i + ":00";
			loc_columns.push({title : loc_timeStr, field : "H" + i, formatter : function(value, rowData, rowIndex) {
				return rowData[this.title] || 0;//field如果带有‘:’页面展示会有问题
			}});
		}
		console.info(loc_columns.length);
		
		goldOfficeUtils.dataGrid({
			gridId : chatVisitorRepT.gridId,
			idField : null,
			sortName : 'dataDate',
			sortOrder : 'desc',
			url : basePath+'/chatVisitorController/repTDatagrid.do',
			queryParams : chatVisitorRepT.getParams(),
			columns : [loc_columns],
			toolbar : '#visitorRepT_datagrid_toolbar',
			onSelect : function(index, data){
				chatVisitorRepT.showChart(chatVisitorRepT.groupName, data);
			},
			onLoadSuccess : function(data){
				if(data.total > 0){
					var loc_groupId = $(this).datagrid('options').queryParams.groupId;
					chatVisitorRepT.groupName = $.trim($("#visitor_repTForm select[name='groupId'] option[value='" + loc_groupId + "']").text());
					$(this).datagrid('selectRow', 0);
				}else{
					chatVisitorRepT.showChart(null, false);
				}
			}
		});
	},
	
	/**
	 * 设置事件
	 */
	setEvent:function(){
		// 列表查询
		$("#visitor_repTForm_search").on("click",function(){
			$('#'+chatVisitorRepT.gridId).datagrid({
				queryParams:chatVisitorRepT.getParams(),
				pageNumber : 1
			});
		});
		
		// 重置
		$("#visitor_repTForm_reset").on("click",function(){
			$("#visitor_repTForm")[0].reset();
		});
	},
	
	/**
	 * 初始化图表
	 */
	initChart : function(){
		var loc_xAxis = [];
		var loc_time = null;
		for(var i = 0; i < 24; i++){
			loc_time = (i < 10 ? "0" : "") + i + ":00";
			loc_xAxis.push(loc_time);
		}
		
		chatVisitorRepT.repChart = new Highcharts.Chart({
	        chart: {
	        	renderTo : 'visitor_repTChart',
	            type: 'line',
	            backgroundColor: {
	    			linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 },
	    			stops: [
	    				[0, 'rgb(255, 255, 255)'],
	    				[1, 'rgb(240, 240, 255)']
	    			]
	    		},
	    		borderWidth: 2,
	    		plotBackgroundColor: 'rgba(255, 255, 255, .9)',
	    		plotShadow: true,
	    		plotBorderWidth: 1
	        },
	        legend : {
	        	enabled : false
	        },
	        xAxis: {
	            categories: loc_xAxis
	        },
	        yAxis: {
	            title: {
	                text: '人数'
	            }
	        },
	        credits : { //水印
	        	enabled: false
	        },
	        title: {
	            text: '整点在线人数统计',
	            style : {fontWeight: 'bold', "fontSize": "22px"}
	        },
	        tooltip: {
	            pointFormat: '在线人数：{point.y}</b>'
	        },
	        plotOptions: {
	        	line: {
	                dataLabels: {
	                    enabled: true
	                }
	            }
	        },
	        series: [{
	            name: '在线人数',
	            data: [{},]
	        }]
	    });
	},
	
	/**
	 * 计算比例
	 */
	getRate : function(num1, num2){
		var loc_rate = 0;
		num1 = Number(num1);
		num2 = Number(num2);
		if(isNaN(num1) || isNaN(num2) || num2 <= 0){
			loc_rate = 0;
		}else{
			loc_rate = num1 / num2 * 100;
		}
		return loc_rate.toFixed(2) + "%";
	},
	
	/**
	 * 获取查询参数
	 */
	getParams : function(src){
		var loc_params = src || {};
		$("#visitor_repTForm input[name],#visitor_repTForm select[name]").each(function(){
			loc_params[this.name] = $(this).val();
		});
		return loc_params;
	},
	
	/**
	 * 重绘图表
	 */
	showChart : function(groupName, data){
		if(data){
			$("#visitor_repTChart").show();
			if(!chatVisitorRepT.repChart){
				chatVisitorRepT.initChart();
			}
			var loc_data = [];
			var loc_time = null;
			for(var i = 0; i < 24; i++){
				loc_time = (i < 10 ? "0" : "") + i + ":00";
				loc_data.push({
					y : data[loc_time] || 0
				});
			}
			var loc_title = {text : "整点在线人数统计——" + groupName + "(" + data.dataDate + ")"};
			chatVisitorRepT.repChart.setTitle(loc_title, null, false);
			chatVisitorRepT.repChart.series[0].setData(loc_data, true);
		}else{
			$("#visitor_repTChart").hide();
		}
	},
	
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatVisitorRepT.gridId).datagrid('reload');
	},
	
	/**
	 * 功能：导出记录
	 */
	exportRecord : function(){
		var loc_params = $('#'+chatVisitorRepT.gridId).datagrid('options').queryParams;
		var path = basePath+ '/chatVisitorController/exportRepT.do?'+$.param(loc_params);
		window.location.href = path;
	}
};
		
//初始化
$(function() {
	chatVisitorRepT.init();
});