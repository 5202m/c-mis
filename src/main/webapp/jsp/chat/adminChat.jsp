<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.xdomainrequest.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/chat/adminChat.js" charset="UTF-8"></script>
<script type="text/javascript">
adminChat.pmApiUrl="${pmApiUrl}";
adminChat.chatUrl="${chatUrl}";
adminChat.chatUrlParam="${chatUrlParam}";
</script>
<style type="text/css">
	.title{
		font-size:16px;
		font-weight:bold;
		padding:20px 10px;
		background:#eee;
		overflow:hidden;
		border-bottom:1px solid #ccc;
	}
	.t-list{
		padding:5px;
	}
	.date-box{width: 500px;height: 40px;padding: 0px 10px;}
	.date-box li{float:left;display:inline-block;width:20%;padding:0;box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;text-align:center}
	.date-box li:last-child{border:none}
	.date-box h2{font-size:12px;color:#878787}
	.date-box p{font:9px arial}
	.date-box .date-sz{font-size:14px}
	.date-up p{color:#00b050}
	.date-down p{color:#e93030}
	.date-box li p span{margin-left:5px;}
</style>
<div class="easyui-layout" data-options="fit:true" id="adminChat_div">
 	<div region="north" class="title" border="false" align="left" style="height:120px;">
		<div id="groupType_tab" class="easyui-tabs" data-options="fit:true" style="height:100px;margin-top:15px;">
					<c:forEach var="groupTypeDetail" items="${groupTypeList}">
						<div id="groupTypeDetail_${groupTypeDetail.code}" title="${groupTypeDetail.nameCN}" style="padding:0px;overflow-x:auto;height:100px;">
					        <input type="hidden" id="chatURL" value="${chatURL}"/>
					 		<c:choose>
					 			<c:when test="${chatGroupList != null}">
						 			<c:forEach items="${chatGroupList}" var="chatGroup">
						 			    <c:if test="${groupTypeDetail.code==chatGroup.groupType}">
						 			    	<a class="easyui-linkbutton" style="margin-right:20px;margin-top:10px;" data-options="plain:true,iconCls:'ope-add',disabled:false" id="${chatGroup.id}" onclick="adminChat.add('${chatGroup.id}','${chatGroup.name}');">${chatGroup.name}</a>
						 			    </c:if>
							 		</c:forEach>
						 		</c:when>
						 		<c:otherwise>
						 			<span class="red">没有聊天室权限</span>
						 		</c:otherwise>
					 		</c:choose>     
						</div>
					</c:forEach>
		</div>
 		<!-- <div style="top:0px;position:absolute;right:0;">
 		     <section class="date-box">
		        <ul class="clearfix" id="product_price_ul">
		            <li name="022">
		                <h2>伦敦金</h2>
		                <p class="date-sz"></p>
		                <p><span></span> <span></span></p>
		            </li>
		            <li  name="023">
		                <h2>伦敦银</h2>
		                <p class="date-sz"></p>
		                <p><span></span> <span></span></p>
		            </li>
		            <li name="050">
		                <h2>美元指数</h2>
		                <p class="date-sz"></p>
		                <p><span></span> <span></span></p>
		            </li>
		            <li name="00E">
		                <h2>纽约原油</h2>
		                <p class="date-sz"></p>
		                <p><span></span> <span></span></p>
		            </li>
		        </ul>
            </section>
 		</div> -->
	</div>
	<div id="pp" region="center" border="false">
	</div>
</div>
