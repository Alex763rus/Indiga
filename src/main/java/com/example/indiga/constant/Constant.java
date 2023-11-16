package com.example.indiga.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constant {

    public static final String APP_NAME = "Indiga";

    @NoArgsConstructor(access = PRIVATE)
    public static final class Command {

        public static final String COMMAND_FAQ = "/faq";

        public static final String COMMAND_REGISTER = "/register";
        public static final String COMMAND_DISCOUNT_SETUP = "/discount_setup";

        public static final String COMMAND_EXPORT_NEW_RECEPTION = "/export_new_reception";
        public static final String COMMAND_EXPORT_ALL_RECEPTION = "/export_all_reception";
        public static final String COMMAND_EXPORT_ALL_USERS = "/export_all_users";
        public static final String COMMAND_SEND_TO_CONTACTS = "/send_to_contacts";

        public static final String COMMAND_FEED_BACK = "/feed_back";
        public static final String COMMAND_DISCOUNT = "/discount";
        public static final String COMMAND_POSSIBILITIES = "/possibilities";

        public static final String COMMAND_SERVICE_1 = "/equipment_design";
        public static final String COMMAND_SERVICE_2 = "/video_animation";
        public static final String COMMAND_SERVICE_3 = "/product_on_website";
        public static final String COMMAND_SERVICE_4 = "/site_redesign";
        public static final String COMMAND_SERVICE_5 = "/selling_design";
        public static final String COMMAND_SERVICE_6 = "/high_download_speed";
        public static final String COMMAND_SERVICE_7 = "/selling_text";
        public static final String COMMAND_SERVICE_8 = "/chat_bots";
        public static final String COMMAND_SERVICE_9 = "/funnel_warm_ups";
        public static final String COMMAND_SERVICE_10 = "/contextual_advertising";
        public static final String COMMAND_SERVICE_11 = "/advertising_purchase";
        public static final String COMMAND_SERVICE_12 = "/startup_solution";
        public static final String COMMAND_SERVICE_13 = "/business_scaling";
        public static final String COMMAND_SERVICE_14 = "/new_sales_channel";
        public static final String COMMAND_SERVICE_15 = "/company_resources";
        public static final String COMMAND_SERVICE_16 = "/business_tasks";
        public static final String COMMAND_SERVICE_17 = "/flexibility";
        public static final String COMMAND_SERVICE_18 = "/expert_knowledge";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class MenuChooseTime {

        public static final String BACK = "back";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class ConfigParams {
        public static final String INPUT_FILE_PATH = "${input.file.path}";
        public static final String INPUT_FILE_PHOTO_PATH = "${input.file.photo.path}";
        public static final String INPUT_FILE_OFFER_PATH = "${input.file.offer.path}";

    }

}
