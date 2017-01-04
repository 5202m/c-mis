/**
 * 聊天室推送管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var chatPushInfo = {
	cGroupComboxData:null,
	roomsComboxData:null,
	gridId : 'chatPushInfo_datagrid',
	position : {
		"0" : "任务栏",
		"1" : "私聊框",
		"2" : "页面提示",
		"3" : "公聊框",
		"4" : "视频框",
	},
	init : function(){
		this.intCombox();
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 初始化规则下拉框
	 */
	intCombox:function(){
	     chatPushInfo.cGroupComboxData=null;
	     chatPushInfo.roomsComboxData=null;
	     $("#chatPushInfoGroupTypeId").change(function(){
	    	 chatPushInfo.roomsComboxData=getJson(basePath +"/chatGroupController/getGroupTreeList.do?groupType="+$(this).val());
	    	 //设置房间下拉框
			 $("#chatPushInfoRoomIds").combotree({
			    data:chatPushInfo.roomsComboxData
			 });
			 chatPushInfo.cGroupComboxData=getJson(basePath +"/chatClientGroupController/getClientGroupList.do",{clientGroup:"${chatGroup.clientGroupStr}",groupType:this.value})
			//设置客户组下拉框
			 $("#chatPushInfoClientGroupId").combotree({
			    data:chatPushInfo.cGroupComboxData
			 });
	     }).trigger("change");
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatPushInfo.gridId,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/chatPushInfoController/datagrid.do?groupType='+$("#chatPushInfoGroupTypeId").val(),
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#chatPushInfo_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#chatPushInfo_datagrid_rowOperation").html();
						}},
			            {title : '推送方式',field : 'pushType',formatter : function(value, rowData, rowIndex) {
							return 0==value?'预定义':'即时执行';
						}},
						{title : '推送位置',field : 'position',formatter : function(value, rowData, rowIndex) {
							return chatPushInfo.position[value + ""] || "";
						}},
						{title : '上线时长(分钟)',field : 'onlineMin'},
						{title : '推送标题',field : 'title'},
						{title : '推送内容',field : 'content'},
						{title:'是否延续推送',field : 'replyRepeat',formatter : function(value, rowData, rowIndex) {
							return (!value||0==value)?"否":"是";
						}},
						{title:'间隔时间(分钟)',field : 'intervalMin'},
						{title : '状态',field : 'statusName',formatter : function(value, rowData, rowIndex) {
							return chatPushInfo.getDictNameByCode("#chatPushInfoStatus",rowData.status);
						}},
						{title : '房间类别',field : 'groupType',formatter : function(value, rowData, rowIndex) {
							return chatPushInfo.getDictNameByCode("#chatPushInfoGroupTypeId",rowData.groupType);
						}},
						{title : '所属房间',field : 'roomId',formatter : function(value, rowData, rowIndex) {
							var nameArr=[],valTmp=rowData.roomIds?rowData.roomIds.toString():'',tmpData=null;
							if(valTmp){
								for(var i in chatPushInfo.roomsComboxData){
									tmpData=chatPushInfo.roomsComboxData[i];
									if(valTmp.indexOf(tmpData.id)!=-1){
										nameArr.push(tmpData.text);
									}
								}
							}
							return nameArr.join("，");
						}},
						{title : '客户组别',field : 'clientGroupStr',formatter : function(value, rowData, rowIndex) {
							var nameArr=[],valTmp=rowData.clientGroup?rowData.clientGroup.toString():'',tmpData=null;
							if(valTmp){
								for(var i in chatPushInfo.cGroupComboxData){
									tmpData=chatPushInfo.cGroupComboxData[i];
									if(valTmp.indexOf(tmpData.id)!=-1){
										nameArr.push(tmpData.text);
									}
								}
							}
							return nameArr.join("，");
						}},
						{title : '有效时间(红色为当前生效)',field : 'pushDate',formatter : function(value, rowData, rowIndex) {
							if(!value){
								return "";
							}else{
								return "<font " + (dateTimeWeekCheck(value, true) ? "style='color:red;'" : "") + " >" + formatDateWeekTime(value) + "</font>"
							}
						}},
						{title : '创建人',field : 'createUser'},
						{title : '创建时间',field : 'createDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title : '修改人',field : 'updateUser'},
						{title : '修改时间',field : 'updateDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}}
						
			]],
			toolbar : '#chatPushInfo_datagrid_toolbar'
		});
	},
	/**
	 * 提取名称
	 */
	getDictNameByCode:function(id,code){
		return $(id).find("option[value='"+code+"']").text();
	},
	setEvent:function(){
		// 列表查询
		$("#chatPushInfo_queryForm_search").on("click",function(){
			var queryParams = $('#'+chatPushInfo.gridId).datagrid('options').queryParams;
			chatPushInfo.clearQueryParams(queryParams);
			$("#chatPushInfo_queryForm input[name],#chatPushInfo_queryForm select[name]").each(function(){
				var qp=queryParams[this.name];
				if(isValid(qp)){
					queryParams[this.name]+=(","+$(this).val());
				}else{
					queryParams[this.name] = $(this).val();
				}
			});
			$('#'+chatPushInfo.gridId).datagrid({
				url : basePath+'/chatPushInfoController/datagrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#chatPushInfo_queryForm_reset").on("click",function(){
			$("#chatPushInfo_queryForm")[0].reset();
		});
	},
	/**
	 * 清空旧的参数
	 */
	clearQueryParams:function(queryParams){
		$("#chatPushInfo_queryForm input[name],#chatPushInfo_queryForm select").each(function(){
			if(isValid($(this).attr("name"))){
				queryParams[this.name] = "";
			}
			if(isValid($(this).attr("comboname"))){
				queryParams[$(this).attr("comboname")] = "";
			}
		});
	},
	/**
	 * 功能：增加
	 */
	add : function(){
		var url = formatUrl(basePath + '/chatPushInfoController/add.do');
		var submitUrl =  formatUrl(basePath + '/chatPushInfoController/create.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			width : 850,
			height : 590,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#chatPushInfoSubmitForm").form('validate')){
					$("#chatPushInfo_pushDate").val($("#chatPushInfo_pushDate_div").dateTimeWeek("getData"));
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatPushInfoSubmitForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								// 提交成功后先关闭弹出框,然后刷新表格,弹出对应的成功提示
								$("#myWindow").dialog("close");
								chatPushInfo.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),'操作成功！','info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'新增失败，原因：'+d.msg,'error');	/**操作提示 新增失败!*/
							}
						}
					});
				}
			}
		});
	},
	/**
	 * 功能：修改
	 * @param recordId   dataGrid行Id
	 */
	edit : function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatPushInfoController/'+recordId+'/edit.do');
		var submitUrl =  formatUrl(basePath + '/chatPushInfoController/update.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			width : 850,
			height : 590,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if($("#chatPushInfoSubmitForm").form('validate')){
					$("#chatPushInfo_pushDate").val($("#chatPushInfo_pushDate_div").dateTimeWeek("getData"));
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatPushInfoSubmitForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#myWindow").dialog("close");
								chatPushInfo.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败，原因：'+d.msg,'error');  /**操作提示  修改失败!*/
							}
						}
					});
				}
			}
		});
	},
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatPushInfo.gridId).datagrid('reload');
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/chatPushInfoController/del.do');
		goldOfficeUtils.deleteBatch('chatPushInfo_datagrid',url);	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#chatPushInfo_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatPushInfoController/del.do');
		goldOfficeUtils.deleteOne('chatPushInfo_datagrid',recordId,url);
	}
};
		
//初始化
$(function() {
	chatPushInfo.init();
});