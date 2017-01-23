package com.mrcrayfish.guns;

import com.mrcrayfish.guns.init.ModGuns;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabGun extends CreativeTabs
{
	public TabGun() 
	{
		super("tabCGM");
	}

	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(ModGuns.shotgun_ammo);
	}

}
