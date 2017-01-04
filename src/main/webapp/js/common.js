/**
 * 通用方法
 * create by alan.wu
 * 2014-3-6
 */
/**
 * 功能：删除数组中某个下标的元素
 */
/*Array.prototype.remove = function(index){ 
    if(isNaN(index) || index > this.length){
    	return false;
    }
    this.splice(index,1); 
};*/
function joinSplit(val){
	return ",".concat(val).concat(",");
}
function isBlank(v) {
    return v == undefined || v == null || $.trim(v) == ''||v=='undefined'||v=='null';
}
function isValid(obj) {
    return !isBlank(obj);
}
function validObj(obj) {
    return (isValid(obj) && !$.isEmptyObject(obj));
}
/**
 * 功能：验证是否为email
 * @param val  验证的值
 */
function isEmail(val){
	return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(val));
}
/**
 * 功能：验证输入金额格式是否正确
 * @param money  金额
 */
function isMoney(val){   
	var str =  /^[0-9]+(.[0-9]{1,2})?$/;
	return  str.test(val);
}
/**
 * 功能：格式化为2位小数
 */
function toFix2Decimal(x) {
	if(isNaN(x)){
		return x;
	}
	var f = parseFloat(x);
	if (isNaN(f)) {
		return 0.00;
	}
	var f = Math.round(x * 100) / 100;
	var s = f.toString();
	var rs = s.indexOf('.');
	if (rs < 0) {
		rs = s.length;
		s += '.';
	}
	while (s.length <= rs + 2) {
		s += '0';
	}
	return s;
}
/**
 * 功能：格式化为1位小数
 */
function toFix1Decimal(x) {
	if(isNaN(x)){
		return x;
	}
	var f = parseFloat(x);
	if (isNaN(f)) {
		return 0.0;
	}
	var f = Math.round(x * 10) / 10;
	var s = f.toString();
	var rs = s.indexOf('.');
	if (rs < 0) {
		rs = s.length;
		s += '.';
	}
	while (s.length <= rs + 1) {
		s += '0';
	}
	return s;
}
/**
 * 功能：input只能输入数字
 */
function allowNumKeyPress(event){
	var keyCode = event.which;
	if(navigator.userAgent.indexOf('Firefox') >= 0 && keyCode == 0){  // 火狐下 delete键盘 、左右方向键盘 keycode都是0,特殊处理
		return true;
	}
	if (keyCode == 46 || keyCode == 8 || (keyCode >= 48 && keyCode <= 57)) {
		return true;
	} else {
		return false;
	}
}
/**
 * 功能：判断是否2位小数(包括小于2位)
 */
function isTwoDecimal(value){
    if(value != null && value != ''){
        var decimalIndex = value.indexOf('.');
        if(decimalIndex != -1){
        	var decimalPart = value.substring(decimalIndex+1,value.length);  
            if(decimalPart.length > 2){
            	return false;    
            }else{
                return true;
            }
        }else{
        	return true;
        }
    }
    return false;
}
/**
 * 功能：格式化開始時間
 */
function formatStartDate(startDate){
	return startDate+" 00:00:00";
}
/**
 * 功能：格式化結束時間
 */
function formatEndDate(endDate){
	return endDate+" 23:59:59";
}
function getParams(params) {
    if (params == null) {
        return null;
    }
    var result = "";
    for (var item in params) {
        if (result.length > 0) {
            result += "&";
        }
        if (params[item] == null || params[item] == undefined) {
            result += (item + "=");
        } else result += (item + "=" + params[item]);
    }
    result = encodeURI(result);
    return result;
}
function getFormFieldsValue(form) {
    var result = {};
    $("input[name][type!=button][type!=submit]", $(form)).each(function () {
        if (this.type == "checkbox") {
            result[this.name] = ($(this).attr("checked") == "checked" ? 1 : 0);   // 0表示没选,1表示选中
        } else {
            result[this.name] = this.value;
        }
    });
    $("textarea[name]", $(form)).each(function () {
        result[this.name] = this.value;
    });
    $("select[name]", $(form)).each(function () {
        result[this.name] = (this.value=="--"?"":this.value);
    });
    return result;
}

/**
 * 空值或未定义值转成空字符
 * @param val
 */
