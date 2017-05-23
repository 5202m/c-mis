/**
 * 财经日历管理<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2016<BR>
 * Author : Dick.guo <BR>
 * Date : 2016年3月21日 <BR>
 * Description : <BR>
 * <p>
 * 
 * </p>
 */
var ZxData = {
	gridId : 'zxData_datagrid',
	dicts : {
		importance : {
			"1" : "低",
			"2" : "中",
			"3" : "高"
		}
	},
	init : function(){
		this.initDicts($("#zxData_dataType"));
		this.initDicts($("#zxData_valid"));
		this.initGrid();
		this.setEvent();
		this.setEventView();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : ZxData.gridId,
			idField:"dataId",
			sortName : '', //涉及到多字段排序，在controller中配置
			//sortOrder : "desc",
			url : basePath+'/zxDataController/datagrid.do',
			queryParams : {
				valid : 1
			},
			columns : [[
			    {title : $.i18n.prop("common.operate"), field:'dataId', formatter: function(value, rowData, rowIndex){
			    	$("#zxData_datagrid_rowOperation input").val(value);
			    	return $("#zxData_datagrid_rowOperation").html();
			    }},
	            {title : "名称",field : 'name'},
	            {title : "国家",field : 'country'},
	            {title : "指标编号",field : 'basicIndexId'},
	            {title : "指标时期",field : 'period'},
	            {title : "公布时间",field : 'date',formatter : function(value, rowData, rowIndex) {
	            	return value + "&nbsp;" + rowData["time"];
				}},
	            /*{title : "重要性",field : 'importance',formatter : function(value, rowData, rowIndex) {
	            	return ZxData.formatByDicts("importance", value);
				}},*/
	            {title : "预期值",field : 'predictValue'},
	            {title : "前值",field : 'lastValue'},
	            {title : "公布值",field : 'value'},
	            /*{title : "指标级数",field : 'level'},*/
	            {title : "重要指数",field : 'importanceLevel',sortable : true,formatter : function(value, rowData, rowIndex) {
					value = value || 0;
					var html = [];
					for(var i = 0; i < 5; i++){
						html.push(i < value ? "★" : "☆");
					}
					return html.join("");
				}},
	            {title : "描述",field : 'description'},
	            {title : "产品类型",field : 'dataType',formatter : function(value, rowData, rowIndex) {
	            	return ZxData.formatByDicts("dataType", value);
				}},
				{title : "有效性",field : 'valid',formatter : function(value, rowData, rowIndex) {
					return ZxData.formatByDicts("valid", value + "");
				}},
				{title : '修改时间',field : 'updateDate',sortable : true,formatter : function(value, rowData, rowIndex) {
					return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
				}}
			]],
			toolbar : '#zxData_datagrid_toolbar'
		});
	},
	
	/** 绑定事件 */
	setEvent:function(){
		/**查询*/
		$("#zxData_queryForm_search").on("click",function(){
			var queryParams = $('#'+ZxData.gridId).datagrid('options').queryParams;
			$("#zxData_queryForm select").add("#zxData_queryForm input").each(function(){
				queryParams[$(this).attr("name")] = $(this).val();
			});
			$('#'+ZxData.gridId).datagrid({
				url : basePath+'/zxDataController/datagrid.do',
				pageNumber : 1
			});
		});
		
		/**重置*/
		$("#zxData_queryForm_reset").on("click",function(){
			$("#zxData_queryForm")[0].reset();
		});
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
		ZxData.dicts[loc_name] = loc_metas;
	},
	
	/**
	 * 数据格式化
	 * @param key
	 * @param value
	 */
	formatByDicts : function(key, value){
		if(isBlank(value) || ZxData.dicts.hasOwnProperty(key) == false){
			return "";
		}
		var loc_obj = ZxData.dicts[key];
		return loc_obj.hasOwnProperty(value) ? loc_obj[value] : (value || "");
	},
	
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+ZxData.gridId).datagrid('reload');
	},
	
	/**
	 * 描述
	 */
	description : {
		$input : null,
		$temp : null,
		$panel : null,
		val : {
			predictValue : null,
			lastValue : null,
			value : null
		},
		//初始化
		init : function(){
			this.$input = $("#zxDataEdit_description");
			this.$panel = $("#zxDataEdit_descriptionPanel");
			this.$temp = $("#zxDataEdit_descriptionTemp tr");
			$("#zxDataEdit_descriptionAdd").bind("click", this, function(e){
				e.data.add();
				e.data.seq();
			});
			this.set();
			
			//初始化值数据
			this.val = {
				predictValue : null,
				lastValue : null,
				value : null
			};
			var panel = $("#zxDataEdit_Form");
			var loc_val = {
				predictValue : panel.find("input[name='predictValue']").val(),
				lastValue : panel.find("input[name='lastValue']").val(),
				value : panel.find("input[name='value']").val()
			};
			var loc_regex = /[^0-9\-\.]/g;
			loc_val.predictValue = loc_val.predictValue.replace(loc_regex, "");
			loc_val.lastValue = loc_val.lastValue.replace(loc_regex, "");
			loc_val.value = loc_val.value.replace(loc_regex, "");
			loc_regex = /^[+-]?\d+(\.\d+)?$/;
			if(loc_regex.test(loc_val.predictValue)){
				this.val.predictValue = parseFloat(loc_val.predictValue);
			}
			if(loc_regex.test(loc_val.lastValue)){
				this.val.lastValue = parseFloat(loc_val.lastValue);
			}
			if(loc_regex.test(loc_val.value)){
				this.val.value = parseFloat(loc_val.value);
			}
		},
		//初始化设置数据
		set : function(){
			var loc_val = this.$input.val();
			if(loc_val){
				var loc_vals = loc_val.split(",");
				for(var i = 0, lenI = loc_vals.length; i < lenI; i++){
					this.add(loc_vals[i]);
				}
				this.seq();
			}
		},
		//获取数据
		get : function(){
			var loc_checkMsg = null;
			var loc_prodTypes = {};
			var loc_val = [];
			this.$panel.find("tr").each(function(){
				var loc_valTmp = [];
				$(this).find("select").each(function(index){
					var val = $(this).val();
					if(!val){
						loc_checkMsg = "描述数据不完整！";
					}else{
						if(index == 0){
							if(loc_prodTypes.hasOwnProperty(val)){
								loc_checkMsg = "描述数据不能有重复的产品！";
							}else{
								loc_prodTypes[val] = 1;
							}
						}
					}
					loc_valTmp.push(val);
				});
				loc_val.push(loc_valTmp.join("_"));
			});
			var result = loc_val.join(",");
			this.$input.val(result);
			if(!result){
				loc_checkMsg = "描述数据不能为空！";
			}
			this.$input.data("check", {
				isOK : !loc_checkMsg,
				msg : loc_checkMsg
			});
			return result;
		},
		//检查数据
		check : function(){
			var val = this.get();
			var checkResult = this.$input.data("check");
			if(!checkResult.isOK){
				alert(checkResult.msg);
				return false;
			}
			return true;
		},
		//增加一行
		add :function(val){
			var tmp = this.$temp.clone();
			if(val){
				var vals = val.split("_");
				tmp.find("select").each(function(index){
					$(this).val(vals[index]);
				});
			}
			tmp.find("a").bind("click", this, function(e){
				$(this).parents("tr:first").remove();
				e.data.seq();
			});
			tmp.find("select:eq(1)").bind("change", this, function(e){
				e.data.change($(this).parents("tr:first"));
			});
			this.$panel.append(tmp);
		},
		//重置序号
		seq : function(){
			this.$panel.find("tr").each(function(index){
				$(this).find("td:first").html(index + 1);
			});
		},
		change:function($tr){
			var $selectes = $tr.find("select");
			var descs = ["WH", $selectes.eq(1).val(), "", "", ""];
			var comp = 0;
			if(descs[1]){
				var isZX = descs[1] == "ZX";
				if (this.val.lastValue == null)
				{
					descs[2] = "U";
					descs[3] = "U";
					descs[4] = "U";
				}else {
					if (this.val.predictValue == null)
					{
						descs[2] = "U";
					}else{
						comp = this.val.predictValue - this.val.lastValue;
						if (comp == 0)
						{
							descs[2] = "FLAT";
						}else if((comp > 0 && isZX) || (comp < 0 && !isZX)){
							descs[2] = "GOOD";
						}else{
							descs[2] = "BAD";
						}
					}
					if (this.val.value == null)
					{
						descs[3] = "U";
						descs[4] = "U";
					}else{
						comp = this.val.value - this.val.lastValue;
						if (comp == 0)
						{
							descs[3] = "FLAT";
						}else if((comp > 0 && isZX) || (comp < 0 && !isZX)){
							descs[3] = "GOOD";
						}else{
							descs[3] = "BAD";
						}
						//影响力度
						var rate = this.val.lastValue == 0 ? this.val.value : ((this.val.value - this.val.lastValue) / this.val.lastValue);
						var importanceLevel = $("#zxDataEdit_importanceLevel").val();
						importanceLevel = parseInt(importanceLevel, 10);
						rate = Math.abs(rate) * importanceLevel;
						if(rate < 0.2){
							descs[4] = "LV1";
						}else if(rate < 0.5){
							descs[4] = "LV2";
						}else{
							descs[4] = "LV3";
						}
					}
				}
			}
			$selectes.eq(2).val(descs[2]);
			$selectes.eq(3).val(descs[3]);
			$selectes.eq(4).val(descs[4]);
		}
	},
	
	/**
	 * 修改
	 * @param item
	 */
	edit : function(item){
		var loc_dataId = $(item).siblings("input").val();
		var url = formatUrl(basePath + '/zxDataController/preEdit.do?dataId=' + loc_dataId);
		var submitUrl = formatUrl(basePath + '/zxDataController/save.do');
		goldOfficeUtils.openEditorDialog({
			title : '修改财经日历',
			width:1000,
			height:650,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){   //提交时处理
				try{
					if($("#zxDataEdit_Form").form('validate')){
						if(!ZxData.description.check()){
							return false;
						}
						var loc_dateTime = $.trim($("#zxDataEdit_dateTime").val() || "");
						if(loc_dateTime){
							var loc_dateTimes = loc_dateTime.split(" ");
							$("#zxDataEdit_dateTime")
								.next().val(loc_dateTimes[0])
								.next().val(loc_dateTimes[1]);
						}
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'zxDataEdit_Form',
							onSuccess : function(data){  		//提交成功后处理
								var d = $.parseJSON(data);
								if (d.success) {
									$("#myWindow").dialog("close");
									ZxData.refresh();
									$.messager.alert($.i18n.prop("common.operate.tips"),'修改成功！','info');
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败，原因：'+d.msg,'error');
								}
							}
						});
					}
				}catch(e){
					alert(e);
				}
			},
			onLoad : function(){
				$("#zxDataEdit_Form span[an]").each(function(){
					$(this).html(ZxData.formatByDicts($(this).attr("an"), $(this).next().val()));
				});
				$("#zxDataEdit_Form select[name]").each(function(){
					$(this).val($(this).next().val());
				});
				ZxData.description.init();
				
				//重要指数变化
				$("#zxDataEdit_importanceLevel").bind("change", function(){
					$("#zxDataEdit_descriptionPanel tr").each(function(){
						ZxData.description.change($(this));
					});
				});
			}
		});
	},
	
	/**
	 * 删除
	 * @param item
	 */
	del : function(item){
		var loc_dataId = $(item).siblings("input").val();
		var url = formatUrl(basePath + '/zxDataController/delete.do');
		goldOfficeUtils.deleteOne(ZxData.gridId, loc_dataId, url, "确认删除吗？");
	},

	setEventView : function(){
		var loc_datagrid = $("#zxDataReview_datagrid").datagrid({
			fit : false,
			fitColumns : true,
			idField : "id",
			columns : [[
				{title : $.i18n.prop("common.operate"), field:'op', formatter: function(value, rowData, rowIndex){
					$("#zxDataReview_datagrid_rowOperation input").val(rowIndex);
					return $("#zxDataReview_datagrid_rowOperation").html();
				}},
				{title : "序号",field : 'id'},
				{title : "分析师",field : 'userName'},
				{title : "点评内容",field : 'comment'}
			]]
		});

		$("#zxDataReview_page").pagination({
			pageSize : 15,
			pageList : [15, 30, 50, 100]
		});
	},
	/**
	 * 设置作者列表
	 * @param id
	 */
	setAuthorList:function(){
		$('#zxDataReview_Form #authorAvatar').combogrid({
			idField:'userNo',
			textField:'userName',
			url:basePath+'/userController/getAnalystList.do?hasOther=true',
			panelWidth:200,
			columns:[[
				{field : 'userNo',hidden:true},
				{field : 'author_Key_id',hidden:true,formatter : function(value, rowData, rowIndex) {
					return 'author_Key_id';
				}},
				{field : 'userName',title : '姓名',width:100},
				{field : 'position',hidden:true},
				{field : 'avatar',title : '头像',width:40,formatter : function(value, rowData, rowIndex) {
					if(isBlank(value)){
						return '';
					}
					return '<img src="'+value+'" style="height:35px;width:35px;"/>';
				}}
			]],
			onSelect:function(rowIndex, rowData){
				$('#zxDataReview_Form input[name=userId]').val(rowData.userNo);
				$('#zxDataReview_Form input[name=name]').val(rowData.userName);
				$('#zxDataReview_Form input[name=avatar]').val(rowData.avatar);
			},
			onBeforeLoad: function (node, param) {
				$('#zxDataReview_Form table tr td span.combo input').width(200);
			}
		});
	},
	/**
	 * 打开点评
	 * @param item
	 */
	review:function(item){
		var loc_dataId = null;
		if(typeof item === "string"){
			loc_dataId = item;
		}else{
			loc_dataId = $(item).siblings("input").val();
		}
		var url = formatUrl(basePath + '/zxDataController/review.do');
		goldOfficeUtils.ajax({
			url : url,
			data : {
				dataId : loc_dataId
			},
			success: function(data) {
				if (data) {
					var loc_dataInfo = data.data;
					var user = data.user;
					goldOfficeUtils.openSimpleDialog({
						dialogId : "zxDataView_win",
						title : '点评数据',
						width : 600,
						height : 400,
						onOpen : function(){
							ZxData.setAuthorList();
							if(/^analyst_/.test(user.role.roleNo)) {
								$('#zxDataReview_Form #authorAvatar').combogrid('setValue', user.userNo);
							}
							ZxData.viewComments(loc_dataInfo);
						},
						buttons	 : [{
								text : '提交',
								iconCls : "ope-edit",
								handler : function() {
									ZxData.saveReview();
								}
							},
							{
								text : '关闭',
								iconCls : "ope-close",
								handler : function() {
									$("#zxDataView_win").dialog("close");
								}
							}]
					});
				}else{
					$.messager.alert($.i18n.prop("common.operate.tips"),'获取点评数据信息失败!','error');
				}
			}
		});
	},

	/**
	 * 显示点评数据
	 * @param data
	 */
	viewComments : function(data){
		$('#zxDataReview_Form input[name="dataId"]').val(data.dataId);

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

		$("#zxDataReview_datagrid").datagrid("loadData", comments.slice(0, 15));
		$("#zxDataReview_page").pagination('refresh', {
			total : comments.length,
			onSelectPage : function(pageNo, pageSize) {
				var start = (pageNo - 1) * pageSize;
				var end = start + pageSize;
				$("#zxDataReview_page").datagrid("loadData", comments.slice(start, end));
			}
		});
	},

	/**
	 * 保存点评
	 */
	saveReview:function(){
		var dataId = $('#zxDataReview_Form input[name="dataId"]').val();
		var id = $('#zxDataReview_Form input[name="id"]').val();
		var comment = $('#zxDataReview_Form #comment').val();
		var avatar = $('#zxDataReview_Form input[name="avatar"]').val();
		var userId = $('#zxDataReview_Form input[name="userId"]').val();
		var name = $('#zxDataReview_Form input[name="name"]').val();
		var url = formatUrl(basePath + '/zxDataController/saveReview.do');
		goldOfficeUtils.ajax({
			url : url,
			data : {
				dataId : dataId,
				id : id,
				userId: userId,
				name: name,
				avatar: avatar,
				comment : comment
			},
			success: function(data) {
				if (data.success) {
					$.messager.alert($.i18n.prop("common.operate.tips"),'点评成功!','info', function(){
						ZxData.review($('#zxDataReview_Form input[name="dataId"]').val());
						ZxData.cancelEdit();
					});
				}else{
					$.messager.alert($.i18n.prop("common.operate.tips"),'点评失败，原因：'+data.msg,'error');
				}
			}
		});
	},
	/**
	 * 修改点评
	 *
	 * @param item
	 */
	editReview : function(item){
		var rowIndex = $(item).siblings("input").val();
		var row = $('#zxDataReview_datagrid').datagrid('getData').rows[rowIndex];
		$('#zxDataReview_Form input[name="id"]').val(row.id);
		$('#zxDataReview_Form #comment').val(row.comment);
		$('#zxDataReview_Form #authorAvatar').combogrid('setValue', row.userId);
	},
	/**
	 * 取消修改
	 */
	cancelEdit : function(){
		$('#zxDataReview_Form input[name="id"]').val('');
		$('#zxDataReview_Form #comment').val('');
	},
	/**
	 * 删除点评
	 * @param item
	 */
	delReview : function(item){
		var rowIndex = $(item).siblings("input").val();
		var row = $('#zxDataReview_datagrid').datagrid('getData').rows[rowIndex];
		var url = formatUrl(basePath + '/zxDataController/delReview.do');
		var message = "您确定要删除记录吗?";
		var dataId = $('#zxDataReview_Form input[name="dataId"]').val();
		$.messager.confirm("操作提示", message , function(r) {
			if (r) {
				goldOfficeUtils.ajax({
					url : url,
					data : {
						dataId : dataId,
						id : row.id
					},
					success: function(data) {
						if (data.success) {
							$('#zxDataReview_datagrid').datagrid('deleteRow', rowIndex);
							$.messager.alert($.i18n.prop("common.operate.tips"),'删除成功!','info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'删除失败，原因：'+data.msg,'error');
						}
					}
				});
			}
		});
	}
};
		
//初始化
$(function() {
	ZxData.init();
});