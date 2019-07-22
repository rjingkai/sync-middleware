package com.sync.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
* @ClassName: PostUtil 
* @Description: Post工具类 
* @author NingChongQing
* @date 2018年4月10日 下午1:54:19
 */
public class PostUtil {
	
	/**
	* **********************************************
	* @Title: doPost 
	* @Description: 实现
	* @param @param content
	* @param @param url
	* @param @param contentType
	* @param @return
	* @param @throws IOException 
	* @return String
	* @throws	
	* @author NingChongQing	
	* @date 2018年4月10日 下午1:54:55 
	* **********************************************
	 */
	public static String doPost(String content,String url,String contentType) throws IOException {
		StringBuilder result = new StringBuilder();
        URL postUrl = new URL(url);  
        URLConnection con = postUrl.openConnection(); 
        con.setConnectTimeout(5000);
        con.setDoOutput(true);  
        con.setRequestProperty("Pragma", "no-cache");  
        con.setRequestProperty("Cache-Control", "no-cache"); 
        if(contentType!=null)
        {
        	con.setRequestProperty("Content-Type", contentType);  
        }
        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());    
        out.write(new String(content.getBytes("UTF-8")));  
        out.flush();  
        out.close();  
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));  
        String line = "";  
        for (line = br.readLine(); line != null; line = br.readLine()) {  
        	result = result.append(line);
        }  
		return result.toString();
	}
}
