<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<style type="text/css">
    #videoUrlPc label,#videoUrlMb label,#audioUrlMb label {width:220px;float:left;padding:5px;}
</style>
<script type="text/javascript"> 
    $(function(){
    	chatAnalyst.getLiveLinks(1);
    	chatAnalyst.getLiveLinks(3);
    	chatAnalyst.getLiveLinks(4);
        var liveLinks = '${mngUser.liveLinks}';
        chatAnalyst.setLiveLinks(liveLinks);
    });
</script>
<div class="easyui-layout" data-options="fit:true" style="padding: 5px; overflow: hidden;">
    <form id="setLiveLinks_form" method="post">
        <input type="hidden" name="userId" id = "userId" value="${mngUser.userId}">
        <!-- center -->
        <div data-options="region:'center',border:false" style="padding: 4px">
            <div class="easyui-layout" data-options="fit:true,border:false" style="width: 750px; height: 200px;">
                <!-- west -->
                <div data-options="region:'west',border:false" style='width: 250px'>
                    <div class="easyui-panel" data-options="fit:true,title:'视频直播(pc)'">
                        <div id="videoUrlPc"></div>
                    </div>
                </div>
                <div data-options="region:'center',border:false" style='width: 250px'>
                    <div class="easyui-panel" data-options="fit:true,title:'视频直播(mb)'">
                        <div id="videoUrlMb"></div>
                    </div>
                </div>
                <div data-options="region:'east',border:false" style='width: 250px'>
                    <div class="easyui-panel" data-options="fit:true,title:'音频直播(mb)'">
                        <div id="audioUrlMb"></div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>