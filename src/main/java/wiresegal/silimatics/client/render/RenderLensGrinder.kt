package wiresegal.silimatics.client.render

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import wiresegal.silimatics.common.block.BlockLensGrinder

class RenderLensGrinder : TileEntitySpecialRenderer<BlockLensGrinder.TileLensGrinder>() {
    override fun renderTileEntityAt(te: BlockLensGrinder.TileLensGrinder?, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        if (te is BlockLensGrinder.TileLensGrinder) {
            if (te.inventory == null) return
            GlStateManager.pushMatrix()
            GlStateManager.translate(x, y, z)
            GlStateManager.scale(0.5, 0.5, 0.5)
            GlStateManager.translate(1.0, 0.5 + (0.15 * te.clicksLeft), 1.0)
            GlStateManager.rotate(90F * te.clicksLeft / 10F, 0F, 1F, 0F)
            GlStateManager.rotate(90F, 1F, 0F, 0F)
            Minecraft.getMinecraft().renderItem.renderItem(te.inventory, ItemCameraTransforms.TransformType.FIXED)
            GlStateManager.popMatrix()
        }
    }
}
