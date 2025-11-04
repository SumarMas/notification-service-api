package com.platform.notification_service.services.template.impl;

import com.platform.notification_service.services.template.ITemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Implementation of the ITemplateService for rendering HTML templates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService implements ITemplateService {
    /** Resource loader for loading template files */
    private final ResourceLoader resourceLoader;
    /**
     * Renders the HTML template replacing placeholders with provided values.
     *
     * @param templateName name of the HTML template file (without path)
     * @param variables    map with placeholder keys and their replacement values
     * @return rendered HTML string
     */
    @Override
    public String render(String templateName, Map<String, String> variables) {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/" + templateName);
            String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            StringSubstitutor substitutor = new StringSubstitutor(variables, "${", "}");
            String styleInLine = CssInlinerUtil.inlineCss(substitutor.replace(template));
            return styleInLine;
        } catch (IOException ex) {
            log.error("Error loading template: {}", templateName, ex);
            throw new RuntimeException("Failed to load template: " + templateName, ex);
        }
    }
}
