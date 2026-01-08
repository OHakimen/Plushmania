package com.roseisproot.plushmania.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class CommonConfig {
    static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue GUILLOTINE_BEHEADING_CHANCE = BUILDER
            .comment("The percentage chance to drop a head from mobs that have a skull or head associated (excludes ender dragon and non vanilla heads)")
            .defineInRange("guillotine_beheading_chance", 0.05, 0, 1);

    public static final ModConfigSpec.DoubleValue SOGGINESS_THRESHOLD = BUILDER
            .comment("The percentage of sog to be actually considered 'soggy'")
            .defineInRange("sogginess_threshold", 0.05, 0, 1);

    public static final ModConfigSpec.IntValue TICKS_BETWEEN_HEALING_FROM_SPOOL_OF_THREAD = BUILDER
            .comment("The amount of time (in ticks) for a charge of a spool be consumed as a healing")
            .defineInRange("ticks_between_healing_from_spool_of_thread", 40, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> BANNED_MOBS_FOR_GUILLOTINE = BUILDER
            .comment("List of mobs that are disallowed to drop heads from guillotine")
            .defineListAllowEmpty("banned_mobs_for_guillotine", new ArrayList<String>(), o -> ResourceLocation.tryParse(o.toString()) != null);


    public static final ModConfigSpec SPEC = BUILDER.build();
}
