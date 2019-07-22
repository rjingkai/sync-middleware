package com.sync.task;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sync.mysql.model.CuxOmsIPaymentHistoryMysql;
import com.sync.service.SyncService;

/**
 * @ClassName: AsyncTaskService
 * @Description: 线程执行任务类
 * @author NingChongQing
 * @date 2018年4月10日 上午9:35:05
 */
@Service
public class AsyncTaskService {
	private Logger logger = LoggerFactory.getLogger(AsyncTaskService.class);

	/**
	 * **********************************************
	 * 
	 * @Title: asyncPayHistoryReturnNoValue
	 * @Description: 线程处理流水的任务类
	 * @param @param syncService
	 * @param @param  threadName
	 * @param @param payHistoryOList
	 * @param @return
	 * @return Future<String>
	 * @throws @author NingChongQing
	 * @date 2018年4月10日 上午9:55:28 **********************************************
	 */
	@Async
	public void asyncPayHistoryReturnNoValue(SyncService syncService, String threadName,
			List<CuxOmsIPaymentHistoryMysql> payHistoryMList) {
		Map<String, Object> result = syncService.asyncPayHistory(payHistoryMList);
		// 执行成功
		if (result.get("error") == null) {
			logger.info("【" + threadName + "】执行成功,新增:" + result.get("added").toString() + "条,重复:"
					+ result.get("repeat").toString() + "条,总共:" + payHistoryMList.size() + "条.");
		} else {
			logger.warn("【" + threadName + "】执行失败,原因:" + result.get("error").toString());
		}
	}
}
