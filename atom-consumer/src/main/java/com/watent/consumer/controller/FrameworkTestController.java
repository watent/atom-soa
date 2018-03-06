package com.watent.consumer.controller;

import com.watent.provider.frameworkservice.FrameworkTestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Atom
 */
@Controller
public class FrameworkTestController {

    @Resource
    private FrameworkTestService frameworkTestService;

    @ResponseBody
    @GetMapping("/frame")
    public String index() {

        String result = frameworkTestService.sleep("100 Day");
        return result;
    }
}
