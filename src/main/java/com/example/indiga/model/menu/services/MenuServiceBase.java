package com.example.indiga.model.menu.services;

import com.example.indiga.enums.ServiceType;
import com.example.indiga.model.jpa.Reception;
import com.example.indiga.model.jpa.User;
import com.example.indiga.model.menu.base.Menu;
import com.vdurmont.emoji.EmojiParser;
import jakarta.persistence.MappedSuperclass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.tgcommons.enums.Emoji;
import org.example.tgcommons.model.button.Button;
import org.example.tgcommons.model.button.ButtonsDescription;
import org.example.tgcommons.model.button.CalendarDescription;
import org.example.tgcommons.model.button.Day;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Timestamp;
import java.util.*;

import static com.example.indiga.constant.Constant.Command.*;
import static com.example.indiga.constant.Constant.Command.COMMAND_DISCOUNT_SETUP;
import static com.example.indiga.constant.Constant.MenuChooseTime.BACK;
import static com.example.indiga.enums.ReceptionExportStatus.NEW_RECEPTION;
import static com.example.indiga.enums.ServiceType.*;
import static com.example.indiga.enums.State.*;
import static com.example.indiga.enums.TimePeriod.TIME_10_14;
import static com.example.indiga.enums.TimePeriod.TIME_14_19;
import static java.util.Calendar.LONG_STANDALONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.example.tgcommons.constant.Constant.Calendar.*;
import static org.example.tgcommons.constant.Constant.TextConstants.*;
import static org.example.tgcommons.utils.MessageUtils.prepareBold;
import static org.example.tgcommons.utils.StringUtils.prepareShield;

@Slf4j
@MappedSuperclass
public abstract class MenuServiceBase extends Menu {


    public abstract ServiceType getServiceType();

    public abstract String getServiceDescription();

