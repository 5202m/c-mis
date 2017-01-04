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
	chatSubscribe.setFormHtml(JSON.parse('${chatSubscribeTypeObj}'));
});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="subscribeAddFrom" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	<tr id="fixed_tr">
    		<th>订阅用户<span class="red">*</span></th>
    		<td>
    			<input type="text" name="userId" id="userId" class="easyui-validatebox" data-options="required:true" placeholder="手机号" />
    		</td>
      	<th>房间组别<span class="red">*</span></th>
		<td>
			<select name="groupType" id="subscribe_groupType_select" style="width: 160px;" class="easyui-validatebox" data-options="required:true">
				<option value="">--请选择--</option>
				<c:forEach var="row" items="${chatGroupList}">
					<c:if test="${empty row.id}">
						<option value="${row.groupType }">
							${row.name}
						</option>
					</c:if>
				</c:forEach>
			</select>
	     </td>
    		<th>状态<span class="red">*</span></th>
    		<td style="width:100px;">
    			<label><input type="radio" name="status" value="1" class="easyui-validatebox" data-options="required:true" />有效</label>
   				<label><input type="radio" name="status" value="0" class="easyui-validatebox" data-options="required:true" />无效</label>
   				<span class="statusTip">请选择</span>
    		</td>
    		<th>消耗积分<span class="red">*</span></th>
    		<td><input type="text" name="point" id="point" value="" /></td>
    	</tr>
    	
      <tr>
        <th>备注</th>
        <td colspan="7"><input type="text" name="remarks" style="width:80%"/></td>
      </tr>
    </table>
    <input type="hidden" name="valid" value="1" />
  </form>
</div>
