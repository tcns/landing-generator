package pro.cedra.landingbot.domain.dto.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    private String biography;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("edge_owner_to_timeline_media")
    private TimeLineMediaList timeLineMediaList;
}
