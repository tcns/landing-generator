package pro.cedra.landingbot.service.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.cedra.landingbot.config.ApplicationProperties;
import pro.cedra.landingbot.domain.Commands;
import pro.cedra.landingbot.service.ChatUserService;
import pro.cedra.landingbot.service.UserService;


/**
 * Created by tignatchenko on 22/04/17.
 */
@Component
public class LandingBot extends TelegramLongPollingBot {
    @Autowired
    ApplicationProperties applicationProperties;
    @Autowired
    LandingService landingService;
    @Autowired
    UserService userService;
    @Autowired
    ChatUserService chatUserService;
    @Autowired
    ChatStateService chatStateService;


    public void sendMessageExternal (SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            SendMessage message = null;
            SendDocument documentMessage = null;
            switch (callbackData.toLowerCase()) {
                case Commands.ADD_LANDING:
                    chatStateService.updateChatStep(0, chatId);
                    message = landingService.initConversation(chatId);
                    break;
                case Commands.LANDING_LIST:
                    chatStateService.updateChatStep(0, chatId);
                    message = landingService.getLandingList(chatId, Commands.GET_LANDING);
                    break;
                case Commands.LANDING_CONTINUE:
                    chatStateService.updateChatStep(0, chatId);
                    message = landingService.getLandingList(chatId, Commands.CONTINUE_ONE_LANDING, true);
                    break;
                case Commands.DELETE_LANDING:
                    chatStateService.updateChatStep(0, chatId);
                    message = landingService.getLandingList(chatId, Commands.DELETE_ONE_LANDING);
                    break;
                case Commands.EDIT_LANDING:
                    chatStateService.updateChatStep(0, chatId);
                    message = landingService.getLandingList(chatId, Commands.EDIT_ONE_LANDING);
                    break;
                case Commands.LANDING_EXPORT:
                    chatStateService.updateChatStep(0, chatId);
                    message = landingService.getLandingList(chatId, Commands.EXPORT_ONE_LANDING);
                    break;
                case Commands.MAIN:
                    chatStateService.updateChatStep(0, chatId);
                    message = getMainMenu(chatId);
                    break;

                default:
                    if (callbackData.startsWith(Commands.GET_LANDING)) {
                        message = landingService.getPageDefinition(chatId,
                            Long.parseLong(callbackData.substring(Commands.GET_LANDING.length())));
                    } else if (callbackData.startsWith(Commands.CONTINUE_ONE_LANDING)) {
                        message = landingService.initConversation(chatId,
                            Long.parseLong(callbackData.substring(Commands.CONTINUE_ONE_LANDING.length())));
                    } else if (callbackData.startsWith(Commands.DELETE_ONE_LANDING)) {
                        message = landingService.deletePage(chatId,
                            Long.parseLong(callbackData.substring(Commands.DELETE_ONE_LANDING.length())));
                    } else if (callbackData.startsWith(Commands.EXPORT_ONE_LANDING)) {
                        try {
                            execute(getWaitMessage(chatId));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        documentMessage = landingService.downloadPageNow(chatId,
                            Long.parseLong(callbackData.substring(Commands.EXPORT_ONE_LANDING.length())));
                    } else if (callbackData.startsWith(Commands.EDIT_ONE_LANDING)) {
                        message = landingService.editLanding(chatId,
                            Long.parseLong(callbackData.substring(Commands.EDIT_ONE_LANDING.length())));
                    } else if (callbackData.startsWith(Commands.EDIT_ONE_PARAM)) {
                        String signature = callbackData.substring(Commands.EDIT_ONE_PARAM.length());
                        String[] kv = signature.split("-");
                        message = landingService.initEdition(chatId, Integer.parseInt(kv[1]), kv[0]);
                    }

                    break;
            }


            try {
                if (message!= null) {
                    sendMessage(message);
                } else {
                    execute(documentMessage);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();

            SendMessage message;
            String input = update.getMessage().getText();
            if (input == null) {
                input = "";
            }
            if (chatUserService.getChatUser(chatId) == null) {
                input = Commands.START;
            }
            switch (input.toLowerCase()) {
                case Commands.START:
                    chatUserService.createOrReturnChatUser(chatId);
                    chatStateService.updateChatStep(0, chatId);
                    message = getMainMenu(chatId);
                    break;
                case Commands.MAIN:
                    chatStateService.updateChatStep(0, chatId);
                    message = getMainMenu(chatId);
                    break;

                default:
                    Integer chatStep = chatStateService.getCurrentStep(chatId);
                    switch (chatStep) {
                        case 0: message = getDefaultMessage(chatId);
                            break;
                        default:
                             message = landingService.handleInput(update.getMessage(), chatId, this);
                            break;
                    }
                    break;
            }

            sendMessage(message);
        }
    }

    public void sendMessage (SendMessage message) {

        addMainButton(message);
        try {
            Message execute = execute(message);
            int  id = chatStateService.saveMessageIdGetPrevious(execute.getChatId(), execute.getMessageId());
            DeleteMessage deleteMessage = new DeleteMessage()
                .setChatId(execute.getChatId())
                .setMessageId(id);
            execute (deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return applicationProperties.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return applicationProperties.getBotToken();
    }



    public SendMessage getDefaultMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage().
            setChatId(chatId)
            .setText("Введите команду");
        return sendMessage;
    }
    public SendMessage getWaitMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage().
            setChatId(chatId)
            .setText("Подождите пожалуйста...");
        return sendMessage;
    }

    public SendMessage getMainMenu(Long chatId) {
        SendMessage sendMessage = landingService.getMainMenu(chatId);
        return sendMessage;
    }

    public void addMainButton(SendMessage sendMessage) {
        if (sendMessage.getReplyMarkup()!= null &&
            sendMessage.getReplyMarkup() instanceof InlineKeyboardMarkup) {
            ((InlineKeyboardMarkup)sendMessage.getReplyMarkup())
                .getKeyboard().add(landingService.getMainMenuButton().getKeyboard().get(0));
        } else {
            sendMessage.setReplyMarkup(landingService.getMainMenuButton());
        }

    }

}
