<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<script type="text/javascript">
	$(function(){
		$('#parentId').combotree({
			panelWidth:200,
			onLoadSuccess:function(node, checked){
				$('#categoryViewForm table tr td span.combo input').width(200);
			}
		});
	})
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="categoryViewForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
         <tr>
	          <th width="15%">上级栏目编号<span class="red">*</span></th>
	          <td width="35%" colspan="4">
	            <input id="parentId" name="parentId" class="easyui-combotree"
           			data-options="url:'<%=request.getContextPath()%>/categoryController/getCategoryTree.do',valueField:'id',textField:'text',panelWidth:200"
           			value="${parentId}" style="width:200px;">
	          </td>
	      </tr>
	      <tr>
	          <th width="15%">栏目编号<span class="red">*</span></th>
	          <td width="35%"><input type="text" name="id" value="${category.id}" class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'栏目编号不能为空'"/></td>
		      <th width="15%">栏目名称<span class="red">*</span></th>
	      	  <td width="35%"><input type="text" name="name" value="${category.name}" class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'栏目名称不能为空'"/></td>
	      </tr>
	      <tr>
	          <th>类型：</th>
	          <td>
	      	      <select name="type" id="category_type">
	      	 	  	 <option value="0">请选择</option>
	      	 	  	 <option value="1" <c:if test="${category.type == 1}">selected="selected"</c:if> >文章</option>
         			 <option value="2" <c:if test="${category.type == 2}">selected="selected"</c:if> >媒体</option>
	      	      </select>
	      	  </td>
	      	  <th>状态： </th>
	      	  <td>
	      	      <select name="status">
	      	 	  	 <option value="1" <c:if test="${category.status == 1}">selected="selected"</c:if> >启用</option>
	        			 <option value="0" <c:if test="${category.status == 0}">selected="selected"</c:if> >禁用</option>
	      	      </select>
	      	  </td>
	      </tr>
	      <tr>
		     <th >排序：</th>
	      	 <td colspan="3"><input type="text" name="sort" value="${category.sort}"/></td>
	      </tr>
    </table>
  </form>
</div>
  
