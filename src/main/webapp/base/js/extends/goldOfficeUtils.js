/**
 * 摘要：常用工具类
 * @author Gavin.guo
 * @date   2014-10-14
 */
var goldOfficeUtils = {
	init : function(){
		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
		$('body').append('<div id="myWindow" class="easyui-dialog" closed="true"></div>');
	},
	/**
	 * 功能：打开简单的弹出框	
	 * @param config    dialog相关参数对象
	 */
	openSimpleDialog : function(config){
		config = $.extend({
			title : '',
			width : 600,
			height : 400,
			href :  null,
			modal : true,
			minimizable : false,
			maximizable : true,
			iconCls : 'ope-view',
			shadow : true,
			cache : false,
			closed : false,
			collapsible : false,
			draggable : true,
			resizable : true,
			loadingMessage : '正在加载数据，请稍等片刻......',
			onLoad : $.noop,
			onOpen : $.noop
        },config);
		var dialogId = config.dialogId === undefined ? 'myWindow' : config.dialogId;
		$('#'+dialogId).dialog({
			title : config.title,
			width : config.width,
			height : config.height,
			top : ($(window).height() - config.width) * 0.5,
			left : ($(window).width() - config.width) * 0.5,
			href : config.href,
			modal : config.modal,
			minimizable : config.minimizable,
			maximizable : config.maximizable,
			iconCls: config.iconCls,
			shadow : config.shadow,
			cache : config.cache,
			closed : config.closed,
			collapsible : config.collapsible,
			draggable : config.draggable,
			resizable : config.resizable,
			loadingMessage : config.loadingMessage,
			onLoad :config.onLoad,
			onOpen : config.onOpen,
			buttons : config.buttons === undefined ? [{
				text : '关闭',
				iconCls : "ope-close",
				handler : function() {
					$(this).parents(".easyui-dialog:first").dialog("close");
				}
			}] : config.buttons
		});
		return this;
	},
	/**
	 * 功能：打开可编辑的弹出框
	 * @param config     dialog相关参数对象(title 弹出框标题、url 请求的url地址)
	 */
	openEditorDialog : function(config){
		config = $.extend({
			dialogId : '',
			title : '',
			width : 600,
			height : 400,
			href :  null,
			modal : true,
			minimizable : false,
			maximizable : true,
			iconCls : 'pag-edit',
			shadow : true,
			cache : false,
			closed : false,
			collapsible : false,
			draggable : true,
			resizable : true,
			loadingMessage : '正在加载数据，请稍等片刻......',
			onLoad : $.noop,
			onOpen : $.noop
        },config);
		var dialogId = config.dialogId === undefined || config.dialogId === ''? 'myWindow' : config.dialogId;
		$('#'+dialogId).dialog({
			title : config.title,
			width : config.width,
			height : config.height,
			top : ($(window).height() - config.height) * 0.5,
			left : ($(window).width() - config.width) * 0.5,
			href : config.href,		
			modal : config.modal,
			minimizable : config.minimizable,
			maximizable : config.maximizable,
			iconCls: config.iconCls,
			shadow : config.shadow,
			cache : config.cache,
			closed : config.closed,
			collapsible : config.collapsible,
			draggable : config.draggable,
			resizable : config.resizable,
			loadingMessage : config.loadingMessage,
			onLoad :function(){
				$('#'+dialogId+" .combo-text").each(function(){
		    		var ctpw=$(this).parent().prev("select").width();
		    		if(ctpw>0){
		    			$(this).width(ctpw-60);
		    		}
		    	});
				if(config.onLoad){
					config.onLoad();
				}
			},
			onOpen : config.onOpen,
			buttons	 : config.buttons === undefined ? [{
				text : '提交',
				iconCls : "ope-save",
				handler : config.handler
			},{
				text : '关闭',
				iconCls : "ope-close",
				handler : function() {
					$(this).parents(".easyui-dialog:first").dialog("close");
				}
			}] : config.buttons
		});
		return this;
	},
	/**
	 * 功能：ajax提交form
	 */
	ajaxSubmitForm : function(config){
		config = $.extend({
			url : '',
			formId : ''
        },config);
		$('#' + config.formId).form('submit', {
			url : config.url,
			onSubmit : config.onSubmit,
			success : config.onSuccess
		});
	},
	/**
	 * 功能：ajax请求提交数据
	 */
	ajax : function(config){
		config = $.extend({
			url : '',
			type : 'post',
			dataType : 'json', 
			cache : false
        },config);
		$.ajax({
			url : config.url,
			type : config.type,
			dataType : config.dataType,
			data : config.data === undefined ? {} : config.data,
			cache : config.cache,
			success : config.success === undefined ? function(data) {
				if(data.success) {
					$.messager.alert("操作提示","删除成功!",'info');
				}
			} : config.success
		});
		return this;
	},
	/**
	 * 功能：datagrid公共方法
	 * @param config     datagrid相关参数对象
	 */
	dataGrid : function(config){
		config = $.extend({
			gridId : '',
			url : '',
			pagination : true,
			pagePosition : 'bottom',
			pageSize : 20,
			pageList : [10, 20, 30, 50, 100],
			fit : false,
			fitColumns : true,
			method :  'GET',
			/*nowrap : true,*/
			nowrap: true, 
			border : false,
			singleSelect:true,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			rownumbers : true,
			columns : [],
			showFooter : false,
			queryParams : {},
			toolbar : null
        },config);
		if(config.gridId){
			var sysFlag = $('#yxui_main_sysfag_select').val(); 
			if(isValid(sysFlag)){
				config.queryParams["systemCategory"]=sysFlag;
			}
			var cgId="#"+config.gridId;
			$(cgId).datagrid({
				url : config.url,
				pagination : config.pagination,
				pagePosition : config.pagePosition,
				pageSize : config.pageSize,
				pageList : config.pageList,
				fit : config.fit,
				fitColumns : config.fitColumns,
				method :  config.method,
				nowrap : config.nowrap,
				border : config.border,
				singleSelect : config.singleSelect,
				idField : config.idField,
				sortName : config.sortName,
				sortOrder : config.sortOrder,
				rownumbers : config.rownumbers,
				columns : config.columns,
				showFooter : config.showFooter,
				queryParams : config.queryParams,
				toolbar : config.toolbar,
				onResizeColumn:function(colName,width){//列大小自适应处理
					$(".datagrid-wrap,.datagrid-view").css({width:"auto"});
					var colsL=$(cgId).siblings('.datagrid-view2').find(".datagrid-header-row").find('td[field!=""]').not("td:hidden").length;
					var maxWidth=$(cgId).parents(".datagrid").parent().width()-colsL*10;
					var obj=$(cgId).datagrid('getColumnOption',colName);
					var eachColSize= parseInt(maxWidth/colsL);
					$(cgId).siblings(".datagrid-header,.datagrid-body").width(maxWidth);
					if(obj.width<eachColSize&&obj.width>30){
						obj.width=eachColSize;	
						obj.boxWidth=eachColSize-8;
						$(".datagrid-header .datagrid-header-row td[field='"+colName+"']",$(cgId).siblings()).width(obj.width);
					}
				},
				onLoadSuccess : config.onLoadSuccess === undefined ? function(data){ //默认去掉所有选中的项
					$("#"+config.gridId).datagrid("clearSelections"); 
				} : config.onLoadSuccess,
				onLoadError : config.onLoadError === undefined ? function(){} : config.onLoadError,
				onClickRow : config.onClickRow === undefined ? function(rowIndex, rowData){} : config.onClickRow,
				onDblClickRow : config.onDblClickRow === undefined ? function(rowIndex, rowData){} : config.onDblClickRow,
				onSelect : config.onSelect === undefined ? function(rowIndex, rowData){} : config.onSelect ,
				onAfterEdit : config.onAfterEdit === undefined ? function(rowIndex, rowData, changes){} : config.onAfterEdit
			});
			//监听body窗口大小，重新调整datagrid列大小
			Event.onResizend(function () {
				$("div[id$=_datagrid]").each(function(){
					 var maxWidth=$(this).parents(".datagrid").parent().width();
					 $(this).parents(".datagrid,.datagrid-wrap").width(maxWidth);
					 $(this).datagrid('resize',{width:(maxWidth)});//重新定义datagrid宽度
			    });
	        });
		}
		return this;
	},
	/**
	 * 功能：批量删除选中的记录
	 * @param gridId     列表gridId
	 * @param deleteUrl  删除url
	 * @param message    删除时提示的消息,未传值，默认为"您确定要删除记录吗?"
	 */
	deleteBatch : function(gridId,deleteUrl,idKey,message){
		message = (message === undefined ? "您确定要删除记录吗?" : message) ;
		var rows = $("#"+gridId).datagrid('getSelections');
		var ids = [];
		if(rows.length > 0){
			 $.messager.confirm("操作提示", message, function(r) {
				   if (r) {
					    var idKeyTmp = idKey ? idKey:"id";
						for(var i = 0; i < rows.length; i++) {
							ids.push(rows[i][idKeyTmp]);
						}
						goldOfficeUtils.ajax({
							url : deleteUrl,
							data : {
								ids : ids.join(',')
							},
							success: function(data) {
								if(data.success) {
									$("#"+gridId).datagrid('unselectAll');
									$('#'+gridId).datagrid('reload');
									ids='';
									$.messager.alert("操作提示","删除成功!",'info');
								}else{
									$.messager.alert($.i18n.prop("common.operate.tips"),'删除失败，原因：'+data.msg,'error');
						    	}
							}
						});
					}
				});
		 }else{
			 $.messager.alert("操作提示", "请选择一行记录!"); 
		 }
	},
	/**
	 * 功能：删除选中的记录
	 * @param gridId     列表gridId
	 * @param recordId   当前记录Id
	 * @param deleteUrl  删除url
	 * @param message    删除时提示的消息,未传值，默认为"您确定要删除记录吗?"
	 */
	deleteOne : function(gridId,recordId,deleteUrl,message,success){
		message = (message === undefined ? "您确定要删除记录吗?" : message) ;
		var record = $("#"+gridId).datagrid('getSelected');
		$.messager.confirm("操作提示", message , function(r) {
			   if (r) {
				   goldOfficeUtils.ajax({
						url : deleteUrl,
						data : {
							id : recordId
						},
						success: function(data) {
							if (data.success) {
								$("#"+gridId).datagrid('unselectAll');
								$('#'+gridId).datagrid('reload');
								$.messager.alert($.i18n.prop("common.operate.tips"),'删除成功!','info');
							}else{
								$.messager.alert($.i18n.prop("common.operate.tips"),'删除失败，原因：'+data.msg,'error');
					    	}
						}
					});
				}
			});
	},
	/**
	 * 功能：删除treeGrid中的记录
	 * @param config
	 */
	deleteTreeGridOne : function(config){
		$.messager.confirm("操作提示", config.message === undefined ? "您确定要删除记录吗?" : config.message, function(r) {
			   if(r) {
				   goldOfficeUtils.ajax({
						url : config.url === undefined ? '' : config.url,
						data : config.data === undefined ? {} : config.data,
						success: config.success === undefined ? function(data) {
							if(data.success) {
								$.messager.alert("操作提示","删除成功!",'info');
							}
						} : config.success
					});
				}
		});
	},
	/**
	 * 功能：批量执行提案
	 * @param gridId     列表gridId
	 * @param url  	     批量执行提案url
	 * @param postData   批量执行提案时传给后台的数据，格式：{pno:'',accountNo:'',status:''}
	 * @param message    执行提案时提示的消息,未传值，默认为"您确定要执行选中的提案吗?"
	 */
	batchApprove : function(gridId,url,postData,message){
		var rows = $("#"+gridId).datagrid('getSelections');
		if(!rows || rows.length == 0) {
			$.messager.alert($.i18n.prop("common.operate.tips"), '请选择记录进行操作!', 'warning');
			return;
		}
		message = (message === undefined ? "您确定要执行选中的提案吗?" : message) ;
		$.messager.confirm("操作提示", message, function(r) {
			if (r) {
				var pnos = [],accountNos = [];
				var pnoTmp = postData.pno ? postData.pno:"pno";
				var accountNoTmp = postData.accountNo ? postData.accountNo:"accountNo";
				for(var i = 0; i < rows.length; i++) {
					pnos.push(rows[i][pnoTmp]);
					accountNos.push(rows[i][accountNoTmp]);
				}
				getJson(url,{pnos : pnos.join(','),accountNos : accountNos.join(','),status:postData.status},function(data){
			    	if(data.success){
			    		$('#'+gridId).datagrid('reload');
			    		pnos='';
			    		accountNos='';
						$.messager.alert("操作提示","执行提案成功!",'info');
			    	}else{
						$.messager.alert($.i18n.prop("common.operate.tips"),'执行提案失败，原因：'+data.msg,'error');
			    	}
				});
			}
		});
	},
	/**
	 * 功能：批量取消提案
	 * @param gridId     列表gridId
	 * @param url  	     批量取消提案url
	 * @param postData   批量执行提案时传给后台的数据，格式：{pno:'',accountNo:''}
	 * @param message    取消提案时提示的消息,未传值，默认为"您确定要执行选中的提案吗?"
	 */
	batchCancelApprove : function(gridId,url,postData,message){
		var rows = $("#"+gridId).datagrid('getSelections');
		if(!rows || rows.length == 0) {
			$.messager.alert($.i18n.prop("common.operate.tips"), '请选择记录进行操作!', 'warning');
			return;
		}
		message = (message === undefined ? "您确定要取消选中的提案吗?" : message) ;
		$.messager.confirm("操作提示", message, function(r) {
			if (r) {
				var pnos = [],accountNos = [];
				var pnoTmp = postData.pno ? postData.pno:"pno";
				var accountNoTmp = postData.accountNo ? postData.accountNo:"accountNo";
				for(var i = 0; i < rows.length; i++) {
					pnos.push(rows[i][pnoTmp]);
					accountNos.push(rows[i][accountNoTmp]);
				}
				getJson(url,{pnos : pnos.join(','),accountNo : accountNos.join(',')},function(data){
			    	if(data.success){
			    		$('#'+gridId).datagrid('reload');
			    		pnos='';
			    		accountNos='';
						$.messager.alert("操作提示","取消提案成功!",'info');
			    	}else{
						$.messager.alert($.i18n.prop("common.operate.tips"),"取消提案失败，原因："+data.msg,'error');
			    	}
				});
			}
		});
	},
	/**
	 * 功能：文件上传
	 * @param config
	 */
	uploadFile : function(config){
		$('#'+config.fileId).uploadify({
            'method': config.method == undefined ? 'post':config.method,							//和后台交互的方式：post/get
            'formData': config.formData == undefined ? {} :config.formData,			                //传递自己的数据
            'width' : config.width === undefined ? 120 : config.width,                                                                
            'height' : config.height === undefined ? 30 : config.height,                  
            'swf' : basePath+'/base/js/uploadify/uploadify.swf',
            'uploader' : config.uploader === undefined ? '' : config.uploader,              		//上传文件触发的url
            'auto' : config.auto === undefined ? false : config.auto,                           	//文件选择完成后，是否自动上传
            'fileObjName' : config.fileObjName === undefined ? 'file' : config.auto ,           	//后台接收方法的参数                                                         
            'debug': config.debug === undefined ? true : config.debug,						    	//debug模式开/关,打开后会显示debug时的信息
            'fileSizeLimit' : config.fileSizeLimit === undefined ? 0 : config.fileSizeLimit,		//文件限制的大小，以字节为单位，0为不限制。1MB:1*1024*1024
            'fileTypeDesc' : config.fileTypeDesc === undefined ? 'All Files' : config.fileTypeDesc, //文件类型描述
        	'fileTypeExts' : config.fileTypeDesc === undefined ? '*.*' : config.fileTypeExts,		//允许上传的文件类型，限制弹出文件选择框里能选择的文件
            'buttonText' : config.buttonText === undefined ? '请选择' : config.buttonText,	    	//上传按钮的文字
            'multi': config.method == undefined ? true : config.method,								//是否能选择多个文件
            'successTimeout' : 240,	
            'onSelectError' : function(file, errorCode, errorMsg){
				switch(errorCode) {
					case -120:
						alert("文件["+file.name+"] 大小超出限制,请重新选择!");
						break;
					case -130 :
						alert("文件["+file.name+"] 类型不对,请重新选择!");
						break;
				}
			},
            'onUploadStart': config.onUploadStart,
            'onUploadSuccess' : config.onUploadSuccess
        });
	},
	/**
	 * 功能：图片预览
	 * @param viewImagePath 查看图片的路径Id
	 */
	onViewImage : function(viewImagePathId){
		var viewImg = $(viewImagePathId).val();
		if(isBlank(viewImg)){
			alert("没有可预览的图片，请先选择图片上传!");
			return;
		}
		var url = formatUrl(basePath + '/uploadController/viewImage.do?imagePath='+viewImg);
		goldOfficeUtils.openSimpleDialog({
			title : '预览图片',
			width : 800,
			height : 600 ,
			href : url,
			iconCls : 'pag-view'
		});
	},
	/**
	 * 功能：图片裁剪
	 * @param  sourceImagePath 需要裁剪原图片的路径Id
	 * @param  cutedImagePathId 裁剪后图片的路径Id
	 * @param  cutedImageSuffix 裁剪后图片名加的后缀(例如：原图片名:a.jpg 裁剪后图片名 a_logo.jpg 此时cutedImageSuffix值为logo)
	 * @param  saveImagePathId  修改保存时需要传值
	 */
	onCutImage : function(sourceImagePathId,cutedImagePathId,cutedImageSuffix,saveImagePathId){
		var sourceImagePath = $(sourceImagePathId).val();
		if(isBlank(sourceImagePath)){
			alert("没有可裁剪的图片，请先选择图片上传!");
			return;
		}
		var url = formatUrl(basePath+'/uploadController/cutImage.do?sourceImagePath='+sourceImagePath);
		var submitUrl = formatUrl(basePath + '/uploadController/doCutImage.do');
		goldOfficeUtils.openEditorDialog({
			title : '裁剪',
			width : 800,
			height : 600 ,
			href : url,
			iconCls : 'pag-view',
			handler : function(){
				if(validCutForm()){  	//先验证是否选择裁剪区域
					var serializeCoordsFormData  = $("#coordsForm").serialize();
					serializeCoordsFormData += "&sourceImagePath="+sourceImagePath
											+"&cutedImageSuffix="+cutedImageSuffix;
					getJson(submitUrl,serializeCoordsFormData,function(data){
						if(data.success){
							if(saveImagePathId !== undefined){   //如果是修改的时候需要传值
								$(saveImagePathId).val(data.obj);
							}
							$(cutedImagePathId).val(data.obj);
							$("#myWindow").dialog("close");
							$.messager.alert($.i18n.prop("common.operate.tips"),'裁剪成功!','info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'裁剪失败，原因：'+data.msg,'error');
						}
					},true);
				}
			}
		});
	},
	/**
	 * 功能：刷新当前选中的Tab
	 */
	refreshTab : function(){
		$('#yxui_main_tabs').tabs('getSelected').panel('refresh');
	},
	
	/**
	 * 列表下拉选择框设置
	 * @param gridId 列表id
	 * @param selectId 下拉框字段
	 * @param selectfieldOfGrid
	 * @param theUrl 提交的请求路径
	 * @param idFieldOfGrid id字段对应的field,默认是id
	 */
	setGridSelectVal:function(gridId,selectId,selectfieldOfGrid,theUrl,idFieldOfGrid){
		$("#"+selectId).on("change",function(){
			var selectDom=$(this).children('option:selected'),
			    val=selectDom.val();
			var rows = $("#"+gridId).datagrid('getSelections');
			var ids = [];
			if(rows.length > 0){
				 $.messager.confirm("操作提示", "您确定要把所选记录设为【"+selectDom.text()+"】？", function(r) {
					   if (r) {
						    var idKeyTmp = idFieldOfGrid ? idFieldOfGrid:"id";
							for(var i = 0; i < rows.length; i++) {
								ids.push(rows[i][idKeyTmp]);
							}
							goldOfficeUtils.ajax({
								url : theUrl,
								data : {
									ids : ids.join(','),
									fieldVal:val
								},
								success: function(data) {
									if(data.success) {
										var rowIndex=0,rowObj={};
										for(var k= 0; k < rows.length; k++){
											rowIndex=$("#"+gridId).datagrid('getRowIndex',rows[k]);
											rowObj[selectfieldOfGrid]=val;
											$('#'+gridId).datagrid('updateRow',{index:rowIndex,row:rowObj}); 
										}
										$("#"+gridId).datagrid('unselectAll');
										$.messager.alert("操作提示","状态设置成功!",'info');
									}else{
										$.messager.alert($.i18n.prop("common.operate.tips"),'状态设置失败，原因：'+data.msg,'error');
							    	}
									$("#"+selectId).find("option[value='']").attr("selected",true);
								}
							});
						}else{
							$("#"+selectId).find("option[value='']").attr("selected",true);
						}
					});
			 }else{
				 $("#"+selectId).find("option[value='']").attr("selected",true);
				 $.messager.alert("操作提示", "请选择一行记录!"); 
			 }
		});
	}
};

$(function() {
	goldOfficeUtils.init();
});