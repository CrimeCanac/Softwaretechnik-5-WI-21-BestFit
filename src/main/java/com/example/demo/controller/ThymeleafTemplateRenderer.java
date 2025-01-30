package com.example.demo.controller;

//Author: Fabian Müller

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.demo.model.entities.Geraet;
import com.example.demo.model.entities.Uebung;
import com.example.demo.service.GeraetService;

@Component
public class ThymeleafTemplateRenderer {

    private static TemplateEngine templateEngine;

    @Autowired
    public ThymeleafTemplateRenderer(TemplateEngine templateEngine) {
        ThymeleafTemplateRenderer.templateEngine = templateEngine;
    }

    public static String render(String templateName, Object model) {
        Context context = new Context();
        context.setVariables((Map<String, Object>) model);
        return templateEngine.process(templateName, context);
    }
}
