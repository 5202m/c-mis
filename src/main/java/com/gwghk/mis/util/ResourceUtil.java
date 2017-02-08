package com.gwghk.mis.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.BoMenu;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.MenuResult;
import com.gwghk.mis.service.UserService;

/**
 * 摘要：项目参数工具类
 * @author Gavin.guo
 */
public class ResourceUtil {

    /**
     * 功能：获取页面请求的参数
     * @param field   页面参数
     * @return  参数对应的值
     */
	public static String getParameter(String field) {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		return request.getParameter(field);
	}
	
	/**
	 * 功能： 获取项目classPath的绝对路径
	 */
	public static String getClassPath() {
		String path = null;
		try {
			path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
		}
		catch (Exception e) {
			path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		}
		if("\\".equals(File.separator))  {   //windows
		    return path.replaceFirst("file:/", "");
		}else if("/".equals(File.separator))  {
			return path.replaceFirst("file:", ""); 
		}else{
			return path.replaceFirst("file:/", "");
		}
	}
	
	/**
	 * 功能：获取tomcat下webapp目录路径
	 * @return  tomcat下webapp目录路径
	 */
	/*
	public static String getWebAppPath(){
		return ContextHolderUtils.getSession().getServletContext().getRealPath("/");
	}
	*/
	
	/**
	 * 功能：获取PM 文件目录路径
	 */
	public static String getPmFilesPath(){
		return PropertiesUtil.getInstance().getProperty("pmfilesRootPath");
	}
	
	/**
	 * 功能：获取项目根目录
	 */
	public static String getPorjectPath() {
		String nowpath; // 当前tomcat的bin目录的路径 如
						// D:\java\software\apache-tomcat-6.0.14\bin
		String tempdir;
		nowpath = System.getProperty("user.dir");
		tempdir = nowpath.replace("bin", "webapps"); // 把bin 文件夹变到 webapps文件里面
		tempdir += "\\"; // 拼成D:\java\software\apache-tomcat-6.0.14\webapps\sz_pro
		return tempdir;
	}
	
	public static String getSystempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getSeparator() {
		return System.getProperty("file.separator");
	}
	
	/**
	 * 功能：获得请求路径
	 */
	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI();
		if (requestPath.indexOf("?") > -1) {   // 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("?"));
		}
		if (requestPath.indexOf("&") > -1) {   // 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1); // 去掉项目路径
		return requestPath;
	}
	
	/**
	 * 功能：获取页面完整的请求地址
	 */
	public static String getFullRequestPath(){
		HttpServletRequest request = ContextHolderUtils.getRequest();
		String fullPath = request.getScheme() + "://"           //协议
				+ request.getServerName() 						//服务器地址
                + ":"+ request.getServerPort()           		//端口号 
                + request.getContextPath()      				//项目名称
                + request.getServletPath()      				//请求页面或其他地址
                + "?" + (request.getQueryString()); 		    //参数
		return fullPath;
	}

	/**
	 * 功能：获取当前登录用户对象
	 */
	public static BoUser getSessionUser() {
		HttpSession session=ContextHolderUtils.getSession();
		Object userNoSession = session.getAttribute(ContextHolderUtils.getSessionId());
		UserService userService = (UserService)SpringContextUtil.getBean("userService");
		if(userNoSession==null){
			return new BoUser();
		}
		Object superFlag = session.getAttribute(WebConstant.SESSION_SUPER_FLAG);
		boolean isSuper=false;
		if(superFlag!=null){
			isSuper=(boolean)superFlag;
		}
		return isSuper?BeanUtils.clone(userService.getBaseUserByNo(userNoSession.toString(), isSuper),BoUser.class):userService.getUserByNo(userNoSession.toString());
	}
	
	/**
	 * 功能：从session中获取当前菜单结果对象
	 */
	public static MenuResult getSessionMenu(){
		return (MenuResult)ContextHolderUtils.getSession().getAttribute(WebConstant.SESSION_MENU_KEY);
	}
	
	/**
	 * 功能：从session中获取语言
	 * @return
	 */
	public static String getSessionLocale(){
		Object obj = ContextHolderUtils.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(obj != null){
			Locale locale = (Locale)obj;
			return locale.toString();
		}
		return "";
	}
	
	
	/**
     * 功能：菜单列表格式成树形列表
     */
    public static void formatMenutoTree(List<BoMenu> list, int level, String parentId, List<BoMenu> result){
        List<BoMenu> subList = list.stream()
        		.filter(e->(StringUtils.isNotBlank(e.getParentMenuId())&&e.getParentMenuId().equals(parentId)
        				||(StringUtils.isBlank(e.getParentMenuId())&&StringUtils.isBlank(parentId))))
        		.collect(Collectors.toList());
        String str="";
        for(BoMenu row : subList){
        	str=StringUtil.fillChar('　', level);
            row.setNameCN(str.concat(row.getNameCN()));
            row.setNameTW(str.concat(row.getNameTW()));
            row.setNameEN(str.concat(row.getNameEN()));
            result.add(row);
            formatMenutoTree(list, level + 1, row.getMenuId(),result);
        }
    }
	
