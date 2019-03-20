package pro.cedra.landingbot.domain.dto.vk;

import lombok.Data;

import java.util.List;

@Data
public class GroupList {
    private List<Group> response;
}
