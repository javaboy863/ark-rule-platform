

package com.ark.rule.platform.common.aviator.function;

import com.ark.rule.platform.common.util.AssertUtil;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorObject;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 描述类的功能.
 *
 */
@Slf4j
public abstract class SimpleAbstractFunction extends AbstractFunction {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject reqParam, AviatorObject configParam,
                              AviatorObject defaultParam) {

        String configValue = FunctionUtils.getStringValue(configParam, env);
        AssertUtil.assertNotBlank(configValue, getName() + " ConfigValue cant not be blank");
        String defaultValue = FunctionUtils.getStringValue(defaultParam, env);
        AssertUtil.assertNotBlank(defaultValue, getName() + " defaultValue cant not be blank");
        if (StringUtils.isNotBlank(defaultValue) && configValue.equals(defaultValue)) {
            return AviatorBoolean.TRUE;
        }
        String reqValue = FunctionUtils.getStringValue(reqParam, env);
        if (StringUtils.isBlank(reqValue)) {
            log.debug("metaCode:{} function:{} reqValue为空不能匹配", ((AviatorJavaType) reqParam).getName(), getName());
            return AviatorBoolean.FALSE;
        }
        boolean operate = operate(reqValue, configValue);
        if (!operate) {
            log.debug("metaCode:{} function:{} reqValue:{}, configValue:{}, defaultValue:{} 未匹配",
                    ((AviatorJavaType) reqParam).getName(), getName(), reqValue, configValue, defaultValue);
        }
        return AviatorBoolean.valueOf(operate);
    }

    /**
     * 每个操作符自己的实现.
     *
     * @param reqValue    请求参数
     * @param configValue 配置值
     * @return 返回的结果
     */
    protected abstract boolean operate(String reqValue, String configValue);
}