function nullValToEmpty(val){
	return isBlank(val)?"":val;
}
/**
 * 将form数据转化为json字符串
 * @param formIds 一个或多个，多个则生成json数组
 * @returns {*}
 */
function formFieldsToJson(formIds) {
    if(isBlank(formIds)){
        return "";
    }
    var setOrPush = function(target, key, value){
        if(target.hasOwnProperty(key)){
            if(target[key] instanceof  Array){
                target[key].push(value);
            }else{
                target[key] = [target[key], value];
            }
        }else{
            target[key] = value;
        }
    };

    var arr=[];
    if($.isArray(formIds)){
        arr=formIds;
    }else if(validObj(formIds)){
        arr=$.makeArray(formIds);
    }else{
        arr[0]=formIds;
    }
    var ObjArr=[], obj = null,$form="";
    for(var i=0;i<arr.length;i++){
        $form = $(arr[i]);
        obj = {};
        $form.find("input[name][type!=button][type!=submit][type!=checkbox]").each(function(){
            var $this = $(this);
            setOrPush(obj, $this.attr("name"), nullValToEmpty($this.val()));
        });
        $form.find("input[name][type=checkbox]:checked").each(function(){
            var $this = $(this);
            setOrPush(obj, $this.attr("name"), nullValToEmpty($this.val()));
        });
        $form.find("textarea[name]").each(function(){
            var $this = $(this);
            var loc_val = nullValToEmpty($this.val());
            loc_val = loc_val.replace(/[\f\n\r\t\v]/g, "");
            setOrPush(obj, $this.attr("name"), loc_val);
        });
        $form.find("select[name]").each(function(){
            var loc_name = $(this).attr("name");
            $(this).find("option:selected").each(function(name){
                setOrPush(obj, name, nullValToEmpty($(this).val()));
            }, [loc_name]);
        });
        ObjArr.push(obj);
    }
    var result = null;
    if(ObjArr.length == 1){
        result = ObjArr[0];
    }else if(ObjArr.length > 1){
        result = ObjArr;
    }
    return JSON.stringify(result);
}

/**
 * 数组转json字符串
 * @param arr
 * @returns
 */
function arrToJson(arr){
	if(isBlank(arr)){
		return "";
	}
    var result="";
    if($.isArray(arr)){
    	var subArr=[];
    	for(var i=0;i<arr.length;i++){
    		subArr=arr[i];
    		var str="";
    		for(var name in subArr){
    			str+="\""+name+"\":\""+subArr[name]+"\""+",";
    		}
    		result+="{"+str.substring(0, str.length-1)+"}";
    		if(i!=arr.length-1){
    			result+=",";
    		}
    	}
    }else{
		for(var name in arr){
			result+="\""+name+"\":\""+arr[name]+"\""+",";
		}
		result="{"+result.substring(0, result.length-1)+"}";
	}
    return "["+result+"]";
}


/**
 * 通用ajax方法
 * @param url
 * @param params
 * @param callback
 * @param async
 * @returns
 */
function getJson(url, params, callback,async) {
    var result = null;
    $.ajax({
        url: url,
        type: "POST",
        timeout : 60000, 							//超时时间设置，单位毫秒
        cache: false,
        async: async!=undefined?async:false,
        dataType: "json",
        data: params/*getParams(params)*/,
        complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
        	if(status=='timeout'){					//超时,status还有success,error等值的情况
        		alert("请求超时,请重试!");
        	}
        },
        success: typeof (callback) == "function" ? callback : function (data) {
            result = data;
        },
        error: function (obj,data) {
            if (isValid(obj.responseText)) {
                if (obj.statusText != "OK") window.document.body.innerHTML = obj.responseText;
            }else{
            	alert("请求超时,请重试!");
            }
        }
    });
    return result;
}

/**
 * 功能：获取数据字典列表(异步)
 * @param  curSelectId          当前select的Id
 * @param  parentCode           父字典code
 * @param  selectedDictionary   选中的字典code(主要用于修改的时候)
 * @param  isShowPleaseSelected  是否显示"请选择"
 * @param  isFileter             是否过滤下拉选项
 * @param  fileterArr       	 下拉选项数组
 */
