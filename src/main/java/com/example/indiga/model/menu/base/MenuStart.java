package com.example.indiga.model.menu.base;

import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.unregister.MenuRegister;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.example.indiga.constant.Constant.Command.*;
import static com.example.indiga.enums.ServiceType.*;
import static com.example.indiga.enums.State.REGISTER_WAIT_FIO;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component(COMMAND_START)
@Slf4j
public class MenuStart extends Menu {

    @Autowired
    private MenuRegister menuRegister;

    @Override
    public String getMenuComand() {
        return COMMAND_START;
    }

    @Override
    public PartialBotApiMethod replaceButton(Update update, User user) {
        return createDeleteMessage(update);
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (user.getUserRole()) {
                case BLOCKED -> createMessageList(user, "Доступ запрещен");
                case UNREGISTERED -> getUnregisteredMenuText(user, update);
                case EMPLOYEE -> getEmployeeMenu(user);
                case MANAGER -> getManagerMenu(user);
                case ADMIN -> getAdminMenu(user);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> getUnregisteredMenuText(User user, Update update) {
        val messageText = new StringBuilder();
        messageText.append("Приветствую, ").append(prepareShield(user.getFirstName())).append(SPACE).append(NEW_LINE)
                .append("Я Инди, чат-бот IndigaStudio").append(NEW_LINE)
                .append("Давайте познакомимся?").append(NEW_LINE)
                .append("Шаг 1/3 как могу к Вам обращаться?").append(NEW_LINE);

        stateService.setMenu(user, menuRegister);
        stateService.setState(user, REGISTER_WAIT_FIO);
        return createMessageList(user, messageText.toString());
    }

    public static StringBuilder getEmployeeMenuText() {
        val messageText = new StringBuilder();
        messageText
                .append("*3Д моделирование:*").append(NEW_LINE)
                .append("- ").append(SERVICE_1.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_1)).append(NEW_LINE)
                .append("- ").append(SERVICE_2.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_2)).append(NEW_LINE)
                .append("- ").append(SERVICE_3.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_3)).append(NEW_LINE)
                .append(NEW_LINE)
                .append("*Редизайн сайта:*").append(NEW_LINE)
                .append("- ").append(SERVICE_4.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_4)).append(NEW_LINE)
                .append("- ").append(SERVICE_5.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_5)).append(NEW_LINE)
                .append("- ").append(SERVICE_7.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_7)).append(NEW_LINE)
                .append(NEW_LINE)
                .append("*Лидогенерация:*").append(NEW_LINE)
                .append("- ").append(SERVICE_8.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_8)).append(NEW_LINE)
                .append("- ").append(SERVICE_9.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_9)).append(NEW_LINE)
                .append("- ").append(SERVICE_10.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_10)).append(NEW_LINE)
                .append("- ").append(SERVICE_11.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_11)).append(NEW_LINE)
                .append(NEW_LINE)
                .append("*Сайты под ключ:*").append(NEW_LINE)
                .append("- ").append(SERVICE_12.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_12)).append(NEW_LINE)
                .append("- ").append(SERVICE_13.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_13)).append(NEW_LINE)
                .append("- ").append(SERVICE_14.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_14)).append(NEW_LINE)
                .append(NEW_LINE)
                .append("*Маркетинг под ключ:*").append(NEW_LINE)
                .append("- ").append(SERVICE_16.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_16)).append(NEW_LINE)
                .append("- ").append(SERVICE_18.getTitle()).append(" ").append(prepareShield(COMMAND_SERVICE_18)).append(NEW_LINE)
                .append(NEW_LINE)
                .append("*О нас:*").append(NEW_LINE)
                .append("- Возможности Индига студии ").append(COMMAND_POSSIBILITIES).append(NEW_LINE)
                .append("- Спецпредложения ").append(COMMAND_DISCOUNT).append(NEW_LINE)
        ;
        return messageText;
    }

    private List<PartialBotApiMethod> getEmployeeMenu(User user) {
        return createMessageList(user, getEmployeeMenuText().toString());
    }

    public static StringBuilder getManagerMenuText() {
        val managerMenuText = getEmployeeMenuText();
        managerMenuText.append(NEW_LINE)
                .append(NEW_LINE)
                .append("*Меню менеджера:*").append(NEW_LINE)
                .append("- ").append("Рассылка контактам").append(" ").append(prepareShield(COMMAND_SEND_TO_CONTACTS)).append(NEW_LINE)
        ;
        return managerMenuText;
    }

    private List<PartialBotApiMethod> getManagerMenu(User user) {
        return createMessageList(user, getManagerMenuText().toString());
    }

    private List<PartialBotApiMethod> getAdminMenu(User user) {
        val adminMenuText = getManagerMenuText();
        adminMenuText.append(NEW_LINE)
                .append("*Меню администратора:*").append(NEW_LINE)
                .append("- ").append("Выгрузить все записи").append(" ").append(prepareShield(COMMAND_EXPORT_ALL_RECEPTION)).append(NEW_LINE)
                .append("- ").append("Выгрузить новые записи").append(" ").append(prepareShield(COMMAND_EXPORT_NEW_RECEPTION)).append(NEW_LINE)
                .append("- ").append("Выгрузить контакты").append(" ").append(prepareShield(COMMAND_EXPORT_ALL_USERS)).append(NEW_LINE)
                .append("- ").append("Настройка акций").append(" ").append(prepareShield(COMMAND_DISCOUNT_SETUP)).append(NEW_LINE)
        ;
        return createMessageList(user, adminMenuText.toString());
    }

    @Override
    public String getDescription() {
        return " Начало работы";
    }
}
