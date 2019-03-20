package pro.cedra.landingbot.domain.dto.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProfilePage {
    @JsonProperty("graphql")
    private GraphQl graphQl;
}
