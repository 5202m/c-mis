/**
 * 聊天室组别规则管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var chatGroupRule = {
	gridId : 'chatGroupRule_datagrid',
	init : function(){
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatGroupRule.gridId,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/chatGroupRuleController/datagrid.do',
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#chatGroupRule_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#chatGroupRule_datagrid_rowOperation").html();
						}},
			            {title : '规则类别',field : 'typeStr',formatter : function(value, rowData, rowIndex) {
							return chatGroupRule.getRuleNameByCode(rowData.type);
						}},
						{title : '规则名称',field : 'name'},
			            {title : '需要使用规则的值',field : 'beforeRuleVal'},
						{title : '使用规则后的值',field : 'afterRuleVal'},
						{title : '执行规则后的提示语',field : 'afterRuleTips'},
						{title : '规则时间(红色当前生效)',field : 'periodDate',formatter : function(value, rowData, rowIndex) {
							if(!value){
								return "";
							}else{
								return "<font " + (dateTimeWeekCheck(value, true) ? "style='color:red;'" : "") + " >" + formatDateWeekTime(value) + "</font>"
							}
						}},
						
						{title : '修改人',field : 'updateUser'},
						{title : '修改时间',field : 'updateDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}}
						
			]],
			toolbar : '#chatGroupRule_datagrid_toolbar'
		});
	},
	/**
	 * 提取规则名称
	 */
	getRuleNameByCode:function(code){
		return $("#chatGroupRuleCode").find("option[value='"+code+"']").text();
	},
	setEvent:function(){
		// 列表查询
		$("#chatGroupRule_queryForm_search").on("click",function(){
			var queryParams = $('#'+chatGroupRule.gridId).datagrid('options').queryParams;
			$("#chatGroupRule_queryForm input[name],#chatGroupRule_queryForm select[name]").each(function(){
				queryParams[this.name] = $(this).val();
			});
			$('#'+chatGroupRule.gridId).datagrid({
				url : basePath+'/chatGroupRuleController/datagrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#chatGroupRule_queryForm_reset").on("click",function(){
			$("#chatGroupRule_queryForm")[0].reset();
		});
	},
	/**
	 * 功能：增加
	 */
	add : function(){
		var url = formatUrl(basePath + '/chatGroupRuleController/add.do');
		var submitUrl =  formatUrl(basePath + '/chatGroupRuleController/create.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			height : 375,
			width:675,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#chatGroupRuleSubmitForm").form('validate')){
					$("#chatGroupRule_periodDate").val($("#chatGroupRule_periodDate_div").dateTimeWeek("getData"));
					if($('#chatGroupRuleSubmitForm select[name="type"]').val()=='online_mem_set') {
						var ruleVal = {'member':$('#member').val(), 'visitor':$('#visitor').val(),'start':$('#start').val(),'end':$('#end').val(),'normal':$('#normal').val()};
						$('#chatGroupRuleSubmitForm input[name="beforeRuleVal"]').val(JSON.stringify(ruleVal));
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatGroupRuleSubmitForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								// 提交成功后先关闭弹出框,然后刷新表格,弹出对应的成功提示
								$("#myWindow").dialog("close");
								chatGroupRule.refresh();
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
		var url = formatUrl(basePath + '/chatGroupRuleController/'+recordId+'/edit.do');
		var submitUrl =  formatUrl(basePath + '/chatGroupRuleController/update.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			height : 375,
			width:675,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if($("#chatGroupRuleSubmitForm").form('validate')){
					$("#chatGroupRule_periodDate").val($("#chatGroupRule_periodDate_div").dateTimeWeek("getData"));
					if($('#chatGroupRuleSubmitForm select[name="type"]').val()=='online_mem_set') {
						var ruleVal = {};
						$('#chatGroupRuleSubmitForm #chatGroupRule_online_mem_tbody input[type="text"]').each(function(){
							ruleVal[$(this).attr('name')] = $(this).val();
						});
						$('#chatGroupRuleSubmitForm input[name="beforeRuleVal"]').val(JSON.stringify(ruleVal));
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatGroupRuleSubmitForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#myWindow").dialog("close");
								chatGroupRule.refresh();
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
		$('#'+chatGroupRule.gridId).datagrid('reload');
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/chatGroupRuleController/del.do');
		goldOfficeUtils.deleteBatch('chatGroupRule_datagrid',url);	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#chatGroupRule_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatGroupRuleController/del.do');
		goldOfficeUtils.deleteOne('chatGroupRule_datagrid',recordId,url);
	}
};
		
//初始化
$(function() {
	chatGroupRule.init();
});