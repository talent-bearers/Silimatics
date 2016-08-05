package wiresegal.silimatics.common.core;

import com.google.common.base.Throwables;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * @author WireSegal
 *         Created at 9:54 AM on 8/5/16.
 */
public class SilimaticMethodHandles {
    @Nonnull
    private static final MethodHandle ticksAliveSetter;

    static {
        try {
            Field f = ReflectionHelper.findField(EntityFireball.class, "av", "field_70236_j", "ticksAlive");
            ticksAliveSetter = MethodHandles.publicLookup().unreflectSetter(f);
        } catch (Throwable t) {
            FMLLog.warning("Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static void setTicksAlive(@Nonnull EntityFireball fireball, int ticks) {
        try {
            ticksAliveSetter.invokeExact(fireball, ticks);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        FMLLog.warning("Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }
}
