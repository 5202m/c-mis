<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/media/mediaAdd.js" charset="UTF-8"></script>
<!-- 媒介基本信息 -->
<div>
   <form id="mediaBaseInfoForm" class="yxForm" method="post">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
     <tr>
        <th width="100%" colspan="6">
        	<div>
    			<a href="#" class="easyui-linkbutton" style="float:right;" onclick="mediaAdd.back()" data-options="iconCls:'ope-previous'" >返回</a>
    			<a href="#" class="easyui-linkbutton" style="float:right;margin-right:10px;" onclick="mediaAdd.onSaveAdd()" data-options="iconCls:'ope-submit'" >提交</a> &nbsp;&nbsp;
			</div>
        </th>
     </tr>
     <tr>
        <th width="15%">栏目<span class="red">*</span></th>
        <td width="35%">
          <input name="categoryId" class="easyui-combotree" style="width:160px;" data-options="url:'<%=request.getContextPath()%>/categoryController/getCategoryTree.do?type=2',valueField:'id',textField:'text'"/>
		</td>
        <th width="15%">发布时间<span class="red">*</span></th>
        <td width="35%">
                          从<input name="publishStartDateStr" id="publishStartDate" class="Wdate"  onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'publishEndDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px" />
                          到<input name="publishEndDateStr" id="publishEndDate" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'publishStartDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px" />
        </td>
      </tr>
      <tr>
      	<th width="15%">状态</th>
        <td width="35%">
         	<t:dictSelect id="mediaStatus" field="status" isEdit="false" isShowPleaseSelected="false"  dataList="${dictMap[dictConstant.DICT_USE_STATUS]}"/>
        </td>
        <th width="15%">应用位置<span class="red">*</span></th>
        <td width="35%">
           <select class="easyui-combotree" style="width:180px;" name="platformStr" 
		     data-options="url:'<%=request.getContextPath()%>/commonController/getPlatformList.do',cascadeCheck:false" multiple></select>
        </td>
      </tr>
      <tr>
        <th width="15%">上传媒体（图片/视频/音频）<span class="red">*</span></th>
        <td width="35%" colspan="3">
        	<div id="media_div">
	        	&nbsp;媒体路径：&nbsp;<input type="text" name="mediaUrl" id="currentMediaPath" style="margin-bottom: 5px;width:450px;" class="easyui-validatebox"
	        				data-options="required:true"/>
	        				<input type="button" value="设置链接" id="addMediaUrlHander">
	        	<input type="file"  id="mediaFileId" style="width:155px">
	        	<!-- 原图片路径 -->
	        	<input type="hidden" id="sourceMediaPath"/>
	        	<!-- 裁剪后图片的路径 -->
	        	<input type="hidden" id="cutedMediaPath"/>
	        	<!-- 表单提交时保存到数据库的字段-->
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:mediaAdd.upload();">上传文件</a> 
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#mediaFileId').uploadify('cancel', '*');">停止上传</a> 
                <a t="viewImage" class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#cutedMediaPath')">预览</a>
                <a t="cutImage" class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#sourceMediaPath','#cutedMediaPath','cut','#currentMediaPath')">裁剪</a> 
            </div>
        </td>
      </tr>
      <tr>
      	<th width="15%">点击媒体链接的URL</th>
        <td width="35%" colspan="3">
        	<input type="text" name="linkUrl" style="width: 800px;" class="easyui-validatebox" data-options="validType:'url',missingMessage:'请填入一个有效的URL'"/>
        </td>
      </tr>
      <tr id="mediaImageRowTr">
      	<th width="15%">媒体图片地址<span class="red">*</span></th>
        <td width="35%" colspan="3">
        	<div>
	        	&nbsp;图片路径：&nbsp;<input type="text" name="mediaImgUrl" id="currentMediaImagePath" style="margin-bottom: 5px;width:450px;" class="easyui-validatebox"
	        			data-options="validType:'url',missingMessage:'请填入一个有效的URL'"/>
	        	<input type="file"  id="mediaImageId" style="width:155px">
	        	<!-- 原图片路径 -->
	        	<input type="hidden" id="sourceMediaImagePath"/>
	        	<!-- 裁剪后图片的路径 -->
	        	<input type="hidden" id="cutedMediaImagePath"/>
	        	<!-- 表单提交时保存到数据库的字段-->
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:$('#mediaImageId').uploadify('upload', '*');">上传文件</a> 
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#mediaImageId').uploadify('cancel', '*');">停止上传</a> 
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#cutedMediaImagePath')">预览</a>
                <a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#sourceMediaImagePath','#cutedMediaImagePath','cut','#currentMediaImagePath')">裁剪</a> 
            </div>
        </td>
      </tr>
      <tr>
      	<th width="15%">序号</th>
        <td width="35%">
        	<input type="text" name="sequence" value="0"/>
        </td>
      	<th width="15%">所需积分</th>
        <td width="35%">
        	<input type="text" name="point" value="0"/>
        </td>
      </tr>
      <tr>
        <th width="15%">语言：<span class="red">*</span></th>
        <td width="85%" colspan="4">
          <c:forEach var="lang" items="${langMap}">
            <input type="checkbox" id="checkbox_lang_${lang.key}" value="${lang.key}" style="margin-right:10px;" tv="${lang.value}"/>${lang.value}
          </c:forEach>
        </td>
     </tr>
    </table>
  </form>
</div>
<div id="addMediaUrl" class="easyui-dialog" closed="true">
	<form>
		<table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
			<tr>
				<th>
					<input type="radio" name="addMediaUrlName" id="addMediaUrlTecent" value="http://v.qq.com/iframe/player.html">
					<label for="addMediaUrlTecent">腾讯</label>
				</th>
				<td>
					vid: <input type="text" pName="vid" style="width:250px">
				</td>
			</tr>
			<tr>
				<th>
					<input type="radio" name="addMediaUrlName" id="addMediaUrlblws" value="type=blws&">
					<label for="addMediaUrlblws">保利威视</label>
				</th>
				<td>
					vid: <input type="text" pName="vid" style="width:250px">
				</td>
			</tr>
		</table>
	</form>
</div>
<style type="text/css">
#media_tab .tabs{
  margin-left:8px;
}
</style>
<!-- 媒介详细信息 -->
<div id="media_tab" class="easyui-tabs" data-options="fit:true" style="height:700px;width:100%;"></div>
<%@ include file="detailTemplate.jsp"%>