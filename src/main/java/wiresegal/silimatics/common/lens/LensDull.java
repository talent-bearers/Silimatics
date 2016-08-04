package wiresegal.silimatics.common.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wiresegal.silimatics.api.lens.ILens;

public class LensDull implements ILens {
    @Override
    public void onUsingTick(World world, EntityPlayer player, ItemStack stack) {
        //NO-OP
    }

    @Override
    public boolean shouldMarkAsOculator(ItemStack stack) {
        return false;
    }
}
