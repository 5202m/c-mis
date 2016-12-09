<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/system/user/user.js" charset="UTF-8"></script>
<div id="editWindow" class="easyui-dialog" closed="true"></div>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:120px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="system_user_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%"><spring:message code="user.no" /><!-- 账号 --></th>
          <td width="23%"><input type="text" name="userNo" id="userNo" style="width:160px"/></td>
          <th width="10%"><spring:message code="common.status" /><!-- 状态 --></th>
          <td width="23%">
          	 <select id="status" name="status" style="width:160px">
          	 	<option value=""><spring:message code="common.pleaseselect" /><!-- 请选择 --></option>
	          	<option value="0"><spring:message code="common.enabled" /><!-- 启用 --></option>
	          	<option value="1"><spring:message code="common.disabled" /><!-- 禁用 --></option>
	          </select>
          </td>
        </tr>
       <tr>
          <th>所属系统</th>
          <td>
              <select name="role.systemCategory" id="user_list_systemCategory" style="width:160px" class="systemCategorySelect" data-type="group1">
                  <option value=""><spring:message code="common.pleaseselect" /><!-- 请选择 --></option>
                  <c:forEach var="systemCategory" items="${systemCategoryList}">
                      <option value="${systemCategory.code}">${systemCategory.name}</option>
                  </c:forEach>
              </select>
          </td>
          <th><spring:message code="user.role" /><!-- 所属角色 --></th>
          <td>
	          <select id="role" name="role" style="width:160px" class="roleSelect"  data-type="group1">
	              <option value=""><spring:message code="common.pleaseselect" /><!-- 请选择 --></option>
	          </select>
          </td>
        </tr>
        <tr>
            <th><spring:message code="user.position" /><!-- 职位 --></th>
            <td><input type="text" name="position" id="position" style="width:160px"/></td>
          	<td colspan="2" align="right">&nbsp;&nbsp;
	        	<a href="#" class="easyui-linkbutton" id="system_user_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /><!-- 查询 --> </a> 
	        	&nbsp;&nbsp; <a href="#" class="easyui-linkbutton" id="system_user_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /><!-- 清空 --> </a>
        	</td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="system_user_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="system_user_datagrid_toolbar">
    <a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="systemUser.add();"><spring:message code="common.buttons.add" /><!-- 新增 --></a> 
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"    onclick="systemUser.batchDel();"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"   onclick="systemUser.refresh();"><spring:message code="common.buttons.refresh" /><!-- 刷新 --></a> 
    <a class="easyui-linkbutton exitRoom" data-options="plain:true,iconCls:'ope-cancel',disabled:false"   onclick="systemUser.exitChatRoom();">退出房间</a>
  </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="system_user_datagrid_rowOperation">
	  <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="systemUser.edit(this.id)"><spring:message code="common.buttons.edit" /><!-- 修改 --></a>
	  <a class="easyui-linkbutton view" data-options="plain:true,iconCls:'ope-view',disabled:false" onclick="systemUser.view(this.id)"><spring:message code="common.buttons.view" /><!-- 查看 --></a>
	  <a class="easyui-linkbutton resetPwd" data-options="plain:true,iconCls:'ope-redo',disabled:false" onclick="systemUser.resetPwd(this.id)"><spring:message code="user.resetpwd" /><!-- 重设密码 --></a>
      <a class="easyui-linkbutton setLiveLinks" data-options="plain:true,iconCls:'ope-edit',disabled:false" onclick="systemUser.saveLiveLinks(this.id)">直播地址</a>
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="systemUser.del(this.id)"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
  </div>
  
  <input style="display: none;" type="hidden" name="opType" id="userOpType" value="${opType }">
</div>
