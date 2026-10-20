package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.api.common.ability.AbilitySetComponent;
import dev.obscuria.aquamirae.common.component.ItemTooltip;
import dev.obscuria.aquamirae.common.component.Keepsake;
import dev.obscuria.aquamirae.common.item.*;
import dev.obscuria.aquamirae.registry.references.AquamiraeJukeboxSongs;
import dev.obscuria.fragmentum.v2.api.common.registry.DeferredItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import java.util.function.Function;

public final class AquamiraeItems {

    public static final DeferredItem<Item> RUNE_OF_THE_STORM;
    public static final DeferredItem<Item> SHELL_HORN;
    public static final DeferredItem<Item> DEAD_SEA_SCROLL;
    public static final DeferredItem<Item> CRACKED_RECORD;
    public static final DeferredItem<Item> MUSIC_DISC_HORIZON;
    public static final DeferredItem<Item> MUSIC_DISC_FORSAKEN_DROWNAGE;

    public static final DeferredItem<Item> GOLDEN_MOTH_SPAWN_EGG;
    public static final DeferredItem<Item> SPINEFISH_SPAWN_EGG;
    public static final DeferredItem<Item> ABYSSAL_SCYPHOID_SPAWN_EGG;
    public static final DeferredItem<Item> MAW_SPAWN_EGG;
    public static final DeferredItem<Item> ANGLERFISH_SPAWN_EGG;
    public static final DeferredItem<Item> MAZE_MOTHER_SPAWN_EGG;
    public static final DeferredItem<Item> CAPTAIN_CORNELIA_SPAWN_EGG;
    public static final DeferredItem<Item> EEL_SPAWN_EGG;
    public static final DeferredItem<Item> TORTURED_SOUL_SPAWN_EGG;
    public static final DeferredItem<Item> SCUTTLER_SPAWN_EGG;
    public static final DeferredItem<Item> SHELLBACK_SPAWN_EGG;

    public static final DeferredItem<Item> TERRIBLE_UPGRADE_SMITHING_TEMPLATE;
    public static final DeferredItem<Item> ABYSSAL_UPGRADE_SMITHING_TEMPLATE;

    public static final DeferredItem<Item> TERRIBLE_HELMET;
    public static final DeferredItem<Item> TERRIBLE_CHESTPLATE;
    public static final DeferredItem<Item> TERRIBLE_LEGGINGS;
    public static final DeferredItem<Item> TERRIBLE_BOOTS;

    public static final DeferredItem<Item> SALVAGER_HELMET;
    public static final DeferredItem<Item> SALVAGER_SUIT;
    public static final DeferredItem<Item> SALVAGER_WADERS;
    public static final DeferredItem<Item> SALVAGER_BOOTS;

    public static final DeferredItem<Item> ABYSSAL_TIARA;
    public static final DeferredItem<Item> ABYSSAL_HEAUME;
    public static final DeferredItem<Item> ABYSSAL_BRIGANDINE;
    public static final DeferredItem<Item> ABYSSAL_LEGGINGS;
    public static final DeferredItem<Item> ABYSSAL_BOOTS;

    public static final DeferredItem<Item> REMNANT_SABER;
    public static final DeferredItem<Item> TERRIBLE_CLEAVER;
    public static final DeferredItem<Item> TERRIBLE_BLADE;
    public static final DeferredItem<Item> TERRIBLE_FANG;
    public static final DeferredItem<Item> DAGGER_OF_GREED;
    public static final DeferredItem<Item> SHATTERBLADE;
    public static final DeferredItem<Item> SHATTERSPIKE;
    public static final DeferredItem<Item> TIDEPIERCER;
    public static final DeferredItem<Item> JAWBREAKER;
    public static final DeferredItem<Item> TEMPEST_TRIDENT;
    public static final DeferredItem<Item> DREADWAKE;

    public static final DeferredItem<Item> TERRIBLE_CHAKRAM;
    public static final DeferredItem<Item> MAZE_ROSE;

    public static final DeferredItem<Item> ECHO_COMPASS;
    public static final DeferredItem<Item> LOGBOOK;

    public static final DeferredItem<Item> SHIP_GRAVEYARD_ECHO;
    public static final DeferredItem<Item> WISTERIA_NIVEIS;
    public static final DeferredItem<Item> OXYHELIUM;
    public static final DeferredItem<Item> MAELSTROM_VESTIGE;
    public static final DeferredItem<Item> ABYSSAL_AMETHYST;
    public static final DeferredItem<Item> ANGLER_FANG;
    public static final DeferredItem<Item> FROZEN_KEY;

