<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/lib/dateTimeWeek.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/dateTimeWeek.js" charset="UTF-8"></script>
<script type="text/javascript">
	var gagDateTmp='${gagDate}';
	$("#userGag_gagDate_div").dateTimeWeek({data:(isValid(gagDateTmp)?JSON.parse(gagDateTmp):null)});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="userGagForm" class="yxForm" method="post">
    <input type="hidden" name="memberId" value="${memberId}"/>
    <input type="hidden" name="groupId" value="${groupId}"/>
    <input type="hidden" name="groupType" value="${groupType}"/>
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
	      <tbody>
		      <tr>
		          <th width="30%">
		          	禁言时间段<br>
		          	<span style="color: red; font-weight: bolder;">
		          		(若不填值，则禁言无效或解除禁言)
		          	</span>
		          </th>
		          <td width="70%">
		            <input type="hidden" name="gagDate"  id="userGag_gagDate"/>
		            <div id="userGag_gagDate_div"></div>
		  		  </td>
		      </tr>
		      <tr>
		          <th>禁言提示语</th>
		          <td><input type="text" name="gagTips" value="${gagTips}" style="width:350px;" /></td>
		      </tr>
		      <tr>
		          <th>备注</th>
		          <td><input type="text" name="gagRemark" value="${gagRemark}" style="width:350px;" /></td>
		      </tr>
	      </tbody>
    </table>
  </form>
</div>
  
