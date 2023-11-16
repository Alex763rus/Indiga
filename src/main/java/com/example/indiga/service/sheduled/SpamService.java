package com.example.indiga.service.sheduled;

import com.example.indiga.model.jpa.User;
import com.example.indiga.model.jpa.UserRepository;
import com.example.indiga.service.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.wrapper.DeleteMessageWrap;
import org.example.tgcommons.model.wrapper.SendMessageWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.indiga.constant.Constant.Command.*;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.ButtonUtils.createVerticalColumnMenu;

@Service
@Slf4j
public class SpamService {

    private int numberInQueue = 0;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "${cron.expression}")
    public void spam() {
        log.info("Scheduled запуск спам рассылки");
        doSpam(scheduledMessageProcess());
    }

    public List<PartialBotApiMethod> scheduledMessageProcess() {
        ++numberInQueue;
        val users = userRepository.findAll();
        val answer = new ArrayList<PartialBotApiMethod>();
        for (User user : users) {
            answer.add(getMessagebase(user));
        }
        return answer;
    }

    private PartialBotApiMethod getMessagebase(User user) {
        if (numberInQueue == 1) {
            return getMessage1(user);
        }
        if (numberInQueue == 2) {
            return getMessage2(user);
        }
        numberInQueue = 1;
        return getMessage1(user);
    }

    private void doSpam(List<PartialBotApiMethod> answers) {
        for (val answer : answers) {
            try {
                if (answer instanceof BotApiMethod<? extends Serializable> botApiMethod) {
                    telegramBot.execute(botApiMethod);
                }
                if (answer instanceof SendDocument sendDocument) {
                    telegramBot.execute(sendDocument);
                }
                if (answer instanceof SendPhoto sendPhoto) {
                    telegramBot.execute(sendPhoto);
                }
            } catch (TelegramApiException e) {
                log.error("SCHEDULED Ошибка во время обработки сообщения: " + e.getMessage());
            }
        }
    }

    private PartialBotApiMethod getMessageNotUsed(User user) {
        val messageText = new StringBuilder();
        messageText.append("Есть вопросы, которые вы часто задаете нашей студии?").append(NEW_LINE)
                .append("Я могу предоставить ответы на часто задаваемые вопросы о наших услугах, ценах и процессе работы.");
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_FAQ).setValue("Да").build(),
                Button.init().setKey(COMMAND_START).setValue("Нет").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(2).setButtons(buttons).build();
        return createMessage(user, messageText.toString(), buttonsDescription);
    }

    private PartialBotApiMethod getMessage1(User user) {
        val messageText = new StringBuilder();
        messageText.append("Мы ценим ваше мнение!").append(NEW_LINE)
                .append("После завершения проекта, пожалуйста, поделитесь своим опытом с нами.").append(NEW_LINE)
                .append("Ваша обратная связь поможет нам стать еще лучше").append(NEW_LINE)
                .append("Хотите поделиться мнением о нашей компании?");
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_FEED_BACK).setValue("Да").build(),
                Button.init().setKey(COMMAND_START).setValue("Нет").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(2).setButtons(buttons).build();
        return createMessage(user, messageText.toString(), buttonsDescription);
    }

    private PartialBotApiMethod getMessage2(User user) {
        val messageText = new StringBuilder();
        messageText.append("У нас есть специальные акции и скидки для наших клиентов.").append(NEW_LINE)
                .append("Хотите узнать больше о текущих предложениях?").append(NEW_LINE);
        val buttons = new ArrayList<Button>(List.of(
                Button.init().setKey(COMMAND_DISCOUNT).setValue("Да").build(),
                Button.init().setKey(COMMAND_START).setValue("Нет").build()
        ));
        val buttonsDescription = ButtonsDescription.init().setCountColumn(2).setButtons(buttons).build();
        return createMessage(user, messageText.toString(), buttonsDescription);
    }

    //=======================================
    protected List<PartialBotApiMethod> createMessageList(User user, String message) {
        return List.of(this.createMessage(user, message));
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message, ButtonsDescription buttonsDescription) {
        return List.of(this.createMessage(user, message, buttonsDescription));
    }

    protected PartialBotApiMethod createMessage(User user, String message) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .build().createMessage();
    }

    protected PartialBotApiMethod createDeleteMessage(Update update) {
        if (!update.hasCallbackQuery()) {
            return null;
        }
        val message = update.getCallbackQuery().getMessage();
        return DeleteMessageWrap.init()
                .setChatIdLong(message.getChatId())
                .setMessageId(message.getMessageId())
                .build().createMessage();
    }

    protected PartialBotApiMethod createMessage(User user, String message, ButtonsDescription buttonsDescription) {
        return createMessage(user, message, createVerticalColumnMenu(buttonsDescription));
    }

    protected PartialBotApiMethod createMessage(User user, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        return SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText(message)
                .setInlineKeyboardMarkup(inlineKeyboardMarkup)
                .build().createMessage();
    }

    protected List<PartialBotApiMethod> createMessageList(User user, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        return List.of(this.createMessage(user, message, inlineKeyboardMarkup));
    }
}
