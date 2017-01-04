<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/ueditor/ueditor.config.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/ueditor/lang/zh-cn/zh-cn.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/ueditor/ueditor.all.min.js" charset="UTF-8"> </script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/js/lib/dateTimeWeek.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/dateTimeWeek.js" charset="UTF-8"></script>
<script type="text/javascript">
    var initclientGroup="${clientGroupStr}",initgroupId="${roomIdStr}";
	//初始化
	$(function() {
		 var pushDateTmp='${chatPushInfo.pushDate}';
		 $("#chatPushInfo_pushDate_div").dateTimeWeek({data:(isValid(pushDateTmp)?JSON.parse(pushDateTmp):null)});
	     $("#chatPushInfoSMGroupTypeId").change(function(){
	    	 if($(this).val().indexOf("studio")==-1){
				 $("#chatPushInfoCGTr").hide();
			 }else{
				 $("#chatPushInfoCGTr").show();
			 }
	    	 //设置房间下拉框
			 $("#chatPushInfoSMRoomIds").combotree({
			    data:getJson(basePath +"/chatGroupController/getGroupTreeList.do",{groupType:this.value,groupId:initgroupId})
			 });
			//设置客户组下拉框
			 $("#chatPushInfoSMClientGroupId").combotree({
			    data:getJson(basePath +"/chatClientGroupController/getClientGroupList.do",{clientGroup:initclientGroup,groupType:this.value})
			 });
			 initclientGroup='';
			 initgroupId='';
	     }).trigger("change");
	     
	     UE.getEditor("chatPushInfoContentId",{
		  		initialFrameWidth : '100%',
		  		initialFrameHeight:'180',
		  		zIndex : 10000,
		  		toolbars:[[
		  		             'undo', //撤销
		  		             'redo', //重做
		  		             'bold', //加粗
		  		             'indent', //首行缩进
		  		             'italic', //斜体
		  		             'underline', //下划线
		  		             'pasteplain', //纯文本粘贴模式
		  		             'selectall', //全选
		  		             'preview', //预览
		  		             'removeformat', //清除格式
		  		             'cleardoc', //清空文档
		  		             'simpleupload', //单图上传
		  		             'justifyleft', //居左对齐
		  		             'justifyright', //居右对齐
		  		             'justifycenter', //居中对齐
		  		             'justifyjustify', //两端对齐
		  		             'imagecenter', //居中
		  		             'link', //超链接
		  		             'unlink', //取消超链接
		  		             'source', //源代码
		  		         ]
		  		     ]
	  	  });
	});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="chatPushInfoSubmitForm" class="yxForm" method="post">
    <input type="hidden"  name="id" value="${chatPushInfo.id}"/>
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0" style="background-color:#fff;">
           <tr>
	          <th width="10%">推送方式</th>
	          <td width="23%">
	             <select name="pushType" style="width:150px;">
	               <option value="0"<c:if test="${chatPushInfo.pushType=='0'}"> selected="selected" </c:if>>预定义</option>
	               <option value="1"<c:if test="${chatPushInfo.pushType=='1'}"> selected="selected" </c:if>>即时执行</option>
	             </select>
	          </td>
	          <th width="10%">推送位置</th>
	          <td width="23%">
	            <select name="position" style="width:150px;">
	               <!-- <option value="0">任务栏</option> -->
	               <option value="1"<c:if test="${chatPushInfo.position=='1'}"> selected="selected" </c:if>>私聊框</option>
	               <!-- <option value="2">页面提示</option> -->
	               <option value="3"<c:if test="${chatPushInfo.position=='3'}"> selected="selected" </c:if>>公聊框</option>
	             	<option value="4"<c:if test="${chatPushInfo.position=='4'}"> selected="selected" </c:if>>视频框</option>
	             </select>
	          </td>
	          <th width="10%">上线时长(分钟)</th>
	          <td width="23%">
	             <input type="number" name="onlineMin" value="${chatPushInfo.onlineMin}" min="0" data-options="validType:'intOrFloat'"/>
	          </td>
          </tr>
          <tr>
	          <th width="10%">房间类别</th>
	          <td width="23%">
	             <t:dictSelect  id="chatPushInfoSMGroupTypeId" selectClass="width:150px;" defaultVal="${chatPushInfo.groupType}" field="groupType" isEdit="true" isShowPleaseSelected="false"  dataList="${groupTypeList}"/>
	          </td>
	          <th width="10%">所属房间</th>
	          <td width="23%">
	            <select class="easyui-combotree" style="width:150px;" name="roomIds"  id="chatPushInfoSMRoomIds" data-options="cascadeCheck:false" multiple></select>
	          </td>
	          <th width="10%">状态</th>
	          <td width="23%">
	             <t:dictSelect field="status" isEdit="true" isShowPleaseSelected="false" defaultVal="${chatPushInfo.status}" dataList="${statusList}"/>
	          </td>
          </tr>
	      <tr id="chatPushInfoCGTr">
	          <th>客户组别</th>
	          <td>
	             <select class="easyui-combotree" id="chatPushInfoSMClientGroupId" name="clientGroup" style="width:200px;" data-options="cascadeCheck:false" multiple>
	             </select>
	          </td>
	          <th>是否延续推送</th>
	          <td>
	             <select name="replyRepeat" style="width:150px;">
	               <option value="1" <c:if test="${chatPushInfo.replyRepeat==1}"> selected="selected" </c:if>>是</option>
	               <option value="0" <c:if test="${chatPushInfo.replyRepeat==0}"> selected="selected" </c:if>>否</option>
	             </select>
	          </td>
	          <th>间隔时间(分钟)</th>
	          <td>
	          	 <input type="number" name="intervalMin" value="${chatPushInfo.intervalMin}" min="0" data-options="validType:'intOrFloat'"/>
	          </td>
	      </tr>
	      <tr>
	          <th>推送标题</th>
	          <td colspan="6" tid="title">
	             <input type="text" name="title" value="${chatPushInfo.title}" style="width:80%"/>
	          </td>
	      </tr>
	      <tr>
	          <th>跳转链接</th>
	          <td colspan="6" tid="url">
	             <input type="text" name="url" value="${chatPushInfo.url}" style="width:80%"/>
	          </td>
	      </tr>
	      <tr>
	          <th>推送内容</th>
	          <td colspan="6" tid="content">
	             <script  id="chatPushInfoContentId" name="content" type="text/plain">${chatPushInfo.content}</script>
	          </td>
	      </tr>
	      <tr>
	         <th>推送有效时间</th>
	         <td colspan="6">
	            <input type="hidden" id="chatPushInfo_pushDate" name="pushDate"/>
	            <div id="chatPushInfo_pushDate_div"></div>
	         </td>
	      </tr>
    </table>
  </form>
</div>
  