    public static final DeferredItem<Item> PIRATE_POUCH;
    public static final DeferredItem<Item> TREASURE_POUCH;

    public static final DeferredItem<Item> SHARP_BONES;
    public static final DeferredItem<Item> FIN;
    public static final DeferredItem<Item> ESCA;
    public static final DeferredItem<Item> ESCA_SKEWER;
    public static final DeferredItem<Item> SPINEFISH;
    public static final DeferredItem<Item> COOKED_SPINEFISH;
    public static final DeferredItem<Item> JELLYFISH_JELLY;
    public static final DeferredItem<Item> SEA_CASSEROLE;
    public static final DeferredItem<Item> SEA_STEW;
    public static final DeferredItem<Item> POSEIDON_BREAKFAST;
    public static final DeferredItem<Item> VOID_SQUID;

    public static final DeferredItem<Item> FROZEN_CHEST;
    public static final DeferredItem<Item> GOLDEN_MOTH_JAR;
    public static final DeferredItem<Item> LUMINESCENT_LAMP;
    public static final DeferredItem<Item> LUMINESCENT_BUBBLE;
    public static final DeferredItem<Item> PAINTING_ANGLERFISH;
    public static final DeferredItem<Item> PAINTING_OXYHELIUM;
    public static final DeferredItem<Item> PAINTING_TORTURED_SOUL;
    public static final DeferredItem<Item> PAINTING_AURORA;

