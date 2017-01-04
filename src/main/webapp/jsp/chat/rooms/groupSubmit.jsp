<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/lib/dateTimeWeek.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/dateTimeWeek.js" charset="UTF-8"></script>
<script type="text/javascript">
	//初始化
	$(function() {
		 $("#chatGroupTypeId").change(function(){
			 if(this.value.indexOf('studio')!=-1){
				 $("#clientGroupTreeId").show();
			 }else{
				 $("#clientGroupTreeId").hide();
			 }
			 $("#clientGroupSelectId").combotree({
				    data:getJson("<%=request.getContextPath()%>/chatClientGroupController/getClientGroupList.do",{clientGroup:"${chatGroup.clientGroup}",groupType:this.value}),
				}); 
		 }).trigger("change");
		 var openDateTmp='${chatGroup.openDate}';
		 $("#chatGroup_openDate_div").dateTimeWeek({data:(isValid(openDateTmp)?JSON.parse(openDateTmp):null)});
		 getJson("<%=request.getContextPath()%>/chatGroupRuleController/getGroupRuleCombox.do",null,function(data){
			var contentRuleIds=$("#chatSubmitRuleIds").attr("tId");
			//设置内容规则的下拉框
			for(var i in data){
				if(contentRuleIds.indexOf(data[i].id)!=-1){
					data[i].checked=true;
				}
			}
			$("#chatSubmitRuleIds").combotree({
			    data:data
			}); 
		},true);
		//对话列表
		getJson("<%=request.getContextPath()%>/commonController/getTalkStyleList.do",null,function(data){
				var chatTalkStyleIds=$("#chatTalkStyleIds").attr("tId");
				if(!chatTalkStyleIds){
					//默认选中“对话”
					chatTalkStyleIds = "0";
				}
				//设置对话列表的下拉框
				for(var i in data){
					if(chatTalkStyleIds.indexOf(data[i].id)!=-1){
						data[i].checked=true;
						if(data[i].id==1){
							$("#chatWhisperRoleSpan").show();
						}
					}
				}
				$("#chatTalkStyleIds").combotree({
				    data:data,
				    onCheck:function(r){
				    	var tsObj=$("#chatGroupSubmitForm input[name=talkStyleStr]");
				    	if(tsObj.length==0){
				    		$("#chatWhisperRoleSpan").hide();
				    	}else{
				    		tsObj.each(function(){
					    		if("1"==$(this).val()){
						    		$("#chatWhisperRoleSpan").show();
						    	}else{
						    		$("#chatWhisperRoleSpan").hide();
						    	}
					    	});
				    	}
				    }
				}); 
		},true);
		//私聊角色
		var whisperRoleData=[{id:3,text:'客服'},{id:2,text:'分析师'},{id:1,text:'管理员'}];
		var whisperRoleTmp=$("#chatWhisperRoleId").attr("tId");
		if(!whisperRoleTmp){
			//默认选中客服
			whisperRoleTmp = "3";
		}
		//设置私聊角色的下拉框
		for(var i in whisperRoleData){
			if(whisperRoleTmp.indexOf(whisperRoleData[i].id)!=-1){
				whisperRoleData[i].checked=true;
			}
		}
		$("#chatWhisperRoleId").combotree({
		    data:whisperRoleData
		});
		var defTemplate = $('#defTemplate').val();
		if(isValid(defTemplate)){
			defTemplate = JSON.parse(defTemplate);
			$('#theme').val(defTemplate.theme);
			$('#style').val(defTemplate.style);
		}
	});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="chatGroupSubmitForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
          <tr>
	          <th width="15%">房间类别</th>
	          <td width="35%">
	             <t:dictSelect  selectClass="width:170px;" id="chatGroupTypeId" defaultVal="${chatGroup.groupType}" field="groupType" isEdit="true" isShowPleaseSelected="false"  dataList="${groupTypeList}"/>
	          </td>
	          <th width="15%">房间等级</th>
	          <td width="35%">
	             <t:dictSelect  selectClass="width:170px;" id="chatGroupLevel" defaultVal="${chatGroup.level}" field="level" isEdit="true" isShowPleaseSelected="false"  dataList="${groupLevelList}"/>
	          </td>
          </tr>
    	   <tr>
	         <th width="15%">编号(系统自动生成)</th>
	         <td width="35%">
	              <input type="hidden" name="id" value="${chatGroup.id}"/>
	              <input type="text" value="${chatGroup.id}" disabled="disabled"/>
	         </td>
	         <th width="15%">名称</th>
	         <td width="35%">
	              <input type="text" name="name" value="${chatGroup.name}" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入名称'" />
	         </td>
	      </tr>
	       <tr id="clientGroupTreeId">
	          <th width="15%">客户组别</th>
	          <td colspan="3">
	            <%-- <select class="easyui-combotree" name="clientGroupStr" style="width:250px;" data-options="url:'<%=request.getContextPath()%>/chatClientGroupController/getClientGroupList.do?clientGroup=${chatGroup.clientGroup}',cascadeCheck:false" multiple>
	            </select> --%>
	            <select class="easyui-combotree" name="clientGroupStr" id="clientGroupSelectId" style="width:250px;" data-options="cascadeCheck:false" multiple>
	            </select>
	          </td>
	      </tr>
	      <tr>
	          <th>聊天方式</th>
	          <td colspan="3">
	             <select class="easyui-combotree" style="width:175px;" name="talkStyleStr"  id="chatTalkStyleIds" tId="${chatGroup.talkStyle}" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入聊天方式',cascadeCheck:false" multiple></select>
	             <span style="margin-left:40px;display:none;" id="chatWhisperRoleSpan">角色授权(私聊)<span style="margin-left:18px;"><select class="easyui-combotree" style="width:175px;" name="chatWhisperRoleStr"  id="chatWhisperRoleId" tId="${chatGroup.whisperRoles}" class="easyui-validatebox" data-options="required:true,missingMessage:'请选择角色',cascadeCheck:false" multiple></select></span></span>
	          </td>
	      </tr>
	      <tr>
	          <th width="15%">聊天规则</th>
	          <td width="80%" colspan="3">
	             <select class="easyui-combotree" style="width:400px;" name="chatRuleId"  id="chatSubmitRuleIds" tId="${chatGroup.chatRuleIds}" data-options="cascadeCheck:false" multiple></select>
	          </td>
	      </tr>
	      <tr>
	          <th>最大人数</th>
	          <td>
	             <input type="number" name="maxCount" value="${chatGroup.maxCount}" class="easyui-validatebox" data-options="required:true"/>
	          </td>
	          <th>默认分析师</th>
	          <td>
	          	<select name="defaultAnalyst.userId" id="groupSubmit_analystList" style="width:170px;">
	          	  <option value="">--请选择--</option>
	          	  <c:forEach var="row" items="${analystList}">
	                 <option value="${row.userId}" <c:if test="${chatGroup.defaultAnalyst != null && row.userId == chatGroup.defaultAnalyst.userId}">selected="selected"</c:if>>${row.userName}【${row.role.roleName}】</option>
	              </c:forEach>
	            </select>
	          </td>
	      </tr>
	      <tr>
	          <th>状态</th>
	          <td>
	             <t:dictSelect field="status" isEdit="true" isShowPleaseSelected="false" defaultVal="${chatGroup.status}" dataList="${statusList}"/>
	          </td>
	          <th>排序</th>
	          <td>
	              <input name="sequence" value="${chatGroup.sequence}" class="easyui-validatebox" data-options="required:true"/>
	          </td>
	      </tr>
	      <tr>
	      	<th>默认皮肤</th>
	      	<td>
	      		<select name="theme" id="theme">
	      			<option value="">请选择</option>
	      			<option value="theme1">主题1</option>
	      		</select>
	      		<select name="style" id="style">
	      			<option value="">请选择</option>
	      			<option value="light">清新蓝</option>
	      			<option value="dark">炫酷黑</option>
	      			<option value="orange">橘子橙</option>
	      			<option value="darkblue">星空蓝</option>
	      			<option value="gold">雅金</option>
	      			<%-- <c:forEach var="v" begin="0" end="5">
	      			<option value="index${v}">样式${v}</option>
	      			</c:forEach> --%>
	      		</select>
	      		<input type="hidden" name="defTemplate" id="defTemplate" value='${chatGroup.defTemplate}' />
	      	</td>
	      	<th>房间类别</th>
	      	<td>
	      		<select name="roomType" id="roomType">
	      			<option value="normal"<c:if test="${chatGroup.roomType=='normal'}"> selected="selected"</c:if>>普通</option>
	      			<option value="vip"<c:if test="${chatGroup.roomType=='vip'}"> selected="selected"</c:if>>VIP</option>
	      			<option value="train"<c:if test="${chatGroup.roomType=='train'}"> selected="selected"</c:if>>培训班</option>
	      		</select>
	      	</td>
	      </tr>
	      <tr>
	      	<th>进入房间积分</th>
	      	<td colspan="3"><input type="text" name="point" id="point" value="${chatGroup.point}" /></td>
	      </tr>
	      <tr>
	         <th>开放时间</th>
	         <td colspan="3">
	            <input type="hidden" name="openDate"  id="chatGroup_openDate"/>
	            <div id="chatGroup_openDate_div"></div>
	         </td>
	      </tr>
	      <tr>
	         <th>房间介绍</th>
	         <td colspan="3">
	           <textarea rows="3" name="remark" style="width:100%;">${chatGroup.remark}</textarea>
	         </td>
	      </tr>
		<tr>
			<th>房间标签</th>
			<td colspan="3">
                <input  name="label" style="width:50%;" value="${chatGroup.label}">
			</td>
		</tr>
    </table>
  </form>
</div>
  
