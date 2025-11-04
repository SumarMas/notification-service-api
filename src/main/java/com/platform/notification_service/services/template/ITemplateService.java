package com.platform.notification_service.services.template;

import java.util.Map;
/**
 * Service for rendering HTML templates with dynamic content.
 */
public interface ITemplateService {
    /**
     * Renders the HTML template replacing placeholders with provided values.
     *
     * @param templateName name of the HTML template file (without path)
     * @param variables     map with placeholder keys and their replacement values
     * @return rendered HTML string
     */
    String render(String templateName, Map<String, String> variables);
}
