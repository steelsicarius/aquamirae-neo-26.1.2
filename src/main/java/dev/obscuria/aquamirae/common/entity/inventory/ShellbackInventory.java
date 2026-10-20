package dev.obscuria.aquamirae.common.entity.inventory;

import dev.obscuria.aquamirae.common.entity.Shellback;
import dev.obscuria.aquamirae.common.entity.ShellbackChestPart;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.Objects;

public class ShellbackInventory implements Container {

    public static final int CHEST_SIZE = 27;
    public static final int CHEST_COUNT = 4;
    public static final int TOTAL_SIZE = CHEST_SIZE * CHEST_COUNT;
    public static final int VALID_DISTANCE = 64;

    protected final NonNullList<ItemStack> items;
    protected final Shellback owner;

    public ShellbackInventory(Shellback owner) {
        this.owner = owner;
        this.items = NonNullList.withSize(TOTAL_SIZE, ItemStack.EMPTY);
    }

    public Container createChestView(ShellbackChestPart part, int chestIndex) {
        Objects.checkIndex(chestIndex, CHEST_COUNT);
        return new ChestSlice(part, chestIndex);
    }

    @Override
    public int getContainerSize() {
        return TOTAL_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (var stack : items)
            if (!stack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        var result = items.get(slot).split(amount);
        setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        var old = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        return old;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize())
            stack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return owner.isAlive() && player.distanceToSqr(owner) <= VALID_DISTANCE;
    }

    @Override
    public void clearContent() {
        Collections.fill(items, ItemStack.EMPTY);
    }

    @Override
    public void setChanged() {}

    public void save(ValueOutput output) {
        ContainerHelper.saveAllItems(output, items);
    }

    public void load(ValueInput input) {
        clearContent();
        ContainerHelper.loadAllItems(input, items);
    }

    protected class ChestSlice implements Container {

        protected final ShellbackChestPart part;
        protected final int offset;

        protected ChestSlice(ShellbackChestPart part, int chestIndex) {
            this.offset = chestIndex * CHEST_SIZE;
            this.part = part;
        }

        @Override
        public int getContainerSize() {
            return CHEST_SIZE;
        }

        @Override
        public boolean isEmpty() {
            for (int i = 0; i < CHEST_SIZE; i++)
                if (!items.get(offset + 1).isEmpty())
                    return false;
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            return items.get(offset + slot);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            return ShellbackInventory.this.removeItem(offset + slot, amount);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            return ShellbackInventory.this.removeItemNoUpdate(offset + slot);
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            ShellbackInventory.this.setItem(offset + slot, stack);
        }

        @Override
        public boolean stillValid(Player player) {
            return ShellbackInventory.this.stillValid(player);
        }

        @Override
        public void clearContent() {
            for (int i = 0; i < CHEST_SIZE; i++)
                items.set(offset + i, ItemStack.EMPTY);
        }

        @Override
        public void setChanged() {
            ShellbackInventory.this.setChanged();
        }

        @Override
        public void startOpen(net.minecraft.world.entity.ContainerUser user) {
            part.playSound(SoundEvents.CHEST_OPEN);
            owner.onChestOpen();
        }

        @Override
        public void stopOpen(net.minecraft.world.entity.ContainerUser user) {
            part.playSound(SoundEvents.CHEST_CLOSE);
            owner.onChestClose();
        }
    }
}