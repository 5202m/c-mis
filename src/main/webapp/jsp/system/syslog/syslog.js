/**
 * 摘要：日志管理公用js
 * @author Gavin.guo
 * @date   2014-10-27
 */
var systemLog = {
	gridId : 'system_log_datagrid',
	init : function(){
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : systemLog.gridId,
			singleSelect : true,
			url : basePath+'/sysLogController/datagrid.do',
			columns : [[
			    {title : 'id',field : 'id',hidden:true,width:100},
	            {title : $.i18n.prop("syslog.no"),field : 'userNo',width:100},				/**账户*/
	            {title : $.i18n.prop("syslog.operatetype"),field : 'operateType',width:100,sortable : true,formatter : function(value, rowData, rowIndex) { /**操作类型*/
					if (value == 1) {
						return $.i18n.prop("syslog.login");
					} else if(value == 2){
						return $.i18n.prop("syslog.exit");
					} else if(value == 3){
						return $.i18n.prop("syslog.add");
					} else if(value == 4){
						return $.i18n.prop("syslog.del");
					} else if(value == 5){
						return $.i18n.prop("syslog.edit");
					} else if(value == 6){
						return $.i18n.prop("syslog.other");
					} else if(value == 7){
						return '审批';
					} else if(value == 8){
						return '取消审批';
					} else if(value == 9){
						return '导出记录';
					}
					return '';
				  }
	            },
				{title : $.i18n.prop("syslog.operatedate"),field : 'operateDate',width:100,sortable : true,formatter : function(value, rowData, rowIndex) {  /**操作时间*/
					return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
				}},
				{title : $.i18n.prop("syslog.content"),field : 'logContent',width:150},				/**日志内容*/
				{title : $.i18n.prop("syslog.browser"),field : 'broswer',width:100},				/**浏览器*/
				{title : $.i18n.prop("syslog.ip"),field : 'createIp',width:100}						/**IP*/
			]]
		});
	},
	setEvent:function(){
		$("#system_log_queryForm_search").on("click",function(){
			var userNo = $("#userNo").val();
			var operateType = $("#operateType").val();
			var startDate =  formatStartDate($("#startDate").val());
			var endDate =  formatEndDate($("#endDate").val());
			var queryParams = $('#'+systemLog.gridId).datagrid('options').queryParams;
			queryParams['userNo'] = userNo;
			queryParams['operateType'] = operateType;
			queryParams['startDate'] = startDate;
			queryParams['endDate'] = endDate;
			$('#'+systemLog.gridId).datagrid({
				url : basePath+'/sysLogController/datagrid.do',
				pageNumber : 1
			});
		});
		$("#system_log_queryForm_reset").on("click",function(){
			$("#system_log_queryForm")[0].reset();
		});
	},
};
		
//初始化
$(function() {
	systemLog.init();
});