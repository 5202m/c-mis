<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript">
$(function(){
	$('#view_userId').text(formatMobileToUserId('${chatSubscribe.userId}'));
	$('#view_type').text(chatSubscribe.getDictNameByCode('#type','${chatSubscribe.type}'));
	$('#view_analyst').text(chatSubscribe.getAnalystCNameByCode('${chatSubscribe.analyst}'));
	var noticeTypes = [];
	if(containSplitStr('${chatSubscribe.noticeType}', 'email')){
		noticeTypes.push('邮件');
	}
	if(containSplitStr('${chatSubscribe.noticeType}', 'sms')){
		noticeTypes.push('短信');
	}
	if(containSplitStr('${chatSubscribe.noticeType}', 'wechat')){
		noticeTypes.push('微信');
	}
	$('#view_noticeType').text(noticeTypes.join('，'));
});
</script>
<div style="padding:5px;overflow:hidden;">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	<tr>
    		<th>订阅用户</th>
    		<td colspan="3"><span id="view_userId"></span></td>
    	</tr>
      <tr>
        <th width="15%">房间组别</th>
        <td width="35%"><span>
        <c:forEach var="row" items="${chatGroupList}">
			<c:if test="${empty row.id}">
				<c:if test="${row.groupType==chatSubscribe.groupType}">${row.name}</c:if>
			</c:if>
		</c:forEach>
        </span></td>
        <th width="15%">订阅服务</th>
        <td width="35%"><span id="view_type"></span></td>
      </tr>
      <tr>
        <th width="15%">订阅开始时间</th>
        <td width="35%"><span>${startDateStr}</span></td>
        <th width="15%">订阅结束时间</th>
        <td width="35%"><span>${endDateStr}</span></td>
      </tr>
      <tr>
        <th width="15%">订阅老师</th>
        <td width="35%"><span id="view_analyst"></span></td>
        <th width="15%">订阅方式</th>
        <td width="35%"><span id="view_noticeType"></span></td>
      </tr>
      <tr>
        <th>消耗积分</th>
        <td colspan="3"><span>${chatSubscribe.point}</span></td>
      </tr>
      <tr>
        <th>备注</th>
        <td colspan="3"><span>${chatSubscribe.remarks}</span></td>
      </tr>
    </table>
</div>
  
