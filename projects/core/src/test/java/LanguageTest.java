import com.zeydie.telegrambot.core.impl.modules.language.LanguageImpl;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LanguageTest {
    @SneakyThrows
    @Test
    public void localize() {
        var language = new LanguageImpl();

        language.preInit();
        language.init();
        language.postInit();

        val string1 = language.localize("Message");
        val string2 = language.localize("translate.1");
        val string3 = language.localize("translate.welcome_");
        val string4 = language.localize("messages.select_language");

        Assertions.assertEquals(string1, "Message");
        Assertions.assertEquals(string2, "translate.1");
        Assertions.assertEquals(string3, "translate.welcome_");
        Assertions.assertEquals(string4, "Select a language");
    }
}