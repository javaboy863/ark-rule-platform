
package com.ark.rule.platform.common.aviator.function;

/**
 * 等于操作函数.
 *
 */
public class FunctionEqual extends SimpleAbstractFunction {

    @Override
    protected boolean operate(String reqValue, String configValue) {
        return reqValue.equals(configValue);
    }
}
