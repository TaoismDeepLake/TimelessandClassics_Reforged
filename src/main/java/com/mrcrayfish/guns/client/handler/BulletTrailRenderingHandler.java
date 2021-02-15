package com.mrcrayfish.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.client.BulletTrail;
import com.mrcrayfish.guns.client.GunRenderType;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.util.OptifineHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class BulletTrailRenderingHandler
{
    private static BulletTrailRenderingHandler instance;

    public static BulletTrailRenderingHandler get()
    {
        if(instance == null)
        {
            instance = new BulletTrailRenderingHandler();
        }
        return instance;
    }

    private Map<Integer, BulletTrail> bullets = new HashMap<>();

    private BulletTrailRenderingHandler() {}

    /**
     * Adds a bullet trail to render into the world
     *
     * @param trail the bullet trail get
     */
    public void add(BulletTrail trail)
    {
        // Prevents trails being added when not in a world
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            this.bullets.put(trail.getEntityId(), trail);
        }
    }

    /**
     * Removes the bullet for the given entity id.
     *
     * @param entityId the entity id of the bullet
     */
    public void remove(int entityId)
    {
        this.bullets.remove(entityId);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            if(event.phase == TickEvent.Phase.END)
            {
                this.bullets.values().forEach(BulletTrail::tick);
                this.bullets.values().removeIf(BulletTrail::isDead);
            }
        }
        else if(!this.bullets.isEmpty())
        {
            this.bullets.clear();
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        for(BulletTrail bulletTrail : this.bullets.values())
        {
            this.renderBulletTrail(bulletTrail, event.getMatrixStack(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void onRespawn(ClientPlayerNetworkEvent.RespawnEvent event)
    {
        this.bullets.clear();
    }

    @SubscribeEvent
    public void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        this.bullets.clear();
    }

    /**
     * @param bulletTrail
     * @param matrixStack
     * @param partialTicks
     */
    private void renderBulletTrail(BulletTrail bulletTrail, MatrixStack matrixStack, float partialTicks)
    {
        if(OptifineHelper.isShadersEnabled())
            return;

        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.getRenderViewEntity();
        if(entity == null || bulletTrail.isDead()) return;

        matrixStack.push();

        Vec3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
        Vec3d position = bulletTrail.getPosition();
        Vec3d motion = bulletTrail.getMotion();
        double bulletX = position.x + motion.x * partialTicks;
        double bulletY = position.y + motion.y * partialTicks;
        double bulletZ = position.z + motion.z * partialTicks;
        matrixStack.translate(bulletX - view.getX(), bulletY - view.getY(), bulletZ - view.getZ());

        matrixStack.rotate(Vector3f.YP.rotationDegrees(bulletTrail.getYaw()));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-bulletTrail.getPitch() + 90));

        Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z);
        float trailLength = (float) ((motionVec.length() / 3.0F) * bulletTrail.getTrailLengthMultiplier());
        float red = (float) (bulletTrail.getTrailColor() >> 16 & 255) / 255.0F;
        float green = (float) (bulletTrail.getTrailColor() >> 8 & 255) / 255.0F;
        float blue = (float) (bulletTrail.getTrailColor() & 255) / 255.0F;
        float alpha = 0.3F;

        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        IRenderTypeBuffer.Impl renderTypeBuffer = mc.getRenderTypeBuffers().getBufferSource();

        if(bulletTrail.isTrailVisible())
        {
            RenderType bulletType = GunRenderType.getBulletTrail();
            IVertexBuilder builder = renderTypeBuffer.getBuffer(bulletType);
            builder.pos(matrix4f, 0, 0, -0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0, 0, 0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0, -trailLength, 0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0, -trailLength, -0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, -0.035F, 0, 0).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0.035F, 0, 0).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0.035F, -trailLength, 0).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, -0.035F, -trailLength, 0).color(red, green, blue, alpha).endVertex();
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish(bulletType);
        }

        if(!bulletTrail.getItem().isEmpty())
        {
            matrixStack.rotate(Vector3f.YP.rotationDegrees((bulletTrail.getAge() + partialTicks) * (float) 50));
            matrixStack.scale(0.275F, 0.275F, 0.275F);

            int combinedLight = WorldRenderer.getCombinedLight(entity.world, new BlockPos(entity.getPositionVec()));
            ItemStack stack = bulletTrail.getItem();
            RenderType renderType = RenderTypeLookup.getRenderType(stack);
            RenderUtil.renderModel(stack, ItemCameraTransforms.TransformType.NONE, matrixStack, renderTypeBuffer, combinedLight, OverlayTexture.NO_OVERLAY);
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish(renderType);
        }

        matrixStack.pop();
    }
}
