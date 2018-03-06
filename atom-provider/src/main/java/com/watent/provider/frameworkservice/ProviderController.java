package com.watent.provider.frameworkservice;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Atom
 */
@Controller
public class ProviderController {

    @ResponseBody
    @GetMapping("/test")
    public String index() {

        return "provider ok";
    }
}
