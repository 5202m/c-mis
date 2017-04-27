<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/chatPraise.js" charset="UTF-8"></script>
<div id="editWindow" class="easyui-dialog" closed="true"></div>
<div class="easyui-layout" data-options="fit:true">
    <!-- notrh -->
    <div data-options="region:'north',border:false" style="height:120px;">
        <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
            <form class="yxForm" id="subscribe_queryForm">
                <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
                    <tr>
                        <th width="10%">房间组别</th>
                        <td width="23%">
                            <select name="groupType" id="praise_groupType_select" style="width: 160px;">
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
                            <select class="easyui-combotree" id="analystsSelectId" style="width:250px;" data-options="cascadeCheck:false" multiple>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="right">&nbsp;&nbsp;
                            <a href="javascript:void(0);" class="easyui-linkbutton" id="praise_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /><!-- 查询 --> </a>
                            &nbsp;&nbsp; <a href="javascript:void(0);" class="easyui-linkbutton" id="praise_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /><!-- 清空 --> </a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <!-- datagrid -->
    <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
        <div id="praise_datagrid" style="display:none"></div>
    </div>

    <!-- datagrid-toolbar -->
    <div id="praise_datagrid_toolbar">
        <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"   onclick="chatPraise.refresh();"><spring:message code="common.buttons.refresh" /><!-- 刷新 --></a>
    </div>

    <!-- datagrid-操作按钮 -->
    <div id="praise_datagrid_rowOperation">
        <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false" onclick="chatPraise.edit(this.id)"><spring:message code="common.buttons.edit" /><!-- 查看 --></a>
    </div>

</div>