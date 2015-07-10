<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<div style="padding:5px;overflow:hidden;">
  <form id="chatClientGroupSubmitForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
	      <tr>
	          <th width="15%">编号</th>
	          <td width="85%">
	             <input type="text" name="id" value="${clientGroup.id}" <c:if test="${not empty clientGroup.id}">readonly="readonly"</c:if> class="easyui-validatebox" data-options="required:true,missingMessage:'请输入编号'"/>
	          </td>
	      </tr>
	      <tr>
	          <th width="15%">名称</th>
	          <td width="85%">
	             <input type="text" name="name" value="${clientGroup.name}" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入名称'"/>
	          </td>
	      </tr>
	      <tr>
	         <th width="15%">默认房间</th>
	         <td width="35%">
	           <select name="defChatGroupId" style="width:180px;">
	              <option value="">--请选择--</option>
	              <c:forEach var="row" items="${chatGroupList}">
	                 <option value="${row.id}" <c:if test="${row.id == clientGroup.defChatGroupId}">selected="selected"</c:if>>${row.name}</option>
	              </c:forEach>
	           </select>
	         </td>
	       </tr>
	       <tr>
	         <th width="15%">排序序号</th>
		     <td width="85%" colspan="3">
		        <input type="text" name="sequence" value="${clientGroup.sequence}" class="easyui-validatebox" data-options="required:true"/>
		     </td>
	       </tr>
	         <tr>
	         <th width="15%">备注</th>
	          <td width="85%">
	             <input type="text" name="remark" value="${clientGroup.remark}" style="width:300px;" />
	          </td>
	      </tr>
    </table>
  </form>
</div>
  
