package wiresegal.silimatics.client.render

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11
import wiresegal.silimatics.common.block.BlockLensGrinder
import java.util.*

class TileEntitySpecialRendererLensGrinder : TileEntitySpecialRenderer<BlockLensGrinder.TileLensGrinder>() {
    override fun renderTileEntityAt(tel: BlockLensGrinder.TileLensGrinder?, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        super.renderTileEntityAt(tel, x, y, z, partialTicks, destroyStage)
        if (tel is BlockLensGrinder.TileLensGrinder){
            val renderItems: ArrayList<ItemStack> = mutableListOf<ItemStack>() as ArrayList
            renderItems.addAll(tel.inventory)
            for (i in tel.inventory.indices){
                GL11.glPushMatrix();
                val item: EntityItem = EntityItem(Minecraft.getMinecraft().theWorld, x, y, z, renderItems[i]);
                item.hoverStart = 0f;
                val random: Random = Random();
                random.setSeed(item.entityItem.hashCode().toLong());
                GL11.glTranslated(x, y, z)
                GL11.glTranslated(0.475 + random.nextFloat() / 20.0, 0.05 + random.nextFloat() / 20.0, 0.475 + random.nextFloat() / 20.0)
                GL11.glScaled(0.65,0.65,0.65)
                GL11.glRotated(random.nextInt(360) * 1.0, 1.0, 1.0, 0.0)
                Minecraft.getMinecraft().renderManager.doRenderEntity(item, 0.0, 0.0, 0.0, 0f, 0f, true)
                GL11.glPopMatrix()
            }
        }
    }
}
