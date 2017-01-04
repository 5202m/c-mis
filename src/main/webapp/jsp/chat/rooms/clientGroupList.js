/**
 * 聊天室客户组管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var chatClientGroup = {
	cGroupComboxData:null,
	gridId : 'chatClientGroup_datagrid',
	init : function(){
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatClientGroup.gridId,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/chatClientGroupController/dataGrid.do?groupType='+$("#chatClientGroup_queryForm select[name=groupType]").val(),
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#chatClientGroup_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#chatClientGroup_datagrid_rowOperation").html();
						}},
						{title : '类别',field : 'groupTypeName',formatter : function(value, rowData, rowIndex) {
							return chatClientGroup.getDictNameByCode("#chatClientGroup_queryForm select[name=groupType]",rowData.groupType);
						}},
			            {title : '编号',field : 'clientGroupId'},
						{title : '名称',field : 'name'},
						{title : '默认房间',field : 'defChatGroupId',formatter : function(value, rowData, rowIndex) {
							return isBlank(value)?"":$("#chatGroupId").find("option[value='"+value+"']").text();
						}},
						{title : '排序号',field : 'sequence',sortable: true},
						{title : '说明',field : 'remark'},
						{title : '权限说明',field : 'authorityDes'}
						
			]],
			toolbar : '#chatClientGroup_datagrid_toolbar'
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
		$("#chatClientGroup_queryForm_search").on("click",function(){
			var queryParams = $('#'+chatClientGroup.gridId).datagrid('options').queryParams;
			chatClientGroup.clearQueryParams(queryParams);
			$("#chatClientGroup_queryForm input[name],#chatClientGroup_queryForm select[name]").each(function(){
				queryParams[this.name] = $(this).val();
			});
			$('#'+chatClientGroup.gridId).datagrid({
				url : basePath+'/chatClientGroupController/dataGrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#chatClientGroup_queryForm_reset").on("click",function(){
			$("#chatClientGroup_queryForm")[0].reset();
		});
	},
	/**
	 * 清空旧的参数
	 */
	clearQueryParams:function(queryParams){
		$("#chatClientGroup_queryForm input[name],#chatClientGroup_queryForm select[name]").each(function(){
			queryParams[this.name] = "";
		});
	},
	/**
	 * 功能：增加
	 */
	add : function(){
		var url = formatUrl(basePath + '/chatClientGroupController/add.do');
		var submitUrl =  formatUrl(basePath + '/chatClientGroupController/create.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			height : 270,
			width:450,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#chatClientGroupSubmitForm").form('validate')){
					if(isNaN($("#chatClientGroupSubmitForm input[name=sequence]").val())){
						alert("排序：请输入数字！");
						return;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatClientGroupSubmitForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								// 提交成功后先关闭弹出框,然后刷新表格,弹出对应的成功提示
								$("#myWindow").dialog("close");
								chatClientGroup.refresh();
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
		var url = formatUrl(basePath + '/chatClientGroupController/'+recordId+'/edit.do');
		var submitUrl =  formatUrl(basePath + '/chatClientGroupController/update.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			height : 270,
			width:450,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if($("#chatClientGroupSubmitForm").form('validate')){
					if(isNaN($("#chatClientGroupSubmitForm input[name=sequence]").val())){
						alert("排序：请输入数字！");
						return;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatClientGroupSubmitForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#myWindow").dialog("close");
								chatClientGroup.refresh();
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
		$('#'+chatClientGroup.gridId).datagrid('reload');
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		goldOfficeUtils.deleteBatch('chatClientGroup_datagrid',formatUrl(basePath + '/chatClientGroupController/del.do'));	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#chatClientGroup_datagrid").datagrid('unselectAll');
		goldOfficeUtils.deleteOne('chatClientGroup_datagrid',recordId,formatUrl(basePath + '/chatClientGroupController/del.do'));
	}
};
		
//初始化
$(function() {
	chatClientGroup.init();
});