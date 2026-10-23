package dev.obscuria.aquamirae.api.common;

import dev.obscuria.fragmentum.v2.api.common.TooltipStack;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

/**
 * Concrete grouped tooltip component for items that provide several tooltip
 * images at once (e.g. an armor piece with an ability set and a keepsake).
 *
 * MC 26.1.2 / Fragmentum v4: the framework registers its own group renderer
 * against the {@code TooltipStack} *interface*, but its registry matches
 * tooltip components by *exact class*, so the internal {@code _TooltipStack}
 * implementation never matches and the game crashes with
 * "Unknown TooltipComponent". Registering this concrete class (see
 * {@code AquamiraeClient#init}) fixes that for the mod's grouped tooltips —
 * the renderer ({@code ClientTooltipStack}) accepts any
 * {@code TooltipStack} implementation.
 */
public record AquamiraeTooltipStack(List<TooltipComponent> components) implements TooltipStack {
}
