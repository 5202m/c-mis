<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<div style="padding:5px;overflow:hidden;">
  <form id="tokenAccessAddForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
      <tr>
      	<th width="15%">使用平台<span class="red">*</span></th>
      	<td width="35%" colspan="3">
      		<input type="hidden" name="platform" id="platform" value="${systemCategory}"/>
      		${systemFlag}
      	</td>
      </tr>
      <tr>
        <th width="15%">appId<span class="red">*</span></th>
        <td width="35%"><input type="text" name="appId" id="appId" style="width: 185px;" class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'请输入appId'"/>
		<th width="15%">appSecret<span class="red">*</span></th>
        <td width="35%"><input type="text" name="appSecret" id="appSecret" value="${appSecret}"/></td>
      </tr>
      <tr>
      	<th width="15%">有效时间</th>
        <td width="35%">
        	<select id="expires" name="expires" style="width: 185px;">
        		<option value="0">一次有效</option>
         		<option value="1">1小时</option>
         		<option value="2" selected="selected">2小时</option>
         	</select>
        </td>
        <th width="15%">状态</th>
        <td width="35%">
        	<select id="status" name="status" style="width: 185px;">
        		<option value="1">启用</option>
         		<option value="0">禁用</option>
         	</select>
        </td>
      </tr>
      <tr>
      	 <th width="15%">备注</th>
      	 <td width="35%" colspan="3">
      	 	<textarea rows="60" cols="80" name="remark"></textarea>
         </td>
      </tr>
    </table>
  </form>
</div>