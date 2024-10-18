import com.google.common.collect.Lists;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.payment.data.PaymentData;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.configs.data.BotConfig;
import com.zeydie.telegrambot.modules.payment.impl.PaymentImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.nio.file.Files;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.PAYMENT_FOLDER_PATH;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentTest {
    private static final TelegramBotCore bot = TelegramBotCore.getInstance();
    private static BotConfig.TestConfig testConfig;

    private static PaymentData paymentData = new PaymentData(
            "BTS",
            "BTS Coin",
            "BTS",
            "←",
            ",",
            ".",
            false,
            true,
            false,
            2.0,
            1.0,
            Double.MAX_VALUE,
            Lists.newArrayList()
    );

    @BeforeAll
    public static void init() {
        bot.launch();

        testConfig = ConfigStore.getBotConfig().getTestConfig();
    }

    @SneakyThrows
    @Test
    @Order(0)
    public void initPayment() {
        bot.getPayment().register(paymentData);
    }

    @SneakyThrows
    @Test
    @Order(1)
    public void checkPaymentFile() {
        Assertions.assertTrue(
                Files.walk(PAYMENT_FOLDER_PATH)
                        .anyMatch(path -> path.equals(PaymentImpl.getFileName(paymentData).toPath()))
        );
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void deletePaymentFile() {
        Assertions.assertTrue(
                Files.walk(PAYMENT_FOLDER_PATH)
                        .anyMatch(path -> path.equals(PaymentImpl.getFileName(paymentData).toPath()) ? path.toFile().delete() : false)
        );
    }
}