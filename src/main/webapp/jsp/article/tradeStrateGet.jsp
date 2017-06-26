<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<style type="text/css">
.strateTitleDiv{
  overflow-y:auto;height:120px;
}
.strateTitleDiv input{
  margin-top:5px;
}
</style>
<script type="text/javascript">
$(function() {
	 $("#addStrateTitleId").click(function(){
		 var textNo=$(".strateTitleDiv p").length==0?0:parseInt($(".strateTitleDiv p:last").find("strong").text());
		 $(".strateTitleDiv").append('<p><strong>'+(textNo+1)+'</strong>:<input type="text" style="width:300px;"/><a href="javascript:">&nbsp;清空</a></p>');
		 $(".strateTitleDiv p:last").find("a").click(function(){
	    	 $(this).prev().val("");
	     });
	 });
     $("#removeStrateTitleId").click(function(){
    	 if($(".strateTitleDiv p").length>1){
    		 $(".strateTitleDiv p:last").remove();
    	 }
	 });
     $("#addStrateTitleId").click();
	//设置多选下拉框宽度
	$('#trade_strategy_platform').combotree({
		panelWidth:180,
		onBeforeLoad: function (node, param) {
			$('#tradeStrateGetForm table tr td span.combo input').width(180);
		}
	});
});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="tradeStrateGetForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	   <tr>
	         <th width="15%">策略来源<span class="red">*</span></th>
	         <td width="80%">
	           <select name="srcPlaform">
	                <option value="web24k">实盘直播(24k官网)</option>
	           </select>
	         </td>
	      </tr>
	      <tr>
	         <th width="15%">提取日期<span class="red">*</span></th>
	         <td width="40%"> 
	           <input type="hidden" id="serverDateId" value="${serverDate}" />
          	   <input name="dateStr" class="Wdate" value="${serverDate}" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'serverDateId\')}',dateFmt:'yyyy-MM-dd'})" style="width:150px" />
		     </td>
	      </tr>
	      <tr>
	         <th width="15%">语言<span class="red">*</span></th>
	         <td width="80%">
	            <select name="lang">
	                <option value="zh">简体</option>
	           </select>
	         </td>
	      </tr>
	      <tr>
	        <th width="15%">标题<br/>(若干,如输入,则提取该标题对应的记录)<a class="easyui-linkbutton" id="addStrateTitleId" data-options="plain:true,iconCls:'ope-add',disabled:false"></a>
	        <a class="easyui-linkbutton" id="removeStrateTitleId" data-options="plain:true,iconCls:'ope-remove',disabled:false"></a></th>
	        <td width="80%" height="120px">
	            <input type="hidden" id="strateTitlesId" name="titles" value=""/>
	            <div class="strateTitleDiv"></div>
	        </td>
	     </tr>
	     <tr>
	      <th width="10%">策略应用位置<span class="red">*</span></th>
          <td width="23%">
	          <select class="easyui-combotree easyui-validatebox" id="trade_strategy_platform" name="platform" style="width:180px;" data-options="required:true,missingMessage:'请输入应用位置',url:'<%=request.getContextPath()%>/commonController/getPlatformList.do',cascadeCheck:false" multiple>
	          </select>
          </td>
        </tr>
    </table>
  </form>
</div>
  
