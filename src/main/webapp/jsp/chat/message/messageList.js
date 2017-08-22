/**
 * 聊天室内容管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var chatMessage = {
	gridId : 'chatMessage_datagrid',
	init : function(){
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatMessage.gridId,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/chatMessageController/datagrid.do',
			queryParams : {
					groupId : $("#chatMessageGroupId").val(),
					/*status : $("#chatMessageStatusId").val(),*/
					valid : $("#chatMessageValidId").val()
				},
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            /*{title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		*//**操作*//*
							$("#chatMessage_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#chatMessage_datagrid_rowOperation").html();
						}},*/
						/*{title : '手机号码',field:'mobilePhone'},*/
						{title : '账号',field:'accountNo', formatter : function(value, rowData, rowIndex) {
							return rowData.accountNo||rowData.userId;
						}},
						{title : '昵称【ID号】',field : 'nickname', formatter : function(value, rowData, rowIndex) {
							return rowData.nickname+"【"+rowData.userId+"】";
						}},
						{title : '头像',field : 'avatar', formatter : function(value, rowData, rowIndex) {
							return !value ? value : '<img style="height:30px;background-color: #D0D0D0;" src="' + value + '">';
						}},
						{title : '信息类型',field : 'msgType',formatter : function(value, rowData, rowIndex) {
							var type=rowData.content.msgType;
							if(type=='text'){
								return "文本";
							}
							if(type=='img'){
								return "图片";
							}
							return "";
						}},
						{title : '内容',field : 'contentStr',formatter : function(value, rowData, rowIndex) {
							return rowData.content.msgType!='text'?'<img src="'+rowData.content.value+'"/>':rowData.content.value;
						}},
						{title : '发布时间',field : 'publishTime',sortable : true,formatter : function(value, rowData, rowIndex) {
							return rowData.publishTime ? timeObjectUtil.longMsTimeConvertToDateTime(Number(rowData.publishTime.replace(/_.+/,""))) : '';
						}},
						{title : '用户类型',field : 'typeName',formatter : function(value, rowData, rowIndex) {
							var loc_val = rowData.userType;
							if(loc_val === 0){
								if(!rowData.clientGroup){
									return "真实";
								}else{
									loc_val = rowData.clientGroup;
								}
							}
							return $.trim(chatMessage.getComboxNameByCode("#chatMessageUserType",loc_val, "[s='1']"));
						}},
			            {title : '房间名称',field : 'groupName',formatter : function(value, rowData, rowIndex) {
							return $.trim(chatMessage.getComboxNameByCode("#chatMessageGroupId",rowData.groupId));
						}},
						{title : '接收者',field : 'toUserName',formatter : function(value, rowData, rowIndex) {
							var toUser=rowData.toUser;
							if(toUser){
								if(isBlank(toUser.userId)){
									return "房间所有人";
								}else{
									return toUser.nickname;
								}
							}
						}},
						{title : '审核状态',field : 'status',formatter : function(value, rowData, rowIndex) {
							var type=rowData.status;
							if(type==1){
								return "通过";
							}
							else if(type==2){
								return "拒绝";
							}else{
								return "待审批";
							}
						}},
						{title : '用户id',field : 'userId',hidden:true},
						{title : '组',field : 'groupId',hidden:true},
						{title : '数据状态',field : 'valid',formatter : function(value, rowData, rowIndex) {
							var type=rowData.valid;
							if(type==0){
								return "删除";
							}
							else{
								return "正常";
							}
						}}
						
			]],
			toolbar : '#chatMessage_datagrid_toolbar'
		});
	},
	/**
	 * 从下拉框中提取名称
	 */
	getComboxNameByCode:function(domId,code, filter){
		return $(domId).find("option[value='"+code+"']" + (filter || "")).text();
	},
	setEvent:function(){
		// 列表查询
		$("#chatMessage_queryForm_search").on("click",function(){
			var startDate=$("#publishStartDate").val(),
					endDate=$("#publishEndDate").val(),
					toUserLabel = $('#toUserLabel').val(),
					userId = $('#toUserInput').val();
			if((isValid(startDate) && isBlank(endDate))||(isValid(endDate) && isBlank(startDate))){
				$.messager.alert("操作提示","请输入完整的发布时间段！",'info');
				return false;
			}
			if(isValid(startDate)&& isValid(endDate) && startDate.substring(0,4)!= endDate.substring(0,4)){
				$.messager.alert("操作提示","暂不支持查询跨年数据！",'info');
				return false;
			}
			/*if(isValid(toUserLabel) && isBlank(userId)){
				$.messager.alert("操作提示","请选择一个接收者!",'info');
				return false;
			}*/
			var queryParams = $('#'+chatMessage.gridId).datagrid('options').queryParams;
			$("#chatMessage_queryForm input[name],#chatMessage_queryForm select[name]").each(function(){
				queryParams[this.name] = $(this).val();
			});
			$('#'+chatMessage.gridId).datagrid({
				url : basePath+'/chatMessageController/datagrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#chatMessage_queryForm_reset").on("click",function(){
			$("#chatMessage_queryForm")[0].reset();
		});
		$("#chatMessageGroupId").change(function(){
			 $("#chatMessageUserType option").show().attr("s", "1");
			 if(this.value.indexOf("studio")!=-1){
				 var val=this.value;
				 $("#chatMessageUserType option[t]").each(function(){
					 if(val.startsWith($(this).attr("t"))){
						 $(this).show().attr("s", "1");
					 }else{
						 $(this).hide().attr("s", "0");
					 }
				 });
			 }else{
				 $("#chatMessageUserType option[t]").hide().attr("s", "0"); 
			 }
			 
		});
		$("#chatMessageGroupId").trigger("change");
		$('#toUserLabel').change(function(){
			var val = $(this).val();
			if($.inArray(val, ['1','2','3']) > -1){
				chatMessage.setAuthorList(val);
				$('#toUser').next('.combo').show();
			}else{
				$('#toUser').combogrid({destroy:function(){}});
				$('#toUser').next('.combo').hide();
			}
		});
	},
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatMessage.gridId).datagrid('reload');
	},
	/**
	 * 审核记录
	 */
	approval:function(_this){
		var rows = $("#"+chatMessage.gridId).datagrid('getSelections');
		var idArr = [],fuIdArr=[],groupId='',userId='',isWait=true;
		if(rows.length > 0){
			for(var i = 0; i < rows.length; i++) {
				groupId=rows[i]["groupId"];
				idArr.push(rows[i]["publishTime"]);
				userId=rows[i]["userId"];
				if(rows[i]["status"]!=0){
					isWait=false;
					break;
				}
				if(fuIdArr.length>0){
	                  var fArrStr=","+fuIdArr.join(",")+",";
	                  if(fArrStr.indexOf(","+userId+",")==-1){
	                      fuIdArr.push(userId);
	                  }
	              }else{
	                  fuIdArr.push(userId);
	              }
			}
			if(!isWait){
				$.messager.alert("操作提示","请选择待审核的记录！",'info');
				return;
			}
			goldOfficeUtils.ajax({
				url : formatUrl(basePath + '/chatMessageController/approvalMsg.do'),
				data : {
					publishTimeArr : idArr.join(','),
					fuIdArr:fuIdArr.join(','),
					status:$(_this).attr("btnType"),
					groupId:groupId
				},
				success: function(data) {
					if(data.success) {
						$("#"+chatMessage.gridId).datagrid('unselectAll');
						$('#'+chatMessage.gridId).datagrid('reload');
						$.messager.alert("操作提示","执行成功!",'info');
					}else{
						$.messager.alert($.i18n.prop("common.operate.tips"),'执行失败，原因：'+data.msg,'error');
			    	}
				}
			});
		 }else{
			 $.messager.alert("操作提示", "请选择记录!"); 
		 }
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var rows = $("#"+chatMessage.gridId).datagrid('getSelections');
		var year=new Date().getFullYear();
		if(rows.length>0){
			year=new Date(Number(rows[0]["publishTime"].replace(/_.+/,""))).getFullYear();
		}
		var url = formatUrl(basePath + '/chatMessageController/del.do?year='+year);
		goldOfficeUtils.deleteBatch('chatMessage_datagrid',url);	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#chatMessage_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatMessageController/del.do');
		goldOfficeUtils.deleteOne('chatMessage_datagrid',recordId,url);
	},
	/**
	 * 功能：导出记录
	 */
	exportRecord : function(){
		var loc_params = $('#'+chatMessage.gridId).datagrid('options').queryParams;
		if(isBlank(loc_params.publishStartDateStr)||isBlank(loc_params.publishEndDateStr)){
			$.messager.alert("操作提示","请输入发布时间查询后再导出",'info');
			return;
		}
		var beginDate=new Date(loc_params.publishStartDateStr),endDate=new Date(loc_params.publishEndDateStr);
		beginDate.setMonth(beginDate.getMonth()+1);
		if(endDate>beginDate){
			$.messager.alert("操作提示","目前导出数据暂支持发布时间段为一个月，请检查发布时间段！",'info');
			return;
		}
		loc_params['hasOther'] = true;
		var path = basePath+ '/chatMessageController/exportRecord.do?'+$.param(loc_params);
		window.location.href = path;
	},
	/**
	 * 设置作者选择列表
	 * @param id
	 */
	setAuthorList:function(userType){
		$('#toUser').combogrid({
			idField:'userNo',
			textField:'userName',
			panelWidth: 200,
			url:basePath+'/userController/getAnalystList.do?hasOther=true&userType='+userType,
			columns:[[
				{field : 'userNo', hidden:true},
				{field : 'author_Key_id',hidden:true,formatter : function(value, rowData, rowIndex) {
					return 'author_Key_id';
				}},
				{field : 'userName',title : '姓名', width:100},
				{field : 'position', hidden:true},
				{field : 'avatar',title : '头像',width:40,formatter : function(value, rowData, rowIndex) {
					if(isBlank(value)){
						return '';
					}
					return '<img src="'+value+'" style="height:35px;width:35px;"/>';
				}}
			]],
			onSelect:function(rowIndex, rowData){
				$('#toUserInput').val(rowData.userNo);
			},
			onChange:function(val){
				//$('#'+id+'Input').val(val);
			},
			onBeforeLoad:function(){
				$('#toUser').next('span.combo').find('input').width(200);
			}
		});
	}
};
		
//初始化
$(function() {
	chatMessage.init();
});