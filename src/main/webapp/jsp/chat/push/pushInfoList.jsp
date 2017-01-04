<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/push/pushInfoList.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:120px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="chatPushInfo_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">推送类型</th>
          <td width="20%">
             <select name="pushType" style="width:150px;">
                 <option value="">--请选择--</option>
	             <option value="0">预定义</option>
	             <option value="1">即时执行</option>
	         </select>
	       </td>
          <th width="10%">推送位置</th>
          <td width="20%">
              <select name="position" style="width:150px;">
                  <option value="">--请选择--</option>
	              <!--  <option value="0">任务栏</option>-->
	              <option value="1">私聊框</option>
	              <!--  <option value="2">页面提示</option> -->
	              <option value="3">公聊框</option>
	              <option value="4">视频框</option>
	          </select>
	       </td>
          <th width="10%">状态</th>
          <td width="20%"><t:dictSelect id="chatPushInfoStatus" field="status" isEdit="false" isShowPleaseSelected="true" dataList="${statusList}"/></td>
        </tr>
        <tr>
          <th>房间类别</th>
          <td>
             <t:dictSelect  selectClass="width:150px;" id="chatPushInfoGroupTypeId" field="groupType" isEdit="false" isShowPleaseSelected="false"  dataList="${groupTypeList}"/>
          </td>
          <th>所属房间</th>
          <td>
             <select class="easyui-combotree" style="width:150px;" name="roomIds"  id="chatPushInfoRoomIds" data-options="cascadeCheck:false" multiple></select>
          </td>
          <th>客户组别</th>
          <td>
             <select class="easyui-combotree" id="chatPushInfoClientGroupId" name="clientGroup" style="width:200px;" data-options="cascadeCheck:false" multiple>
	         </select>
          </td>
	   </tr>
        <tr>
          <td colspan="6" align="right">&nbsp;&nbsp;
			  <a href="#" class="easyui-linkbutton" id="chatPushInfo_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /></a> &nbsp;&nbsp; 
			  <a href="#" class="easyui-linkbutton" id="chatPushInfo_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /> </a></td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="chatPushInfo_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="chatPushInfo_datagrid_toolbar" style="display:none;">
    <a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="chatPushInfo.add();">新增</a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"  onclick="chatPushInfo.refresh()"><spring:message code="common.buttons.refresh" /></a>
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatPushInfo.batchDel();">删除</a>
  </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="chatPushInfo_datagrid_rowOperation" style="display:none;">
	  <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatPushInfo.edit(this.id)"><spring:message code="common.buttons.edit" /></a>
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatPushInfo.del(this.id)"><spring:message code="common.buttons.delete" /></a>
  </div>
</div>
