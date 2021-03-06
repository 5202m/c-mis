var chatSubscribe = {
		gridId : 'subscribe_datagrid',
		init: function(){
			this.initGrid();
			this.setEvent();
			this.setUserList();
		},
		initGrid:function(){
			goldOfficeUtils.dataGrid({
				gridId : chatSubscribe.gridId,
				idField:"id",
				sortName : 'startDate',
				sort:'desc',
				singleSelect : false,
				url : basePath+'/chatSubscribeController/datagrid.do',
				columns : [[
							{title : 'id',field : 'id',checkbox : true},
							{title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
								$("#subscribe_datagrid_rowOperation a").each(function(){
									$(this).attr("id",rowData.id);
							    });
								return $("#subscribe_datagrid_rowOperation").html();
							}},
							
							{title : '订阅服务类型',field : 'type',formatter : function(value, rowData, rowIndex) {
								var types = !value ? [] : value.split(/[,，]/);
								for(var i = 0, lenI = types.length; i < lenI; i++){
									types[i] = chatSubscribe.getDictNameByCode('#type', types[i]);
								}
								return types.join("，");
							}},
				            {title : '用户ID',field : 'userId',sortable : true,formatter : function(value, rowData, rowIndex) {
								return formatMobileToUserId(rowData.userId);
							}},                  	
				            {title : '订阅老师',field : 'analyst',sortable : true,formatter : function(value, rowData, rowIndex) {
								return chatSubscribe.getAnalystCNameByCode(value);
							}},
							{title:'订阅方式',field:'noticeType', formatter:function(value, rowData, rowIndex){
								if(rowData.noticeType=='email'){
									return '邮件';
								}else if(rowData.noticeType=='sms'){
									return '短信';
								}else if(rowData.noticeType=='wechat'){
									return '微信';
								}
							}},
							{title : '房间组别',field : 'groupType',formatter : function(value, rowData, rowIndex) {
								return chatSubscribe.getDictNameByCode("#subscribe_groupType_select",rowData.groupType);
							}},
							{title : "订阅开始时间", field : 'startDate' ,sortable : true, formatter : function(value, rowData, rowIndex) {
								return rowData.startDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.startDate) : '';
							}},
							{title : "订阅结束时间", field : 'endDate' ,sortable : true, formatter : function(value, rowData, rowIndex) {
								return rowData.endDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.endDate) : '';
							}},
							{title:'状态',field : 'status' ,formatter : function(value, rowData, rowIndex) {
								if(rowData.status==1){
									return '有效';
								}else if(rowData.status==0){
									return '无效';
								}
							}},
							{title : "备注", field : 'remarks'}
							
				]],
				toolbar : '#subscribe_datagrid_toolbar'
			});
		},
		setEvent:function(){
			// 列表查询
			$("#subscribe_queryForm_search").on("click",function(){
				var queryParams = $('#'+chatSubscribe.gridId).datagrid('options').queryParams;
				$('#subscribe_queryForm select[name],#subscribe_queryForm input[name]').each(function(){
					queryParams[$(this).attr("name")] = $(this).val();
				});
				var analystArr = $("#analystsSelectId").combo("getValues");
				queryParams["analyst"] = analystArr && analystArr.length > 0 ? analystArr.join(",") : "";

				$('#'+chatSubscribe.gridId).datagrid({
					url : basePath+'/chatSubscribeController/datagrid.do',
					pageNumber : 1
				});
			});
			// 重置
			$("#subscribe_queryForm_reset").on("click",function(){
				$("#subscribe_queryForm")[0].reset();
			});
			$('#subscribeAddFrom #type,#subscribeEditFrom #type').change(function(){
				if($.inArray($(this).val(),['live_reminder','shout_single_strategy','trading_strategy'])>-1){
					$('input[name="noticeType"][value="sms"]').parent().show();
				}else{
					$('input[name="noticeType"][value="sms"]').parent().hide();
				}
			});
		},
		//显示用户列表
		setUserList:function(){
		     chatSubscribe.setAnalystList("analystsSelectId");
		},
		setUserEdit:function(value){
			chatSubscribe.setAnalystList("chatSubscribeEditAnalyst");
		     $('#chatSubscribeEditAnalyst').combogrid('setValue', value);
		},
		setUserAdd:function(){
			chatSubscribe.setAnalystList("chatSubscribeAddAnalyst");
		},
		/**
		 * 功能：查看
		 * @param recordId   dataGrid行Id
		 */
		view : function(recordId){
			$("#subscribe_datagrid").datagrid('unselectAll');
			var url = formatUrl(basePath + '/chatSubscribeController/'+recordId+'/view.do');
			goldOfficeUtils.openSimpleDialog({
				title : $.i18n.prop("common.operatetitle.view"),       /**查看记录*/
				height : 575 ,
				href : url ,
				iconCls : 'pag-view'
			});
		},
		/**
		 * 功能：增加
		 */
		add : function(){
			
			var url = formatUrl(basePath + '/chatSubscribeController/add.do');
			var submitUrl =  formatUrl(basePath + '/chatSubscribeController/create.do');
			goldOfficeUtils.openEditorDialog({
				dialogId : "editWindow",
				title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
				width : 1000,
				height : 500,
				href : url,
				iconCls : 'pag-add',
				handler : function(){   //提交时处理
					if(chatSubscribe.validate("#subscribeAddFrom")){//if($("#subscribeAddFrom").form('validate')){//validatebox-invalid
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'subscribeAddFrom',
							onSuccess : function(data){  //提交成功后处理
								var d = $.parseJSON(data);
								if(d.success) {
									$("#editWindow").dialog("close");
									chatSubscribe.refresh();
									$.messager.alert("操作提示",'新增订阅成功');
								}else{
									$.messager.alert('错误提示','新增订阅失败，原因：订阅老师'+d.msg);
								}
							}
						});
					}
				},
				onLoad : function(){
					//chatSubscribe.setUserAdd();
				}
			});
		},
		/**
		 * 功能：修改
		 * @param recordId   dataGrid行Id
		 */
		edit : function(recordId){
			$("#subscribe_datagrid").datagrid('unselectAll');
			var url = formatUrl(basePath + '/chatSubscribeController/'+recordId+'/edit.do');
			var submitUrl =  formatUrl(basePath + '/chatSubscribeController/update.do');
			goldOfficeUtils.openEditorDialog({
				dialogId : "editWindow",
				title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
				width : 1000,
				height : 500,
				href : url,
				iconCls : 'pag-edit',
				handler : function(){    //提交时处理
					if(chatSubscribe.validate("#subscribeEditFrom")){//if($("#subscribeEditFrom").form('validate')){
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'subscribeEditFrom',
							onSuccess : function(data){   //提交成功后处理
								var d = $.parseJSON(data);
								if (d.success) {
									$("#editWindow").dialog("close");
									chatSubscribe.refresh();
									$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败','error');  /**操作提示  修改失败!*/
								}
							}
						});
					}
				}
			});
		},
		/**
		 * 提取名称
		 */
		getDictNameByCode:function(id,code){
			return $(id).find("option[value='"+code+"']:first").text();
		},
		/**
		 * 功能：刷新
		 */
		refresh : function(){
			$('#'+chatSubscribe.gridId).datagrid('reload');
		},
		/**
		 * 功能：批量删除
		 */
		batchDel : function(){
			var url = formatUrl(basePath + '/chatSubscribeController/batchDel.do');
			goldOfficeUtils.deleteBatch('subscribe_datagrid',url);	
		},
		/**
		 * 功能：删除单行
		 * @param recordId  dataGrid行Id
		 */
		del : function(recordId){
			$("#subscribe_datagrid").datagrid('unselectAll');
			var url = formatUrl(basePath + '/chatSubscribeController/oneDel.do');
			goldOfficeUtils.deleteOne('subscribe_datagrid',recordId,url);
		},
		/**
		 * 设置分析师选择列表
		 * @param id
		 */
		setAnalystList:function(id, analyst){
			$('#'+id).combotree({
				data:getJson(basePath+"/chatSubscribeTypeController/getMultipleCkAnalystList.do",{analysts:analyst, hasOther: true})
			});
		},
		validate:function(form){
			//validatebox-invalid
			var errorCount = 0;
			if(isBlank($(form+' #type').val())){
				$(form+' #type').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$(form+' #type').removeClass('validatebox-invalid');
			}
			if($.inArray($(form+' #type').val(),['live_reminder','shout_single_strategy','trading_strategy'])>-1){
				if(isBlank($(form+' input[name="analyst"]').val())){
					$(form+' input[name="analyst"]').next().next().find('.combo-text').addClass('validatebox-invalid');
					errorCount++;
				}else{
					$(form+' input[name="analyst"]').next().next().find('.combo-text').removeClass('validatebox-invalid');
				}
				if($(form+' input[name="noticeCycle"]').is(':checked')){
					$('.noticeCycleTip').hide();
					$(form+' input[name="noticeCycle"]').removeClass('validatebox-invalid');
				}else{
					$('.noticeCycleTip').show();
					$(form+' input[name="noticeCycle"]').addClass('validatebox-invalid');
					errorCount++;
				}
				if(isBlank($(form+' #point').val())){
					$(form+' #point').addClass('validatebox-invalid');
					errorCount++;
				}else{
					$(form+' #point').removeClass('validatebox-invalid');
				}
			}
			if($(form+' input[name="status"]').is(':checked')){
				$('.statusTip').hide();
				$(form+' input[name="status"]').removeClass('validatebox-invalid');
			}else{
				$('.statusTip').show();
				$(form+' input[name="status"]').addClass('validatebox-invalid');
				errorCount++;
			}
			if($(form+' input[name="noticeType"]').is(':checked')){
				$('.noticeTip').hide();
				$(form+' input[name="noticeType"]').removeClass('validatebox-invalid');
			}else{
				$('.noticeTip').show();
				$(form+' input[name="noticeType"]').addClass('validatebox-invalid');
				errorCount++;
			}
			if(isBlank($(form+' input[name="userId"]').val())){
				$(form+' input[name="userId"]').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$(form+' input[name="userId"]').removeClass('validatebox-invalid');
			}
			if(isBlank($(form+' #subscribe_groupType_select').val())){
				$(form+' #subscribe_groupType_select').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$(form+' #subscribe_groupType_select').removeClass('validatebox-invalid');
			}
			return errorCount>0?false:true;
		},
		/**
		 * 提取名称
		 * @param value
		 */
		getAnalystCNameByCode:function(value){
			var cName = [];
			if(isValid(value)){
				value = value.split(',');
				$.each(value, function(key, val){
					cName.push($('div[node-id="'+val+'"] span.tree-title:first').text());
				});
			}
			return cName.join('，');
		},
	/**
	 * 功能：导出记录
	 */
	exportRecord : function(){
		var queryParams = $('#'+chatSubscribe.gridId).datagrid('options').queryParams;
		$('#subscribe_queryForm select[name],#subscribe_queryForm input[name]').each(function(){
			queryParams[$(this).attr("name")] = $(this).val();
		});
		var analystArr = $("#analystsSelectId").combo("getValues");
		queryParams["analyst"] = analystArr && analystArr.length > 0 ? analystArr.join(",") : "";

		queryParams['hasOther'] = true;
		var path = basePath+ '/chatSubscribeController/exportRecord.do?'+$.param(queryParams);
		window.location.href = path;
	}
}; 

//初始化
$(function() {
	chatSubscribe.init();
	
});