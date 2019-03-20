package pro.cedra.landingbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.cedra.landingbot.domain.ChatUser;
import pro.cedra.landingbot.repository.ChatUserRepository;


/**
 * Created by tignatchenko on 25/04/17.
 */
@Service
public class ChatUserService {

    @Autowired
    ChatUserRepository chatUserRepository;

    public ChatUser createOrReturnChatUser (Long telegramChatId) {
        ChatUser chatUser = chatUserRepository.getChatUserByTelegramChatId(telegramChatId);
        if (chatUser != null) {
            return chatUser;
        }
        chatUser = new ChatUser();
        chatUser.setTelegramChatId(telegramChatId);
        chatUser = chatUserRepository.save(chatUser);
        return chatUser;
    }
    public ChatUser getChatUser (Long chatId) {
        ChatUser chatUser = chatUserRepository.getChatUserByTelegramChatId(chatId);
        return chatUser;
    }

    public ChatUser saveYandexToken (Long chatId, String yandexToken) {
        ChatUser chatUser = chatUserRepository.getChatUserByTelegramChatId(chatId);
        if (chatUser==null) {
            chatUser = createOrReturnChatUser(chatId);
        }
        chatUser.setYaToken(yandexToken);
        return chatUserRepository.save(chatUser);
    }

}
