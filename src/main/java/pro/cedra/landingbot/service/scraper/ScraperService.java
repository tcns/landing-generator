package pro.cedra.landingbot.service.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.cedra.landingbot.domain.dto.InstagramData;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ScraperService {

    @Autowired
    OkHttpClient okHttpClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    protected static final String GROUP_NAME = "json";
    private static final Pattern JSON_PATTERN = Pattern.compile(
        String.format("window\\._sharedData = (?<%s>.+);</script>", GROUP_NAME));

    private static final HttpEntity REQUEST_ENTITY = ScraperUtils.getEntity();

    public List<String> extractInstagramGallery(String instagramUrl, int count) throws IOException {

        String pageContent = restTemplate.exchange("https://www.instagram.com/" + getAccountName(instagramUrl) + "/", HttpMethod.GET, REQUEST_ENTITY, String.class).getBody();

        String val = ScraperUtils.getFieldValueFromPage(pageContent, JSON_PATTERN, GROUP_NAME);
        InstagramData object =
            objectMapper.readValue(val, InstagramData.class);
        List<String> response = object.getEntryData().getProfilePage().get(0)
            .getGraphQl()
            .getUser()
            .getTimeLineMediaList()
            .getNodes()
            .stream()
            .map(a->a.getNode().getDisplayUrl())
            .limit(count)
            .collect(Collectors.toList());
        return response;

    }

    private String getAccountName(String instagramUrl) {
        String baseUrl = "instagram.com/";
        if (!instagramUrl.contains(baseUrl)) {
            return instagramUrl;
        }
        String name = instagramUrl.substring(instagramUrl.indexOf(baseUrl) + baseUrl.length()).replace("/", "");
        return name;

    }

}
