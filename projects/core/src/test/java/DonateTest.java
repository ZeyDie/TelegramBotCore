import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.SendInvoice;
import com.zeydie.telegrambot.core.TelegramBotCore;
import com.zeydie.telegrambot.core.configs.ConfigStore;
import com.zeydie.telegrambot.core.configs.data.BotConfig;
import com.zeydie.telegrambot.core.utils.LanguageUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DonateTest {
    private static final TelegramBotCore bot = TelegramBotCore.getInstance();

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
        @NonNull val donateConfig = ConfigStore.getDonateConfig();

        val chatId = testConfig.getChatId();

        @NonNull val xtrPrice = new LabeledPrice(
                LanguageUtil.localize(chatId, "messages.donate.title"),
                donateConfig.getAmount()
        );
        @NonNull val invoice = new SendInvoice(
                chatId,
                LanguageUtil.localize(chatId, "messages.donate.title"),
                LanguageUtil.localize(chatId, "messages.donate.description"),
                donateConfig.getPayload(),
                donateConfig.getCurrency(),
                xtrPrice
        );

        bot.execute(invoice);
    }
}