package com.ced.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ced.app.service.ReinitialisationService;

@Controller
public class ReinitialisationController {
    @Autowired
    private ReinitialisationService reinitialisationService;
}
