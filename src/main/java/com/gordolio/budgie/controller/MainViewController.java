package com.gordolio.budgie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gordolio.budgie.service.SoundService;

@Controller
public class MainViewController {

    @Autowired
    private SoundService soundService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("sounds", SoundService.FILE_NAMES);
        return "index";
    }

    @RequestMapping("/anySound")
    @ResponseBody
    public String playSound() {
        this.soundService.playAnySound();
        return "success";
    }

    @RequestMapping("/sound")
    @ResponseBody
    public String playSound(@RequestParam("sound") String sound) {
        this.soundService.playWav(sound);
        return "success";
    }

}
