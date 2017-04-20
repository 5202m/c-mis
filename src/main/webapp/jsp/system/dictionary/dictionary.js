/**
 * 摘要：数据字典管理公用js
 * @author Gavin.guo
 * @date   2014-10-27
 */
var systemDictionary = {
	gridId : 'system_dictionary_datagrid',
	init : function(){
		this.initTreeGrid();
		this.setEvent();
	},
	/**
	 * 功能：treeGrid初始化
	 */
	initTreeGrid : function(){
		$('#'+systemDictionary.gridId).treegrid({
			idField : 'id', 
 			treeField : 'code',
			method : 'post',
			url : basePath+'/dictionaryController/treeGrid.do',
			nowrap: false,
			fit : true,
			fitColumns:true ,
			rownumbers: true,
			animate:true,
			collapsible:true,
			frozenColumns:[[
	            {title : $.i18n.prop("common.operate"),field : 'todo',width:170,fixed : false,formatter : function(value, rowData, rowIndex) {
				    	$("#system_dictionary_datagrid_rowOperation a").each(function(){
				    		$(this).attr("id",rowData.id);
						});
				    	return $("#system_dictionary_datagrid_rowOperation").html();
	           	}},
			    {title:$.i18n.prop("dictionary.no"),field:'code',width:250}   /**字典编号*/
			]],
			columns:[[
			    {title : 'id',field:'id',hidden : true},
			    {title : 'parentCode',field:'parentCode',hidden : true},
			    {title : 'nameCN',field:'nameCN',hidden : true},
			    {title : 'nameTW',field:'nameTW',hidden : true},
			    {title : 'nameEN',field:'nameEN',hidden : true},
			    {title : $.i18n.prop("dictionary.name"),field:'name',width:70},  
			    {title : "系统类别",field:'flag',width:70},
			    {title : "参数值",field:'value',width:70},
			    {title : '状态',field:'status',width:30,formatter:function(value, rowData, rowIndex) {
			    	if (value == 1) {
			    		return '启用';
			    	} else {
			    		return '禁用';
			    	}
			    }},
			    {title : 'type',field:'type',width:100,hidden : true},
			    {title : "排序号",field:'sort',width:30},
			]],
			// 此方法主要用于异步处理
			onBeforeLoad:function(row,param){
				if(row){
					$(this).treegrid('options').url = basePath+'/dictionaryController/loadChildTreeGrid.do?typeGroupId='+row.code;
				} else {
					$(this).treegrid('options').url = basePath+'/dictionaryController/treeGrid.do?dictionaryCodeS='
													+$("#dictionaryCodeS").val()+"&dictionaryNameS="+$("#dictionaryNameS").val()+"&dictionaryStatusS="+$("#dictionaryStatusS").val();
				}
			},
			toolbar : '#system_dictionary_datagrid_toolbar'
		});
	},
	setEvent:function(){
		$("#system_dictionary_queryForm_search").on("click",function(){
			$('#'+systemDictionary.gridId).treegrid({
				url : basePath+'/dictionaryController/treeGrid.do?dictionaryCodeS='+$("#dictionaryCodeS").val()
					          +"&dictionaryNameS="+$("#dictionaryNameS").val()+"&dictionaryStatusS="+$("#dictionaryStatusS").val(),
				method : 'post'
			});
		});
		$("#system_dictionary_queryForm_reset").on("click",function(){
			$("#system_dictionary_queryForm")[0].reset();
		});
	},
	/**
	 * 功能：新增字典分组
	 */
	addTypeGroup : function(){
		systemDictionary.addCommon(1);
	},
	/**
	 * 功能：新增字典
	 */
	addType : function(){
		var node = $('#'+systemDictionary.gridId).treegrid('getSelected');
		if(node == null || node.type == 2){
			$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("dictionary.pleaseselectgroup"),'warning'); /**操作提示 请选择一个字典分组!*/
			return;
		}
		systemDictionary.addCommon(2);
	},
	formatName:function(obj){
		return LOCALE_ZH_CN==locale?obj.nameCN:(LOCALE_ZH_TW==locale?obj.nameTW:obj.nameEN);
	},
	addCommon : function(type){
		var node = $('#'+systemDictionary.gridId).treegrid('getSelected');
		if(node && type == 2){
			parentCode = node.code;
		}else{
			parentCode="";
		}
		var url = basePath + '/dictionaryController/add.do?type='+type+'&parentCode='+parentCode;
		var submitUrl =  basePath + '/dictionaryController/create.do';
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			height : 235,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#dictionaryAddForm").form('validate')){
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'dictionaryAddForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								//如果新增的是数据字典分组，则刷新treeGrid,如果新增的是数据字典,则将新增的节点添加到选中的分组下面
								if(type == 1){                    
									$('#'+systemDictionary.gridId).treegrid('reload');
								}
								else{
									if(d.obj != null){
										var node = $('#'+systemDictionary.gridId).treegrid('getSelected');
										$('#'+systemDictionary.gridId).treegrid('append',{
											parent: node.id,
											data: [{
												code : d.obj.code,
												parentCode:node.parentCode,
												name: systemDictionary.formatName(d.obj),
												nameCN: d.obj.nameCN,
												nameTW: d.obj.nameTW,
												nameEN: d.obj.nameEN,
												flag:d.obj.systemCategory,
												value:d.obj.value,
												type : 2
											}]
										});
										$('#'+systemDictionary.gridId).treegrid('reload',node.id);
									}
								}
								$("#myWindow").dialog("close");
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.addsuccess"),'info'); /**操作提示 新增成功!*/
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.addfail"),'error'); /**操作提示 新增失败!*/
							}
						}
					});
				}
			},
			onLoad:function(){
				systemDictionary.setComboTreeWidth('systemCategoryStr', 'dictionaryAddForm');
			}
		});
	},
	/**
	 * 功能：修改
	 */
	edit : function(thisObj){
		$(thisObj).parent().click();//先触发父类点击事件使其选中
		var node = $('#'+systemDictionary.gridId).treegrid('getSelected');
		var url = basePath + '/dictionaryController/edit.do'+'?id='+node.id+'&type='+node.type;
		var submitUrl =  basePath + '/dictionaryController/update.do';
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.edit"),	 /**修改记录*/
			height : 235,
			href : encodeURI(url),
			iconCls : 'pag-edit',
			handler : function(){   //提交时处理
				if($("#dictionaryEditForm").form('validate')){
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'dictionaryEditForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$('#'+systemDictionary.gridId).treegrid('update',{
									id: d.obj.id,
									row: {
										name: systemDictionary.formatName(d.obj),
										status: d.obj.status,
										nameCN: d.obj.nameCN,
										nameTW: d.obj.nameTW,
										nameEN: d.obj.nameEN,
										value:d.obj.value,
										flag:d.obj.systemCategory
									}
								});
								$("#myWindow").dialog("close");
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editfail"),'error');  /**操作提示  修改失败!*/
							}
						}
					});
				}
			},
			onLoad:function(){
				systemDictionary.setComboTreeWidth('systemCategoryStr', 'dictionaryEditForm');
			}
		});
	},
	/**
	 * 功能：删除
	 * @param id treeGrid 字段(字段之间以,分割)
	 */
	del : function(thisObj){
		$(thisObj).parent().click();//先触发父类点击事件使其选中
		var node = $('#'+systemDictionary.gridId).treegrid('getSelected');
		var type = node.type , message = $.i18n.prop("common.confirmdel");  /**您确定要删除记录吗?*/
		if(type == 1){
			message = $.i18n.prop("menu.delete");	/**确定要删除该节点及对应的子节点吗?*/
		}
		goldOfficeUtils.deleteTreeGridOne({
			message : message,
			url : basePath + '/dictionaryController/oneDel.do',
			data : {
				id : node.id,
				type : type
			},
			success : function(data){
				if(data.success){
					$('#'+systemDictionary.gridId).treegrid('remove',node.id);
					$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.delsuccess"),'info'); /**操作提示 删除成功!*/
				}else{
					$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.delfail"),'error');  /**操作提示 删除失败!*/
				}
			}
		});
	},
	/**
	 * 功能：刷新
	 */
	reload : function(){
		var node = $('#'+systemDictionary.gridId).treegrid('getSelected');
		if(node){
			$('#'+systemDictionary.gridId).treegrid('reload',node.id);
		}else{
			$('#'+systemDictionary.gridId).treegrid('reload');
		}
	},
	/**
	 * 设置多选下拉框宽度
	 * @param id
	 * @param formId
	 */
	setComboTreeWidth:function(id, formId){
		$('#'+id).combotree({
			panelWidth:180,
			onBeforeLoad: function (node, param) {
				$('#'+formId+' table tr td span.combo input').width(180);
			}
		});
	}
};
		
//初始化
$(function() {
	systemDictionary.init();
});