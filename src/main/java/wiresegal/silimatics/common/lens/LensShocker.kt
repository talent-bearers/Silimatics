package wiresegal.silimatics.common.lens

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.ItemLens
import wiresegal.silimatics.common.item.ItemLensFrames
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.getLensStack
import wiresegal.silimatics.common.lib.LibMisc

class LensShocker : ILens {

    companion object {
        val TAG_WARMUP = "warmup"
        val TOTAL_WARMUP = 5 * 20

        val LOOK_THRESHOLD = Math.cos(Math.PI/4)
    }

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (player.isSneaking) {
            val warmup = ItemNBTHelper.getInt(stack, TAG_WARMUP, 0)
            if (warmup < TOTAL_WARMUP)
                ItemNBTHelper.setInt(stack, TAG_WARMUP, warmup + 1)
            else if (world.totalWorldTime % 10 == 0L)
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.2f, 0.001f)
        } else {
            val warmup = ItemNBTHelper.getInt(stack, TAG_WARMUP, 0)
            if (warmup == TOTAL_WARMUP) {
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 2f, 0.001f)
                if (!world.isRemote) {
                    val entities = world.getEntitiesWithinAABBExcludingEntity(player, player.entityBoundingBox.expand(10.0, 10.0, 10.0))
                    for (entity in entities) if (entity is EntityLivingBase) {
                        val dir = Vec3d(player.posX, player.posY + player.eyeHeight, player.posZ).subtract(entity.posX, entity.posY, entity.posZ)
                        val look = entity.lookVec
                        val dot = dir.normalize().dotProduct(look.normalize())
                        if (dot > LOOK_THRESHOLD) {
                            val normDir = dir.normalize()
                            for (i in 1..dir.lengthVector().toInt()) {
                                val pos = entity.position.add(0.0, entity.eyeHeight.toDouble(), 0.0).add(normDir.xCoord * i, normDir.yCoord * i, normDir.zCoord * i)
                                val state = world.getBlockState(pos)
                                if (!state.isFullCube && !world.isAirBlock(pos)) {
                                    continue
                                }
                            }
                            entity.addPotionEffect(PotionEffect(MobEffects.NIGHT_VISION, 20 * 20))
                            entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 10 * 20, 1))
                            entity.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 10 * 20))
                        }
                    }
                    player.addPotionEffect(PotionEffect(MobEffects.NIGHT_VISION, 15 * 20, 0, true, false))
                }
            }
            ItemNBTHelper.setInt(stack, TAG_WARMUP, 0)
        }
    }
}
