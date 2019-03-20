package pro.cedra.landingbot.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tignatchenko on 23/04/17.
 */
public class Commands {
    public static final String START = "/start";

    public static final String ADD_LANDING = "/landingadd";

    public static final String DELETE_LANDING = "/landingdelete";

    public static final String EDIT_LANDING = "/landingedit";

    public static final String LANDING_LIST = "/landinglist";

    public static final String LANDING_EXPORT = "/landingexport";

    public static final String LANDING_CONTINUE = "/landingcontinue";

    public static final String LANDING_STATS = "/landingstats";

    public static final String DEALS = "/deals";

    public static final String MAIN = "/menu";

    public static final String DEALS_EDIT = "/dealsedit-";

    public static final String GET_LANDING = "/getlanding-";

    public static final String DEALS_DATE = "/dealsdate-";

    public static final String DELETE_ONE_LANDING = "/deletelanding-";

    public static final String EDIT_ONE_LANDING = "/editlanding-";

    public static final String STAT_ONE_LANDING = "/statlanding-";

    public static final String EXPORT_ONE_LANDING = "/exportlanding-";

    public static final String CONTINUE_ONE_LANDING = "/continuelanding-";

    public static final String EDIT_ONE_PARAM = "/editparam-";

    public static final String DEALS_EDIT_FINAL = "dealsedit-";

    public static final String EDIT_ONE_PARAM_FINAL = "edit-";

    public static Map<String, String> MAIN_COMMANDS = new LinkedHashMap<>();
    static {
        MAIN_COMMANDS.put(ADD_LANDING, "Создать");
        MAIN_COMMANDS.put(DELETE_LANDING, "Удалить");
        MAIN_COMMANDS.put(EDIT_LANDING, "Редактировать");
        MAIN_COMMANDS.put(LANDING_CONTINUE, "Продолжить заполнять");
        MAIN_COMMANDS.put(LANDING_LIST, "Список");
        MAIN_COMMANDS.put(LANDING_EXPORT, "Экспорт");
        MAIN_COMMANDS.put(MAIN, "Меню");
    }

}
