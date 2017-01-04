var ArticleTemplate = {
	config : {},
	editor : null,
	
	/**
	 * 预览
	 * @param article
	 */
	view : function(article){
		var detail = {};
		if(article.detailList && article.detailList.length > 0){
			detail = article.detailList[0];
		}
		this.initView(detail.content, true);
		var articleInfo = ArticleEdit.article;
		var $form = $("#articleBaseInfoForm");
		$form.find("#articleId").val(articleInfo.id);
		$form.find("#publishStartDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishStartDate));
		$form.find("#publishEndDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishEndDate));
		$form.find("#articleStatus").val(articleInfo.status);
		$form.find("#platformTxt").text(articleInfo.platform);
		$form.find("#articleSeq").val(articleInfo.sequence);
		if(detail){
			var $detailForm = $("#article_detail_zh");
			$detailForm.find("input[name='title']").val(detail.title);
			$detailForm.find("#authorList_zh").html("<option>" + (detail.authorInfo ? detail.authorInfo.name : "") + "</option>");
			$detailForm.find("select[name='tag']").val(detail.tag);
			$detailForm.find("input[name='remark']").val(detail.remark);
			var $tagTab = $('#'+detail.tag);
			var remarkArr = eval('('+detail.remark+')');
			if(remarkArr.length>0){
				$.each(remarkArr, function(i, row){
					$tagTab.find('tr:first td:last .ope-add').click();
					$.each(row, function(key, val){
						$tagTab.find('tr:eq('+i+') td select[name="'+key+'"]').val(val);
						$tagTab.find('tr:eq('+i+') td input[name="'+key+'"]').val(val);
					});
				});
				$tagTab.find('tr:last td:last .ope-remove').click();
			}
			$detailForm.find("select[name='tag']").trigger('change', "R");
		}
		$("#articleBasePanel").find("input,select,textarea").prop("disabled", true);
		$("#sendSubscribeBtn").prop("disabled", false);
	},
	/**
	 * 新增初始化
	 * @param article
	 */
	preAdd : function(){
		this.initView('', false);
		$("#article_detail_zh select[name='tag']").trigger('change', "C");
	},
	/**
	 * 编辑初始化
	 * @param article
	 */
	preEdit : function(article){
		var detail = {};
		if(article.detailList && article.detailList.length > 0){
			detail = article.detailList[0];
		}
		this.initView(detail.content, false);
		var articleInfo = ArticleEdit.article;
		var $form = $("#articleBaseInfoForm");
		$form.find("#articleId").val(articleInfo.id);
		$form.find("#publishStartDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishStartDate));
		$form.find("#publishEndDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishEndDate));
		$form.find("#articleStatus").val(articleInfo.status);
		$form.find("#platformStr").combotree('setValues', articleInfo.platform.split(","));
		$form.find("#articleSeq").val(articleInfo.sequence);
		if(detail){
			var $detailForm = $("#article_detail_zh");
			$detailForm.find("input[name='title']").val(detail.title);
			$detailForm.find("#authorList_zh").combogrid('setValue', detail.authorInfo ? detail.authorInfo.userId : "");
			$detailForm.find("select[name='tag']").val(detail.tag);
			$detailForm.find("input[name='remark']").val(detail.remark);
			var $tagTab = $('#'+detail.tag);
			var remarkArr = eval('('+detail.remark+')');
			if(remarkArr.length>0){
				$.each(remarkArr, function(i, row){
					$tagTab.find('tr:first td:last .ope-add').click();
					$.each(row, function(key, val){
						$tagTab.find('tr:eq('+i+') td select[name="'+key+'"]').val(val);
						$tagTab.find('tr:eq('+i+') td input[name="'+key+'"]').val(val);
					});
				});
				$tagTab.find('tr:last td:last .ope-remove').click();
			}
			$detailForm.find("select[name='tag']").trigger('change', "U");
		}
	},
	/**
	 * 提取页面文档数据(新增保存和修改保存，校验失败返回null)
	 */
	getArticle : function(){
		if(!this.validate()){
			return false;
		}
		this.checkClearAuthor();//清除无效的作者值
		if(isValid($('#article_detail_zh #tag').val())){
			var remark = [];
			$('#'+$('#article_detail_zh #tag').val()+' tr').each(function(){
				var row = {};
				$(this).find('select').each(function(){
					if(isValid($(this).val())){
						row[$(this).attr('name')] = $(this).val();
						if($(this).attr('name')=='symbol'){
							row['name'] = $(this).find('option:selected').text();
						}
					}
				});
				$(this).find('input').each(function(){
					if(isValid($(this).val())){
						row[$(this).attr('name')] = $(this).val();
					}
				});
				if(!isEmptyObject(row)){
					remark.push(row);
				}
			});
			$('#remark').val(JSON.stringify(remark));
		}
		var serializeFormData = $("#articleBaseInfoForm").serialize();
		var detaiInfo=formFieldsToJson($("#article_detail_zh"));
		var detaiInfoObj = eval("("+detaiInfo+")");
		if($.isArray(detaiInfoObj)){
			$.each(detaiInfoObj, function(key, value){
				var authorInfo = {};
				authorInfo.userId = value.userId;
				authorInfo.avatar = value.avatar;
				authorInfo.position = value.position;
				authorInfo.name = value.name;
				authorInfo.tag = value.usertag.replace('，',',');
				this.filterContent(value);
				detaiInfoObj[key].authorInfo = authorInfo;
			});
		}else{
			var authorInfo = {};
			authorInfo.userId = detaiInfoObj.userId;
			authorInfo.avatar = detaiInfoObj.avatar;
			authorInfo.position = detaiInfoObj.position;
			authorInfo.name = detaiInfoObj.name;
			authorInfo.tag = detaiInfoObj.usertag.replace('，',',');
			detaiInfoObj.authorInfo = authorInfo;
			this.filterContent(detaiInfoObj);
		}
		detaiInfo = JSON.stringify(detaiInfoObj);
		if($("#sendSubscribe").is(":visible") && $("#sendSubscribe").prop("checked")){
			serializeFormData += "&sendSubscribe=1";
		}
		$.messager.progress();//提交时，加入进度框
		var submitInfo = serializeFormData+"&detaiInfo="+encodeURIComponent(detaiInfo);
		return submitInfo;
	},
	/**销毁页面编辑器*/
	destroy : function(){
		if(ArticleTemplate.editor){
			ArticleTemplate.editor.destroy();
		}
	},
	/**
	 * 校验数据
	 */
	validate : function(){
	    var isPass=true;
		$("#articleBaseInfoForm input,#articleBaseInfoForm select").each(function(){
			if(isBlank($(this).val())){
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
			}
		});
		if(isValid($('#article_detail_zh #tag').val())){
			$('#'+$('#tag').val()+' input,#'+$('#tag').val()+' select').each(function(){
				if(isBlank($(this).val())){
					if($(this).attr("name")=="symbol" && $('#tag').val()=='shout_single'){
						alert("请选择品种！");
						isPass=false;
						return false;
					}
					/*if($(this).attr("name")=="support_level"){
						alert("支撑位不能为空！");
						isPass=false;
						return false;
					}*/
					if($(this).attr("name")=="longshort"){
						alert("请选择方向！");
						isPass=false;
						return false;
					}
					if($(this).attr("name")=="point"){
						alert("进场点位不能为空！");
						isPass=false;
						return false;
					}
					if($(this).attr("name")=="profit"){
						alert("止盈不能为空！");
						isPass=false;
						return false;
					}
					if($(this).attr("name")=="loss"){
						alert("止损不能为空！");
						isPass=false;
						return false;
					}
				}
			});
		}
		if(isPass){
			isPass = $("#article_detail_zh").form('validate');
		}
		return isPass;
	},
	/**初始化元素*/
	initView : function(content, isView){
		//状态
		var html = [];
		var statuses = ArticleTemplate.config.dictMap[ArticleTemplate.config.dictConstant.DICT_USE_STATUS];
		for(var i in statuses){
			html.push('<option value="' + statuses[i].code + '">' + statuses[i].name + '</option>')
		}
		$("#articleStatus").html(html.join(""));
		
		//应用位置
		$("#platformStr").combotree({
		    url:formatUrl(basePath + '/commonController/getPlatformList.do'),
		    cascadeCheck:false
		});
		
		content = content || "";
		if(isView){
			$("#tradeNodeRoomId").before("--").remove();
			//编辑器
			$("#article_detail_zh td[tid=content]").html(content)
		}else{
			//初始化房间
			var chatGroupList = ArticleTemplate.config.chatGroupList;
			html = [];
			for(var i in chatGroupList){
				html.push('<option value="' + chatGroupList[i].id + '" gt="' + (chatGroupList[i].groupType || '') + '">' + chatGroupList[i].name + '</option>')
			}
			$("#tradeNodeRoomId").append(html.join(""));
			
			//选择房间，拉取课程表信息
			$("#tradeNodeRoomId").bind("change", function(){
				var ops = $(this).find(":selected");
				ArticleTemplate.getSingleCourse(ops.attr("gt"), ops.val());
			});
			
			//作者
			ArticleTemplate.setAuthorList('authorList_zh');
			
			//编辑器
			$("#article_detail_zh td[tid=content]").html('<script id="article_editor_zh" name="content" type="text/plain" style="width:auto;height:auto;">' + content + '</script>');
			ArticleTemplate.editor = UE.getEditor("article_editor_zh", {
				initialFrameWidth : '100%',
				initialFrameHeight : '400'
			});
		}
		
		//模板切换
		$('#tag').bind("change", function(e, opType){
			opType = opType || $(this).data("opType");
			$(this).data("opType", opType);
			if(opType == "U"){
				$("#sendSubscribe,#sendSubscribeLabel,#sendSubscribeBtn").hide();
			}else if(opType == "R"){
				$("#sendSubscribe,#sendSubscribeLabel").hide();
				$("#sendSubscribeBtn").show();
			}else{//C
				var tag = $(this).val();
				if(!tag){
					$("#sendSubscribe,#sendSubscribeLabel,#sendSubscribeBtn").hide();
				}else{
					$("#sendSubscribe,#sendSubscribeLabel").show();
					$("#sendSubscribeBtn").hide();
				}
			}
			$('.tag_tab').hide();
			$('#'+$(this).val()).show();
		});
		
		
		$('.tag_tab .ope-add').click(function(){
			$('#'+$('#tag').val()+' tr:first').clone().appendTo($('#'+$('#tag').val()));
			$('#'+$('#tag').val()+' tr:last').find('input').val('');
			$('#'+$('#tag').val()+' tr:last td:last .ope-add').hide();
			$('#'+$('#tag').val()+' tr:last td:last .ope-remove').show();
		});
		
		$('.tag_tab .ope-remove').live('click', function(){
			$(this).parent().parent().remove();
		});
		
		/**发送订阅通知*/
		$("#sendSubscribeBtn").bind("click", function(){
			var articleId = $("#articleBaseInfoForm #articleId").val();
			goldOfficeUtils.ajax({
				url : basePath +'/articleController/sendSubscribe.do?articleId='+articleId,
				success : function(data){
					alert(data.msg);
				}
			});
		});
	},
	/**
	 * 拉取课程表信息
	 * @param groupType
	 * @param groupId
	 */
	getSingleCourse:function(groupType, groupId){
		if(groupType && groupId){
			var loc_url = ArticleTemplate.config.pmApiCourseUrl + "?flag=S&groupType=" + groupType + "&groupId=" + groupId;
			$.getJSON(loc_url,function(data){
				if(data && data.result == "0" && data.data && data.data.length > 0){
					var course = data.data[0];
					var $form = $("#articleBaseInfoForm");
					var dateStr = timeObjectUtil.longMsTimeConvertToDate(course.date);
					$form.find("#publishStartDate").val(dateStr + " " + course.startTime + ":00");
					$form.find("#publishEndDate").val(dateStr + " " + course.endTime + ":00");
					$form.find("#platformStr").combotree('setValue', groupId);
					$form = $("#article_detail_zh");
					$form.find("input[name='title']").val(course.title);
					if(course.lecturerId){
						var lecturerId = course.lecturerId.replace(/,.*/g, "");
						$form.find("#authorList_zh").combogrid('setValue', lecturerId);
					}
				}else{
					alert("未找到相应课程信息！");
					$("#tradeNodeRoomId option:first").prop("selected", true);
				}
			});
		}
	},
	/**
	 * 设置作者列表
	 * @param id
	 */
	setAuthorList:function(id){
		$('#'+id).combogrid({
		    idField:'userNo',
		    textField:'userName',
		    url:basePath+'/userController/getAnalystList.do?hasOther=true',
		    columns:[[
		        {field : 'userNo',hidden:true},
		        {field : 'author_Key_id',hidden:true,formatter : function(value, rowData, rowIndex) {
					return 'author_Key_id';
				}},
		        {field : 'userName',title : '姓名',width:100},
				{field : 'position',hidden:true},
				{field : 'tag',hidden:true},
		        {field : 'avatar',title : '头像',width:40,formatter : function(value, rowData, rowIndex) {
		        	if(isBlank(value)){
		        		return '';
		        	}
					return '<img src="'+value+'" style="height:35px;width:35px;"/>';
				}}
		    ]],
		    onSelect:function(rowIndex, rowData){
		       var lang=id.replace("authorList_","");
			   $('#article_detail_'+lang+' input[name=userId]').val(rowData.userNo);
			   $('#article_detail_'+lang+' input[name=name]').val(rowData.userName);
			   $('#article_detail_'+lang+' input[name=position]').val(rowData.position);
			   $('#article_detail_'+lang+' input[name=avatar]').val(rowData.avatar);
			   $('#article_detail_'+lang+' input[name=usertag]').val(rowData.tag);
		    },
		    onChange:function(val){
		    	var lang=id.replace("authorList_","");
		    	$("td[field=author_Key_id]").parent().parent().find("td div").each(function(){
		    		if(val!=$(this).text()){
		    			$('#article_detail_'+lang+' input[name=name]').val(val);
			    	}
		    	});
		    }
		}); 
	},
	/**
	 * 清除无效的作者值
	 */
	checkClearAuthor:function(){
		$("input[type=hidden][name^=authorList_]").each(function(){
			 var lang=this.name.replace("authorList_","");
			 var authorDom=$('#article_detail_'+lang+' input[name=author]');
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
	 * 过滤文档内容特殊字符
	 * @param detail
	 */
	filterContent : function(detail){
		if(detail){
			detail.content = detail.content || "";
			detail.content = detail.content.replace(/[\r\n]/g, "");
			detail.content = detail.content.replace(/(<p>(\s*<br\/?>\s*)*<\/p>)*$/g, "");
		}
	}
};
