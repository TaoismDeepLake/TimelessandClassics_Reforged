package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.util.GunEnchantmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Mr. Pineapple
 */
public class glock_17_animation implements IOverrideModel {

    //The render method, similar to what is in DartEntity. We can render the item
    /*@Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {

        //matrices.translate(0.01, 0.1, -0.1);
        //matrices.rotate(Vector3f.YP.rotationDegrees(-0.5F));

        *//*
            // So this area will be tested for the item specific name, allowing the use of custom attachments
        if(Gun.getAttachment(IAttachment.Type.BARREL,stack).getItem().getName() != "")
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_SUPPRESSOR_OVERIDE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        *//*

        GunItem gunItem = ((GunItem) stack.getItem());

        if(Gun.getAttachment(IAttachment.Type.BARREL,stack).getItem() == ModItems.SILENCER.get())
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_SUPPRESSOR_OVERIDE.getModel(), stack, matrices, renderBuffer, light, overlay);
            //Gun.getAttachment(IAttachment.Type.BARREL, GunItem.getItemById(Item.getIdFromItem(stack.getItem())).getDefaultInstance())
        }

        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0)
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        RenderUtil.renderModel(SpecialModels.GLOCK_17.getModel(), stack, matrices, renderBuffer, light, overlay);

            //Always push
            matrices.push();

            CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());
         

        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            if(GunEnchantmentHelper.getRate(stack, gunItem.getGun()) <= 1 && cooldownOg != 0)
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
            else
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            }
            else
            {
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
            }
        }
        //matrices.translate(0.00, 0.0, -0.1);
        RenderUtil.renderModel(SpecialModels.GLOCK_17_SLIDE.getModel(), stack, matrices, renderBuffer, light, overlay);

            //Always pop
            matrices.pop();
    }*/

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {

        //matrices.translate(0.01, 0.1, -0.1);
        //matrices.rotate(Vector3f.YP.rotationDegrees(-0.5F));

        /*
            // So this area will be tested for the item specific name, allowing the use of custom attachments
        if(Gun.getAttachment(IAttachment.Type.BARREL,stack).getItem().getName() != "")
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_SUPPRESSOR_OVERIDE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        */

        if(ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GUI) && Config.CLIENT.quality.reducedGuiWeaponQuality.get())
        {
            matrices.push();
            matrices.rotate(Vector3f.XP.rotationDegrees(-60.0F));
            matrices.rotate(Vector3f.YP.rotationDegrees(225.0F));
            matrices.rotate(Vector3f.ZP.rotationDegrees(-90.0F));
            matrices.translate(0.9,0,0);
            matrices.scale(1.5F,1.5F,1.5F);
            RenderUtil.renderModel(stack, stack, matrices, renderBuffer, light, overlay);
            matrices.pop();
            return;
        }

        GunItem gunItem = ((GunItem) stack.getItem());

        if(Gun.getAttachment(IAttachment.Type.PISTOL_BARREL,stack).getItem() == ModItems.PISTOL_SILENCER.get())
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0)
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        RenderUtil.renderModel(SpecialModels.GLOCK_17.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always push
        matrices.push();

        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());


        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            if(GunEnchantmentHelper.getRate(stack, gunItem.getGun()) <= 1 && cooldownOg != 0)
            {
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0);
            }
            else
            {
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
            }
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
            }
            else
            {
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0);
            }
        }
        RenderUtil.renderModel(SpecialModels.GLOCK_17_SLIDE.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always pop
        matrices.pop();
    }
     

    //TODO comments
}
