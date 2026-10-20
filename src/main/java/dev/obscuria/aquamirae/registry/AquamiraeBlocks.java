package dev.obscuria.aquamirae.registry;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.block.*;
import dev.obscuria.fragmentum.v2.api.common.registry.DeferredBlock;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public final class AquamiraeBlocks {

    public static final DeferredBlock<WisteriaNiveisBlock> WISTERIA_NIVEIS = register("wisteria_niveis", properties -> new WisteriaNiveisBlock(properties
            .strength(1f, 10f)
            .mapColor(MapColor.PLANT)
            .pushReaction(PushReaction.DESTROY)
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .sound(SoundType.WEEPING_VINES)
            .noCollision()));
    public static final DeferredBlock<StashBlock> PIRATE_POUCH = register("pirate_pouch", properties -> new StashBlock(properties
            .instabreak()
            .mapColor(MapColor.COLOR_BLACK)
            .pushReaction(PushReaction.DESTROY)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .sound(SoundType.WOOL)
            .noCollision()));
    public static final DeferredBlock<StashBlock> TREASURE_POUCH = register("treasure_pouch", properties -> new StashBlock(properties
            .instabreak()
            .mapColor(MapColor.COLOR_BLACK)
            .pushReaction(PushReaction.DESTROY)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .sound(SoundType.WOOL)
            .noCollision()));
    public static final DeferredBlock<OxyheliumBlock> OXYHELIUM = register("oxyhelium", properties -> new OxyheliumBlock(properties
            .mapColor(MapColor.WATER)
            .sound(SoundType.CORAL_BLOCK)
            .strength(0.8f, 1f)
            .randomTicks()
            .requiresCorrectToolForDrops()
            .noCollision()
            .speedFactor(0.8f)
            .jumpFactor(0.8f)
            .postProcess((bs, br, bp) -> OxyheliumBlock.isGlowing(bs) ? bp : null)
            .emissiveRendering((bs, br, bp) -> OxyheliumBlock.isGlowing(bs))
            .lightLevel(s -> OxyheliumBlock.isGlowing(s) ? 12 : 0)));
    public static final DeferredBlock<LuminescentBubbleBlock> LUMINESCENT_BUBBLE = register("luminescent_bubble", properties -> new LuminescentBubbleBlock(properties
            .mapColor(MapColor.WATER)
            .sound(SoundType.CORAL_BLOCK)
            .strength(0.1f, 0.5f)
            .lightLevel(s -> 15)
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .noCollision()
            .noOcclusion()
            .postProcess((bs, br, bp) -> bp)
            .emissiveRendering((bs, br, bp) -> true)
            .dynamicShape()));
    public static final DeferredBlock<LuminescentLampBlock> LUMINESCENT_LAMP = register("luminescent_lamp", properties -> new LuminescentLampBlock(properties
            .mapColor(MapColor.WOOD)
            .sound(SoundType.WOOD)
            .strength(1.4f, 6f)
            .lightLevel(s -> 15)
            .noOcclusion()));
    public static final DeferredBlock<FrozenChestBlock> FROZEN_CHEST = register("frozen_chest", properties -> new FrozenChestBlock(properties
            .strength(-1, 3600000)
            .mapColor(MapColor.WOOD)
            .sound(SoundType.WOOD)
            .noOcclusion()));
    public static final DeferredBlock<GoldenMothJarBlock> GOLDEN_MOTH_JAR = register("golden_moth_jar", properties -> new GoldenMothJarBlock(properties
            .strength(1f, 10f)
            .mapColor(MapColor.GOLD)
            .sound(SoundType.GLASS)
            .lightLevel(s -> 8)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .noLootTable()));
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_ANGLERFISH = register("painting_anglerfish", AquamiraeBlocks::painting);
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_OXYHELIUM = register("painting_oxyhelium", AquamiraeBlocks::painting);
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_TORTURED_SOUL = register("painting_tortured_soul", AquamiraeBlocks::painting);
    public static final DeferredBlock<CollectiblePaintingBlock> PAINTING_AURORA = register("painting_aurora", AquamiraeBlocks::painting);

    /**
     * MC 26.1.2: BlockBehaviour resolves its id from the properties
     * (effectiveDrops -> "Block id not set"), so every block receives its
     * ResourceKey through the factory.
     */
    private static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, T> factory) {
        var key = ResourceKey.create(Registries.BLOCK, Aquamirae.id(name));
        return AquamiraeRegistries.REGISTRAR.registerBlock(Aquamirae.id(name),
                () -> factory.apply(BlockBehaviour.Properties.of().setId(key)));
    }

    private static CollectiblePaintingBlock painting(BlockBehaviour.Properties properties) {
        return new CollectiblePaintingBlock(properties
                .strength(1f)
                .requiresCorrectToolForDrops()
                .mapColor(MapColor.WOOD)
                .sound(new SoundType(1.0f, 1.0f,
                        SoundEvents.PAINTING_BREAK,
                        SoundEvents.WOOL_STEP,
                        SoundEvents.PAINTING_PLACE,
                        SoundEvents.WOOD_HIT,
                        SoundEvents.WOOD_FALL))
                .noCollision()
                .noOcclusion());
    }

    static void init() {}
}
