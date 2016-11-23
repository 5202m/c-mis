/**
 * 短信配置管理<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2015<BR>
 * Author : Dick.guo <BR>
 * Date : 2015年10月29日 <BR>
 * Description : <BR>
 * <p>
 * 
 * </p>
 */
var Syllabus = {
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
    	    	{name:'yy:92628431/92628431',url:'http://yy.com/s/92628431/92628431/yyscene.swf'},
        		{name:'yy:92628431/2439533296',url:'http://yy.com/s/92628431/2439533296/yyscene.swf'}
    	    ]
    	},
    	fxstudio:{
    		obsCode:["03","04"],
    	    yyLink:[
    	    	{name:'yy:92628431/2537339360',url:'http://yy.com/s/92628431/2537339360/yyscene.swf'},
        		{name:'yy:92628431/2559592756',url:'http://yy.com/s/92628431/2559592756/yyscene.swf'}
    	    ]
    	},
    	hxstudio:{
    		obsCode:["10","11","12"],
    	    yyLink:[
    	    	{name:'yy:46327234/46327234',url:'http://yy.com/s/46327234/46327234/yyscene.swf'},
    	    	{name:'yy:82992602/82992602',url:'http://yy.com/s/82992602/82992602/yyscene.swf'}
    	    ]
    	}
    },	
	gridId : 'syllabus_datagrid',
	daysCN : ['星期天','星期一','星期二','星期三','星期四','星期五','星期六'],
	chatGroups : {},
	lecturerList:null,
	coursesData:null,
	studioLink:null,
	init : function(){
		this.initGrid();
		this.initConst();
		this.setEvent();
	},
	/**
	 * 提取连接
	 * @param type 设备类型
	 * @param rmType 房间类型
	 * @returns 
	 */
	getLiveLinks:function(type,rmType){
		var devLinks=Syllabus.liveLinks[rmType];
		var rw=null;
		var obsLabel=Syllabus.liveLinks.obsLabel;
		var lst=[],codeList=null,urlTmp=null;
		for(var rwIndex in obsLabel){
			rw=obsLabel[rwIndex];
			for(var i in rw.url){
				codeList=devLinks.obsCode;
				for(var cd in codeList){
					if(type==1){
						urlTmp="rtmp://"+rw.url[i]+"/"+codeList[cd];
					}else{
						urlTmp="http://"+rw.url[i].replace("h6","h5").replace("sz6","sz")+"/"+codeList[cd]+(type==4?"A/":"/")+Syllabus.liveLinks.obsUrlIndex[i];
					}
					lst.push({name:rw.name.formatStr(rw.url.length==1?"":parseInt(i)+1,codeList[cd]),url:urlTmp});
				}
			}
		}
		if(type==1){
			lst=devLinks.yyLink.concat(lst);
		}
		return lst;
	},
	/**
	 * 功能：dataGrid初始化
	 */
	initGrid : function(){
		goldOfficeUtils.dataGrid({
			gridId : Syllabus.gridId,
			idField:"id",
			sortName : 'groupType-asc,groupId-asc,publishStart', //
			sortOrder : "desc",
			url : basePath+'/chatSyllabusController/datagrid.do',
			columns : [[
			    {title : $.i18n.prop("common.operate"), field:'id', formatter: function(value, rowData, rowIndex){
			    	$("#syllabus_datagrid_rowOperation input").val(value);
			    	return $("#syllabus_datagrid_rowOperation").html();
			    }},
	            {title : "应用组别",field : 'groupType',formatter : function(value, rowData, rowIndex) {
	            	return Syllabus.chatGroups[value + ","];
				}},
				{title : "应用房间",field : 'groupId',formatter : function(value, rowData, rowIndex) {
					return value ? (Syllabus.chatGroups[rowData.groupType + "," + value] || value) : "";
				}},
	            {title : "发布开始时间",field : 'publishStart',formatter : function(value, rowData, rowIndex) {
	            	return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
				}},
				{title : "发布结束时间",field : 'publishEnd',formatter : function(value, rowData, rowIndex) {
					return value ? timeObjectUtil.longMsTimeConvertToDateTime(value) : '';
				}}
			]],
			toolbar : '#syllabus_datagrid_toolbar'
		});
	},
	
	/**
	 * 初始化常量
	 */
	initConst : function(){
		$("#syllabus_groupType_select option").each(function(){
			var loc_val = $(this).val();
			if(loc_val){
				Syllabus.chatGroups[loc_val + ","] = $(this).text();
			}
		});
		$("#syllabus_groupId_select option").each(function(){
			var loc_val = $(this).val();
			if(loc_val){
				Syllabus.chatGroups[$(this).attr("t") + "," + loc_val] = $(this).text();
			}
		});
	},
	
	/** 绑定事件 */
	setEvent:function(){
		/**查询*/
		$("#syllabus_groupType_select").on("change",function(){
			var loc_val = $(this).val();
			var loc_target = $("#syllabus_groupId_select");
			loc_target.find("option[t]").hide();
			if(loc_val)
			{
				loc_target.find("option[t='" + loc_val + "']").show();
			}
			loc_target.val("");
		});
		$("#syllabus_groupType_select").trigger("change");
		
		/**查询*/
		$("#syllabus_queryForm_search").on("click",function(){
			var queryParams = $('#'+Syllabus.gridId).datagrid('options').queryParams;
			$("#syllabus_queryForm select").each(function(){
				queryParams[$(this).attr("name")] = $(this).val();
			});
			$('#'+Syllabus.gridId).datagrid({pageNumber : 1});
		});
		
		/**重置*/
		$("#syllabus_queryForm_reset").on("click",function(){
			$("#syllabus_queryForm")[0].reset();
		});
	},
	/**
	 * 设置作者/分析师
	 */
    setLecturerSelect:function(dom,data){
		dom.find("select[id^='select_lecturer']").combotree({
    		data:data,
    		onLoadSuccess:function(node,param){
    			for(var i in data){
    				var dom=$(this).find(".tree-node[node-id="+data[i].id+"]");
    				if(dom.length>0){
    					var icn=$(dom.get(0)).css({height:"32px","margin":"2px 2px 0px"}).find(".tree-icon");
    					icn.removeClass("tree-file").css({height:"32px",width:"32px",padding:"2px"});
    					icn.append('<img src="'+(data[i].iconImg?data[i].iconImg:'')+'" style="height: 30px;width: 30px;">');
    					var dt=dom.find(".tree-title");
    					dt.text(dt.text()+"【"+data[i].id+"】");
    				}
    			}
    		}
		});
    },
   
    /**
     *  填充课程
     * @param dom
     * @param courses
     */
    fillCourses:function(dom,course){
    	if(course){
    		var selectDom=dom.find("select[name=status]").val(course.status);
    		dom.find("select[name=courseType]").val(course.courseType);
        	dom.find("input[name=title]").val(course.title);
        	dom.find("textarea[name=context]").val(course.context);
        	if(isValid(course.lecturerId)){
        		var tmpList=JSON.parse(JSON.stringify(this.lecturerList));
        		for(var i in tmpList){
					if(containSplitStr(course.lecturerId,tmpList[i].id)){
						tmpList[i].checked=true;
					}
				}
        		this.setLecturerSelect(dom,tmpList);
        	}else{
        		this.setLecturerSelect(dom,this.lecturerList);
        	}
        	if("0"==course.status){
        		selectDom.trigger("change");
        	}
    	}else{
    		this.setLecturerSelect(dom,this.lecturerList);
    	}
    },
    /**
     * 提取分析师列表
     */
    getLecturerList:function(){
    	var gtype=$("#syllabusEdit_groupType_select").val(),gId=$("#syllabusEdit_groupId_select").val()||$("#syllabusEdit_groupId_select").attr("tv");
    	this.lecturerList=getJson(basePath +"/chatSyllabusController/getAuthorList.do",{groupType:gtype,groupId:gId});
    	$("#panel_editSyllabus tbody:visible").each(function(){
    		Syllabus.setLecturerSelect($(this).find(".main"),Syllabus.lecturerList);
    	});
    },
    /**
     * 设置房间组
     */
    setRoomSelect:function(groupType){
    	if(isBlank(groupType)){
    		return;
    	}
    	var data=getJson(basePath +"/chatGroupController/getGroupTreeList.do",{groupType:groupType});
    	var selectDom=$("#syllabusEdit_groupId_select").html("").append('<option value="" >--请选择--</option>');
    	for(var i in data){
    		selectDom.append('<option value="'+data[i].id+'" '+(selectDom.attr("tv")==data[i].id?'selected="selected"':'')+'>'+data[i].text+'</option>');
    	}
    	Syllabus.getLecturerList();
    },
    /**
     * 通过克隆方法填充元素
     * @param timeBucket
     * @param courses
     */
    setCourseByClone:function(timeBucket,courses){
    	if(isBlank($("#syllabusEdit_groupType_select").val())){
    		alert("房间组别不能为空");
    		return;
    	}
    	if(isBlank($("#courses_hidden_id").val()) && isBlank($("#syllabusEdit_groupId_select").val())){
    		alert("房间不能为空");
    		return;
    	}
    	var cloneObj=$("#panel_editSyllabus table tbody:hidden").clone(true);
     	var ltId="select_lecturer"+($("#panel_editSyllabus table tbody").length)+"_";
     	cloneObj.find(".main").each(function(){
     		$(this).find(".lecturerDiv").find("select").attr("id",ltId+$(this).index());
     		 var loc_day = $(this).attr("dy");
     	});
     	cloneObj.find("input[name='startTime']").val(timeBucket&& isValid(timeBucket.startTime)?timeBucket.startTime:"00:00").timespinner({});
        cloneObj.find("input[name='endTime']").val(timeBucket&& isValid(timeBucket.endTime)?timeBucket.endTime:"00:00").timespinner({});
        $("#panel_editSyllabus table").append(cloneObj).find("tbody:last").show().find(".main").each(function(){
     		 var loc_day = $(this).attr("dy");
     		 $(this).find("select[name='status']").change(function () {
         		var disable=this.value!="1";
         		var pMain=$(this).parent().parent().parent();
         		var txtEl=pMain.find("textarea,input[name=title],select[name=courseType]").prop("disabled", disable);
         		if(disable){
         			txtEl.val("");
         			pMain.find("select[id^='select_lecturer']").combotree("disable").combotree("clear");
         		}else{
         			pMain.find("select[id^='select_lecturer']").combotree("enable");
         		}
             });
     		 Syllabus.fillCourses($(this),(courses&&courses.hasOwnProperty(loc_day)?courses[loc_day]:null));
     	});
    },
    /**
     * 设置直播链接
     * @param val
     */
    setStudioLink:function(val){
    	 if(val.indexOf("studio")==-1){
  	 	    $("#studioLinkLabel,#studioLinkSpan").hide();
  	 	 }else{
  	 	    $("#studioLinkLabel,#studioLinkSpan").show();
  	 	    $("#studioLinkSelect").val("1").trigger("change");
  	 	 }
    },
    /**
     * 提取链接
     */
    getStudioLink:function(code,isSetVal){
    	var hv=Syllabus.studioLink;
    	if(isValid(hv)){
    		hv=JSON.parse(hv);
    		for(var i in hv){
    			if(isSetVal){
    				$("#studioLink_"+hv[i].code).val(hv[i].url);
    				var hvUrl=hv[i].url;
    				if(isValid(hvUrl)){
    					if(hvUrl.indexOf("rtmp")!=-1||hvUrl.indexOf("yy.")!=-1){
    						$("#studioLinkSelect").val("1").trigger("change");
    						$('#studiolinkAddr').val(hvUrl);
    				    }
    				}
    			}else{
    				if(hv[i].code==code){
            			return hv[i].url;
            		}
    			}
        	}
    	}
    	return '';
    },
    /**
     * 设置编辑事件
     */
    setEditEvent : function() {
    	/**
         * 预览地址
         */
    	$("#showLinksId").hover(function(){
	        	var groupType=$("#syllabusEdit_groupType_select").val();
	        	var ret=$('#studioLinkSpan input[id^=studioLink_]').map(function(){
	        		if($(this).attr("t")==2){
	        			return null;
	        		}
	        		var lrowList=Syllabus.getLiveLinks($(this).attr("t"),groupType);
	        		var val=$.trim($(this).val());
	        		var tipVal='<strong style="color:red;">地址配置有误，请检查！</strong>';
	        		var name='';
	        		if(val){
	        			for(var rw in lrowList){
	        				if(lrowList[rw].url==val){
	        					name=lrowList[rw].name;
	        					tipVal="【"+name+"】"+val;
	        				}
	        			}
	        			if(val.indexOf("yy")!=-1){
	        				tipVal="【YY】"+val;
	        				name='yy';
	        			}
	        		}
	        		tipVal=$("#studioLinkSelect").find("option[value='"+$(this).attr("t")+"']").text()+"-->"+tipVal;
	    			return '<p style="width:500px;"><label style="display:none;">'+(isValid(name)?name:'')+'</label>'+tipVal+'</p>';
	    		}).get().join("\n");
	        	$("#showLinksDiv").show().html('<p style="width:500px;">直播地址详情如下(备注：红色表示地址配置可能有误,请检查确认！):</p>'+ret);
	        	var pmc=$("#showLinksDiv p");
	        	pmc.removeClass('p-cl');
	        	var lpSize=$('#showLinksDiv p label:contains("yy")').length>0?2:3;
	        	if(pmc.find("strong").length==0 && pmc.length>1){
	        		var hasError=$('#showLinksDiv p label:contains("大陆")').length<lpSize && $('#showLinksDiv p label:contains("海外")').length<lpSize;
		        	if(!hasError){
		        		hasError=$('#showLinksDiv p label:contains("'+$('#showLinksDiv p label:last').text().split(":")[1]+'")').length<lpSize;
		        	}
		        	if(hasError){
		        		pmc.addClass('p-cl');
		        	}
	        	}
    	    },
	    	function(){
	    		$("#showLinksDiv").hide();
	    	}
        );
    	
    	$("#studioLinkSelect").change(function(){
    		if(this.value=='3' || this.value=='4' || this.value=='1'){
    			$('#studiolinkAddr').html("");
    			var currLinks=Syllabus.getLiveLinks(this.value,$("#syllabusEdit_groupType_select").val());
    			$('#studiolinkAddr').append('<option value="">--请选择--</option>');
    			for(var lk in currLinks){
    				$('#studiolinkAddr').append('<option value="'+currLinks[lk].url+'">'+currLinks[lk].name+'</option>');
    			}
    			$('#studiolinkAddr').val("").show();
    		}else{
    			$('#studiolinkAddr').hide();
    		}
    		var studioLinkVal=Syllabus.getStudioLink(this.value);
    		$(this).parent().find('input[id^=studioLink_]').hide();
    		var valObj=$("#studioLink_"+this.value);
    		if(isBlank(valObj.val())){
    			valObj.val(studioLinkVal);
    		}
    		if(isValid(valObj.val())){
				$('#studiolinkAddr').val(valObj.val());
			}
    		valObj.show();
    	});
    	$('#studiolinkAddr').change(function(){
    		if(isValid(this.value)){
    			$('#studioLink_'+$("#studioLinkSelect").val()).val(this.value);
    		}else{
    			$('#studioLink_'+$("#studioLinkSelect").val()).val('');
    		}
    	});
    	this.setStudioLink($("#syllabusEdit_groupType_select").val());
    	Syllabus.getStudioLink(null,true);
    	//头tab点击事件
   	 	$("#panel_editSyllabus .courseThCls").click(function(){
	   		$("#panel_editSyllabus .courseThCls").removeClass("clickThCls");
	   		$(this).addClass("clickThCls");
	   		$("#panel_editSyllabus td.main").removeClass("clickTdCls").hide();
	   		$("#panel_editSyllabus td.main[tid=courseTd_"+$(this).index()+"]").addClass("clickTdCls").show();
	   		var sdom=$(this).find("select[name=status]");
	   		if(sdom.val()!="1"){
   				sdom.trigger("change");
   			}
   	 	});
   	 	//房间组及房间选择
   	 	Syllabus.setRoomSelect($("#syllabusEdit_groupType_select").val());
   	 	$("#syllabusEdit_groupType_select").change(function(){
   		     Syllabus.setRoomSelect(this.value);
   		     Syllabus.setStudioLink(this.value);
	    });
   	 	
   	    /**
         * 休市或无效(全天)
         */
        $("#panel_editSyllabus thead select[name='status']").change(function () {
    		var disable=this.value!="1";
    		var pMain=$("#panel_editSyllabus tbody:visible td.main[dy="+$(this).parent().attr("dy")+"]");
    		var txtEl=pMain.find("select[name=status],select[name=courseType],textarea,input[name=title]").prop("disabled", disable);
    		if(disable){
    			txtEl.val("");
     			pMain.find("select[id^='select_lecturer']").combotree("disable").combotree("clear");
    		}else{
    			pMain.find("select[id^='select_lecturer']").combotree("enable");
    		}
        });
        //新增默认状态为无效
        if(isBlank($("#courses_hidden_id").val())){
        	$("#panel_editSyllabus .courseThCls[dy=0] select[name=status],#panel_editSyllabus .courseThCls[dy=6] select[name=status]").val(2);
        }
    	this.coursesData=$("#courses_data_id").val();
    	if(this.coursesData){
    		this.coursesData=JSON.parse(this.coursesData);
    		//设置星期的状态
            var loc_days = this.coursesData.days || [];
            $("#panel_editSyllabus table:first").find("thead tr th[dy]").each(function(){
            	var hasRow=false;
            	for(var i = 0, lenI = loc_days.length; i < lenI; i++)
                {
            		if($(this).attr("dy")==loc_days[i].day){
            			$(this).find("select[name=status]").val(loc_days[i].status);
            			hasRow=true;
            			break;
            		}
                }
            	if(!hasRow){
            		$(this).find("select[name=status]").val("2");
            	}
        	});
            //设置课程
            if(this.coursesData.hasOwnProperty("timeBuckets"))
            {
                var loc_timeBuckets = this.coursesData.timeBuckets;
                for(var i in loc_timeBuckets)
                {  
                	var timeBucket=loc_timeBuckets[i];
                	var loc_courses = {},course=timeBucket.course;
                    for(var k=0;k<course.length;k++)
                    {
                        loc_courses[loc_days[k].day + ""] = course[k];
                    }
                    Syllabus.setCourseByClone(timeBucket,loc_courses);
                }
            }
            $("#panel_editSyllabus .courseThCls:first").click();
    	 }
    	
        /**
         * 添加
         */
        $("#panel_editSyllabus table:first a.ope-add").bind("click", function () {
        	Syllabus.setCourseByClone(null,null);
        });

        /**
         * 移除
         */
        $("#panel_editSyllabus table:first a.ope-remove").bind("click", function () {
            $(this).parents("tbody:first").remove();
        });
    },
	
	/**
	 * 获取房间名称
	 * @param chatGroupType
	 * @param chatGroupId
	 * @returns
	 */
	getGroupName : function(chatGroupType, chatGroupId)
	{
		var loc_result = Syllabus.chatGroups[chatGroupType + ","];
		if(!!chatGroupId)
		{
			loc_result += "-" + (Syllabus.chatGroups[chatGroupType + "," + chatGroupId] || chatGroupId);
		}
		return loc_result; 
	},
	
	/**
	 * 预览
	 * @param item
	 */
	view : function(item){
		var loc_syllabusId = $(item).siblings("input").val();
		goldOfficeUtils.openSimpleDialog({
			dialogId : "viewSyllabusWin",
			title : '课程表预览',
			width : 1050,
			height : 560,
			onOpen : function(){
				Syllabus.getSyllabus(loc_syllabusId);
			},
			buttons	 : [{
				text : '关闭',
				iconCls : "ope-close",
				handler : function() {
					$("#viewSyllabusWin").dialog("close");
				}
			}]
		});
	},
	
	/**
	 * 获取课程表记录
	 * @param syllabusId
	 */
	getSyllabus : function(syllabusId)
	{
		$("#panel_viewSyllabus table").html("");
		goldOfficeUtils.ajax({
			url : basePath +'/chatSyllabusController/view.do?id=' + syllabusId,
			type : 'get',
			success : function(data){
				var loc_syllabus = data;
				$("#panel_viewSyllabus").data("syllabus", loc_syllabus);
				$("#panel_viewSyllabus span:first").html(Syllabus.getGroupName(loc_syllabus.groupType, loc_syllabus.groupId));
				if(loc_syllabus && loc_syllabus.courses)
				{
					var loc_nowTime = new Date();
					var loc_currTime = {
							day : loc_nowTime.getDay(),
							timeLong : loc_nowTime.getTime(),
							time : (loc_nowTime.getHours() < 10 ? "0" : "") + loc_nowTime.getHours() + ":"
							+ (loc_nowTime.getMinutes() < 10 ? "0" : "") + loc_nowTime.getMinutes()
					};
					var loc_style = 1;
					if(loc_syllabus.groupType === "wechat" || loc_syllabus.groupType === "fxchat")
					{
						loc_style = 2;
					}
					var loc_syllabusPanel = $("#panel_viewSyllabus div.syllabus");
					loc_syllabusPanel.html(Syllabus.viewSyllabus(loc_syllabus.courses, loc_currTime, loc_style));
                    if(loc_style == 1)
                    {
                    	loc_syllabusPanel.find(".sy_nav a").bind("click", function(){
                            var loc_this = $(this);
                            loc_this.siblings(".active").removeClass("active");
                            loc_this.addClass("active");
                            var loc_day = loc_this.attr("d");
                            var loc_panel = loc_this.parent().next();
                            loc_panel.find("tbody:visible").hide();
                            loc_panel.find("tbody[d='" + loc_day + "']").show();
                        });
                    	loc_syllabusPanel.find(".sy_nav a.active").trigger("click");
                    };
				}
				else
				{
					var loc_html = '<table cellpadding="0" cellspacing="1" border="0" class="tableForm_L" style="width: 730px;">';
					loc_html += '<tr><th>未设置任何课程表，请点击修改按钮！</th></tr>';
					loc_html += '</table>';
					$("#panel_viewSyllabus div.syllabus").html(loc_html);
				}
			}
		});
	},
	
	/**
	 * 修改
	 * @param item
	 */
	edit : function(item)
	{
		var loc_syllabusId = !item ? "" : $(item).siblings("input").val();
		var loc_url = formatUrl(basePath + '/chatSyllabusController/preEdit.do?id=' + loc_syllabusId);
		goldOfficeUtils.openEditorDialog({
			title : '新增/编辑课程表',
			width : 1050,
			height : 600,
			href : loc_url,
			iconCls : 'ope-edit',
			handler : function(){
				var gtype=$("#syllabusEdit_groupType_select").val();
				if(!gtype){
					$.messager.alert($.i18n.prop("common.operate.tips"),'房间组别不能为空！','error');
					return ;
				}
				if(!$("#syllabus_publishStart").val() || !$("#syllabus_publishEnd").val()){
					$.messager.alert($.i18n.prop("common.operate.tips"),'发布时间不能为空！','error');
					return ;
				}
				$("#form_editSyllabus input[name='courses']").val(Syllabus.getCoursesJson());
				if(gtype.indexOf("studio")!=-1){
					$("#studioLink_hidden_id").val(JSON.stringify($('#studioLinkSpan input[id^=studioLink_]').map(function(){
						return {code:$(this).attr("t"),url:$(this).val()};
					}).get()));
				}
				goldOfficeUtils.ajaxSubmitForm({
					url : formatUrl(basePath + '/chatSyllabusController/edit.do'),
					formId : 'form_editSyllabus',
					onSuccess : function(data){   //提交成功后处理
						var d = $.parseJSON(data);
						if (d.code === "OK") {
							$("#myWindow").dialog("close");
                            Syllabus.refresh();
							$.messager.alert($.i18n.prop("common.operate.tips"),'保存成功!','info');
						}else{
							$.messager.alert($.i18n.prop("common.operate.tips"),'保存失败','error');
						}
					}
				});
			},
			onLoad : function(){
				var loc_panel = $("#panel_editSyllabus");
                Syllabus.setEditEvent();
			}
		});
	},
	
	/**
	 * 删除
	 * @param item
	 */
	del : function(item){
		var loc_id = $(item).siblings("input").val();
		var url = formatUrl(basePath + '/chatSyllabusController/delete.do');
		goldOfficeUtils.deleteOne(Syllabus.gridId, loc_id, url, "确认删除吗？");
	},
	
	/**
	 * 刷新
	 */
	refresh : function(){
		$('#'+Syllabus.gridId).datagrid('reload');
	},

    /**
     * 获取课程json字符串
     * {days : [{day: Integer, status : Integer}], timeBuckets : [{startTime : String, endTime : String, course : [{context : String, courseType : Integer,lecturer:String,lecturerId:String, title : String, status : Integer}]}]}
     */
    getCoursesJson : function()
    {
    	var $panel=$("#panel_editSyllabus table.tableForm_L"),allowDayTd="";
        //星期
        var weekArr=$panel.find("thead .courseThCls").map(function(){
        	var status=$(this).find("select[name='status']").val();
            //仅记录有效或者休市的课程
        	if(status!== "2"){
        		var day=$(this).attr("dy");
        		allowDayTd+=(allowDayTd?",":"")+".main[dy="+day+"]";//记录有效的td
        		return {day:parseInt(day),status:parseInt(status)};
        	}else{
        		return null;
        	}
        }).get();
        if(isValid(allowDayTd)){
        	//课表
        	var allTime=$panel.find("tbody:visible").map(function(){
        		var courseArr=$(this).find(allowDayTd).map(function(){
                	 var lectDom=$(this).find("select[id^='select_lecturer']");
                	 var txt=lectDom.combotree("getText");
                	 if(txt){
                		 txt=txt.replace("请选择","");
                	 }
                	 var currContent=$(this).find("textarea[name='context']").val();
                	 currContent=isValid(currContent)?currContent.replace(/[\r\n]/g,""):'';
                     return {title:$(this).find("input[name='title']").val().replace(/["']+/g,""),
    	    	        	 status:parseInt($(this).find("select[name='status']").val()),
    	    	        	 courseType:parseInt($(this).find("select[name='courseType']").val()),
    	    	        	 lecturer:txt,lecturerId:lectDom.combotree("getValues").join(","),
    	    	        	 context : currContent};
                }).get();
        		return {startTime: $(this).find("input[name='startTime']").timespinner('getValue'), endTime : $(this).find("input[name='endTime']").timespinner('getValue'),course:courseArr};
        	}).get();
			console.log(allTime);
            return JSON.stringify({
                days : weekArr,
                timeBuckets : allTime.sort(arraySort("startTime",false))
            });
        }else{
        	return "";
        }
    },

    /**
     * 格式化显示课程安排表
     * @param syllabus {{days : [{day: Integer, status : Integer}], timeBuckets : [{startTime : String, endTime : String, course : [{lecturer : String, title : String, status : Integer}]}]}}
     * @param currTime {{day : Integer, time : String}}
     * @param style 类型 1-直播间 2-微解盘
     */
	viewSyllabus : function(syllabus, currTime, style){
        var loc_constants = {
            dayCN : ['星期天','星期一','星期二','星期三','星期四','星期五','星期六'],
            indexCN : ['第一节','第二节','第三节','第四节','第五节','第六节','第七节','第八节'],
            courseCls : ['prev', 'ing', 'next'],
            tableCls : "tableForm_L"
        };
        //计算时间状态,返回对应的样式
        var loc_timeClsFunc = function(day, startTime, endTime, currTime, notCheckTime){
            var loc_index = 0;
            var tempDay = (day + 6) % 7; //将1,2,3,4,5,6,0转化为0,1,2,3,4,5,6
            var currTempDay = (currTime.day + 6) % 7;
            if(tempDay > currTempDay){
                loc_index = 2;
            }else if(tempDay < currTempDay){
                loc_index = 0;
            }else{
                if(notCheckTime){
                    loc_index = 1;
                }else if(endTime <= currTime.time){
                    loc_index = 0;
                }else if(startTime > currTime.time){
                    loc_index = 2;
                }else{
                    loc_index = 1;
                }
            }
            return loc_constants.courseCls[loc_index];
        };

        var loc_html = [];
        try
        {
            if(syllabus)
            {
                var loc_courses = JSON.parse(syllabus);
                var loc_dayLen = !loc_courses.days ? 0 : loc_courses.days.length;
                if(loc_dayLen > 0)
                {
                    if(style === 1)
                    {
                        loc_html.push('<div class="sy_panel">');
                        //星期（头部）
                        var loc_startDateTime = currTime.timeLong - 86400000 * ((currTime.day + 6) % 7);
                        var loc_date = null;
                        var loc_dateStr = null;
                        var loc_activeCls = null;
                        loc_html.push('<div class="sy_nav">');
                        var loc_width = ' style="width:' + (100 / loc_dayLen) + '%;"'
                        for(var i = 0; i < loc_dayLen; i++)
                        {
                            loc_date = new Date(loc_startDateTime + ((loc_courses.days[i].day + 6) % 7) * 86400000);
                            loc_dateStr = loc_date.getFullYear() + "-"
                                        + (loc_date.getMonth() < 9 ? "0" : "") + (loc_date.getMonth() + 1) + "-"
                                        + (loc_date.getDate() < 10 ? "0" : "") + loc_date.getDate();
                            loc_activeCls = (loc_courses.days[i].day == currTime.day) ? ' class="active"' : "";
                            loc_html.push('<a href="javascript:void(0)" d="' + loc_courses.days[i].day + '"' + loc_activeCls + loc_width + '>' + loc_constants.dayCN[loc_courses.days[i].day] + '<br/></a>');
                        }
                        loc_html.push('</div>');
                        //课程
                        loc_html.push('<div class="sy_cont"><div>');
                        loc_html.push('<table cellpadding="0" cellspacing="1" border="0" class="' + loc_constants.tableCls + '">');
                        var loc_timeBucket = null;
                        var loc_timeCls = null;
                        for(var i = 0; i < loc_dayLen; i++)
                        {
                            loc_html.push('<tbody d="' + loc_courses.days[i].day + '">');
                            if(loc_courses.days[i].status == 0)
                            {
                                //全天休市
                                loc_timeCls = loc_timeClsFunc(loc_courses.days[i].day, null, null, currTime, true);
                                loc_html.push('<tr class="' + loc_timeCls + '" width="100%"><td>休市</td></tr>');
                            }
                            else
                            {
                                for(var j = 0, lenJ = loc_courses.timeBuckets.length; j < lenJ; j++)
                                {
                                    loc_timeBucket = loc_courses.timeBuckets[j];
                                    loc_timeCls = loc_timeClsFunc(loc_courses.days[i].day, loc_timeBucket.startTime, loc_timeBucket.endTime, currTime, false);
                                    if(loc_timeBucket.course[i].status == 0)
                                    {
                                    	//时间段休市
                                    	loc_html.push('<tr class="' + loc_timeCls + '">');
                                    	loc_html.push('<td width="25%">' + loc_timeBucket.startTime + '-' + loc_timeBucket.endTime + '</td>');
                                        loc_html.push('<td colspan="2" width="75%">休市</td>');
                                        loc_html.push('</tr>');
                                    }
                                    else if(loc_timeBucket.course[i].lecturer)
                                    {
                                    	//讲师内容不为空才会显示对应的时间段
                                    	loc_html.push('<tr class="' + loc_timeCls + '">');
                                    	loc_html.push('<td width="25%">' + loc_timeBucket.startTime + '-' + loc_timeBucket.endTime + '</td>');
                                        loc_html.push('<td width="25%">' + loc_timeBucket.course[i].lecturer + '</td>');
                                        loc_html.push('<td width="50%">' + loc_timeBucket.course[i].title + '</td>');
                                        loc_html.push('</tr>');
                                    }
                                }
                            }
                            loc_html.push('</tbody>');
                        }
                        loc_html.push('</table>');
                        loc_html.push('</div></div>');
                        loc_html.push('</div>');
                    }
                    else if(style === 2)
                    {
                        loc_html.push('<table cellpadding="0" cellspacing="1" border="0" class="' + loc_constants.tableCls + '">');
                        //头部
                        loc_html.push('<tr>');
                        loc_html.push('<th>星期\\节次</th>');
                        var lenJ = !loc_courses.timeBuckets ? 0 : loc_courses.timeBuckets.length;
                        var loc_timeBucket = null;
                        var loc_timeCls = null;
                        for(var j = 0; j < lenJ; j++)
                        {
                            loc_timeBucket = loc_courses.timeBuckets[j];
                            loc_html.push('<th>' + loc_constants.indexCN[j] + "<br><span>(" + loc_timeBucket.startTime + "-" + loc_timeBucket.endTime + ')</span></th>');
                        }
                        loc_html.push('</tr>');
                        //课程
                        var loc_day = null;
                        for(var i = 0, lenI = !loc_courses.days ? 0 : loc_courses.days.length; i < lenI; i++)
                        {
                            loc_day = loc_courses.days[i];
                            loc_html.push('<tr>');
                            loc_html.push('<th>' + loc_constants.dayCN[loc_day.day] + '</th>');
                            if(loc_day.status == 0)
                            {
                                //全天休市
                                loc_timeCls = loc_timeClsFunc(loc_day.day, null, null, currTime, true);
                                loc_html.push('<td class="' + loc_timeCls + '" colspan="' + lenJ + '">休市</td>');
                            }
                            else
                            {
                                for(var j = 0; j < lenJ; j++){
                                    loc_timeBucket = loc_courses.timeBuckets[j];
                                    loc_timeCls = loc_timeClsFunc(loc_day.day, loc_timeBucket.startTime, loc_timeBucket.endTime, currTime, false);
                                    loc_html.push('<td class="' + loc_timeCls + '">' + (loc_timeBucket.course[i].status == 0 ? "休市" : loc_timeBucket.course[i].lecturer) + '</td>');
                                }
                            }
                            loc_html.push('</tr>');
                        }
                        loc_html.push('</table>');
                    }
                }
            }
        }
        catch(e1)
        {
            console.error("formatSyllabus->"+e1);
            return "";
        }
        return loc_html.join("");
    }
};
		
//初始化
$(function() {
	Syllabus.init();
});