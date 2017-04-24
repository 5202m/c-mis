<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/trade/showTrade.js" charset="UTF-8"></script>
<div id="editWindow" class="easyui-dialog" closed="true"></div>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:120px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="show_trade_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
        	<th>姓名</th>
        	<td><input type="text" name="userName" id="userName" /></td>
        	<th>晒单类别</th>
        	<td>
	        	<select id="tradeType" name="tradeType">
	        		<option value="0">--请选择--</option>
	        		<option value="1">分析师晒单</option>
	        		<option value="2">客户晒单</option>
	        	</select>
        	</td>
        </tr>
        <tr>
          <th width="10%">房间组别</th>
			<td width="23%">
				<select name="groupType" id="showTrade_groupType_select" style="width: 160px;">
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
          <th width="10%">分析师</th>
          <td width="23%">
          	<input type="hidden" name="userNo" id="chatTradeSearchUserNoInput">
            <select id="chatTradeSearchUserNo" style="width:280px;"></select>
            <input type="hidden" id="showTradeListImgView">
          </td>
        </tr>
        <tr>
        <th width="10%">状态</th>
        <td colspan="3">
        	<select name="status" id="showTrade_status_select">
   				<option value="">--请选择--</option>
   				<option value="1">通过</option>
   				<option value="0">待审核</option>
   				<option value="-1">未通过</option>
   			</select>
        </td>
        </tr>
        <tr>
          	<td colspan="4" align="right">&nbsp;&nbsp;
	        	<a href="#" class="easyui-linkbutton" id="show_trade_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /><!-- 查询 --> </a> 
	        	&nbsp;&nbsp; <a href="#" class="easyui-linkbutton" id="show_trade_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /><!-- 清空 --> </a>
        	</td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="show_trade_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="show_trade_datagrid_toolbar">
    <a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="chatShowTrade.add();"><spring:message code="common.buttons.add" /><!-- 新增 --></a> 
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"    onclick="chatShowTrade.batchDel();"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"   onclick="chatShowTrade.refresh();"><spring:message code="common.buttons.refresh" /><!-- 刷新 --></a>
    <a class="easyui-linkbutton setStatus" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatShowTrade.setStatus(1)">审核通过 </a>
    <a class="easyui-linkbutton setStatus" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatShowTrade.setStatus(-1)">审核不通过 </a> 
  </div> 
  
  <!-- datagrid-操作按钮 -->
  <div id="show_trade_datagrid_rowOperation">
	  <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="chatShowTrade.edit(this.id)"><spring:message code="common.buttons.edit" /><!-- 修改 --></a>
	  <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false" onclick="chatShowTrade.view(this.id)"><spring:message code="common.buttons.view" /><!-- 查看 --></a>
      <a class="easyui-linkbutton review" data-options="plain:true,iconCls:'ope-view',disabled:false" onclick="chatShowTrade.openViewComment(this.id)">管理评论</a>
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="chatShowTrade.del(this.id)"><spring:message code="common.buttons.delete" /><!-- 删除 --></a>
  </div>

    <!-- 评论数据 -->
    <div id="showTradeCommentView_win" class="easyui-dialog" closed="true">
        <div data-options="region:'center'" id="showTradeCommentView_panel">
            <input type="hidden" name="sid" value="" />
            <div>
                <div data-options="region:'center',title:'评论列表',iconCls:'pag-list'">
                    <div id="showTradeComment_datagrid" style="display: none"></div>
                    <div id="showTradeComment_page" class="datagrid-pager pagination"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- showTradeCommentView_datagrid-操作按钮 -->
    <div id="showTradeComment_datagrid_rowOperation" style="display: none;">
        <input type="hidden" value="" />
        <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false" onclick="chatShowTrade.delComment(this);">删除</a>
    </div>
</div>