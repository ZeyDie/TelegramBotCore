import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.TelegramBotCore;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

public class FomartingText {
    private final long userId = 5099834947L;

    @Test
    public void sendMessageMarkdownV1() {
        final var botCore = TelegramBotCore.getInstance();
        final var mode = ParseMode.Markdown;

        botCore.launch();
        botCore.sendMessage(userId, mode.name());
        botCore.execute(
                new SendMessage(userId, "**test1** *test2* `test3` ~~test3~~")
                        .parseMode(ParseMode.Markdown)
        );
        botCore.stop();
    }

    @Test
    public void sendMessageMarkdownV2() {
        final var botCore = TelegramBotCore.getInstance();
        final var mode = ParseMode.MarkdownV2;

        botCore.launch();
        botCore.sendMessage(userId, mode.name());
        botCore.execute(
                new SendMessage(userId, "**test1** *test2* `test3` ~~test3~~")
                        .parseMode(ParseMode.MarkdownV2)
        );
        botCore.stop();
    }

    @Test
    public void sendMessageHTML() {
        final var botCore = TelegramBotCore.getInstance();
        final var mode = ParseMode.HTML;

        botCore.launch();
        botCore.sendMessage(userId, mode.name());
        botCore.execute(
                new SendMessage(userId, "<b>test1</b> <i>test2</i> <code>test3</code> <s>test3</s> <u>test4</u> <pre language=\"c++\">code</pre>")
                        .parseMode(ParseMode.HTML)
        );
        botCore.stop();
    }
}