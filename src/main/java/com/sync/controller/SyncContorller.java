package com.sync.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sync.mysql.model.CuxOmsIPaymentHistoryMysql;
import com.sync.service.SyncService;
import com.sync.task.AsyncTaskConfig;
import com.sync.task.AsyncTaskService;

/**
* @ClassName: SyncContorller 
* @Description: 同步数据入口 
* @author NingChongQing
* @date 2018年4月10日 下午2:31:28
 */
@RestController
@RequestMapping("/sync")
public class SyncContorller {
	private Logger logger = LoggerFactory.getLogger(SyncContorller.class);
	
	@Autowired
	private SyncService syncService;

	/**
	 * **********************************************
	 * 
	 * @Title: syncPayHistory
	 * @Description: 同步OMS中间表流水
	 * @param @return
	 * @return String
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws @author
	 *             NingChongQing
	 * @date 2018年4月10日 上午9:45:18 **********************************************
	 */
	@RequestMapping(value = "/syncPayHistory", produces = "text/plain;charset=UTF-8")
	public String syncPayHistory() throws InterruptedException, ExecutionException {
		long currentTimeMillis = System.currentTimeMillis();
		logger.info("【SyncPayHistory-" + currentTimeMillis + "】执行开始......");
		//查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		// 状态：待同步
		params.put("status", "PENDING");
		// 单次同步数量
		params.put("limitCount", 5000);
		//查出来需要同步的数据
		List<CuxOmsIPaymentHistoryMysql> payHistoryMList = this.syncService.selectOmsPayhistoryByParms(params);

		if (CollectionUtils.isNotEmpty(payHistoryMList)) {
			// 注入多线程的Bean
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
			AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);
			// 总的同步条数
			int totalCount = payHistoryMList.size();
			// 每次同步500条数据
			int eachTimeSynCount = 500;
			// 执行次数
			int excuteTimes = (totalCount % eachTimeSynCount == 0) ? (totalCount / eachTimeSynCount) : (totalCount / eachTimeSynCount + 1);
			for (int i = 0; i < excuteTimes; i++) {
				// 每页第一个数据在result中的位置
				int fromIndex = i * eachTimeSynCount;
				// 每页最后一个数据在result中的位置
				int toIndex = (totalCount - fromIndex) > eachTimeSynCount ? ((i + 1) * eachTimeSynCount) : totalCount;
				String threadName = "Thread-" + currentTimeMillis + "-" + (i + 1);
				// 开辟新的线程同步订单信息
				try {
					// 线程池超过最大线程数时，会抛出TaskRejectedException，则等待1秒钟，直到不抛出异常为止
					asyncTaskService.asyncPayHistoryReturnNoValue(syncService, threadName, payHistoryMList.subList(fromIndex, toIndex));
				} catch (TaskRejectedException e) {
					logger.info("========线程池满，等待1秒钟.========");
					Thread.sleep(1000);
				}
			}
			context.close();
		}
		logger.info("【SyncPayHistory-" + currentTimeMillis + "】执行结束......");
		return "【SyncPayHistory-" + currentTimeMillis + "】执行成功.";
	}
}
