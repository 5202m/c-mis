<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/highchats/highcharts.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/highchats/modules/exporting.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/highchats/highcharts-3d.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/visitor/visitorRepD.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
	<!-- notrh -->
	<div data-options="region:'north',border:false" style="height: 100px;">
		<div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
			<form class="yxForm" id="visitor_repDForm">
				<table class="tableForm_L" style="margin-top: 3px" width="99%" heigth="auto" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<th width="10%">所属房间</th>
						<td width="30%">
							<select name="groupId" style="width: 160px;">
								<c:forEach var="row" items="${chatGroupList}">
									<c:if test="${row.groupType=='studio' or row.id=='studio' or row.groupType=='fxstudio' or row.id=='fxstudio'}"></c:if>
										<option value="${row.id}<c:if test="${empty row.groupType}">,</c:if>">${row.name}</option>
									
								</c:forEach>
							</select>
						</td>
						<th width="10%">数据日期</th>
						<th>
							<input name="dateDateStart" id="visitor_repDDateStart" class="Wdate" value="${dayBefore6 }" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'visitor_repDDateEnd\')}',isShowClear:false})" style="width: 160px" />
							—
							<input name="dateDateEnd" id="visitor_repDDateEnd" class="Wdate" value="${dayBefore1 }" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'visitor_repDDateStart\')}',maxDate:'${dayBefore1 }',isShowClear:false})" style="width: 160px" />
						</th>
					</tr>
					<tr>
						<td colspan="6" align="right">
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="visitor_repDForm_search" data-options="iconCls:'ope-search'">
								<spring:message code="common.buttons.search" />
							</a>
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="visitor_repDForm_reset" data-options="iconCls:'ope-empty'">
								<spring:message code="common.buttons.clear" />
							</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<!-- datagrid -->
	<div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
		<div id="chatVisitorRepD_datagrid" style="display: none"></div>
	</div>
	
	<!-- 图表 -->
	<div data-options="region:'south',title:'图表',iconCls:'pag-list'" style="min-height:350px;">
		<div id="visitor_repDChart" style="height:90%; display: none;"></div>
	</div>

	<!-- datagrid-toolbar -->
	<div id="visitorRepD_datagrid_toolbar" style="display: none;">
		<a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false" onclick="chatVisitorRepD.refresh()">
			<spring:message code="common.buttons.refresh" />
		</a>
		<a class="easyui-linkbutton export" data-options="plain:true,iconCls:'ope-export',disabled:false" onclick="chatVisitorRepD.exportRecord();">导出记录</a>
	</div>
	
	<!-- chart -->
</div>
