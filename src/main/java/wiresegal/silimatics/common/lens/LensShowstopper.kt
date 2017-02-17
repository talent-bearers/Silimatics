package wiresegal.silimatics.common.lens

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import com.teamwizardry.librarianlib.common.util.RaycastUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIAttackMelee
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.ReflectionHelper
import wiresegal.silimatics.api.lens.ILens
import java.util.*





/**
 * Created by Elad on 2/17/2017.
 */
object LensShowstopper : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        val entity = RaycastUtils.getEntityLookedAt(player) as? EntityLiving? ?: return
        val range = 20

        val mobs = player.worldObj.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range), Predicates.instanceOf(IMob::class.java))
        if (mobs.size > 1) {
            brainwashEntity(entity, mobs.map { it as IMob })
        }
    }

    fun brainwashEntity(entity: EntityLiving, mobs: List<IMob>): Boolean {
        val target = entity.attackTarget
        var did = false

        if (target == null || target !is IMob) {
            var newTarget: IMob
            do
                newTarget = mobs[entity.worldObj.rand.nextInt(mobs.size)]
            while (newTarget === entity)

            if (newTarget is EntityLiving) {
                val entries = ArrayList(entity.tasks.taskEntries)
                entries.addAll(ArrayList(entity.targetTasks.taskEntries))

                for (entry in entries)
                    if (entry.action is EntityAINearestAttackableTarget<*>) {
                        messWithGetTargetAI(entry.action as EntityAINearestAttackableTarget<*>, newTarget)
                        did = true
                    } else if (entry.action is EntityAIAttackMelee) {
                        did = true
                    }

                if (did)
                    entity.attackTarget = newTarget
            }
        }

        return did
    }

    val TARGET_CLASS = arrayOf("targetClass", "field_75307_b", "a")
    val TARGET_ENTITY_SELECTOR = arrayOf("targetEntitySelector", "field_82643_g", "c")
    private fun messWithGetTargetAI(aiEntry: EntityAINearestAttackableTarget<*>, target: EntityLivingBase) {
        ReflectionHelper.setPrivateValue<EntityAINearestAttackableTarget<*>, Class<Entity>>(EntityAINearestAttackableTarget::class.java, aiEntry, Entity::class.java, *TARGET_CLASS)
        ReflectionHelper.setPrivateValue<EntityAINearestAttackableTarget<*>, Predicate<EntityLivingBase>>(EntityAINearestAttackableTarget::class.java, aiEntry, Predicates.equalTo(target), *TARGET_ENTITY_SELECTOR) // todo 1.8 will this leak `target`?
    }
}