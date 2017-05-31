<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<div style="padding:5px;overflow:hidden;">
  <form id="chatPointsInfoAdd_Form" class="yxForm" method="post">
    <input type="hidden" name="pointsId" id="chatPointsInfoAdd_pointsId">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
      <tr>
        <th width="30%">组别</th>
        <td width="70%" id="chatPointsInfoAdd_groupTypeTd">
            <span></span>
        	<select name="groupType" id="chatPointsInfoAdd_groupType" style="width: 155px;">
        	</select>
		</td>
      </tr>
      <tr>
        <th>手机号</th>
        <td id="chatPointsInfoAdd_userIdTd">
            <span></span>
        	<input type="text" name="userId" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入客户手机号'">
		</td>
      </tr>
      <tr>
        <th>类别</th>
        <td>
        	<select name="type" id="chatPointsInfoAdd_type" style="width: 155px;">
        	</select>
		</td>
      </tr>
      <tr>
        <th>项目</th>
        <td>
        	<select name="journal[0].item" id="chatPointsInfoAdd_item" style="width: 155px;">
        	</select>
		</td>
      </tr>
      <tr>
        <th>
        	积分
        	<span style="color: red;">（减分请输入负值）</span>
        </th>
        <td>
        	<input type="text" size="5" name="journal[0].change" class="easyui-validatebox" data-options="required:true,validType:'integer',missingMessage:'请输入积分值'">
		</td>
      </tr>
      <tr>
        <th>备注</th>
        <td>
        	<input type="text" name="journal[0].remark" style="width: 462px">
		</td>
      </tr>
    </table>
  </form>
</div>
  
