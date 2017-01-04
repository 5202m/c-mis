<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<style type="text/css">
   .header_default div label{
      margin-right: 20px;
      margin-left: 20px;
      width: 33px;
   }
   .header_default div img{
      margin-right:10px;
      margin-left: 10px;
      width: 33px;
   }
   .analystsTip,.noticeTip,.statusTip,.noticeCycleTip,.startTip,.endTip{color:red;display:none;}
</style>
<script type="text/javascript">
$(function(){
	chatSubscribeType.setUserEdit('${chatSubscribeType.analysts}');
	var noticeTypes = '${chatSubscribeType.noticeTypes}';
	if(isValid(noticeTypes)){
		$.each(JSON.parse(noticeTypes),function(i, row){
			$('input[name="noticeType"]').each(function(){
				if($(this).val()==row.type){
					$(this).prop('checked', true);
					$('#'+$(this).val()+'Point').val(row.point);
				}
			});
		});
	}
	var noticeCycle = '${chatSubscribeType.noticeCycle}';
	if(isValid(noticeCycle)){
		$.each(JSON.parse(noticeCycle),function(i, row){
			$('input[name="cycle"]').each(function(){
				if($(this).val()==row.cycle){
					$(this).prop('checked', true);
					$('#'+$(this).val()+'Point').val(row.point);
				}
			});
		});
	}
});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="subscribeTypeEditFrom" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	<tr>
    		<th width="15%">服务类型编码<span class="red">*</span></th>
    		<td width="35%">
    			<input type="text" name="code" id="code" class="easyui-validatebox" data-options="required:true" value="${chatSubscribeType.code}" />
    		</td>
    		<th width="15%">服务类型名称<span class="red">*</span></th>
    		<td width="35%">
    			<input type="text" name="name" id="name" class="easyui-validatebox" data-options="required:true" value="${chatSubscribeType.name}" />
    		</td>
    	</tr>
    	<tr>
	      	<th>房间组别<span class="red">*</span></th>
			<td>
				<select name="groupType" id="subscribeType_groupType_select" style="width: 160px;" class="easyui-validatebox" data-options="required:true">
					<option value="">--请选择--</option>
					<c:forEach var="row" items="${chatGroupList}">
						<c:if test="${empty row.id}">
							<option value="${row.groupType }"<c:if test="${row.groupType==chatSubscribeType.groupType}"> selected="selected"</c:if>>
								${row.name}
							</option>
						</c:if>
					</c:forEach>
				</select>
		     </td>
		     <th>排序</th>
		     <td><input type="text" name="sequence" value="${chatSubscribeType.sequence}" /></td>
    	</tr>
    	<tr>
    		<th width="15%">可订阅老师<span class="red">*</span></th>
	        <td colspan="3">
		        <select class="easyui-combotree analysts" id="analystsEditSelectId" style="width:250px;" data-options="cascadeCheck:false" multiple>
	        	</select>
	        	<span class="analystsTip">请选择</span>
	        	<div id="analystsContent">
	        	</div>
	        	<input type="hidden" name="analysts" id="analysts" />
	        </td>
    	</tr>
    	<tr>
    		<th>提供订阅方式<span class="red">*</span></th>
    		<td>
    			<label><input type="checkbox" name="noticeType" value="email" cval="邮件" />邮件</label>
    			<input type="text" id="emailPoint" style="width:50px;" />积分
    			<label><input type="checkbox" name="noticeType" value="sms" cval="短信" />短信</label>
    			<input type="text" id="smsPoint" style="width:50px;" />积分<br />
    			<label><input type="checkbox" name="noticeType" value="wechat" cval="微信" />微信</label>
    			<input type="text" id="wechatPoint" style="width:50px;" />积分<br />
    			<span class="noticeTip">请选择</span>
    			<input type="hidden" name="noticeTypes" id="noticeTypes" />
    		</td>
    		<th>订阅周期</th>
    		<td>
    			<label><input type="checkbox" name="cycle" value="week" cval="1周" />1周</label>
    			<input type="text" id="weekPoint" style="width:50px;" />积分<br />
   				<label><input type="checkbox" name="cycle" value="month" cval="1月" />1月</label>
   				<input type="text" id="monthPoint" style="width:50px;" />积分<br />
   				<span class="noticeCycleTip">请选择</span>
   				<input type="hidden" name="noticeCycle" id="noticeCycle" />
    		</td>
    	</tr>
    	<tr>
	        <th width="15%">状态<span class="red">*</span></th>
    		<td colspan="3">
    			<label><input type="radio" name="status" value="1" class="easyui-validatebox" data-options="required:true"<c:if test="${chatSubscribeType.status==1}"> checked="checked"</c:if> />有效</label>
   				<label><input type="radio" name="status" value="0" class="easyui-validatebox" data-options="required:true"<c:if test="${chatSubscribeType.status==0}"> checked="checked"</c:if> />无效</label>
   				<span class="statusTip">请选择</span>
    		</td>
    	</tr>
    	<tr>
    		<th>服务开始时间<span class="red">*</span></th>
    		<td>
    			<input type="text" name="startDateStr" id="startDate" value="${startDateStr}" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate easyui-validatebox" data-options="required:true" />
    			<span class="startTip">请选择</span>
    		</td>
    		<th>服务结束时间<span class="red">*</span></th>
    		<td>
    			<input type="text" name="endDateStr" id="endDate" value="${endDateStr}" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate easyui-validatebox" data-options="required:true" />
    			<span class="endTip">请选择</span>
    		</td>
    	</tr>
      <tr>
        <th>备注</th>
        <td colspan="3"><input type="text" name="remark" style="width:80%" value="${chatSubscribeType.remark}" /></td>
      </tr>
    </table>
    <input type="hidden" name="id" value="${chatSubscribeType.id}" />
    <input type="hidden" name="valid" value="${chatSubscribeType.valid}" />
  </form>
</div>
