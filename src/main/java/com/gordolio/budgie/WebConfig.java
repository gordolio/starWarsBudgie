package com.gordolio.budgie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            InputStream stream = WebConfig.class.getResourceAsStream("/resourcesMapping.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while(null != (line = reader.readLine())) {
                String[] split = line.split("\\|");
                if(split.length > 1 && StringUtils.isNotEmpty(split[0]) && StringUtils.isNotEmpty(split[1])) {
                    registry.addResourceHandler(split[0].trim()).addResourceLocations(split[1].trim());
                }
            }
        } catch(IOException ex) {
            LOG.error("Error mapping resources", ex);
        }
    }
}
