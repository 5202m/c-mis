<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/highchats/highcharts.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/infoManage/zxDataRep.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
    <!-- notrh -->
    <div data-options="region:'north',border:false" style="height: 100px;">
        <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
            <form class="yxForm" id="zxData_queryRepForm">
                <table class="tableForm_L" style="margin-top: 3px" width="99%" heigth="auto" border="0" cellpadding="0" cellspacing="1">
                    <tr>
                        <th width="10%">指标编号</th>
                        <td>
                            <input type="text" id="basicIndexId" name="basicIndexId" style="width: 160px" value="134" />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            &nbsp;&nbsp;
                            <a href="javascript:void(0);" class="easyui-linkbutton" id="zxData_queryRepForm_search" data-options="iconCls:'ope-search'">
                                <spring:message code="common.buttons.search" />
                                <!-- 查询 -->
                            </a>
                            &nbsp;&nbsp;
                            <a href="javascript:void(0);" class="easyui-linkbutton" id="zxData_queryRepForm_reset" data-options="iconCls:'ope-empty'">
                                <spring:message code="common.buttons.clear" />
                                <!-- 清空 -->
                            </a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <!-- datagrid -->
    <div data-options="region:'center',title:'图表',iconCls:'pag-list'">
        <div id="zxData_repChart" style="height:90%; display: none;"></div>
    </div>

</div>