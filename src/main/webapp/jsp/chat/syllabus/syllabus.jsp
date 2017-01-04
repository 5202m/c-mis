<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/syllabus/syllabus.js" charset="UTF-8"></script>
<style>
	#panel_viewSyllabus div.syllabus_title{text-align:center; font-size:20px; font-weight:bolder; margin-top: 20px;}
	#panel_viewSyllabus .syllabus .sy_panel{width:600px; margin: 0 auto; background-color: rgba(219,219,219,.9);}
	#panel_viewSyllabus .syllabus .sy_panel .sy_nav a{float: left; display: block; color: #757575; font-size: 12pt; font-weight: bold; padding: 6px 0px 6px 0px; text-align: center; text-decoration: none; lin}
	#panel_viewSyllabus .syllabus .sy_panel .sy_nav a span{margin-top: 5px; font-size:12px; font-weight: normal;}
	#panel_viewSyllabus .syllabus .sy_panel .sy_nav a.active{ color: #a17d20; background: #f6f6f6;}
	#panel_viewSyllabus .syllabus .sy_panel .sy_cont{padding-top: 31px;}
	#panel_viewSyllabus .syllabus .sy_panel .sy_cont div{padding-top: 5px; background-color: #f6f6f6;}
	#panel_viewSyllabus .syllabus .sy_panel table{width: 600px;}
	#panel_viewSyllabus .syllabus .sy_panel table tbody{display: none;}
	#panel_viewSyllabus .syllabus .sy_panel table tr.prev{color: #808080;}
	#panel_viewSyllabus .syllabus .sy_panel table tr.ing{color: #3181c6;}
	#panel_viewSyllabus .syllabus > table{margin-top: 5px; width: auto;}
	#panel_viewSyllabus .syllabus > table tr{height: 66px;}
	#panel_viewSyllabus .syllabus > table tr th{ width: 110px;}
	#panel_viewSyllabus .syllabus > table tr td{ width: 110px;}
	#panel_viewSyllabus table tr th{font-weight:bold; vertical-align: middle; text-align: center; font-size: 14px;}
	#panel_viewSyllabus table tr td{vertical-align: middle; text-align: center; font-size: 14px;}
	#panel_viewSyllabus table tr th span{font-size: 80%; font-weight: normal;}
	#panel_viewSyllabus table tr td.prev{color: #808080;}
	#panel_viewSyllabus table tr td.ing{color: #3181c6;}
	#panel_viewSyllabus table tr td.next{}
</style>
<div class="easyui-layout" data-options="fit:true">
	<!-- notrh -->
	<div data-options="region:'north',border:false" style="height: 120px;">
		<div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
			<form class="yxForm" id="syllabus_queryForm">
				<table class="tableForm_L" style="margin-top: 3px" width="99%" heigth="auto" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<th width="10%">房间组别</th>
						<td width="40%">
							<select name="groupType" id="syllabus_groupType_select" style="width: 160px;">
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
						<th width="10%">房间</th>
						<td width="40%">
							<select name="groupId" id="syllabus_groupId_select" style="width: 160px;">
								<option value="">--请选择--</option>
								<c:forEach var="row" items="${chatGroupList}">
									<c:if test="${not empty row.id}">
										<option value="${row.id }" t="${row.groupType }">
											${row.name}
										</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="6" align="right">
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="syllabus_queryForm_search" data-options="iconCls:'ope-search'">
								<spring:message code="common.buttons.search" />
								<!-- 查询 -->
							</a>
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="syllabus_queryForm_reset" data-options="iconCls:'ope-empty'">
								<spring:message code="common.buttons.clear" />
								<!-- 清空 -->
							</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<!-- datagrid -->
	<div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
		<div id="syllabus_datagrid" style="display: none"></div>
	</div>

	<!-- datagrid-操作按钮 -->
	<div id="syllabus_datagrid_rowOperation" style="display: none;">
		<input type="hidden" value="">
		<a class="easyui-linkbutton view" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="Syllabus.view(this)"><spring:message code="common.buttons.view" /><!-- 预览 --></a>
		<a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="Syllabus.edit(this)"><spring:message code="common.buttons.edit" /><!-- 修改 --></a>
	  	<a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="Syllabus.del(this)"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
	</div>
	
	<!-- datagrid-toolbar -->
	<div id="syllabus_datagrid_toolbar" style="display: none;">
		<a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="Syllabus.edit();">
			<spring:message code="common.buttons.add" /><!-- 新增 -->
		</a> 
		<a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false" onclick="Syllabus.refresh();">
			<spring:message code="common.buttons.refresh" />
			<!-- 刷新 -->
		</a>
	</div>

	<div id="viewSyllabusWin" class="easyui-dialog" closed="true">
		<div data-options="region:'center'" id="panel_viewSyllabus">
			<div class="syllabus_title">
				<span></span>
				课程表
			</div>
			<div class="syllabus"></div>
		</div>
	</div>
</div>