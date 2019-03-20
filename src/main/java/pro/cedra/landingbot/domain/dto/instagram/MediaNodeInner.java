package pro.cedra.landingbot.domain.dto.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MediaNodeInner {
    @JsonProperty("display_url")
    private String displayUrl;
    private String shortcode;
}
