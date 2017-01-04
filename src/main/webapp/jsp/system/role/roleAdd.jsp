<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<div style="padding:5px;overflow:hidden;">
  <form id="roleAddForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
      <tr>
        <th width="15%"><spring:message code="role.roleno" /><!-- 角色编号 --><span class="red">*</span></th>
        <td width="35%">
        	<input type="text" name="roleNo" id="roleNo" class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="role.valid.rolenorequire" />'"/>
		</td>
        <th width="15%"><spring:message code="role.rolename" /><!-- 角色名称 --><span class="red">*</span></th>
        <td width="35%"><input type="text" name="roleName" id="roleName" class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="role.valid.rolenamerequire" />'"/></td>
      </tr>
      <tr>
        <th width="15%">角色类别</th>
        <td width="35%">
        	<t:dictSelect field="roleType" isEdit="true" isShowPleaseSelected="true"  dataList="${roleTypeList}"/>
         </td>
        <th width="15%"><spring:message code="common.status" /><!-- 状态 --></th>
        <td width="35%">
        	<select id="status" name="status">
         		<option value="0"><spring:message code="common.enabled" /><!-- 启用 --></option>
         		<option value="1"><spring:message code="common.disabled" /><!-- 禁用 --></option>
         	</select>
         </td>
      </tr>
      <tr>
        <th width="15%"><spring:message code="common.remark" /><!-- 备注 --></th>
        <td width="35%" colspan="3"><textarea name="remark" id="remark"  rows="5" cols="70"></textarea></td>
      </tr>
    </table>
  </form>
</div>
  
