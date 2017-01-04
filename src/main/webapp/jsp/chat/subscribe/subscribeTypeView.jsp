<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript">
$(function(){
	var noticeTypes = '${chatSubscribeType.noticeTypes}',noticeTypesStr='';
	if(containSplitStr(noticeTypes, 'email')){
		if(isValid(noticeTypesStr)){ noticeTypesStr += ',';}
		noticeTypesStr += '邮件';
	}
	if(containSplitStr(noticeTypes, 'sms')){
		if(isValid(noticeTypesStr)){ noticeTypesStr += ',';}
		noticeTypesStr += '短信';
	}
	if(containSplitStr(noticeTypes, 'wechat')){
		if(isValid(noticeTypesStr)){ noticeTypesStr += ',';}
		noticeTypesStr += '微信';
	}
	$("#noticeTypes").text(noticeTypesStr);
	$('#analysts').text(chatSubscribeType.getAnalystCNameByCode('${chatSubscribeType.analysts}'));
	var noticeCycle = '${chatSubscribeType.noticeCycle}',noticeCycleStr='';
	if(containSplitStr(noticeCycle, 'week')){
		if(isValid(noticeCycleStr)){ noticeCycleStr += ',';}
		noticeCycleStr += '1周';
	}
	if(containSplitStr(noticeCycle, 'month')){
		if(isValid(noticeCycleStr)){ noticeCycleStr += ',';}
		noticeCycleStr += '1月';
	}
	$('#noticeCycle').text(noticeCycleStr);
});
</script>
<div style="padding:5px;overflow:hidden;">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	<tr>
    		<th>服务类型名称</th>
    		<td><span>${chatSubscribeType.name }</span></td>
	        <th width="15%">类型编码</th>
	        <td width="35%"><span>${chatSubscribeType.code }</span></td>
    	</tr>
      <tr>
        <th width="15%">订阅老师</th>
        <td width="35%" colspan="3"><span id="analysts"></span></td>
      </tr>
      <tr>
        <th width="15%">可订阅方式</th>
        <td width="35%"><span id="noticeTypes"></span></td>
        <th width="15%">可订阅周期</th>
        <td width="35%"><span id="noticeCycle"></span></td>
      </tr>
      <tr>
        <th width="15%">订阅老师</th>
        <td width="35%" colspan="3">
	        <span>
	        <c:if test="${chatSubscribeType.status==1}">有效</c:if>
	        <c:if test="${chatSubscribeType.status==0}">无效</c:if>
	        </span>
        </td>
      </tr>
      <tr>
        <th>备注</th>
        <td colspan="3"><span>${chatSubscribeType.remark}</span></td>
      </tr>
    </table>
</div>
  
