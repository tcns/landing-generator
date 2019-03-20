package pro.cedra.landingbot.domain.dto.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GraphQl {
    @JsonProperty("user")
    private User user;
}
