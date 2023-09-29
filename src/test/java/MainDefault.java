import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.Application;
import com.zeydie.telegrambot.api.configs.BotFileConfig;

import java.nio.file.Paths;

public class MainDefault {
    public static void main(String[] args) {
        final SGsonFile sGsonFile = new SGsonFile(Paths.get(".idea", "bot.json"));
        final Application application = new Application(sGsonFile.fromJsonToObject(new BotFileConfig.Data()));
    }
}
