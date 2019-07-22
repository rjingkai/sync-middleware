package com.sync.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sync.mysql.model.CuxOmsIPaymentHistoryMysql;
import com.sync.mysql.model.mapper.CuxOmsIPaymentHistoryMysqlMapper;
import com.sync.oraclel.model.CuxOmsIPaymentHistoryOracle;
import com.sync.oraclel.model.mapper.CuxOmsIPaymentHistoryOracleMapper;
import com.sync.service.SyncService;

@Service
public class SyncServiceImpl implements SyncService {
	@Autowired
	private CuxOmsIPaymentHistoryMysqlMapper paymentHistoryMysqlMapper;
	@Autowired
	private CuxOmsIPaymentHistoryOracleMapper paymentHistoryOracleMapper;
	
	/**
	 *  根据状态值查询OMS中间表流水信息
	 */
	@Override
	public List<CuxOmsIPaymentHistoryMysql> selectOmsPayhistoryByParms(Map<String, Object> params) {
		return this.paymentHistoryMysqlMapper.selectOmsPayhistoryByParms(params);
	}
	
	/**
	 *  将待同步的流水同步至EBS
	 */
	@Override
	public Map<String, Object> asyncPayHistory(List<CuxOmsIPaymentHistoryMysql> payHistoryMList) {
		Map<String, Object> result = new HashMap<String, Object>();
		// EBS新增的流水数量
		int added = payHistoryMList.size();
		// EBS重复的流水数量
		int repeat = 0;
		try {
			StringBuffer sourceIds = new StringBuffer();
			List<CuxOmsIPaymentHistoryOracle> payHistoryOList = new ArrayList<CuxOmsIPaymentHistoryOracle>();
			sourceIds.append("(");
			for (int i = 0; i < payHistoryMList.size(); i++) {
				sourceIds.append("'" + payHistoryMList.get(i).getSourceId() + "'");
				if (payHistoryMList.size() != (i + 1)) {
					sourceIds.append(",");
				}
				// 将OMS流水对象赋值给EBS流水对象
				CuxOmsIPaymentHistoryOracle payHistoryO = new CuxOmsIPaymentHistoryOracle();
				BeanUtils.copyProperties(payHistoryMList.get(i), payHistoryO);
				payHistoryOList.add(payHistoryO);
			}
			sourceIds.append(")");
			// 校验流水是否已经同步EBS
			List<CuxOmsIPaymentHistoryOracle> payHistoryOracleList = this.paymentHistoryOracleMapper.selectBySouceIds(sourceIds.toString());
			// 本批流水存在已同步至EBS的流水
			if (CollectionUtils.isNotEmpty(payHistoryOracleList)) {
				// 移除已同步至EBS的流水信息
				for (CuxOmsIPaymentHistoryOracle payHistoryOracle : payHistoryOracleList) {
					payHistoryOList.removeIf(payHistoryO->payHistoryO.getSourceId().equals(payHistoryOracle.getSourceId()));
				}
				added -= payHistoryOracleList.size();
				repeat += payHistoryOracleList.size();
			}
			// 将流水同步至EBS
			if (CollectionUtils.isNotEmpty(payHistoryOList)) {
				this.paymentHistoryOracleMapper.batchInsert(payHistoryOList);
			}
			// 更新OMS流水的状态
			if (CollectionUtils.isNotEmpty(payHistoryMList)) {
				this.paymentHistoryMysqlMapper.batchUpdate(payHistoryMList);
			}
		} catch (Exception e) {
			result.put("error", e.getMessage());
		}
		result.put("added", added);
		result.put("repeat", repeat);
		return result;
	}
}
