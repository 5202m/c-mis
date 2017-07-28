<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<div class="easyui-layout" data-options="fit:true" style="padding: 5px; overflow: hidden;">
	<form id="userClientGroupSet_form" method="post">
		<input type="hidden" name="groupType" value="${groupType}" />
		<input type="hidden" name="type" value="${type}" />
		<!-- center -->
		<table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
			<tr>
				<th>
					<label><input type="radio" name="userGroup" value="3" />用户等级</label>
					<select name="clientGroup" id="clientGroup">
						<option value="">--请选择--</option>
						<c:forEach var="row" items="${clientGroupList}">
							<c:if test="${row.clientGroupId != 'visitor' && row.clientGroupId != 'vip'}">
								<option value="${row.clientGroupId}" t="${row.groupType}">${row.name}</option>
							</c:if>
						</c:forEach>
					</select>
					<label><input type="radio" name="userGroup" value="2" />VIP用户</label>
					<select name="vipUser" id="vipUser">
						<option value="">--请选择--</option>
						<option value="true">是</option>
						<option value="false">否</option>
					</select>
				</th>
			</tr>
			<tr>
				<th>
					填入待修改级别的手机号码，用逗号隔开
				</th>
			</tr>
			<tr>
				<td>
					<textarea id="mobiles" name="mobiles" rows="20" style="margin-top: 5px; width: 100%"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>