<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="mediaDetailTemp" style="display:none;">
<div style="padding:0px;overflow-x:hidden;height:700px;width:100%;">
  <form name="mediaDetailForm" class="yxForm">
    <input type="hidden" name="lang"/>
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
      <tr>
      	<th width="15%">标题</th>
        <td width="85%"><input type="text" name="title" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入标题'" style="width:600px"/>
	    </td>
      </tr>
      <tr>
		<th>作者</th>
        <td>
        <!--input type="hidden" name="author"/>
        <input type="hidden" name="authorId"/-->
        <input type="hidden" name="userId" />
        <input type="hidden" name="name" />
        <input type="hidden" name="position" />
        <input type="hidden" name="avatar" />
        <select name="authorAvatar" style="width:180px;"></select>
        <!--input type="text" name="author" style="width:600px"/--></td>
      </tr>
      <tr>
		<th width="15%">简介</th>
        <td width="85%"><textarea rows="2" cols="6" name="remark" style="width:600px"></textarea></td>
       </tr>
       <tr>
		<th width="15%">标签</th>
        <td width="85%"><input type="text" name="tag" style="width:600px"/></td>
	  </tr>
      <tr>
		<th width="15%">SEO标题</th>
        <td width="85%"><input type="text" name="seoTitle" style="width:600px"/></td>
      </tr>
       <tr>
		<th width="15%">SEO关键字(多个用逗号分隔)</th>
        <td width="85%"><input type="text" name="seoKeyword" style="width:600px"/></td>
       </tr>
       <tr>
		<th width="15%">描述</th>
        <td width="85%"><textarea rows="2" cols="6" name="seoDescription" style="width:600px"></textarea></td>
       </tr>
    </table>
  </form>
</div>
</div>