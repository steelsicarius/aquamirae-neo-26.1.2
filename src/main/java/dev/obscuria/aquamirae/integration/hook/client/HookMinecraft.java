package dev.obscuria.aquamirae.integration.hook.client;

import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.client.CameraShake;
import dev.obscuria.aquamirae.client.FogSmoother;
import dev.obscuria.aquamirae.config.early.EarlyConfig;
import dev.obscuria.aquamirae.registry.AquamiraeMusic;
import dev.obscuria.aquamirae.registry.AquamiraeSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;
import net.minecraft.sounds.Musics;

import java.util.Objects;
import java.util.function.Consumer;

public final class HookMinecraft {

    public static void onSetLevel(Minecraft minecraft) {
        FogSmoother.INSTANCE.reset();
    }

    public static void onRunTick(Minecraft minecraft) {
        CameraShake.tick(minecraft.getDeltaTracker().getGameTimeDeltaTicks() / 20.0f);
    }

    public static void supplyMenuMusic(Minecraft minecraft, Music current, Consumer<Music> consumer) {
        if (!EarlyConfig.THEMED_MAIN_MENU.get()) return;
        if (!Objects.equals(current, Musics.MENU)) return;
        if (!AquamiraeSounds.MUSIC_TITLE_SCREEN_THEME.holder().isBound()) return;
        consumer.accept(AquamiraeMusic.TITLE_SCREEN_THEME.get());
    }

    /**
     * MC 26.1.2: situational music resolves through the environment attribute
     * BACKGROUND_MUSIC, so this hook returns the override (or null) instead of
     * consuming a callback after the biome lookup.
     */
    public static @Nullable Music gameplayMusic(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null) return null;
        if (!AquamiraeHelper.isInIceMaze(minecraft.player)) return null;
        return minecraft.level.isRaining()
                ? AquamiraeMusic.BLIZZARD_THEME.get()
                : AquamiraeMusic.ICE_MAZE_THEME.get();
    }

    public static void supplyBossMusic(Minecraft minecraft, Consumer<Music> consumer) {
        for (var event : minecraft.gui.getBossOverlay().events.values()) {
            if (!(event.getName().getContents() instanceof TranslatableContents contents)) continue;
            if (!contents.getKey().equals("entity.aquamirae.captain_cornelia")) continue;
            consumer.accept(AquamiraeMusic.FORSAKEN_DROWNAGE.get());
        }
    }
}
