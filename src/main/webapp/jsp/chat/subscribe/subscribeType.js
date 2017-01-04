var chatSubscribeType = {
		gridId : 'subscribeType_datagrid',
		opType : '',
		init: function(){
			this.opType = $("#userOpType").val();
			this.initGrid();
			this.setEvent();
			this.setUserList();
		},
		initGrid:function(){
			goldOfficeUtils.dataGrid({
				gridId : chatSubscribeType.gridId,
				idField:"id",
				sortName : 'startDate',
				sort:'desc',
				singleSelect : false,
				url : basePath+'/chatSubscribeTypeController/datagrid.do?opType=' + chatSubscribeType.opType,
				columns : [[
				            {title : 'id',field : 'id',checkbox : true},
				            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
								$("#subscribeType_datagrid_rowOperation a").each(function(){
									$(this).attr("id",rowData.id);
							    });
								return $("#subscribeType_datagrid_rowOperation").html();
							}},
							
				            {title : '名称',field : 'name'/*,formatter : function(value, rowData, rowIndex) {
								return chatSubscribeType.subscribeTypeObj[rowData.type];
							}*/},                   	
				            {title : '编码',field : 'code'/*,sortable : true,formatter : function(value, rowData, rowIndex) {
								return formatMobileToUserId(rowData.userId);
							}*/},     
							{title : '房间组别',field : 'groupType',formatter : function(value, rowData, rowIndex) {
								return chatSubscribeType.getDictNameByCode("#subscribeType_groupType_select",rowData.groupType);
							}},             	
				            {title : '订阅老师',field : 'analysts',sortable : true, formatter : function(value, rowData, rowIndex) {
				            	var analystsArr = [];
				            	$.each(JSON.parse(value),function(i,row){
				            		analystsArr.push(row.name);
				            	});
				            	return analystsArr.join('，');
							}},
							{title:'订阅方式',field:'noticeTypes', formatter:function(value, rowData, rowIndex){
								var noticeTypes = [];
								$.each(JSON.parse(value),function(i,row){
									noticeTypes.push(row.name);
								});
								return noticeTypes.join('，');
							}},
							{title : '可订阅周期',field : 'noticeCycle',formatter : function(value, rowData, rowIndex) {
								var noticeCycle = [];
								$.each(JSON.parse(value),function(i,row){
									noticeCycle.push(row.name);
								});
								return noticeCycle.join('，');
							}},
							{title : "开始时间", field : 'startDate' ,sortable : true, formatter : function(value, rowData, rowIndex) {
								return rowData.startDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.startDate) : '';
							}},
							{title : "结束时间", field : 'endDate' ,sortable : true, formatter : function(value, rowData, rowIndex) {
								return rowData.endDate? timeObjectUtil.longMsTimeConvertToDateTime(rowData.endDate) : '';
							}},
							{title:'状态',field : 'status' ,formatter : function(value, rowData, rowIndex) {
								if(rowData.status==1){
									return '有效';
								}else if(rowData.status==0){
									return '无效';
								}
							}},
							{title : "备注", field : 'remark'}
							
				]],
				toolbar : '#subscribeType_datagrid_toolbar'
			});
		},
		setEvent:function(){
			// 列表查询
			$("#subscribeType_queryForm_search").on("click",function(){  
				var groupType = $("#subscribeType_queryForm #subscribeType_groupType_select").val();  
				var status = $('#subscribeType_queryForm #subscribeType_status_select').val();
				var queryParams = $('#'+chatSubscribeType.gridId).datagrid('options').queryParams;
				var name = $('#subscribeType_queryForm #name').val();
				var analysts = $('#subscribeType_queryForm [name="analysts"]').val();
				queryParams['groupType'] = groupType;
				queryParams['analysts'] = analysts;
				queryParams['status'] = status;
				queryParams['name'] = name;
				$('#'+chatSubscribeType.gridId).datagrid({
					url : basePath+'/chatSubscribeTypeController/datagrid.do?opType=' + chatSubscribeType.opType,
					pageNumber : 1
				});
			});
			// 重置
			$("#subscribeType_queryForm_reset").on("click",function(){
				$("#subscribeType_queryForm")[0].reset();
			});
		},
		//显示用户列表
		setUserList:function(){
		     chatSubscribeType.setAnalystList("analystsSelectId");
		},
		setUserEdit:function(value){
			var analystArr = JSON.parse(value),analysts = [],analystsHtml=[];
			$.each(analystArr, function(i, row){
				analysts.push(row.userId);
				analystsHtml.push('<span id="'+row.userId+'_span" style="float:left;margin-right:3px;">');
				analystsHtml.push('<input type="text" class="analyst" readonly="readonly" id="'+row.userId+'" value="'+row.name+'" style="width:70px;margin-right:3px;" />');
				analystsHtml.push('<input type="text" class="analystPoint" value="'+row.point+'" style="width:50px;" />积分');
				analystsHtml.push('</span>');
			});
			$('#analystsContent').html(analystsHtml.join(''));
			chatSubscribeType.setAnalystList("analystsEditSelectId", analysts.join(','), true);
		},
		setUserAdd:function(){
			chatSubscribeType.setAnalystList("analystsAddSelectId", '', true);
		},
		/**
		 * 功能：查看
		 * @param recordId   dataGrid行Id
		 */
		view : function(recordId){
			$("#subscribeType_datagrid").datagrid('unselectAll');
			var url = formatUrl(basePath + '/chatSubscribeTypeController/'+recordId+'/view.do');
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
			var url = formatUrl(basePath + '/chatSubscribeTypeController/add.do?opType=' + chatSubscribeType.opType);
			var submitUrl =  formatUrl(basePath + '/chatSubscribeTypeController/create.do');
			goldOfficeUtils.openEditorDialog({
				dialogId : "editWindow",
				title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
				width : 750,
				height : 350,
				href : url,
				iconCls : 'pag-add',
				handler : function(){   //提交时处理
					chatSubscribeType.setAnalystNoticeTypeNoticeCycleVal("#subscribeTypeAddFrom");
					if(chatSubscribeType.validate("#subscribeTypeAddFrom")){//if($("#subscribeAddFrom").form('validate')){//validatebox-invalid
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'subscribeTypeAddFrom',
							onSuccess : function(data){  //提交成功后处理
								var d = $.parseJSON(data);
								if(d.success) {
									$("#editWindow").dialog("close");
									chatSubscribeType.refresh();
									$.messager.alert("操作提示",'新增订阅成功');
								}else{
									$.messager.alert('错误提示','新增订阅失败，原因：订阅老师'+d.msg);
								}
							}
						});
					}
				},
				onLoad : function(){
					chatSubscribeType.setUserAdd();
				}
			});
		},
		/**
		 * 功能：修改
		 * @param recordId   dataGrid行Id
		 */
		edit : function(recordId){
			$("#subscribeType_datagrid").datagrid('unselectAll');
			var url = formatUrl(basePath + '/chatSubscribeTypeController/'+recordId+'/edit.do?opType=' + chatSubscribeType.opType);
			var submitUrl =  formatUrl(basePath + '/chatSubscribeTypeController/update.do');
			goldOfficeUtils.openEditorDialog({
				dialogId : "editWindow",
				title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
				width : 750,
				height : 350,
				href : url,
				iconCls : 'pag-edit',
				handler : function(){    //提交时处理
					chatSubscribeType.setAnalystNoticeTypeNoticeCycleVal("#subscribeTypeEditFrom");
					if(chatSubscribeType.validate("#subscribeTypeEditFrom")){//if($("#subscribeEditFrom").form('validate')){
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'subscribeTypeEditFrom',
							onSuccess : function(data){   //提交成功后处理
								var d = $.parseJSON(data);
								if (d.success) {
									$("#editWindow").dialog("close");
									chatSubscribeType.refresh();
									$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败','error');  /**操作提示  修改失败!*/
								}
							}
						});
					}
				},
				onLoad : function(){
					//chatSubscribeType.setUserEdit($('#chatSubscribeEditAnalystInput').attr('data-userName'));
				}
			});
		},
		/**
		 * 提取名称
		 */
		getDictNameByCode:function(id,code){
			return $(id).find("option[value='"+code+"']").text();
		},
		/**
		 * 功能：刷新
		 */
		refresh : function(){
			$('#'+chatSubscribeType.gridId).datagrid('reload');
		},
		/**
		 * 功能：批量删除
		 */
		batchDel : function(){
			var url = formatUrl(basePath + '/chatSubscribeTypeController/batchDel.do');
			goldOfficeUtils.deleteBatch('subscribeType_datagrid',url);	
		},
		/**
		 * 功能：删除单行
		 * @param recordId  dataGrid行Id
		 */
		del : function(recordId){
			$("#subscribeType_datagrid").datagrid('unselectAll');
			var url = formatUrl(basePath + '/chatSubscribeTypeController/oneDel.do');
			goldOfficeUtils.deleteOne('subscribeType_datagrid',recordId,url);
		},
		/**
		 * 设置分析师选择列表
		 * @param id
		 */
		setAnalystList:function(id, analyst, isEdit){
			if(!isEdit){
				$('#'+id).combotree({
					data:getJson(basePath+"/chatSubscribeTypeController/getMultipleCkAnalystList.do",{analysts:analyst})
				});
			} else {
				$('#'+id).combotree({
					data:getJson(basePath+"/chatSubscribeTypeController/getMultipleCkAnalystList.do",{analysts:analyst}),
					onCheck:function(node,checked){
						if(checked && $('#'+node.id+'_span').size()==0){
							var html = [];
							html.push('<span id="'+node.id+'_span" style="float:left;margin-right:3px;">');
							html.push('<input type="text" class="analyst" readonly="readonly" id="'+node.id+'" value="'+node.text+'" style="width:70px;margin-right:3px;" />');
							html.push('<input type="text" class="analystPoint" style="width:50px;" />积分');
							html.push('</span>');
							$('#analystsContent').append(html.join(''));
						}else if(checked == false && $('#'+node.id+'_span').size()==1){
							$('#analystsContent #'+node.id+'_span').remove();
						}
					}
				});
			}
		},
		validate:function(form){
			var errorCount = 0;
			if(isBlank($(form+' #name').val())){
				$(form+' #name').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$(form+' #name').removeClass('validatebox-invalid');
			}
			if(isBlank($(form+' #subscribeType_groupType_select').val())){
				$(form+' #subscribeType_groupType_select').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$(form+' #subscribeType_groupType_select').removeClass('validatebox-invalid');
			}
			if(isBlank($(form+' #startDate').val())){
				$('.startTip').show();
				$(form+' #startDate').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$('.startTip').hide();
				$(form+' #startDate').removeClass('validatebox-invalid');
			}
			if(isBlank($(form+' #endDate').val())){
				$('.endTip').show();
				$(form+' #endDate').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$('.endTip').hide();
				$(form+' #endDate').removeClass('validatebox-invalid');
			}
			if(isBlank($(form+' #code').val())){
				$(form+' #code').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$(form+' #code').removeClass('validatebox-invalid');
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
				$(form+' input[name="noticeType"]').each(function(){
					if($(this).is(':checked') && isBlank($('#'+$(this).val()+'Point').val())){
						$('#'+$(this).val()+'Point').addClass('validatebox-invalid');
						errorCount++;
					}else{
						$('#'+$(this).val()+'Point').removeClass('validatebox-invalid');
					}
				});
			}else{
				$('.noticeTip').show();
				$(form+' input[name="noticeType"]').addClass('validatebox-invalid');
				errorCount++;
			}
			if(isBlank($(form+' [name="analysts"]').val())){
				$('.analystsTip').show();
				$(form+' .analysts').addClass('validatebox-invalid');
				errorCount++;
			}else{
				$(form+' .analystPoint').each(function(){
					if(isBlank($(this).val())){
						$(this).addClass('validatebox-invalid');
						errorCount++;
					}else{
						$(this).removeClass('validatebox-invalid');
					}
				});
				$(form+' .analysts').removeClass('validatebox-invalid');
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
		 * 组合订阅老师/方式/周期值
		 */
		setAnalystNoticeTypeNoticeCycleVal: function(form){
			var analystsArr = [], noticeTypesArr = [], noticeCycleArr = [];
			$(form+' #analystsContent span').each(function(){
				var analyst = {'userId':$(this).find('.analyst').attr('id'),'name':$(this).find('.analyst').val(),'point':$(this).find('.analystPoint').val()};
				analystsArr.push(analyst);
			});
			$(form+' #analysts').val(JSON.stringify(analystsArr));
			$(form+' input[name="noticeType"]').each(function(){
				if($(this).is(':checked')){
					var noticeType = {'type':$(this).val(),'name':$(this).attr('cval'),'point':$(form+' #'+$(this).val()+'Point').val()};
					noticeTypesArr.push(noticeType);
				}
			});
			$(form+' #noticeTypes').val(JSON.stringify(noticeTypesArr));
			$(form+' input[name="cycle"]').each(function(){
				if($(this).is(':checked')){
					var cycle = {'cycle':$(this).val(),'name':$(this).attr('cval'),'point':$(form+' #'+$(this).val()+'Point').val()};
					noticeCycleArr.push(cycle);
				}
			});
			$(form+' #noticeCycle').val(JSON.stringify(noticeCycleArr));
		}
}; 

//初始化
$(function() {
	chatSubscribeType.init();
	
});