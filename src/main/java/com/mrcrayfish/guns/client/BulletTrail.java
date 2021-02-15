package com.mrcrayfish.guns.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Author: MrCrayfish
 */
public class BulletTrail
{
    private int entityId;
    private Vec3d position;
    private Vec3d motion;
    private float yaw;
    private float pitch;
    private boolean dead;
    private ItemStack item;
    private int trailColor;
    private double trailLengthMultiplier;
    private int age;
    private int maxAge;
    private double gravity;
    private int shooterId;

    public BulletTrail(int entityId, Vec3d position, Vec3d motion, ItemStack item, int trailColor, double trailMultiplier, int maxAge, double gravity, int shooterId)
    {
        this.entityId = entityId;
        this.position = position;
        this.motion = motion;
        this.item = item;
        this.trailColor = trailColor;
        this.trailLengthMultiplier = trailMultiplier;
        this.maxAge = maxAge;
        this.gravity = gravity;
        this.shooterId = shooterId;
        this.updateYawPitch();
    }

    private void updateYawPitch()
    {
        float horizontalLength = MathHelper.sqrt(this.motion.x * this.motion.x + this.motion.z * this.motion.z);
        this.yaw = (float) Math.toDegrees(MathHelper.atan2(this.motion.x, this.motion.z));
        this.pitch = (float) Math.toDegrees(MathHelper.atan2(this.motion.y, (double) horizontalLength));
    }

    public void tick()
    {
        this.age++;

        this.position = this.position.add(this.motion);

        if(this.gravity != 0)
        {
            this.motion = this.motion.add(0, this.gravity, 0);
            this.updateYawPitch();
        }

        Entity entity = Minecraft.getInstance().getRenderViewEntity();
        double distance = entity != null ? Math.sqrt(entity.getDistanceSq(this.position)) : Double.MAX_VALUE;
        if(this.age >= this.maxAge || distance > 256)
        {
            this.dead = true;
        }
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public Vec3d getPosition()
    {
        return this.position;
    }

    public Vec3d getMotion()
    {
        return this.motion;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public boolean isDead()
    {
        return this.dead;
    }

    public int getAge()
    {
        return this.age;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public int getTrailColor()
    {
        return this.trailColor;
    }

    public double getTrailLengthMultiplier()
    {
        return this.trailLengthMultiplier;
    }

    public boolean isTrailVisible()
    {
        Entity entity = Minecraft.getInstance().getRenderViewEntity();
        return entity != null && entity.getEntityId() != this.shooterId;
    }

    @Override
    public int hashCode()
    {
        return this.entityId;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof BulletTrail)
        {
            return ((BulletTrail) obj).entityId == this.entityId;
        }
        return false;
    }
}
