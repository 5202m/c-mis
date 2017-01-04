<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<div style="padding:5px;overflow:hidden;">
  <form id="categoryEditForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
         <tr>
		   <th width="15%">系统编码<span class="red">*</span>（唯一）</th>
		   <td width="35%">
				   <%--  <span>${dictionaryJsonParam.code}</span> --%>
			   <input <c:if test="${not empty systemCategory.id}">readonly="readonly"</c:if> type="text" name="code" id="codeTmp" value="${systemCategory.code}" class="easyui-validatebox"
					  data-options="required:true,missingMessage:'系统编码不能为空'"/>
		   </td>
		  </tr>
	      <tr>
			  <th width="15%">系统名称<span class="red">*</span></th>
			  <td width="35%"><input type="text" name="name" id="name" value="${systemCategory.name}"  class="easyui-validatebox"
									 data-options="required:true,missingMessage:'系统名称不能为空'"/>
			  </td>
		  </tr>
		<tr>
			<th width="15%">描述信息</th>
			<td width="35%">
				<textarea name="remark" rows="5" style="width: 100%;">${systemCategory.remark}</textarea>
			</td>
		</tr>
		<tr>
			<th width="15%">状态<span class="red">*</span></th>
			<td width="35%">
				<select name="status">
					<option value="1" <c:if test="${systemCategory.status == 1}">selected</c:if> >启用</option>
					<option value="0" <c:if test="${systemCategory.status == 0}">selected</c:if> >禁用</option>
				</select>
			</td>
		</tr>
    </table>
    <input type="hidden" name="id"  value="${systemCategory.id}"/>
  </form>
</div>
  