    static {
        RUNE_OF_THE_STORM = register("rune_of_the_storm", properties -> new Item(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.rune_of_the_storm"))));
        SHELL_HORN = register("shell_horn", properties -> new ShellHornItem(properties.stacksTo(1).rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.shell_horn"))));
        DEAD_SEA_SCROLL = register("dead_sea_scroll", properties -> new Item(properties));
        CRACKED_RECORD = register("cracked_record", properties -> new Item(properties.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AquamiraeJukeboxSongs.CRACKED_RECORD).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.cracked_record"))));
        MUSIC_DISC_HORIZON = register("music_disc_horizon", properties -> new Item(properties.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AquamiraeJukeboxSongs.HORIZON)));
        MUSIC_DISC_FORSAKEN_DROWNAGE = register("music_disc_forsaken_drownage", properties -> new Item(properties.stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(AquamiraeJukeboxSongs.FORSAKEN_DROWNAGE)));

        GOLDEN_MOTH_SPAWN_EGG = register("golden_moth_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.GOLDEN_MOTH, -3381760, -205, properties.rarity(Rarity.UNCOMMON)));
        SPINEFISH_SPAWN_EGG = register("spinefish_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.SPINEFISH, 1589067, 11451069, properties));
        ABYSSAL_SCYPHOID_SPAWN_EGG = register("abyssal_scyphoid_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.ABYSSAL_SCYPHOID, -39169, -10092442, properties));
        MAW_SPAWN_EGG = register("maw_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.MAW, -16764109, -16737895, properties));
        ANGLERFISH_SPAWN_EGG = register("anglerfish_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.ANGLERFISH, -12963033, -4942746, properties));
        MAZE_MOTHER_SPAWN_EGG = register("maze_mother_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.MAZE_MOTHER, -10092442, -39169, properties));
        CAPTAIN_CORNELIA_SPAWN_EGG = register("captain_cornelia_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.CAPTAIN_CORNELIA, -7760229, -13355980, properties.rarity(Rarity.EPIC)));
        EEL_SPAWN_EGG = register("eel_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.EEL, -16764109, -16737844, properties));
        TORTURED_SOUL_SPAWN_EGG = register("tortured_soul_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.TORTURED_SOUL, -13421773, -16724737, properties));
        SCUTTLER_SPAWN_EGG = register("scuttler_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.SCUTTLER, 1589067, -16737895, properties));
        SHELLBACK_SPAWN_EGG = register("shellback_spawn_egg", properties -> Aquamirae.SERVICE.newSpawnEggItem(AquamiraeEntityTypes.SHELLBACK, 1589067, -16737895, properties));

        TERRIBLE_UPGRADE_SMITHING_TEMPLATE = register("terrible_upgrade_smithing_template", properties -> AquamiraeSmithingTemplateItem.createTerribleUpgradeTemplate(properties));
        ABYSSAL_UPGRADE_SMITHING_TEMPLATE = register("abyssal_upgrade_smithing_template", properties -> AquamiraeSmithingTemplateItem.createAbyssalUpgradeTemplate(properties));

        TERRIBLE_HELMET = register("terrible_helmet", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorType.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));
        TERRIBLE_CHESTPLATE = register("terrible_chestplate", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorType.CHESTPLATE, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));
        TERRIBLE_LEGGINGS = register("terrible_leggings", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorType.LEGGINGS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));
        TERRIBLE_BOOTS = register("terrible_boots", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.TERRIBLE, ArmorType.BOOTS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.15), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_ARMOR))));

        SALVAGER_HELMET = register("salvager_helmet", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_ANCIENT, ArmorType.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.5), properties.rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));
        SALVAGER_SUIT = register("salvager_suit", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_MODERN, ArmorType.CHESTPLATE, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.1).addValue(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.33).addValue(Attributes.SUBMERGED_MINING_SPEED, 0.33), properties.rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));
        SALVAGER_WADERS = register("salvager_waders", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_MODERN, ArmorType.LEGGINGS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.1).addValue(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.33).addValue(Attributes.SUBMERGED_MINING_SPEED, 0.33), properties.rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));
        SALVAGER_BOOTS = register("salvager_boots", properties -> new AquamiraeArmorItem(false, AquamiraeArmorMaterials.SALVAGER_MODERN, ArmorType.BOOTS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.1).addValue(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.33).addValue(Attributes.SUBMERGED_MINING_SPEED, 0.33), properties.rarity(Rarity.RARE).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SALVAGER_ARMOR))));

        ABYSSAL_TIARA = register("abyssal_tiara", properties -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_LIGHT, ArmorType.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.3), properties.rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_HEAUME = register("abyssal_heaume", properties -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorType.HELMET, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), properties.rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_BRIGANDINE = register("abyssal_brigandine", properties -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorType.CHESTPLATE, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), properties.rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_LEGGINGS = register("abyssal_leggings", properties -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorType.LEGGINGS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), properties.rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ABYSSAL_BOOTS = register("abyssal_boots", properties -> new AquamiraeArmorItem(true, AquamiraeArmorMaterials.ABYSSAL_HEAVY, ArmorType.BOOTS, new AquamiraeArmorItem.AttributeBuilder().addMultipliedBase(AquamiraeAttributes.DEPTHS_RESILIENCE, 0.2), properties.rarity(Rarity.EPIC).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.ABYSSAL_ARMOR)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));

        REMNANT_SABER = register("remnant_saber", properties -> new Item(properties.sword(AquamiraeTiers.REMNANT_SABER.material(), 4, -2.0F).attributes(SwordItemFactory.createAttributes(4, -2, 0.2)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.REMNANT_SABER))));
        TERRIBLE_CLEAVER = register("terrible_cleaver", properties -> new Item(properties.rarity(Rarity.UNCOMMON).sword(AquamiraeTiers.TERRIBLE_CLEAVER.material(), 6, -3.0F).attributes(SwordItemFactory.createAttributes(6, -3, 0.4)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_CLEAVER))));
        TERRIBLE_BLADE = register("terrible_blade", properties -> new Item(properties.rarity(Rarity.UNCOMMON).sword(AquamiraeTiers.TERRIBLE_BLADE.material(), 4, -1.0F).attributes(SwordItemFactory.createAttributes(4, -1f, 0.4)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_BLADE))));
        TERRIBLE_FANG = register("terrible_fang", properties -> new Item(properties.rarity(Rarity.UNCOMMON).sword(AquamiraeTiers.TERRIBLE_FANG.material(), 5, -2.0F).attributes(SwordItemFactory.createAttributes(5, -2, 0.4)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TERRIBLE_FANG))));
        DAGGER_OF_GREED = register("dagger_of_greed", properties -> new Item(properties.rarity(Rarity.RARE).sword(AquamiraeTiers.DAGGER_OF_GREED.material(), 8, -2.0F).attributes(SwordItemFactory.createAttributes(8, -2, 1)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.DAGGER_OF_GREED))));
        SHATTERBLADE = register("shatterblade", properties -> new Item(properties.rarity(Rarity.EPIC).sword(AquamiraeTiers.SHATTERBLADE.material(), 6, -2.6F).attributes(SwordItemFactory.createAttributes(6, -2.6, 0.6)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SHATTERBLADE)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        SHATTERSPIKE = register("shatterspike", properties -> new Item(properties.rarity(Rarity.EPIC).sword(AquamiraeTiers.SHATTERSPIKE.material(), 13, -3.2F).attributes(SwordItemFactory.createAttributes(13, -3.2, 0.6, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.SHATTERSPIKE)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        TIDEPIERCER = register("tidepiercer", properties -> new Item(properties.rarity(Rarity.EPIC).fireResistant().sword(AquamiraeTiers.TIDEPIERCER.material(), 13, -2.8F).attributes(SwordItemFactory.createAttributes(13, -2.8, 0.8, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TIDEPIERCER))));
        JAWBREAKER = register("jawbreaker", properties -> new JawbreakerItem(properties.rarity(Rarity.EPIC).fireResistant().sword(AquamiraeTiers.JAWBREAKER.material(), 13, -2.8F).food(new FoodProperties.Builder().saturationModifier(0.3f).nutrition(2).build(), Consumables.defaultFood().build()).attributes(SwordItemFactory.createAttributes(13, -2.8, 0.8, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.JAWBREAKER))));
        TEMPEST_TRIDENT = register("tempest_trident", properties -> new Item(properties.rarity(Rarity.EPIC).fireResistant().sword(AquamiraeTiers.TEMPEST_TRIDENT.material(), 13, -2.8F).attributes(SwordItemFactory.createAttributes(13, -2.8, 0.8, 0.33)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.TEMPEST_TRIDENT)).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        DREADWAKE = register("dreadwake", properties -> new DreadwakeItem(properties.rarity(Rarity.EPIC).fireResistant().sword(AquamiraeTiers.DREADWAKE.material(), 6, -2.0F).attributes(SwordItemFactory.createAttributes(6, -2, 0.5)).component(AquamiraeDataComponents.ABILITY_SET, AbilitySetComponent.of(AquamiraeAbilitySets.DREADWAKE)).component(AquamiraeDataComponents.KEEPSAKE, new Keepsake("VenOos")).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));

        TERRIBLE_CHAKRAM = register("terrible_chakram", properties -> new TerribleChakramItem(properties.stacksTo(5).rarity(Rarity.UNCOMMON).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.chakram"))));
        MAZE_ROSE = register("maze_rose", properties -> new MazeRoseItem(properties.stacksTo(5).rarity(Rarity.EPIC).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.chakram"))));

        ECHO_COMPASS = register("echo_compass", properties -> new EchoCompassItem(properties.stacksTo(1).rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.echo_compass"))));
        LOGBOOK = register("logbook", properties -> new LogBookItem(properties.stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

        SHIP_GRAVEYARD_ECHO = register("ship_graveyard_echo", properties -> new Item(properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(AquamiraeDataComponents.NO_GRAVITY, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.ship_graveyard_echo"))));
        WISTERIA_NIVEIS = register("wisteria_niveis", properties -> new WisteriaNiveisItem(properties.rarity(Rarity.COMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.wisteria_niveis"))));
        OXYHELIUM = register("oxyhelium", properties -> new Item(properties));
        MAELSTROM_VESTIGE = register("maelstrom_vestige", properties -> new Item(properties.rarity(Rarity.EPIC).component(AquamiraeDataComponents.NO_GRAVITY, Unit.INSTANCE).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.maelstrom_vestige"))));
        ABYSSAL_AMETHYST = register("abyssal_amethyst", properties -> new Item(properties.rarity(Rarity.EPIC).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.abyssal_amethyst"))));
        ANGLER_FANG = register("angler_fang", properties -> new Item(properties.rarity(Rarity.UNCOMMON)));
        FROZEN_KEY = register("frozen_key", properties -> new Item(properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.frozen_key"))));

        PIRATE_POUCH = register("pirate_pouch", properties -> new StashItem(AquamiraeBlocks.PIRATE_POUCH, AquamiraeSounds.ITEM_PIRATE_POUCH_OPEN, properties.component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.pirate_pouch"))));
        TREASURE_POUCH = register("treasure_pouch", properties -> new StashItem(AquamiraeBlocks.TREASURE_POUCH, AquamiraeSounds.ITEM_TREASURE_POUCH_OPEN, properties.component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.treasure_pouch"))));

        SHARP_BONES = register("sharp_bones", properties -> new SharpBonesItem(properties.food(new FoodProperties.Builder().nutrition(1).saturationModifier(0f).build())));
        FIN = register("fin", properties -> new SeaFoodItem(properties.food(new FoodProperties.Builder().nutrition(1).saturationModifier(0f).build())));
        ESCA = register("esca", properties -> new SeaFoodItem(properties.food(new FoodProperties.Builder().saturationModifier(1.2f).nutrition(2).alwaysEdible().build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.FURY_OF_THE_DEPTHS.holder(), 3600, 0), 1.0f)).build()).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE)));
        ESCA_SKEWER = register("esca_skewer", properties -> new SeaFoodItem(properties.food(new FoodProperties.Builder().saturationModifier(1.2f).nutrition(8).alwaysEdible().build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.FURY_OF_THE_DEPTHS.holder(), 9600, 2), 1.0f)).build()).usingConvertsTo(Items.STICK).component(AquamiraeDataComponents.NO_SHADING, Unit.INSTANCE).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.esca_skewer"))));
        SPINEFISH = register("spinefish", properties -> new SeaFoodItem(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0f).build()).usingConvertsTo(SHARP_BONES.get()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.spinefish"))));
        COOKED_SPINEFISH = register("cooked_spinefish", properties -> new SeaFoodItem(properties.food(new FoodProperties.Builder().saturationModifier(0.8f).nutrition(8).alwaysEdible().build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 3600, 1), 1.0f)).build()).usingConvertsTo(SHARP_BONES.get()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.cooked_spinefish"))));
        JELLYFISH_JELLY = register("jellyfish_jelly", properties -> new SeaFoodItem(properties.stacksTo(16).rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(1).saturationModifier(2f).build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 60, 2), 0.1f)).build()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.jellyfish_jelly"))));
        SEA_CASSEROLE = register("sea_casserole", properties -> new SeaFoodItem(properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).alwaysEdible().build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 9600, 2), 1.0f)).build())));
        SEA_STEW = register("sea_stew", properties -> new SeaFoodItem(properties.stacksTo(1).rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(12).saturationModifier(1.2f).alwaysEdible().build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 6000, 2), 1.0f)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH, 6000, 0), 1.0f)).build()).usingConvertsTo(Items.BOWL)));
        POSEIDON_BREAKFAST = register("poseidon_breakfast", properties -> new SeaFoodItem(properties.stacksTo(1).rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(20).saturationModifier(1.6f).alwaysEdible().build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.FURY_OF_THE_DEPTHS.holder(), 12000, 3), 1.0f)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.RESILIENCE_OF_THE_DEPTHS.holder(), 12000, 3), 1.0f)).onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH, 12000, 1), 1.0f)).build()).usingConvertsTo(Items.BOWL).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
        VOID_SQUID = register("void_squid", properties -> new SeaFoodItem(properties.stacksTo(8).rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(20).saturationModifier(2f).alwaysEdible().build(), Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(AquamiraeEffects.DEVOURING_CURSE.holder(), 12000, 0), 1.0f)).build()).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translateEmptyAfter("tooltip.aquamirae.void_squid"))));

        FROZEN_CHEST = register("frozen_chest", properties -> new BlockItem(AquamiraeBlocks.FROZEN_CHEST.get(), properties.rarity(Rarity.EPIC)));
        GOLDEN_MOTH_JAR = register("golden_moth_jar", properties -> new BlockItem(AquamiraeBlocks.GOLDEN_MOTH_JAR.get(), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.golden_moth_jar"))));
        LUMINESCENT_LAMP = register("luminescent_lamp", properties -> new BlockItem(AquamiraeBlocks.LUMINESCENT_LAMP.get(), properties.component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.luminescent_lamp"))));
        LUMINESCENT_BUBBLE = register("luminescent_bubble", properties -> new LuminescentBubbleItem(properties.component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.luminescent_bubble"))));
        PAINTING_ANGLERFISH = register("painting_anglerfish", properties -> new BlockItem(AquamiraeBlocks.PAINTING_ANGLERFISH.get(), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_anglerfish"))));
        PAINTING_OXYHELIUM = register("painting_oxyhelium", properties -> new BlockItem(AquamiraeBlocks.PAINTING_OXYHELIUM.get(), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_oxyhelium"))));
        PAINTING_TORTURED_SOUL = register("painting_tortured_soul", properties -> new BlockItem(AquamiraeBlocks.PAINTING_TORTURED_SOUL.get(), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_tortured_soul"))));
        PAINTING_AURORA = register("painting_aurora", properties -> new BlockItem(AquamiraeBlocks.PAINTING_AURORA.get(), properties.rarity(Rarity.UNCOMMON).component(AquamiraeDataComponents.TOOLTIP, ItemTooltip.translate("tooltip.aquamirae.painting_aurora"))));
    }

    /**
     * MC 26.1.2: Item constructors resolve their id from the properties
     * (effectiveDescriptionId -> "Item id not set"), so every item receives
     * its ResourceKey through the factory.
     */
    private static DeferredItem<Item> register(String name, Function<Item.Properties, Item> factory) {
        var key = ResourceKey.create(Registries.ITEM, Aquamirae.id(name));
        return AquamiraeRegistries.REGISTRAR.registerItem(Aquamirae.id(name),
                () -> factory.apply(new Item.Properties().setId(key)));
    }

    static void init() {}
}
