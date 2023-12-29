import com.zeydie.telegrambot.exceptions.LanguageNotRegisteredException;
import com.zeydie.telegrambot.modules.language.impl.LanguageImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LanguageTest {
    @Test
    public void localize() {
        try {
            var language = new LanguageImpl();

            language.preInit();
            language.init();
            language.postInit();

            var string1 = language.localize("Message");
            var string2 = language.localize("translate.1");
            var string3 = language.localize("translate.welcome_");
            var string4 = language.localize("messages.select_language");

            Assertions.assertEquals(string1, "Message");
            Assertions.assertEquals(string2, "translate.1");
            Assertions.assertEquals(string3, "translate.welcome_");
            Assertions.assertEquals(string4, "Select a language");
        } catch (LanguageNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }
}
