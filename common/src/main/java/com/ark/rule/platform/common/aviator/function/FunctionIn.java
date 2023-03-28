
package com.ark.rule.platform.common.aviator.function;

import lombok.extern.slf4j.Slf4j;

/**
 * in操作函数.
 *
 */
@Slf4j
public class FunctionIn extends SimpleAbstractFunction {

    private static final String SPLIT = ",";

    @Override
    protected boolean operate(String reqValue, String configValue) {
        String[] configArray = configValue.split(SPLIT);
        if (configArray == null || configArray.length <= 0) {
            log.warn("configValue = {} : not reasonable", configValue);
            return false;
        }

        for (String config : configArray) {
            if (reqValue.equals(config)) {
                return true;
            }
        }
        return false;
    }
}
