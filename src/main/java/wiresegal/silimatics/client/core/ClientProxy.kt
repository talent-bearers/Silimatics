package wiresegal.silimatics.client.core

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.client.render.GlowingOutlineEventHandler
import wiresegal.silimatics.client.render.RenderLensGrinder
import wiresegal.silimatics.common.block.BlockLensGrinder
import wiresegal.silimatics.common.core.CommonProxy

class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        ClientRegistry.bindTileEntitySpecialRenderer(BlockLensGrinder.TileLensGrinder::class.java, RenderLensGrinder())
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)

        GlowingOutlineEventHandler

        Minecraft.getMinecraft().fontRendererObj = FontHijacker(Minecraft.getMinecraft().fontRendererObj)
    }
}

