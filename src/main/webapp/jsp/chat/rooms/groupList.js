/**
 * 聊天室组别管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var chatGroup = {
	ruleComboxData:null,
	talkStyleComboxData:null,
	gridId : 'chatGroup_datagrid',
	init : function(){
		this.intRuleCombox();
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 初始化规则下拉框
	 */
	intRuleCombox:function(){
		var data=getJson(basePath +"/chatGroupRuleController/getGroupRuleCombox.do");
		chatGroup.talkStyleComboxData=getJson(basePath +"/commonController/getTalkStyleList.do");
		chatGroup.ruleComboxData=data;
		//设置规则的下拉框
		$("#chatRuleIds").combotree({
			panelWidth:300,
		    data:data
		});
		$("#chatTalkStyleId").combotree({
			panelWidth:180,
		    data:chatGroup.talkStyleComboxData
		}); 
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatGroup.gridId,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/chatGroupController/datagrid.do?status=' + $("#chatGroupStatus").val() + '&groupType='+$("#chatGroupType").val(),
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#chatGroup_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#chatGroup_datagrid_rowOperation").html();
						}},
			            {title : '编号',field : 'idStr',formatter : function(value, rowData, rowIndex) {
							return rowData.id;
						}},
						{title : '名称',field : 'name'},
						{title : '房间类别',field : 'groupTypeName',formatter : function(value, rowData, rowIndex) {
							return chatGroup.getDictNameByCode("#chatGroupType",rowData.groupType);
						}},
						{title : '状态',field : 'statusName',formatter : function(value, rowData, rowIndex) {
							return chatGroup.getDictNameByCode("#chatGroupStatus",rowData.status);
						}},
						{title : '聊天方式',field : 'talkStyle',formatter : function(value, rowData, rowIndex) {
							var nameArr=[],valTmp=rowData.talkStyle,tmpData=null;
							if(!!valTmp){
								for(var i in chatGroup.talkStyleComboxData){
									tmpData=chatGroup.talkStyleComboxData[i];
									if(valTmp.indexOf(tmpData.id)!=-1){
										nameArr.push(tmpData.text);
									}
								}
							}
							return nameArr.join("，");
						}},
						{title : '聊天规则',field : 'chatRuleName',formatter : function(value, rowData, rowIndex) {
							var chatRules=rowData.chatRules,result=[];
							if(chatRules==null){
								return "";
							}
							for(var index in chatRules){
								result.push(chatRules[index].name);
							}
							return result.join("，");
						}},
						{title : '最大人数',field : 'maxCount'},
						{title : '序号',field : 'sequence',sortable : true},
						{title : '开放时间(红色当前生效)',field : 'openDate',formatter : function(value, rowData, rowIndex) {
							if(!value){
								return "";
							}else{
								return "<font " + (dateTimeWeekCheck(value, true) ? "style='color:red;'" : "") + " >" + formatDateWeekTime(value) + "</font>"
							}
						}},
						/*{title : '创建人',field : 'createUser'},
						{title : '创建时间',field : 'createDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},*/
						{title : '修改人',field : 'updateUser'},
						{title : '修改时间',field : 'updateDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}}
						
			]],
			toolbar : '#chatGroup_datagrid_toolbar',
			onLoadSuccess:function(data){
				$("#"+chatGroup.gridId).datagrid("clearSelections");
				for(var i in data.rows){
					if($('#yxui_main_sysfag_select').val() == 'hx'){
						if(data.rows[i].status != 2){//if(data.rows[i].roomType!='train' && data.rows[i].roomType!='vip'){
							$("#chatGroup_datagrid_toolbar").next().find(".datagrid-view2 .datagrid-btable tr:eq("+i+")").find(".bookingUser").hide();
							$("#chatGroup_datagrid_toolbar").next().find(".datagrid-view2 .datagrid-btable tr:eq("+i+")").find(".importClient").hide();
						}
					}else {
						if (data.rows[i].roomType != 'train') {
							$("#chatGroup_datagrid_toolbar").next().find(".datagrid-view2 .datagrid-btable tr:eq(" + i + ")").find(".bookingUser").hide();
						}
						if (data.rows[i].roomType != 'vip') {
							$("#chatGroup_datagrid_toolbar").next().find(".datagrid-view2 .datagrid-btable tr:eq(" + i + ")").find(".importClient").hide();
						}
					}
				}
			}
			
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
		$("#chatGroup_queryForm_search").on("click",function(){
			var queryParams = $('#'+chatGroup.gridId).datagrid('options').queryParams;
			chatGroup.clearQueryParams(queryParams);
			$("#chatGroup_queryForm input[name],#chatGroup_queryForm select[name]").each(function(){
				var qp=queryParams[this.name];
				if(isValid(qp)){
					queryParams[this.name]+=(","+$(this).val());
				}else{
					queryParams[this.name] = $(this).val();
				}
			});
			$('#'+chatGroup.gridId).datagrid({
				url : basePath+'/chatGroupController/datagrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#chatGroup_queryForm_reset").on("click",function(){
			$("#chatGroup_queryForm")[0].reset();
		});
		
		$.fn.validatebox.defaults.rules['idString'] = { // 判断编号
			validator : function(value) {
				return /^\w+$/.test(value);
			},
			message : "编号只能由字母、数字和下划线组成。"
		};
	},
	/**
	 * 清空旧的参数
	 */
	clearQueryParams:function(queryParams){
		$("#chatGroup_queryForm input[name],#chatGroup_queryForm select").each(function(){
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
		var url = formatUrl(basePath + '/chatGroupController/add.do');
		var submitUrl =  formatUrl(basePath + '/chatGroupController/create.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			width : 750,
			height : 650,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#chatGroupSubmitForm").form('validate')){
					if(isNaN($("#chatGroupSubmitForm input[name=sequence]").val())){
						alert("排序：请输入数字！");
						return;
					}
					$("#chatGroup_openDate").val($("#chatGroup_openDate_div").dateTimeWeek("getData"));
					var reVal=$('#chatGroupSubmitForm textarea[name="remark"]').val();
					reVal=isValid(reVal)?reVal.replace(/[\r\n]/g,""):'';
					$('#chatGroupSubmitForm textarea[name="remark"]').val(reVal);
					var defTemplate = {'theme':$('#theme').val(),'style':$('#style').val()};
					if(isValid($('#theme').val()) && isValid($('#style').val())){
						$('#defTemplate').val(JSON.stringify(defTemplate));
					}else{
						$('#defTemplate').val('');
					}
					var logoJson = {'pc':$('#pc_logo').val(),'mb':$('#mb_logo').val()};
					$('#logo').val(JSON.stringify(logoJson));
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatGroupSubmitForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								// 提交成功后先关闭弹出框,然后刷新表格,弹出对应的成功提示
								$("#myWindow").dialog("close");
								chatGroup.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),'操作成功！','info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'新增失败，原因：'+d.msg,'error');	/**操作提示 新增失败!*/
							}
						}
					});
				}
			},
			onLoad :function(){
				chatGroup.initUploadFile();//初始化上传控件
			}
		});
	},
	/**
	 * 功能：修改
	 * @param recordId   dataGrid行Id
	 */
	edit : function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatGroupController/'+recordId+'/edit.do');
		var submitUrl =  formatUrl(basePath + '/chatGroupController/update.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			width : 750,
			height : 650,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if($("#chatGroupSubmitForm").form('validate')){
					if(isNaN($("#chatGroupSubmitForm input[name=sequence]").val())){
						alert("排序：请输入数字！");
						return;
					}
					$("#chatGroup_openDate").val($("#chatGroup_openDate_div").dateTimeWeek("getData"));
					var reVal=$('#chatGroupSubmitForm textarea[name="remark"]').val();
					reVal=isValid(reVal)?reVal.replace(/[\r\n]/g,""):'';
					$('#chatGroupSubmitForm textarea[name="remark"]').val(reVal);
					var defTemplate = {'theme':$('#theme').val(),'style':$('#style').val()};
					if(isValid($('#theme').val()) && isValid($('#style').val())){
						$('#defTemplate').val(JSON.stringify(defTemplate));
					}else{
						$('#defTemplate').val('');
					}
					var logoJson = {'pc':$('#pc_logo').val(),'mb':$('#mb_logo').val()};
					$('#logo').val(JSON.stringify(logoJson));
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatGroupSubmitForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#myWindow").dialog("close");
								chatGroup.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败，原因：'+d.msg,'error');  /**操作提示  修改失败!*/
							}
						}
					});
				}
			},
			onLoad :function(){
				chatGroup.initUploadFile();//初始化上传控件
			}
		});
	},
	/**
	 * 用户授权
	 */
	authUser: function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatGroupController/'+recordId+'/preAuthUser.do');
		var submitUrl =  formatUrl(basePath + '/chatGroupController/authUser.do');
		goldOfficeUtils.openEditorDialog({
			title : "用户授权",
			width : 550,
			height : 413,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){
				$("#groupUserAuth_authUsers option").each(function(){
					$(this).prop("selected", true);
				});
				goldOfficeUtils.ajaxSubmitForm({
					url : submitUrl,
					formId : 'groupUserAuth_form',
					onSuccess : function(data){   //提交成功后处理
						var d = $.parseJSON(data);
						if (d.success) {
							$("#myWindow").dialog("close");
							$.messager.alert($.i18n.prop("common.operate.tips"),'用户授权成功','info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'用户授权失败，原因：'+data.msg,'error');
						}
					}
				});
			}
		});
	},
	
	/**
	 * 设置token规则
	 */
	setToken : function(recordId){
		var url = formatUrl(basePath + '/chatGroupController/toSetToken.do?chatGroupId='+recordId);
		var submitUrl =  formatUrl(basePath + '/chatGroupController/setToken.do');
		goldOfficeUtils.openEditorDialog({
			title : '设置token',
			height : 120,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#setTokenForm").form('validate')){
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'setTokenForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								// 提交成功后先关闭弹出框,然后刷新表格,弹出对应的成功提示
								$("#myWindow").dialog("close");
								chatGroup.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),'操作成功！','info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'操作失败，原因：'+d.msg,'error');
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
		$('#'+chatGroup.gridId).datagrid('reload');
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/chatGroupController/del.do');
		goldOfficeUtils.deleteBatch('chatGroup_datagrid',url);	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#chatGroup_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatGroupController/del.do');
		goldOfficeUtils.deleteOne('chatGroup_datagrid',recordId,url);
	},
	/**
	 * 功能：查看
	 * @param recordId   dataGrid行Id
	 */
	bookingUser2 : function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatGroupController/'+recordId+'/getTrainClient.do');
		goldOfficeUtils.openSimpleDialog({
			title : '查看预约用户',       /**查看记录*/
			height : 575 ,
			href : url ,
			iconCls : 'pag-edit',			
		});
	},
	
	trainClient: function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatGroupController/'+recordId+'/getTrainClient.do');
		var submitUrl =  formatUrl(basePath + '/chatGroupController/authTrainClient.do');
		goldOfficeUtils.openEditorDialog({
			title : "查看报名客户",
			width : 550,
			height : 575,
			href : url,
			iconCls : 'pag-edit',
			onOpen:function(){
				$(".right_export_btn").parent().parent().css("float","left");
			},
			buttons:[
				{
					text:'导出',
					iconCls:"ope-export right_export_btn",
					left:"100",
					cls:"hhhh",
					handler:function(){
						var chatGroupId = $("#chatGroupId").val();
						window.location.href = formatUrl(basePath+"/chatGroupController/"+chatGroupId+"/exportUnAuthClient.do")
					}
				},
				{
					text : '提交',
					iconCls : "ope-save",
					handler : function(){
						var $select = $(this).parent().siblings().find('select');
						var chatGroupId = $("#chatGroupId").val();
						var unAuthTraninClientJson;
						var authTraninClientJosn;
						$select.each(function(i, n) {
							if($(n).hasClass('unAuthTraninClientSelect')) {
								unAuthTraninClientJson = yxui.findSelectMultipleValueStrong("clientId","nickname",n.options);

							};
							if($(n).hasClass('authTraninClientSelect')) {
								authTraninClientJosn = yxui.findSelectMultipleValueStrong("clientId","nickname",n.options);
							};
						});
						goldOfficeUtils.ajax({
							url : submitUrl,
							data : {
								chatGroupId:chatGroupId,
								unAuthTranin: unAuthTraninClientJson,
								authTranin: authTraninClientJosn
							},
							success : function(data){
								if (data.success) {
									$("#myWindow").dialog("close");
									$.messager.alert($.i18n.prop("common.operate.tips"),'报名审批成功','info');
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'报名审批失败，原因：'+data.msg,'error');
								}
							}
						});
					}
				},{
					text : '关闭',
					iconCls : "ope-close",
					handler : function() {
						$(this).parents(".easyui-dialog:first").dialog("close");
				}
			}]
		});
	},
	/**
	 * 导入客户
	 * @param recordId
	 */
	importClient : function(recordId){
		$("#system_user_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatGroupController/'+recordId+'/preImportClient.do');
		var submitUrl =  formatUrl(basePath + '/chatGroupController/importClient.do');
		goldOfficeUtils.openEditorDialog({
			title : "导入指定客户",
			width : 550,
			height : 450,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){
				var mobiles = $("#groupUserImport_form textarea").val();
				mobiles = mobiles || "";
				mobiles = mobiles.replace(/\s/g, "").replace(/，/g, ",");
				if(/^\d{11}(,\d{11})*$/.test(mobiles) == false){
					alert("手机号码有误！");
					return;
				}
				$("#groupUserImport_form textarea").val(mobiles);
				
				goldOfficeUtils.ajaxSubmitForm({
					url : submitUrl,
					formId : 'groupUserImport_form',
					onSuccess : function(data){
						data = $.parseJSON(data);
						if (data.success) {
							if(!data.obj || data.obj.length == 0){
								$("#myWindow").dialog("close");
								$.messager.alert($.i18n.prop("common.operate.tips"),'客户全部导入成功','info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'以下手机号未注册直播间，导入失败：<br>' + data.obj.join(", "),'info');
							}
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'报名审批失败，原因：'+data.msg,'error');
						}
					}
				});	
			}
		});
	},

	/**
	 * 上传文件
	 */
	upload:function(){
		var val=$("#mediaBaseInfoForm input[name=categoryId]").val();
		if(isBlank(val)){
			alert("请选择栏目！");
			return;
		}
		if(val.indexOf('advertisement')!=-1 || val=='sysPicture'){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'pic'});
		}else if(val=='student_style'){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'pic', 'saveSrc':'1'});
		}else if(val.indexOf("video")!=-1 || val.indexOf("audio")!=-1){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'video'});
		}else if(val == "download"){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'attach'});
		}else{
			alert("栏目：" + val + "对应的上传目录不存在！");
			return ;
		}
		$('#mediaFileId').uploadify('upload', '*');
	},
	/**
	 * 初始化上传控件
	 */
	initUploadFile : function(){
		//PC Logo
		goldOfficeUtils.uploadFile({
			'fileId' : 'mediaPcLogoFileId',
			'formData' : {'fileDir' : 'pic'},
			'fileSizeLimit' : 10*1024*1024,
			'fileTypeDesc': '只能上传*.jpg;*.gif;*.png;*.jpeg类型的图片',
			'fileTypeExts' : '*.jpg;*.gif;*.png;*.jpeg',
			'uploader' : basePath+'/uploadController/upload.do',
			'onUploadSuccess' : function(file, data, response){
				var d = eval("("+data+")");			//转换为json对象
				if(d.success){
					alert(file.name + ' 上传成功！');
					if(d.obj != null){
						$("#pc_logo").val(d.obj);
						$("#sourcePcLogoPath").val(d.obj);
						$("#cutedPcLogoPath").val(d.obj);
					}
				}else{
					alert(file.name + d.msg);
				}
			}
		});

		//MB Logo
		goldOfficeUtils.uploadFile({
			'fileId' : 'mediaMbLogoFileId',
			'formData' : {'fileDir' : 'pic'},
			'fileSizeLimit' : 10*1024*1024,
			'fileTypeDesc': '只能上传*.jpg;*.gif;*.png;*.jpeg类型的图片',
			'fileTypeExts' : '*.jpg;*.gif;*.png;*.jpeg',
			'uploader' : basePath+'/uploadController/upload.do',
			'onUploadSuccess' : function(file, data, response){
				var d = eval("("+data+")");			//转换为json对象
				if(d.success){
					alert(file.name + ' 上传成功！');
					if(d.obj != null){
						$("#mb_logo").val(d.obj);
						$("#sourceMbLogoPath").val(d.obj);
						$("#cutedMbLogoPath").val(d.obj);
					}
				}else{
					alert(file.name + d.msg);
				}
			}
		});
	},
	setLogoUrl:function(handerObj, target){
		$("#addMediaUrlHander").bind("click", function(){
			var loc_targetDom = $("#currentMediaPath");
			goldOfficeUtils.openSimpleDialog({
				dialogId : "addMediaUrl",
				title : '设置链接',
				height:130,
				onOpen : function(){
					var loc_url = loc_targetDom.val();
					if(isValid(loc_url)){
						$("#addMediaUrl input:radio").each(function(){
							if(loc_url.startsWith($(this).val())){
								$(this).prop("checked", true);
								var pDom=$(this).parent().next().find("input[pName]"),pName=pDom.attr("pName");
								var pnVal=loc_url.match(eval('/'+pName+'=([^&]+)/g'));
								if(isValid(pnVal)){
									pDom.val(pnVal.toString().replace(pName+'=',""));
									return false;
								}
							}
						});
					}
				},
				buttons	 : [{
					text : '清空',
					iconCls : "ope-close",
					handler : function() {
						$("#addMediaUrl form")[0].reset();
					}
				},{
					text : '确定',
					iconCls : "ope-save",
					handler : function(){
						var checkDom=$("#addMediaUrl input:checked"),pDom=checkDom.parent().next().find("input[pName]");
						var pVal=pDom.val(),pName=pDom.attr("pName");
						var locUrl = checkDom.val();
						if(isBlank(pVal)){
							return false;
						}
						if(locUrl.indexOf('&')==-1){
							locUrl = locUrl + '?'+pName+'='+ pVal;
						}else{
							locUrl += (/&$/g.test(locUrl)?"":"&") + pName + "=" +pVal;
						}
						loc_targetDom.val(locUrl);
						$("#addMediaUrl form")[0].reset();
						$("#addMediaUrl").dialog("close");
					}
				},{
					text : '关闭',
					iconCls : "ope-close",
					handler : function() {
						$("#addMediaUrl form")[0].reset();
						$("#addMediaUrl").dialog("close");
					}
				}]
			});
		});
	}
};
		
//初始化
$(function() {
	chatGroup.init();
});