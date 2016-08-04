package wiresegal.silimatics.common.lens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LensOculator extends ItemLenBase {

    public LensOculator(String name) {
        super(name);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean cast(World world, EntityPlayer player) {
        return false;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        //if(Minecraft.getMinecraft().theWorld != null)
            //GlStateManager.enableOutlineMode(0x12345);

    }
}
