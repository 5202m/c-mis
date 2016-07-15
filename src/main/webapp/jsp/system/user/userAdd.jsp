<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<style type="text/css">
   .header_default div{
      margin: 15px 25px 0 0;
      width: 33px;
      height: 55px;
      float: left;
   }
   .header_default div img{
      width: 33px;
      height: 46px;
   }
   .header_default div label{
      margin:0 10px;
      width: 33px;
   }
</style>
<script type="text/javascript">
//初始化
$(function() {
	//图片地址
	goldOfficeUtils.uploadFile({
		'fileId' : 'avatarId',
		'formData' : {'fileDir' : 'pic/header/chat'},
		'fileSizeLimit' : 10*1024*1024,
		'fileTypeDesc': '只能上传*.jpg;*.gif;*.png;*.jpeg类型的图片',
		'fileTypeExts' : '*.jpg;*.gif;*.png;*.jpeg',
		'uploader' : basePath+'/uploadController/upload.do',
		'onUploadSuccess' : function(file, data, response){
			var d = eval("("+data+")");			//转换为json对象 
			if(d.success){
				alert(file.name + ' 上传成功！');
				if(d.obj != null){
					$("#currentAvatarPath").val(d.obj);
					$("#sourceAvatarPath").val(d.obj);
					$("#cutedAvatarPath").val(d.obj);
				}
			}else{
				alert(file.name + d.msg);
			}
		}
	});
	$("#user_header_default div input[name=defaultHeader]").click(function(){
		$("#currentAvatarPath").val($("#user_header_default div img[t="+$(this).attr("t")+"]").attr("src"));
	});
	
	//简介图片
	goldOfficeUtils.uploadFile({
		'fileId' : 'introductionImgFile',
		'formData' : {'fileDir' : 'pic'},
		'fileSizeLimit' : 10*1024*1024,
		'fileTypeDesc': '只能上传*.jpg;*.gif;*.png;*.jpeg类型的图片',
		'fileTypeExts' : '*.jpg;*.gif;*.png;*.jpeg',
		'uploader' : basePath+'/uploadController/upload.do',
		'onUploadSuccess' : function(file, data, response){
			var d = eval("("+data+")");			//转换为json对象 
			if(d.success){
				alert(file.name + ' 上传成功！');
				if(d.obj != null){
					$("#introductionImgPath").val(d.obj);
					$("#introductionImgPathSrc").val(d.obj);
					$("#introductionImgPathCut").val(d.obj);
				}
			}else{
				alert(file.name + d.msg);
			}
		}
	});
	//微信二维码
	goldOfficeUtils.uploadFile({
		'fileId' : 'wechatCodeImgFile',
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
					$("#wechatCodeImgPath").val(d.obj);
					$("#wechatCodeImgPathSrc").val(d.obj);
					$("#wechatCodeImgPathCut").val(d.obj);
				}
			}else{
				alert(file.name + d.msg);
			}
		}
	});
});
</script>

