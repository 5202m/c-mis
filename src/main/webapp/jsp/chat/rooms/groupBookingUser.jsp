<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<div class="easyui-layout" data-options="fit:true" style="padding: 5px; overflow: hidden;">
	<form id="groupTrainRegis_form" method="post">
	<input type="hidden" name="chatGroupId" id = "chatGroupId" value="${chatGroup.id }">
	<!-- center -->
	<div data-options="region:'center',border:false" style="padding: 4px">
		<div class="easyui-layout" data-options="fit:true,border:false" style="width: 400px; height: 200px;">
			<!-- west -->
			<div data-options="region:'west',border:false" style='width: 230px'>
				<div class="easyui-panel" data-options="fit:true,title:'未授权用户'">
					<select multiple="multiple" ondblclick="yxui.left2right(this);" style="margin: 1px; width: 98%; height: 99%" class="unAuthTraninClientSelect" name="unAuthTraninClient">
						<c:forEach var="unAuthTraninClient" items="${unAuthTraninClientList }">
							<option value="${unAuthTraninClient.clientId}" >${unAuthTraninClient.nickname}【${unAuthTraninClient.nickname}】</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<!-- center -->
			<div data-options="region:'center',border:false">
				<div data-options="region:'center',border:false">
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="controlList">
						<tbody>
							<tr>
								<td>
									<input type="button" class="button" value="&gt;&nbsp;&gt;&nbsp;" style="width: 50px" onclick="yxui.leftall2right(this);" title='<spring:message code="role.assginuser.allselected" />' />
									<!-- 全部选中 -->
								</td>
							</tr>
							<tr>
								<td>
									<input type="button" class="button" value="&gt;&nbsp;" style="width: 50px" onclick="yxui.left2right(this);" title='<spring:message code="role.assginuser.selected" />' />
									<!-- 选中 -->
								</td>
							</tr>
							<tr>
								<td>
									<input type="button" class="button" value="&lt;&nbsp;" style="width: 50px" onclick="yxui.right2left(this);" title='<spring:message code="role.assginuser.removed" />' />
									<!-- 移除 -->
								</td>
							</tr>
							<tr>
								<td>
									<input type="button" class="button" value="&lt;&nbsp;&lt;&nbsp;" style="width: 50px" onclick="yxui.rightall2left(this);" title='<spring:message code="role.assginuser.allremoved" />' />
									<!-- 全部移除 -->
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- east -->
			<div data-options="region:'east',border:false" style='width: 230px'>
				<div class="easyui-panel" data-options="fit:true,title:'已授权用户'">
					<select multiple="multiple" class="authTraninClientSelect" id="groupTrainRegis_authUsers" name="authTraninClient" ondblclick="yxui.right2left(this);"
						style="margin: 1px; width: 98%; height: 99%">
						<c:forEach var="authTraninClient" items="${authTraninClientList }">
							<option value="${authTraninClient.clientId}" >${authTraninClient.nickname}【${authTraninClient.nickname}】</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
	</div>
	</form>
</div>