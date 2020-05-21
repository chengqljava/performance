package com.chengql.template.controller.ibeetl_demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/beetl")
public class BeetlSimpleController {

    @RequestMapping("/hello")
    public String hello(Model model) {
        return "hello";
    }

    @RequestMapping("/redirect/hello")
    public String redirectHello(Model model) {
        return "redirect:/hello";
    }
}
