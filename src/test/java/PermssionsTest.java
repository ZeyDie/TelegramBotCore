import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.api.modules.permissions.data.PermissionData;
import lombok.extern.java.Log;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PermssionsTest {
    private static long userId = new Random().nextLong();
    private static String permissionName = "test";

    private static IPermissions permissions;

    @BeforeAll
    public static void init() {
        permissions = TelegramBotCore.getInstance().getPermissions();
    }

    @Test
    @Order(1)
    public static void userIsNotExist() {
        Assertions.assertNull(permissions.getPermissionData(userId));
    }

    @Test
    @Order(2)
    public static void addPermission() {
        permissions.addPermission(userId, permissionName);

        Assertions.assertTrue(permissions.hasPermission(userId, permissionName));
    }

    @Test
    @Order(3)
    public static void userIsExist() {
        Assertions.assertNotNull(permissions.getPermissionData(userId));
    }

    @Test
    @Order(4)
    public static void removePermission() {
        permissions.removePermission(userId, permissionName);

        Assertions.assertFalse(permissions.hasPermission(userId, permissionName));
    }

    @Test
    @Order(5)
    public static void addPermissions() {
        permissions.addPermission(userId, permissionName + 1);
        permissions.addPermission(userId, permissionName + 2);
        permissions.addPermission(userId, permissionName + 3);

        Assertions.assertTrue(permissions.hasPermission(userId, permissionName + 1));
        Assertions.assertTrue(permissions.hasPermission(userId, permissionName + 2));
        Assertions.assertTrue(permissions.hasPermission(userId, permissionName + 3));
    }

    @Test
    @Order(6)
    public static void removePermissions() {
        permissions.removePermission(userId, permissionName + 1);
        permissions.removePermission(userId, permissionName + 2);
        permissions.removePermission(userId, permissionName + 3);

        Assertions.assertFalse(permissions.hasPermission(userId, permissionName + 1));
        Assertions.assertFalse(permissions.hasPermission(userId, permissionName + 2));
        Assertions.assertFalse(permissions.hasPermission(userId, permissionName + 3));
    }

    @Test
    @Order(7)
    public static void addPermissionsRandom() {
        for (int i = 0; i < new Random().nextInt(5); i++)
            permissions.addPermission(userId, RandomStringUtils.randomAlphabetic(10));
    }

    @Test
    @Order(8)
    public static void removePermissionsRandom() {
        permissions.removePermissions(userId);

        Assertions.assertNull(permissions.getPermissionData(userId));
    }
}