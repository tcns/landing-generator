package pro.cedra.landingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.cedra.landingbot.domain.ChatState;

/**
 * Created by tignatchenko on 24/04/17.
 */
public interface ChatStateRepository extends JpaRepository<ChatState, Long> {
    ChatState findOneByChatUser_TelegramChatId(Long userId);
}
