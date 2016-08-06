package wiresegal.silimatics.client.render

import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumAction
import net.minecraft.util.EnumHandSide
import net.minecraft.util.ResourceLocation
import wiresegal.silimatics.common.item.ItemLens
import wiresegal.silimatics.common.item.ItemLensFrames
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.getLensStack
import wiresegal.silimatics.common.lens.LensCourier
import wiresegal.silimatics.common.lib.LibMisc

class OculatorRenderLayer(val render: RenderPlayer) : LayerRenderer<AbstractClientPlayer> {

    val TEXTURE = ResourceLocation("${LibMisc.MODID}", "textures/blocks/empty.png")

    fun EntityPlayer.isWearingOculatorLens(): Boolean {
        val headStack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) ?: return false
        if (headStack.item is ItemLensFrames) {
            val lensStack = headStack.getLensStack()
            return ItemLens.getLensFromStack(lensStack) is LensCourier
        }
        return false
    }
    override fun doRenderLayer(entitylivingbaseIn: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        if(!entitylivingbaseIn.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean(ItemLens.Companion.EventHandler.OCULATOR)) return
        if(!entitylivingbaseIn.isWearingOculatorLens()) return
        render.bindTexture(TEXTURE)
        setModelVisibilities(entitylivingbaseIn)
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN)
        val glowing = entitylivingbaseIn.isGlowing
        GlStateManager.color(255F, 255F, 25F)
        entitylivingbaseIn.isGlowing = true
        render.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN)
        GlStateManager.color(1F, 1F, 1F)
        entitylivingbaseIn.isGlowing = glowing
    }

    override fun shouldCombineTextures(): Boolean {
        return false
    }

    private fun setModelVisibilities(clientPlayer: AbstractClientPlayer) {
        val modelplayer = render.mainModel

        if (clientPlayer.isSpectator) {
            modelplayer.setInvisible(false)
            modelplayer.bipedHead.showModel = true
            modelplayer.bipedHeadwear.showModel = true
        } else {
            val stackMain = clientPlayer.heldItemMainhand
            val stackOff = clientPlayer.heldItemOffhand
            modelplayer.setInvisible(true)
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT)
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET)
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG)
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG)
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE)
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE)
            modelplayer.isSneak = clientPlayer.isSneaking
            var poseMain = ModelBiped.ArmPose.EMPTY
            var poseOff = ModelBiped.ArmPose.EMPTY

            if (stackMain != null) {
                poseMain = ModelBiped.ArmPose.ITEM

                if (clientPlayer.itemInUseCount > 0) {
                    val enumaction = stackMain.itemUseAction

                    if (enumaction == EnumAction.BLOCK) {
                        poseMain = ModelBiped.ArmPose.BLOCK
                    } else if (enumaction == EnumAction.BOW) {
                        poseMain = ModelBiped.ArmPose.BOW_AND_ARROW
                    }
                }
            }

            if (stackOff != null) {
                poseOff = ModelBiped.ArmPose.ITEM

                if (clientPlayer.itemInUseCount > 0) {
                    val enumaction1 = stackOff.itemUseAction

                    if (enumaction1 == EnumAction.BLOCK) {
                        poseOff = ModelBiped.ArmPose.BLOCK
                    }
                }
            }

            if (clientPlayer.primaryHand == EnumHandSide.RIGHT) {
                modelplayer.rightArmPose = poseMain
                modelplayer.leftArmPose = poseOff
            } else {
                modelplayer.rightArmPose = poseOff
                modelplayer.leftArmPose = poseMain
            }
        }
    }
}
