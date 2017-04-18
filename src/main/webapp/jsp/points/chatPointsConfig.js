/**
 * 积分配置管理<BR>
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
var chatPointsConfig = {
	gridId : 'chatPointsConfig_datagrid',
	dicts : {
		limitUnit : {
			A : '金币',
			B : '金币/天',
			C : '次',
			D : '次/天',
		}
	},
	init : function(){
		this.initDicts($("#chatPointsConfig_groupType"));
		this.initDicts($("#chatPointsConfig_type"));
		this.initDicts($("#chatPointsConfig_item"));
		this.initDicts($("#chatPointsConfig_status"));
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatPointsConfig.gridId,
			idField:"cfgId",
			sortName : 'cfgId',
			sortOrder : "desc",
			queryParams : {groupType : $("#chatPointsConfig_groupType").val()},
			url : basePath+'/chatPointsConfig/datagrid.do',
			columns : [[
			    {title : $.i18n.prop("common.operate"), field:'cfgId', formatter: function(value, rowData, rowIndex){
			    	$("#chatPointsConfig_datagrid_rowOperation input").val(value);
			    	return $("#chatPointsConfig_datagrid_rowOperation").html();
			    }},
				{title : "房间组别",field : 'groupType',formatter : function(value, rowData, rowIndex) {
					return chatPointsConfig.formatByDicts("groupType", value);
				}},
				{title : '客户组别',field : 'clientGroup',formatter : function(value, rowData, rowIndex) {
					var nameArr=[],tmpData={"simulate":"模拟用户",register:"注册用户",vip:"VIP用户,",active:"真实A用户",notActive:"真实N用户"};;
					if(value){
						for(var i in tmpData){
							if(value.join(",").indexOf(i)!=-1){
								nameArr.push(tmpData[i]);
							}
						}
					}
					return nameArr.join("，");
				}},
				{title : "积分类别",field : 'type',formatter : function(value, rowData, rowIndex) {
					return chatPointsConfig.formatByDicts("type", value);
				}},
				{title : "积分项目",field : 'item',formatter : function(value, rowData, rowIndex) {
					return chatPointsConfig.formatByDicts("item", value);
				}},
				{title : "积分值",field : 'val'},
				{title : "积分提示",field : 'tips'},
	            {title : "积分上限",field : 'limitVal',formatter : function(value, rowData, rowIndex) {
	            	return value ? (value + "&nbsp;" + chatPointsConfig.formatByDicts("limitUnit", rowData["limitUnit"])) : "无上限";
				}},
	            {title : "状态",field : 'status',formatter : function(value, rowData, rowIndex) {
	            	return chatPointsConfig.formatByDicts("status", value);
				}},
			]],
			toolbar : '#chatPointsConfig_datagrid_toolbar'
		});
	},
	
	/** 绑定事件 */
	setEvent:function(){
		/**查询*/
		$("#chatPointsConfig_queryForm_search").on("click",function(){
			var queryParams = $('#'+chatPointsConfig.gridId).datagrid('options').queryParams;
			var loc_val = $("#chatPointsConfig_val").val() || "";
			if(/^\d*$/.test(loc_val) == false){
				alert("积分值只能输入整数！");
				$("#chatPointsConfig_val").val(loc_val.replace(/[^\d]/g, ""));
				return;
			}
			queryParams["val"] = loc_val;
			$("#chatPointsConfig_queryForm select").each(function(){
				queryParams[$(this).attr("name")] = $(this).val();
			});
			$('#'+chatPointsConfig.gridId).datagrid({
				url : basePath+'/chatPointsConfig/datagrid.do',
				pageNumber : 1
			});
		});
		
		/**重置*/
		$("#chatPointsConfig_queryForm_reset").on("click",function(){
			$("#chatPointsConfig_queryForm")[0].reset();
		});
		
		/**类别联动事件*/
		chatPointsConfig.setEventType($("#chatPointsConfig_type"), $("#chatPointsConfig_item"));
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
	 * 检查表单
	 * @param $form
	 */
	checkForm : function($form){
		var limitVal = $form.find("input[name='limitVal']").val();
		var limitUnit = $form.find("select[name='limitUnit']").val();
		if(limitVal && !limitUnit){
			alert("请选择上限值单位。");
			return false;
		}else if(!limitVal && limitUnit){
			$form.find("select[name='limitUnit']").val("");
		}
		return true;
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
		chatPointsConfig.dicts[loc_name] = loc_metas;
	},
	
	/**
	 * 数据格式化
	 * @param key
	 * @param value
	 */
	formatByDicts : function(key, value){
		if(isBlank(value) || chatPointsConfig.dicts.hasOwnProperty(key) == false){
			return "";
		}
		var loc_obj = chatPointsConfig.dicts[key];
		return loc_obj.hasOwnProperty(value) ? loc_obj[value] : (value || "");
	},
	
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatPointsConfig.gridId).datagrid('reload');
	},
	
	/**
	 * 新增
	 */
	add : function(){
		var url = formatUrl(basePath + '/chatPointsConfig/preAdd.do');
		var submitUrl = formatUrl(basePath + '/chatPointsConfig/save.do');
		goldOfficeUtils.openEditorDialog({
			title : '新增积分配置',
			width:710,
			height:350,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				try{
					if($("#chatPointsConfigAdd_Form").form('validate') && chatPointsConfig.checkForm($("#chatPointsConfigAdd_Form"))){
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'chatPointsConfigAdd_Form',
							onSuccess : function(data){  		//提交成功后处理
								var d = $.parseJSON(data);
								if (d.success) {
									$("#myWindow").dialog("close");
									chatPointsConfig.refresh();
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
				chatPointsConfig.initSelect($("#chatPointsConfig_queryForm"), $("#chatPointsConfigAdd_Form"), false);
				/**类别联动事件*/
				chatPointsConfig.setEventType($("#chatPointsConfigAdd_type"), $("#chatPointsConfigAdd_item"));
				//默认手动加减分
				$("#chatPointsConfigAdd_type").val("hand");
				$("#chatPointsConfigAdd_item").val("hand_manual");
				 $("#chatPointsConfigAdd_groupType").change(function(){
						$("#chatPointsConfigAdd_clientGroup").combotree({
							panelWidth: 155,
							data:getJson(basePath + "/chatClientGroupController/getClientGroupList.do",{filter:'visitor',groupType:this.value}),
							onLoadSuccess:function(node, checked){
								$('#chatPointsConfigAdd_Form table tr td span.combo input').width(155);
							}
						}); 
				 }).trigger("change");
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
	 * 修改
	 * @param item
	 */
	edit : function(item){
		var loc_cfgId = $(item).siblings("input").val();
		var url = formatUrl(basePath + '/chatPointsConfig/preEdit.do?cfgId=' + loc_cfgId);
		var submitUrl = formatUrl(basePath + '/chatPointsConfig/save.do');
		goldOfficeUtils.openEditorDialog({
			title : '修改积分配置',
			width:710,
			height:350,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){   //提交时处理
				try{
					if($("#chatPointsConfigEdit_Form").form('validate') && chatPointsConfig.checkForm($("#chatPointsConfigEdit_Form"))){
						goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'chatPointsConfigEdit_Form',
							onSuccess : function(data){  		//提交成功后处理
								var d = $.parseJSON(data);
								if (d.success) {
									$("#myWindow").dialog("close");
									chatPointsConfig.refresh();
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
				chatPointsConfig.initSelect($("#chatPointsConfig_queryForm"), $("#chatPointsConfigEdit_Form"), true);
				 $("#chatPointsConfigEdit_groupType").change(function(){
					    var  val=$("#chatPointsConfigEdit_clientGroup").attr("defVal");
						$("#chatPointsConfigEdit_clientGroup").combotree({
							panelWidth: 155,
							data:getJson(basePath+"/chatClientGroupController/getClientGroupList.do",{filter:'visitor',clientGroup:val,groupType:$("#chatPointsConfigEdit_groupType_val").val()}),
							onLoadSuccess:function(node, checked){
								$('#chatPointsConfigEdit_Form table tr td span.combo input').width(155);
							}
						}); 
						$("#chatPointsConfigEdit_clientGroup").attr("defVal","");
				 }).trigger("change");
				/**类别联动事件*/
				chatPointsConfig.setEventType($("#chatPointsConfigEdit_type"), $("#chatPointsConfigEdit_item"));
			}
		});
	},
	
	/**
	 * 删除
	 * @param item
	 */
	del : function(item){
		var loc_smsCfgId = $(item).siblings("input").val();
		var url = formatUrl(basePath + '/chatPointsConfig/delete.do');
		goldOfficeUtils.deleteOne(chatPointsConfig.gridId, loc_smsCfgId, url, "确认删除吗？");
	}
};
		
//初始化
$(function() {
	chatPointsConfig.init();
});