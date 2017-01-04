<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/system/syslog/syslog.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:120px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="system_log_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%"><spring:message code="user.no" /><!-- 账号 --></th>
          <td width="23%"><input type="text" name="userNo" id="userNo" style="width:160px"/></td>
          <th width="10%"><spring:message code="syslog.operatetype" /><!-- 操作类型 --></th>
          <td width="23%">
          <select name="operateType" id="operateType" style="width:160px">
            <option value="1">登陆</option>
            <option value="2">退出</option>
            <option value="3">新增</option>
            <option value="4">删除</option>
            <option value="5">更新</option>
            <option value="6">其他</option>
            <option value="7">审批</option>
            <option value="8">取消审批</option>
            <option value="9">导出记录</option>
          </select>
          </td>
        </tr>
         <tr>
          <th width="10%"><spring:message code="syslog.operatedate" /><!-- 操作日期 --></th>
          <td width="23%">
              <input name="startDate" id="startDate" class="Wdate"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" style="width:160px" />
          </td>
          <th width="10%"><spring:message code="syslog.to" /><!-- 到 --></th>
          <td width="23%">
          	  <input name="endDate" id="endDate" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'})" style="width:160px" />
          </td>
        </tr>
        <tr>
          <td colspan="6" align="right">&nbsp;&nbsp;
			<a href="#" class="easyui-linkbutton" id="system_log_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /> </a> &nbsp;&nbsp;
			<a href="#" class="easyui-linkbutton" id="system_log_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="system_log_datagrid" style="display:none"></div>
  </div>
  
</div>
