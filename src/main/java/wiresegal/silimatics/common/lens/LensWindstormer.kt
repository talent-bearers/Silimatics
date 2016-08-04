package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens

class LensWindstormer : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        player.motionX -= player.lookVec.xCoord / 20
        player.motionY -= player.lookVec.yCoord / 20
        player.motionZ -= player.lookVec.zCoord / 20
        //todo
    }
}