function getDictionaryList(curSelectId,parentCode,selectedDictionaryCode,isShowPleaseSelected,isFileter,fileterArr){
	selectedDictionaryCode = selectedDictionaryCode === undefined ? "" : selectedDictionaryCode;
	isShowPleaseSelected = isShowPleaseSelected === undefined ?true : isShowPleaseSelected;
	isFileter = isFileter === undefined ? false : isFileter;
	fileterArr = fileterArr === undefined ? "" : fileterArr;
	if(parentCode){
		var tempData = eval('('+dictionaryTempData+')');
		$curSelectId = $("#"+curSelectId);
		$curSelectId.find("option").remove();
		if(isShowPleaseSelected){
			if(selectedDictionaryCode != ''){
				$curSelectId.append("<option value=''>---请选择---</option>");
			}else{
				$curSelectId.append("<option value='' selected=\"selected\">---请选择---</option>");
			}
		}
		$.each(tempData,function(i,obj){   //先找出父节点dictId
			   if(obj.parentCode != '' && obj.parentCode == parentCode){
					var dictinaryName='';
					if(locale == "zh_TW"){
						dictinaryName = obj.nameTW;
					}else if(locale == "zh_CN"){
						dictinaryName = obj.nameCN;
					}
					else if(locale == "en_US"){
						dictinaryName = obj.nameEN;
					}
					if(isFileter){
						if(fileterArr != '' && $.inArray(obj.code,fileterArr) != -1){
							return true;
						}
					}
					if(selectedDictionaryCode != '' && obj.code == selectedDictionaryCode){
						$curSelectId.append("<option value='"+obj.code+"' selected=\"selected\">"+dictinaryName+"</option>");
					}else{
						$curSelectId.append("<option value='"+obj.code+"'>"+dictinaryName+"</option>");
					}
			   }
			   
			   
			   
		});
		
	}
}

/**
 * 功能：根据字典code --> 获取字典名称
 * @param  dictionaryCode   据字典code
 */
function getDictionaryName(dictionaryCode){
	if(dictionaryCode == '' || dictionaryCode === undefined){
		return '';
	}
	var tempData = eval('('+dictionaryTempData+')'),dName = "";
	$.each(tempData,function(i,obj){
		if(obj.code == dictionaryCode){ 
			if(locale == "zh_TW"){
				dName = obj.nameTW;
			}else if(locale == "zh_CN"){
				dName = obj.nameCN;
			}else if(locale == "en_US"){
				dName = obj.nameEN;
			}else{
				dName = obj.nameTW;
			}
		    return dName;
		}
	});
	return dName;
}

/**
 * 功能：格式化应用平台
 * @param value   选择的应用平台(多个平台用","隔开)
 * @param listStr 数据字典对应的json字符串
 */
function formatPlatfrom(value,listStr){
	if(isBlank(value)){
		return "";
	}
	var platformJson = $.parseJSON(listStr);
	var row=null,result=[];
	var valArr=value.split(",");
	for(var i=0;i<valArr.length;i++){
		for(var index in platformJson){
			row = platformJson[index];
			if(valArr[i] == row.code){
				result.push(row.nameCN);
				break;
			}
		}
	}
	return result.join("，");
}

/**
 * 功能：以页面跳转的方式跳转回到主页
 */
function jumpRequestPage(href){
	var $center = $('#yxui_main_tabs');
	var tab = $center.tabs('getSelected');
	var options = tab.panel('options');
	options.returnUrl = options.href;
	options.href = href;
	tab.panel(options);
	tab.panel('refresh');
}

/**
 * 功能：选择国家时-->联动省份
 * @param countryVal        国家对应的值
 * @param selectProviceId  省份对应的Id
 * @param isCascadeSelectCity  是否级联选择市
 * @param selectCityId     市对应的Id
 */
