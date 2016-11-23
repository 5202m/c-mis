<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/infoManage/zxData.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
	<!-- notrh -->
	<div data-options="region:'north',border:false" style="height: 140px;">
		<div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
			<form class="yxForm" id="zxData_queryForm">
				<table class="tableForm_L" style="margin-top: 3px" width="99%" heigth="auto" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<th width="10%">指标编号</th>
						<td width="23%">
							<input type="text" name="basicIndexId" style="width: 160px" />
						</td>
						<th width="10%">名称</th>
						<td width="23%">
							<input type="text" name="name" style="width: 160px" />
						</td>
						<th width="10%">国家</th>
						<td>
							<input type="text" name="country" style="width: 160px" />
						</td>
					</tr>
					<tr>
						<th width="10%">产品类型</th>
						<td>
							<select id="zxData_dataType" name="dataType" style="width: 155px;">
								<option value=""><spring:message code="common.pleaseselect" /></option>
								<option value="0">所有</option>
								<option value="1">外汇</option>
								<option value="2">贵金属</option>
							</select>
						</td>
						<th>数据状态</th>
						<td>
							<select id="zxData_valid" name="valid" style="width: 155px;">
								<option value=""><spring:message code="common.pleaseselect" /></option>
								<option value="1" selected="selected">有效</option>
								<option value="0">无效</option>
								<option value="2">接口删除</option>
							</select>
						</td>
						<th>发布时间</th>
						<td>
							<input name="dateStart" id="zxData_dateStart" class="Wdate" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'zxData_dateEnd\')}'})" style="width: 160px" />
							—
							<input name="dateEnd" id="zxData_dateEnd" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'zxData_dateStart\')}'})" style="width: 160px" />
						</td>
					</tr>
					<tr>
						<td colspan="6" align="right">
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="zxData_queryForm_search" data-options="iconCls:'ope-search'">
								<spring:message code="common.buttons.search" />
								<!-- 查询 -->
							</a>
							&nbsp;&nbsp;
							<a href="#" class="easyui-linkbutton" id="zxData_queryForm_reset" data-options="iconCls:'ope-empty'">
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
		<div id="zxData_datagrid" style="display: none"></div>
	</div>

	<!-- datagrid-toolbar -->
	<div id="zxData_datagrid_toolbar" style="display: none;">
		<a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false" onclick="ZxData.refresh();">
			<spring:message code="common.buttons.refresh" />
			<!-- 刷新 -->
		</a>
	</div>

	<!-- datagrid-操作按钮 -->
	<div id="zxData_datagrid_rowOperation" style="display: none;">
		<input type="hidden" value="">
		<a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="ZxData.edit(this)"><spring:message code="common.buttons.edit" /><!-- 修改 --></a>
		<a class="easyui-linkbutton review" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="ZxData.review(this)">点评</a>
	  	<a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="ZxData.del(this)"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
	</div>

	<!-- 点评数据 -->
	<div id="zxDataView_win" class="easyui-dialog" closed="true">
		<div data-options="region:'center'" id="zxDataView_panel">
			<div>
				<div data-options="region:'center',title:'点评列表',iconCls:'pag-list'">
					<div id="zxDataReview_datagrid" style="display: none"></div>
					<div id="zxDataReview_page" class="datagrid-pager pagination"></div>
				</div>
				<form id="zxDataReview_Form" class="yxForm" method="post">
					<input type="hidden" name="dataId" value="" />
					<input type="hidden" name="id" value="" />
					<input type="hidden" name="userId" />
					<input type="hidden" name="name" />
					<input type="hidden" name="avatar" />
					<table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
						<tr>
							<th>分析师</th>
							<td><select id="authorAvatar" name="authorAvatar" style="width: 180px;"></select></td>
						</tr>
						<tr>
							<th width="15%">点评内容</th>
							<td>
								<textarea name="comment" id="comment" style="width:500px;height:100px;"></textarea>
								<input type="button" value="重置" onclick="ZxData.cancelEdit();" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>

	<!-- view_datagrid-操作按钮 -->
	<div id="zxDataReview_datagrid_rowOperation" style="display: none;">
		<input type="hidden" value="" />
		<a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false" onclick="ZxData.editReview(this);">修改</a>
		<a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false" onclick="ZxData.delReview(this);">删除</a>
	</div>

</div>