package pro.cedra.landingbot.domain;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by tignatchenko on 23/04/17.
 */
public class ChatSteps {
    public static Map<Integer, String> states = new HashMap<>();



    public static final int NAME_STEP = 1;
    public static final int MAIN_COLOR_STEP = 2;
    public static final int LOGO_STEP = 3;
    public static final int DESC_STEP = 4;
    public static final int KEYWORD_DESC_STEP = 5;
    public static final int TITLE_STEP = 6;
    public static final int SECOND_TITLE_STEP = 7;
    public static final int PHONE_STEP = 8;
    public static final int VK_STEP = 9;
    public static final int INSTAGRAM_STEP = 10;
    public static final int FORM_TITLE_STEP = 11;
    public static final int HAS_FORM_CLIENT_NAME_STEP = 12;
    public static final int HAS_FORM_CLIENT_EMAIL_STEP = 13;
    public static final int HAS_FORM_CLIENT_PHONE_STEP = 14;
    public static final int FORM_BUTTON_TEXT_STEP = 15;
    public static final int ADDRESS_STEP = 16;
    public static final int YA_COUNTER_STEP = 17;
    public static final int GA_COUNTER_STEP = 18;
    public static final int PIC_STEP = 19;
    public static final int FINAL_STEP = 20;



    public static Set<Integer> mainPageParamsState = Sets.newHashSet(NAME_STEP, MAIN_COLOR_STEP, LOGO_STEP, DESC_STEP,
        KEYWORD_DESC_STEP, TITLE_STEP, SECOND_TITLE_STEP, PHONE_STEP, VK_STEP, INSTAGRAM_STEP, FORM_TITLE_STEP, HAS_FORM_CLIENT_NAME_STEP,
        HAS_FORM_CLIENT_EMAIL_STEP, HAS_FORM_CLIENT_PHONE_STEP, FORM_BUTTON_TEXT_STEP, ADDRESS_STEP, YA_COUNTER_STEP, GA_COUNTER_STEP, PIC_STEP);


    public static Set<Integer> photoSteps = Sets.newHashSet(LOGO_STEP, PIC_STEP);
    static {
        states.put(NAME_STEP, "Введите название проекта\n" +
            "(Будет использоваться в названиях)");
        states.put(LOGO_STEP, "Прикрепить логотип? (N – Сгенерировать на основе названия)");
        states.put(MAIN_COLOR_STEP, "Укажите основной акцидентный цвет сайта в RGB(N –пропустить)\n" +
            "Например, «FF0000». (Будет использован в качестве заливки кнопок, иконок)");
        states.put(DESC_STEP, "Введите краткое описание проекта (N – пропустить)\n" +
            "(Будет использовано в расшифровке проекта в шапке сайта)");
        states.put(KEYWORD_DESC_STEP, "Введите описание проекта в 1-2 предложениях с упоминанием ключевых слов\n" +
            "(N – пропустить)\n" +
            "(Будет использовано в коде сайта для продвижения сайта в поисковых системах)");
        states.put(TITLE_STEP, ". Введите ваше торговое предложение.\n" +
            "Например, Готовые бани с печкой и отделкой за 2 недели.\n" +
            "(Будет использовано в заголовке сайта на первом экране)");
        states.put(SECOND_TITLE_STEP, "Введите второй подзаголовок (N – пропустить)\n" +
            "Например, «Монтаж за 1 день. Цены от 130000руб.»");
        states.put(PHONE_STEP, "Введите телефон по которому можно с вами связаться\n" +
            "(Будет использовано в контактных данных)");
        states.put(VK_STEP, "Введите ссылку на вконтакте (N – пропустить)\n" +
            "(Будет использовано в блоке со ссылками)");
        states.put(INSTAGRAM_STEP, "Введите ссылку на инстаграм (N – пропустить)\n" +
            "(Будет использовано в блоке со ссылками, возможно создание блока с фотогалереей из\n" +
            "инстаграм)");
        states.put(FORM_TITLE_STEP, "Введите заголовок формы");
        states.put(HAS_FORM_CLIENT_NAME_STEP, "Требуется ли поле с именем клиента? (Y | N)");
        states.put(HAS_FORM_CLIENT_EMAIL_STEP, " Требуется ли поле с email клиента? (Y | N)");
        states.put(HAS_FORM_CLIENT_PHONE_STEP, "Требуется ли поле с телефоном клиента? (Y | N)");
        states.put(FORM_BUTTON_TEXT_STEP, "Какая надпись будет использована на кнопке отправки формы? Например, скачать\n" +
            "каталог или оформить заявку?");
        states.put(ADDRESS_STEP, "Введите адрес расположения компании, например, Саратов, ул. Московская, 52. (N –\n" +
            "пропустить)\n" +
            "(Будет использовано в контактных данных.)");
        states.put(YA_COUNTER_STEP, "Введите код счетчика яндекс метрики или Google Analitics (N – пропустить)");
        states.put(GA_COUNTER_STEP, "Введите реквизиты компании (N – пропустить) (Будет использовано в контактных данных)");
        states.put(PIC_STEP, "Прикрепите изображение, которое будет использовано на заднем фоне первого экрана.");
        states.put(FINAL_STEP, "Сайт сгенерирован!");

    }
}
