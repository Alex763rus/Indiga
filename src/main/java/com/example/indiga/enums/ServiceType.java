package com.example.indiga.enums;

public enum ServiceType {

    //3Д моделирование:
    SERVICE_1("Дизайн оборудования"),
    SERVICE_2("Видео анимирование"),
    SERVICE_3("Карточка товара на сайте"),

    //Редизайн сайта:
    SERVICE_4("Улучшенная навигация сайта"),
    SERVICE_5("Продающий дизайн"),
    SERVICE_6("Высокая скорость загрузки"),
    SERVICE_7("Продающие тексты"),

    //Лидогенерация:
    SERVICE_8("Чат боты"),
    SERVICE_9("Автоворонка и прогревы"),
    SERVICE_10("Контекстная реклама"),
    SERVICE_11("Закуп рекламы"),

    //Сайты под ключ:
    SERVICE_12("Решение для стартапа"),
    SERVICE_13("Масштабирования бизнеса"),
    SERVICE_14("Новый канал для продаж"),

    //Маркетинг под ключ:
    SERVICE_15("Экономия ресурсов компании"),
    SERVICE_16("Ваш фокус на задачах бизнеса"),
    SERVICE_17("Гибкость при выполнении задач"),
    SERVICE_18("Знания экспертов")
    ;

    private String title;

    ServiceType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
