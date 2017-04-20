<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript">
var articlePlatformStr='${articlePlatformJson}';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/article/article.js" charset="UTF-8"></script>
<div class="easyui-layout" data-options="fit:true">
  <!-- notrh -->
   <div data-options="region:'north',border:false" style="height:140px;">
    <div class="easyui-panel" data-options="fit:true,title:'<spring:message code="common.searchCondition" />',border:false,iconCls:'pag-search'">
    <form class="yxForm" id="article_queryForm">
      <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th width="10%">编号</th>
          <td width="23%"><input type="text" name="id" style="width:160px"/></td>
          <th width="10%">标题</th>
          <td width="23%"><input type="text" name="title" style="width:160px"/></td>
          <th width="10%">应用位置</th>
          <td width="23%">
	          <select class="easyui-combotree" id="articlePlatform" name="platform" style="width:180px;" data-options="url:'<%=request.getContextPath()%>/commonController/getPlatformList.do',cascadeCheck:false" multiple>
	          </select>
          </td>
        </tr>
        <tr>
          <th width="10%">状态</th>
          <td width="23%">
          	<t:dictSelect field="status" isEdit="false" isShowPleaseSelected="true"  dataList="${dictMap[dictConstant.DICT_USE_STATUS]}"/>
          </td>
          <th width="10%">栏目</th>
          <td width="23%"> 
            <input name="categoryId" id="articleCategoryId" class="easyui-combotree" style="width:160px;" data-options="url:'<%=request.getContextPath()%>/categoryController/getCategoryTree.do?type=1',valueField:'id',textField:'text'"/>
          </td>
          <th width="10%">作者</th>
          <td width="23%"><input type="text" name="author" style="width:160px"/></td>
        </tr>
         <tr>
          <th width="10%">发布时间</th>
          <td width="23%" colspan="8"> 
          	从&nbsp; <input name="publishStartDateStr" id="publishStartDate" class="Wdate"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'publishEndDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
             &nbsp;&nbsp;&nbsp;&nbsp; 到&nbsp;<input name="publishEndDateStr" id="publishEndDate" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'publishStartDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px" />
		  </td>
        </tr>
        <tr>
          	<td colspan="6" align="right">&nbsp;&nbsp;
	        	<a href="#" class="easyui-linkbutton" id="article_queryForm_search" data-options="iconCls:'ope-search'" ><spring:message code="common.buttons.search" /><!-- 查询 --> </a> 
	        	&nbsp;&nbsp; <a href="#" class="easyui-linkbutton" id="article_queryForm_reset" data-options="iconCls:'ope-empty'" ><spring:message code="common.buttons.clear" /><!-- 清空 --> </a>
        	</td>
        </tr>
      </table>
    </form>
    </div>
  </div>
  
  <!-- datagrid -->
  <div data-options="region:'center',title:'<spring:message code="common.datalist" />',iconCls:'pag-list'">
    <div id="article_datagrid" style="display:none"></div>
  </div>
  
   <!-- datagrid-toolbar -->
  <div id="article_datagrid_toolbar">
    <a class="easyui-linkbutton add" data-options="plain:true,iconCls:'ope-add',disabled:false"  onclick="article.getArticleInfo('C', '');">新增<!-- 新增 --></a> 
    <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"    onclick="article.batchDel();">删除<!-- 删除 --></a>
    <a class="easyui-linkbutton refresh" data-options="plain:true,iconCls:'ope-reload',disabled:false"   onclick="article.refresh();">刷新<!-- 刷新 --></a> 
    <a class="ope-undo setStatus">状态设置【<t:dictSelect id="article_setStatusSelect" isEdit="false" isShowPleaseSelected="true" dataList="${dictMap[dictConstant.DICT_USE_STATUS]}" selectClass="width:80px;"/>】</a>
    <a class="easyui-linkbutton getTradeStrate" data-options="plain:true,iconCls:'ope-import',disabled:false" onclick="article.getTradeStrate();">交易策略提取</a>
  </div>
  
  <!-- datagrid-操作按钮 -->
  <div id="article_datagrid_rowOperation">
	  <a class="easyui-linkbutton edit" data-options="plain:true,iconCls:'ope-edit',disabled:false"  onclick="article.getArticleInfo('U', this.id);">修改<!-- 修改 --></a>
	  <a class="easyui-linkbutton view" data-options="plain:true,iconCls:'ope-view',disabled:false" onclick="article.getArticleInfo('R', this.id);">查看<!-- 查看 --></a>
	  <a class="easyui-linkbutton delete" data-options="plain:true,iconCls:'ope-remove',disabled:false"  onclick="article.del(this.id)">删除<!-- 删除 --></a>
  </div>
  
</div>
