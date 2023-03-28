
package com.ark.rule.platform.common.enums;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * 操作符枚举.
 *
 */
public enum OperatorEnum {
    /**
     * 相等.
     */
    EQUALITY("==", "逻辑相等", "FunctionEqual(metaCode.reqArg, metaCode.configArg, metaCode.defaultArg)"),
    /**
     * 包含.
     */
    IN("in", "包含", "FunctionIn(metaCode.reqArg, metaCode.configArg, metaCode.defaultArg)"),
    /**
     * 之间,介于.
     */
    BETWEEN("between", "之间,介于", "FunctionBetween(metaCode.reqArg, metaCode.configArg, metaCode.defaultArg)");

    /**
     * 操作符code.
     */
    private String code;
    /**
     * 操作符描述.
     */
    private String desc;
    /**
     * 操作符对应方法.
     * 注:使用时用replace方式将'metaCode' 替换成 实际的metaCode.
     */
    private String functionName;

    /**
     * 根据操作符获取方法名.
     *
     * @param operator ''
     * @return ''
     */
    public static String getFunctionByOperator(String operator) {
        if (StringUtils.isBlank(operator)) {
            return null;
        }
        for (OperatorEnum operatorEnum : OperatorEnum.values()) {
            if (operatorEnum.getCode().equals(operator)) {
                return operatorEnum.getFunctionName();
            }
        }
        return null;
    }

    /**
     * 根据code获取操作符枚举.
     *
     * @param operator 操作符code
     * @return ''
     */
    public static OperatorEnum getOperatorEnumByCode(String operator) {
        if (StringUtils.isBlank(operator)) {
            return null;
        }
        for (OperatorEnum operatorEnum : OperatorEnum.values()) {
            if (operatorEnum.getCode().equals(operator)) {
                return operatorEnum;
            }
        }
        return null;
    }

    /**
     * 获取所有操作符方法.
     *
     * @return ''
     */
    public static List<String> getAllFunction() {
        List<String> functions = Lists.newArrayList();
        for (OperatorEnum operatorEnum : OperatorEnum.values()) {
            functions.add(operatorEnum.functionName);
        }
        return functions;
    }

    OperatorEnum(String code, String desc, String functionName) {
        this.code = code;
        this.desc = desc;
        this.functionName = functionName;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getFunctionName() {
        return functionName;
    }
}