	/**
	 * 功能：根据数据字典code-->获取对应的数据字典
	 * @param code   数据字典code
	 * @return  数据字典
	 */
	public static BoDict getDictByCode(String systemCategory,String code,String parentCode){
		List<BoDict> dictParamList = getDictList();
		if(dictParamList != null){
			List<BoDict> childrenList =null;
			for(BoDict dict : dictParamList){
				if(parentCode.equals(dict.getCode())&&(childrenList=dict.getChildren())!=null){
					return childrenList.stream().filter(e->e.getCode().equals(code)&&e.getSystemCategory().equals(systemCategory)).findFirst().get();
				}
			}
		}
		return null;
	}
	
	/**
	 * 功能：根据数据字典code-->获取对应的字典值
	 * @param code   数据字典code
	 * @return  字典值
	 */
	public static String getDictValueByCode(String systemCategory,String code,String parentCode){
		if(null == code || null == parentCode){
			return "";
		}
		BoDict BoDict = getDictByCode(systemCategory,code,parentCode);
		if(null == BoDict){
			return "";
		}
		String locale = getSessionLocale();
		return getDictName(BoDict,locale).getName();
	}
	
	
	/**
	 * 获取对应父类code获取子数据字典列表
	 * @param parentCode 父数据code的数组
	 * @return
	 */
	public static List<BoDict> getSubDictListByParentCode(String systemCategory,String parentCode){
		List<BoDict> dictList = getDictList();
		List<BoDict> subDictList=null;
		for(BoDict row:dictList){
			if(row.getCode().equals(parentCode)){
				subDictList=row.getChildren().stream().filter(e->StringUtil.containKeyword(e.getSystemCategory(),systemCategory,false)).collect(Collectors.toList());
				return subDictList;
			}
		}
		return subDictList;
	}
	
	/**
	 * 获取对应父类code获取子数据字典列表
	 * @param parentCodeArr 父数据code的数组
	 * @return
	 */
	public static Map<String, List<BoDict>> getDictListByLocale(String systemCategory,String ...parentCodeArr){
		Map<String, List<BoDict>> dictMap=new HashMap<String, List<BoDict>>();
		List<BoDict> dictList = getDictList();
		String locale = getSessionLocale();
		List<BoDict> subDictList=null;
		String str=StringUtils.join(parentCodeArr, ",");
		List<BoDict> newDictList=null;
		if (dictList != null) {
			newDictList=dictList.stream().filter(e->StringUtil.containKeyword(str,e.getCode(),false)).collect(Collectors.toList());
			for(BoDict row:newDictList){
				subDictList=row.getChildren().stream().filter(e->StringUtil.containKeyword(e.getSystemCategory(),systemCategory,false)).collect(Collectors.toList());
				if(subDictList!=null){
					subDictList.forEach(e ->getDictName(e,locale));
				}
				dictMap.put(row.getCode(),subDictList);
			}
		}
		return dictMap;
	}
	
	/**
	 * 根据语言调整字典名称
	 * @param dict
	 * @param locale
	 * @return
	 */
	private static BoDict getDictName(BoDict dict,String locale){
		if(WebConstant.LOCALE_ZH_TW.equals(locale)){
			dict.setName(dict.getNameTW());
		}else if(WebConstant.LOCALE_ZH_CN.equals(locale)){
			dict.setName(dict.getNameCN());
		}else if(WebConstant.LOCALE_EN_US.equals(locale)){
			dict.setName(dict.getNameEN());
		}else{
			dict.setName(dict.getNameTW());
		}
		return dict;
	}
	
	 /**
		 * 功能：提取字典列表(备注：先在缓存中提取，不存在再通过接口提取列表数据)
		 */
	@SuppressWarnings("unchecked")
	public static List<BoDict> getDictList(){
		Cache cache = CacheManager.getContent(WebConstant.DICT_KEY);
		List<BoDict> dictParamList = null;
		if(cache != null && cache.getValue() != null){
			dictParamList  = (List<BoDict>)cache.getValue();
		}else{
			return null;
		}
		return dictParamList;
	}
}