    @Override
    public List<PartialBotApiMethod> menuRun(User user, Update update) {
        try {
            return switch (stateService.getState(user)) {
                case FREE -> freelogic(user, update);
                case CALENDAR_WAIT_CHOOSE_DATE -> waitChooseDateLogic(user, update);
                case CALENDAR_WAIT_TIME -> waitTimeLogic(user, update);
                default -> errorMessageDefault(update);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessageList(user, ex.toString());
        }
    }

    @Override
    public PartialBotApiMethod afterProcessing(Update update, User user) {
        try {
            return switch (user.getUserRole()) {
                case BLOCKED -> createMessageList(user, "Доступ запрещен").get(0);
                case UNREGISTERED -> getEmployeeMenu(user).get(0);
                case EMPLOYEE -> getEmployeeMenu(user).get(0);
                case MANAGER -> getManagerMenu(user).get(0);
                case ADMIN -> getAdminMenu(user).get(0);
            };
        } catch (Exception ex) {
            log.error(ex.toString());
            return createMessage(user, ex.toString());
        }
    }
    private List<PartialBotApiMethod> freelogic(User user, Update update) {
        val calendar = calendarService.getOrCurrentCalendar(user);
        stateService.setState(user, CALENDAR_WAIT_CHOOSE_DATE);
        return createCalendarMessage(user, calendar);
    }

    private List<PartialBotApiMethod> waitChooseDateLogic(User user, Update update) {
        val data = getInputCallback(update);
        val calendar = calendarService.getCalendar(user);
        switch (data) {
            case NEXT_MONTH -> calendar.add(Calendar.MONTH, 1);
            case PREV_MONTH -> {
                val year = calendar.get(Calendar.YEAR);
                val month = calendar.get(Calendar.MONTH);
                val currentDate = Calendar.getInstance();
                val currentYear = currentDate.get(Calendar.YEAR);
                val currentMonth = currentDate.get(Calendar.MONTH);
                if (year > currentYear || (year == currentYear && month > currentMonth)) {
                    calendar.add(Calendar.MONTH, -1);
                }
            }
            case MAIN_MENU -> stateService.setState(user, FREE);
            default -> {
                try {
                    val day = Integer.parseInt(data);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendarService.add(user, calendar);
                    return haveDateLogic(user);
                } catch (NumberFormatException ignore) {
                    int ignored = 0;
                }
            }
        }
        calendarService.add(user, calendar);
        return stateService.getState(user) == FREE ? emptyList()/*menuStart.menuRun(user, update)*/ : createCalendarMessage(user, calendar);
    }

    private List<PartialBotApiMethod> createCalendarMessage(User user, Calendar calendar) {
        val message = new StringBuilder();
        message.append(getServiceDescription()).append(NEW_LINE)
                .append(NEW_LINE)
                .append("Предлагаем созвониться и обсудить ваш запрос").append(NEW_LINE)
                .append("Консультация займет около 20 минут").append(NEW_LINE)
                .append(NEW_LINE)
                .append("В какой день и время вам было бы удобно?");
        return createMessageList(user, message.toString(), createCalendar(calendar, emptyMap()));
    }

    private List<PartialBotApiMethod> haveDateLogic(User user) {
        val calendar = calendarService.getCalendar(user);
        val message = new StringBuilder();
        val buttons = new ArrayList<Button>();
        message.append("Выбрана дата: ").append(prepareBold(new java.sql.Date(calendar.getTimeInMillis()).toString())).append(NEW_LINE);
        message.append("Укажите удобный для звонка интервал времени:");
        buttons.add(Button.init().setKey(TIME_10_14.getTitle()).setValue(TIME_10_14.getTitle()).build());
        buttons.add(Button.init().setKey(TIME_14_19.getTitle()).setValue(TIME_14_19.getTitle()).build());
        buttons.add(Button.init().setKey(BACK).setValue("Назад").build());
        val buttonsDescription = ButtonsDescription.init().setCountColumn(1).setButtons(buttons).build();
        stateService.setState(user, CALENDAR_WAIT_TIME);
        return createMessageList(user, message.toString(), buttonsDescription);
    }


    private List<PartialBotApiMethod> waitTimeLogic(User user, Update update) {
        val data = getInputCallback(update);
        if (data.equals(BACK)) {
            return freelogic(user, update);
        }
        val calendar = calendarService.getCalendar(user);
        val reception = new Reception();
        reception.setUser(user);
        reception.setServiceType(getServiceType());
        reception.setReceptionExportStatus(NEW_RECEPTION);
        reception.setReceptionDate(new java.sql.Date(calendar.getTimeInMillis()));
        reception.setReceptionRegistrationDate(new Timestamp(System.currentTimeMillis()));
        if (data.equals(TIME_10_14.getTitle())) {
            reception.setReceptionTime(TIME_10_14);
        }
        if (data.equals(TIME_14_19.getTitle())) {
            reception.setReceptionTime(TIME_14_19);
        }
        receptionRepository.save(reception);
        val clientMessage = new StringBuilder();
        clientMessage.append("Вы успешно записаны на бесплатную консультацию!").append(NEW_LINE)
                .append("*Услуга:* ").append(reception.getServiceType().getTitle()).append(NEW_LINE)
                .append("*Дата:* ").append(reception.getReceptionDate()).append(NEW_LINE)
                .append("*Время:* ").append(reception.getReceptionTime().getTitle()).append(NEW_LINE)
                .append("Менеджер свяжется с Вами в указанный временной интервал!").append(NEW_LINE)
        ;

        val adminMessage = new StringBuilder();
        adminMessage.append("Новая запись на консультацию:").append(NEW_LINE)
                .append("*Контакт:* ").append(NEW_LINE)
                .append(" -").append(prepareShield(user.getInputFio())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getInputCompany())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getInputPhone())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getUserName())).append(NEW_LINE)
                .append(" -").append(prepareShield(user.getFirstName())).append(SPACE).append(user.getLastName()).append(NEW_LINE)
                .append("*Услуга:* ").append(reception.getServiceType().getTitle()).append(NEW_LINE)
                .append("*Дата:* ").append(reception.getReceptionDate()).append(NEW_LINE)
                .append("*Время:* ").append(reception.getReceptionTime().getTitle()).append(NEW_LINE);

        val answer = new ArrayList<PartialBotApiMethod>();
        answer.add(createMessage(user, clientMessage.toString()));
        answer.addAll(createAdminMessages(adminMessage.toString()));
        stateService.setState(user, FREE);
        return answer;
    }

    public static InlineKeyboardMarkup createCalendar(Calendar calendar, Map<Integer, String> markedDays) {
        val days = new ArrayList<Day>();
        val day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        val dayOfWeekStart = dayOfWeek == 0 ? 7 : dayOfWeek;

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        val selectedMonth = calendar.getDisplayName(Calendar.MONTH, LONG_STANDALONE, new Locale("ru"));
        val year = calendar.get(Calendar.YEAR);

        calendar.add(Calendar.MONTH, 1);
        val nextMonth = calendar.getDisplayName(Calendar.MONTH, LONG_STANDALONE, new Locale("ru"));
        calendar.add(Calendar.MONTH, -2);
        val prevMonth = calendar.getDisplayName(Calendar.MONTH, LONG_STANDALONE, new Locale("ru"));
        calendar.add(Calendar.MONTH, 1);


        val currentDate = Calendar.getInstance();
        val currentMonth = currentDate.getDisplayName(Calendar.MONTH, LONG_STANDALONE, new Locale("ru"));
        val currentYear = currentDate.get(Calendar.YEAR);
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        for (int iDay = 1; iDay <= maxDay; iDay++) {
            val dayKey = String.valueOf(iDay);
            if (currentYear == year && currentMonth.equals(selectedMonth)) {
                if (iDay == currentDay) {
                    days.add(Day.init().setKey(dayKey).setValue(
                            dayKey + SPACE + EmojiParser.parseToUnicode(Emoji.ROBOT_FACE.getCode())).build());
                } else if (iDay < currentDay) {
                    days.add(Day.init().setKey(SPACE + dayKey).setValue(SPACE).build());
                } else {
                    val dayValue = markedDays.getOrDefault(iDay, dayKey);
                    days.add(Day.init().setKey(dayKey).setValue(dayValue).build());
                }
            } else {
                val dayValue = markedDays.getOrDefault(iDay, dayKey);
                days.add(Day.init().setKey(dayKey).setValue(dayValue).build());
            }
        }

        return createCalendar(CalendarDescription.init()
                .setSelectedMonth(selectedMonth + " " + year)
                .setPrevMonth("<< " + prevMonth)
                .setNextMonth(nextMonth + " >>")
                .setDayOfWeekStart(dayOfWeekStart)
                .setDays(days)
                .build());
    }

    private static InlineKeyboardMarkup createCalendar(CalendarDescription calendarDescription) {
        val inlineKeyboardMarkup = new InlineKeyboardMarkup();
        val rows = new ArrayList<List<InlineKeyboardButton>>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(createButton(SELECTED_MONTH, calendarDescription.getSelectedMonth()));
        rows.add(rowInline);

        rowInline = new ArrayList<>();
        rowInline.add(createButton("Monday", "Пн"));
        rowInline.add(createButton("Tuesday", "Вт"));
        rowInline.add(createButton("Wednesday", "Ср"));
        rowInline.add(createButton("Thursday", "Чт"));
        rowInline.add(createButton("Friday", "Пт"));
        rowInline.add(createButton("Saturday", "Сб"));
        rowInline.add(createButton("Sunday", "Вс"));
        rows.add(rowInline);

        rowInline = new ArrayList<>();
        int dayCounter = 1;
        val days = calendarDescription.getDays();

        while (dayCounter < calendarDescription.getDayOfWeekStart()) {
            rowInline.add(createButton(EMPTY_DAY + dayCounter, SPACE));
            ++dayCounter;
        }

        for (Day day : days) {
            rowInline.add(createButton(day.getKey(), day.getValue()));
            if (dayCounter % 7 == 0) {
                rows.add(rowInline);
                rowInline = new ArrayList<>();
            }
            ++dayCounter;
        }

        while (dayCounter % 7 != 1) {
            rowInline.add(createButton(EMPTY_DAY + dayCounter, SPACE));
            ++dayCounter;
        }
        rows.add(rowInline);
        rowInline = new ArrayList<>();

        rowInline.add(createButton(PREV_MONTH, calendarDescription.getPrevMonth()));
        rowInline.add(createButton(NEXT_MONTH, calendarDescription.getNextMonth()));
        rows.add(rowInline);

        rowInline = new ArrayList<>();
        rowInline.add(createButton(MAIN_MENU, "Главное меню"));
        rows.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private static InlineKeyboardButton createButton(String key, String value) {
        return createButton(key, value, null);
    }

    private static InlineKeyboardButton createButton(String key, String value, String link) {
        val btn = new InlineKeyboardButton();
        btn.setCallbackData(key);
        btn.setText(value);
        if (link != null) {
            btn.setUrl(link);
        }
        return btn;
    }

    //=================================================================================================================
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

}
