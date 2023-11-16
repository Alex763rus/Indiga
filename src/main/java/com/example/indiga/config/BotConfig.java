package com.example.indiga.config;

import com.example.indiga.enums.UserRole;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.indiga.constant.Constant.Command.*;
import static com.example.indiga.enums.UserRole.*;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_DEFAULT;
import static org.example.tgcommons.constant.Constant.Command.COMMAND_START;
import static org.example.tgcommons.constant.Constant.ConfigParams.*;

@Configuration
@EnableScheduling
@Getter
@PropertySource(PROPERTY_SOURCE)
public class BotConfig {

    @Value(BOT_VERSION)
    String botVersion;

    @Value(BOT_USERNAME)
    String botUserName;

    @Value(BOT_TOKEN)
    String botToken;

    @Value("${service.file_info.uri}")
    String fileInfoUri;

    @Value("${service.file_storage.uri}")
    String fileStorageUri;

    @Value("${input.file.path}")
    String inputFilePath;

    @Value("${input.file.possibilities.path}")
    String inputFilePossibilitiesPath;

    @Bean
    public Map<UserRole, List<String>> roleAccess() {
        val roleAccess = new HashMap<UserRole, List<String>>();
        roleAccess.put(BLOCKED, List.of(COMMAND_DEFAULT, COMMAND_START));
        roleAccess.put(UNREGISTERED, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_REGISTER));
        roleAccess.put(EMPLOYEE, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_FAQ, COMMAND_FEED_BACK, COMMAND_DISCOUNT,COMMAND_POSSIBILITIES,
                COMMAND_SERVICE_1, COMMAND_SERVICE_2, COMMAND_SERVICE_3, COMMAND_SERVICE_4, COMMAND_SERVICE_5, COMMAND_SERVICE_6, COMMAND_SERVICE_7, COMMAND_SERVICE_8, COMMAND_SERVICE_9, COMMAND_SERVICE_10, COMMAND_SERVICE_11, COMMAND_SERVICE_12, COMMAND_SERVICE_13, COMMAND_SERVICE_14, COMMAND_SERVICE_15, COMMAND_SERVICE_16, COMMAND_SERVICE_17, COMMAND_SERVICE_18)
        );
        roleAccess.put(MANAGER, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_FAQ, COMMAND_FEED_BACK, COMMAND_DISCOUNT,COMMAND_POSSIBILITIES,
                COMMAND_SERVICE_1, COMMAND_SERVICE_2, COMMAND_SERVICE_3, COMMAND_SERVICE_4, COMMAND_SERVICE_5, COMMAND_SERVICE_6, COMMAND_SERVICE_7, COMMAND_SERVICE_8, COMMAND_SERVICE_9, COMMAND_SERVICE_10, COMMAND_SERVICE_11, COMMAND_SERVICE_12, COMMAND_SERVICE_13, COMMAND_SERVICE_14, COMMAND_SERVICE_15, COMMAND_SERVICE_16, COMMAND_SERVICE_17, COMMAND_SERVICE_18
                , COMMAND_SEND_TO_CONTACTS)
        );
        roleAccess.put(ADMIN, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_FAQ, COMMAND_FEED_BACK, COMMAND_DISCOUNT,COMMAND_POSSIBILITIES,
                COMMAND_SERVICE_1, COMMAND_SERVICE_2, COMMAND_SERVICE_3, COMMAND_SERVICE_4, COMMAND_SERVICE_5, COMMAND_SERVICE_6, COMMAND_SERVICE_7, COMMAND_SERVICE_8, COMMAND_SERVICE_9, COMMAND_SERVICE_10, COMMAND_SERVICE_11, COMMAND_SERVICE_12, COMMAND_SERVICE_13, COMMAND_SERVICE_14, COMMAND_SERVICE_15, COMMAND_SERVICE_16, COMMAND_SERVICE_17, COMMAND_SERVICE_18
                , COMMAND_EXPORT_NEW_RECEPTION, COMMAND_EXPORT_ALL_RECEPTION, COMMAND_DISCOUNT_SETUP, COMMAND_EXPORT_ALL_USERS, COMMAND_SEND_TO_CONTACTS)
        );
        return roleAccess;
    }

}
