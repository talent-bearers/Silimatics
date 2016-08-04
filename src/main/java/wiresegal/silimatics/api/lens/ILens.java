package wiresegal.silimatics.api.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import wiresegal.silimatics.common.core.ItemNBTHelper;
import wiresegal.silimatics.common.item.ItemLens;

import java.util.List;

public interface ILens {
    void onUsingTick(World world, EntityPlayer player, ItemStack stack);
    default boolean shouldMarkAsOculator(ItemStack stack) {
        return true;
    }

    default void addTooltip(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(ItemLens.getLensFromStack(getLensStack(stack)) != null)
            tooltip.add(ItemLens.getLensFromStack(getLensStack(stack)).getClass().getCanonicalName());
        tooltip.add(getLensStack(stack).getMetadata() + "");
    }
    default ItemStack getLensStack(ItemStack stack) {
        NBTTagCompound compound = ItemNBTHelper.getCompound(stack, "lens", true);
        if(compound != null)
            return ItemStack.loadItemStackFromNBT(compound);
        return null;
    }
}
