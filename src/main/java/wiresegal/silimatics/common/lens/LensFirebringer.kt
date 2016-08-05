package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntitySmallFireball
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens

class LensFirebringer : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if(player.isSneaking) {
            val fireball: EntitySmallFireball = EntitySmallFireball(world, addPlayerLook(player, player.position).xCoord * 1.0, player.posY + 1, addPlayerLook(player, player.position).zCoord * 1.0, player.lookVec.xCoord / 8, player.lookVec.yCoord / 8, player.lookVec.zCoord / 8)
            world.spawnEntityInWorld(fireball)
        }
    }
}
