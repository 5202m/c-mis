<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<div style="padding:5px;overflow:hidden;">
  <form id="dictionaryEditForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
	      <tr>
	          <th width="15%"><spring:message code="dictionary.no" /><!-- 字典编号 --><span class="red">*</span></th>
	          <td width="35%">
	          <%--  <span>${dictionaryJsonParam.code}</span> --%>
	          <input type="text" name="code" id="codeTmp" value="${dictionaryJsonParam.code}" class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="dictionary.valid.no" />'"/>
		      </td>
	          <th width="15%"><spring:message code="menu.name.zh" /><!-- 简体名称 --><span class="red">*</span></th>
	      	  <td width="35%"><input type="text" name="nameCN" id="nameCN" value="${dictionaryJsonParam.nameCN}"  class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="menu.valid.name.zh" />'"/>
		      </td>
	      </tr>
	      <tr>
	      	  <th width="15%"><spring:message code="menu.name.tw" /><!-- 繁体名称 --><span class="red">*</span></th>
	      	  <td width="35%"><input type="text" name="nameTW" id="nameTW" value="${dictionaryJsonParam.nameTW}"  class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="menu.valid.name.tw" />'"/>
		      </td>
		      <th width="15%"><spring:message code="menu.name.en" /><!-- 英文名称 --><span class="red">*</span></th>
	      	  <td width="35%"><input type="text" name="nameEN" id="nameEN" value="${dictionaryJsonParam.nameEN}"  class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="menu.valid.name.en" />'"/>
		      </td>
		  </tr>
		  <tr>
	      	  <th width="15%"><spring:message code="menu.order" /><!-- 排序 --><span class="red">*</span></th>
	      	  <td width="35%" colspan="3"><input type="text" name="sort" id="sort" value="${dictionaryJsonParam.sort}" class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="menu.valid.order" />'"/>&nbsp;&nbsp;<span class="red">(相对于同一个父节点下的顺序)</span></td>
	      </tr>
          <c:if test="${dictionaryJsonParam.type!=1}">
            <tr>
	          <th>系统类别</th>
	          <td colspan="3">
	             <select class="easyui-combotree" id="systemCategoryStr" name="systemCategoryStr" data-options="cascadeCheck:false,url:'<%=request.getContextPath()%>/systemCategoryController/getCategoryTreeList.do?categoryId=${systemCategory}'" multiple>
	             </select>
	          </td>
	        </tr>
	        </c:if>
	        <tr>
	          <th>参数值</th>
	          <td colspan="3">
	             <input type="text" name="value" value="${dictionaryJsonParam.value}" style="width:450px;"/>
	          </td>
	        </tr>
	      <tr>
	          <th>状态： </th>
	      	  <td colspan="3">
	      	      <select name="status">
	      	 	  	 <option value="1" <c:if test="${dictionaryJsonParam.status == 1}">selected="selected"</c:if> >启用</option>
	        		 <option value="0" <c:if test="${dictionaryJsonParam.status != 1}">selected="selected"</c:if> >禁用</option>
	      	      </select>
	      	  </td>
	      </tr>
    </table>
    <input type="hidden" name="type"  value="${dictionaryJsonParam.type}"/>
    <input type="hidden" name="id"  value="${dictionaryJsonParam.id}"/>
    <input type="hidden" name="parentCode"  value="${dictionaryJsonParam.parentCode}"/>
  </form>
</div>
  
