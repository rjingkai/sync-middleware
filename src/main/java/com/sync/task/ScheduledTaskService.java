package com.sync.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sync.utils.PostUtil;

/**
* @ClassName: ScheduledTask 
* @Description: 定时任务执行类型 
* @author NingChongQing
* @date 2018年4月10日 下午1:55:56
 */
@Component
public class ScheduledTaskService {
	private Logger logger = LoggerFactory.getLogger(ScheduledTaskService.class);
	public final static String url = "http://192.168.50.135:8080/";
	
	/**
	* **********************************************
	* @Title: timerCron 
	* @Description: 定时任务同步流水->10秒钟执行一次
	* @param  
	* @return void
	* @throws	
	* @author NingChongQing	
	* @date 2018年4月10日 下午2:03:27 
	* **********************************************
	 */
    @Scheduled(cron = "0/10 * * * * ?")
    public void timerCron() {
    	logger.info("定时任务开始执行......");
    	JSONObject jo = new JSONObject();
		try {
			PostUtil.doPost(jo.toJSONString(), url + "sync/syncPayHistory","text/plain");
		} catch (Exception e) {
			logger.info("定时任务执行失败,原因:" + e.getMessage()+".");
		}
		logger.info("定时任务执行结束......");
    }
    
}
