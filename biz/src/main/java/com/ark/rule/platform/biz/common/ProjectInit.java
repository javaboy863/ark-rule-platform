
package com.ark.rule.platform.biz.common;

import com.ark.rule.platform.common.aviator.AviatorUtil;
import com.ark.rule.platform.common.enums.OperatorEnum;
import com.ark.rule.platform.domain.dto.inner.IRuleTemplateDTO;
import com.ark.rule.platform.domain.dto.response.GroupRuleBaseResDTO;
import com.ark.rule.platform.domain.service.db.IGroupRuleDbService;
import com.ark.rule.platform.domain.service.db.IRuleTemplateDbService;
import com.ark.rule.platform.domain.service.guava.IGroupGuavaService;
import com.ark.rule.platform.domain.service.guava.IRuleGuavaService;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * 项目初始化加载.
 *
 */
@Component
@Slf4j
public class ProjectInit {

    @Resource
    private IRuleTemplateDbService ruleTemplateDbService;

    @Resource
    private IGroupRuleDbService groupRuleDbService;

    @Resource
    private IGroupGuavaService groupGuavaService;

    @Resource
    private IRuleGuavaService ruleGuavaService;


    /**
     * 初始化数据.
     */
    @PostConstruct
    public void init() {
        aviatorCompileExpressions();
        initRuleGuava();
    }

    /**
     * 初始化规则guava缓存.
     */
    private void initRuleGuava() {
        initGroupRuleGuava();
    }

    /**
     * 初始化规则组guava.
     */
    private void initGroupRuleGuava() {
        try {
            List<GroupRuleBaseResDTO> allGroup = groupRuleDbService.queryAllValidGroup();
            if (CollectionUtils.isEmpty(allGroup)) {
                return;
            }
            List<Long> groupIds = allGroup.stream().map(GroupRuleBaseResDTO::getId).collect(Collectors.toList());
            groupGuavaService.getGroupByIdList(groupIds);
            for (Long groupId : groupIds) {
                //规则组规则列表
                ruleGuavaService.getGroupRuleByGroupId(groupId);
                //规则组元数据值对应规则列表
                ruleGuavaService.getMetaValueRuleIds(groupId);
            }
        } catch (Exception e) {
            log.error("initGroupRuleGuava error:", e);
        }
    }

    /**
     * 预编译表达式.
     */
    private void aviatorCompileExpressions() {
        try {
            //预编译规则模板表达式.
            List<IRuleTemplateDTO> ruleTemplates = ruleTemplateDbService.queryAllRuleTemplate();
            if (CollectionUtils.isNotEmpty(ruleTemplates)) {
                List<String> scripts = ruleTemplates.stream()
                        .map(IRuleTemplateDTO::getScript).collect(Collectors.toList());
                AviatorUtil.compileExpressions(scripts);
            }
            //预编译操作符表达式.
            List<String> allFunction = OperatorEnum.getAllFunction();
            AviatorUtil.compileExpressions(allFunction);
        } catch (Exception e) {
            log.error("aviatorCompileExpressions error:", e);
        }
    }
}

