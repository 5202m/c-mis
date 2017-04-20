<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/subscribe/subscribeType.js" charset="UTF-8"></script>
<script type="text/javascript"></script>
<div id="editWindow" class="easyui-dialog" closed="true"></div>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:120px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="subscribeType_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
        	<th width="10%">服务类型</th>
        	<td><input type="text" name="name" id="name" /></td>        	
          <th width="10%">分析师</th>
          <td>
            <select class="easyui-combotree" name="analysts" id="subscribeTypeAnalystsSelectId" style="width:250px;" data-options="cascadeCheck:false" multiple>
	        </select>
          </td>
        </tr>
        <tr>
          <th width="10%">房间组别</th>
			<td>
				<select name="groupType" id="subscribeType_groupType_select" style="width: 160px;">
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
        <th width="10%">状态</th>
        <td>
        	<select name="status" id="subscribeType_status_select">
   				<option value="">--请选择--</option>
   				<option value="1">有效</option>
   				<option value="0">无效</option>
   			</select>
        </td>
        </tr>
        <tr>
          	<td colspan="4" align="right">&nbsp;&nbsp;
	        	<a href="javascript:void(0);" class="easyui-linkbutton" id="subscribeType_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /><!-- 查询 --> </a> 
	        	&nbsp;&nbsp; <a href="javascript:void(0);" class="easyui-linkbutton" id="subscribeType_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /><!-- 清空 --> </a>
        	</td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="subscribeType_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="subscribeType_datagrid_toolbar">
    <a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="chatSubscribeType.add();"><spring:message code="common.buttons.add" /><!-- 新增 --></a> 
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"    onclick="chatSubscribeType.batchDel();"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"   onclick="chatSubscribeType.refresh();"><spring:message code="common.buttons.refresh" /><!-- 刷新 --></a>
    <!--a class="easyui-linkbutton setStatus" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatSubscribeType.setStatus(1)">审核通过 </a>
    <a class="easyui-linkbutton setStatus" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatSubscribeType.setStatus(-1)">审核不通过 </a--> 
  </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="subscribeType_datagrid_rowOperation">
	  <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatSubscribeType.edit(this.id)"><spring:message code="common.buttons.edit" /><!-- 修改 --></a>
	  <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false" onclick="chatSubscribeType.view(this.id)"><spring:message code="common.buttons.view" /><!-- 查看 --></a>
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatSubscribeType.del(this.id)"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
  </div>
  
</div>