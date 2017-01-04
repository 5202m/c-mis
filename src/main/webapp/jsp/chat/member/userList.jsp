<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/member/userList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true" id="chatUserListPanel">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:170px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="chatUser_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">手机号码</th>
          <td width="21%"><input type="text" name="mobilePhone"></td>
          <th width="10%">账号</th>
          <td width="21%"><input type="text" name="loginPlatform.chatUserGroup[0].accountNo"></td>
          <th width="10%">昵称</th>
          <td width="28%"><input type="text" name="loginPlatform.chatUserGroup[0].nickname"></td>
        </tr>
        <tr>
          <th>所属房间</th>
          <td>
	          <select name="loginPlatform.chatUserGroup[0].id" id="chatUserGroupId" style="width:160px;">
	              <c:forEach var="row" items="${chatGroupList}">
	                 <option value="${row.id}<c:if test="${empty row.groupType}">,</c:if>">${row.name}</option>
	              </c:forEach>
	          </select>
          </td>
          <th>在线状态</th>
          <td>
	          <select name="loginPlatform.chatUserGroup[0].rooms[0].onlineStatus" id="chatUserOnlineStatus" style="width:160px;">
          		<option value="">--请选择--</option>
          		<option value="1">在线</option>
          		<option value="0">下线</option>
	          </select>
          </td>
          <th>上线时间</th>
          <td> 
          	从&nbsp; <input name="onlineStartDate" id="onlineStartDate" class="Wdate"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'onlineEndDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
             &nbsp;&nbsp;&nbsp;&nbsp; 到&nbsp;<input name="onlineEndDate" id="onlineEndDate" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'onlineStartDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
		  </td>
        </tr>
        <tr>
          <th>价值用户</th>
          <td>
	          <select name="loginPlatform.chatUserGroup[0].valueUser" style="width:160px;">
          		<option value="">--请选择--</option>
          		<option value="1">是</option>
          		<option value="0">否</option>
	          </select>
          </td>
          <th>用户级别</th>
          <td>
	          <select name="loginPlatform.chatUserGroup[0].clientGroup" id="userList_clientGroup" style="width:160px;">
          		<option value="">--请选择--</option>
              	<c:forEach var="row" items="${clientGroupList}">
              		<c:if test="${row.clientGroupId != 'visitor' }">
              			<option value="${row.clientGroupId}" t="${row.groupType}">${row.name}</option>
              		</c:if>
              	</c:forEach>
	          </select>
          </td>
          <th>注册时间</th>
          <td> 
          	从&nbsp; <input name="userCreateDateStart" id="userCreateDateStart" class="Wdate"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'userCreateDateEnd\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
             &nbsp;&nbsp;&nbsp;&nbsp; 到&nbsp;<input name="userCreateDateEnd" id="userCreateDateEnd" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'userCreateDateStart\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
		  </td>
        </tr>
        <tr>
          <th>用户是否禁言</th>
          <td>
	          <select name="loginPlatform.chatUserGroup[0].gagStatus" style="width:160px;">
          		<option value="">--请选择--</option>
          		<option value="1">是</option>
          		<option value="0">否</option>
	          </select>
          </td>
          <th>房间是否禁言</th>
          <td colspan="3">
	          <select name="loginPlatform.chatUserGroup[0].rooms[0].gagStatus" id="chatUser_gagStatus" style="width:160px;">
          		<option value="">--请选择--</option>
          		<option value="1">是</option>
          		<option value="0">否</option>
	          </select>
          </td>
        </tr>
        <tr>
          <td colspan="6" align="right">&nbsp;&nbsp;
			  <a href="#" class="easyui-linkbutton" id="chatUser_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /></a> &nbsp;&nbsp; 
			  <a href="#" class="easyui-linkbutton" id="chatUser_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="chatUser_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="chatUser_datagrid_toolbar" style="display:none;">
      <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"  onclick="chatUser.refresh()"><spring:message code="common.buttons.refresh" /></a>
      <a class="easyui-linkbutton export" data-options="plain:true,iconCls:'ope-export',disabled:false"  onclick="chatUser.exportRecord();">导出记录</a>
 </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="chatUser_datagrid_rowOperation" style="display:none;">
	  <!-- <a class="easyui-linkbutton setGagTime" data-options="plain:true,iconCls:'ope-save',disabled:false"  onclick="chatUser.setUserGag(this)">禁言</a> -->
      <a class="easyui-linkbutton userSetting" data-options="plain:true,iconCls:'ope-save',disabled:false" onclick="chatUser.userUnbind(this)">解除绑定</a>
      <a class="easyui-linkbutton userSetting" data-options="plain:true,iconCls:'ope-save',disabled:false" t="1" onclick="chatUser.userSetting(this)">价值用户</a>
      <a class="easyui-linkbutton userSetting" data-options="plain:true,iconCls:'ope-save',disabled:false" t="2" onclick="chatUser.userSetting(this)">VIP用户</a>
      <a class="easyui-linkbutton userSetting" data-options="plain:true,iconCls:'ope-save',disabled:false" t="3" onclick="chatUser.userSetting(this)">用户级别</a>
      <a class="easyui-linkbutton setGagTime" data-options="plain:true,iconCls:'ope-save',disabled:false" onclick="chatUser.setUserGag(this)">禁言</a>
  </div>
 
</div>
