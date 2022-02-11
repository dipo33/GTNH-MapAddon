package io.github.dipo33.gtmapaddon;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

    private static class Defaults {
        public static final String[] USERS = new String[]{"psuchtak", "DirtyFaced", "RainDrop_x"};
        public static final int[] COLORS = new int[]{0x0000FF, 0xFF0000, 0xFFFF00};

        public static final int UNRECOGNIZED_COLOR = 0x808080;
    }

    private static class Keys {
        public static final String USERS = "users";
        public static final String COLORS = "colors";

        public static final String UNRECOGNIZED_COLOR = "unrecognizedColor";
    }

    private static class Categories {
        public static final String GENERAL = "general";
    }

    private static Map<String, Integer> userColors;
    private static int unrecognizedColor = Defaults.UNRECOGNIZED_COLOR;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile, Reference.VERSION);
        configuration.setCategoryPropertyOrder(Categories.GENERAL, Stream.of(Keys.USERS, Keys.COLORS, Keys.UNRECOGNIZED_COLOR).collect(Collectors.toList()));
        configuration.load();

        final String[] users = configuration.get(Categories.GENERAL, Keys.USERS, Defaults.USERS, "List of users with assigned colors").getStringList();
        final int[] colors = configuration.get(Categories.GENERAL, Keys.COLORS, Defaults.COLORS, "List of colors assigned to the users").getIntList();
        unrecognizedColor = configuration.get(Categories.GENERAL, Keys.UNRECOGNIZED_COLOR, Defaults.UNRECOGNIZED_COLOR, "Color assigned to unrecognized users").getInt();

        userColors = new HashMap<>();
        if (users.length != colors.length)
            throw new RuntimeException(String.format("Config property %s has to be same sized as property %s", Keys.USERS, Keys.COLORS));

        for (int i = 0; i < users.length; i++) {
            userColors.put(users[i], colors[i]);
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static int getColorForUser(String userName) {
        return userColors.getOrDefault(userName, unrecognizedColor);
    }
}
