package pro.cedra.landingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.cedra.landingbot.domain.MainPage;

import java.util.Set;


/**
 * Created by tignatchenko on 24/04/17.
 */
public interface MainPageRepository extends JpaRepository<MainPage, Long> {
    Set<MainPage> findByChatUser_TelegramChatId(Long chatId);
    Set<MainPage> findByChatUser_TelegramChatIdAndCompleted(Long chatId, boolean completed);
}
