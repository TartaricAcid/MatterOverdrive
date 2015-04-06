package com.MO.MatterOverdrive.tile;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import com.MO.MatterOverdrive.api.matter.IMatterHandler;
import com.MO.MatterOverdrive.api.matter.IMatterStorage;
import com.MO.MatterOverdrive.data.MatterStorage;
import cofh.lib.util.helpers.MathHelper;

public abstract class MOTileEntityMachineMatter extends MOTileEntityMachineEnergy implements IMatterHandler
{
	protected MatterStorage matterStorage;
	
	public MOTileEntityMachineMatter(int slotCount)
	{
        super(slotCount);
		matterStorage = new MatterStorage();
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt)
	{
		super.writeCustomNBT(nbt);
		matterStorage.writeToNBT(nbt);

	}
	
	@Override
	public void readCustomNBT(NBTTagCompound nbt)
	{
		super.readCustomNBT(nbt);
		matterStorage.readFromNBT(nbt);

	}
	
	@Override
	public int getMatterStored() {
		return this.matterStorage.getMatterStored();
	}

	@Override
	public int getMatterCapacity() {
		return this.matterStorage.getCapacity();
	}

	@Override
	public int receiveMatter(ForgeDirection side, int amount, boolean simulate)
    {
        int lastMatter = this.matterStorage.getMatterStored();
        int received = this.matterStorage.receiveMatter(side, amount, simulate);
        if(!simulate) {
            if (hasMatterEmptied(lastMatter, this.matterStorage.getMatterStored()))
                onMatterEmpty();
            else if (hasMatterFilled(lastMatter, this.matterStorage.getMatterStored()))
                onMatterFilled();
        }

        return received;
	}

	@Override
	public int extractMatter(ForgeDirection direction, int amount,
			boolean simulate)
    {
        int lastMatter = this.matterStorage.getMatterStored();
        int extracted = this.matterStorage.extractMatter(direction, amount, simulate);

        if(!simulate) {
            if (hasMatterEmptied(lastMatter, this.matterStorage.getMatterStored()))
                onMatterEmpty();
            else if (hasMatterFilled(lastMatter, this.matterStorage.getMatterStored()))
                onMatterFilled();
        }

		return extracted;
	}
	
	public IMatterStorage getMatterStorage()
	{
		return this.matterStorage;
	}

	public void setMatterStored(int matter)
	{
        if(hasMatterEmptied(this.matterStorage.getMatterStored(),matter))
            onMatterEmpty();
        else if(hasMatterEmptied(this.matterStorage.getMatterStored(),matter))
            onMatterFilled();

		this.matterStorage.setMatterStored(matter);
	}

    public void onMatterEmpty()
    {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void onMatterFilled()
    {
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }

    boolean hasMatterEmptied(int lastMatter, int newMatter)
    {
        return lastMatter > 0 && newMatter == 0;
    }

    boolean hasMatterFilled(int lastMatter,int newMatter)
    {
        return lastMatter == 0 && newMatter > 0;
    }

	@Override
	public void readFromPlaceItem(ItemStack itemStack)
	{
		super.readFromPlaceItem(itemStack);

		if(itemStack != null)
		{
			if(itemStack.hasTagCompound())
			{
				matterStorage.readFromNBT(itemStack.getTagCompound());
			}
		}
	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{
		super.writeToDropItem(itemStack);

		if(itemStack != null)
		{
			if(matterStorage.getMatterStored() > 0) {
				if (!itemStack.hasTagCompound())
					itemStack.setTagCompound(new NBTTagCompound());

				matterStorage.writeToNBT(itemStack.getTagCompound());
				itemStack.getTagCompound().setInteger("MaxMatter", matterStorage.getCapacity());
				itemStack.getTagCompound().setInteger("MatterSend", matterStorage.getMaxExtract());
				itemStack.getTagCompound().setInteger("MatterReceive",matterStorage.getMaxReceive());
			}
		}
	}
}