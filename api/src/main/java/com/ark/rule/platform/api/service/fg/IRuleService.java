package com.ark.rule.platform.api.service.fg;

import com.ark.rule.platform.api.Result;
import com.ark.rule.platform.api.domain.fg.request.BatchExecuteReqDTO;
import com.ark.rule.platform.api.domain.fg.request.ExecuteReqDTO;
import com.ark.rule.platform.api.domain.fg.response.ExecuteResultDTO;
import com.ark.rule.platform.api.domain.fg.response.HitRuleDTO;
import java.util.List;

/**
 * 规则引擎C端接口.
 *
 */
public interface IRuleService {

    /**
     * 规则执行.
     *
     * @param execute 规则执行请求体
     * @return 执行结果
     */
    Result<HitRuleDTO> execute(ExecuteReqDTO execute);

    /**
     * 规则批量执行.
     *
     * @param req 批量请求数据
     * @return 返回的执行结果
     */
    Result<List<ExecuteResultDTO>> batchExecute(BatchExecuteReqDTO req);
}
