<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/rooms/groupList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:120px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="chatGroup_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">编号</th>
          <td width="20%"><input type="text" name="id" /></td>
          <th width="10%">名称</th>
          <td width="20%"><input type="text" name="name" /></td>
          <th width="10%">状态</th>
          <td width="20%"><t:dictSelect id="chatGroupStatus" field="status" isEdit="false" isShowPleaseSelected="true" dataList="${statusList}"/></td>
        </tr>
        <tr>
          <th>房间类别</th>
          <td>
             <t:dictSelect  selectClass="width:170px;" id="chatGroupType" field="groupType" isEdit="false" isShowPleaseSelected="false"  dataList="${groupTypeList}"/>
          </td>
          <th>聊天规则</th>
          <td>
             <select class="easyui-combotree" style="width:400px;" name="chatRuleId"  id="chatRuleIds" data-options="cascadeCheck:false" multiple></select>
          </td>
          <th>聊天方式</th>
          <td>
             <select class="easyui-combotree" id="chatTalkStyleId" name="talkStyleStr" style="width:200px;" data-options="cascadeCheck:false" multiple>
	         </select>
          </td>
	   </tr>
        <tr>
          <td colspan="6" align="right">&nbsp;&nbsp;
			  <a href="#" class="easyui-linkbutton" id="chatGroup_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /></a> &nbsp;&nbsp; 
			  <a href="#" class="easyui-linkbutton" id="chatGroup_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="chatGroup_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="chatGroup_datagrid_toolbar" style="display:none;">
    <a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="chatGroup.add();">新增</a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"  onclick="chatGroup.refresh()"><spring:message code="common.buttons.refresh" /></a>
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatGroup.batchDel();">删除</a>
  </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="chatGroup_datagrid_rowOperation" style="display:none;">
	  <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatGroup.edit(this.id)"><spring:message code="common.buttons.edit" /></a>
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatGroup.del(this.id)"><spring:message code="common.buttons.delete" /></a>
	  <a class="easyui-linkbutton assignUser" data-options="plain:true,iconCls:'ope-grant',disabled:false"  onclick="chatGroup.authUser(this.id)">用户授权</a>
	  <a class="easyui-linkbutton bookingUser" data-options="plain:true,iconCls:'ope-grant',disabled:false"  onclick="chatGroup.trainClient(this.id)">客户授权</a>
	  <a class="easyui-linkbutton importClient" data-options="plain:true,iconCls:'ope-grant',disabled:false"  onclick="chatGroup.importClient(this.id)">客户导入</a>
  </div>
 
</div>
