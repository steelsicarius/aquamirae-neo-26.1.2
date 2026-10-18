package dev.obscuria.aquamirae.integration.hook.client;

import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class HookItemRenderer {

    /**
     * MC 26.1.2: ItemRenderer (and with it the old packedLight mixin target) is
     * gone; item quads flow through ItemStackRenderState.submit(lightCoords, ...).
     * The ItemStack is only known at model resolution time, so we capture the
     * NO_SHADING flag there (MixinItemModelResolver) and apply full bright light
     * at submit time (MixinItemStackRenderState).
     */
    private static final Set<ItemStackRenderState> NO_SHADING_STATES =
            Collections.newSetFromMap(new WeakHashMap<>());

    public static void captureNoShading(ItemStackRenderState state, ItemStack stack) {
        if (stack != null && stack.has(AquamiraeDataComponents.NO_SHADING)) {
            NO_SHADING_STATES.add(state);
        } else {
            NO_SHADING_STATES.remove(state);
        }
    }

    public static int modifyLight(ItemStackRenderState state, int lightCoords) {
        return NO_SHADING_STATES.contains(state) ? LightCoordsUtil.FULL_BRIGHT : lightCoords;
    }
}
