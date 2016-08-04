package wiresegal.silimatics.api.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public interface ILens {
    void onUsingTick(World world, EntityPlayer player, ItemStack stack);
    default boolean shouldMarkAsOculator(ItemStack stack) {
        return true;
    }

    default String addTooltip(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        return "";
    }
}
