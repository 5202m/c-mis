<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/visitor/visitorList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:170px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
			<form class="yxForm" id="visitor_queryForm">
				<table class="tableForm_L" style="margin-top: 3px" width="99%" heigth="auto" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<th width="10%">手机号码</th>
						<td width="23%"><input type="text" name="mobile"></td>
						<th width="10%">所属房间</th>
						<td width="23%">
							<select name="roomId" id="visitorGroupId" style="width: 160px;">
								<c:forEach var="row" items="${chatGroupList}">
									<option value="${row.id}<c:if test="${empty row.groupType}">,</c:if>">${row.name}</option>
								</c:forEach>
							</select>
						</td>
						<th width="10%">在线状态</th>
						<td width="24%">
							<select name="onlineStatus" style="width: 160px;">
								<option value="">--请选择--</option>
								<option value="1">在线</option>
								<option value="0">下线</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>是否登录</th>
						<td>
							<select name="loginStatus" style="width: 160px;">
								<option value="">--请选择--</option>
								<option value="1">已登录</option>
								<option value="0">未登录</option>
								<option value="2">从未登录</option>
							</select>
						</td>
						<th>上线时间</th>
						<td>
							<input name="onlineDateStart" id="visitor_onlineDateStart" class="Wdate" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'visitor_onlineDateEnd\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 45%" />
							 — 
							 <input name="onlineDateEnd" id="visitor_onlineDateEnd" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'visitor_onlineDateStart\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 45%" />
						</td>
						
						<th>昵称</th>
						<td>
							<input type="text" name="nickname">
						</td>
						
					</tr>
					<tr>
						<th>客户设备</th>
						<td>
							<select name="userAgent" style="width: 160px;">
								<option value="">--请选择--</option>
								<option value="pc">PC</option>
								<option value="mobile">手机</option>
							</select>
						</td>
						<th>客户来源</th>
						<td>
							<select name="platform" id="visitorPlatform" style="width: 160px;">
								<option value="">--请选择--</option>
								<option value="studio">直播间</option>
								<option value="pc">PCUI</option>
								<option value="webui">WEBUI</option>
								<option value="app">APP</option>
							</select>
						</td>
						<th>登录时间</th>
						<td>
							<input name="loginDateStart" id="visitor_loginDateStart" class="Wdate" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'visitor_loginDateEnd\')}'})" style="width: 45%" />
							 — 
							 <input name="loginDateEnd" id="visitor_loginDateEnd" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'visitor_loginDateStart\')}'})" style="width: 45%" />
						</td>
					</tr>
					<tr>
						<th>用户级别</th>
						<td colspan="5">
							<select name="clientGroup" id="clientGroup">
								<option value="">--请选择--</option>
								<option value="vip">vip</option>
								<option value="active">真实激活</option>
								<option value="notactive">真实未激活</option>
								<option value="simulate">模拟</option>
								<option value="register">注册</option>
								<option value="visitor">游客</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="6" align="right">
							&nbsp;&nbsp; <a href="#" class="easyui-linkbutton" id="visitor_queryForm_search" data-options="iconCls:'ope-search'"><spring:message code="common.buttons.search" /></a> 
							&nbsp;&nbsp; <a href="#" class="easyui-linkbutton" id="visitor_queryForm_reset"	data-options="iconCls:'ope-empty'"><spring:message code="common.buttons.clear" /> </a>
						</td>
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
