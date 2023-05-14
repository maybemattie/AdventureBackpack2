package com.darkona.adventurebackpack.entity.ai;

import java.util.UUID;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

import com.darkona.adventurebackpack.reference.BackpackTypes;
import com.darkona.adventurebackpack.util.Wearing;

public class EntityAIHorseFollowOwner extends EntityAIBase {

    private final EntityHorse theHorse;
    private EntityPlayer theOwner;
    final World theWorld;
    private final double speed;
    private PathNavigate petPathfinder;
    private int tickCounter;
    final float maxDist;
    final float minDist;
    private boolean avoidWater;

    public EntityAIHorseFollowOwner(EntityHorse horse, double speed, float minDist, float maxDist) {
        theHorse = horse;
        theWorld = horse.worldObj;
        theOwner = theWorld.func_152378_a(UUID.fromString(theHorse.func_152119_ch()));
        this.speed = speed * 2;
        petPathfinder = horse.getNavigator();
        this.minDist = minDist;
        this.maxDist = maxDist;
    }

    public double getDistanceSquaredToOwner() {
        double relX = theHorse.posX - theOwner.posX;
        double relY = theHorse.posY - theOwner.posY;
        double relZ = theHorse.posZ - theOwner.posZ;
        return relX * relX + relY * relY + relZ * relZ;
    }

    @Override
    public boolean shouldExecute() {
        if (!theHorse.isTame() || theHorse.getLeashed() || !theHorse.hasCustomNameTag()) return false;
        if (theOwner == null) {
            theOwner = theWorld.func_152378_a(UUID.fromString(theHorse.func_152119_ch()));
            if (theOwner == null) {
                return false;
            }
        }
        if (!Wearing.isWearingTheRightBackpack(theOwner, BackpackTypes.HORSE)) return false;
        return !(theHorse.getDistanceSqToEntity(theOwner) < minDist * minDist * 20);
    }

    @Override
    public boolean continueExecuting() {
        return (Wearing.isWearingTheRightBackpack(theOwner, BackpackTypes.HORSE) && !this.petPathfinder.noPath()
                && theHorse.getDistanceSqToEntity(theOwner) > this.maxDist * this.maxDist * 2);
    }

    @Override
    public void startExecuting() {
        tickCounter = 0;
        avoidWater = this.theHorse.getNavigator().getAvoidsWater();
        petPathfinder = theHorse.getNavigator();
    }

    @Override
    public void resetTask() {
        theOwner = null;
        petPathfinder.clearPathEntity();
        theHorse.getNavigator().setAvoidsWater(this.avoidWater);
    }

    /**
     * This function is likely unfinished.
     */
    @Override
    public void updateTask() {
        if (--tickCounter <= 0) {
            tickCounter = 10;
            if (!theHorse.getLeashed()) {
                if (!petPathfinder.tryMoveToEntityLiving(theOwner, speed)) {
                    return;
                }
            }
        }
    }
}
