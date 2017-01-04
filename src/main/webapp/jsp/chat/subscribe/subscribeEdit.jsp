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
   .noticeTip,.statusTip,.noticeCycleTip{color:red;display:none;}
</style>
<script type="text/javascript">
$(function(){
	var setData = {'type':'${chatSubscribe.type}','analyst':'${chatSubscribe.analyst}','noticeType':'${chatSubscribe.noticeType}','startDate':formatterDate('${chatSubscribe.startDate}'),'endDate':formatterDate('${chatSubscribe.endDate}')};
	chatSubscribe.setFormHtml(JSON.parse('${chatSubscribeTypeObj}'), setData);
});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="subscribeEditFrom" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	<tr id="fixed_tr">
    		<th>订阅用户<span class="red">*</span></th>
    		<td>
    			<input type="text" name="userId" id="userId" value="${chatSubscribe.userId}" class="easyui-validatebox" data-options="required:true" placeholder="手机号" />
    		</td>
      	<th>房间组别<span class="red">*</span></th>
		<td>
			<select name="groupType" id="subscribe_groupType_select" style="width: 160px;" class="easyui-validatebox" data-options="required:true">
				<option value="">--请选择--</option>
				<c:forEach var="row" items="${chatGroupList}">
					<c:if test="${empty row.id}">
						<option value="${row.groupType }"<c:if test="${row.groupType==chatSubscribe.groupType}"> selected="selected"</c:if>>
							${row.name}
						</option>
					</c:if>
				</c:forEach>
			</select>
	     </td>
    		<th>状态<span class="red">*</span></th>
    		<td style="width:100px;">
    			<label><input type="radio" name="status" value="1"<c:if test="${chatSubscribe.status=='1'}"> checked="checked"</c:if> class="easyui-validatebox" data-options="required:true" />有效</label>
   				<label><input type="radio" name="status" value="0"<c:if test="${chatSubscribe.status=='0'}"> checked="checked"</c:if> class="easyui-validatebox" data-options="required:true" />无效</label>
   				<span class="statusTip">请选择</span>
    		</td>
    		<th>消耗积分<span class="red">*</span></th>
    		<td><input type="text" name="point" id="point" value="${chatSubscribe.point }" /></td>
    	</tr>
    	
      <tr>
        <th>备注</th>
        <td colspan="7"><input type="text" name="remarks" style="width:80%" value="${chatSubscribe.remarks}" /></td>
      </tr>
    </table>
    <input type="hidden" name="id" value="${chatSubscribe.id}" />
    <input type="hidden" name="valid" value="${chatSubscribe.valid}" />
  </form>
</div>
