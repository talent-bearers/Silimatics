package wiresegal.silimatics.common.lens

import com.teamwizardry.librarianlib.common.util.RaycastUtils
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumAction
import net.minecraft.util.EnumHandSide
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.ItemLensFrames
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.getLensStack

/**
 * Created by Elad on 2/18/2017.
 */
object LensDisguiser : ILens {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onPlayerRender(render: RenderPlayerEvent.Pre) {
        val headStack = render.entityLiving.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
        if (headStack != null && headStack.item is ItemLensFrames) {
            val lensStack = headStack.getLensStack()
            if (lensStack.item == ModItems.lens && lensStack.itemDamage == EnumSandType.ILLUSION.ordinal) {
                render.isCanceled = true
                val renderManager = render.renderer.renderManager
                val renderPlayer = RenderPlayer(renderManager)
                val entity = RaycastUtils.getEntityLookedAt(render.entity) as? AbstractClientPlayer? ?: return
                if (renderManager.renderViewEntity === entity) {
                    var d0 = render.y

                    if (entity.isSneaking && entity !is EntityPlayerSP) {
                        d0 = render.y - 0.125
                    }

                    renderPlayer.setModelVisibilities(entity)
                    GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN)
                    renderPlayer.doRender(entity, render.x, d0, render.z, entity.rotationYaw, render.partialRenderTick)
                    GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN)
                }
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(net.minecraftforge.client.event.RenderPlayerEvent.Post(entity, renderPlayer, render.partialRenderTick, render.x, render.y, render.z))
            }
        }
    }

    private fun RenderPlayer.setModelVisibilities(clientPlayer: AbstractClientPlayer) {
        val modelplayer = this.mainModel

        if (clientPlayer.isSpectator) {
            modelplayer.setInvisible(false)
            modelplayer.bipedHead.showModel = true
            modelplayer.bipedHeadwear.showModel = true
        } else {
            val itemstack = clientPlayer.heldItemMainhand
            val itemstack1 = clientPlayer.heldItemOffhand
            modelplayer.setInvisible(true)
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT)
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET)
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG)
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG)
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE)
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE)
            modelplayer.isSneak = clientPlayer.isSneaking
            var `modelbiped$armpose`: ModelBiped.ArmPose = ModelBiped.ArmPose.EMPTY
            var `modelbiped$armpose1`: ModelBiped.ArmPose = ModelBiped.ArmPose.EMPTY

            if (itemstack != null) {
                `modelbiped$armpose` = ModelBiped.ArmPose.ITEM

                if (clientPlayer.itemInUseCount > 0) {
                    val enumaction = itemstack.itemUseAction

                    if (enumaction == EnumAction.BLOCK) {
                        `modelbiped$armpose` = ModelBiped.ArmPose.BLOCK
                    } else if (enumaction == EnumAction.BOW) {
                        `modelbiped$armpose` = ModelBiped.ArmPose.BOW_AND_ARROW
                    }
                }
            }

            if (itemstack1 != null) {
                `modelbiped$armpose1` = ModelBiped.ArmPose.ITEM

                if (clientPlayer.itemInUseCount > 0) {
                    val enumaction1 = itemstack1.itemUseAction

                    if (enumaction1 == EnumAction.BLOCK) {
                        `modelbiped$armpose1` = ModelBiped.ArmPose.BLOCK
                    }
                }
            }

            if (clientPlayer.primaryHand == EnumHandSide.RIGHT) {
                modelplayer.rightArmPose = `modelbiped$armpose`
                modelplayer.leftArmPose = `modelbiped$armpose1`
            } else {
                modelplayer.rightArmPose = `modelbiped$armpose1`
                modelplayer.leftArmPose = `modelbiped$armpose`
            }
        }
    }

}