package wiresegal.silimatics.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
/**
    Fired when a block could need brightsand power
 */
public class BrightsandEvent extends Event {
    private boolean isPowered;
    public final BlockPos pos;
    public final World world;

    public BrightsandEvent(BlockPos pos, World world) {
        this.pos = pos;
        this.world = world;
    }
    public void setPowered(boolean isPowered) {
        this.isPowered = isPowered;
    }
    public boolean isPowered() {
        return isPowered;
    }
}
