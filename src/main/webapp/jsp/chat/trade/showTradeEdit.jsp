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
//初始化
$(function() {
	//晒单图片
	goldOfficeUtils.uploadFile({
		'fileId' : 'tradeImgFile',
		'formData' : {'fileDir' : 'pic', 'saveSrc':'1'},
		'fileSizeLimit' : 10*1024*1024,
		'fileTypeDesc': '只能上传*.jpg;*.gif;*.png;*.jpeg类型的图片',
		'fileTypeExts' : '*.jpg;*.gif;*.png;*.jpeg',
		'uploader' : basePath+'/uploadController/upload.do',
		'onUploadSuccess' : function(file, data, response){
			var d = eval("("+data+")");			//转换为json对象 
			if(d.success){
				alert(file.name + ' 上传成功！');
				if(d.obj != null){
					$("#tradeImgPath").val(d.obj);
					$("#tradeImgPathSrc").val(d.obj);
					$("#tradeImgPathCut").val(d.obj);
				}
			}else{
				alert(file.name + d.msg);
			}
		}
	});
});
</script>
<div style="padding:5px;overflow:hidden;">
  <form id="showTradeEditFrom" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
    	<tr<c:if test="${chatTrade.tradeType==1}"> style="display:none"</c:if>>
    		<th>标题<span class="red">*</span></th>
    		<td colspan="3"><input type="text" name="title" id="title" style="width:350px;" value="${chatTrade.title}" class="easyui-validatebox"<c:if test="${chatTrade.tradeType==2}"> data-options="required:true"</c:if> /></td>
    	</tr>
    	<tr<c:if test="${chatTrade.tradeType==1}"> style="display:none"</c:if>>
    		<th>类别</th>
    		<td>
    			<label><input type="radio" name="trade_Type" disabled="disabled" value="1"<c:if test="${chatTrade.tradeType==1 || chatTrade.groupType!=''}"> checked="checked"</c:if> />分析师晒单</label>
    			<label><input type="radio" name="trade_Type" disabled="disabled" value="2"<c:if test="${chatTrade.tradeType==2 || chatTrade.groupType==''}"> checked="checked"</c:if> />客户晒单</label>
    			<input type="hidden" name="tradeType" value="${chatTrade.tradeType}" />
    			<input type="hidden" name="avatar" value="${chatTrade.boUser.avatar}" />
    			<input type="hidden" name="userName" value="${chatTrade.boUser.userName}" />
    			<input type="hidden" name="userId" value="${chatTrade.boUser.userNo}" />
    			<input type="hidden" name="telephone" value="${chatTrade.boUser.telephone}" />
    		</td>
    		<th>状态</th>
    		<td>
    			<select name="status" id="status">
    				<option value="1"<c:if test="${chatTrade.status==1}"> selected="selected"</c:if>>通过</option>
    				<option value="0"<c:if test="${chatTrade.status==0}"> selected="selected"</c:if>>待审核</option>
    				<option value="-1"<c:if test="${chatTrade.status==-1}"> selected="selected"</c:if>>未通过</option>
    			</select>
    		</td>
    	</tr>
      <tr>
      
      <th width="10%">房间组别<span class="red">*</span></th>
		<td colspan="3">
			<select name="groupType" id="syllabus_groupType_select" style="width: 160px;">
				<option value="">--请选择--</option>
				<c:forEach var="row" items="${chatGroupList}">
					<c:if test="${empty row.id}">
						<option value="${row.groupType}" <c:if test="${row.groupType==chatTrade.groupType}">selected="selected"</c:if>>
							${row.name}
						</option>
					</c:if>
				</c:forEach>
			</select>
      </td>
      </tr>
      <tr<c:if test="${chatTrade.tradeType==2}"> style="display:none"</c:if>>
        <th width="10%">分析师<span class="red">*</span></th>
        <td colspan="3">
        <input type="hidden" name="userNo" id="chatTradeEditUserNoInput" value="${chatTrade.boUser.userNo}" data-userName="${chatTrade.boUser.userName}">
        <select  id="chatTradeEditUserNo" style="width:280px;"></select>
       </td>
      </tr>
      <tr<c:if test="${chatTrade.tradeType==2}"> style="display:none"</c:if>>
        <th>获利</th>
        <td colspan="3"><input type="text" name="profit" id="profit" value="${chatTrade.profit}" />
        <span class="red"> ( 若不填值视为"持仓中" )</span>
        </td>
      </tr>
      
      <tr>
        <th>晒单图片<span class="red">*</span></th>
        <td colspan="3">
        	<div>图片路径：<input type="text" id="tradeImgPath" name="tradeImg" style="width:350px;margin-top:5px;" class="easyui-validatebox" data-options="required:true" value="${chatTrade.tradeImg}"/>
	        	<input type="file"  id="tradeImgFile" style="width:155px">
	        	<!-- 原图片路径 -->
	        	<input type="hidden" id="tradeImgPathSrc" value="${chatTrade.tradeImg}"/>
	        	<!-- 裁剪后图片的路径 -->
	        	<input type="hidden" id="tradeImgPathCut" value="${chatTrade.tradeImg}"/>
	        	<!-- 表单提交时保存到数据库的字段-->
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:$('#tradeImgFile').uploadify('upload', '*');">上传文件</a> 
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#tradeImgFile').uploadify('cancel', '*');">停止上传</a> 
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#tradeImgPathCut')">预览</a>
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#tradeImgPathSrc','#tradeImgPathCut','cut','#tradeImgPath')">裁剪</a> 
            </div>
        </td>
      </tr>
      <tr>
        <th>备注</th>
        <td colspan="3"><input type="text" name="remark" style="width:80%" value="${chatTrade.remark}"/></td>
      </tr>
    </table>
    <input type="hidden" name="id" value="${chatTrade.id}"/>
  </form>
</div>
