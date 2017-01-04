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
		jumpRequestPage(formatUrl(basePath + '/mediaController/'+recordId+'/view.do'));
	},
	/**
	 * 功能：增加
	 */
	add : function(){
		jumpRequestPage(formatUrl(basePath + '/mediaController/add.do'));
	},
	/**
	 * 功能：修改
	 */
	edit : function(recordId){
		jumpRequestPage(formatUrl(basePath + '/mediaController/'+recordId+'/edit.do'));
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
	}
};
		
//初始化
$(function() {
	media.init();
});