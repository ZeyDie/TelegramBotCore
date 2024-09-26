import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.SendInvoice;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.configs.data.BotConfig;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DonateTest {
    private static TelegramBotCore bot = new TelegramBotCore();
    private static BotConfig.TestConfig testConfig;

    @BeforeAll
    public static void init() {
        bot.launch();

        testConfig = ConfigStore.getBotConfig().getTestConfig();
    }

    @SneakyThrows
    @Test
    @Order(0)
    public void sendDonateButton() {
        @NonNull val language = bot.getLanguage();

        @NonNull val donateConfig = ConfigStore.getDonateConfig();

        @NonNull val xtrPrice = new LabeledPrice(
                language.localize("messages.donate.title"),
                donateConfig.getAmount()
        );
        @NonNull val invoice = new SendInvoice(
                testConfig.getChatId(),
                language.localize("messages.donate.title"),
                language.localize("messages.donate.description"),
                donateConfig.getPayload(),
                donateConfig.getCurrency(),
                xtrPrice
        );

        bot.execute(invoice);
    }
}