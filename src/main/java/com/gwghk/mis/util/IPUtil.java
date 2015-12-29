package com.gwghk.mis.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 摘要：IP处理类
 * @author Gavin.guo
 * @date 2014-03-11
 */
public class IPUtil {

	/**
	 * 功能：获取客户端IP
	 * @param request  客户端request
	 * @return   客户端IP
	 */
	public static String getClientIP(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
	        return ip.split(",")[0];
		}
		ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			return ip;
		}
		return request.getRemoteAddr();
    }
	
	/**
	 * 功能：获取真实本地的IP
	 * @return 本机IP
	 */
	public static String getRealIp(){
		String localip = "";												// 本地IP，如果没有配置外网IP则返回它
		String netip = "";													// 外网IP
		Enumeration<NetworkInterface> netInterfaces = null;
		try{
			 netInterfaces = NetworkInterface.getNetworkInterfaces();
			 InetAddress ip = null;
				boolean finded = false;										// 是否找到外网IP
				while (netInterfaces.hasMoreElements() && !finded) {
					NetworkInterface ni = netInterfaces.nextElement();
					Enumeration<InetAddress> address = ni.getInetAddresses();
					while (address.hasMoreElements()) {
						ip = address.nextElement();
						if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {		// 外网IP
							netip = ip.getHostAddress();
							finded = true;
							break;
						} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {	// 内网IP
							localip = ip.getHostAddress();
						}
					}
				}
				if (netip != null && !"".equals(netip)) {
					return netip;
				} else {
					return localip;
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	private static StringBuilder sb = new StringBuilder();  
    /** 
     * 从ip的字符串形式得到字节数组形式 
     * @param ip 字符串形式的ip 
     * @return 字节数组形式的ip 
     */  
    public static byte[] getIpByteArrayFromString(String ip) {  
        byte[] ret = new byte[4];  
        StringTokenizer st = new StringTokenizer(ip, ".");  
        try {  
            ret[0] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);  
            ret[1] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);  
            ret[2] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);  
            ret[3] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);  
        } catch (Exception e) {  
          System.out.println("从ip的字符串形式得到字节数组形式报错");  
        }  
        return ret;  
    }  
    /** 
     * @param ip ip的字节数组形式 
     * @return 字符串形式的ip 
     */  
    public static String getIpStringFromBytes(byte[] ip) {  
        sb.delete(0, sb.length());  
        sb.append(ip[0] & 0xFF);  
        sb.append('.');       
        sb.append(ip[1] & 0xFF);  
        sb.append('.');       
        sb.append(ip[2] & 0xFF);  
        sb.append('.');       
        sb.append(ip[3] & 0xFF);  
        return sb.toString();  
    }  
      
    /** 
     * 根据某种编码方式将字节数组转换成字符串 
     * @param b 字节数组 
     * @param offset 要转换的起始位置 
     * @param len 要转换的长度 
     * @param encoding 编码方式 
     * @return 如果encoding不支持，返回一个缺省编码的字符串 
     */  
    public static String getString(byte[] b, int offset, int len, String encoding) {  
        try {  
            return new String(b, offset, len, encoding);  
        } catch (UnsupportedEncodingException e) {  
            return new String(b, offset, len);  
        }  
    }  
}
