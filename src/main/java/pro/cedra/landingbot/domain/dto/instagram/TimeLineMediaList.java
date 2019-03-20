package pro.cedra.landingbot.domain.dto.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TimeLineMediaList {

    private int count;

    @JsonProperty("edges")
    private List<MediaNode> nodes;
}
