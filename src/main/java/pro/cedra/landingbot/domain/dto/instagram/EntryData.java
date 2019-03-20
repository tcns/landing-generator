package pro.cedra.landingbot.domain.dto.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EntryData {
    @JsonProperty("ProfilePage")
    private List<ProfilePage> profilePage;
}
