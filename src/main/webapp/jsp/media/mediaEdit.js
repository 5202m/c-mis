/**
 * 摘要：媒体管理修改js
 * @author alan.wu
 * @date   2015/03/19
 */
var mediaEdit = {
	videoSuffix:'*.avi;*.mp4;*.rm;*.rmvb;*.wmv;*.swf;',
	imgSuffix:'*.jpg;*.gif;*.png;*.jpeg;',
	init : function(){
		this.setEvent();
		this.initUploadFile();
	},
	setEvent:function(){
		this.setLangCheck();
		//加载页面时设置作者列表
		$("#mediaBaseInfoForm input[id^=checkbox_lang_]").each(function(){
			if(this.checked){
				var title=$(this).attr("tv"),lang=this.value;
				var tabId="media_detail_"+lang;
				var tabTid="#"+tabId;
				var authorListId="media_authorList_"+lang;
			    $(tabTid+" select[name=authorAvatar]").attr("id",authorListId).attr("name",authorListId);
			    mediaEdit.setAuthorList(authorListId);
			}
		});
		$("#mediaBaseInfoForm input[name=categoryId]").combotree({
			onChange:function(newValue,oldValue){
				$('#mediaImageRowTr').hide();
				$("#media_div a[t=viewImage],#media_div a[t=cutImage]").show();
                if(newValue.indexOf("video")!=-1){
                	$('#mediaImageRowTr').show();
                	$("#media_div a[t=viewImage],#media_div a[t=cutImage]").hide();
				}
			}
		});
		

		$("#addMediaUrlHander").bind("click", function(){
			var loc_targetDom = $("#currentMediaPath");
			goldOfficeUtils.openSimpleDialog({
				dialogId : "addMediaUrl",
				title : '设置链接',
				height:130,
				onOpen : function(){
					var loc_url = loc_targetDom.val();
					if(isValid(loc_url)){
						$("#addMediaUrl input:radio").each(function(){
							if(loc_url.startsWith($(this).val())){
								$(this).prop("checked", true);
								var pDom=$(this).parent().next().find("input[pName]"),pName=pDom.attr("pName");
								var pnVal=loc_url.match(eval('/'+pName+'=([^&]+)/g'));
								if(isValid(pnVal)){
									pDom.val(pnVal.toString().replace(pName+'=',""));
									return false;
								}
							}
						});
					}
				},
				buttons	 : [{
					text : '清空',
					iconCls : "ope-close",
					handler : function() {
						$("#addMediaUrl form")[0].reset();
					}
				},{
					text : '确定',
					iconCls : "ope-save",
					handler : function(){
						var checkDom=$("#addMediaUrl input:checked"),pDom=checkDom.parent().next().find("input[pName]");
						var pVal=pDom.val(),pName=pDom.attr("pName");
						var locUrl = checkDom.val();
						if(isBlank(pVal)){
							return false;
						}
						if(locUrl.indexOf('&')==-1){
							locUrl = locUrl + '?'+pName+'='+ pVal;
						}else{
							locUrl += (/&$/g.test(locUrl)?"":"&") + pName + "=" +pVal;
						}
						loc_targetDom.val(locUrl);
						$("#addMediaUrl form")[0].reset();
						$("#addMediaUrl").dialog("close");
					}
				},{
					text : '关闭',
					iconCls : "ope-close",
					handler : function() {
						$("#addMediaUrl form")[0].reset();
						$("#addMediaUrl").dialog("close");
					}
				}]
			});
		});
	},
	/**
	 * 设置作者列表
	 */
	setAuthorList:function(id){
		var lang=id.replace("media_authorList_","");
		var author=$('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=userId]').val();
		$('#'+id).combogrid({
		    idField:'userNo',
		    textField:'userName',
		    value:author,
		    url:basePath+'/userController/getAnalystList.do?hasOther=true',
		    columns:[[
		        {field : 'userNo',hidden:true},
		        {field : 'author_Key_id',hidden:true,formatter : function(value, rowData, rowIndex) {
					return 'author_Key_id';
				}},
		        {field : 'userName',title : '姓名',width:100},
				{field : 'position', hidden:true},
		        {field : 'avatar',title : '头像',width:40,formatter : function(value, rowData, rowIndex) {
		        	if(isBlank(value)){
		        		return '';
		        	}
					return '<img src="'+value+'" style="height:35px;width:35px;"/>';
				}}
		    ]],
		    onSelect:function(rowIndex, rowData){
		       var lang=id.replace("media_authorList_","");
			   $('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=userId]').val(rowData.userNo);
			   $('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=name]').val(rowData.userName);
			   $('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=position]').val(rowData.position);
			   $('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=avatar]').val(rowData.avatar);
		    },
		    onChange:function(val){
		    	var lang=id.replace("media_authorList_","");
		    	$("td[field=author_Key_id]").parent().parent().find("td div").each(function(){
		    		if(val!=$(this).text()){
		    			$('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=name]').val(val);
			    	}
		    	});
		    }
		}); 
	},
	/**
	 * 上传文件
	 */
	upload:function(){
		var val=$("#mediaBaseInfoForm input[name=categoryId]").val();
		if(isBlank(val)){
			alert("请选择栏目！");
			return;
		}
		if(val.indexOf('advertisement')!=-1 || val=='sysPicture'){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'pic'});
		}else if(val=='student_style'){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'pic', 'saveSrc':'1'});
		}else if(val.indexOf("video")!=-1 || val.indexOf("audio")!=-1){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'video'});
		}else if(val == "download"){
			$('#mediaFileId').uploadify('settings','formData',{'fileDir' : 'attach'});
		}else{
			alert("栏目：" + val + "对应的上传目录不存在！");
			return ;
		}
		$('#mediaFileId').uploadify('upload', '*');
	},
	/**
	 * 初始化上传控件
	 */
	initUploadFile : function(){
		var suffix=mediaEdit.videoSuffix+mediaEdit.imgSuffix;
		//媒体上传(图片/视频/音频)
		goldOfficeUtils.uploadFile({
			'fileId' : 'mediaFileId',
			'formData' : {'fileDir' : ''},
			'fileSizeLimit' :300*1024*1024,
			'fileTypeDesc': '只能上传'+suffix+'格式文件',
			'fileTypeExts' : "*",
			'uploader' : basePath+'/uploadController/upload.do',
			'onUploadSuccess' : function(file, data, response){
				var d = eval("("+data+")");			//转换为json对象 
				if(d.success){
					alert(file.name + ' 上传成功！');
					if(d.obj != null){
						$("#currentMediaPath").val(d.obj);
						$("#sourceMediaPath").val(d.obj);
						$("#cutedMediaPath").val(d.obj);
					}
				}else{
					alert(file.name + d.msg);
				}
			}
		});
		//媒体图片地址
		goldOfficeUtils.uploadFile({
			'fileId' : 'mediaImageId',
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
						$("#currentMediaImagePath").val(d.obj);
						$("#sourceMediaImagePath").val(d.obj);
						$("#cutedMediaImagePath").val(d.obj);
					}
				}else{
					alert(file.name + d.msg);
				}
			}
		});
		var val=$("#mediaBaseInfoForm input[name=categoryId]").val();
		if(val.indexOf("video")!=-1){
			$('#mediaImageRowTr').show();
        	$("#media_div a[t=viewImage],#media_div a[t=cutImage]").hide();
		}
	},
	/**
	 * 设置勾选语言，显示媒体信息
	 */
	setLangCheck:function(){
		$("#mediaBaseInfoForm input[id^=checkbox_lang_]").click(function(){
			var title=$(this).attr("tv"),lang=this.value;
			var tabId="media_detail_"+lang;
			var tabTid="#"+tabId;
		    if(this.checked){
		    	$('#media_tab').tabs('add',{    
		        	  id:tabId,
	                  title: title,
	                  selected: true,
	                  content:$("#mediaDetailTemp").html()
		         });
			     $(tabTid+" form[name=mediaDetailForm] input[type=hidden][name=lang]").val(lang);
			     var authorListId="media_authorList_"+lang;
			     $(tabTid+" select[name=authorAvatar]").attr("id",authorListId).attr("name",authorListId);
			     mediaEdit.setAuthorList(authorListId);
			}else{
				 var hasVal=false;
				 $(tabTid+" input[name]").each(function(){
					if(isValid($(this).val())){
						hasVal=true;
						return false;
					}
				 });
				 if(hasVal){//判断是否有内容
					$.messager.confirm("操作提示","该语言下存在内容，确定不需要这些内容吗?", function(r) {
					   if(r){
						   $("#media_tab").tabs("close",title);
						}else{
							this.checked=true;
						}
					});
				 }else{
					$("#media_tab").tabs("close",title);
				 }
			}
		});
	},
	/**
	 * 检查表单输入框
	 */
	checkForm:function(){
	    var isPass=true;
		$("#mediaBaseInfoForm input,#mediaBaseInfoForm select").each(function(){
			if(isBlank($(this).val())){
				if($(this).attr("name")=="categoryId"){
				    alert("栏目不能为空！");
				    isPass=false;
				    return false;
				}
				if($(this).attr("name")=="publishStartDateStr"||$(this).attr("name")=="publishEndDateStr"){
					alert("发布时间不能为空！");
					isPass=false;
					return false;
				}
				if($(this).attr("name")=="platformStr"){
					alert("应用位置不能为空！");
					isPass=false;
					return false;
				}
				if($(this).attr("name")=="mediaUrl"){
					alert("媒体路径不能为空！");
					isPass=false;
					return false;
				}
			}
		});
		if(isPass && $("#mediaBaseInfoForm input[type=checkbox]:checked").length==0){
			alert("请选择语言！");
			isPass=false;
		}
		return isPass;
	},
	/**
	 * 清除无效的作者值
	 */
	checkClearAuthor:function(){
		$("input[type=hidden][name^=media_authorList_]").each(function(){
			 var lang=this.name.replace("media_authorList_","");
			 var authorDom=$('#media_detail_'+lang+' form[name=mediaDetailForm] input[name=author]');
			 if(isBlank(this.value)){
				 authorDom.val('');
			 }else{
				 if(isBlank(authorDom.val())){
					 authorDom.val(this.value);
				 }else{
					 if(this.value!=authorDom.val().split(";")[0]){
						 authorDom.val(this.value);
					 }
				 }
			 }
		});
	},
	/**
	 * 功能：修改时保存
	 */
	onSaveEdit : function(){
		if(this.checkForm() && $("#mediaBaseInfoForm").form('validate') && $("#media_tab form[name=mediaDetailForm]").form('validate')){
			this.checkClearAuthor();//清除无效的作者值
			var serializeFormData = $("#mediaBaseInfoForm").serialize();
			var detaiInfo=formFieldsToJson($("#media_tab form[name=mediaDetailForm]"));
			var detaiInfoObj = eval("("+detaiInfo+")");
			if($.isArray(detaiInfoObj)){
				$.each(detaiInfoObj, function(key, value){
					var authorInfo = {};
					authorInfo.userId = value.userId;
					authorInfo.avatar = value.avatar;
					authorInfo.position = value.position;
					authorInfo.name = value.name;
					detaiInfoObj[key].authorInfo = authorInfo;
				});
			}
			else{
				var authorInfo = {};
				authorInfo.userId = detaiInfoObj.userId;
				authorInfo.avatar = detaiInfoObj.avatar;
				authorInfo.position = detaiInfoObj.position;
				authorInfo.name = detaiInfoObj.name;
				detaiInfoObj.authorInfo = authorInfo;
			}
			detaiInfo = JSON.stringify(detaiInfoObj);
			$.messager.progress();//提交时，加入进度框
			var submitInfo = serializeFormData+"&detaiInfo="+encodeURIComponent(detaiInfo);
			getJson(formatUrl(basePath + '/mediaController/update.do'),submitInfo,function(data){
				$.messager.progress('close');
				if(data.success){
					alert("更新成功 !");
					jumpRequestPage(basePath + '/mediaController/index.do');
				}else{
					alert("更新失败，错误信息："+data.msg);
				}
			},true);
		}
	},
	/**
	 * 功能：返回到主列表页面
	 */
	back : function(){
		jumpRequestPage(basePath + '/mediaController/index.do');
	}
};
		
//初始化
$(function() {
	mediaEdit.init();
});