import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.configs.AbstractFileConfig;
import com.zeydie.telegrambot.api.configs.BotFileConfig;

public class MainDefault {
    public static void main(String[] args) {
        TelegramBotApp.setup(
                new SGsonFile(AbstractFileConfig.CONFIGS_SERVER_FOLDER.resolve("bot.json"))
                        .fromJsonToObject(new BotFileConfig.Data())
        );
        TelegramBotApp.init();
    }
}
