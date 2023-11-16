package com.example.indiga.model.menu;

import com.example.indiga.model.jpa.User;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.EditMessageTextWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.indiga.enums.State.FREE;
import static com.example.indiga.enums.State.SHEDULED_WAIT_COMMAND;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;


public interface MenuActivity {

    String getMenuComand();

    String getDescription();

    List<PartialBotApiMethod> menuRun(User user, Update update);

    default PartialBotApiMethod replaceButton(Update update, User user) {
        if (!update.hasCallbackQuery()) {
            return null;
        }
        val message = update.getCallbackQuery().getMessage();
        val menuName = message.getReplyMarkup().getKeyboard().stream()
                .flatMap(Collection::stream)
                .filter(e -> e.getCallbackData().equals(update.getCallbackQuery().getData()))
                .findFirst().get().getText();
        return EditMessageTextWrap.init()
                .setChatIdLong(message.getChatId())
                .setMessageId(message.getMessageId())
                .setText("Выбрано меню: " + menuName)
                .build().createMessage();
    }

    default PartialBotApiMethod afterProcessing(Update update, User user) {
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_START).setValue("Главное меню").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText("Главное меню:")
                .setInlineKeyboardMarkup(createVerticalColumnMenu(buttonsDescription))
                .build().createMessage();
    }
}
