package com.roseisproot.plushmania.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DISABLE_BACK_ITEM_DISPLAY = BUILDER.define("disable_back_item_display", false);


    public static final ModConfigSpec SPEC = BUILDER.build();
}
