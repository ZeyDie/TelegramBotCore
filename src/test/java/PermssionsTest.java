import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.modules.permissions.impl.UserPermissionsImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.Random;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PermssionsTest {
    private static final long userId = new Random().nextLong();
    private static final String permissionName = "test";

    private static IPermissions permissions;

    @BeforeAll
    public static void init() {
        permissions = new UserPermissionsImpl();

        permissions.preInit();
        permissions.init();
        permissions.postInit();
    }

    @Test
    @Order(1)
    public void userIsNotExist() {
        Assertions.assertNull(permissions.getPermissionData(userId));
    }

    @Test
    @Order(2)
    public void addPermission() {
        permissions.addPermission(userId, permissionName);

        Assertions.assertTrue(permissions.hasPermission(userId, permissionName));
    }

    @Test
    @Order(3)
    public void userIsExist() {
        Assertions.assertNotNull(permissions.getPermissionData(userId));
    }

    @Test
    @Order(4)
    public void removePermission() {
        permissions.removePermission(userId, permissionName);

        Assertions.assertFalse(permissions.hasPermission(userId, permissionName));
    }

    @Test
    @Order(5)
    public void addPermissions() {
        permissions.addPermission(userId, permissionName + 1);
        permissions.addPermission(userId, permissionName + 2);
        permissions.addPermission(userId, permissionName + 3);

        Assertions.assertTrue(permissions.hasPermission(userId, permissionName + 1));
        Assertions.assertTrue(permissions.hasPermission(userId, permissionName + 2));
        Assertions.assertTrue(permissions.hasPermission(userId, permissionName + 3));
    }

    @Test
    @Order(6)
    public void removePermissions() {
        permissions.removePermission(userId, permissionName + 1);
        permissions.removePermission(userId, permissionName + 2);
        permissions.removePermission(userId, permissionName + 3);

        Assertions.assertFalse(permissions.hasPermission(userId, permissionName + 1));
        Assertions.assertFalse(permissions.hasPermission(userId, permissionName + 2));
        Assertions.assertFalse(permissions.hasPermission(userId, permissionName + 3));
    }

    @Test
    @Order(7)
    public void addPermissionsRandom() {
        for (int i = 0; i < new Random().nextInt(5); i++)
            permissions.addPermission(userId, RandomStringUtils.secure().nextAlphabetic(10));
    }

    @Test
    @Order(8)
    public void removePermissionsRandom() {
        permissions.removePermissions(userId);

        Assertions.assertNull(permissions.getPermissionData(userId));
    }
}