/**
 * 摘要：用户管理公用js
 * @author Gavin.guo
 * @date   2014-10-14
 */
var systemUser = {
	gridId : 'system_user_datagrid',
	opType : '',
	init : function(){
		this.opType = $("#userOpType").val();
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : systemUser.gridId,
			idField:"userId",
			sortName : 'userId',
			singleSelect : false,
			url : basePath+'/userController/datagrid.do?opType=' + systemUser.opType,
			columns : [[
			            {title : 'userId',field : 'userId',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#system_user_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.userId);
						    });
							return $("#system_user_datagrid_rowOperation").html();
						}},
			            {title : $.i18n.prop("user.no"),field : 'userNo'},                   		/**账号*/
			            {title : $.i18n.prop("user.name"),field : 'userName',sortable : true},		/**姓名*/
			            {title : $.i18n.prop("user.email"),field : 'email'},						/**Email*/
						{title : $.i18n.prop("user.phone"),field : 'telephone',sortable : true},	/**手机号*/		
						{title : $.i18n.prop("user.role"),field :'roleName',sortable : true,formatter : function(value, rowData, rowIndex) {/**状态*/
						    if(rowData.role != null){
						    	return rowData.role.roleName;
						    }
							return '';
						}},   	/**所属角色*/
						{title : $.i18n.prop("common.status"),field : 'status',sortable : true,formatter : function(value, rowData, rowIndex) {/**状态*/
							if (value == 0) {
								return $.i18n.prop("common.enabled");
							} else {
								return $.i18n.prop("common.disabled");
							}}}, 
						{title : $.i18n.prop("user.position"),field : 'position',sortable : true},  	/**职位*/
						{title : $.i18n.prop("user.logintime"),field : 'loginDate',sortable : true,formatter : function(value, rowData, rowIndex) {/**登录时间*/
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title : "备注", field : 'remark'},
						{title : $.i18n.prop("user.loginip"),field : 'loginIp'}, 					  	/**登录IP*/
						{title : $.i18n.prop("user.logintimes"),field : 'loginTimes',sortable : true}  /**登录次数*/
			]],
			toolbar : '#system_user_datagrid_toolbar'
		});
	},
	setEvent:function(){
		// 列表查询
		$("#system_user_queryForm_search").on("click",function(){
			var userNo = $("#userNo").val();                       //账号 
			var status = $("#status  option:selected").val();      //状态
			var position = $("#position").val();                   //职位
			var role = $("#role  option:selected").val();          //角色
			var queryParams = $('#'+systemUser.gridId).datagrid('options').queryParams;
			queryParams['userNo'] = userNo;
			queryParams['status'] = status;
			queryParams['position'] = position;
			queryParams['roleId'] = role;
			$('#'+systemUser.gridId).datagrid({
				url : basePath+'/userController/datagrid.do?opType=' + systemUser.opType,
				pageNumber : 1
			});
		});
		// 重置
		$("#system_user_queryForm_reset").on("click",function(){
			$("#system_user_queryForm")[0].reset();
		});
	},
	/**
	 * 功能：查看
	 * @param recordId   dataGrid行Id
	 */
	view : function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/userController/'+recordId+'/view.do');
		goldOfficeUtils.openSimpleDialog({
			title : $.i18n.prop("common.operatetitle.view"),       /**查看记录*/
			height : 320 ,
			href : url ,
			iconCls : 'pag-view'
		});
	},
	/**
	 * 功能：用户退出房间
	 */
	exitChatRoom : function(){
		var rows = $("#"+systemUser.gridId).datagrid('getSelections');
		var ids = [];
		if(rows.length > 0){
			for(var i = 0; i < rows.length; i++) {
				ids.push(rows[i]["userNo"]);
			}
		 }else{
			$.messager.alert("操作提示", "请选择一行记录!"); 
			return;
		}
		var url = formatUrl(basePath + '/chatUserController/toExitRoom.do?userIds='+ids.join(','));
		var submitUrl =  formatUrl(basePath + '/chatUserController/exitRoom.do');
		goldOfficeUtils.openEditorDialog({
			dialogId : "editWindow",
			title : '退出房间',			/**添加记录*/
			width : 280,
			height : 120,
			href : url,
			iconCls : 'pag-cancel',
			handler : function(){   //提交时处理
				if($("#userExitForm").form('validate')){
					$.messager.confirm("操作提示", "你确定要强制让这些用户退出对应的房间吗？", function(r) {
						if(r){
							   goldOfficeUtils.ajaxSubmitForm({
									url : submitUrl,
									formId : 'userExitForm',
									onSuccess : function(data){  //提交成功后处理
										var d = $.parseJSON(data);
										if(d.success) {
											$("#editWindow").dialog("close");
											$.messager.alert($.i18n.prop("common.operate.tips"),'操作已执行','info');
										}else{
											$.messager.alert($.i18n.prop("common.operate.tips"),'操作失败：'+d.msg,'error');
										}
									}
								});
							}
						});
				}
			}
		});
	},
	/**
	 * 功能：增加
	 */
	add : function(){
		var url = formatUrl(basePath + '/userController/add.do?opType=' + systemUser.opType);
		var submitUrl =  formatUrl(basePath + '/userController/create.do');
		goldOfficeUtils.openEditorDialog({
			dialogId : "editWindow",
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			width : 800,
			height : 720,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#userAddForm").form('validate')){
					if(systemUser.validWinRate() == false){
						return ;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'userAddForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if(d.success) {
								$("#editWindow").dialog("close");
								systemUser.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),'新增用户成功！密码为：'+$("#pwd").val(),'info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'新增失败，原因：'+d.msg,'error');
							}
						}
					});
				}
			},
			onLoad : function(){
				if(window.location.href.indexOf("24k.hk") != -1){
					$("#user_header_default>div:gt(7)").remove();
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
		var url = formatUrl(basePath + '/userController/'+recordId+'/edit.do?opType=' + systemUser.opType);
		var submitUrl =  formatUrl(basePath + '/userController/update.do');
		goldOfficeUtils.openEditorDialog({
			dialogId : "editWindow",
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			width : 800,
			height : 700,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if($("#userEditForm").form('validate')){
					if(systemUser.validWinRate() == false){
						return ;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'userEditForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#editWindow").dialog("close");
								systemUser.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败，原因：'+d.msg,'error');  /**操作提示  修改失败!*/
							}
						}
					});
				}
			},
			onLoad : function(){
				if(window.location.href.indexOf("24k.hk") != -1){
					$("#user_header_default>div:gt(7)").remove();
				}
				var avatarSrc=$("#currentAvatarPath").val();
				var loc_defaultFlag = false;
				if(isValid(avatarSrc)){
					$("#user_header_default div img").each(function(){
						if(this.src==avatarSrc){
							$("#user_header_default div input[name=defaultHeader][t="+$(this).attr("t")+"]").click();
							loc_defaultFlag = true;
						}
					});
					if(!loc_defaultFlag){
						$("#user_header_tab").tabs("select", "本地上传");
					}
				}
			}
		});
	},
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+systemUser.gridId).datagrid('reload');
	},
	/**
	 * 功能：重设密码
	 * @param recordId  dataGrid行Id
	 */
	resetPwd : function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/userController/resetPwd.do?id='+recordId);
		var submitUrl =  formatUrl(basePath + '/userController/saveResetPwd.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("user.resetpwd"),     /**重设密码*/
			height : 150,
			href : url,
			iconCls : 'ope-redo',
			handler : function(){    //提交时处理
				if($("#resetPwdForm").form('validate')){
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'resetPwdForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#myWindow").dialog("close");
								systemUser.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),'重置密码成功！您的新密码是：'+$("#pwd").val(),'info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("user.resetpwd.fail"),'error');	/**操作提示  重设密码失败!*/
							}
						}
					});
				}
			}
		});
	},
	/**
	 * 验证数字
	 */
	validWinRate:function(){
		var winRate = $('#winRate').val();
		if(!winRate){
			return true;
		}
		if(winRate.indexOf('%')!=-1){
			winRate = winRate.replace('%','');
		}else{
			$.messager.alert("提示信息","胜率为有效的百分比(两位小数),例如75.00%");
			$('#winRate').focus();
			return false;
		}
		winRate = parseFloat(winRate);
		if(isNaN(winRate)){
			$.messager.alert("提示信息","胜率为有效的百分比(两位小数),例如75.00%");
			$('#winRate').focus();
			return false;
		}else{
			if(winRate<1){
				winRate = winRate * 100;
			}
			$('#winRate').val((winRate).toFixed(2) + '%');
			return true;
		}
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/userController/batchDel.do');
		goldOfficeUtils.deleteBatch('system_user_datagrid',url,"userId");	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/userController/oneDel.do');
		goldOfficeUtils.deleteOne('system_user_datagrid',recordId,url);
	}
};
		
//初始化
$(function() {
	systemUser.init();
});