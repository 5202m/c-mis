<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">

	var visitorTab =
	{
		menuAuth : null,
		
		/**页卡加载*/
		tabOnLoad : function(){
			if (validObj(visitorTab.menuAuth)){
				for (var i = 0; i < visitorTab.menuAuth.length; i++){
					$("[class~=" + visitorTab.menuAuth[i].code + "]", "#chatVisitor_tab").show();
				}
			}
		},

		/**初始化*/
		init : function() {
			var loc_menuId = $("#chatVisitor_tab").parent().attr("id");
			var result = getJson(basePath + "/authorityController/getFuns.do", {
				menuId : loc_menuId
			}, null);
			visitorTab.menuAuth = result.obj;
		}
	};
	
	$(function(){
		visitorTab.init();
	});
</script>
<div id="chatVisitor_tab" class="easyui-tabs" data-options="fit:true,onLoad:visitorTab.tabOnLoad" style="margin-top: 10px;">
	<div title="访客记录" data-options="href:'chatVisitorController/visitor.do'"></div>
	<div title="在线时长统计" data-options="href:'chatVisitorController/report.do?type=duration'"></div>
	<div title="各类在线人数统计" data-options="href:'chatVisitorController/report.do?type=online'"></div>
	<div title="整点在线人数统计" data-options="href:'chatVisitorController/report.do?type=timePoint'"></div>
</div>