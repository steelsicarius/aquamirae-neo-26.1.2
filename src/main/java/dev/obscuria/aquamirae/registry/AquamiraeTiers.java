package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.Locale;

/**
 * MC 26.1.2: the old {@code Tier} interface is gone; tool data is now carried by
 * {@link ToolMaterial}. Each tier keeps its custom durability (uses) and the
 * mining properties of its vanilla base material. Repair ingredients are now
 * tag-driven: {@code data/aquamirae/tags/item/repairs/<tier>.json} must list the
 * repair items (see port report).
 */
public enum AquamiraeTiers {

    REMNANT_SABER(ToolMaterial.STONE, 250),
    TERRIBLE_BLADE(ToolMaterial.IRON, 750),
    TERRIBLE_FANG(ToolMaterial.IRON, 750),
    TERRIBLE_CLEAVER(ToolMaterial.IRON, 750),
    DAGGER_OF_GREED(ToolMaterial.DIAMOND, 0),
    SHATTERBLADE(ToolMaterial.DIAMOND, 2200),
    SHATTERSPIKE(ToolMaterial.DIAMOND, 1400),
    TIDEPIERCER(ToolMaterial.NETHERITE, 4400),
    JAWBREAKER(ToolMaterial.NETHERITE, 4400),
    TEMPEST_TRIDENT(ToolMaterial.NETHERITE, 4400),
    DREADWAKE(ToolMaterial.NETHERITE, 4400);

    private final ToolMaterial baseMaterial;
    private final int uses;

    AquamiraeTiers(ToolMaterial baseMaterial, int uses) {
        this.baseMaterial = baseMaterial;
        this.uses = uses;
    }

    public int getUses() {
        return this.uses;
    }

    public ToolMaterial baseMaterial() {
        return this.baseMaterial;
    }

    /**
     * The material used by {@code Item.Properties#sword}. Attack damage bonus
     * stays 0 (matching the old {@code Tier#getAttackDamageBonus} override);
     * repair items come from a per-tier item tag.
     */
    public ToolMaterial material() {
        return new ToolMaterial(
                this.baseMaterial.incorrectBlocksForDrops(),
                this.uses,
                this.baseMaterial.speed(),
                0.0F,
                this.baseMaterial.enchantmentValue(),
                repairItems());
    }

    /**
     * Unbound tag keys match nothing, which faithfully reproduces the old
     * {@code Ingredient.EMPTY} ("cannot be repaired") case; tiers with actual
     * repair items get their entries from datapack tag files.
     */
    public TagKey<Item> repairItems() {
        return TagKey.create(Registries.ITEM, Aquamirae.id("repairs/" + name().toLowerCase(Locale.ROOT)));
    }
}
