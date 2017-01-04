/**
 * 聊天室访客记录报表（各类在线人数）
 * @author dick.guo
 * @date   2016/01/21
 */
var chatVisitorRepO = {
	gridId : 'chatVisitorRepO_datagrid',
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
			gridId : chatVisitorRepO.gridId,
			idField : null,
			sortName : 'dataDate',
			sortOrder : 'desc',
			url : basePath+'/chatVisitorController/repODatagrid.do',
			queryParams : chatVisitorRepO.getParams(),
			columns : [[
			            {title : '序号',checkbox : true},
						{title : '数据日期', field : 'dataDate'},
						{title : 'VIP用户', field : 'vip', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_vip', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepO.getRate(rowData["vip"], rowData.statSum);
						}},
						{title : '真实A用户', field : 'active', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_active', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepO.getRate(rowData["active"], rowData.statSum);
						}},
						{title : '真实N用户', field : 'notActive', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_notActive', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepO.getRate(rowData["notActive"], rowData.statSum);
						}},
						{title : '模拟用户', field : 'simulate', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_simulate', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepO.getRate(rowData["simulate"], rowData.statSum);
						}},
						{title : '注册用户', field : 'register', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_register', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepO.getRate(rowData["register"], rowData.statSum);
						}},
						{title : '游客', field : 'visitor', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}},
						{title : '比例', field : 'rate_visitor', formatter : function(value, rowData, rowIndex) {
							return chatVisitorRepO.getRate(rowData["visitor"], rowData.statSum);
						}},
						{title : '合计', field : 'statSum', formatter : function(value, rowData, rowIndex) {
							return value || 0;
						}}
			]],
			toolbar : '#visitorRepO_datagrid_toolbar',
			onSelect : function(index, data){
				chatVisitorRepO.showChart(chatVisitorRepO.groupName, data);
			},
			onLoadSuccess : function(data){
				if(data.total > 0){
					var loc_groupId = $(this).datagrid('options').queryParams.groupId;
					chatVisitorRepO.groupName = $.trim($("#visitor_repOForm select[name='groupId'] option[value='" + loc_groupId + "']").text());
					$(this).datagrid('selectRow', data.total - 1);
				}else{
					chatVisitorRepO.showChart(null, null);
				}
			}
		});
	},
	
	/**
	 * 设置事件
	 */
	setEvent:function(){
		// 列表查询
		$("#visitor_repOForm_search").on("click",function(){
			$('#'+chatVisitorRepO.gridId).datagrid({
				queryParams:chatVisitorRepO.getParams(),
				pageNumber : 1
			});
		});
		
		// 重置
		$("#visitor_repOForm_reset").on("click",function(){
			$("#visitor_repOForm")[0].reset();
		});
	},
	
	/**
	 * 初始化图表
	 */
	initChart : function(){
		chatVisitorRepO.repChart = new Highcharts.Chart({
	        chart: {
	        	renderTo : 'visitor_repOChart',
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
	            text: '各类在线人数统计',
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
		$("#visitor_repOForm input[name],#visitor_repOForm select[name]").each(function(){
			loc_params[this.name] = $(this).val();
		});
		return loc_params;
	},
	
	/**
	 * 重绘图表
	 */
	showChart : function(groupName, data){
		if(data){
			$("#visitor_repOChart").show();
			if(!chatVisitorRepO.repChart){
				chatVisitorRepO.initChart();
			}
			var loc_title = {text : "各类在线人数统计——" + groupName + "(" + data.dataDate + ")"};
			var loc_data = [
                            ['VIP用户',data.vip],
                            ['真实A用户',data.active],
                            ['真实N用户',data.notActive],
                            ['模拟用户',data.simulate],
                            ['注册用户',data.register],
                            ['游客',data.visitor]
                            ];
			chatVisitorRepO.repChart.setTitle(loc_title, null, false);
			chatVisitorRepO.repChart.series[0].setData(loc_data, true);
		}else{
			$("#visitor_repOChart").hide();
		}
	},
	
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatVisitorRepO.gridId).datagrid('reload');
	},
	
	/**
	 * 功能：导出记录
	 */
	exportRecord : function(){
		var loc_params = $('#'+chatVisitorRepO.gridId).datagrid('options').queryParams;
		var path = basePath+ '/chatVisitorController/exportRepO.do?'+$.param(loc_params);
		window.location.href = path;
	}
};
		
//初始化
$(function() {
	chatVisitorRepO.init();
});