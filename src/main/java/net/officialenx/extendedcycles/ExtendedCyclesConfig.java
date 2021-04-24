package net.officialenx.extendedcycles;

import java.io.File;
import java.util.List;

import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;

import net.fabricmc.loader.api.FabricLoader;

import static com.google.common.collect.ImmutableList.of;

public class ExtendedCyclesConfig extends Config {
    public static final ConfigItemGroup configGroup = new ConfigGroup();
    public static final List<ConfigItemGroup> configs = of(configGroup);

    public ExtendedCyclesConfig() {
        super(configs, new File(FabricLoader.getInstance().getConfigDir().toFile(), "extended_cycles.json"),
                "extendedcycles");
    }

    public static class ConfigGroup extends ConfigItemGroup {
        public static final ConfigItem<Integer> dayEntry = new ConfigItem<>("dayEntry", 6000,
                "Specific time in ticks when the extended day will start");
        public static final ConfigItem<Integer> dayMinutes = new ConfigItem<>("dayMinutes", 30,
                "Number of minutes to extend the day");
        public static final ConfigItem<Integer> nightEntry = new ConfigItem<>("nightEntry", 18000,
                "Specific time in ticks when the extended night will start");
        public static final ConfigItem<Integer> nightMinutes = new ConfigItem<>("nightMinutes", 10,
                "Number of minutes to extend the night");

        public ConfigGroup() {
            super(of(dayEntry, dayMinutes, nightEntry, nightMinutes), "config");
        }
    }
}
