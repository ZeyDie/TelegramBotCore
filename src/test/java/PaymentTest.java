import com.google.common.collect.Lists;
import com.zeydie.telegrambot.api.modules.payment.IPayment;
import com.zeydie.telegrambot.api.modules.payment.data.PaymentData;
import com.zeydie.telegrambot.modules.payment.impl.PaymentImpl;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.nio.file.Files;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.PAYMENT_FOLDER_PATH;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentTest {
    private static final @NotNull IPayment payment = new PaymentImpl();
    private static final @NotNull PaymentData paymentData = new PaymentData(
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
        payment.preInit();
        payment.init();
        payment.postInit();
    }

    @SneakyThrows
    @Test
    @Order(0)
    public void initPayment() {
        payment.register(paymentData);
    }

    @SneakyThrows
    @Test
    @Order(1)
    public void checkPaymentFile() {
        boolean value = false;

        try (@NonNull val stream = Files.walk(PAYMENT_FOLDER_PATH)) {
            value = stream.anyMatch(path -> path.equals(PaymentImpl.getFileName(paymentData).toPath()));
        }

        Assertions.assertTrue(value);
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void deletePaymentFile() {
        boolean value = false;

        try (@NonNull val stream = Files.walk(PAYMENT_FOLDER_PATH)) {
            value = Files.walk(PAYMENT_FOLDER_PATH)
                    .anyMatch(path -> path.equals(PaymentImpl.getFileName(paymentData).toPath()) && path.toFile().delete());
        }

        Assertions.assertTrue(value);
    }
}