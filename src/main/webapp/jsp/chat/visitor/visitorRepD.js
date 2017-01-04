/**
 * 聊天室访客记录报表（在线时长）
 * @author dick.guo
 * @date   2016/01/19
 */
var chatVisitorRepD = {
	gridId : 'chatVisitorRepD_datagrid',
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
		goldOfficeUtils.dataGrid({
			gridId : chatVisitorRepD.gridId,
			idField : null,
			sortName : 'dataDate',
			sortOrder : 'desc',
			url : basePath+'/chatVisitorController/repDDatagrid.do',
			queryParams : chatVisitorRepD.getParams(),
			columns : [[
			            {title : '序号',checkbox : true},
						{title : '数据日期', field : 'dataDate'},
						{title : '1分钟以内', field : 'M0_1', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_M0_1', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepD.getRate(rowData["M0_1"], rowData.statSum);
						}},
						{title : '1-5分钟', field : 'M1_5', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_M1_5', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepD.getRate(rowData["M1_5"], rowData.statSum);
						}},
						{title : '5-30分钟', field : 'M5_30', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_M5_30', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepD.getRate(rowData["M5_30"], rowData.statSum);
						}},
						{title : '30分钟-1小时', field : 'M30_60', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_M30_60', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepD.getRate(rowData["M30_60"], rowData.statSum);
						}},
						{title : '1小时-2小时', field : 'M60_120', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_M60_120', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepD.getRate(rowData["M60_120"], rowData.statSum);
						}},
						{title : '2小时以上', field : 'M120_I', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_M120_I', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepD.getRate(rowData["M120_I"], rowData.statSum);
						}},
						{title : '合计', field : 'statSum', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}}
			]],
			toolbar : '#visitorRepD_datagrid_toolbar',
			onSelect : function(index, data){
				chatVisitorRepD.showChart(chatVisitorRepD.groupName, data);
			},
			onLoadSuccess : function(data){
				if(data.total > 0){
					var loc_groupId = $(this).datagrid('options').queryParams.groupId;
					chatVisitorRepD.groupName = $.trim($("#visitor_repDForm select[name='groupId'] option[value='" + loc_groupId + "']").text());
					$(this).datagrid('selectRow', data.total - 1);
				}else{
					chatVisitorRepD.showChart(null, null);
				}
			}
		});
	},
	
	/**
	 * 设置事件
	 */
	setEvent:function(){
		// 列表查询
		$("#visitor_repDForm_search").on("click",function(){
			$('#'+chatVisitorRepD.gridId).datagrid({
				queryParams:chatVisitorRepD.getParams(),
				pageNumber : 1
			});
		});
		
		// 重置
		$("#visitor_repDForm_reset").on("click",function(){
			$("#visitor_repDForm")[0].reset();
		});
	},
	
	/**
	 * 初始化图表
	 */
	initChart : function(){
		chatVisitorRepD.repChart = new Highcharts.Chart({
	        chart: {
	        	renderTo : 'visitor_repDChart',
	            type: 'pie',
//	            options3d: {
//	                enabled: true,
//	                alpha: 45,
//	                beta: 0
//	            },
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
	        credits : { //水印
	        	enabled: false
	        },
	        title: {
	            text: '在线时长统计',
	            style : {fontWeight: 'bold', "fontSize": "22px"}
	        },
	        tooltip: {
	            pointFormat: '{point.y}人，占比: <b>{point.percentage:.2f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                depth: 25,
	                dataLabels: {
	                    enabled: true,
	                    format: '{point.percentage:.2f}%'
	                },
	                showInLegend : true
	            }
	        },
	        series: [{
	            type: 'pie',
	            data: null
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
		$("#visitor_repDForm input[name],#visitor_repDForm select[name]").each(function(){
			loc_params[this.name] = $(this).val();
		});
		return loc_params;
	},
	
	/**
	 * 重绘图表
	 */
	showChart : function(groupName, data){
		if(data){
			$("#visitor_repDChart").show();
			if(!chatVisitorRepD.repChart){
				chatVisitorRepD.initChart();
			}
			var loc_title = {text : "在线时长统计图——" + groupName + "(" + data.dataDate + ")"};
			var loc_data = [
                            ['1分钟以内',data.M0_1],
                            ['1-5分钟',data.M1_5],
                            ['5-30分钟',data.M5_30],
                            ['30分钟-1小时',data.M30_60],
                            ['1小时-2小时',data.M60_120],
                            ['2小时以上',data.M120_I]
                            ];
			chatVisitorRepD.repChart.setTitle(loc_title, null, false);
			chatVisitorRepD.repChart.series[0].setData(loc_data, true);
		}else{
			$("#visitor_repDChart").hide();
		}
	},
	
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatVisitorRepD.gridId).datagrid('reload');
	},
	
	/**
	 * 功能：导出记录
	 */
	exportRecord : function(){
		var loc_params = $('#'+chatVisitorRepD.gridId).datagrid('options').queryParams;
		var path = basePath+ '/chatVisitorController/exportRepD.do?'+$.param(loc_params);
		window.location.href = path;
	}
};
		
//初始化
$(function() {
	chatVisitorRepD.init();
});