package pro.cedra.landingbot.service.bot;

import com.cloudinary.Cloudinary;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import pro.cedra.landingbot.config.ApplicationProperties;
import pro.cedra.landingbot.domain.ChatState;
import pro.cedra.landingbot.domain.ChatSteps;
import pro.cedra.landingbot.domain.Commands;
import pro.cedra.landingbot.domain.MainPage;
import pro.cedra.landingbot.repository.MainPageRepository;
import pro.cedra.landingbot.service.ChatUserService;
import pro.cedra.landingbot.service.template.RenderService;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by tignatchenko on 24/04/17.
 */
@Service
public class LandingService {

    @Autowired
    MainPageRepository mainPageRepository;

    @Autowired
    ChatStateService chatStateService;

    @Autowired
    ChatUserService service;

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    RenderService renderService;

    public SendMessage initConversation (Long chatId, long pageId) {

        MainPage page = mainPageRepository.findById(pageId).get();
        SendMessage message = new SendMessage()
            .setChatId(chatId)
            .setText(ChatSteps.states.get(page.getChatStep() + 1));

        chatStateService.updateChatStep(page.getChatStep() + 1, chatId, pageId+"");
        return message;
    }
    public SendMessage initConversation (Long chatId) {

        SendMessage message = new SendMessage()
            .setChatId(chatId)
            .setText(ChatSteps.states.get(1));
        MainPage page = new MainPage();
        page.setChatUser(service.getChatUser(chatId));
        page.setCreateDate(ZonedDateTime.now());
        page.setCompleted(false);
        page.setChatStep(0);
        page = mainPageRepository.save(page);
        chatStateService.updateChatStep(1, chatId, page.getId()+"");
        return message;
    }

    public SendMessage initEdition (Long chatId, Integer step, String metricId) {
        SendMessage message = new SendMessage()
            .setChatId(chatId)
            .setText("Введите новое значение параметра");
        chatStateService.updateChatStep(step, chatId, Commands.EDIT_ONE_PARAM_FINAL + metricId);
        return message;
    }

    private String handlePhoto(Message message, LandingBot landingBot) {
        PhotoSize fileSize = BotUtils.getPhoto(message);
        String filePath = BotUtils.getFilePath(fileSize, landingBot);
        String fullPath = org.telegram.telegrambots.meta.api.objects.File.getFileUrl(applicationProperties.getBotToken(), filePath);
        try {
            Map fileConfig = cloudinary.uploader().upload(fullPath, new HashMap());
            return (String) fileConfig.get("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public SendMessage handleInput(Message message, Long chatId, LandingBot landingBot) {
        String input = message.getText();
        ChatState chatState = chatStateService.getCurrentChatState(chatId);
        int chatStep = chatState.getStep();
        if (ChatSteps.photoSteps.contains(chatStep) && input == null) {
            try {
                input = handlePhoto(message, landingBot);
            } catch (Exception ex) {
                input = "N";
            }

        }

        SendMessage sendMessage;
        Long pageId = 0L;
        String editType = "";
        if (chatState.getData().startsWith(Commands.EDIT_ONE_PARAM_FINAL)) {
            editType = Commands.EDIT_ONE_PARAM_FINAL;
            try {
                pageId = Long.parseLong(chatState.getData().substring(Commands.EDIT_ONE_PARAM_FINAL
                                                                            .length()));
            } catch (Exception ex){}
        } else {
            try {
                pageId = Long.parseLong(chatState.getData());
            } catch (Exception ex){}
        }


        MainPage page = mainPageRepository.findById(pageId).get();
        EntityInputUtil<MainPage> mainPageEntityInputUtil = new EntityInputUtil<>();
        try {
            mainPageEntityInputUtil.setField(chatStep, input, page);
        } catch (Exception ex) {
            sendMessage = new SendMessage().setText("Вы ввели невалидное значение").
                setChatId(chatId);
            return sendMessage;
        }

        if (chatStep == ChatSteps.FINAL_STEP - 1 ||
            Commands.EDIT_ONE_PARAM_FINAL.equals(editType)) {
            chatStateService.updateChatStep(0, chatId);
            sendMessage = new SendMessage().setText(ChatSteps.states.get(ChatSteps.FINAL_STEP))
                .setChatId(chatId);
            if (chatStep == ChatSteps.FINAL_STEP - 1) {
                page.setCompleted(true);
                page.setChatStep(0);
            }
            try {
                renderService.renderMain(page);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Commands.EDIT_ONE_PARAM_FINAL.equals(editType)) {
                sendMessage.setText("Значение успешно обновлено!");
            }
        } else if (Commands.DEALS_EDIT_FINAL.equals(editType)) {
            chatStateService.updateChatStep(0, chatId);
            sendMessage = chatStateService.updateStepAndGetMessage(0, chatId, "Значение успешно обновлено");
        } else {
            page.setChatStep(chatStep);
            sendMessage = chatStateService.updateStepAndGetMessage(chatStep + 1, chatId, pageId + "");
        }

        mainPageRepository.save(page);
        return sendMessage;


    }

    public SendMessage getMainMenu(Long chatId) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();

        Commands.MAIN_COMMANDS.entrySet().forEach((command) -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(command.getValue());
            inlineKeyboardButton.setCallbackData(command.getKey());
            keyboardButtons.add(Collections.singletonList(inlineKeyboardButton));
        });

        replyMarkup.setKeyboard(keyboardButtons);
        return new SendMessage().setText("Главное меню")
            .setChatId(chatId)
            .setReplyMarkup(replyMarkup);
    }
    public InlineKeyboardMarkup getMainMenuButton() {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(">>>>>Вернуться в меню");
        inlineKeyboardButton.setCallbackData(Commands.MAIN);
        keyboardButtons.add(Collections.singletonList(inlineKeyboardButton));

        replyMarkup.setKeyboard(keyboardButtons);
        return replyMarkup;
    }
    public SendMessage getLandingList(Long chatId, String command) {
        return getLandingList(chatId, command, false);
    }
    public SendMessage getLandingList(Long chatId, String command, boolean onlyDraft) {
        Set<MainPage> pages = mainPageRepository.findByChatUser_TelegramChatId(chatId);
        if (onlyDraft) {
            pages = mainPageRepository.findByChatUser_TelegramChatIdAndCompleted(chatId, false);
        }
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>(pages.size());

        pages.stream().forEach((page) -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            String pageName = "";
            if (!page.isCompleted()) {
                pageName += "Черновик: ";
            }
            pageName += page.getName()==null?"Не указано":page.getName();
            if (page.getCreateDate() != null) {
                pageName += ": " + page.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            inlineKeyboardButton.setText(pageName);
            inlineKeyboardButton.setCallbackData(command+page.getId());
            keyboardButtons.add(Collections.singletonList(inlineKeyboardButton));
        });

        replyMarkup.setKeyboard(keyboardButtons);
        return new SendMessage().setText("список отчетов")
            .setChatId(chatId)
            .setReplyMarkup(replyMarkup);
    }



