<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/points/chatPointsInfo.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
	<!-- notrh -->
	<div data-options="region:'north',border:false" style="height: 140px;">
		<div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
			<form class="yxForm" id="chatPointsInfo_queryForm">
				<table class="tableForm_L" style="margin-top: 3px" width="99%" heigth="auto" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<th width="10%">组别</th>
						<td width="23%">
							<t:dictSelect id="chatPointsInfo_groupType" field="groupType" isEdit="true" defaultVal="" isShowPleaseSelected="false"  dataList="${groupList}"/>
						</td>
						<th width="10%">手机号码</th>
						<td width="23%">
							<input type="text" name="userId" style="width: 160px" />
						</td>
						<th width="10%">积分范围</th>
						<td width="24%">
							<input type="text" id="chatPointsInfo_pointsStart" name="pointsStart" size="10" />
							——
							<input type="text" id="chatPointsInfo_pointsEnd" name="pointsEnd" size="10" />
						</td>
					</tr>
					<tr>
						<th width="10%">类别</th>
						<td width="23%">
							<t:dictSelect id="chatPointsInfo_type" field="type" isEdit="true" defaultVal="" isShowPleaseSelected="true"  dataList="${type}"/>
						</td>
						<th width="10%">项目</th>
						<td width="23%">
							<t:dictSelect id="chatPointsInfo_item" field="journal[0].item" isEdit="true" defaultVal="" isShowPleaseSelected="true"  dataList="${item}"/>
						</td>
						<th>积分时间</th>
						<td>
							<input name="timeStart" id="chatPointsInfo_timeStart" class="Wdate" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'chatPointsInfo_timeEnd\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 160px" />
							—
							<input name="timeEnd" id="chatPointsInfo_timeEnd" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'chatPointsInfo_timeStart\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 160px" />
						</td>
					</tr>
					<tr>
						<td colspan="6" align="right">
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="chatPointsInfo_queryForm_search" data-options="iconCls:'ope-search'">
								<spring:message code="common.buttons.search" />
								<!-- 查询 -->
							</a>
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="chatPointsInfo_queryForm_reset" data-options="iconCls:'ope-empty'">
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
		<div id="chatPointsInfo_datagrid" style="display: none"></div>
	</div>

	<!-- datagrid-toolbar -->
	<div id="chatPointsInfo_datagrid_toolbar" style="display: none;">
		<a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="chatPointsInfo.add();">
			<spring:message code="common.buttons.add" /><!-- 新增 -->
		</a> 
		<a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false" onclick="chatPointsInfo.refresh();">
			<spring:message code="common.buttons.refresh" />
			<!-- 刷新 -->
		</a>
		<a class="easyui-linkbutton export" data-options="plain:true,iconCls:'ope-export',disabled:false"  onclick="chatPointsInfo.exportRecordPage('collect');">导出汇总记录</a>
		<a class="easyui-linkbutton export" data-options="plain:true,iconCls:'ope-export',disabled:false"  onclick="chatPointsInfo.exportRecordPage('details');">导出明细记录</a>
	</div>

	<!-- datagrid-操作按钮 -->
	<div id="chatPointsInfo_datagrid_rowOperation" style="display: none;">
		<input type="hidden" value="">
		<a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatPointsInfo.edit(this)">调整</a>
		<a class="easyui-linkbutton view" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="chatPointsInfo.view(this)">明细</a>
	  	<a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatPointsInfo.del(this)"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
	</div>
	
	<!-- 积分详情 -->
	<div id="chatPointsInfoView_win" class="easyui-dialog" closed="true">
		<div data-options="region:'center'" id="chatPointsInfoView_panel">
			<div>
				<input type="hidden" id="chatPointsInfoView_pointsId">
				<table class="tableForm_L" id="chatPointsInfoView_table" border="0" cellspacing="1" cellpadding="0">
					<tr>
						<th width="15%">组别</th>
						<td width="35%" field="groupType"></td>
						<th width="15%">用户编号</th>
						<td width="35%" field="userId"></td>
					</tr>
					<tr>
						<th width="15%">总积分</th>
						<td width="35%" field="pointsGlobal"></td>
						<th width="15%">有效积分</th>
						<td width="35%" field="points"></td>
					</tr>
					<tr>
						<th width="15%">备注</th>
						<td colspan="3" field="remark"></td>
					</tr>
				</table>
				<div data-options="region:'center',title:'积分流水',iconCls:'pag-list'">
					<div id="chatPointsInfoView_datagrid" style="display: none"></div>
					<div id="chatPointsInfoView_page" class="datagrid-pager pagination"></div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- view_datagrid-操作按钮 -->
	<div id="chatPointsInfoView_datagrid_rowOperation" style="display: none;">
		<input type="hidden" value="">
		<a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false" onclick="chatPointsInfo.reversal(this)">冲正</a>
	</div>

	<!-- 积分汇总总条数分页 -->
	<div id="chatPointsExportPageView_win" class="easyui-dialog" closed="true">
		<div data-options="region:'center'" id="chatPointsExportPageView_panel">
			<div>
				<div data-options="region:'center',title:'积分流水分页导出',iconCls:'pag-list'">
					<div id="chatPointsExportPageView_datagrid" style="display: none"></div>
					<div id="chatPointsExportPageView_page" class="datagrid-pager pagination"></div>
				</div>
			</div>
		</div>
	</div>
	<!-- exportpage_view_datagrid-操作按钮 -->
	<div id="chatPointsExportPageView_datagrid_rowOperation" style="display: none;">
		<input type="hidden" value="" />
		<a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false" onclick="chatPointsInfo.exportRecord(this);">导出</a>
	</div>
</div>