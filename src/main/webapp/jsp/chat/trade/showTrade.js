/**
 * 摘要：晒单
 * @author Henry.cao
 * @date   2016-06-22
 */
var chatShowTrade = {
	gridId : 'show_trade_datagrid',
	opType : '',
	init : function(){
		this.opType = $("#userOpType").val();
		this.initGrid();
		this.setEvent();
		this.setUserList();
		this.setCommentEventView();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatShowTrade.gridId,
			idField:"id",
			sortName : 'showDate',
			sort:'desc',
			singleSelect : false,
			url : basePath+'/chatShowTradeController/datagrid.do?opType=' + chatShowTrade.opType,
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#show_trade_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#show_trade_datagrid_rowOperation").html();
						}},
						
						{title : '晒单人账号',field : 'boUser.userNo',formatter : function(value, rowData, rowIndex) {
							return rowData.boUser.userNo;
						}},                   	
						{title : $.i18n.prop("user.name"),field : 'boUser.userName',sortable : true,formatter : function(value, rowData, rowIndex) {
							return rowData.boUser.userName;
						}},
						{title:'类别',field:'tradeType', formatter:function(value, rowData, rowIndex){
							if(rowData.tradeType==1){
								return '分析师晒单';
							}else if(rowData.tradeType==2){
								return '客户晒单';
							}
						}},
						{title : '房间类别',field : 'groupTypeName',formatter : function(value, rowData, rowIndex) {
							return chatShowTrade.getDictNameByCode("#showTrade_groupType_select",rowData.groupType);
						}},
						
						{title : "头像",field : 'boUser.avatar' ,formatter : function(value, rowData, rowIndex) {
							return '<img src="'+rowData.boUser.avatar+'" style="height:60px;">';
						}},
						
						{title : "微信号",field : 'boUser.wechatCode' ,formatter : function(value, rowData, rowIndex) {
							return rowData.boUser.wechatCode;
						}},
						
						{title : "胜率",field : 'boUser.winRate' ,formatter : function(value, rowData, rowIndex) {
							return rowData.boUser.winRate;
						}},
			            
						{title : "获利",field : 'profit',sortable : true , formatter : function(value, rowData, rowIndex) {
							return rowData.profit == '' ? '持仓中' : rowData.profit
						}},
						{title : "晒单图片",field : 'tradeImg' , formatter : function(value, rowData, rowIndex) {
							return rowData.tradeImg ? '<a onclick="return chatShowTrade.setViewImage($(this));" class="chatShowTradePreImage" href="'+rowData.tradeImg+'" alt="image" target="_blank"><img src="'+rowData.tradeImg+'" style="height:60px;width:150px;"></a>' : '没有图片'
						}},
						{title : "排序",field : 'sorted',sortable : true },
						{title : "晒单时间", field : 'showDate' ,sortable : true, formatter : function(value, rowData, rowIndex) {
							return rowData.showDate ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title:'审核状态',field : 'status' ,formatter : function(value, rowData, rowIndex) {
							if(rowData.tradeType==1){
								return '通过';
							}else{
								if(rowData.status==1){
									return '通过';
								}else if(rowData.status==0){
									return '待审核';
								}else{
									return '不通过';
								}
							}
						}},
						{title: "晒单盖楼", field:'isAccord' ,formatter : function(value, rowData, rowIndex) {
							if(rowData.tradeType==1){
								return '';
							}else{
								if(rowData.isAccord==1){
									return '是';
								}else{
									return '否';
								}
							}
						}},
						{title : "备注", field : 'remark'}
						
			]],
			toolbar : '#show_trade_datagrid_toolbar'
		});
	},
	setEvent:function(){
		// 列表查询
		$("#show_trade_queryForm_search").on("click",function(){
			var userNo = $("#chatTradeSearchUserNoInput").val(); 
			if(userNo == '请选择'){
				userNo = '';
			}
			var groupType = $("#showTrade_groupType_select").val();  
			var status = $('#showTrade_status_select').val();
			var queryParams = $('#'+chatShowTrade.gridId).datagrid('options').queryParams;
			var userName = $('#userName').val();
			var tradeType = $('#tradeType').val();
			var isAccord = $('#isAccord').val();
			queryParams['userNo'] = userNo;
			queryParams['groupType'] = groupType;
			queryParams['status'] = status;
			queryParams['tradeType'] = tradeType;
			queryParams['userName'] = userName;
			queryParams['isAccord'] = isAccord;
			$('#'+chatShowTrade.gridId).datagrid({
				url : basePath+'/chatShowTradeController/datagrid.do?opType=' + chatShowTrade.opType,
				pageNumber : 1
			});
		});
		// 重置
		$("#show_trade_queryForm_reset").on("click",function(){
			$("#show_trade_queryForm")[0].reset();
		});
	},
	//显示用户列表
	setUserList:function(){
	     chatShowTrade.setAuthorList("chatTradeSearchUserNo");
	},
	setUserEdit:function(value){
	     chatShowTrade.setAuthorList("chatTradeEditUserNo");
	     $('#chatTradeEditUserNo').combogrid('setValue', value);
	},
	setUserAdd:function(){
		chatShowTrade.setAuthorList("chatTradeAddUserNo");
	},
	/**
	 * 验证数字
	 */
	validProfit:function(){
		if(!$('#profit').val()){
			return true;
		}
		var profit_float = parseFloat($('#profit').val());
		if(isNaN(profit_float)){
			$.messager.alert("提示信息","获利为有效的数字(两位小数)");
			$('#profit').focus();
			return false;
		}else{
			$('#profit').val(profit_float.toFixed(2));
			return true;
		}
	},
	/**
	 * 预览图片
	 */
	setViewImage:function(obj){
		var id = '#showTradeListImgView';
	    $(id).val('');
		var imgPath = obj.children('img').attr('src');
		if(imgPath){
			$(id).val(imgPath);
			goldOfficeUtils.onViewImage(id);
		}
		return false;
	},
	/**
	 * 功能：查看
	 * @param recordId   dataGrid行Id
	 */
	view : function(recordId){
		$("#show_trade_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatShowTradeController/'+recordId+'/view.do');
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
		var url = formatUrl(basePath + '/chatShowTradeController/add.do?opType=' + chatShowTrade.opType);
		var submitUrl =  formatUrl(basePath + '/chatShowTradeController/create.do');
		goldOfficeUtils.openEditorDialog({
			dialogId : "editWindow",
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			width : 650,
			height : 350,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#showTradeAddFrom").form('validate')){
					if(chatShowTrade.validProfit() == false){
						return ;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'showTradeAddFrom',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if(d.success) {
								$("#editWindow").dialog("close");
								chatShowTrade.refresh();
								$.messager.alert("操作提示",'新增晒单成功');
							}else{
								$.messager.alert('错误提示','新增晒单失败，原因：分析师'+d.msg);
							}
						}
					});
				}
			},
			onLoad : function(){
				chatShowTrade.setUserAdd();
			}
		});
	},
	/**
	 * 功能：修改
	 * @param recordId   dataGrid行Id
	 */
	edit : function(recordId){
		$("#show_trade_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatShowTradeController/'+recordId+'/edit.do?opType=' + chatShowTrade.opType);
		var submitUrl =  formatUrl(basePath + '/chatShowTradeController/update.do');
		goldOfficeUtils.openEditorDialog({
			dialogId : "editWindow",
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			width : 650,
			height : 350,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if($("#showTradeEditFrom").form('validate')){
					if(chatShowTrade.validProfit() == false){
						return ;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'showTradeEditFrom',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#editWindow").dialog("close");
								chatShowTrade.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败','error');  /**操作提示  修改失败!*/
							}
						}
					});
				}
			},
			onLoad : function(){
				chatShowTrade.setUserEdit($('#chatTradeEditUserNoInput').attr('data-userName'));
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
		$('#'+chatShowTrade.gridId).datagrid('reload');
	},
	
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/chatShowTradeController/batchDel.do');
		goldOfficeUtils.deleteBatch('show_trade_datagrid',url);	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#show_trade_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/chatShowTradeController/oneDel.do');
		goldOfficeUtils.deleteOne('show_trade_datagrid',recordId,url);
	},
	/**
	 * 设置作者选择列表
	 * @param id
	 */
	setAuthorList:function(id){
		$('#'+id).combogrid({
		    idField:'userNo',
		    textField:'userName',
			panelWidth: 200,
		    url:basePath+'/userController/getAnalystList.do?hasOther=true',
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
				   $('#'+id+'Input').val(rowData.userNo);
			},
		    onChange:function(val){
		    	$('#'+id+'Input').val(val);
		    },
			onBeforeLoad:function(){
				$('#'+id).next('span.combo').find('input').width(200);
			}
		}); 
	},
	/**
	 * 审核晒单
	 * @param status
	 */
	setStatus:function(status){
		var url = formatUrl(basePath + '/chatShowTradeController/batchSetStatus.do');
		var rows = $("#"+chatShowTrade.gridId).datagrid('getSelections');
		if(!rows || rows.length == 0) {
			$.messager.alert($.i18n.prop("common.operate.tips"), '请选择记录进行操作!', 'warning');
			return;
		}
		var message = '您确定要审核通过选中的晒单吗？';
		if(status==-1){
			message = '您确定要审核不通过选中的晒单吗？' ;
		}
		$.messager.confirm("操作提示", message, function(r) {
			if (r) {
				var tradeIds = [];
				for(var i = 0; i < rows.length; i++) {
					tradeIds.push(rows[i].id);
				}
				goldOfficeUtils.ajax({
					url : url,
					data: {tradeIds : tradeIds.join(','),status : status},
					success : function(data){   
						if (data.success) {
							$('#'+chatShowTrade.gridId).datagrid('reload');
							$.messager.alert("操作提示","审核成功!",'info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败','error');  /**操作提示  修改失败!*/
						}
					}
				});
			}
		});
	},

	/**
	 * 设置评论列表显示
	 */
	setCommentEventView : function(){
		$("#showTradeComment_datagrid").datagrid({
			fit : false,
			fitColumns : true,
			idField : "id",
			columns : [[
				{title : $.i18n.prop("common.operate"), field:'op', formatter: function(value, rowData, rowIndex){
					$("#showTradeComment_datagrid_rowOperation input").val(rowIndex);
					return $("#showTradeComment_datagrid_rowOperation").html();
				}},
				{title : "序号",field : 'id'},
				{title : "昵称",field : 'userName'},
				{title : "评论内容",field: 'content'},
				{title : "评论时间",field: 'dateTime', formatter : function(value, rowData, rowIndex) {
					return rowData.dateTime ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
				}},
			]]
		});

		$("#showTradeComment_page").pagination({
			pageSize : 15,
			pageList : [15, 30, 50, 100]
		});
	},
	/**
	 * 打开点评
	 * @param item
	 */
	openViewComment:function(recordId){
		var url = formatUrl(basePath + '/chatShowTradeController/review.do');
		goldOfficeUtils.ajax({
			url : url,
			data : {
				sid : recordId
			},
			success: function(data) {
				if (data) {
					var loc_dataInfo = data.data;
					var user = data.user;
					goldOfficeUtils.openSimpleDialog({
						dialogId : "showTradeCommentView_win",
						title : '评论数据',
						width : 600,
						height : 400,
						onOpen : function(){
							chatShowTrade.viewComments(loc_dataInfo);
						},
						buttons	 : [
							{
								text : '关闭',
								iconCls : "ope-close",
								handler : function() {
									$("#showTradeCommentView_win").dialog("close");
								}
							}]
					});
				}else{
					$.messager.alert($.i18n.prop("common.operate.tips"),'获取评论数据信息失败!','error');
				}
			}
		});
	},
	/**
	 * 显示点评数据
	 * @param data
	 */
	viewComments : function(data){
		$('#showTradeCommentView_panel input[name="sid"]').val(data.id);

		//点评数据
		var comments = [];
		if(isValid(data.comments) && data.comments.length>0) {
			$.each(data.comments, function (i, row) {
				if (row.valid == 1) {
					comments.push(row);
				}
			});
			comments = comments ? comments.reverse() : [];
		}

		$("#showTradeComment_datagrid").datagrid("loadData", comments.slice(0, 15));
		$("#showTradeComment_page").pagination('refresh', {
			total : comments.length,
			onSelectPage : function(pageNo, pageSize) {
				var start = (pageNo - 1) * pageSize;
				var end = start + pageSize;
				$("#showTradeComment_page").datagrid("loadData", comments.slice(start, end));
			}
		});
	},
	/**
	 * 删除点评
	 * @param item
	 */
	delComment : function(item){
		var rowIndex = $(item).siblings("input").val();
		var row = $('#showTradeComment_datagrid').datagrid('getData').rows[rowIndex];
		var url = formatUrl(basePath + '/chatShowTradeController/delComment.do');
		var message = "您确定要删除记录吗?";
		var dataId = $('#showTradeCommentView_panel input[name="sid"]').val();
		$.messager.confirm("操作提示", message , function(r) {
			if (r) {
				goldOfficeUtils.ajax({
					url : url,
					data : {
						sid : dataId,
						cid : row.id
					},
					success: function(data) {
						if (data.success) {
							$('#showTradeComment_datagrid').datagrid('deleteRow', rowIndex);
							$.messager.alert($.i18n.prop("common.operate.tips"),'删除成功!','info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'删除失败，原因：'+data.msg,'error');
						}
					}
				});
			}
		});
	},
  /**
   * 审核晒单盖楼
   * @param isAccord
   */
	setIsAccord:function(isAccord){
		var url = formatUrl(basePath + '/chatShowTradeController/batchSetIsAccord.do');
		var rows = $("#"+chatShowTrade.gridId).datagrid('getSelections');
		if(!rows || rows.length == 0) {
			$.messager.alert($.i18n.prop("common.operate.tips"), '请选择记录进行操作!', 'warning');
			return;
		}
		var message = '您确定要审核通过选中的晒单为晒单盖楼吗？';
		if(isAccord==0){
			message = '您确定要审核不通过选中的晒单为晒单盖楼吗？' ;
		}
		$.messager.confirm("操作提示", message, function(r) {
			if (r) {
				var tradeIds = [];
				for(var i = 0; i < rows.length; i++) {
					tradeIds.push(rows[i].id);
				}
				goldOfficeUtils.ajax({
					url : url,
					data: {tradeIds : tradeIds.join(','),isAccord : isAccord},
					success : function(data){
						if (data.success) {
							$('#'+chatShowTrade.gridId).datagrid('reload');
							$.messager.alert("操作提示","审核晒单盖楼成功!",'info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'审核晒单盖楼失败','error');  /**操作提示  修改失败!*/
						}
					}
				});
			}
		});
	},
  /**
   * 功能：导出记录
   */
  exportRecord : function(){
    var groupType = $("#showTrade_groupType_select").val();
    var status = $('#showTrade_status_select').val();
    var queryParams = $('#'+chatShowTrade.gridId).datagrid('options').queryParams;
    var userName = $('#userName').val();
    //var tradeType = $('#tradeType').val();
    var isAccord = $('#isAccord').val();
    queryParams['groupType'] = groupType;
    queryParams['status'] = status;
    //queryParams['tradeType'] = tradeType;
    queryParams['userName'] = userName;
    queryParams['isAccord'] = isAccord;
    
    var path = basePath+ '/chatShowTradeController/exportRecord.do?'+$.param(queryParams);
    window.location.href = path;
  }
};
		
//初始化
$(function() {
	chatShowTrade.init();
	
});