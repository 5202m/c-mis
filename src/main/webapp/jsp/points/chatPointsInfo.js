/**
 * 积分信息管理<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2015<BR>
 * Author : Dick.guo <BR>
 * Date : 2015年10月29日 <BR>
 * Description : <BR>
 * <p>
 * 
 * </p>
 */
var chatPointsInfo = {
	gridId : 'chatPointsInfo_datagrid',
	dicts : {},
	init : function(){
		this.initDicts($("#chatPointsInfo_groupType"));
		this.initDicts($("#chatPointsInfo_type"));
		this.initDicts($("#chatPointsInfo_item"));
		this.initGrid();
		this.setEvent();
		this.setEventView();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatPointsInfo.gridId,
			idField:"pointsId",
			sortName : 'pointsId',
			sortOrder : "desc",
			url : basePath+'/chatPointsInfo/datagrid.do',
			columns : [[
			    {title : $.i18n.prop("common.operate"), field:'pointsId', formatter: function(value, rowData, rowIndex){
			    	$("#chatPointsInfo_datagrid_rowOperation input").val(value);
			    	return $("#chatPointsInfo_datagrid_rowOperation").html();
			    }},
	            {title : "组别",field : 'groupType',formatter : function(value, rowData, rowIndex) {
	            	return chatPointsInfo.formatByDicts("groupType", value);
				}},
				{title : "用户编号",field : 'userId',formatter : function(value, rowData, rowIndex) {
					return formatMobileToUserId(value);
				}},
				{title : "总积分",field : 'pointsGlobal'},
				{title : "有效积分",field : 'points'},
				{title : "类别",field : 'type',formatter : function(value, rowData, rowIndex) {
					var item = rowData.journal[0].item;
						item = item.replace(/_.*$/g, "");
					return chatPointsInfo.formatByDicts("type", item);
				}},
				{title : "项目",field : 'item',formatter : function(value, rowData, rowIndex) {
					var item = rowData.journal[0].item;
					return chatPointsInfo.formatByDicts("journal[0].item", item);
				}},
				{title : "积分",field : 'change',formatter : function(value, rowData, rowIndex) {
					var change = rowData.journal[0].change;
					return change;
				}},
				{title : "时间",field : 'date',formatter : function(value, rowData, rowIndex){
					var date = rowData.journal[0].date;
					return timeObjectUtil.formatterDateTime(date);
				}},
				{title : "积分备注",field : 'remark',formatter : function(value, rowData, rowIndex) {
					var remark = rowData.journal[0].remark;
					return remark;
				}}
			]],
			toolbar : '#chatPointsInfo_datagrid_toolbar'
		});
	},
	
	/** 绑定事件 */
	setEvent:function(){
		/**查询*/
		$("#chatPointsInfo_queryForm_search").on("click",function(){
			var queryParams = $('#'+chatPointsInfo.gridId).datagrid('options').queryParams;
			var loc_val1 = $("#chatPointsInfo_pointsStart").val() || "";
			var loc_val2 = $("#chatPointsInfo_pointsEnd").val() || "";
			if(/^\d*$/.test(loc_val1) == false && /^\d*$/.test(loc_val2) == false){
				alert("积分范围只能输入整数！");
				$("#chatPointsInfo_pointsStart").val(loc_val1.replace(/[^\d]/g, ""));
				$("#chatPointsInfo_pointsEnd").val(loc_val2.replace(/[^\d]/g, ""));
				return;
			}
			
			$("#chatPointsInfo_queryForm select,#chatPointsInfo_queryForm input").each(function(){
				queryParams[$(this).attr("name")] = $(this).val();
			});
			$('#'+chatPointsInfo.gridId).datagrid({
				url : basePath+'/chatPointsInfo/datagrid.do',
				pageNumber : 1
			});
		});
		
		/**重置*/
		$("#chatPointsInfo_queryForm_reset").on("click",function(){
			$("#chatPointsInfo_queryForm")[0].reset();
		});
		
		/**类别联动事件*/
		chatPointsInfo.setEventType($("#chatPointsInfo_type"), $("#chatPointsInfo_item"));
	},
	
	/**
	 * 设置类别和项目联动事件
	 * @param $type
	 * @param $item
	 */
	setEventType : function($type, $item){
		$type.bind("change", $item, function(e){
			var loc_item = e.data;
			var type = $(this).val();
			loc_item.find("option[value!='']").attr("s","0").hide();
			if(type){
				loc_item.find("option[value^='" + type + "_']").attr("s","1").show();
			}
			if(loc_item.find("option:selected").attr("s") == "0"){
				loc_item.find("option[s='1']").eq(0).prop("selected", true);
			}
		});
		
		$item.find("option[value='']").attr("s","1");
		$type.trigger("change");
	},
	
	/**
	 * 初始化字典
	 * @param $select
	 */
	initDicts : function($select){
		var loc_name = $select.attr("name");
		var loc_metas = {};
		$select.find("option").each(function(metas){
			if(!isBlank($(this).val())){
				metas[$(this).val()] = $(this).text(); 
			}
		},[loc_metas]);
		chatPointsInfo.dicts[loc_name] = loc_metas;
	},
	
	/**
	 * 数据格式化
	 * @param key
	 * @param value
	 */
	formatByDicts : function(key, value){
		if(isBlank(value) || chatPointsInfo.dicts.hasOwnProperty(key) == false){
			return "";
		}
		var loc_obj = chatPointsInfo.dicts[key];
		return loc_obj.hasOwnProperty(value) ? loc_obj[value] : (value || "");
	},
	
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatPointsInfo.gridId).datagrid('reload');
	},
	
	/**
	 * 新增
	 */
	add : function(){
		var url = formatUrl(basePath + '/chatPointsInfo/preAdd.do');
		var submitUrl = formatUrl(basePath + '/chatPointsInfo/save.do');
		goldOfficeUtils.openEditorDialog({
			title : '新增积分信息',
			width:710,
			height:350,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				try{
					if($("#chatPointsInfoAdd_Form").form('validate')){
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'chatPointsInfoAdd_Form',
							onSuccess : function(data){  		//提交成功后处理
								var d = $.parseJSON(data);
								if (d.success) {
									$("#myWindow").dialog("close");
									chatPointsInfo.refresh();
									$.messager.alert($.i18n.prop("common.operate.tips"),'添加成功！','info');
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'添加失败，原因：'+d.msg,'error');
								}
							}
						});
					}
				}catch(e){
					alert(e);
				}
			},
			onLoad : function(){
				chatPointsInfo.initSelect($("#chatPointsInfo_queryForm"), $("#chatPointsInfoAdd_Form"), false);

				/**类别联动事件*/
				chatPointsInfo.setEventType($("#chatPointsInfoAdd_type"), $("#chatPointsInfoAdd_item"));
			}
		});
	},
	
	/**
	 * 
	 * @param $srcpanel
	 * @param $panel
	 * @param initVal
	 */
	initSelect : function($srcpanel, $panel, initVal){
		$panel.find("select[name]").each(function(){
			var $select = $srcpanel.find("select[name='" + $(this).attr("name") + "']");
			if($select.size() == 1){
				$(this).html($select.html());
				$(this).find("option[value='']").remove();
			}
			if(initVal){
				$(this).val($(this).next().val());
			}
		});
	},
	
	/**
	 * 调整 == 手工追加一个积分流水
	 * @param item
	 */
	edit : function(item){
		var $tr = $(item).parents("tr");
		var pointsInfo = {
			pointsId : $(item).siblings("input").val(),
			groupType : $tr.find("td[field='groupType']").text(),
			userId : $tr.find("td[field='userId']").text()
		}
		var url = formatUrl(basePath + '/chatPointsInfo/preAdd.do');
		var submitUrl = formatUrl(basePath + '/chatPointsInfo/save.do');
		goldOfficeUtils.openEditorDialog({
			title : '调整积分信息',
			width:710,
			height:350,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				try{
					if($("#chatPointsInfoAdd_Form").form('validate')){
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'chatPointsInfoAdd_Form',
							onSuccess : function(data){  		//提交成功后处理
								var d = $.parseJSON(data);
								if (d.success) {
									$("#myWindow").dialog("close");
									chatPointsInfo.refresh();
									$.messager.alert($.i18n.prop("common.operate.tips"),'添加成功！','info');
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'添加失败，原因：'+d.msg,'error');
								}
							}
						});
					}
				}catch(e){
					alert(e);
				}
			},
			onLoad : function(){
				$("#chatPointsInfoAdd_pointsId").val(pointsInfo.pointsId);
				$("#chatPointsInfoAdd_groupTypeTd").html(pointsInfo.groupType);
				$("#chatPointsInfoAdd_userIdTd").html(pointsInfo.userId);
				$("#chatPointsInfoAdd_userIdTd").prev().html("用户编号");
				
				chatPointsInfo.initSelect($("#chatPointsInfo_queryForm"), $("#chatPointsInfoAdd_Form"), false);

				/**类别联动事件*/
				chatPointsInfo.setEventType($("#chatPointsInfoAdd_type"), $("#chatPointsInfoAdd_item"));
				
			}
		});
	},
	
	/**
	 * 删除
	 * @param item
	 */
	del : function(item){
		var loc_pointsId = $(item).siblings("input").val();
		var url = formatUrl(basePath + '/chatPointsInfo/delete.do');
		goldOfficeUtils.deleteOne(chatPointsInfo.gridId, loc_pointsId, url, "确认删除吗？");
	},
	
	/**
	 * 预览
	 * @param item
	 */
	view : function(item){
		var loc_pointsId = null;
		if(typeof item === "string"){
			loc_pointsId = item;
		}else{
			loc_pointsId = $(item).siblings("input").val();
		}
		var url = formatUrl(basePath + '/chatPointsInfo/view.do');
		goldOfficeUtils.ajax({
			url : url,
			data : {
				pointsId : loc_pointsId
			},
			success: function(data) {
				if (data) {
					var loc_pointsInfo = data;
					goldOfficeUtils.openSimpleDialog({
						dialogId : "chatPointsInfoView_win",
						title : '积分详情',
						width : 1050,
						height : 585,
						onOpen : function(){
							chatPointsInfo.viewJournal(loc_pointsInfo);
						},
						buttons	 : [{
							text : '关闭',
							iconCls : "ope-close",
							handler : function() {
								$("#chatPointsInfoView_win").dialog("close");
							}
						}]
					});
				}else{
					$.messager.alert($.i18n.prop("common.operate.tips"),'获取积分信息失败!','error');
		    	}
			}
		});
	},
	
	setEventView : function(){
		var loc_datagrid = $("#chatPointsInfoView_datagrid").datagrid({
			fit : false,
			fitColumns : true,
			idField : "journalId",
			sortName : 'date',
			sortOrder : "desc",
			columns : [[
	            {title : $.i18n.prop("common.operate"), field:'op', formatter: function(value, rowData, rowIndex){
			    	$("#chatPointsInfoView_datagrid_rowOperation input").val(rowData['journalId']);
			    	return $("#chatPointsInfoView_datagrid_rowOperation").html();
			    }},
				{title : "序号",field : 'journalId'},
			    {title : "类别",field : 'type',formatter : function(value, rowData, rowIndex) {
					var item = rowData.item;
						item = item.replace(/_.*$/g, "");
					return chatPointsInfo.formatByDicts("type", item);
				}},
				{title : "项目",field : 'item',formatter : function(value, rowData, rowIndex) {
					var item = rowData.item;
					return chatPointsInfo.formatByDicts("journal[0].item", item);
				}},
				{title : "积分前值",field : 'before'},
				{title : "积分",field : 'change'},
				{title : "积分后值",field : 'after'},
				{title : "操作者",field : 'opUser'},
				{title : "时间",field : 'date', formatter:timeObjectUtil.formatterDateTime},
				{title : "备注",field : 'remark'},
				{title : "标记",field : 'tag'}
			]]
		});
		
		$("#chatPointsInfoView_page").pagination({
			pageSize : 15,
			pageList : [15, 30, 50, 100]
		});

		$('#chatPointsExportPageView_datagrid').datagrid({
			fit : false,
			fitColumns : true,
			columns : [[
				{title : $.i18n.prop("common.operate"), field:'op', formatter: function(value, rowData, rowIndex){
					$("#chatPointsExportPageView_datagrid_rowOperation input").val(rowData['start']+','+rowData['end']);
					return $("#chatPointsExportPageView_datagrid_rowOperation").html();
				}},
				{title : "从",field : 'start', formatter : function (value, rowData, rowIndex) {
					return value;
				}},
				{title : "到",field : 'end',formatter : function(value, rowData, rowIndex) {
					return value;
				}}
			]]
		});
	},
	
	/**
	 * 显示积分详情
	 * @param points
	 */
	viewJournal : function(points){
		//基础信息
		$("#chatPointsInfoView_table td[field]").each(function(){
			var filed = $(this).attr("field");
			var val = points[filed];
			if(filed == "groupType"){
				val = chatPointsInfo.formatByDicts("groupType", val);
			}else if(filed == "userId"){
				val = formatMobileToUserId(val);
			}
			$(this).html(val);
		});
		$("#chatPointsInfoView_pointsId").val(points.pointsId);
		
		//流水
		var journals = points.journal.reverse();
		
		$("#chatPointsInfoView_datagrid").datagrid("loadData", journals.slice(0, 15));
		$("#chatPointsInfoView_page").pagination('refresh', {
			total : journals.length,
			onSelectPage : function(pageNo, pageSize) {
				var start = (pageNo - 1) * pageSize;
				var end = start + pageSize;
				$("#chatPointsInfoView_datagrid").datagrid("loadData", journals.slice(start, end));
			}
		});
	},
	
	/**
	 * 冲正
	 * 
	 * @param item
	 */
	reversal : function(item){
		var loc_journalId = $(item).siblings("input").val();
		var loc_pointsId = $("#chatPointsInfoView_pointsId").val();
		var url = formatUrl(basePath + '/chatPointsInfo/reversal.do');
		$.messager.confirm("操作提示", "您确定要对该积分记录冲正处理吗?" , function(r) {
		   if (r) {
			   goldOfficeUtils.ajax({
					url : url,
					data : {
						pointsId : loc_pointsId,
						journalId : loc_journalId
					},
					success: function(data) {
						if (data.success) {
							$.messager.alert($.i18n.prop("common.operate.tips"),'冲正成功!','info', function(){
								chatPointsInfo.view($("#chatPointsInfoView_pointsId").val());
							});
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'冲正失败，原因：'+data.msg,'error');
				    	}
					}
				});
			}
		});
	},
	/**
	 * 导出积分记录分页
	 */
	exportRecordPage : function(){
		var loc_params = $('#'+chatPointsInfo.gridId).datagrid('options').queryParams;
		var loc_val1 = $("#chatPointsInfo_pointsStart").val() || "";
		var loc_val2 = $("#chatPointsInfo_pointsEnd").val() || "";
		if(/^\d*$/.test(loc_val1) == false && /^\d*$/.test(loc_val2) == false){
			alert("积分范围只能输入整数！");
			$("#chatPointsInfo_pointsStart").val(loc_val1.replace(/[^\d]/g, ""));
			$("#chatPointsInfo_pointsEnd").val(loc_val2.replace(/[^\d]/g, ""));
			return;
		}

		$("#chatPointsInfo_queryForm select,#chatPointsInfo_queryForm input").each(function(){
			loc_params[$(this).attr("name")] = $(this).val();
		});
		var url = formatUrl(basePath + '/chatPointsController/exportRecordCount.do');
		goldOfficeUtils.ajax({
			url : url+'&'+$.param(loc_params),
			type: 'get',
			success: function(data) {
				if (data) {
					var loc_Info = data;
					goldOfficeUtils.openSimpleDialog({
						dialogId : "chatPointsExportPageView_win",
						title : '积分分页导出',
						width : 300,
						height : 200,
						onOpen : function(){
							$("#chatPointsExportPageView_datagrid").datagrid("loadData", loc_Info.obj);
						},
						buttons	 : [{
							text : '关闭',
							iconCls : "ope-close",
							handler : function() {
								$("#chatPointsExportPageView_win").dialog("close");
							}
						}]
					});
				}else{
					$.messager.alert($.i18n.prop("common.operate.tips"),'获取积分分页信息失败!','error');
				}
			}
		});
	},
	/**
	 * 功能：导出记录
	 */
	exportRecord : function(item){
		var loc_params = $('#'+chatPointsInfo.gridId).datagrid('options').queryParams;
		var loc_val1 = $("#chatPointsInfo_pointsStart").val() || "";
		var loc_val2 = $("#chatPointsInfo_pointsEnd").val() || "";
		if(/^\d*$/.test(loc_val1) == false && /^\d*$/.test(loc_val2) == false){
			alert("积分范围只能输入整数！");
			$("#chatPointsInfo_pointsStart").val(loc_val1.replace(/[^\d]/g, ""));
			$("#chatPointsInfo_pointsEnd").val(loc_val2.replace(/[^\d]/g, ""));
			return;
		}

		$("#chatPointsInfo_queryForm select,#chatPointsInfo_queryForm input").each(function(){
			loc_params[$(this).attr("name")] = $(this).val();
		});
		var pageValue = $(item).siblings("input").val().split(',');
		loc_params['start'] = pageValue[0];
		loc_params['end'] = pageValue[1];
		var path = basePath+ '/chatPointsController/exportRecord.do?'+$.param(loc_params);
		window.location.href = path;
	}
};
		
//初始化
$(function() {
	chatPointsInfo.init();
});