function showProvinceWithCountryId(countryVal,selectProviceId,isCascadeSelectCity,selectCityId){
	var selId = $("#"+selectProviceId),dName = "";
	selId.find("option").remove();
	if(countryVal == ''){
		return;
	}
	getJson(formatUrl(path + '/regionController/getProvinceList.do'),{"id" : countryVal},function(data){
    	if(data.length > 0){
    		$.each(data,function(i,obj){
    			if(locale == "zh_TW"){
    				dName = obj.name;
    			}else if(locale == "zh_CN"){
    				dName = obj.name;
    			}else if(locale == "en_US"){
    				dName = obj.nameEN;
    			}else{
    				dName = obj.name;
    			}
    			if(i == 0){
    				selId.append("<option value='"+obj.id+"' selected=\"selected\">"+dName+"</option>");
    			}else{
    				selId.append("<option value='"+obj.id+"'>"+dName+"</option>");
    			}
    			
    		})
    		//国家选择完后，联动选择省份和联动选择市，默认是选中安徽省以及对应的市
			if(isCascadeSelectCity){
				if(selId.find("option:selected").val() == 93){  //如果选中的省份是安徽，默认带出安徽省对应的市
					showCityListWithProvinceId(93,selectCityId);
				}else{
					$("#"+selectCityId).find("option").remove();
					$("#"+selectCityId).append("<option value='3424'>"+"国外与其它"+"</option>");
				}
				
			}
    	}
	});
}

/**
 * 功能：选择省份时-->联动显示市
 * @param provinceVal  省份对应的值
 * @param selectId    市的id
 */
function  showCityListWithProvinceId(provinceVal,selectCityId){
	var selId = $("#"+selectCityId),dName = "";
	selId.find("option").remove();
	getJson(formatUrl(path + '/regionController/getCityList.do'),{"id" : provinceVal},function(data){
    	if(data.length > 0){
    		$.each(data,function(i,obj){
    			if(locale == "zh_TW"){
    				dName = obj.name;
    			}else if(locale == "zh_CN"){
    				dName = obj.name;
    			}else if(locale == "en_US"){
    				dName = obj.nameEN;
    			}else{
    				dName = obj.name;
    			}
    			selId.append("<option value='"+obj.id+"'>"+dName+"</option>");
    		})
    	}
	});
}

/**
 * 遍历树，将树的每一个节点作为参数传递给callback。
 * @param tree，结构形如{attrA:valA... children:[{...}]}
 * @param callback
 */
function visitTree(tree, callback){
	if(tree instanceof Array){
		var node = null;
		for(var i = 0, lenI = tree.length; i < lenI; i++){
			node = tree[i];
			callback(node);
			if(node.children){
				visitTree(node.children, callback);
			}
		}
	}else{
		callback(tree);
		if(tree.children){
			visitTree(tree.children, callback);
		}
	}
}

/**
 * 将数组转化为对象，比如[1,2,3]==>{'0':1,'1':2,'2':3}
 * @param arr 待转化的数组对象
 * @param parseCb 转化方法，如果没有该参数，则在转化的时候直接返回对象本身，否则将数组的元素作为参数，返回值赋值给对象属性值。
 */
function convertArr2Obj(arr, parseCb){
	if(typeof parseCb !== "function"){
		parseCb = function(obj){return obj};
	}
	if(arr instanceof Array){
		var loc_obj = {};
		for(var i = 0, lenI = arr.length; i < lenI; i++){
			loc_obj[i + ""] = parseCb(arr[i]);
		}
		return loc_obj;
	}else{
		return arr;
	}
}

/**
 * 格式url
 * 加入menuId参数
 * @param url
 * @returns
 */
function formatUrl(url,params){
	var selectTab = $('#yxui_main_tabs').tabs('getSelected'); 
	if(isValid(selectTab)&&selectTab.length>0){
		url+=(url.indexOf("?")!=-1?"&":"?")+"menuId="+selectTab.panel('options').id;
	}
	var sysFlag = $('#yxui_main_sysfag_select').val(); 
	if(isValid(sysFlag)){
		url+=(url.indexOf("?")!=-1?"&":"?")+"systemCategory="+sysFlag;
	}
	if(validObj(params)){
		url+="&"+getParams(params);
	}
	return url;
}

