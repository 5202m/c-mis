<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/visitorList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:170px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="visitor_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">手机号码</th>
          <td width="21%"><input type="text" name="mobile"></td>
          <th>所属房间</th>
          <td>
	          <select name="roomId" id="visitorGroupId" style="width:160px;">
	              <c:forEach var="row" items="${chatGroupList}">
	                 <c:if test="${row.groupType=='studio' or row.id=='studio'}">
	                   <option value="${row.id}<c:if test="${empty row.groupType}">,</c:if>">${row.name}</option>
	                 </c:if>
	              </c:forEach>
	          </select>
          </td>
        </tr>
        <tr>
          <th>在线状态</th>
          <td>
	          <select name="onlineStatus" style="width:160px;">
          		<option value="">--请选择--</option>
          		<option value="1">在线</option>
          		<option value="0">下线</option>
	          </select>
          </td>
          <th>是否登录</th>
          <td>
	          <select name="loginStatus" style="width:160px;">
          		<option value="">--请选择--</option>
          		<option value="1">已登录</option>
          		<option value="0">未登录</option>
	          </select>
          </td>
        </tr>
        <tr>
          <td colspan="6" align="right">&nbsp;&nbsp;
			  <a href="#" class="easyui-linkbutton" id="visitor_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /></a> &nbsp;&nbsp; 
			  <a href="#" class="easyui-linkbutton" id="visitor_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="chatVisitor_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="visitor_datagrid_toolbar" style="display:none;">
      <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"  onclick="chatVisitor.refresh()"><spring:message code="common.buttons.refresh" /></a>
      <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatVisitor.batchDel();">删除</a>
      <a class="easyui-linkbutton export" data-options="plain:true,iconCls:'ope-export',disabled:false"  onclick="chatVisitor.exportRecord();">导出记录</a>
 </div> 
 
</div>