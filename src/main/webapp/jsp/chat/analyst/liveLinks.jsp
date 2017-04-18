<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<style type="text/css">
    .live-tab-panel{
      overflow-y: auto;height:460px;border: 1px solid #B89E61;padding: 5px;
    }
    .live_sub_tab{
       height: 20px;
       line-height: 20px;
    }
    .live-sel-num{
       border: 1px solid #B89E61;
       padding: 4px;
       border-radius: 2px;
       margin: 5px;
    }
    .live-sel-num-tmp{
      display: none;
    }
    .live-next-labdev{
      margin-top: 4px;padding:5px;
    }
    .liveMainSelect,.liveSelect,.dictSetSelect{
       width:100px;
    } 
   .live-sel-num .ope-cancel{
      background-position-y:1px; 
   }
   .live-sel-num .easyui-linkbutton {
      float:right; margin-top: -2px;width: 25px;height: 25px;
    }
</style>
<script type="text/javascript"> 
	function removeLiveNum(obj){
		$.messager.confirm("操作提示", "确实要移除该项地址吗？" , function(r) {
			if (r) {
				var pDom=$(obj).parents(".live-tab-panel").find(".live_sub_tab");
				if(pDom.next().find(".live-sel-num").length==1){
					pDom.find(".liveMainSelect").find("option[value='']").attr("selected",true);
					pDom.find(".liveSelect").find("option[value='']").attr("selected",true);
					pDom.find(".dictSetSelect").find("option[value='']").attr("selected",true);
				}
				$(obj).parent().remove();
			}
		});
	}
    $(function(){
    	$(".liveSelect,.dictSetSelect").change(function(){
    		var pSelectBox=null;
			var tn = this.value;
    		if($(this).hasClass("dictSetSelect")){
    			pSelectBox=$(this).prev().prev();
    		}else{
    			pSelectBox=$(this).prev();
    		}
    		if(isBlank(tn) || (!$(this).hasClass("dictSetSelect") && tn.indexOf("X")!=-1) ||isBlank(pSelectBox.val())){
    			$(this).val("");
    			return false;
    		}
    		var tabNext=$(this).parents(".live_sub_tab").next();
			var tl = pSelectBox.val().formatStr(tn);
    		if(tabNext.find(".live-sel-num[tn='"+tn+"'][tl='"+tl+"']").length>0){
    			alert("已选择该编号【"+tn+"】");
    		}else{
    			var cloneTmp=$(".live-sel-num-tmp").clone();
    			cloneTmp.removeClass("live-sel-num-tmp").addClass("live-sel-num").attr("tc",$(this).parents(".live-tab-panel").attr("tc")).attr("tn",tn).attr("tl",pSelectBox.val()).find("label").text(pSelectBox.find("option[value='"+pSelectBox.val()+"']").text()+"："+tn);
    			tabNext.append(cloneTmp);
    		}
    	});
    	$(".live_sub_tab .liveMainSelect").change(function(){
    		var tabNext=$(this).parents(".live_sub_tab").next();
    		var numDom=tabNext.find(".live-sel-num");
    		if(isBlank(this.value) && numDom.length>0){
    			$.messager.confirm("操作提示", "不选择，所选线路会移除！" , function(r) {
    				if (r) {
    					numDom.remove();
    				}
    			});
    		}
    		$(".liveSelect").show();
    		var nSelectBox=null;
    		var isDictSet=this.value && this.value.indexOf(";")!=-1;//数据字典设置有默认值的项
    		var isNotParamVal=!/\{0\}/g.test(this.value);//数据字典设置没有带参数的项，如{0}
    		var nbxVal="";
    		if(isDictSet){
    			$(".liveSelect").val("").hide();
    			$(".dictSetSelect").show().html('<option value="">--请选择--</option>');
    			var lval=this.value.split(";");
    			if(isValid(lval)){
    				lval=lval[1].split(",");
    				for(var i in lval){
    					$(".dictSetSelect").append('<option value="'+lval[i]+'">'+lval[i]+'</option>');
    				}
    			}
    			nbxVal=$(this).next().next().val();
    		}else{
    			nbxVal=isNotParamVal?"":$(this).next().val();
    			$(".dictSetSelect").val("").hide();
    		}
    		if(!isNotParamVal && (isBlank(nbxVal) || (!isDictSet && nbxVal.indexOf("X")!=-1))){
    			return false;
    		}
    		if((isValid(nbxVal) && tabNext.find(".live-sel-num[tn='"+nbxVal+"'][tl='"+this.value+"']").length>0)||(isNotParamVal&& tabNext.find(".live-sel-num[tl='"+this.value+"']").length>0)){
    			alert("已选择该地址");
    		}else{
    			var cloneTmp=$(".live-sel-num-tmp").clone();
    			cloneTmp.removeClass("live-sel-num-tmp").addClass("live-sel-num").attr("tc",$(this).parents(".live-tab-panel").attr("tc")).attr("tn",nbxVal).attr("tl",this.value).find("label").text($(this).find("option[value='"+this.value+"']").text()+(isValid(nbxVal)?"："+nbxVal:""));
    			tabNext.append(cloneTmp);
    		}
    	});
    }); 
