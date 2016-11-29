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
			queryParams :{operateType : $("#operateType").val()},
			columns : [[
			    {title : 'id',field : 'id',hidden:true,width:100},
	            {title : $.i18n.prop("syslog.no"),field : 'userNo',width:100},				/**账户*/
	            {title : $.i18n.prop("syslog.operatetype"),field : 'operateType',width:100,sortable : true,formatter : function(value, rowData, rowIndex) { /**操作类型*/
					return $("#operateType option[value='" + value + "']").text();
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