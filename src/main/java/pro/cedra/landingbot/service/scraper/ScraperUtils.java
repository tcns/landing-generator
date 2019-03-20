package pro.cedra.landingbot.service.scraper;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ScraperUtils {

    public static HttpEntity getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authority", "socialblade.com");
        headers.set("accept", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.set("upgrade-insecure-requests", "1");
        headers.set("Accept-Language", "en");
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        return new HttpEntity(headers);
    }

    public final String getFieldValueFromPage(String pageContent, Pattern pattern, String groupName) {
        return Optional.of(pattern.matcher(pageContent))
            .filter(Matcher::find)
            .map(matcher -> matcher.group(groupName))
            .orElse("");
    }

    public final List<String> getFieldValuesFromPage(String pageContent, Pattern pattern, String groupName) {
        Matcher matcher = pattern.matcher(pageContent);
        List<String> values = new ArrayList<>();
        while (matcher.find()) {
            values.add(matcher.group(groupName));
        }
        return values;
    }
}