</script>
<div style="padding: 5px; overflow: hidden;">
    <div class="live-sel-num-tmp" tn="" tl=""><label></label><a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false" onclick="removeLiveNum(this)"></a></div>
    <form id="setLiveLinks_form" method="post">
        <input type="hidden" name="userId" id = "userId" value="${mngUser.userId}">
         <c:forEach var="row" items="${liveList}">
              <div style='width: 255px;height:490px;float:left;'>
                <strong>${row.nameCN}</strong>
                <div class="live-tab-panel" tc="${row.value}">
                     <div class="live_sub_tab">
                       		地址：<select class="liveMainSelect">
	                                 <option value="">--请选择--</option>
	                                  <c:forEach var="child" items="${row.children}">
	                                     <option value="${child.value}">${child.nameCN}</option>  
	                                </c:forEach>		      
                                  </select>
                                  <select class="liveSelect">
	                       		     <option value="">--请选择--</option>
	                       		     <option value="0X">0X</option> 
			                       		  <c:forEach var="i" begin="1" end="9" step="1">   
			                                <option value="0${i}">&nbsp;&nbsp;0${i}</option>   
			                              </c:forEach>
			                              <option value="1X">1X</option>  
			                              <c:forEach var="i" begin="10" end="19" step="1">   
			                                <option value="${i}">&nbsp;&nbsp;${i}</option>   
			                              </c:forEach>
			                              <option value="2X">2X</option>  
			                              <c:forEach var="i" begin="20" end="29" step="1">   
			                                <option value="${i}">&nbsp;&nbsp;${i}</option>   
			                              </c:forEach>
			                              <option value="2XX">2XX</option>
			                              <c:forEach var="i" begin="200" end="201" step="1">   
			                                <option value="${i}">&nbsp;&nbsp;${i}</option>   
				                          </c:forEach>
                       		     </select>
                       		     <select class="dictSetSelect" style="display:none;"></select>
                       </div>
                       <div class="live-next-labdev">
                            <p>已选地址:</p>
                            <c:forEach var="exRow" items="${existLiveList}">
                             <c:if test="${exRow.code==row.value}">
                                <div class="live-sel-num" tc="${exRow.code}" tn="${exRow.numCode}" tl="${exRow.url}"><label>${exRow.name}<c:if test="${not empty exRow.numCode}">：${exRow.numCode}</c:if></label><a class="easyui-linkbutton" data-options="plain:true,iconCls:'ope-cancel',disabled:false" onclick="removeLiveNum(this)"></a></div>
                             </c:if>             
                             </c:forEach>
                       </div>
                </div>
              </div>
         </c:forEach>
    </form>
</div>