    public SendMessage getPageDefinition (Long chatId, Long pageId) {
            MainPage page = mainPageRepository.findById(pageId).get();
            String pageDefinition = "";
            if (!page.getChatUser().getTelegramChatId().equals(chatId)) {
                pageDefinition = "Страница не найдена";
            } else {
                pageDefinition = page.toString();
            }
            SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(pageDefinition);
            return sendMessage;
    }

    public SendDocument downloadPageNow (Long chatId, Long pageId) {
        MainPage mainPage = mainPageRepository.findById(pageId).get();
        SendDocument sendDocument = new SendDocument()
            .setChatId(chatId)
            .setCaption(mainPage.getName() + " экспортирован");
        try {
            File file = renderService.renderMain(mainPage);
            if (file != null) {
                sendDocument.setDocument(file);
                return sendDocument;
            } else {
                throw new IOException();
            }

        } catch (IOException e) {
            sendDocument.setCaption("Не получилось сохранить страницу");
        }

        return sendDocument;
    }


    private InlineKeyboardMarkup getKeyBoard(List<Pair> counts) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>(counts.size());

        counts.stream().forEach((count) -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(""+count.getValue());
            inlineKeyboardButton.setCallbackData("" + count.getKey());
            keyboardButtons.add(Collections.singletonList(inlineKeyboardButton));
        });

        replyMarkup.setKeyboard(keyboardButtons);
        return replyMarkup;
    }

    private InlineKeyboardMarkup getFieldKeyBoard(Long siteId) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>(ChatSteps.states.keySet().size());

        ChatSteps.states.forEach((key, value) -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(value);
            inlineKeyboardButton.setCallbackData(
                Commands.EDIT_ONE_PARAM + siteId + "-" + key);
            if (ChatSteps.mainPageParamsState.contains(key)) {
                keyboardButtons.add(Collections.singletonList(inlineKeyboardButton));
            }
        });

        replyMarkup.setKeyboard(keyboardButtons);
        return replyMarkup;
    }

    public List<MainPage> findAll () {
        return mainPageRepository.findAll();
    }

    public SendMessage editLanding (Long chatId, Long metricId) {
        return new SendMessage()
            .setChatId(chatId)
            .setText("Редактирование лендинга")
            .setReplyMarkup(getFieldKeyBoard(metricId));
    }


    public SendMessage deletePage (Long chatId, Long pageId) {
        MainPage page = mainPageRepository.getOne(pageId);
        String name = "";
        try {
            name = page.getName();
        } catch (Exception e){}
        mainPageRepository.deleteById(pageId);
        return new SendMessage()
            .setChatId(chatId)
            .setText("Сайт " + name + " удален" );
    }
}
