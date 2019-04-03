package pro.cedra.landingbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pro.cedra.landingbot.anno.Cast;
import pro.cedra.landingbot.anno.CastMethod;
import pro.cedra.landingbot.anno.Step;
import pro.cedra.landingbot.domain.dto.vk.Good;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
public class MainPage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Step(ChatSteps.NAME_STEP)
    private String name;

    @Column
    @Step(ChatSteps.LOGO_STEP)
    private String logo;

    @Column
    @Step(ChatSteps.MAIN_COLOR_STEP)
    private String color;

    @Column
    @Step(ChatSteps.DESC_STEP)
    private String desc;

    @Column
    @Step(ChatSteps.KEYWORD_DESC_STEP)
    private String keywordDesc;

    @Column
    @Step(ChatSteps.TITLE_STEP)
    private String title;

    @Column
    @Step(ChatSteps.SECOND_TITLE_STEP)
    private String secondTitle;

    @Column
    @Step(ChatSteps.PHONE_STEP)
    private String phone;

    @Column
    @Step(ChatSteps.VK_STEP)
    private String vk;

    @Column
    @Step(ChatSteps.INSTAGRAM_STEP)
    private String instagram;

    @Column
    @Step(ChatSteps.FORM_TITLE_STEP)
    private String formTitle;

    @Column
    @Step(ChatSteps.HAS_FORM_CLIENT_NAME_STEP)
    @Cast(CastMethod.BOOLEAN)
    private boolean hasFormClientName;

    @Column
    @Step(ChatSteps.HAS_FORM_CLIENT_EMAIL_STEP)
    @Cast(CastMethod.BOOLEAN)
    private boolean  hasFormClientEmail;

    @Column
    @Step(ChatSteps.HAS_FORM_CLIENT_PHONE_STEP)
    @Cast(CastMethod.BOOLEAN)
    private boolean  hasFormClientPhone;

    @Column
    @Step(ChatSteps.FORM_BUTTON_TEXT_STEP)
    private String formButtonText;

    @Column
    @Step(ChatSteps.ADDRESS_STEP)
    private String address;

    @Column
    @Step(ChatSteps.YA_COUNTER_STEP)
    private String yaCounterName;

    @Column
    @Step(ChatSteps.GA_COUNTER_STEP)
    private String gaCounterName;

    @Column
    @Step(ChatSteps.PIC_STEP)
    private String pic;

    @Column
    private ZonedDateTime createDate;

    private boolean completed = false;

    private Integer chatStep;

    @JsonIgnore
    @ManyToOne
    private ChatUser chatUser;


    @Transient
    @JsonInclude
    private String readColor;

    @Transient
    @JsonInclude
    private List<String> gallery;

    @Transient
    @JsonInclude
    private List<Good> goods;

    @Override
    public String toString() {
        return
            "Название: " + name + '\n' +
            "Логотип: " + logo + '\n' +
            "Цвет: " + color + '\n' +
            "Описание: " + desc + '\n' +
            "Ключевые слова: " + keywordDesc + '\n' +
            "Основной заголовк: " + title + '\n' +
            "Второй заголовок: " + secondTitle + '\n' +
            "Телефон: " + phone + '\n' +
            "Вк: " + vk + '\n' +
            "Инстаграм: " + instagram + '\n' +
            "Заголовок формы: " + formTitle + '\n' +
            "Добавлять имя на форму: " + hasFormClientName + '\n' +
            "Добавлять почту на форму: " + hasFormClientEmail + '\n' +
            "Добавлять телефон на форму: " + hasFormClientPhone + '\n' +
            "Текст на кнопке формы: " + formButtonText + '\n' +
            "Адрес: " + address + '\n' +
            "Имя яндекс счетчика: " + yaCounterName + '\n' +
            "Имя ga счетчика: " + gaCounterName + '\n' +
            "Основной баннер: " + pic;
    }
}
