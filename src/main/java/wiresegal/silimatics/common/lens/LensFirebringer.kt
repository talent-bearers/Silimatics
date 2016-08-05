package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntitySmallFireball
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens

class LensFirebringer : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        val fireball: EntitySmallFireball = EntitySmallFireball(world, player.posX, player.posY + 1, player.posZ, player.lookVec.xCoord / 8, player.lookVec.yCoord / 8, player.lookVec.zCoord / 8)
        world.spawnEntityInWorld(fireball)
    }
}
