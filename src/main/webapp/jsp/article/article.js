/**
 * 摘要：文章管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var article = {
	gridId : 'article_datagrid',
	init : function(){
		this.initGrid();
		this.setEvent();
		this.formatPlatfrom();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : article.gridId,
			idField : 'id',
			sortName : 'createDate',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/articleController/datagrid.do',
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#article_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#article_datagrid_rowOperation").html();
						}},
			            {title : '编号',field : 'idStr',formatter : function(value, rowData, rowIndex) {
							return rowData.id;
						}},
						{title : '标题',field : 'title',formatter : function(value, rowData, rowIndex) {
								return rowData.detailList[0].title;
						}},
						{title : '语言',field : 'lang',formatter : function(value, rowData, rowIndex) {
							var subList=rowData.detailList,result=[],langStr="";
							for(var index in subList){
								langStr=subList[index].lang;
								if(langStr=="zh"){
									result.push("简体");	
								}
								if(langStr=="tw"){
									result.push("繁体");	
								}
								if(langStr=="en"){
									result.push("英文");	
								}
							}
							return result.join("，");
					    }},
			            {title : '所属栏目',field : 'categoryNamePath',formatter : function(value, rowData, rowIndex) {
							return value.replace(",","--");
						}},
						{title : '所属平台',field : 'platform',formatter : function(value, rowData, rowIndex) {
							return article.formatPlatfrom(value);
						}},
						{title : '使用状态',field : 'status',sortable : true,formatter : function(value, rowData, rowIndex) {
							if (value == 0) {
								return '禁用';
							} else {
								return '启用';
							}
						}},
						{title : '作者',field : 'fieldvalue',formatter : function(value, rowData, rowIndex) {
							var val=rowData.detailList[0].authorInfo;
							if(isBlank(val)){
								return '';
							}
							return '<img src="'+val.avatar+'" style="width:30px;height:30px;" />'+val.name;
						}},
						{title : '发布开始时间',field : 'publishStartDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title : '发布结束时间',field : 'publishEndDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title : '创建时间',field : 'createDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}}
			]],
			toolbar : '#article_datagrid_toolbar'
		});
	},
	/**
	 * 
	 * @MethodName: formatPlatfrom
	 * @Description: TODO
	 * @param value
	 * @returns
	 */
	formatPlatfrom:function(value){
		if(isBlank(value)){
			return "";
		}
		var articlePlatformJson=$.parseJSON(articlePlatformStr);
		var row=null,result=[];
		var valArr=value.split(",");
		for(var i=0;i<valArr.length;i++){
			for(var index in articlePlatformJson){
				row=articlePlatformJson[index];
				if(valArr[i]==row.code){
					result.push(row.nameCN);
					break;
				}
			}
		}
		return result.join("，");
	},
	/**
	 * 
	 * @MethodName: setEvent
	 * @Description: TODO
	 * @return void
	 */
	setEvent:function(){
		// 列表查询
		$("#article_queryForm_search").on("click",function(){
			var queryParams = $('#'+article.gridId).datagrid('options').queryParams;
			article.clearQueryParams(queryParams);
			$("#article_queryForm input[name],#article_queryForm select[name]").each(function(){
				var qp=queryParams[this.name];
				if(isValid(qp)){
					queryParams[this.name]+=(","+$(this).val());
				}else{
					queryParams[this.name] = $(this).val();
				}
			});
			$('#'+article.gridId).datagrid({
				url : basePath+'/articleController/datagrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#article_queryForm_reset").on("click",function(){
			$("#article_queryForm")[0].reset();
		});
		//列表状态设置
		goldOfficeUtils.setGridSelectVal(article.gridId,"article_setStatusSelect","status",formatUrl(basePath + '/articleController/setStatus.do'));
	},
	/**
	 * 清空旧的参数
	 */
	clearQueryParams:function(queryParams){
		$("#article_queryForm input[name],#article_queryForm select").each(function(){
			if(isValid($(this).attr("name"))){
				queryParams[this.name] = "";
			}
			if(isValid($(this).attr("comboname"))){
				queryParams[$(this).attr("comboname")] = "";
			}
		});
	},
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+article.gridId).datagrid('reload');
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/articleController/del.do');
		goldOfficeUtils.deleteBatch('article_datagrid',url);	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#article_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/articleController/del.do');
		goldOfficeUtils.deleteOne('article_datagrid',recordId,url);
	},
	/**
	 * 跳转到预览、编辑、新增页面
	 * @param opType
	 * @param articleId
	 */
	getArticleInfo:function(opType, articleId){
		var title = {"C":"新增文档","U":"修改文档","R":"文档预览"}[opType];
		var buttons;
		if(opType=="R"){
			buttons = [{
				text : '关闭',
				iconCls : "ope-close",
				handler : function() {
					$(this).parents(".easyui-dialog:first").dialog("close");
				}
			}];
		}
		goldOfficeUtils.openEditorDialog({
			title : title,
			width : 900,
			height : 600,
			href : formatUrl(basePath + '/articleController/articleInfo.do?articleId=' + articleId + "&opType=" + opType+'&t='+new Date().getTime()),
			iconCls : 'ope-import',
			buttons : buttons,
			handler : function(){
				var articleInfo = ArticleTemplate.getArticle();
				if(!articleInfo){
					return;
				}
				var template = $("#aTempTd input:checked").val();
				if(opType == "C"){
					article.createArticle(articleInfo, template);
				}else if(opType == "U"){
					article.updateArticle(articleInfo, template);
				}
			}
		});
	},
	/**创建文档信息*/
	createArticle : function(articleInfo, template){
		getJson(formatUrl(basePath + '/articleController/create.do?template=' + template),articleInfo,function(data){
			$.messager.progress('close');
			if(data.success){
				$("#myWindow").dialog("close");
				article.refresh();
				$.messager.alert($.i18n.prop("common.operate.tips"),'添加成功！','info');
			}else{
				$.messager.alert($.i18n.prop("common.operate.tips"),'新增文档失败，错误信息：' + data.msg,'error');
			}
		},true);
	},
	/**修改文档信息*/
	updateArticle : function(articleInfo, template){
		getJson(formatUrl(basePath + '/articleController/update.do?template=' + template),articleInfo,function(data){
			$.messager.progress('close');
			if(data.success){
				$("#myWindow").dialog("close");
				article.refresh();
				$.messager.alert($.i18n.prop("common.operate.tips"),'更新文档成功！','info');
			}else{
				$.messager.alert($.i18n.prop("common.operate.tips"),'更新文档成功，错误信息：' + data.msg,'error');
			}
		},true);
	},
	/**
	 * 交易策略提取
	 */
	getTradeStrate:function(){
		var url = formatUrl(basePath + '/articleController/toTradeStrateGet.do');
		var submitUrl =  formatUrl(basePath + '/articleController/getTradeStrate.do');
		goldOfficeUtils.openEditorDialog({
			title : '交易策略提取',
			width : 680,
			height : 320,
			href : url,
			iconCls : 'ope-import',
			handler : function(){   //提交时处理
				if($("#tradeStrateGetForm").form('validate')){
					 var titles=$(".strateTitleDiv input").map(function(){
						return $.trim($(this).val());
					 }).get().join("|");
					 $("#strateTitlesId").val(titles);
					 $.messager.progress();
					 goldOfficeUtils.ajaxSubmitForm({
							url : submitUrl,
							formId : 'tradeStrateGetForm',
							onSuccess : function(data){//提交成功后处理
								var d = $.parseJSON(data);
								$.messager.progress('close');
								if(d.success) {
									$("#article_datagrid").datagrid('reload');
									$("#myWindow").dialog("close");
									$.messager.alert('提示','操作已执行','info');
								}else{
									$.messager.alert('提示','操作失败：'+d.msg,'error');
								}
							}
					});
				}
			}
		});
	}
};
		
//初始化
$(function() {
	article.init();
});