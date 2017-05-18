/**
 * main类
 */
var main={
	init:function(){
		var nowDate = new Date();
		var year = nowDate.getFullYear();
		var month = (nowDate.getMonth() + 1) < 10 ? '0' + (nowDate.getMonth() + 1) : (nowDate.getMonth() + 1);
		var day = nowDate.getDate() < 10 ? '0' + nowDate.getDate() : nowDate.getDate();
		$('#yxui_main_today').html(year + '-' + month + '-' + day);
		$('#yxui_main_fullyear').html(year);
		var westUrl = basePath+'/menuController/getMenuRoleTree.do';
		var $west = $('#yxui_main_accordion');
		var $center = $('#yxui_main_tabs');
		var $centerMenu = $('#yxui_main_tabs_menu');
		/*
		 * center init
		 */
		$center.tabs({
					fit : true,
					border : false,
					onContextMenu : function(e, title) {
						e.preventDefault();
						$centerMenu.menu('show', {
									left : e.pageX,
									top : e.pageY
								}).data('tabTitle', title);
					},
					onClose:function(title){
						if(title=="聊天室"){
							if(adminChat!=null && adminChat.intervalId!=null){
								 clearInterval(adminChat.intervalId);
							}
						}
					}
				});
		/*
		 * centerMenu init
		 */
		$centerMenu.menu({
					onClick : function(item) {
						var title = $(this).data('tabTitle');
						var type = $(item.target).attr('type');
						if (type === 'refresh') {
							centerRefreshTab(title);
							return;
						}
						if (type === 'close') {
							var t = $center.tabs('getTab', title);
							if (t.panel('options').closable) {
								$center.tabs('close', title);
							}
							return;
						}
						var allTabs = $center.tabs('tabs');
						var closeTabsTitle = [];
						$.each(allTabs, function() {
									var opt = $(this).panel('options');
									if (opt.closable && opt.title != title && type === 'closeOther') {
										closeTabsTitle.push(opt.title);
									} else if (opt.closable && type === 'closeAll') {
										closeTabsTitle.push(opt.title);
									}
								});
						for (var i = 0; i < closeTabsTitle.length; i++) {
							$center.tabs('close', closeTabsTitle[i]);
						}
					}
				});

		/*
		 * west 请求数据 init
		 */
		$.ajax({
					url : westUrl,
					type : 'post',
					dataType : 'json',
					cache : false,
					success : function(data) {
						if (data.length > 0) {
							for (var i = 0; i < data.length; i++) {
								var selected = false;
								/*if (i == 0) {// 默认打开的菜单
									selected = true;
								}*/
								$west.accordion('add', {
											title : data[i].title,
											selected : selected,
											content : westTreeInit(data[i].child)
										});
							}
						}
					},
					error : function() {
						$.messager.alert($.i18n.prop("tips.systemtips"), $.i18n.prop("main.tips.loaderror")); //系统提示，加载错误
					}
				});
		/*
		 * accordion 树初始化
		 */
		westTreeInit = function(data) {
			var $ulTree = $('<ul/>').tree({
						data : data,
						onClick : westTreeClick,
						onDblClick : westTreeDbClick
					});
			return $ulTree;
		};
		/*
		 * 树单击事件
		 */
		westTreeClick = function(node) {
			// 先判断session是否过期,如果过期,则跳转到登录页面
			$.ajax({
				url : basePath+'/sessionValid.do',
				type : 'get',
				cache : false,
				success : function(data) {
					if(data.success) {
						if (node.attributes && node.attributes.url && node.attributes.url.length > 0) {
							centerTabsAddTab(node);
						} else {
							westTreeParentClick(this, node);
						}
					}else{
						alert($.i18n.prop("main.sessiontimeout")); //对不起，会话已经过期,请重新登录!
						top.location.href = basePath+'/login.do';
					}
				}
			});
		};
		/*
		 * 树双击方法
		 */
		westTreeDbClick = function(node) {
			if (node.state == 'closed') {
				$(this).tree('expand', node.target);
			} else {
				$(this).tree('collapse', node.target);
			}
		};
		/*
		 * 树父节点单击方法
		 */
		westTreeParentClick = function(tree, node) {
			if (node.state == 'closed') {
				$(tree).tree('expand', node.target);
			} else {
				$(tree).tree('collapse', node.target);
			}
		};
		/*
		 * center新增tab
		 */
		centerTabsAddTab = function(node) {
			if ($center.tabs('exists', node.text)) {
				$center.tabs('select', node.text);
			} else {
				var tabsLength = 4;// 最大页签数量
				if ($center.tabs('tabs').length > tabsLength) {
					//您打开的页面过多,为提高访问速度,系统默认允许打开
					$.messager.alert($.i18n.prop("main.tips.systemtips"),  $.i18n.prop("main.tabtips")+ tabsLength + '个页签!', 'info');
				} else {
					if (node.attributes && node.attributes.url && node.attributes.url.length > 0) {
						var url = node.attributes.url;
						if(url != null && url.indexOf("?") == -1){
							url += "?menuId=" + node.id;
						}else{
							url += "&menuId=" + node.id;
						}
						$center.tabs('add', {
							title : node.text,
							id:node.id,
							iconCls : node.iconCls ? node.iconCls : 'res-s-014',
							closable : true,
							href : url,
							onLoad:function(){    //按钮权限控制
								var result = getJson(basePath+"/authorityController/getFuns.do",{menuId:node.id},null);
								var obj = result.obj;
							    if(validObj(obj)){
										$('#operateType').empty();
							       for(var i=0;i<obj.length;i++){
							    	   $("[class~="+obj[i].code+"]","#"+node.id).show();
											 if(node.attributes.url == 'sysLogController/index.do'){
												 $('#operateType').append('<option value="'+obj[i].code+'">'+obj[i].nameCN+'</option>');
											 }
							       }
							       if(node.attributes.url == 'sysLogController/index.do'){
											 systemLog.initGrid();
										 }
							    }
						   }
						});
					}
				}
			}
		};
		/*
		 * center更新Tab
		 */
		centerRefreshTab = function(title) {
			var tab = $center.tabs('getTab', title);
			var options = tab.panel('options');
			if (options.content == null) {
				tab.panel('refresh');
			} else {
				$center.tabs('update', {
							tab : tab,
							options : options
						});
			}
		};

	}
};
/*
 * init
 */
