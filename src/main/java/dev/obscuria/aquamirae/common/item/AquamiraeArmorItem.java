package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.renderer.AquamiraeArmorRendererProvider;
import dev.obscuria.aquamirae.registry.AquamiraeAttributes;
import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.fragmentum.v2.api.common.registry.DeferredAttribute;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * MC 26.1.2: {@code ArmorItem} is gone; armor is a plain {@link Item} whose
 * properties are configured through {@code Item.Properties#humanoidArmor}.
 * The custom attribute modifiers are merged on top of the material's own
 * attribute set (replacing the old {@code defaultModifiers} override).
 */
public class AquamiraeArmorItem extends Item implements GeoItem {

    protected final AnimatableInstanceCache cache;
    protected final boolean glowing;
    protected final ArmorType type;

    public AquamiraeArmorItem(
            boolean glowing, ArmorMaterial material,
            ArmorType type, AttributeBuilder builder, Properties properties
    ) {
        super(configure(glowing, material, type, builder, properties));
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.glowing = glowing;
        this.type = type;
    }

    private static Properties configure(
            boolean glowing, ArmorMaterial material, ArmorType type,
            AttributeBuilder builder, Properties properties
    ) {
        var modifierId = Aquamirae.id("armor." + type.getName());
        var equipmentSlotGroup = EquipmentSlotGroup.bySlot(type.getSlot());
        var attributes = builder.build(material.createAttributes(type), modifierId, equipmentSlotGroup);
        return properties.stacksTo(1).humanoidArmor(material, type).attributes(attributes);
    }

    public boolean hasGlowingLayer() {
        return glowing;
    }

    public ArmorType getType() {
        return this.type;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {}

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new AquamiraeArmorRendererProvider(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static class AttributeBuilder {

        private final List<Entry> entries = new ArrayList<>();

        public AttributeBuilder addValue(DeferredAttribute attribute, double value) {
            return addValue(attribute.holder(), value);
        }

        public AttributeBuilder addValue(Holder<Attribute> attribute, double value) {
            return add(attribute, value, AttributeModifier.Operation.ADD_VALUE);
        }

        public AttributeBuilder addMultipliedBase(DeferredAttribute attribute, double value) {
            return addMultipliedBase(attribute.holder(), value);
        }

        public AttributeBuilder addMultipliedBase(Holder<Attribute> attribute, double value) {
            return add(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        }

        public AttributeBuilder add(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            this.entries.add(new Entry(attribute, value, operation));
            return this;
        }

        private ItemAttributeModifiers build(ItemAttributeModifiers modifiers, Identifier modifierId, EquipmentSlotGroup group) {
            for (var entry : entries) modifiers = modifiers.withModifierAdded(entry.attribute, entry.buildModifier(modifierId), group);
            return modifiers;
        }

        private record Entry(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {

            public AttributeModifier buildModifier(Identifier modifierId) {
                return new AttributeModifier(modifierId, value, operation);
            }
        }
    }
}
