package wiresegal.silimatics.api.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ILens {
    boolean cast(World world, EntityPlayer player);
    String getUnlocalizedName(ItemStack stack);
}
