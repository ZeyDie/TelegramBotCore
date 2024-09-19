import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.TelegramBotCore;
import org.junit.jupiter.api.Test;

public class FomartingText {
    @Test
    public void sendMessageMarkdownV1() {
        final TelegramBotCore botCore = TelegramBotCore.getInstance();

        botCore.launch();
        botCore.execute(
                new SendMessage(5099834947L, "**test1** *test2* `test3` ~~test3~~")
                        .parseMode(ParseMode.Markdown)
        );
    }

    @Test
    public void sendMessageMarkdownV2() {
        final TelegramBotCore botCore = TelegramBotCore.getInstance();

        botCore.launch();
        botCore.execute(
                new SendMessage(5099834947L, "**test1** *test2* `test3` ~~test3~~")
                        .parseMode(ParseMode.MarkdownV2)
        );
    }

    @Test
    public void sendMessageHTML() {
        final TelegramBotCore botCore = TelegramBotCore.getInstance();

        botCore.launch();
        botCore.execute(
                new SendMessage(5099834947L, "<b>test1</b> <i>test2</i> <code>test3</code> <s>test3</s> <u>test4</u> <pre language=\"c++\">code</pre>")
                        .parseMode(ParseMode.HTML)
        );
    }
}