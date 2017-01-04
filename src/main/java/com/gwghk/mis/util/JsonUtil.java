package com.gwghk.mis.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.gwghk.mis.common.model.TreeBean;

/**
 * 摘要：JSON工具类
 * @author Gavin.guo
 * @date   2014-11-19
 */
public class JsonUtil {
	
	/**
	 * 功能：将JSON字符串转换成list集合
	 * @param jsonString  JSON字符
	 * @param obj  转换的对象
	 * @return list集合
	 */ 
	public static List json2List(String jsonString,Object obj){
		return JSON.parseArray(jsonString, obj.getClass());
	}
	
	/**
     * 功能：树形格式化
     * @param nodeListTmp
     * @param fillChild 是否填充child节点数据
     * @return
     */
	public static String formatListToTreeJson(List<TreeBean> nodeListTmp,boolean fillChild){
    	return JSONArray.fromObject(formatToTreeJson(nodeListTmp,fillChild)).toString();
    }
	
	/**
     * 功能：树形格式化
     * @param nodeListTmp
     * @param fillChild 是否填充child节点数据
     * @return
     */
	public static List<TreeBean> formatToTreeJson(List<TreeBean> nodeListTmp,boolean fillChild){
    	List<TreeBean> nodeList = new ArrayList<TreeBean>(); 
    	for(TreeBean outNode : nodeListTmp){
    	    boolean flag = false;  
    	    for(TreeBean inNode : nodeListTmp){ 
    	        if(outNode.getParentId()!=null && outNode.getParentId().equals(inNode.getId())){  
    	            flag = true; 
    	            if(fillChild&&(StringUtils.isBlank(inNode.getParentId())||"0".equals(inNode.getParentId()))){
    	            	if(inNode.getChild()== null){  
	    	                inNode.setChild(new ArrayList<TreeBean>());  
    	            	}
    	            	inNode.getChild().add(outNode);
    	            }else{
	    	            if(inNode.getChildren()== null){  
	    	                inNode.setChildren(new ArrayList<TreeBean>());  
	    	            }
	    	            inNode.getChildren().add(outNode); 
    	            }
    	            break;  
    	        }  
    	    }
    	    if(!flag){
    	        nodeList.add(outNode);   
    	    }  
    	} 
    	return nodeList;
    }
	
	/**
	 * 格式json
	 * @ jsonStr
	 * @return
	 */
	public static String formatJsonStr(String jsonStr){
		return jsonStr.startsWith("[")?jsonStr:"[".concat(jsonStr).concat("]");
	}
}
