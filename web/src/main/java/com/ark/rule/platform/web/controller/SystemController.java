
package com.ark.rule.platform.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口探活.
 *
 */
@RestController
@RequestMapping
@Slf4j
public class SystemController {

    /**
     * 服务探活检查.
     *
     * @return ''
     */
    @GetMapping("/web/template/checkServerHealth")
    public String checkServerHealth() {
        log.info("web template checkServerHealth ok");
        return "ok";
    }
}
