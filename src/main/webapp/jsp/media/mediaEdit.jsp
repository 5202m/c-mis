<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/base/js/uploadify/css/uploadify.css" type="text/css">
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/base/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/media/mediaEdit.js" charset="UTF-8"></script>
<!-- 媒体基本信息 -->
<div>
   <form id="mediaBaseInfoForm" class="yxForm" method="post">
    <input type="hidden" name="id" value="${media.id}"/>
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
     <tr>
        <th width="100%" colspan="6">
        	<div>
    			<a href="#" class="easyui-linkbutton" style="float:right;" onclick="mediaEdit.back()" data-options="iconCls:'ope-previous'" >返回</a>
    			<a href="#" class="easyui-linkbutton" style="float:right;margin-right:10px;" onclick="mediaEdit.onSaveEdit()" data-options="iconCls:'ope-submit'" >提交</a> &nbsp;&nbsp;
			</div>
        </th>
     </tr>
     <tr>
        <th width="15%">栏目<span class="red">*</span></th>
        <td width="35%">
          <input name="categoryId" class="easyui-combotree"  style="width:160px;" 
           data-options="url:'<%=request.getContextPath()%>/categoryController/getCategoryTree.do',valueField:'id',textField:'text'" 
           value="${media.categoryId}">
		</td>
        <th width="15%">发布时间<span class="red">*</span></th>
        <td width="35%">
                          从<input name="publishStartDateStr" id="publishStartDate" class="Wdate" value="<fmt:formatDate value="${media.publishStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'publishEndDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px" />
                          到<input name="publishEndDateStr" id="publishEndDate" class="Wdate" value="<fmt:formatDate value="${media.publishEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'publishStartDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:140px" />
        </td>
      </tr>
      <tr>
      	<th width="15%">状态</th>
        <td width="35%">
         	<t:dictSelect id="mediaStatus" field="status" isEdit="true" defaultVal="${media.status}" isShowPleaseSelected="false"  dataList="${dictMap[dictConstant.DICT_USE_STATUS]}"/>
        </td>
        <th width="15%">应用位置<span class="red">*</span></th>
        <td width="35%">
           <select class="easyui-combotree" style="width:180px;" name="platformStr" data-options="url:'<%=request.getContextPath()%>/commonController/getPlatformList.do?platform=${media.platform}',cascadeCheck:false" multiple></select>
        </td>
      </tr>
      <tr>
        <th width="15%">上传媒体（图片/视频/音频）<span class="red">*</span></th>
        <td width="35%" colspan="3">
        	<div id="media_div">
	        	&nbsp;媒体路径：&nbsp;<input type="text" id="currentMediaPath" name="mediaUrl" value="${media.mediaUrl}" style="margin-bottom: 5px;width:450px;"/>
	        	<input type="file"  id="mediaFileId" style="width:155px">
	        	<!-- 原图片路径 -->
	        	<input type="hidden" id="sourceMediaPath" value="${media.mediaUrl}"/>
	        	<!-- 裁剪后图片的路径 -->
	        	<input type="hidden" id="cutedMediaPath" value="${media.mediaUrl}"/>
	        	<!-- 表单提交时保存到数据库的字段-->
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-upload',disabled:false"  onclick="javascript:mediaEdit.upload();">上传文件</a> 
	        	<a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false"  onclick="javascript:$('#mediaFileId').uploadify('cancel', '*');">停止上传</a> 
                <a t="viewImage" class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-view',disabled:false"  onclick="goldOfficeUtils.onViewImage('#cutedMediaPath')">预览</a>
                <a t="cutImage" class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cut',disabled:false"  onclick="goldOfficeUtils.onCutImage('#sourceMediaPath','#cutedMediaPath','cut','#currentMediaPath')">裁剪</a> 
            </div>
        </td>
      </tr>
      <tr>
      	<th width="15%">点击媒体链接的URL</th>
        <td width="35%" colspan="3">
        	<input type="text" name="linkUrl" value="${media.linkUrl}" style="width: 800px;"/>
        </td>
      </tr>
      <tr id="mediaImageRowTr" style="display:none;">
      	<th width="15%">媒体图片地址<span class="red">*</span></th>
        <td width="35%" colspan="3">
        	<div>
	        	&nbsp;图片路径：&nbsp;<input type="text" id="currentMediaImagePath" name="mediaImgUrl" value="${media.mediaImgUrl}" style="margin-bottom: 5px;width:450px;"/>
	        	<input type="file"  id="mediaImageId" style="width:155px">
	        	<!-- 原图片路径 -->
	        	<input type="hidden" id="sourceMediaImagePath" value="${media.mediaImgUrl}" />
	        	<!-- 裁剪后图片的路径 -->
	        	<input type="hidden" id="cutedMediaImagePath" value="${media.mediaImgUrl}" />
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
        <td width="35%" colspan="3">
        	<input type="text" name="sequence" value="${media.sequence}"/>
        </td>
      </tr>
      <tr>
        <th width="15%">语言：<span class="red">*</span></th>
        <td width="85%" colspan="4">
          <c:forEach var="lang" items="${langMap}">
             <input type="checkbox" id="checkbox_lang_${lang.key}" 
                    value="${lang.key}" <c:forEach var="mediaDetail" items="${media.detailList}"><c:if test="${lang.key == mediaDetail.lang}">checked="checked"</c:if></c:forEach>
                    style="margin-right:10px;" tv="${lang.value}"/>${lang.value}
          </c:forEach>
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
<!-- 媒体详细信息 -->
<div id="media_tab" class="easyui-tabs" data-options="fit:true" style="height:700px;width:100%;">
<c:forEach var="mediaDetail" items="${media.detailList}">
	<div id="media_detail_${mediaDetail.lang}" title="${langMap[mediaDetail.lang]}" style="padding:0px;overflow-x:auto;height:700px;width:100%;">
	  <form name="mediaDetailForm" class="yxForm">
	    <input type="hidden" name="lang" value="${mediaDetail.lang}"/>
	    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
	      <tr>
	      	<th width="15%">标题</th>
	        <td width="85%"><input type="text" name="title" value="${mediaDetail.title}" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入标题'" style="width:600px"/>
		    </td>
	      </tr>
	       <tr>
			<th width="15%">简介</th>
        	<td width="85%"><textarea rows="2" cols="6" name="content" style="width:600px">${mediaDetail.remark}</textarea></td>
           </tr>
           <tr>
			<th width="15%">标签</th>
	        <td width="85%"><input type="text" name="tag" value="${mediaDetail.tag}" style="width:600px"/></td>
	       </tr>
	       <tr>
			<th width="15%">SEO标题</th>
	        <td width="85%"><input type="text" name="seoTitle" style="width:600px" value="${mediaDetail.seoTitle}"/></td>
	      </tr>
	       <tr>
			<th width="15%">SEO关键字(多个用逗号分隔)</th>
	        <td width="85%"><input type="text" name="seoKeyword" value="${mediaDetail.seoKeyword}" style="width:600px"/></td>
	      </tr>
	       <tr>
			<th width="15%">SEO描述</th>
	        <td width="85%"><textarea rows="2" cols="6" name="seoDescription" style="width:600px">${mediaDetail.seoDescription}</textarea></td>
	       </tr>
	    </table>
	  </form>
	</div>
</c:forEach>
</div>
<%@ include file="detailTemplate.jsp"%>