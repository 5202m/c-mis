<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/groupList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:120px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="chatGroup_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">编号</th>
          <td width="20%"><input type="text" name="id" /></td>
          <th width="10%">名称</th>
          <td width="20%"><input type="text" name="name" /></td>
          <th width="10%">状态</th>
          <td width="20%"><t:dictSelect id="chatGroupStatus" field="status" isEdit="false" isShowPleaseSelected="false"  dataList="${statusList}"/></td>
        </tr>
        <tr>
          <th width="15%">主页链接规则</th>
          <td width="35%">
             <select class="easyui-combotree" style="width:170px;" id="chatHomeUrlRuleId" name="homeUrlRuleId" ></select>
          </td>
          <th width="15%">聊天内容规则</th>
          <td width="35%" colspan="4">
             <select class="easyui-combotree" style="width:170px;" name="contentRuleId"  id="chatContentRuleIds" data-options="cascadeCheck:false" multiple></select>
          </td>
	      </tr>
        <tr>
          <td colspan="6" align="right">&nbsp;&nbsp;
			  <a href="#" class="easyui-linkbutton" id="chatGroup_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /></a> &nbsp;&nbsp; 
			  <a href="#" class="easyui-linkbutton" id="chatGroup_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="chatGroup_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="chatGroup_datagrid_toolbar" style="display:none;">
    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="chatGroup.add();">新增</a>
    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-reload',disabled:false"  onclick="chatGroup.refresh()"><spring:message code="common.buttons.refresh" /></a>
    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatGroup.batchDel();">删除</a>
  </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="chatGroup_datagrid_rowOperation" style="display:none;">
	  <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatGroup.edit(this.id)"><spring:message code="common.buttons.edit" /></a>
	  <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatGroup.del(this.id)"><spring:message code="common.buttons.delete" /></a>
  </div>
 
</div>