<div style="padding:5px;overflow:hidden;">
  <form id="userAddForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
      <tr>
        <th width="15%"><spring:message code="user.no" /><!-- 账号 --><span class="red">*</span></th>
        <td width="35%"><input type="text" name="userNo" id="userNo"  class="easyui-validatebox" 
		          	    data-options="required:true,validType:'userno',missingMessage:'<spring:message code="user.valid.no" />'"/></td>
        <th width="15%"><spring:message code="user.name" /><!-- 姓名 --><span class="red">*</span></th>
        <td width="35%"><input type="text" name="userName" id="userName"  class="easyui-validatebox" 
		          	    data-options="required:true,missingMessage:'<spring:message code="user.valid.name" />'"/></td>
      </tr>
      <tr>
        <th><spring:message code="user.pass" /><!-- 密码 --><span class="red">*</span></th>
        <td><input type="password" name="password" id="pwd" class="easyui-validatebox"
        				data-options="required:true,missingMessage:'<spring:message code="user.valid.pass" />'"/></td>
        <th><spring:message code="user.confirmpass" /><!-- 确认密码 --><span class="red">*</span></th>
        <td><input type="password"  id="confirmPwd" class="easyui-validatebox" 
        				data-options="required:true,validType:'same[\'pwd\']',missingMessage:'<spring:message code="user.valid.confirmpass" />'"/></td>
      </tr>
      <tr>
        <th><spring:message code="user.phone" /><!-- 手机号 --><span class="red">*</span></th>
        <td><input type="text" name="telephone" id="telephone" class="easyui-validatebox" readonly="readonly"
        				data-options="required:true,validType:'mobile',missingMessage:'<spring:message code="user.valid.phone" />'" value="${telephone}" /></td>
        <th><spring:message code="user.email" /><!-- Email --></th>
        <td><input type="text" name="email" id="email" class="easyui-validatebox" 
        				data-options="required:true,validType:'email',missingMessage:'<spring:message code="user.valid.email" />'"/></td>
      </tr>
      <tr>
        <th><spring:message code="user.role" /><!-- 所属角色 --></th>
        <td>
         	<select id="roleId" name="role.roleId" style="width: 155px;">
         		<option value=""><spring:message code="common.pleaseselect" /><!-- 请选择 --></option>
         		<c:forEach var="role" items="${roleList}">
      				<option value="${role.roleId}">${role.roleName}【${role.remark}】</option>
      			</c:forEach>   		
         	</select>
        </td>
        <th><spring:message code="common.status" /><!-- 状态 --></th>
        <td>
        	<select id="status" name="status" style="width: 155px;">
         		<option value="0"><spring:message code="common.enabled" /><!-- 启用 --></option>
         		<option value="1"><spring:message code="common.disabled" /><!-- 禁用 --></option>
         	</select>
        </td>
      </tr>
      
      <tr>
        <th>微信号</th>
        <td>
         	<input type="text" name="wechatCode" id="wechatCode" value="${wechatCode}" />
        </td>
        <th>胜率<!-- 胜率 --></th>
        <td>
        	<input type="text" name="winRate" id="winRate" value="${winRate}" />
        </td>
      </tr>
      
      <tr>
        <th><spring:message code="user.position" /><!-- 职位 --><span class="red">*</span></th>
        <td colspan="3"><input type="text" name="position" id="position" class="easyui-validatebox" 
        		data-options="required:true,missingMessage:'<spring:message code="user.valid.position" />'"/></td>
      </tr>
      <tr>
        <th>头像</th>
        <td colspan="3">
          <div id="user_header_tab" class="easyui-tabs" data-options="fit:true" style="height:180px;width:300px;margin-top:2px;">
			   <div id="user_header_default" title="选择默认" style="padding:0px;height:180px;" class="header_default">
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_admin1.png"   t="1"/>
				       <label><input type="radio" name="defaultHeader" t="1"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_admin2.png"   t="2"/>
				       <label><input type="radio" name="defaultHeader" t="2"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst1.png" t="3"/>
				       <label><input type="radio" name="defaultHeader" t="3"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst2.png" t="4"/>
				       <label><input type="radio" name="defaultHeader" t="4"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst3.png" t="5"/>
				       <label><input type="radio" name="defaultHeader" t="5"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst4.png" t="6"/>
				       <label><input type="radio" name="defaultHeader" t="6"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst5.png" t="7"/>
				       <label><input type="radio" name="defaultHeader" t="7"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst6.png" t="8"/>
				       <label><input type="radio" name="defaultHeader" t="8"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_admin3.png"   t="9"/>
				       <label><input type="radio" name="defaultHeader" t="9"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_admin4.png"   t="10"/>
				       <label><input type="radio" name="defaultHeader" t="10"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst7.png" t="11"/>
				       <label><input type="radio" name="defaultHeader" t="11"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst8.png" t="12"/>
				       <label><input type="radio" name="defaultHeader" t="12"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst9.png" t="13"/>
				       <label><input type="radio" name="defaultHeader" t="13"/></label>
			     	</div>
			     	<div>
				       <img src="${filePath}/upload/pic/header/chat/201508/20150817140000_analyst10.png" t="14"/>
				       <label><input type="radio" name="defaultHeader" t="14"/></label>
			     	</div>
			   </div>
			  <div id="user_header_upload" title="本地上传" style="padding:0px;height:180px;">
			        <div>图片路径：<input type="text" id="currentAvatarPath" name="avatar" style="width:350px;margin-top:5px;"/>
			        	<input type="file"  id="avatarId" style="width:155px">
			        	<!-- 原图片路径 -->
			        	<input type="hidden" id="sourceAvatarPath"/>
			        	<!-- 裁剪后图片的路径 -->
			        	<input type="hidden" id="cutedAvatarPath"/>
			        	<!-- 表单提交时保存到数据库的字段-->
			        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:$('#avatarId').uploadify('upload', '*');">上传文件</a> 
			        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#avatarId').uploadify('cancel', '*');">停止上传</a> 
		                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#cutedAvatarPath')">预览</a>
		                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#sourceAvatarPath','#cutedAvatarPath','cut','#currentAvatarPath')">裁剪</a> 
		            </div>
			  </div>
		 </div>
        </td>
      </tr>
      <tr>
        <th>简介</th>
        <td colspan="3"><textarea name="introduction" rows="5" cols="76"></textarea></td>
      </tr>
      <tr>
        <th>简介图片</th>
        <td colspan="3">
        	<div>图片路径：<input type="text" id="introductionImgPath" name="introductionImg" style="width:350px;margin-top:5px;"/>
	        	<input type="file"  id="introductionImgFile" style="width:155px">
	        	<!-- 原图片路径 -->
	        	<input type="hidden" id="introductionImgPathSrc"/>
	        	<!-- 裁剪后图片的路径 -->
	        	<input type="hidden" id="introductionImgPathCut"/>
	        	<!-- 表单提交时保存到数据库的字段-->
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:$('#introductionImgFile').uploadify('upload', '*');">上传文件</a> 
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#introductionImgFile').uploadify('cancel', '*');">停止上传</a> 
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#introductionImgPathCut')">预览</a>
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#introductionImgPathSrc','#introductionImgPathCut','cut','#introductionImgPath')">裁剪</a> 
            </div>
        </td>
      </tr>
      <tr>
        <th>微信二维码</th>
        <td colspan="3">
        	<div>图片路径：<input type="text" id="wechatCodeImgPath" name="wechatCodeImg" style="width:350px;margin-top:5px;"/>
	        	<input type="file"  id="wechatCodeImgFile" style="width:155px">
	        	<!-- 原图片路径 -->
	        	<input type="hidden" id="wechatCodeImgPathSrc"/>
	        	<!-- 裁剪后图片的路径 -->
	        	<input type="hidden" id="wechatCodeImgPathCut"/>
	        	<!-- 表单提交时保存到数据库的字段-->
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:$('#wechatCodeImgFile').uploadify('upload', '*');">上传文件</a> 
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#wechatCodeImgFile').uploadify('cancel', '*');">停止上传</a> 
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#wechatCodeImgPathCut')">预览</a>
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#wechatCodeImgPathSrc','#wechatCodeImgPathCut','cut','#wechatCodeImgPath')">裁剪</a> 
            </div>
        </td>
      </tr>
      
      <tr>
        <th>备注</th>
        <td colspan="3"><input type="text" name="remark" size="100"/></td>
      </tr>
    </table>
  </form>
</div>
  
