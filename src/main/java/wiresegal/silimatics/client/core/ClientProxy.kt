package wiresegal.silimatics.client.core

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleFallingDust
import net.minecraft.client.particle.ParticleRedstone
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.client.render.OculatorRenderLayer
import wiresegal.silimatics.client.render.TileEntitySpecialRendererLensGrinder
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.block.BlockLensGrinder
import wiresegal.silimatics.common.core.CommonProxy
import wiresegal.silimatics.common.core.ModBlocks

class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        ModelLoader.setCustomStateMapper(ModBlocks.glassPane,
                StateMap.Builder()
                        .withName(BlockGlass.SAND_TYPE)
                        .withSuffix("GlassPane").build())
        ClientRegistry.bindTileEntitySpecialRenderer(BlockLensGrinder.TileLensGrinder::class.java, TileEntitySpecialRendererLensGrinder())
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        val skinMap = Minecraft.getMinecraft().renderManager.skinMap

        var render = skinMap["default"]
        render?.addLayer(OculatorRenderLayer(render))

        render = skinMap["slim"]
        render?.addLayer(OculatorRenderLayer(render))
    }

    override fun makeParticleDust(x: Double, y: Double, z: Double, xs: Double, ys: Double, zs: Double, id: Int) {
        for (x0 in 1..4) {
            val dust = SieveDustParticle(Minecraft.getMinecraft().theWorld,
                    x + 0.8 * Minecraft.getMinecraft().theWorld.rand.nextFloat() + 0.15,
                    y + 0.69,
                    z + 0.8 * Minecraft.getMinecraft().theWorld.rand.nextFloat() + 0.15,
                    1f,
                    1f,
                    153 / 255f)

            Minecraft.getMinecraft().effectRenderer.addEffect(dust)
        }
    }

    override fun makeParticleOculator(worldIn: World?, xCoordIn: Double, yCoordIn: Double, zCoordIn: Double, scale: Float, red: Float, green: Float, blue: Float) {
        for (x0 in 1..4) {
            val dust = OculatorParticle(Minecraft.getMinecraft().theWorld,
                    xCoordIn + 0.8 * Minecraft.getMinecraft().theWorld.rand.nextFloat() + 0.15,
                    yCoordIn + 0.69,
                    zCoordIn + 0.8 * Minecraft.getMinecraft().theWorld.rand.nextFloat() + 0.15,
                    scale,
                    red,
                    green,
                    blue)

            Minecraft.getMinecraft().effectRenderer.addEffect(dust)
        }
    }

    //particles under this line are literally vanilla particles but i can't be bothered to use their constructors the correct way
    private class SieveDustParticle(worldIn: World?, posXIn: Double, posYIn: Double, posZIn: Double, red: Float,
                                    green: Float, blue: Float) : ParticleFallingDust(worldIn,
            posXIn, posYIn, posZIn, red, green, blue)

    private class OculatorParticle(worldIn: World?, xCoordIn: Double, yCoordIn: Double, zCoordIn: Double, scale: Float,
                                   red: Float, green: Float, blue: Float) : ParticleRedstone(worldIn, xCoordIn, yCoordIn,
            zCoordIn, scale, red, green, blue)
}

