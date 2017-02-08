package wiresegal.silimatics.client.render

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import wiresegal.silimatics.common.block.BlockLensGrinder
import wiresegal.silimatics.common.block.BlockLensGrinder.TileLensGrinder.Companion.TAG_SHOULD_FLIP
import wiresegal.silimatics.common.core.ItemNBTHelper

class RenderLensGrinder : TileEntitySpecialRenderer<BlockLensGrinder.TileLensGrinder>() {
    override fun renderTileEntityAt(te: BlockLensGrinder.TileLensGrinder?, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        if (te != null) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(x, y, z)
            GlStateManager.scale(0.5, 0.5, 0.5)
            for (i in 0 until te.inventory.slots) {
                val stack = te.inventory.getStackInSlot(i) ?: continue
                GlStateManager.pushMatrix()
                GlStateManager.translate(1.0, 0.5 + 0.15 * (10 - i) + if (ItemNBTHelper.getBoolean(stack.copy(), TAG_SHOULD_FLIP, false) && i != 0) 0.075 else 0.0, 1.0)
                GlStateManager.rotate(90F * i / 10F, 0F, 1F, 0F)
                GlStateManager.rotate(90F, 1F, 0F, 0F)
                Minecraft.getMinecraft().renderItem.renderItem(stack, ItemCameraTransforms.TransformType.FIXED)
                GlStateManager.popMatrix()
            }
            GlStateManager.popMatrix()
        }
    }
}
