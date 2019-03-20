package pro.cedra.landingbot.service.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.cedra.landingbot.domain.dto.vk.Good;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VkScraper {


    private static final HttpEntity REQUEST_ENTITY = ScraperUtils.getEntity();
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    VkApiClient vk;
    protected static final String GROUP_NAME = "json";
    private static final Pattern IMAGE_PATTERN = Pattern.compile(
        String.format("<img class=\"market_row_img\" src=\"(?<%s>.+)\"", GROUP_NAME));
    private static final Pattern NAME_PATTERN = Pattern.compile(
        String.format("<a href=\"/market-.*\" onclick=\"return Market.showItem(.*);\">(?<%s>.+)</a>", GROUP_NAME));
    private static final Pattern PRICE_PATTERN = Pattern.compile(
        String.format("<div class=\"market_row_price\">(?<%s>.+)</div>", GROUP_NAME));
    private static final Pattern LINK_PATTERN = Pattern.compile(
        String.format("<a href=\"(?<%s>.+)\" onclick=\"return Market.showItem(.*);\">.*</a>", GROUP_NAME));



//    public static void main(String[] args) throws ClientException, ApiException {
//        VkScraper scraper = new VkScraper();
//        if (scraper.vk == null) {
//            BotConfiguration botConfiguration = new BotConfiguration();
//            scraper.vk = botConfiguration.vkApiClient();
//            scraper.restTemplate = new RestTemplate();
//        }
//        ServiceActor serviceActor = scraper.auth();
//        long groupId = scraper.getGroupId(serviceActor, "https://vk.com/bani_muromets");
//        List<Good> marketItemsResponse = scraper.getMarketItemsResponse(groupId);
//
//        System.out.println();
//    }

    public List<Good> getCatalog(String vkPath) {
        ServiceActor serviceActor = auth();
        try {
            long groupId = getGroupId(serviceActor, vkPath);
            return getMarketItemsResponse(groupId);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ServiceActor auth(){
        return  new ServiceActor(6742074, "D1JnwWBBYfIMmyJ3Mgyr", "89f2651c89f2651c89f2651cb089948526889f289f2651cd22df615ae5f78ecac05f1b2");
    }

    public List<Good> getMarketItemsResponse(long marketId) {
        String pageContent = restTemplate.exchange("https://vk.com/market-" + marketId, HttpMethod.GET, REQUEST_ENTITY, String.class).getBody();
        List<Good> goods = new ArrayList<>();
        if (pageContent != null) {
            List<String> images = ScraperUtils.getFieldValuesFromPage(pageContent, IMAGE_PATTERN, GROUP_NAME);
            List<String> names = ScraperUtils.getFieldValuesFromPage(pageContent, NAME_PATTERN, GROUP_NAME);
            List<String> links = ScraperUtils.getFieldValuesFromPage(pageContent, LINK_PATTERN, GROUP_NAME);
            List<String> prices = ScraperUtils.getFieldValuesFromPage(pageContent, PRICE_PATTERN, GROUP_NAME);

            for (int i = 0; i<images.size(); i++) {
                Good good = new Good();
                good.setImage(images.get(i));
                if (names.size() > i) {
                    good.setName(names.get(i));
                }
                if (links.size() > i) {
                    good.setLink(links.get(i));
                }
                if (prices.size() > i) {
                    good.setPrice(prices.get(i).replace("rub", "руб"));
                }
                goods.add(good);
            }
        }

        return goods;
    }

    public long getGroupId(ServiceActor serviceActor, String name) throws ClientException, ApiException {
        name = getVkName(name);
        try {
            return vk.groups().getById(serviceActor).groupIds(name).execute().get(0).getId();
        } catch (Exception ex) {
            return 0;
        }

    }

    private String getVkName(String name) {
        String vk = "vk.com/";
        String groupName = "groupName";
        Pattern pattern = Pattern.compile(
            String.format("vk.com/(?<%s>.+)", groupName));
        return  Optional.of(pattern.matcher(name))
            .filter(Matcher::find)
            .map(matcher -> matcher.group(groupName))
            .orElse("");
    }
}
