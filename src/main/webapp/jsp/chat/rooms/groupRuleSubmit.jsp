<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/lib/dateTimeWeek.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/dateTimeWeek.js" charset="UTF-8"></script>
<script type="text/javascript">
	//初始化
	$(function() {
		//初始化生效时间数据
		 var periodDateTmp='${chatGroupRule.periodDate}';
		 $("#chatGroupRule_periodDate_div").dateTimeWeek({data:(isValid(periodDateTmp)?JSON.parse(periodDateTmp):null)});
		 
		function selectVal(){
			var val=$("#chatGroupRuleSubmitForm select[name=type]").find("option:selected").val();
			$("tbody[id^=chatGroupRule_]").hide();
			if(val.indexOf("_set")!=-1||val.indexOf("_filter")!=-1||val.indexOf("url_")!=-1){
				$("#chatGroupRule_beforeRule_tbody").show();
			}
			if(val.indexOf("_replace")!=-1){
				$("#chatGroupRule_beforeRule_tbody").show();
				$("#chatGroupRule_afterRule_tbody").show();
			}
			if(val.indexOf('img')!=-1){
				var loc_groupType = isPlatform("HX") ? "hxstudio" : "studio";
				$("#clientGroupSelectId").combotree({
					data:getJson("<%=request.getContextPath()%>/chatClientGroupController/getClientGroupList.do",{clientGroup:"${chatGroupRule.clientGroup}",groupType:loc_groupType}),
				}); 
				$('#chatGroupRule_clientGroup_tbody').show();
			}
			if(val.indexOf('online_mem')!=-1){
				$("tbody[id^=chatGroupRule_]").hide();
				$('#chatGroupRule_online_mem_tbody').show();
			}
			if(val == "visitor_filter"){
				$("#chatGroupRule_beforeRule_tbody th:first").html("禁言游客的昵称(多个可以用逗号分隔)");
			}else if(val == "login_time_set"){
				$("#chatGroupRule_beforeRule_tbody th:first").html("登录框弹出时间(分钟)");
			}else if(val == "speak_num_set"){
				$("#chatGroupRule_beforeRule_tbody th:first").html("发言次数限制 (次)");
			}else{
				$("#chatGroupRule_beforeRule_tbody th:first").html("需要使用规则的值(多个可以用逗号分隔)");
			}
		}
		selectVal();
		$("#chatGroupRuleSubmitForm select[name=type]").change(function(){
			$("#chatGroupRuleSubmitForm input[type=text]").each(function(){
				$(this).val("");
			});
			selectVal();
		});
		if('${chatGroupRule.type}'=='online_mem_set' && isValid('${chatGroupRule.beforeRuleVal}')){
			var ruleVal = JSON.parse('${chatGroupRule.beforeRuleVal}');
			$.each(ruleVal, function(k, v){
				$('#chatGroupRuleSubmitForm #chatGroupRule_online_mem_tbody input[name="'+k+'"]').val(v);
			});
		}
		$('#chatGroupRuleSubmitForm #chatGroupRule_online_mem_tbody input[type="text"]').bind('propertychange input',function(){
			$(this).val($(this).val().replace(/[^\d]/g,''));
		});
	});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="chatGroupRuleSubmitForm" class="yxForm" method="post">
    <input type="hidden" name="id" value="${chatGroupRule.id}"/>
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
        <tbody>
	      <tr>
	          <th width="30%">规则类型</th>
	          <td width="70%">
	             <c:choose>
	               <c:when test="${not empty chatGroupRule.type}">
	               		<t:dictSelect field="type" isEdit="true" isDisabled="true" isShowPleaseSelected="false" defaultVal="${chatGroupRule.type}"  dataList="${dictList}"/>
	               </c:when>
	               <c:otherwise>
	                 <t:dictSelect field="type" isEdit="false" isShowPleaseSelected="false"  dataList="${dictList}"/>
	               </c:otherwise>
	             </c:choose>
	          </td>
	       </tr>
	       <tr>
	          <th>规则名称</th>
	          <td>
	             <input type="text" name="name" value="${chatGroupRule.name}"/>
	          </td>
	      </tr>
	      </tbody>
	      <tbody id="chatGroupRule_clientGroup_tbody">
	      	<tr>
	      		<th>客户组别</th>
	      		<td>
	      			<select class="easyui-combotree" name="clientGroupStr" id="clientGroupSelectId" style="width:250px;" data-options="cascadeCheck:false" multiple>
	            	</select>
	      		</td>
	      	</tr>
	      </tbody>
	      <tbody id="chatGroupRule_beforeRule_tbody">
		       <tr>
		          <th>需要使用规则的值(多个可以用逗号分隔)</th>
		          <td><input type="text" name="beforeRuleVal" value="${chatGroupRule.beforeRuleVal}" style="width:350px;" /></td>
			   </tr>
	      </tbody>
	      <tbody id="chatGroupRule_afterRule_tbody" style="display:none;">
		       <tr>
			      <th>使用规则后的值</th>
		      	  <td><input type="text" name="afterRuleVal" value="${chatGroupRule.afterRuleVal}" style="width:350px;"/></td>
		      </tr>
	      </tbody>
		  <tbody id="chatGroupRule_online_mem_tbody" style="display:none;">
		      <tr>
				  <th rowspan="3">虚拟在线人数</th>
				  <td><span style="width:75px;display:inline-block;text-align: right;">会员</span><input type="text" name="member" id="member" style="width:80px;" /><span style="width:30px;display:inline-block;text-align: center;">游客</span><input type="text" name="visitor" id="visitor" style="width:80px;" /></td>
			  </tr>
			  <tr>
				  <td><span style="width:75px;display:inline-block;text-align: right;">VIP会员范围</span><input type="text" name="vipstart" id="start" style="width:80px;" /><span style="width:30px;display:inline-block;text-align: center;">~</span><input type="text" name="vipend" id="end" style="width:80px;" /></td>
			  </tr>
		  		<tr>
					<td><span style="width:75px;display:inline-block;text-align: right;">普通会员范围</span><input type="text" name="normalstart" id="normalstart" style="width:80px;" /><span style="width:30px;display:inline-block;text-align: center;">~</span><input type="text" name="normalend" id="normalend" style="width:80px;" /></td>
				</tr>
		  </tbody>
	      <tbody>
		      <tr>
		          <th>执行规则后的提示语</th>
		          <td><input type="text" name="afterRuleTips" value="${chatGroupRule.afterRuleTips}" style="width:350px;" /></td>
		      </tr>
		      <tr>
		          <th>生效时间段(若不填值，则不受该时间段限制)</th>
		          <td>
		            <input type="hidden" name="periodDate"  id="chatGroupRule_periodDate"/>
		            <div id="chatGroupRule_periodDate_div"></div>
		  		  </td>
		      </tr>
	      </tbody>
    </table>
  </form>
</div>
  
