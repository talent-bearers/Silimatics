package wiresegal.silimatics.api.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ILens {
    void onUsingTick(World world, EntityPlayer player, ItemStack stack);
    default boolean shouldMarkAsOculator(ItemStack stack) {
        return true;
    }
}
