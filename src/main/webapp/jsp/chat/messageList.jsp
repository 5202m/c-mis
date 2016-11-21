<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/messageList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:195px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="chatMessage_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">手机号码</th>
          <td width="40%"><input type="text" name="mobilePhone"/></td>
          <th width="10%">昵称</th>
          <td width="40%"><input type="text" name="nickname"/></td>
        </tr>
        <tr>
          <th width="10%">账号</th>
          <td width="40%"><input type="text" name="accountNo"/></td>
          <th width="10%">用户类型</th>
          <td width="40%">
	          <select name="clientGroup" id="chatMessageUserType" style="width:160px;">
          		<option value="">--请选择--</option>
          		<option value="0">普通会员</option>
          		<c:forEach var="row" items="${clientGroupList}">
              		<c:if test="${row.clientGroupId != 'visitor' }">
              			<option value="${row.clientGroupId}" t="${row.groupType}">&nbsp;&nbsp;&nbsp;&nbsp;${row.name}</option>
              		</c:if>
              	</c:forEach>
          		<option value="-1">&nbsp;&nbsp;&nbsp;&nbsp;游客</option>
          		<option value="1">系统管理员</option>
          		<option value="2">分析师</option>
          		<option value="3">客服</option>
	          </select>
          </td>
        </tr>
         <tr>
          <th width="10%">所属房间</th>
          <td width="40%">
	          <select name="groupId" id="chatMessageGroupId" style="width:160px;">
	              <c:forEach var="row" items="${chatGroupList}">
	                 <option value="${row.id}<c:if test="${empty row.groupType}">,</c:if>">${row.name}</option>
	              </c:forEach>
	          </select>
          </td>
          <th width="10%">信息类型</th>
          <td width="40%"> 
             <select name="content.msgType" id="chatMessageMsgType" style="width:160px;">
                  <option value="">--请选择--</option>
	              <option value="text">文本</option>
	              <option value="img">图片</option>
             </select>
          </td>
        </tr>
        <tr>
          <!-- <th width="10%">审核状态</th>
          <td width="40%">
	          <select name="status" id="chatMessageStatusId"  style="width:160px;">
          		<option value="1">通过</option>
          		<option value="0">待审批</option>
          		<option value="2">拒绝</option>
	          </select>
          </td> -->
          <th width="10%">聊天方式</th>
          <td width="40%">
	          <select name="talkStyle" style="width:160px;">
	            <option value="">--请选择--</option>
          		<option value="0">公聊</option>
          		<option value="1">私聊</option>
          		<option value="2">公聊，不包含@TA</option>
          		<option value="3">公聊，@TA</option>
	          </select>
          </td>
          <th width="10%">内容关键词</th>
          <td width="40%"><input type="text" name="content.value"/></td>
        </tr>
         <tr>
          <th width="10%">发布时间</th>
          <td width="40%"> 
          	从&nbsp; <input name="publishStartDateStr" id="publishStartDate" class="Wdate"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'publishEndDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
             &nbsp;&nbsp;&nbsp;&nbsp; 到&nbsp;<input name="publishEndDateStr" id="publishEndDate" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'publishStartDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
		  </td>
		  <th width="10%">数据状态</th>
          <td width="40%"> 
            <select name="valid" style="width:160px;" id="chatMessageValidId">
          		<option value="1">正常</option>
          		<option value="0">删除</option>
          		<option value="">所有</option>
	        </select>
          </td>
        </tr>
        <tr>
          <td colspan="6" align="right">&nbsp;&nbsp;
			  <a href="#" class="easyui-linkbutton" id="chatMessage_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /></a> &nbsp;&nbsp; 
			  <a href="#" class="easyui-linkbutton" id="chatMessage_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="chatMessage_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="chatMessage_datagrid_toolbar" style="display:none;">
    <a class="easyui-linkbutton approve_chat_pass" data-options="plain:true,iconCls:'ope-ok',disabled:false"  btnType="1" onclick="chatMessage.approval(this)">通过</a>
    <a class="easyui-linkbutton approve_chat_reject" data-options="plain:true,iconCls:'ope-cancel',disabled:false" btnType="2" onclick="chatMessage.approval(this)">拒绝</a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"  onclick="chatMessage.refresh()"><spring:message code="common.buttons.refresh" /></a>
    <a class="easyui-linkbutton export" data-options="plain:true,iconCls:'ope-export',disabled:false"  onclick="chatMessage.exportRecord();">导出记录</a>
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatMessage.batchDel();">删除</a>
  </div> 
  <!-- datagrid-操作按钮 -->
 <%--  <div id="chatMessage_datagrid_rowOperation" style="display:none;">
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatMessage.del(this.id)"><spring:message code="common.buttons.delete" /></a>
  </div> --%>
 
</div>