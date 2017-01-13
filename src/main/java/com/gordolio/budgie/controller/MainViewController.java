package com.gordolio.budgie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gordolio.budgie.service.SoundService;

@Controller
public class MainViewController {

    @Autowired
    private SoundService soundService;

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/sound")
    @ResponseBody
    public String playSound() {
        this.soundService.playSound();
        return "success";
    }

}
