package dev.obscuria.aquamirae.neoforge.client;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.EchoCompassAngle;
import dev.obscuria.aquamirae.client.FogEvents;
import dev.obscuria.aquamirae.client.FogSmoother;
import dev.obscuria.aquamirae.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.Nullable;

/**
 * MC 26.1.2: the old MixinFogRenderer hooked FogRenderer.setupColor/setupFog,
 * which no longer exist. NeoForge now offers ViewportEvent.ComputeFogColor and
 * ViewportEvent.RenderFog for the same purpose, so the fog port lives on the
 * game event bus instead of mixins. The range select item model property
 * registration replaces the removed Fragmentum registerItemProperty API.
 */
public final class NeoClientEvents {

    private NeoClientEvents() {}

    /** Game (NeoForge) bus events. Registered from NeoAquamiraeClient. */
    public static final class GameBus {

        @SubscribeEvent
        public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
            Camera camera = event.getCamera();
            ClientLevel level = Minecraft.getInstance().level;

            float vanillaR = event.getRed();
            float vanillaG = event.getGreen();
            float vanillaB = event.getBlue();

            @Nullable FogEvents.FogColorResult override = null;
            if (FogEvents.SET_COLOR != null) {
                override = FogEvents.SET_COLOR.override(camera, level, camera.getFluidInCamera(),
                        (float) event.getPartialTick(), 0,
                        vanillaR, vanillaG, vanillaB);
            }

            float dt = AquamiraeClient.partialTicks() / 20.0f;
            var smoothed = FogSmoother.INSTANCE.smoothColor(vanillaR, vanillaG, vanillaB, override, dt);
            event.setRed(smoothed.r);
            event.setGreen(smoothed.g);
            event.setBlue(smoothed.b);
        }

        @SubscribeEvent
        public static void onRenderFog(ViewportEvent.RenderFog event) {
            if (!ClientConfig.BLIZZARD_CUSTOM_FOG.get()) return;
            FogEvents.FogDistanceCallback cb = FogEvents.SET_DISTANCE;

            var fogData = event.getFogData();
            float vanillaStart = fogData.environmentalStart;
            float vanillaEnd = fogData.environmentalEnd;

            FogEvents.FogDistanceResult override = null;
            if (cb != null) {
                override = cb.override(
                        event.getCamera(),
                        event.getCamera().getFluidInCamera(),
                        aquamirae$toCommonFogMode(event.getEnvironment()),
                        event.getFarPlaneDistance(),
                        (float) event.getPartialTick(),
                        vanillaStart, vanillaEnd
                );
            }

            float dt = AquamiraeClient.partialTicks() / 20.0f;
            var smoothed = FogSmoother.INSTANCE.smoothDistance(vanillaStart, vanillaEnd, override, dt);
            fogData.environmentalStart = smoothed.start;
            fogData.environmentalEnd = smoothed.end;
        }

        /**
         * MC 26.1.2: FogMode went from (FOG_SKY, FOG_TERRAIN) to (NONE, WORLD);
         * a null environment means atmospheric (sky) fog.
         */
        private static FogEvents.FogMode aquamirae$toCommonFogMode(@Nullable net.minecraft.client.renderer.fog.environment.FogEnvironment environment) {
            return environment == null
                    ? FogEvents.FogMode.FOG_SKY
                    : FogEvents.FogMode.FOG_TERRAIN;
        }
    }

    /** Mod bus events. Registered from NeoAquamiraeClient via the injected mod IEventBus. */
    public static final class ModBus {

        @SubscribeEvent
        public static void onRegisterRangeSelectItemModelProperty(RegisterRangeSelectItemModelPropertyEvent event) {
            event.register(Aquamirae.id("echo_compass/angle"), EchoCompassAngle.MAP_CODEC);
        }
    }
}
