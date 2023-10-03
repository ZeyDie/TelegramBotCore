import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.configs.AbstractFileConfig;
import com.zeydie.telegrambot.api.configs.BotChatFileConfig;
import com.zeydie.telegrambot.api.configs.BotFileConfig;

public class MainDefault {
    public static void main(String[] args) {
        TelegramBotApp.setup(BotFileConfig.getJson());
        TelegramBotApp.init();

        final SendMessage sendMessage = new SendMessage("5099834947", "keyboard");

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
        );*/

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("url").url("www.google.com"),
                new InlineKeyboardButton("callback_data").callbackData("callback_data")
        );
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query"));

        sendMessage.replyMarkup(inlineKeyboardMarkup);


        TelegramBotApp.getTelegramBot().execute(sendMessage);
    }
}

// AnswerCallBackQuery - всплывающее окно
// InlineKeyboardButton - кнопка по горизонтале
// ReplyKeyoardMarkup - нопки вместо стандартной клавиатуры