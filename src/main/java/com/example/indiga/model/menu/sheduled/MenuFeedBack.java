package com.example.indiga.model.menu.sheduled;

import com.example.indiga.enums.ServiceType;
import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import com.example.indiga.model.menu.services.MenuServiceBase;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_FEED_BACK;
import static com.example.indiga.constant.Constant.Command.COMMAND_SERVICE_1;
import static com.example.indiga.enums.ServiceType.SERVICE_1;
import static com.example.indiga.enums.State.SHEDULED_WAIT_COMMAND;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;

@Component(COMMAND_FEED_BACK)
@Slf4j
public class MenuFeedBack extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_FEED_BACK;
    }

    @Override
    public String getDescription() {
        return COMMAND_FEED_BACK;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return freelogic(user, update);
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val messageText = "Благодарю! Это поможет нам становиться лучше!";
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_FEED_BACK).setValue("Оставить отзыв").setLink("https://yandex.ru/maps/org/indiga_studio/89368149376/").build(),
                Button.init().setKey(COMMAND_START).setValue("Главное меню").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        stateService.setState(user, SHEDULED_WAIT_COMMAND);
        return createMessageList(user, messageText, buttonsDescription);
    }

}
