<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<style type="text/css">
   .header_default div label{
      margin-right: 20px;
      margin-left: 20px;
      width: 33px;
   }
   .header_default div img{
      margin-right:10px;
      margin-left: 10px;
      width: 33px;
   }
</style>
<script type="text/javascript">
	$('#analyst').text(chatPraise.getAnalystCNameByCode("${chatPraise.praiseId }"));
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="praiseEditFrom" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	<tr>
    		<th>分析师</th>
    		<td>
    			<span id="analyst"></span>
    		</td>
		</tr>
		<tr>
      		<th>点赞数<span class="red">*</span></th>
			<td>
				<input type="number" id="praiseNum" name="praiseNum" value="${chatPraise.praiseNum }" />
			 </td>
      </tr>
    </table>
    <input type="hidden" name="id" value="${chatPraise.id}" />
  </form>
</div>
