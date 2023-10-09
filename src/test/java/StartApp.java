import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
import com.zeydie.telegrambot.api.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.api.modules.keyboard.IMessageKeyboard;
import com.zeydie.telegrambot.api.modules.keyboard.impl.MessageKeyboardImpl;
import org.jetbrains.annotations.Nullable;


// AnswerCallBackQuery - всплывающее окно
// InlineKeyboardButton - кнопка по горизонтале
// ReplyKeyoardMarkup - нопки вместо стандартной клавиатуры

public class StartApp {
    public static void main(@Nullable final String[] args) throws LanguageRegisteredException {
        TelegramBotApp.setup(BotFileConfig.getJson());
        TelegramBotApp.init();

        final long chatId = 5099834947L;

        final IMessageKeyboard messageKeyboard = new MessageKeyboardImpl();

        TelegramBotApp.getLanguage()
                .getRegisteredLanguages()
                .forEach(
                        languageData -> messageKeyboard.addButton(
                                new InlineKeyboardButton(languageData.label())
                                        .callbackData(languageData.uniqueId())
                        )
                );

        messageKeyboard.sendKeyboard(chatId, "Change language");
    }
}
