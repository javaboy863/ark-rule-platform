
package com.ark.rule.platform.common.aviator.function;

import com.ark.rule.platform.common.constant.RuleConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * between操作函数.
 *
 */
@Slf4j
public class FunctionBetween extends SimpleAbstractFunction {

    private static final int PARAM_SIZE = 2;

    @Override
    protected boolean operate(String reqValue, String configValue) {
        String[] configArray = configValue.split(RuleConstant.SPLIT);
        if (configArray.length != PARAM_SIZE) {
            log.error("configValue = {} : not reasonable", configValue);
            throw new IllegalArgumentException("configValue : " + configValue + " size must be " + PARAM_SIZE);
        }
        double reqNum = NumberUtils.toDouble(reqValue);
        double lower = NumberUtils.toDouble(configArray[0], Long.MIN_VALUE);
        double upper = NumberUtils.toDouble(configArray[1], Long.MIN_VALUE);

        return reqNum >= lower && reqNum < upper;
    }
}
