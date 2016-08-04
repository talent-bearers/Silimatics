package wiresegal.silimatics.common.lens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LensOculator extends ItemLensBase {

    public LensOculator(String name) {
        super(name);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onUsingTick(World world, EntityPlayer player, ItemStack stack) {
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderTick(RenderPlayerEvent event) {
        if(Minecraft.getMinecraft().theWorld != null)
            GlStateManager.enableOutlineMode(0x12345);
    }
}
