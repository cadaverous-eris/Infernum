package infernum.common.blocks;

import infernum.Infernum;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPedestal extends Block implements ITileEntityProvider {

	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);

	public BlockPedestal() {
		super(Material.ROCK);
		this.setRegistryName("pedestal");
		this.setUnlocalizedName(Infernum.MODID + ".pedestal");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setCreativeTab(Infernum.tab);
		setSoundType(SoundType.STONE);
		setHardness(0.8F);
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName().toString(), "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TilePedestal.class, new TESRPedestal());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TilePedestal();
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TilePedestal) {
			ItemStack stack = ((TilePedestal) worldIn.getTileEntity(pos)).getStack();
			worldIn.removeTileEntity(pos);
			spawnAsEntity(worldIn, pos, stack);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {

		if (!world.isRemote && world.getTileEntity(pos) instanceof TilePedestal) {
			TilePedestal pedestal = (TilePedestal) world.getTileEntity(pos);
			if (pedestal.getStack().func_190926_b()) {
				if (!player.getHeldItem(hand).func_190926_b()) {
					pedestal.setStack(player.getHeldItem(hand));
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.field_190927_a);
					player.openContainer.detectAndSendChanges();
				}
			} else {
				ItemStack stack = pedestal.getStack();
				pedestal.setStack(ItemStack.field_190927_a);
				if (!player.inventory.addItemStackToInventory(stack)) {
					EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack);
					world.spawnEntity(entityItem);
				} else {
					player.openContainer.detectAndSendChanges();
				}
			}
		}

		return true;
	}

}
