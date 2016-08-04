package wiresegal.silimatics.common.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wiresegal.silimatics.api.lens.ILens;

import java.util.List;

public class LensWindstormer implements ILens {
    @Override
    public void onUsingTick(World world, EntityPlayer player, ItemStack stack) {
        player.motionX += -player.getLookVec().xCoord / 20;
        player.motionY += -player.getLookVec().yCoord / 20;
        player.motionZ += -player.getLookVec().zCoord / 20;
    }

    @Override
    public boolean shouldMarkAsOculator(ItemStack stack) {
        return true;
    }
}
