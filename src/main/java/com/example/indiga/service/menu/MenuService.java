package com.example.indiga.service.menu;

import com.example.indiga.model.dictionary.security.Security;
import com.example.indiga.model.menu.MenuActivity;
import com.example.indiga.model.menu.base.MenuDefault;
import com.example.indiga.service.database.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

import static com.example.indiga.enums.State.FREE;
import static com.example.indiga.enums.State.SHEDULED_WAIT_COMMAND;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;

@Slf4j
@Service
public class MenuService {

    @Autowired
    private UserService userService;

    @Autowired
    private Security security;

    @Autowired
    private MenuDefault menuActivityDefault;

    @Autowired
    private StateService stateService;

    public List<PartialBotApiMethod> messageProcess(Update update) {
        val user = userService.getUser(update);
        MenuActivity menuActivity = null;
        if (update.hasMessage()) {
            val menu = security.getMenuActivity(update.getMessage().getText());
            if (menu != null) {
                menuActivity = security.checkAccess(user, menu.getMenuComand()) ? menu : menuActivityDefault;
            }
        }
        if (update.hasCallbackQuery()) {
            val menu = security.getMenuActivity(update.getCallbackQuery().getData());
            if (menu != null) {
                menuActivity = security.checkAccess(user, menu.getMenuComand()) ? menu : menuActivityDefault;
            }
        }

        if (menuActivity != null) {
            stateService.setMenu(user, menuActivity);
        } else {
            menuActivity = stateService.getMenu(user);
            if (menuActivity == null) {
                log.warn("Не найдена команда с именем: " + update.getMessage().getText());
                menuActivity = menuActivityDefault;
            }
        }

        val answer = new ArrayList<PartialBotApiMethod>();
        val editButton = menuActivity.replaceButton(update, user);
        if (editButton != null) {
            answer.add(editButton);
        }
        answer.addAll(menuActivity.menuRun(user, update));

        if (stateService.getState(user) == FREE && !menuActivity.getMenuComand().equals(COMMAND_START)) {
            stateService.setState(user, SHEDULED_WAIT_COMMAND);
            answer.add(menuActivity.afterProcessing(update, user));
        }
//        if (stateService.getState(user) == FREE && !menuActivity.getMenuComand().equals(COMMAND_START)) {
//            val buttons = new ArrayList<Button>(List.of(
//                    Button.init().setKey(COMMAND_START).setValue("Главное меню").build()
//            ));
//            val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
//            stateService.setState(user, SHEDULED_WAIT_COMMAND);
//            answer.add(SendMessageWrap.init()
//                    .setChatIdLong(user.getChatId())
//                    .setText("Главное меню:")
//                    .setInlineKeyboardMarkup(createVerticalColumnMenu(buttonsDescription))
//                    .build().createMessage());
//            //answer.addAll(menuStart.menuRun(user, update));
//        }
        return answer;
    }

    public List<BotCommand> getMainMenuComands() {
        val menu = security.getMenuActivity(COMMAND_START);
        return List.of(new BotCommand(menu.getMenuComand(), menu.getDescription()));
    }

}