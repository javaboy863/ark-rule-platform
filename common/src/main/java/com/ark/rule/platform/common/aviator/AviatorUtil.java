package com.ark.rule.platform.common.aviator;

import com.ark.rule.platform.common.aviator.function.FunctionEqual;
import com.ark.rule.platform.common.aviator.function.FunctionIn;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.ark.rule.platform.common.aviator.function.FunctionBetween;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 工具类.
 *
 */
public class AviatorUtil {

    private static AviatorEvaluatorInstance aviatorEvaluator;

    static {
        aviatorEvaluator = AviatorEvaluator.newInstance();
        aviatorEvaluator.addFunction(new FunctionBetween());
        aviatorEvaluator.addFunction(new FunctionIn());
        aviatorEvaluator.addFunction(new FunctionEqual());
    }

    /**
     * 预编译表达式.
     *
     * @param scripts ''
     */
    public static void compileExpressions(List<String> scripts) {
        if (CollectionUtils.isEmpty(scripts)) {
            return;
        }
        for (String script : scripts) {
            if (StringUtils.isEmpty(script)) {
                continue;
            }
            aviatorEvaluator.compile(script, true);
        }
    }

    /**
     * 进行表达式执行.
     *
     * @param express 表达式
     * @param env     上下文
     * @return 执行结果
     */
    public static boolean execute(String express, Map<String, Object> env) {
        if (StringUtils.isEmpty(express) || CollectionUtils.isEmpty(env)) {
            return Boolean.FALSE;
        }
        Expression expression = aviatorEvaluator.compile(express, true);
        return (boolean) expression.execute(env);
    }
}
