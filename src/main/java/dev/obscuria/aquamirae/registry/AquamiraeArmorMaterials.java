package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * MC 26.1.2: the ArmorMaterial registry is gone; {@link ArmorMaterial} is a
 * plain record value now. These are direct constants mirroring their vanilla
 * base materials (defense, durability, enchantability, sounds, repair items),
 * with a mod-specific equipment asset id used as fallback for vanilla-style
 * rendering (GeckoLib renderers take precedence).
 */
public final class AquamiraeArmorMaterials {

    public static final ArmorMaterial TERRIBLE = register("terrible", ArmorMaterials.IRON);
    public static final ArmorMaterial SALVAGER_ANCIENT = register("salvager_ancient", ArmorMaterials.NETHERITE);
    public static final ArmorMaterial SALVAGER_MODERN = register("salvager_modern", ArmorMaterials.GOLD);
    public static final ArmorMaterial ABYSSAL_LIGHT = register("abyssal_light", ArmorMaterials.DIAMOND);
    public static final ArmorMaterial ABYSSAL_HEAVY = register("abyssal_heavy", ArmorMaterials.DIAMOND);

    private static ArmorMaterial register(String name, ArmorMaterial base) {
        return new ArmorMaterial(
                base.durability(),
                base.defense(),
                base.enchantmentValue(),
                base.equipSound(),
                base.toughness(),
                base.knockbackResistance(),
                base.repairIngredient(),
                ResourceKey.create(EquipmentAssets.ROOT_ID, Aquamirae.id(name)));
    }

    static void init() {}
}
