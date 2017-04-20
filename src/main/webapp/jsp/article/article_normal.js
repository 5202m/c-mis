var ArticleTemplate = {
	config : {},
	editorMap : {},
	/**
	 * 预览
	 * @param article
	 */
	view : function(article){
		this.initView();
		var articleInfo = ArticleEdit.article;
		var $form = $("#articleBaseInfoForm");
		$("#categoryIdTxt").text(articleInfo.categoryId);
		$form.find("#publishStartDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishStartDate));
		$form.find("#publishEndDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishEndDate));
		$form.find("#articleStatus").val(articleInfo.status);
		$form.find("#platformTxt").text(articleInfo.platform);
		$form.find("#articleSeq").val(articleInfo.sequence);
		if(article.detailList && article.detailList.length > 0){
			var detail = null;
			var $detailForm = null;
			for(var i in article.detailList){
				detail = article.detailList[i];
				$form.find("#checkbox_lang_" + detail.lang).prop("checked", true).trigger("click", {content:detail.content, isView:true}).prop("checked", true);
				$detailForm = $("#article_detail_" + detail.lang + " form");
				$detailForm.find("input[name='title']").val(detail.title);
				$detailForm.find("#authorList_" + detail.lang).html("<option>" + (detail.authorInfo ? detail.authorInfo.name : "") + "</option>");
				$detailForm.find("textarea[name='remark']").val(detail.remark);
				$detailForm.find("input[name='tag']").val(detail.tag);
				$detailForm.find("input[name='seoTitle']").val(detail.seoTitle);
				$detailForm.find("input[name='seoKeyword']").val(detail.seoKeyword);
				$detailForm.find("textarea[name='seoDescription']").val(detail.seoDescription);
			}
		}
		$("#articleBasePanel").find("input,select,textarea").prop("disabled", true);
	},
	/**
	 * 新增初始化
	 * @param article
	 */
	preAdd : function(){
		this.initView();
	},
	/**
	 * 编辑初始化
	 * @param article
	 */
	preEdit : function(article){
		this.initView();
		var articleInfo = ArticleEdit.article;
		var $form = $("#articleBaseInfoForm");
		$form.find("#articleId").val(articleInfo.id);
		$form.find("#categoryId").combotree("setValue", articleInfo.categoryId);
		$form.find("#publishStartDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishStartDate));
		$form.find("#publishEndDate").val(timeObjectUtil.longMsTimeConvertToDateTime(articleInfo.publishEndDate));
		$form.find("#articleStatus").val(articleInfo.status);
		$form.find("#platformStr").combotree('setValues', articleInfo.platform.split(","));
		$form.find("#articleSeq").val(articleInfo.sequence);
		if(article.detailList && article.detailList.length > 0){
			var detail = null;
			var $detailForm = null;
			for(var i in article.detailList){
				detail = article.detailList[i];
				$form.find("#checkbox_lang_" + detail.lang).prop("checked", true).trigger("click", {content:detail.content}).prop("checked", true);
				$detailForm = $("#article_detail_" + detail.lang + " form");
				$detailForm.find("input[name='title']").val(detail.title);
				$detailForm.find("#authorList_" + detail.lang).combogrid('setValue', detail.authorInfo ? detail.authorInfo.userId : "");
				$detailForm.find("textarea[name='remark']").val(detail.remark);
				$detailForm.find("input[name='tag']").val(detail.tag);
				$detailForm.find("input[name='seoTitle']").val(detail.seoTitle);
				$detailForm.find("input[name='seoKeyword']").val(detail.seoKeyword);
				$detailForm.find("textarea[name='seoDescription']").val(detail.seoDescription);
			}
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
		var serializeFormData = $("#articleBaseInfoForm").serialize();
		var detaiInfo=formFieldsToJson($("#article_tab form[name=articleDetailForm]"));
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
		return submitInfo;
	},
	/**销毁页面编辑器*/
	destroy : function(){
		for(var k in ArticleTemplate.editorMap){
			ArticleTemplate.editorMap[k].destroy();
		}
	},
	/**
	 * 校验数据
	 */
	validate : function(){
	    var isPass=true;
		$("#articleBaseInfoForm input,#articleBaseInfoForm select").each(function(){
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
			}
		});
		if(isPass && $("#articleBaseInfoForm input[type=checkbox]:checked").length==0){
			alert("请选择语言！");
			isPass=false;
		}
		
		if(isPass){
			isPass = $("#articleDetailForm").form('validate');
		}
		return isPass;
	},
	/**初始化元素*/
	initView : function(){
		//栏目
		$("#categoryId").combotree({
		    url:formatUrl(basePath + '/categoryController/getCategoryTree.do?type=1'),
		    valueField:'id',
		    textField:'text',
				panelWidth:180,
				onBeforeLoad: function (node, param) {
					$('#articleBaseInfoForm table tr td span.combo input').width(180);
				}
		});
		
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
		    cascadeCheck:false,
				panelWidth:180,
				onBeforeLoad: function (node, param) {
					$('#articleBaseInfoForm table tr td span.combo input').width(180);
				}
		});
		
		//语言
		html = [];
		var langMap = ArticleTemplate.config.langMap,lang;
		for(var i in langMap){
			html.push('<input type="checkbox" id="checkbox_lang_'+ i +'" value="'+ i +'" tv="'+ langMap[i] +'" />');
			html.push('<label for="checkbox_lang_'+ i +'">'+ langMap[i] +'</label>')
		}
		$("#langTd").html(html.join(""));
		
		//语言tab
		$("#article_tab").tabs({
			fit:true
		});
		
		//语言勾选
		this.setLangCheck();
	},
	/**
	 * 设置勾选语言，显示文章信息
	 */
	setLangCheck:function(){
		$("#articleBaseInfoForm input[id^=checkbox_lang_]").bind("click", function(e, params){
			params = params || {};
			content = params.content || "";
			isView = params.isView;
			var title=$(this).attr("tv"),lang=this.value;
			var _this=this;
			var tabId="article_detail_"+lang;
			var tabTid="#"+tabId;
			var editorId="article_editor_"+lang;
		    if(this.checked){
		    	$('#article_tab').tabs('add',{    
		        	  id:tabId,
	                  title: title,
	                  selected: true,
	                  content:$("#articleDetailTemp").html()
		         });
			     var authorListId="authorList_"+lang;
			     $(tabTid+" select[name=authorAvatar]").attr("id",authorListId).attr("name",authorListId);
			     if(isView){
			    	 $(tabTid+" td[tid=content]").html(content);
			     }else{
			    	 $(tabTid+" td[tid=content]").html('<script id="'+editorId+'" name="content" type="text/plain" style="width:auto;height:auto;">' + content + '</script>');
			    	 ArticleTemplate.editorMap[editorId] = UE.getEditor(editorId,{
					  		initialFrameWidth : '100%',
					  		initialFrameHeight:'200'
				  	  });
				     $(tabTid+" form[name=articleDetailForm] input[type=hidden][name=lang]").val(lang);
				     ArticleTemplate.setAuthorList(authorListId);
			     }
			}else{
				 var hasVal=false;
				 $(tabTid+" input[name!=lang]").each(function(){
					if(isValid($.trim($(this).val()))){
						hasVal=true;
						return false;
					}
				 });
				 if(hasVal){//判断是否有内容
					$.messager.confirm("操作提示","该语言下存在内容，确定不需要这些内容吗?", function(r) {
					   if(r){
						   $("#article_tab").tabs("close",title);
						   UE.delEditor(editorId);
						   delete ArticleTemplate.editorMap[editorId];
						}else{
						   _this.checked=true;
						}
					});
				 }else{
					UE.delEditor(editorId);
					delete ArticleTemplate.editorMap[editorId];
					$("#article_tab").tabs("close",title);
				 }
			}
		});
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
		        {field : 'avatar',title : '头像',width:40,formatter : function(value, rowData, rowIndex) {
		        	if(isBlank(value)){
		        		return '';
		        	}
					return '<img src="'+value+'" style="height:35px;width:35px;"/>';
				}}
		    ]],
		    onSelect:function(rowIndex, rowData){
		       var lang=id.replace("authorList_","");
			   $('#article_detail_'+lang+' form[name=articleDetailForm] input[name=userId]').val(rowData.userNo);
			   $('#article_detail_'+lang+' form[name=articleDetailForm] input[name=name]').val(rowData.userName);
			   $('#article_detail_'+lang+' form[name=articleDetailForm] input[name=position]').val(rowData.position);
			   $('#article_detail_'+lang+' form[name=articleDetailForm] input[name=avatar]').val(rowData.avatar);
		    },
		    onChange:function(val){
		    	var lang=id.replace("authorList_","");
		    	$("td[field=author_Key_id]").parent().parent().find("td div").each(function(){
		    		if(val!=$(this).text()){
		    			$('#article_detail_'+lang+' form[name=articleDetailForm] input[name=name]').val(val);
			    	}
		    	});
		    },
				panelWidth:180,
				onBeforeLoad: function (node, param) {
					$('#articleDetailForm table tr td span.combo input').width(180);
				}
		}); 
	},
	/**
	 * 清除无效的作者值
	 */
	checkClearAuthor:function(){
		$("input[type=hidden][name^=authorList_]").each(function(){
			 var lang=this.name.replace("authorList_","");
			 var authorDom=$('#article_detail_'+lang+' form[name=articleDetailForm] input[name=author]');
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
	}
};