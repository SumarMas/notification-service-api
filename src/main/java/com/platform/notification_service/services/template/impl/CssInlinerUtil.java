package com.platform.notification_service.services.template.impl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Utility class to apply inline CSS to email HTML templates.
 * Works with Gmail, Outlook, and most clients.
 */
@Slf4j
@SuppressWarnings("PMD.LooseCoupling")
public class CssInlinerUtil {
    /** Constant for the number two. */
    private static final int TWO = 2;

    /**
     * Converts <style> rules in the HTML into inline CSS
     * for better email compatibility.
     *
     * @param html original HTML template
     * @return HTML string with inline CSS
     */
    public static String inlineCss(String html) {
        Document doc = Jsoup.parse(html);

        // Extract <style> blocks
        Elements styleElements = doc.select("style");
        StringBuilder combinedStyles = new StringBuilder();

        for (Element style : styleElements) {
            combinedStyles.append(style.data());
            style.remove(); // Gmail ignores <style>
        }

        String css = combinedStyles.toString();

        // ✅ Remove CSS comments like /* ... */
        css = css.replaceAll("/\\*.*?\\*/", "").trim();

        if (!css.isBlank()) {
            applyInline(css, doc);
        }

        return doc.html();
    }

    private static void applyInline(String css, Document doc) {
        // Handle simple selectors: tag, .class, #id
        String[] rules = css.split("}");
        for (String rule : rules) {
            String[] parts = rule.split("\\{");
            if (parts.length != TWO) {
                continue;
            }
            String selector = parts[0].trim();
            String styles = parts[1].trim();

            if (selector.isEmpty() || styles.isEmpty()) {
                continue;
            }

            try {
                Elements elements = doc.select(selector);
                for (Element el : elements) {
                    String existingStyle = el.attr("style");
                    el.attr("style", (existingStyle.isBlank() ? "" : existingStyle + "; ") + styles);
                }
            } catch (Exception e) {
                // Ignorar selectores no válidos (ej. @media, keyframes, etc.)
                log.debug(e.getMessage());
            }
        }
    }
}
