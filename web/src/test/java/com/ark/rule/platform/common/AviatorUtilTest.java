package com.ark.rule.platform.common;


import com.ark.rule.platform.common.aviator.AviatorUtil;
import com.ark.rule.platform.common.aviator.bo.ExecuteArgBO;
import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.Test;

/**
 * 描述类的功能.
 *
 */

public class AviatorUtilTest  {

    @Test
    public void testExecutor() {
        Map<String, Object> param = Maps.newHashMap();
        /*param.put("region_r", "1");
        param.put("region_c", "0");
        param.put("region_d", "0");

        param.put("new_user_r", "1");
        param.put("new_user_c", "0");
        param.put("new_user_d", "3");*/

        ExecuteArgBO region = ExecuteArgBO.builder().reqArg("1").configArg("0").defaultArg("0").build();

        ExecuteArgBO new_user = ExecuteArgBO.builder().reqArg("1").configArg("2").defaultArg("0").build();
        param.put("region", region);
        param.put("new_user", new_user);

        String express = "FunctionIn(region.reqArg,region.configArg,region.defaultArg)&&FunctionEqual(new_user.reqArg,new_user.configArg,new_user.defaultArg)";
        Boolean result = AviatorUtil.execute(express, param);
        System.out.println(result);
    }
}