$(function() {
   main.init();
});

/*
 * 修改密码
 */
function updatePwd() {
	var url = basePath+'/loginController/pwdChange.do';
	var submitUrl =  basePath+'/loginController/doPwdChange.do';
	goldOfficeUtils.openEditorDialog({
		title : '修改密码',
		width : 480,
		height : 190,
		href : url,
		iconCls : 'pag-edit',
		handler : function(){   //提交时处理
			if($("#pwdchangeForm").form('validate')){
				if($("#newPwd").val() != $("#repePwd").val()){
					alert("密码不一致！");
					$("#repePwd").focus();
					return;
				}
				goldOfficeUtils.ajaxSubmitForm({
					url : submitUrl,
					formId : 'pwdchangeForm',
					onSuccess : function(data){  //提交成功后处理
						var d = $.parseJSON(data);
						if (d.success) {
							$("#myWindow").dialog("close");
							$.messager.alert($.i18n.prop("common.operate.tips"),'修改成功!','info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败，原因：'+d.msg,'error');
						}
					}
				});
			}
		}
	});
};
/*
 * 退出登录
 */
function loginOut() {
	$.messager.confirm($.i18n.prop("tips.systemtips"), $.i18n.prop("comfirmlogout"), function(r) {
		if (r) {
			window.location = basePath+"/logout.do";
		}
	});
};

/**
 * 功能：帮助页面
 */
function helpOpen(){
	var p = parent.yxui.dialog({
		title : $.i18n.prop("main.help"),
		//iconCls : 'icon-help',
		href : 'helpContent.html',
		width : 700,
		height : 500
	});
};

/**
 * 功能：版本信息
 */
function versionOpen(){
	var p = parent.yxui.dialog({
		title : $.i18n.prop("main.version"),
		//iconCls : 'pag-version',
		href : 'version.html',
		width : 700,
		height : 500
	});
};

/**
 * 功能：问题反馈
 */
function feedbackOpen(){
	var p = parent.yxui.dialog({
		title : $.i18n.prop("main.feedback"),
		//iconCls : 'pag-version',
		href : 'feedback.html',
		width : 700,
		height : 450,
		buttons : [{
					text : $.i18n.prop("buttons.submit"),
					iconCls : 'ope-save',
					handler : function() {
						$.messager.alert($.i18n.prop("tips.systemtips"), $.i18n.prop("savesuccess"));
					}
				}, {
					text : $.i18n.prop("buttons.cancel"),
					iconCls : 'ope-close',
					handler : function() {
						p.dialog('close');
					}
				}]
	});
};