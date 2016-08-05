package wiresegal.silimatics.client.render

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import org.lwjgl.opengl.GL11
import wiresegal.silimatics.common.block.BlockLensGrinder
import java.util.*

class TileEntitySpecialRendererLensGrinder : TileEntitySpecialRenderer<BlockLensGrinder.TileLensGrinder>() {
    override fun renderTileEntityAt(tel: BlockLensGrinder.TileLensGrinder?, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        if (tel is BlockLensGrinder.TileLensGrinder) {
            if(tel.inventory == null) return
            GL11.glPushMatrix();
            val item: EntityItem = EntityItem(Minecraft.getMinecraft().theWorld, x, y, z, tel.inventory);
            item.hoverStart = 0f;
            val random: Random = Random()
            random.setSeed(item.entityItem.hashCode().toLong());
            GL11.glTranslated(x, y, z)
            GL11.glTranslated(x + 1, y - 1.5 + (0.15 * tel.i), z)
            GL11.glScaled(2.0, 2.0, 2.0)
            GL11.glRotated(90.0, 32.0, 1.0, 0.0)
            Minecraft.getMinecraft().renderManager.doRenderEntity(item, 0.0, 0.0, 0.0, 0f, 0f, true)
            GL11.glPopMatrix()
        }
    }
}
