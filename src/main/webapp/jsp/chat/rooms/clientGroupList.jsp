<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/rooms/clientGroupList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:90px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="chatClientGroup_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">类别</th>
          <td width="15%">
             <t:dictSelect  selectClass="width:150px;" defaultVal="studio" field="groupType" isEdit="true" isShowPleaseSelected="false"  dataList="${groupTypeList}"/>
          </td>
          <th width="10%">编号</th>
          <td width="15%"><input type="text" name="clientGroupId" /></td>
          <th width="10%">名称</th>
          <td width="15%"><input type="text" name="name" /></td>
          <th width="10%">默认房间</th>
          <td width="15%">
             <select name="defChatGroupId" id="chatGroupId" style="width:250px;">
	              <option value="">--请选择--</option>
	              <c:forEach var="row" items="${chatGroupList}">
	                 <option value="${row.id}">
	                 	 ${row.name}
	      				 <c:forEach var="currGroupType" items="${groupTypeList}">
	      					<c:if test="${currGroupType.code == row.groupType}">
	      						【${currGroupType.nameCN }】
	      					</c:if>
	      				 </c:forEach>
	                 </option>
	              </c:forEach>
	         </select>
          </td>
        </tr>
        <tr>
          <td colspan="8" align="right">&nbsp;&nbsp;
			  <a href="#" class="easyui-linkbutton" id="chatClientGroup_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /></a> &nbsp;&nbsp; 
			  <a href="#" class="easyui-linkbutton" id="chatClientGroup_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="chatClientGroup_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="chatClientGroup_datagrid_toolbar" style="display:none;">
    <a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="chatClientGroup.add();">新增</a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"  onclick="chatClientGroup.refresh()"><spring:message code="common.buttons.refresh" /></a>
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatClientGroup.batchDel();">删除</a>
  </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="chatClientGroup_datagrid_rowOperation" style="display:none;">
	  <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatClientGroup.edit(this.id)"><spring:message code="common.buttons.edit" /></a>
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatClientGroup.del(this.id)"><spring:message code="common.buttons.delete" /></a>
  </div>
 
</div>
