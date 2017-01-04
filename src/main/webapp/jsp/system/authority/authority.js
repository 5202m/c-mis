/**
 * 摘要：权限管理公用js
 * @author Gavin.guo
 * @date   2014-10-24
 */
var systemAuthority = {
	gridId : 'system_authority_role_datagrid',
	menuId : '',
	count : 1,
	init : function(){
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : systemAuthority.gridId,
			singleSelect : true,
			idField:"roleId",
			sortName : 'roleId',
			url : basePath+'/roleController/datagrid.do',
			columns : [[
	            {title : 'roleId',field : 'roleId',hidden : true},
	            {title : $.i18n.prop("role.roleno"),field : 'roleNo',width : 60},     /**角色编号*/
	            {title : $.i18n.prop("role.rolename"),field : 'roleName',width : 80,sortable : true},   /**角色名称*/
				{title : $.i18n.prop("common.remark"),field : 'remark',width : 100}	   /**备注*/
			]],
			onSelect : function(rowIndex, rowData){
				systemAuthority.initMenuTree(rowData.roleId);
			}
		});
	},
	/**
	 * 功能：初始化菜单树
	 */
	initMenuTree : function(roleId){
		$("#menuAuthorityTree").show();
		systemAuthority.menuId = $('#menuAuthorityTree');
		systemAuthority.menuId.tree({
            checkbox: true,
            cascadeCheck : false,
            lines: true,
            onLoadSuccess : function(node, data){
            	systemAuthority.menuId.tree('options').cascadeCheck = true;
            },
            url: basePath+'/authorityController/loadMenuTree.do?roleId='+roleId
		});
	},
	/**
	 * 功能：保存菜单
	 */
	saveAuthorityMenu : function(){
		var ids=[],types=[],nodes = systemAuthority.menuId.tree('getChecked');
		var selectRecords = $("#"+systemAuthority.gridId).datagrid("getSelected");
		if(null == selectRecords){
			$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("role.pleaseselectrole"),'warning');   /**操作提示  请选择一个角色!*/
			return false;
		}
		// 获取勾选中的
		if(nodes != null && nodes.length > 0){
			for(var i=0;i<nodes.length;i++){
				ids.push(nodes[i].id);
				types.push(nodes[i].attributes.type);
			}
		}
		// 获取级联选中的
		var otherNodes = systemAuthority.menuId.tree('getChecked','indeterminate');
		if(otherNodes != null &&  otherNodes.length > 0){
			for(var i=0;i<otherNodes.length;i++){
				ids.push(otherNodes[i].id);
				types.push(otherNodes[i].attributes.type);
			}
		}
		goldOfficeUtils.ajax({
			url : basePath + '/authorityController/update.do',
			data : {
				roleId : selectRecords.roleId,
				ids : ids.join(","),
				types:types.join(",")
			},
			success: function(data) {
				if(data.success) {
					$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.savesuccess"),'info');   /**操作提示 保存成功!*/
				}else{
					$.messager.alert($.i18n.prop("common.operate.tips"),data.msg,'error');     /**操作提示 保存失败!*/
					systemAuthority.initMenuTree(selectRecords.roleId);
				}
			}
		});
	},
	setEvent:function(){
		$("#system_authority_role_search").on("click",function(){
			var roleNoOrName =  $("#roleNoOrName").val();
			var queryParams = $('#'+systemAuthority.gridId).datagrid('options').queryParams;
			queryParams['roleNoOrName'] = roleNoOrName;
			$('#'+systemAuthority.gridId).datagrid({
				url : basePath+'/roleController/datagrid.do',
				pageNumber : 1
			});
			$("#menuAuthorityTree").hide();
		});
	}
};
		
//初始化
$(function() {
	systemAuthority.init();
});