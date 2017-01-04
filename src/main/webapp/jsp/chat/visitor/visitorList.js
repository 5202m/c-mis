/**
 * 聊天室在线用户管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var chatVisitor = {
	gridId : 'chatVisitor_datagrid',
	init : function(){
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatVisitor.gridId,
			idField : 'chatVisitorId',
			sortName : 'onlineDate',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/chatVisitorController/visitorDatagrid.do',
			queryParams : {roomId : $("#visitorGroupId").val()},
			columns : [[
			            {title : 'chatVisitorId',checkbox : true},
						/*{title : '手机号码',field : 'mobile'},*/
						{title : '账号',field : 'userId',formatter : function(value, rowData, rowIndex) {
							return isBlank(rowData.userId)?rowData.visitorId:(rowData.userId+"【" + rowData.visitorId + "】");
						}},
						{title : '昵称',field : 'nickname'},
			            {title : '房间名称',field : 'groupName',formatter : function(value, rowData, rowIndex) {
							return chatVisitor.getComboxNameByCode("#visitorGroupId",rowData.roomId);
						}},
						{title : '访问次数',field : 'visitTimes'},
						{title : '上次上线时间',field : 'onlinePreDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return rowData.onlinePreDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.onlinePreDate) : '';
						}},
						{title : '上线时间',field : 'onlineDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return rowData.onlineDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.onlineDate) : '';
						}},
						{title : '在线状态',field : 'onlineStatus',formatter : function(value, rowData, rowIndex) {
							return value&&value=='1'?'在线':'下线';
						}},
						{title : '在线时长',field : 'onLineDuration'},
						{title : '下线时间',field : 'offlineDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return rowData.offlineDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.offlineDate) : '';
						}},
						{title : '上次登录时间',field : 'loginPreDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return rowData.loginPreDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.loginPreDate) : '';
						}},
						{title : '登录次数',field : 'loginTimes'},
						{title : '登录时间',field : 'loginDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return rowData.loginDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.loginDate) : '';
						}},
						{title : '登录状态',field : 'loginStatus',formatter : function(value, rowData, rowIndex) {
							return value&&value=='1'?'已登入':(rowData.loginTimes>0?'已登出':'从未登录');
						}},
						{title : '客户IP',field : 'ip'},
						{title : '客户来源',field : 'platform',formatter : function(value, rowData, rowIndex) {
							return chatVisitor.getComboxNameByCode("#visitorPlatform",value || "studio");
						}},
						{title : '客户所在地',field : 'ipCity'},
						{title : '使用设备',field : 'userAgents',formatter : function(value, rowData, rowIndex) {
							return isMobile(rowData.userAgent) ? "手机" : "PC";
						}},
						{title : '使用设备详情',field : 'userAgent'}
			]],
			toolbar : '#visitor_datagrid_toolbar'
		});
	},
	/**
	 * 从下拉框中提取名称
	 */
	getComboxNameByCode:function(domId,code){
		return $(domId).find("option[value='"+code+"']").text();
	},
	setEvent:function(){
		// 列表查询
		$("#visitor_queryForm_search").on("click",function(){
			var queryParams = $('#'+chatVisitor.gridId).datagrid('options').queryParams;
			$("#visitor_queryForm input[name],#visitor_queryForm select[name]").each(function(){
				queryParams[this.name] = $(this).val();
			});
			if(queryParams["onlineDateStart"]){
				queryParams["onlineDateStart"] =  formatStartDate(queryParams["onlineDateStart"]);
			}
			if(queryParams["onlineDateEnd"]){
				queryParams["onlineDateEnd"] =  formatEndDate(queryParams["onlineDateEnd"]);
			}
			if(queryParams["loginDateStart"]){
				queryParams["loginDateStart"] =  formatStartDate(queryParams["loginDateStart"]);
			}
			if(queryParams["loginDateEnd"]){
				queryParams["loginDateEnd"] =  formatEndDate(queryParams["loginDateEnd"]);
			}
			$('#'+chatVisitor.gridId).datagrid({
				pageNumber : 1
			});
		});
		// 重置
		$("#visitor_queryForm_reset").on("click",function(){
			$("#visitor_queryForm")[0].reset();
		});
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		goldOfficeUtils.deleteBatch(chatVisitor.gridId,formatUrl(basePath + '/chatVisitorController/visitorDel.do'),"chatVisitorId");	
	},
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatVisitor.gridId).datagrid('reload');
	},
	/**
	 * 功能：导出记录
	 */
	exportRecord : function(){
		var loc_params = $('#'+chatVisitor.gridId).datagrid('options').queryParams;
		var path = basePath+ '/chatVisitorController/exportRecord.do?'+$.param(loc_params);
		window.location.href = path;
	}
};
		
//初始化
$(function() {
	chatVisitor.init();
});