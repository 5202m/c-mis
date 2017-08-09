<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/lib/dateTimeWeek.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/dateTimeWeek.js" charset="UTF-8"></script>
<script type="text/javascript">
	//初始化
	$(function() {
		 $("#chatGroupTypeId").change(function(){
			 if(this.value.indexOf('studio')!=-1 || this.value.indexOf('stock')!=-1){
				 $("#clientGroupTreeId").show();
			 }else{
				 $("#clientGroupTreeId").hide();
			 }
			 $("#clientGroupSelectId").combotree({
				    panelWidth:180,
				    data:getJson("<%=request.getContextPath()%>/chatClientGroupController/getClientGroupList.do",{clientGroup:"${chatGroup.clientGroup}",groupType:this.value}),
				}); 
		 }).trigger("change");
		 var openDateTmp='${chatGroup.openDate}';
		 $("#chatGroup_openDate_div").dateTimeWeek({data:(isValid(openDateTmp)?JSON.parse(openDateTmp):null)});
		 var reData=getJson("<%=request.getContextPath()%>/chatGroupRuleController/getGroupRuleCombox.do");
		 var contentRuleIds=$("#chatSubmitRuleIds").attr("tId");
		 //设置内容规则的下拉框
		 for(var i in reData){
			if(contentRuleIds.indexOf(reData[i].id)!=-1){
				reData[i].checked=true;
			}
		 }
		 $("#chatSubmitRuleIds").combotree({
			panelWidth:300,
			data:reData
		}); 
		//对话列表
		var tsData=getJson("<%=request.getContextPath()%>/commonController/getTalkStyleList.do");
		var chatTalkStyleIds=$("#chatTalkStyleIds").attr("tId");
		if(!chatTalkStyleIds){
			//默认选中“对话”
			chatTalkStyleIds = "0";
		}
		//设置对话列表的下拉框
		for(var i in tsData){
			if(chatTalkStyleIds.indexOf(tsData[i].id)!=-1){
				tsData[i].checked=true;
				if(tsData[i].id==1){
					$("#chatWhisperRoleSpan").show();
				}
			}
		}
		$("#chatTalkStyleIds").combotree({
			panelWidth:120,
		    data:tsData,
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
			panelWidth:120,
		    data:whisperRoleData
		});
		var defTemplate = $('#defTemplate').val();
		if(isValid(defTemplate)){
			defTemplate = JSON.parse(defTemplate);
			$('#theme').val(defTemplate.theme);
			$('#style').val(defTemplate.style);
		}
		var logoJson = $('#logo').val();
		if(isValid(logoJson)){
			logoJson = JSON.parse(logoJson);
			$('#pc_logo,#sourcePcLogoPath,#cutedPcLogoPath').val(logoJson.pc);
			$('#mb_logo,#sourceMbLogoPath,#cutedMbLogoPath').val(logoJson.mb);
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
	             <select class="easyui-combotree" style="width:180px;" name="talkStyleStr"  id="chatTalkStyleIds" tId="${chatGroup.talkStyle}" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入聊天方式',cascadeCheck:false" multiple></select>
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
		<tr>
			<th colspan="2">培训班配置（标记详情页、lp页面的配置信息）</th>
			<td colspan="2">
				<input  name="trainConfig" style="width:50%;" value="${chatGroup.trainConfig}">
			</td>
		</tr>
		<tr>
			<th>房间Logo</th>
			<td>
				<input type="hidden" name="logo" id="logo" value='${chatGroup.logo}' />
				<div id="pc_logo_div">
					&nbsp;PC：&nbsp;<input type="text" id="pc_logo" style="margin-bottom: 5px;" class="easyui-validatebox"
											data-options="validType:'url',missingMessage:'请填入一个有效的URL'"/>
					<!--input type="button" value="设置链接" id="addPcLogoHander"-->
					<input type="file"  id="mediaPcLogoFileId" style="width:155px" />
					<!-- 原图片路径 -->
					<input type="hidden" id="sourcePcLogoPath"/>
					<!-- 裁剪后图片的路径 -->
					<input type="hidden" id="cutedPcLogoPath"/>
					<!-- 表单提交时保存到数据库的字段-->
					<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:$('#mediaPcLogoFileId').uploadify('upload', '*');">上传文件</a>
					<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#mediaPcLogoFileId').uploadify('cancel', '*');">停止上传</a>
					<a t="viewImage" class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#cutedPcLogoPath')">预览</a>
					<a t="cutImage" class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#sourcePcLogoPath','#cutedPcLogoPath','cut','#pc_logo')">裁剪</a>
				</div>
			</td>
			<td colspan="2">
				<div>
					&nbsp;MB：&nbsp;<input type="text" id="mb_logo" style="margin-bottom: 5px;" class="easyui-validatebox"
											data-options="validType:'url',missingMessage:'请填入一个有效的URL'"/>
					<!--input type="button" value="设置链接" id="addMbLogoHander"-->
					<input type="file"  id="mediaMbLogoFileId" style="width:155px" />
					<!-- 原图片路径 -->
					<input type="hidden" id="sourceMbLogoPath"/>
					<!-- 裁剪后图片的路径 -->
					<input type="hidden" id="cutedMbLogoPath"/>
					<!-- 表单提交时保存到数据库的字段-->
					<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:$('#mediaMbLogoFileId').uploadify('upload', '*');">上传文件</a>
					<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#mediaMbLogoFileId').uploadify('cancel', '*');">停止上传</a>
					<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#cutedMbLogoPath')">预览</a>
					<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#sourceMbLogoPath','#cutedMbLogoPath','cut','#mb_logo')">裁剪</a>
				</div>
			</td>
		</tr>
    </table>
  </form>
</div>
  
