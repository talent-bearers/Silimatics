package wiresegal.silimatics.common.block

import com.teamwizardry.librarianlib.common.base.block.IBlockColorProvider
import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType

/**
 * @author WireSegal
 * Created at 10:53 PM on 8/4/16.
 */
class BlockGlassPane(name: String) : BlockModPane(name, Material.GLASS, true, *EnumSandType.getSandTypeNamesFor(name)), IBlockColorProvider, ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity {
        return BlockGlass.SmedryGlassTile()
    }


    override val blockColorFunction: ((IBlockState, IBlockAccess?, BlockPos?, Int) -> Int)?
        get() = { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(BlockGlass.SAND_TYPE).glassColor }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].glassColor }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        super.breakBlock(worldIn, pos, state)
        worldIn.removeTileEntity(pos)
    }

    override fun eventReceived(state: IBlockState?, worldIn: World, pos: BlockPos?, eventID: Int, eventParam: Int): Boolean {
        val tileentity = worldIn.getTileEntity(pos)
        return tileentity?.receiveClientEvent(eventID, eventParam) ?: false
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess?, pos: BlockPos?): Int {
        return if (state.getValue(BlockGlass.SAND_TYPE) == EnumSandType.BRIGHT) 15 else 0
    }

    init {
        blockHardness = 0.3F
        soundType = SoundType.GLASS
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, BlockGlass.SAND_TYPE)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(BlockGlass.SAND_TYPE).ordinal
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(BlockGlass.SAND_TYPE, EnumSandType.values()[meta % EnumSandType.values().size])
    }

    override fun canPaneConnectTo(world: IBlockAccess, pos: BlockPos, dir: EnumFacing): Boolean {
        return super.canPaneConnectTo(world, pos, dir) || world.getBlockState(pos.offset(dir))?.block == ModBlocks.glass
    }

    override fun getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): MutableList<ItemStack>? {
        return if(state.getValue(BlockGlass.SAND_TYPE) == EnumSandType.VIEW) mutableListOf(ModItems.shard.getCommunicatorShardStack()) else super.getDrops(world, pos, state, fortune)
    }

    override val stateMapper: ((Block) -> Map<IBlockState, ModelResourceLocation>)?
        get() = { StateMap.Builder()
                .withName(BlockGlass.SAND_TYPE)
                .withSuffix("GlassPane").build().putStateModelLocations(it) }
}
