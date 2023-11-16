package com.example.indiga.model.menu.admin;

import com.example.indiga.model.jpa.Discount;
import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.DeleteMessageWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.*;
import static com.example.indiga.enums.State.*;
import static com.example.indiga.enums.UserRole.EMPLOYEE;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component(COMMAND_DISCOUNT_SETUP)
@Slf4j
public class MenuDiscountSetup extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_DISCOUNT_SETUP;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freelogic(user);
                case DISCOUNT_SETUP_WAIT_BEGIN -> waitBeginLogic(user, update);
                case DISCOUNT_SETUP_WAIT_MESSAGE -> waitMessageLogic(user, update);
                default -> errorMessageDefault(update);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> waitMessageLogic(User user, Update update) {
        if (!update.hasMessage()) {
            return createMessageList(user, "Сообщение должно содержать описание акции");
        }
        val discountDescription = update.getMessage().getText();
        val discounts = discountRepository.findAll();
        val discount = discounts.isEmpty() ? new Discount() : discounts.get(0);
        discount.setDescription(discountDescription);
        discountRepository.save(discount);
        val messageText = new StringBuilder();
        messageText.append("Акция успешно сохранена:").append(NEW_LINE)
                .append(discount.getDescription());
        stateService.setState(user, FREE);
        return createMessageList(user, messageText.toString());
    }

    private List<PartialBotApiMethod> waitBeginLogic(User user, Update update) {
        if (!update.hasCallbackQuery()) {
            return errorMessageDefault(update);
        }
        if (update.getCallbackQuery().getData().equals(DISCOUNT_SETUP_WAIT_BEGIN.name())) {
            stateService.setState(user, DISCOUNT_SETUP_WAIT_MESSAGE);
            return createMessageList(user, "Введите новое акционное сообщение:");
        }
        return errorMessageDefault(update);
    }

    private List<PartialBotApiMethod> freelogic(User user) {
        val messageText = new StringBuilder();
        val discounts = discountRepository.findAll();
        val discountMessage = discounts.isEmpty() ? "Акций пока нет" : discounts.get(0).getDescription();
        messageText.append("Текущее сообщение об акциях:").append(NEW_LINE)
                .append(discountMessage).append(NEW_LINE)
        ;
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(DISCOUNT_SETUP_WAIT_BEGIN.name()).setValue("Ввести новое").build(),
                Button.init().setKey(COMMAND_START).setValue("Назад").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(2).setButtons(buttons).build();
        stateService.setState(user, DISCOUNT_SETUP_WAIT_BEGIN);
        return createMessageList(user, messageText.toString(), buttonsDescription);
    }


    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