/**
 * 扩展easyui中不支持自属性对数据获取问题。
 * 当如果需要对字段属性进一步处理的时候，需要将this对象传入。（注：在datagrid中，column.field不允许相同，所以that可以直接传入字段名）
 * 示例1：{title : "用户名",field : 'loginPlatform.financePlatForm.nickName'， formatter:getValue4EasyUI},
 * 示例2：{title : "用户名",field : 'loginPlatform.financePlatForm.nickName'， formatter:function(value, rowData, rowIndex){
 * 		value = getValue4EasyUI(value, rowData, rowIndex, this);
 * 		//value = getValue4EasyUI(value, rowData, rowIndex, 'loginPlatform.financePlatForm.nickName');
 * 		//...
 * 		return ...;
 * }},
 * @param value
 * @param rowData
 * @param rowIndex
 * @param that 
 */
function getValue4EasyUI(value, rowData, rowIndex, that){
	if(!that){
		that = this;
	}
	var loc_field = typeof that === "string" ? that : that.field;
	var value = null;
	try{
		value = eval("rowData['"+loc_field.replace(/\./g,"']['")+"']");
	}catch(e){
		value = "";
	}
	return value;
}

/**
 * 过滤空格
 * @param val
 */
function trimStrVal(val){
	return isBlank(val)?'':$.trim(val);
}

/**
 * 格式化去日期（含时间）
 */
function formatterDate (date,splitChar) {
    if(!splitChar){
        splitChar='-';
    }
    if(!(date instanceof Date)){
        date=new Date(date);
    }
    var datetime = date.getFullYear()
        + splitChar// "年"
        + ((date.getMonth() + 1) >=10 ? (date.getMonth() + 1) : "0"
        + (date.getMonth() + 1))
        + splitChar// "月"
        + (date.getDate() < 10 ? "0" + date.getDate() : date.getDate());
    return datetime;
}
/**
 * 将html转换成字符串(去掉html标签)
 */
function html2str(content){
    return content.replace(/<.*?>/ig ,'');
}
/**
 * 截取指定长度的字符串
 */
function cutStr(content,size){
	if(content&&content.length>size){
		return html2str(content).substring(0,size)+"...";
	}else{
		return html2str(content);
	}
}

/**
 * 提取时分秒
 */
function getHHMMSS(date){
    if(!(date instanceof Date)){
        date=new Date(date);
    }
    var datetime = (date.getHours() < 10 ? "0" + date.getHours() : date.getHours())
        + ":"
        + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes())
        + ":"
        + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds());
    return datetime;
}
/**
 * 格式dateWeekTime 日期
 * @param openDate
 * @returns {String}
 */
function formatDateWeekTime(openDate){
	var weekStrArr = ["周日","周一","周二","周三","周四","周五","周六"];
    var dateStr='';
    if(isValid(openDate)){
        openDate=JSON.parse(openDate);
        if(isValid(openDate.beginDate)){
            dateStr=formatterDate(openDate.beginDate,'.');
        }
        if(isValid(openDate.endDate)){
            if(openDate.endDate!=openDate.beginDate) {
                dateStr += "-" + formatterDate(openDate.endDate, '.');
            }
        }
        var weekTime=openDate.weekTime;
        if(isValid(weekTime)){
            var row=null;
            for(var i in weekTime){
                row=weekTime[i];
            	dateStr+=' ';
            	if(isValid(row.week)){
            		dateStr+="[" + weekStrArr[parseInt(row.week, 10)] + "]";
            	}
                if(isValid(row.beginTime)){
                    dateStr+=row.beginTime ;
                }
                if(isValid(row.endTime)){
                    if(row.endTime!=row.beginTime) {
                        dateStr += "-" + row.endTime ;
                    }
                }
            }
        }
    }
    return dateStr;
}
/**
 * 检查当前日期是否符合日期插件数据
 * @param dateTime
 * @param nullResult 空值结果
 *          1）对于禁言设置，空值表示没有设置禁言，即当前时间不包含在其中。传值false
 *          2）对于聊天规则设置，空值表示永久生效，即当前时间包含在其中。传值true
 */
