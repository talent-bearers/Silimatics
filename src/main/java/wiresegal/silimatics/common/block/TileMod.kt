package wiresegal.silimatics.common.block

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer

/**
 * @author WireSegal
 * Created at 11:06 AM on 8/4/16.
 */
abstract class TileMod : TileEntity() {
    override fun shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean {
        return oldState.block !== newState.block
    }

    override fun writeToNBT(par1nbtTagCompound: NBTTagCompound): NBTTagCompound {
        writeCustomNBT(par1nbtTagCompound)
        super.writeToNBT(par1nbtTagCompound)

        return par1nbtTagCompound
    }

    override fun readFromNBT(par1nbtTagCompound: NBTTagCompound) {
        readCustomNBT(par1nbtTagCompound)
        super.readFromNBT(par1nbtTagCompound)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(super.getUpdateTag())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity {
        return SPacketUpdateTileEntity(pos, -999, updateTag)
    }

    open fun writeCustomNBT(cmp: NBTTagCompound) {
        // NO-OP
    }

    open fun readCustomNBT(cmp: NBTTagCompound) {
        // NO-OP
    }

    override fun markDirty() {
        super.markDirty()
        if(!worldObj.isRemote)
            dispatchTileToNearbyPlayers()
    }

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        super.onDataPacket(net, packet)
        readCustomNBT(packet.nbtCompound)
    }

    open fun dispatchTileToNearbyPlayers() {
        if(worldObj is WorldServer) {
            val ws: WorldServer = worldObj as WorldServer
            val packet: SPacketUpdateTileEntity = updatePacket

            for(player in ws.playerEntities) {
                val playerMP = player as EntityPlayerMP
                if (playerMP.getDistanceSq(getPos()) < 64 * 64
                        && ws.playerChunkMap.isPlayerWatchingChunk(playerMP, pos.x shr 4, pos.z shr 4)) {
                    playerMP.connection.sendPacket(packet);
                }
            }
        }
    }
}
