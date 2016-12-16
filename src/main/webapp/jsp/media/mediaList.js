/**
 * 摘要：媒介管理列表js
 * @author alan.wu
 * @date   2015/03/19
 */
var media = {
	gridId : 'media_datagrid',
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
			gridId : media.gridId,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			singleSelect : false,
			url : basePath+'/mediaController/datagrid.do',
			columns : [[
			            {title : 'id',field : 'id',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#media_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.id);
						    });
							return $("#media_datagrid_rowOperation").html();
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
							return media.formatPlatfrom(value);
						}},
						{title : '使用状态',field : 'status',sortable : true,formatter : function(value, rowData, rowIndex) {
							if (value == 0) {
								return '禁用';
							} else {
								return '启用';
							}
						}},
						{title : '发布开始时间',field : 'publishStartDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title : '发布结束时间',field : 'publishEndDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title : '序号',field : 'sequence',sortable : true},
						{title : '创建时间',field : 'createDate',sortable : true,formatter : function(value, rowData, rowIndex) {
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}}
			]],
			toolbar : '#media_datagrid_toolbar'
		});
	},
	/**
	 * 格式化平台
	 * @param value
	 * @returns
	 */
	formatPlatfrom:function(value){
		if(isBlank(value)){
			return "";
		}
		var mediaPlatformJson=$.parseJSON(mediaPlatformStr);
		var row=null,result=[];
		var valArr=value.split(",");
		for(var i=0;i<valArr.length;i++){
			for(var index in mediaPlatformJson){
				row=mediaPlatformJson[index];
				if(valArr[i]==row.code){
					result.push(row.nameCN);
					break;
				}
			}
		}
		return result.join("，");
	},
	setEvent:function(){
		// 列表查询
		$("#media_queryForm_search").on("click",function(){
			var queryParams = $('#'+media.gridId).datagrid('options').queryParams;
			media.clearQueryParams(queryParams);
			$("#media_queryForm input[name],#media_queryForm select[name]").each(function(){
				var qp=queryParams[this.name];
				if(isValid(qp)){
					queryParams[this.name]+=(","+$(this).val());
				}else{
					queryParams[this.name] = $(this).val();
				}
			});
			$('#'+media.gridId).datagrid({
				url : basePath+'/mediaController/datagrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#media_queryForm_reset").on("click",function(){
			$("#media_queryForm")[0].reset();
		});
		//列表状态设置
		goldOfficeUtils.setGridSelectVal(media.gridId,"media_setStatusSelect","status",formatUrl(basePath + '/articleController/setStatus.do'));
	},
	/**
	 * 清空旧的参数
	 */
	clearQueryParams:function(queryParams){
		$("#media_queryForm input[name],#media_queryForm select").each(function(){
			if(isValid($(this).attr("name"))){
				queryParams[this.name] = "";
			}
			if(isValid($(this).attr("comboname"))){
				queryParams[$(this).attr("comboname")] = "";
			}
		});
	},
	/**
	 * 功能：查看
	 * @param recordId   dataGrid行Id
	 */
	view : function(recordId){
		//jumpRequestPage(formatUrl(basePath + '/mediaController/'+recordId+'/view.do'));
		var url = formatUrl(basePath + '/mediaController/'+recordId+'/view.do');
		goldOfficeUtils.openSimpleDialog({
			title : $.i18n.prop("common.operatetitle.view"),       /**查看记录*/
			width : 1000,
			height : 515 ,
			href : url ,
			iconCls : 'pag-view'
		});
	},
	/**
	 * 功能：增加
	 */
	add : function(){
		//jumpRequestPage(formatUrl(basePath + '/mediaController/add.do'));
		var url = formatUrl(basePath + '/mediaController/add.do');
		var submitUrl =  formatUrl(basePath + '/mediaController/create.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			width : 1000,
			height : 700,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if(media.checkForm() && $("#mediaDetailForm").form('validate') && $("#media_tab form[name=mediaDetailForm]").form('validate')){
					media.checkClearAuthor();//清除无效的作者值
					var serializeFormData = $("#mediaBaseInfoForm").serialize();
					var detaiInfo=formFieldsToJson($("#media_tab form[name=mediaDetailForm]"));
					var detaiInfoObj = eval("("+detaiInfo+")");
					if($.isArray(detaiInfoObj)){
						$.each(detaiInfoObj, function(key, value){
							var authorInfo = {};
							authorInfo.userId = value.userId;
							authorInfo.avatar = value.avatar;
							authorInfo.position = value.position;
							authorInfo.name = value.name;
							detaiInfoObj[key].authorInfo = authorInfo;
						});
					}
					else{
						var authorInfo = {};
						authorInfo.userId = detaiInfoObj.userId;
						authorInfo.avatar = detaiInfoObj.avatar;
						authorInfo.position = detaiInfoObj.position;
						authorInfo.name = detaiInfoObj.name;
						detaiInfoObj.authorInfo = authorInfo;
					}
					detaiInfo = JSON.stringify(detaiInfoObj);
					$.messager.progress();//提交时，加入进度框
					var submitInfo = serializeFormData+"&detaiInfo="+encodeURIComponent(detaiInfo);
					getJson(submitUrl,submitInfo,function(data){
						$.messager.progress('close');
						if(data.success){
							$("#myWindow").dialog("close");
							media.refresh();
							$.messager.alert($.i18n.prop("common.operate.tips"),"新增成功 !",'info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),"新增失败，错误信息："+data.msg,'error');
						}
					},true);
				}
			}
		});
	},
	/**
	 * 功能：修改
	 */
	edit : function(recordId){
		//jumpRequestPage(formatUrl(basePath + '/mediaController/'+recordId+'/edit.do'));
		var url = formatUrl(basePath + '/mediaController/'+recordId+'/edit.do');
		var submitUrl =  formatUrl(basePath + '/mediaController/update.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			width : 1000,
			height : 700,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if(media.checkForm() && $("#mediaBaseInfoForm").form('validate') && $("#media_tab form[name=mediaDetailForm]").form('validate')){
					media.checkClearAuthor();//清除无效的作者值
					var serializeFormData = $("#mediaBaseInfoForm").serialize();
					var detaiInfo=formFieldsToJson($("#media_tab form[name=mediaDetailForm]"));
					var detaiInfoObj = eval("("+detaiInfo+")");
					if($.isArray(detaiInfoObj)){
						$.each(detaiInfoObj, function(key, value){
							var authorInfo = {};
							authorInfo.userId = value.userId;
							authorInfo.avatar = value.avatar;
							authorInfo.position = value.position;
							authorInfo.name = value.name;
							detaiInfoObj[key].authorInfo = authorInfo;
						});
					}
					else{
						var authorInfo = {};
						authorInfo.userId = detaiInfoObj.userId;
						authorInfo.avatar = detaiInfoObj.avatar;
						authorInfo.position = detaiInfoObj.position;
						authorInfo.name = detaiInfoObj.name;
						detaiInfoObj.authorInfo = authorInfo;
					}
					detaiInfo = JSON.stringify(detaiInfoObj);
					$.messager.progress();//提交时，加入进度框
					var submitInfo = serializeFormData+"&detaiInfo="+encodeURIComponent(detaiInfo);
					getJson(submitUrl, submitInfo, function(data){
						$.messager.progress('close');
						if(data.success){
							$("#myWindow").dialog("close");
							media.refresh();
							$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败，原因：'+d.msg,'error');  /**操作提示  修改失败!*/
						}
					},true);
				}
			}
		});
	},
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+media.gridId).datagrid('reload');
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/mediaController/del.do');
		goldOfficeUtils.deleteBatch('media_datagrid',url);	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#media_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/mediaController/del.do');
		goldOfficeUtils.deleteOne('media_datagrid',recordId,url);
	},
	/**
	 * 清除无效的作者值
	 */
	checkClearAuthor:function(){
		$("input[type=hidden][name^=media_authorList_]").each(function(){
			var lang=this.name.replace("media_authorList_","");
			var authorDom=$('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=author]');
			if(isBlank(this.value)){
				authorDom.val('');
			}else{
				if(isBlank(authorDom.val())){
					authorDom.val(this.value);
				}else{
					if(this.value!=authorDom.val().split(";")[0]){
						authorDom.val(this.value);
					}
				}
			}
		});
	},
	/**
	 * 检查表单输入框
	 */
	checkForm:function(){
		var isPass=true;
		$("#mediaBaseInfoForm input,#mediaBaseInfoForm select").each(function(){
			if(isBlank($(this).val())){
				if($(this).attr("name")=="categoryId"){
					alert("栏目不能为空！");
					isPass=false;
					return false;
				}
				if($(this).attr("name")=="publishStartDateStr"||$(this).attr("name")=="publishEndDateStr"){
					alert("发布时间不能为空！");
					isPass=false;
					return false;
				}
				if($(this).attr("name")=="platformStr"){
					alert("应用位置不能为空！");
					isPass=false;
					return false;
				}
				if($(this).attr("name")=="mediaUrl"){
					alert("媒体路径不能为空！");
					isPass=false;
					return false;
				}
			}
		});
		if(isPass && $("#mediaBaseInfoForm input[type=checkbox]:checked").length==0){
			alert("请选择语言！");
			isPass=false;
		}
		return isPass;
	}
};
		
//初始化
$(function() {
	media.init();
});