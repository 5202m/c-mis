/**
 * 摘要：用户管理公用js
 * @author Gavin.guo
 * @date   2014-10-14
 */
var chatAnalyst = {
	liveLinks:{
		obsUrlIndex:["playlist.m3u8","index.m3u8","playlist.m3u8"],
		obsLabel:{
			main_sz:{name:'大陆{0},房间:{1}',url:['sz.hhkcdn.com/live']},
			main_hk:{name:'海外{0},房间:{1}',url:['hk.hhkcdn.com/live']},
			other_sz:{name:'大陆(备用{0}),房间:{1}',url:['sz.vpcdn.com/live','sz6.phgse.cn/live','ct1.phgsa.cn:1935/live']},
			other_hk:{name:'海外(备用{0}),房间:{1}',url:['hk.vpcdn.com/live','h6.phgse.cn/live','ct.phgsa.cn:1935/live']}
		},
		studio:{
			obsCode:["01","02","05"],
			yyLink:[
				{code:1,name:'yy:92628431/92628431',url:'http://yy.com/s/92628431/92628431/yyscene.swf'},
				{code:1,name:'yy:92628431/2439533296',url:'http://yy.com/s/92628431/2439533296/yyscene.swf'},
			]
		},
		fxstudio:{
			obsCode:["03","04"],
			yyLink:[
				{code:1,name:'yy:92628431/2537339360',url:'http://yy.com/s/92628431/2537339360/yyscene.swf'},
				{code:1,name:'yy:92628431/2559592756',url:'http://yy.com/s/92628431/2559592756/yyscene.swf'}
			]
		},
		hxstudio:{
			obsCode:["10","11","12"],
			yyLink:[
				{code:1,name:'yy:46327234/46327234',url:'http://yy.com/s/46327234/46327234/yyscene.swf'},
				{code:1,name:'yy:82992602/82992602',url:'http://yy.com/s/82992602/82992602/yyscene.swf'}
			]
		},
		oneTVLink:{code:1,name:'oneTV',url:'http://www.one-tv.com/stream/live_mpegts.swf'}
	},
	gridId : 'chat_analyst_datagrid',
	opType : '',
	init : function(){
		this.opType = $("#userOpType").val();
		this.initGrid();
		this.setEvent();
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : chatAnalyst.gridId,
			idField:"userId",
			sortName : 'userId',
			singleSelect : false,
			url : basePath+'/analystController/datagrid.do',
			columns : [[
			            {title : 'userId',field : 'userId',checkbox : true},
			            {title : $.i18n.prop("common.operate"),field : 'todo',formatter : function(value, rowData, rowIndex) {		/**操作*/
							$("#chat_analyst_datagrid_rowOperation a").each(function(){
								$(this).attr("id",rowData.userId);
						    });
							return $("#chat_analyst_datagrid_rowOperation").html();
						}},
			            {title : $.i18n.prop("user.no"),field : 'userNo'},                   		/**账号*/
			            {title : $.i18n.prop("user.name"),field : 'userName',sortable : true},		/**姓名*/
			            {title : $.i18n.prop("user.email"),field : 'email'},						/**Email*/
						{title : $.i18n.prop("user.phone"),field : 'telephone',sortable : true},	/**手机号*/		
						{title : $.i18n.prop("user.role"),field :'role',sortable : true,formatter : function(value, rowData, rowIndex) {
							if(value){
								return value.roleName;
							}
						}},   	/**所属角色*/
						{title : $.i18n.prop("common.status"),field : 'status',sortable : true,formatter : function(value, rowData, rowIndex) {/**状态*/
							if (value == 0) {
								return $.i18n.prop("common.enabled");
							} else {
								return $.i18n.prop("common.disabled");
							}}}, 
						{title : $.i18n.prop("user.position"),field : 'position',sortable : true},  	/**职位*/
						{title : $.i18n.prop("user.logintime"),field : 'loginDate',sortable : true,formatter : function(value, rowData, rowIndex) {/**登录时间*/
							return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
						}},
						{title : "备注", field : 'remark'},
						{title : $.i18n.prop("user.loginip"),field : 'loginIp'}, 					  	/**登录IP*/
						{title : $.i18n.prop("user.logintimes"),field : 'loginTimes',sortable : true}  /**登录次数*/
			]],
			toolbar : '#chat_analyst_datagrid_toolbar'
		});
	},
	setEvent:function(){
		// 列表查询
		$("#chat_analyst_queryForm_search").on("click",function(){
			var userNo = $("#userNo").val();                       //账号 
			var status = $("#status  option:selected").val();      //状态
			var position = $("#position").val();                   //职位
			var role = $("#role  option:selected").val();          //角色
			var queryParams = $('#'+chatAnalyst.gridId).datagrid('options').queryParams;
			queryParams['userNo'] = userNo;
			queryParams['status'] = status;
			queryParams['position'] = position;
			queryParams['roleId'] = role;
			$('#'+chatAnalyst.gridId).datagrid({
				url : basePath+'/analystController/datagrid.do',
				pageNumber : 1
			});
		});
		// 重置
		$("#chat_analyst_queryForm_reset").on("click",function(){
			$("#chat_analyst_queryForm")[0].reset();
		});
	},
	/**
	 * 功能：查看
	 * @param recordId   dataGrid行Id
	 */
	view : function(recordId){
		$("#chat_analyst_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/analystController/'+recordId+'/view.do');
		goldOfficeUtils.openSimpleDialog({
			title : $.i18n.prop("common.operatetitle.view"),       /**查看记录*/
			height : 320 ,
			href : url ,
			iconCls : 'pag-view'
		});
	},
	/**
	 * 功能：用户退出房间
	 */
	exitChatRoom : function(){
		var rows = $("#"+chatAnalyst.gridId).datagrid('getSelections');
		var ids = [];
		if(rows.length > 0){
			for(var i = 0; i < rows.length; i++) {
				ids.push(rows[i]["userNo"]);
			}
		 }else{
			$.messager.alert("操作提示", "请选择一行记录!"); 
			return;
		}
		var url = formatUrl(basePath + '/chatanalystController/toExitRoom.do?userIds='+ids.join(','));
		var submitUrl =  formatUrl(basePath + '/chatanalystController/exitRoom.do');
		goldOfficeUtils.openEditorDialog({
			dialogId : "editWindow",
			title : '退出房间',			/**添加记录*/
			width : 280,
			height : 120,
			href : url,
			iconCls : 'pag-cancel',
			handler : function(){   //提交时处理
				if($("#aystExitForm").form('validate')){
					$.messager.confirm("操作提示", "你确定要强制让这些用户退出对应的房间吗？", function(r) {
						if(r){
							   goldOfficeUtils.ajaxSubmitForm({
									url : submitUrl,
									formId : 'aystExitForm',
									onSuccess : function(data){  //提交成功后处理
										var d = $.parseJSON(data);
										if(d.success) {
											$("#editWindow").dialog("close");
											$.messager.alert($.i18n.prop("common.operate.tips"),'操作已执行','info');
										}else{
											$.messager.alert($.i18n.prop("common.operate.tips"),'操作失败：'+d.msg,'error');
										}
									}
								});
							}
						});
				}
			}
		});
	},
	/**
	 * 功能：增加
	 */
	add : function(){
		var url = formatUrl(basePath + '/analystController/add.do');
		var submitUrl =  formatUrl(basePath + '/analystController/create.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.add"),			/**添加记录*/
			width : 800,
			height : 720,
			href : url,
			iconCls : 'pag-add',
			handler : function(){   //提交时处理
				if($("#chatAystAddForm").form('validate')){
					if(chatAnalyst.validWinRate() == false){
						return ;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatAystAddForm',
						onSuccess : function(data){  //提交成功后处理
							var d = $.parseJSON(data);
							if(d.success) {
								$("#myWindow").dialog("close");
								chatAnalyst.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),'新增用户成功！密码为：'+$("#pwd").val(),'info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'新增失败，原因：'+d.msg,'error');
							}
						}
					});
				}
			},
			onLoad : function(){
				if(window.location.href.indexOf("24k.hk") != -1){
					$("#ayst_header_default>div:gt(7)").remove();
				}
			}
		});
	},
	/**
	 * 功能：修改
	 * @param recordId   dataGrid行Id
	 */
	edit : function(recordId){
		$("#chat_analyst_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/analystController/'+recordId+'/edit.do');
		var submitUrl =  formatUrl(basePath + '/analystController/update.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
			width : 800,
			height : 700,
			href : url,
			iconCls : 'pag-edit',
			handler : function(){    //提交时处理
				if($("#chatAystEditForm").form('validate')){
					if(chatAnalyst.validWinRate() == false){
						return ;
					}
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'chatAystEditForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#myWindow").dialog("close");
								chatAnalyst.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'修改失败，原因：'+d.msg,'error');  /**操作提示  修改失败!*/
							}
						}
					});
				}
			},
			onLoad : function(){
				if(window.location.href.indexOf("24k.hk") != -1){
					$("#ayst_header_default>div:gt(7)").remove();
				}
				var avatarSrc=$("#ayst_currentAvatarPath").val();
				var loc_defaultFlag = false;
				if(isValid(avatarSrc)){
					$("#ayst_header_default div img").each(function(){
						if(this.src==avatarSrc){
							$("#ayst_header_default div input[name=defaultHeader][t="+$(this).attr("t")+"]").click();
							loc_defaultFlag = true;
						}
					});
					if(!loc_defaultFlag){
						$("#chatAyst_header_tab").tabs("select", "本地上传");
					}
				}
			}
		});
	},
	/**
	 * 功能：刷新
	 */
	refresh : function(){
		$('#'+chatAnalyst.gridId).datagrid('reload');
	},
	/**
	 * 功能：重设密码
	 * @param recordId  dataGrid行Id
	 */
	resetPwd : function(recordId){
		$("#chat_analyst_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/analystController/resetPwd.do?id='+recordId);
		var submitUrl =  formatUrl(basePath + '/analystController/saveResetPwd.do');
		goldOfficeUtils.openEditorDialog({
			title : $.i18n.prop("user.resetpwd"),     /**重设密码*/
			height : 150,
			href : url,
			iconCls : 'ope-redo',
			handler : function(){    //提交时处理
				if($("#ayst_resetPwdForm").form('validate')){
					goldOfficeUtils.ajaxSubmitForm({
						url : submitUrl,
						formId : 'ayst_resetPwdForm',
						onSuccess : function(data){   //提交成功后处理
							var d = $.parseJSON(data);
							if (d.success) {
								$("#myWindow").dialog("close");
								chatAnalyst.refresh();
								$.messager.alert($.i18n.prop("common.operate.tips"),'重置密码成功！您的新密码是：'+$("#pwd").val(),'info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("user.resetpwd.fail"),'error');	/**操作提示  重设密码失败!*/
							}
						}
					});
				}
			}
		});
	},
	/**
	 * 验证数字
	 */
	validWinRate:function(){
		var winRate = $('#winRate').val();
		if(!winRate){
			return true;
		}
		if(winRate.indexOf('%')!=-1){
			winRate = winRate.replace('%','');
		}else{
			$.messager.alert("提示信息","胜率为有效的百分比(两位小数),例如75.00%");
			$('#winRate').focus();
			return false;
		}
		winRate = parseFloat(winRate);
		if(isNaN(winRate)){
			$.messager.alert("提示信息","胜率为有效的百分比(两位小数),例如75.00%");
			$('#winRate').focus();
			return false;
		}else{
			if(winRate<1){
				winRate = winRate * 100;
			}
			$('#winRate').val((winRate).toFixed(2) + '%');
			return true;
		}
	},
	/**
	 * 功能：批量删除
	 */
	batchDel : function(){
		var url = formatUrl(basePath + '/analystController/batchDel.do');
		goldOfficeUtils.deleteBatch('chat_analyst_datagrid',url,"userId");	
	},
	/**
	 * 功能：删除单行
	 * @param recordId  dataGrid行Id
	 */
	del : function(recordId){
		$("#chat_analyst_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/analystController/oneDel.do');
		goldOfficeUtils.deleteOne('chat_analyst_datagrid',recordId,url);
	},
	/**
	 * 提取连接
	 * @param type 设备类型
	 * @param rmType 房间类型
	 * @returns
	 */
	getLiveLinks:function(type){
		$.each(['studio','fxstudio','hxstudio'],function(k, v){
			var pcSz=[],pcHk=[],mbSz=[],mbHk=[],mbAudioSz=[],mbAudioHk=[];
			var devLinks=chatAnalyst.liveLinks[v];
			var rw=null;
			var obsLabel=chatAnalyst.liveLinks.obsLabel;
			var codeList=null,urlTmp=null;
			if(type==1){
				if(k==0) {
					var oneTVLink = chatAnalyst.liveLinks.oneTVLink;
					pcSz.push('<label><input type="checkbox" name="liveLink" value="' + oneTVLink.url + '" code="' + oneTVLink.code + '" />' + oneTVLink.name + '</label>');
				}
				$.each(devLinks.yyLink,function(i, row){
					pcSz.push('<label><input type="checkbox" name="liveLink" value="'+row.url+'" code="'+row.code+'" />'+row.name+'</label>');
				});
			}
			for(var rwIndex in obsLabel){
				rw=obsLabel[rwIndex];
				for(var i in rw.url){
					codeList=devLinks.obsCode;
					for(var cd in codeList){
						if(type==1){
							urlTmp="rtmp://"+rw.url[i]+"/"+codeList[cd];
						}else{
							urlTmp="http://"+rw.url[i].replace("h6","h5").replace("sz6","sz")+"/"+codeList[cd]+(type==4?"A/":"/")+chatAnalyst.liveLinks.obsUrlIndex[i];
						}
						if(type==1){
							if(rw.name.indexOf('大陆')>-1){
								pcSz.push('<label><input type="checkbox" name="liveLink" value="'+urlTmp+'" code="'+type+'" />'+(rw.name.formatStr(rw.url.length==1?"":parseInt(i)+1,codeList[cd]))+'</label>');
							}else{
								pcHk.push('<label><input type="checkbox" name="liveLink" value="'+urlTmp+'" code="'+type+'" />'+(rw.name.formatStr(rw.url.length==1?"":parseInt(i)+1,codeList[cd]))+'</label>');
							}
						}else if(type==3){
							if(rw.name.indexOf('大陆')>-1){
								mbSz.push('<label><input type="checkbox" name="liveLink" value="'+urlTmp+'" code="'+type+'" />'+(rw.name.formatStr(rw.url.length==1?"":parseInt(i)+1,codeList[cd]))+'</label>');
							}else{
								mbHk.push('<label><input type="checkbox" name="liveLink" value="'+urlTmp+'" code="'+type+'" />'+(rw.name.formatStr(rw.url.length==1?"":parseInt(i)+1,codeList[cd]))+'</label>');
							}
						}else if(type==4){
							if(rw.name.indexOf('大陆')>-1){
								mbAudioSz.push('<label><input type="checkbox" name="liveLink" value="'+urlTmp+'" code="'+type+'" />'+(rw.name.formatStr(rw.url.length==1?"":parseInt(i)+1,codeList[cd]))+'</label>');
							}else{
								mbAudioHk.push('<label><input type="checkbox" name="liveLink" value="'+urlTmp+'" code="'+type+'" />'+(rw.name.formatStr(rw.url.length==1?"":parseInt(i)+1,codeList[cd]))+'</label>');
							}
						}
					}
				}
			}
			pcSz = pcSz.concat(pcHk);
			mbSz = mbSz.concat(mbHk);
			mbAudioSz = mbAudioSz.concat(mbAudioHk);
			$('#setLiveLinks_form #videoUrlPc').append(pcSz.join(''));
			$('#setLiveLinks_form #videoUrlMb').append(mbSz.join(''));
			$('#setLiveLinks_form #audioUrlMb').append(mbAudioSz.join(''));
		});
	},
	setLiveLinks:function(liveLinks){
		if(isValid(liveLinks)){
			liveLinks = JSON.parse(liveLinks);
			$.each(liveLinks, function(i, row){
				$('#setLiveLinks_form input[name="liveLink"][code="'+row.code+'"][value="'+row.url+'"]').prop('checked',true);
			});
		}
	},
	/**
	 * 设置直播地址
	 */
	saveLiveLinks : function(recordId){
		$("#chat_analyst_datagrid").datagrid('unselectAll');
		var url = formatUrl(basePath + '/analystController/'+recordId+'/getLiveLinks.do');
		var submitUrl =  formatUrl(basePath + '/analystController/setLiveLinks.do');
		goldOfficeUtils.openEditorDialog({
			title : "设置直播地址",
			width : 800,
			height : 575,
			href : url,
			iconCls : 'pag-edit',
			buttons:[
				{
					text : '提交',
					iconCls : "ope-save",
					handler : function(){
						var liveLinks = [];
						$('#setLiveLinks_form input[name="liveLink"]').each(function(){
							if($(this).is(':checked')) {
								liveLinks.push({'code':$(this).attr('code'),'url':$(this).val(),'name':$(this).parent().text()});
							}
						});
						goldOfficeUtils.ajax({
							url : submitUrl,
							data : {
								userId:$('#setLiveLinks_form #userId').val(),
								liveLinks: JSON.stringify(liveLinks)
							},
							success : function(data){
								if (data.success) {
									$("#myWindow").dialog("close");
									$.messager.alert($.i18n.prop("common.operate.tips"),'直播地址设置成功','info');
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'直播地址设置失败，原因：'+data.msg,'error');
								}
							}
						});
					}
				},
				{
					text : '关闭',
					iconCls : "ope-close",
					handler : function() {
						$(this).parents(".easyui-dialog:first").dialog("close");
					}
				}]
		});
	}
};

//初始化
$(function() {
	chatAnalyst.init();
});