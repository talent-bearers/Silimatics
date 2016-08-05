package wiresegal.silimatics.client.core

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleFallingDust
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.core.CommonProxy
import wiresegal.silimatics.common.core.ModBlocks

class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        ModelLoader.setCustomStateMapper(ModBlocks.glassPane,
                StateMap.Builder()
                        .withName(BlockGlass.SAND_TYPE)
                        .withSuffix("GlassPane").build())
    }

    override fun makeParticleDust(x: Double, y: Double, z: Double, xs: Double, ys: Double, zs: Double, id: Int) {
        for (x0 in 1..4) {
            val dust = SieveDustParticle(Minecraft.getMinecraft().theWorld,
                    x + 0.8 * Minecraft.getMinecraft().theWorld.rand.nextFloat() + 0.15,
                    y + 0.69,
                    z + 0.8 * Minecraft.getMinecraft().theWorld.rand.nextFloat() + 0.15,
                    1f,
                    1f,
                    153/255f);

            Minecraft.getMinecraft().effectRenderer.addEffect(dust);
        }
    }
    private class SieveDustParticle(worldIn: World?, posXIn: Double, posYIn: Double, posZIn: Double, red: Float,
                                    green: Float, blue: Float) : ParticleFallingDust(worldIn,
                                    posXIn, posYIn, posZIn, red, green, blue) {    }
}

