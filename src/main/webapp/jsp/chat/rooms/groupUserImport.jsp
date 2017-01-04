<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<div class="easyui-layout" data-options="fit:true" style="padding: 5px; overflow: hidden;">
	<form id="groupUserImport_form" method="post">
	<input type="hidden" name="groupId" value="${groupId }">
		<!-- center -->
		<table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
			<tr>
				<th>
					导入客户（填入待导入的所有客户手机号码，用逗号隔开）
				</th>
			</tr>
			<tr>
				<td>
					<textarea name="mobiles" rows="20" style="margin-top: 5px; width: 100%"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>