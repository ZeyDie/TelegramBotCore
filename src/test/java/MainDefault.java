import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
import com.zeydie.telegrambot.api.modules.keyboard.IKeyboard;
import com.zeydie.telegrambot.api.modules.keyboard.impl.UserKeyboardImpl;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;

import java.util.function.Consumer;

public class MainDefault {
    public static void main(String[] args) {
        TelegramBotApp.setup(BotFileConfig.getJson());
        TelegramBotApp.init();

        final long chatId = 5099834947L;

        /*sendMessage.replyMarkup(
                new ReplyKeyboardMarkup(
                        new String[]{"first row button1", "first row button2"},
                        new String[]{"second row button1", "second row button2"})
                        .oneTimeKeyboard(true)   // optional
                        .resizeKeyboard(true)    // optional
                        .selective(true)        // optional
        );

        sendMessage.replyMarkup(
                new ReplyKeyboardMarkup(
                                new KeyboardButton("text"),
                                new KeyboardButton("contact").requestContact(true),
                                new KeyboardButton("location").requestLocation(true)
                )
        );

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("url").url("www.google.com"),
                new InlineKeyboardButton("callback_data").callbackData("callback_data")
        );
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query"));

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        TelegramBotApp.getTelegramBot().execute(sendMessage);*/

        /*new UserKeyboardImpl()
                .minimizeButtons(true)
                .addButton(new KeyboardButton("Сауна\uD83C\uDF41"))
                .addButton(new KeyboardButton("Баня\uD83E\uDDDC"))
                .completeRow()
                .addButton(new KeyboardButton("Посмотреть объявления на Avito\uD83D\uDCD1"))
                .sendKeyboard(chatId, "Информация");*/

        /*final IKeyboard userKeyboard = new UserKeyboardImpl().minimizeButtons(true);

        TelegramBotApp.getLanguage().getRegisteredLanguages().forEach(languageData -> userKeyboard.addButton(new KeyboardButton(languageData.getLabel())));

        userKeyboard.sendKeyboard(chatId, "Change language");*/
    }
}

// AnswerCallBackQuery - всплывающее окно
// InlineKeyboardButton - кнопка по горизонтале
// ReplyKeyoardMarkup - нопки вместо стандартной клавиатуры