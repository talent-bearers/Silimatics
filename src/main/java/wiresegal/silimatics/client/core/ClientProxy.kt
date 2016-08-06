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
import wiresegal.silimatics.client.render.RenderLensGrinder
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.block.BlockLensGrinder
import wiresegal.silimatics.common.core.CommonProxy
import wiresegal.silimatics.common.core.ModBlocks

class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        ClientRegistry.bindTileEntitySpecialRenderer(BlockLensGrinder.TileLensGrinder::class.java, RenderLensGrinder())
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
        val skinMap = Minecraft.getMinecraft().renderManager.skinMap

        var render = skinMap["default"]
        render?.addLayer(OculatorRenderLayer(render))

        render = skinMap["slim"]
        render?.addLayer(OculatorRenderLayer(render))

        Minecraft.getMinecraft().fontRendererObj = FontHijacker(Minecraft.getMinecraft().fontRendererObj)
    }
}

