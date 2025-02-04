# TelegramBotCore

![GitHub top language](https://img.shields.io/github/languages/top/ZeyDie/TelegramBotCore)
![GitHub](https://img.shields.io/github/license/ZeyDie/TelegramBotCore)
![GitHub Repo stars](https://img.shields.io/github/stars/ZeyDie/TelegramBotCore)
![GitHub issues](https://img.shields.io/github/issues/ZeyDie/TelegramBotCore)

Java библиотека, основанная на [Java Telegram Bot Api by Pengrad](https://github.com/pengrad/java-telegram-bot-api)
Основные преимущества:
- Event система;
- Мультиязычность;
- Кеширование сообщений пользователей (TODO: шифрование сообщений);
- Быстрое создание клавиатур, кнопок и их реализация;
- Поддержка отправки счета пользователю (по умолчанию счет в Звездах Telegram XTR);
- Стилизация сообщений HTML, Markdown (v1, v2);
- Понятная лог система.

## Создание бота

```java
public final class ExampleBotTest implements ISubcore {
    @Getter
    private static final @NotNull ExampleBotTest instance = new ExampleBotTest();

    @Getter
    private static final @NotNull TelegramBotCore telegramBotCore = TelegramBotCore.getInstance();

    // Имя Telegram бота для логирования
    @Override
    public @NotNull String getName() {
        return this.getClass().getName();
    }

    // Главный метод запуска и регистрация в роли дополнения
    @SneakyThrows
    @Override
    public static void main(@Nullable final String[] args) {
        LoggerUtil.info("Starting {}...", ExampleBotTest.class.getName());

        LoggerUtil.info("Register subcore {}...", instance.getName());
        telegramBotCore.registerSubcore(instance);

        LoggerUtil.info("Launch {}...", telegramBotCore.getName());
        telegramBotCore.launch(args);
    }

    // Второстепенный запуск с сериализацией аргументов
    @Override
    public void launch(@Nullable final String[] strings) {
        
    }
    
    // Метод обработки остановки бота
    @Override
    public void stop() {

    }

    // Метод подготовки перед инициализацией модулей, сервисов
    @Override
    public void preInit() {

    }

    // Метод инициализации меодулей, сервисов
    @Override
    public void init() {

    }

    // Метод конечной инициализации модулей, сервисов
    @Override
    public void postInit() {

    }
}
```

## API Utils
- FileUtil - работа с файловой системой, создание папок, файлов, получение имени или типа файла.
- LanguageUtil - локализация ключей, исходя из языка пользователя; по умолчанию английский en.lang.
- LoggerUtil - система логирования на основе Log4j2.
- ReferencePaths - все используемые пути для реализаций файлов конфигураций, рекомендуется использовать для создания собственный конфигов в существующих директориях.
- RequestUtil - создание запросов, получение значений или запись параметров запроса.
- SendMessageUtil - отправка сообщения через различные параметры.

## API Events

- ConfigSubscribe - создание и регистрация конфига с помощью Gson;
- LanguageRegisterEvent - событие при регистрации языка;
- LanguageRegisterEventSubscribe - слушатель событий LanguageRegisterEvent;
- CancelableSubscribe - определение, что слушатель может быть отменен;
- ConfigSubscribesRegister - слушатель событий ConfigSubscribe;
- PrioritySubscribe - приоритезация слушателей.

```java
@EventSubscribesRegister
public final class ExampleEventSubscriber {
    // Callback Subscriber кнопки
    @CallbackQueryEventSubscribe(callbacks = "list.select", startWith = true)
    public void select(@NonNull final CallbackQueryEvent event) {
        @NonNull val datas = event.getCallbackQuery().data().split("\\.");
        val userId = Long.parseLong(datas[datas.length - 1]);
        @NonNull val selected = datas[datas.length - 2];
    }

    // Command Subscriber
    @CommandEventSubscribe(commands = "/test")
    public void test(@NonNull final CommandEvent event) {
        val chatId = event.getSender().id();
        @NonNull val text = event.getMessage().text();
    }

    // Message Subscriber
    @MessageEventSubscribe
    public void message(@NonNull final MessageEvent event) {
        val chatId = event.getSender().id();
        @NonNull val text = event.getMessage().text();
    }

    // Update Subscriber
    @UpdateEventSubscribe
    public void message(@NonNull final UpdateEvent event) {
        val chatId = event.getSender();
        @NonNull val update = event.getUpdate();
    }
}
```

## API Modules

- Cache - 2 способа кеширования сообщений: большими сообщениями или 1 сообщение = 1 файл; кеширование пользователей;
- Keyboard - интерфейсы реализации InlineKeyboardButton и KeyboardButton, создание пользовательской клавиатуры как в сообщений, так и в панели набора текста;
- Language - интерфейс регистрации, проверки и список мировых языков, а также локализация ключей;
- Payment - информация о мировых валютах с специфическими прописями;
- Permissions - система прав пользователей с доступом к командам, действиям с ботом и т.д.

```java
// Так же возможно установка собственных обработчиков 
// telegramBotCore.setLanguage(...); и т.д.
telegramBotCore.setMessagesCache(new IMessagesCache() {
    @Override
    public void put(@NonNull MessageData messageData) {
    }

    @Override
    public void save(){
    }

    @Override
    public void preInit() {
    }

    @Override 
    public void init() {
    }

    @Override
    public void postInit() {
    }
});
```

## Клавиатуры

Существует 2 типа клавиатур:
- MessageKeyboardImpl - кнопки внутри сообщения;
- MessageKeyboardImpl - кнопки в пользовательской клавиатуре.

```java
@CommandEventSubscribe(commands = "/google")
public void g00gle(final @NonNull CommandEvent event){
    MessageKeyboardImpl.create("Go to Google!")
        .addButton(new InlineKeyboardButton("G00gle").url("https://google.com"))
        .sendKeyboard(event.getSender());
}

@CommandEventSubscribe(commands = "/search")
public void search(final @NonNull CommandEvent event){
        UserKeyboardImpl.create("Select a search engine!")
        .addButton(new KeyboardButton("G00gle").url("https://google.com"))
        .sendKeyboard(event.getSender());
}
```

Возможно организовать меню сразу с готовой клавиатурой

```java
public final class SettingsKeyboard extends UserKeyboardImpl {
    @Getter
    private static final @NotNull SettingsKeyboard instance = new SettingsKeyboard();

    public SettingsKeyboard() {
        super("keyboard.settings");

        this.minimizeButtons(true);

        this.addButton(new ListButton());
        this.completeRow();
        this.addButton(new AddButton());
        this.addButton(new DeleteButton());
        this.completeRow();
        this.addButton(new MenuButton());
    }
}
```
```java
SettingsKeyboard.getInstance().sendKeyboard(userId);
```