function dateTimeWeekCheck(dateTime, nullResult){
    if(isBlank(dateTime)){
        return !!nullResult;
    }
    if(!$.isPlainObject(dateTime)){
        dateTime=JSON.parse(dateTime);
    }
    var currDate=new Date(),isPass=false, currDateStr = formatterDate(currDate);
    isPass=isBlank(dateTime.beginDate)||currDateStr>=dateTime.beginDate;
    if(isPass){
        isPass=isBlank(dateTime.endDate)||currDateStr<=dateTime.endDate;
    }
    if(!isPass){
        return false;
    }
    var weekTime=dateTime.weekTime;
    if(isBlank(weekTime)){
        return isPass;
    }
    var row=null,currTime=null,weekTimePass=false;
    for(var i in weekTime){
        row=weekTime[i];
        if(isValid(row.week) && currDate.getDay()!=parseInt(row.week)){
            continue;
        }
        if(isBlank(row.beginTime) && isBlank(row.beginTime)){
            return true;
        }
        currTime=getHHMMSS(currDate);
        weekTimePass=isBlank(row.beginTime)||currTime>=row.beginTime;
        if(weekTimePass){
            weekTimePass=isBlank(row.endTime)||currTime<=row.endTime;
        }
        if(weekTimePass){
           break;
        }
    }
    return weekTimePass;
};
/**
 * 判断客户端是否手机
 * @param userAgent
 */
function isMobile(userAgent){
	return /(iphone|ipod|ipad|android|mobile|playbook|bb10|meego)/.test((userAgent || "").toLowerCase());
};
/**
 * 对象数组排序
 * @param key 对象的key值
 * @param desc true 为降序，false升序
 * @returns {Function}
 */
function arraySort(key,desc){
	return function(a,b){
		var result = a[key] < b[key] ? -1 : ((a[key] > b[key]) ? 1 : 0);
		if(desc){
			result = result * -1;
		}
		return result;
	};
}

/**
 * 判定平台
 * @param platform GW-PM+FX HX-HX
 */
function isPlatform(platform){
	if(!platform){
		return false;
	}
	var type = "GW";
	var url = location.href;
	if(/handanadmin\.hx9999\.com/.test(url)){
		type = "HX";
	}else if(/pmmis\.24k\.hk/.test(url)){
		type = "GW";
	}
	return type == platform.toUpperCase();
}

/**
 * 将手机号格式化为userId
 * @param mobilePhone
 * @returns {String}
 */
function formatMobileToUserId(mobilePhone){
    var str=[];
    str[0]='p',str[1]='x',str[2]='i',str[3]='u',str[4]='d',str[5]='c',str[6]='v',str[7]='s',str[8]='n',str[9]='f';
    var userId='';
    for(var i=0;i<mobilePhone.length;i++){
        userId+=str[parseInt(mobilePhone.charAt(i))];
    }
    var index1=Math.floor(Math.random() * 10),index2=Math.floor(Math.random() * 10);
    return str[index1]+userId+str[index2];
}


/**
 * 包含字符，逗号分隔
 * @param src
 * @param subStr
 */
function containSplitStr(src,subStr){
    if(isBlank(src)||isBlank(subStr)) {
        return false;
    }
    return (','+src+',').indexOf((','+subStr+','))!=-1;
}

/**
 * 判断{}对象是否为空
 * @param e
 * @returns {Number}
 */
function isEmptyObject(e) {  
    var t;  
    for (t in e)  
        return !1;  
    return !0  
}  

/*替换字符串中占位符 扩展方法 begin*/
String.prototype.formatStr=function() {
    if(arguments.length==0) return this;
    for(var s=this, i=0; i<arguments.length; i++)
        s=s.replace(new RegExp("\\{"+i+"\\}","g"), (arguments[i] == null || arguments[i] == undefined) ? "" : arguments[i]);
    return s;
};
/*替换字符串中占位符 扩展方法 end*/

//语言类型
var LOCALE_ZH_TW = "zh_TW";
var LOCALE_ZH_CN = "zh_CN";
var LOCALE_EN_US = "en_US";
//初始化
$(function() {
	var langStr=(locale==LOCALE_ZH_CN?"zh":(locale==LOCALE_ZH_TW?"tw":"en"));
	$.i18n.properties({
	    name : 'lang', //资源文件名称
	    path : path+'/js/i18n/', //资源文件路径
	    mode : 'map', //用both或map的方式使用资源文件中的值
	    language : langStr,
	    cache:false, 
	    encoding: 'UTF-8', 
	    callback : function() {//加载成功后设置显示内容
	    }
	});
});