package net.pulsir.blackMarket.utils.color;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Color {

    public static Component translate(String s) {
        if (s.trim().contains("&")) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
        } else if (s.trim().startsWith("§")) {
            return LegacyComponentSerializer.legacySection().deserialize(s);
        } else {
            return MiniMessage.miniMessage().deserialize(s);
        }
    }
}
