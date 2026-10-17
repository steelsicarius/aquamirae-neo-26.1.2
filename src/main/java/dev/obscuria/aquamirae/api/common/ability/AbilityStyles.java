package dev.obscuria.aquamirae.api.common.ability;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.resources.Identifier;

public enum AbilityStyles implements AbilityStyle {

    BLUE_GEM(Aquamirae.id("textures/gui/ability/gem_blue.png")),
    GREEN_GEM(Aquamirae.id("textures/gui/ability/gem_green.png")),
    PURPLE_GEM(Aquamirae.id("textures/gui/ability/gem_purple.png")),
    RED_GEM(Aquamirae.id("textures/gui/ability/gem_red.png"));

    private final Identifier texture;

    AbilityStyles(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public Identifier texture() {
        return texture;
    }
}
