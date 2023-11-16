package com.example.indiga.model.menu.unregister;

import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_REGISTER;
import static com.example.indiga.enums.State.*;
import static com.example.indiga.enums.UserRole.EMPLOYEE;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Component(COMMAND_REGISTER)
@Slf4j
public class MenuRegister extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_REGISTER;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case REGISTER_WAIT_FIO -> registerWaitFioLogic(user, update);
                case REGISTER_WAIT_COMPANY -> registerWaitCompanyLogic(user, update);
                case REGISTER_WAIT_PHONE -> registerWaitPhoneLogic(user, update);
                default -> errorMessageDefault(update);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> registerWaitPhoneLogic(User user, Update update) {
        if (!update.hasMessage()) {
            return createMessageList(user, "Сообщение должно содержать контактный номер телефона. Повторите, пожалуйста, ввод");
        }
        user.setInputPhone(update.getMessage().getText());
        user.setUserRole(EMPLOYEE);
        userService.saveUser(user);
        stateService.deleteUser(user);

        val adminMessage = new StringBuilder();
        adminMessage.append("Зарегистрировался новый пользователь:").append(NEW_LINE)
                .append("*Контакт:* ").append(NEW_LINE)
                .append(" -").append(prepareShield(user.getInputFio())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getInputCompany())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getInputPhone())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getUserName())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getFirstName())).append(SPACE).append(user.getLastName()).append(NEW_LINE);

        stateService.setState(user, FREE);

        val answer = new ArrayList<PartialBotApiMethod>();
        answer.add(createMessage(user, "Приятно познакомиться, " + user.getInputFio() + "!"));
        answer.addAll(createAdminMessages(adminMessage.toString()));
        return answer;
    }

    private List<PartialBotApiMethod> registerWaitCompanyLogic(User user, Update update) {
        if (!update.hasMessage()) {
            return createMessageList(user, "Сообщение должно содержать название вашей компании. Повторите, пожалуйста, ввод");
        }
        user.setInputCompany(update.getMessage().getText());
        userService.saveUser(user);
        stateService.setState(user, REGISTER_WAIT_PHONE);
        return createMessageList(user, "Шаг 3/3 Укажите контактный телефон:");
    }

    private List<PartialBotApiMethod> registerWaitFioLogic(User user, Update update) {
        if (!update.hasMessage()) {
            return createMessageList(user, "Сообщение должно содержать Ваше имя. Повторите, пожалуйста, ввод");
        }
        user.setInputFio(update.getMessage().getText());
        userService.saveUser(user);
        stateService.setState(user, REGISTER_WAIT_COMPANY);
        return createMessageList(user, "Шаг 2/3 Укажите название вашей компании:");
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
