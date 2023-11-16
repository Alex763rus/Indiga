package com.example.indiga.model.menu.admin;

import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.wrapper.SendDocumentWrap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.COMMAND_SEND_TO_CONTACTS;
import static com.example.indiga.enums.FileType.USER_IN;
import static com.example.indiga.enums.State.ADMIN_WAIT_CONTACT_MESSAGE;
import static com.example.indiga.enums.State.FREE;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component(COMMAND_SEND_TO_CONTACTS)
@Slf4j
public class MenuSendToContacts extends Menu {

    @Override
    public String getMenuComand() {
        return COMMAND_SEND_TO_CONTACTS;
    }

    @Override
    public String getDescription() {
        return COMMAND_SEND_TO_CONTACTS;
    }

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        switch (stateService.getState(user)) {
            case FREE:
                return freelogic(user, update);
            case ADMIN_WAIT_CONTACT_MESSAGE:
                return waitWarningMessageLogic(user, update);
        }
        return createErrorDefaultMessage(user);
    }

    @Override
    public PartialBotApiMethod replaceButton(Update update, User user) {
        return super.replaceButton(update, user);
    }

    private List<PartialBotApiMethod> waitWarningMessageLogic(User user, Update update) {
        try {
            if (update.hasCallbackQuery()) {
                return createErrorDefaultMessage(user);
            }
            val answer = new ArrayList<PartialBotApiMethod>();
            val users = userService.getAllUsers();
            if (update.getMessage().hasDocument()) {
                val field = update.getMessage().getDocument();
                val fileFullPath = fileUploadService.getFileName(USER_IN, field.getFileName());
                val file = fileUploadService.uploadFileFromTg(fileFullPath, field.getFileId());
                val message = update.getMessage().getCaption();
                users.forEach(contact -> answer.add(createDocumentMessage(contact, message, new InputFile(file))));
            } else {
                val message = update.getMessage().getText();
                users.forEach(contact -> answer.add(createMessage(contact, message)));
            }
            stateService.setState(user, FREE);
            answer.add(createMessage(user, "Сообщение успешно разослано контактам. Количество: " + answer.size()));
            return answer;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return createMessageList(user, "Ошибка массовой рассылки: " + ex.getMessage());
        }
    }

    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        stateService.setState(user, ADMIN_WAIT_CONTACT_MESSAGE);
        val message = new StringBuilder();
        message.append("Рассылка сообщения контактам.").append(NEW_LINE)
                .append("Введите сообщение:").append(NEW_LINE);
        return createMessageList(user, message.toString());
    }

}
