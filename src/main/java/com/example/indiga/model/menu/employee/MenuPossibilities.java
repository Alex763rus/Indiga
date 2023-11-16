package com.example.indiga.model.menu.employee;

import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_POSSIBILITIES;
import static java.util.Objects.requireNonNull;

@Component(COMMAND_POSSIBILITIES)
@Slf4j
public class MenuPossibilities extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_POSSIBILITIES;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        val path = new File(botConfig.getInputFilePossibilitiesPath());
        if (path.isDirectory()) {
            val file = requireNonNull(path.listFiles())[0];
            stateService.refreshUser(user);
            return List.of(SendDocumentWrap.init()
                    .setChatIdLong(user.getChatId())
                    .setCaption("Возможности индига студии")
                    .setDocument(new InputFile(file))
                    .build().createMessage());
        }
        return errorMessageDefault(update);
    }

    @Override
    public String getDescription() {
        return getMenuComand();
    }

}
