package com.h3bpm.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.entity.BizObjectInfo;
import com.h3bpm.web.entity.WorkFlowTask;
import com.h3bpm.web.enumeration.ApiActionUrl;
import com.h3bpm.web.enumeration.WorkflowExecuteType;
import com.h3bpm.web.mapper.WorkFlowMapper;
import com.h3bpm.web.mapper.WorkFlowTaskMapper;

@Service
public class WorkFlowService extends ApiDataService {

	@Autowired
	private WorkFlowTaskMapper workFlowTaskMapper;
	
	@Autowired
	private WorkFlowMapper workFlowMapper;

	/**
	 * 新建一个流程
	 * 
	 * @param id
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public String createWorkFlow(String id) throws ServiceException {
		WorkFlowTask workFlowTask = null;

		try {
			workFlowTask = workFlowTaskMapper.getWorkFlowTaskById(id);
			Map<String, Object> map = new HashMap<>();

			map.put("userCode", workFlowTask.getUserLoginName());
			map.put("finishStart", false);
			map.put("workflowCode", workFlowTask.getWorkFlowCode());

			Map<String, Object> tmp = this.processSyncBpm(String.format(ApiActionUrl.CREATE_WORKFLOW.getUrl(), workFlowTask.getWorkFlowCode()), ApiActionUrl.CREATE_WORKFLOW.getHttpRequestType(), map);

			if (tmp != null) {
				Map<String, Object> data = (Map<String, Object>) tmp.get("data");
				workFlowTask.setExecuteType(WorkflowExecuteType.SUCCESS.getValue());

				if (data == null || data.get("instanceId") == null) {
					return null;
				}

				workFlowTask.setInstanceId((String) data.get("instanceId"));
			}

			workFlowTask.setExecuteType(WorkflowExecuteType.SUCCESS.getValue());

		} catch (Exception e) {
			workFlowTask.setExecuteType(WorkflowExecuteType.FAIL.getValue());
			throw new ServiceException("create workflow task error!");
		}
		
		workFlowTaskMapper.updateWorkFlowTask(workFlowTask);

		return workFlowTask.getInstanceId();
	}
	
	public BizObjectInfo getBizObjectInfoByInstanceId(String instanceId){
		BizObjectInfo bizObjectInfo = workFlowMapper.getBizObjectIdByInstanceId(instanceId);
		bizObjectInfo = workFlowMapper.getBizObjectInfoByBizObjectIdAndWorkflowCode(bizObjectInfo.getId(), bizObjectInfo.getWorkflowCode());
		
		return bizObjectInfo;
	}
}
