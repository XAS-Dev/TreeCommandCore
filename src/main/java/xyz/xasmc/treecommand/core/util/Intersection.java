package xyz.xasmc.treecommand.core.util;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class Intersection {
    /**
     * 获取玩家视线与方块的碰撞点
     *
     * @param player 玩家
     * @return 碰撞点
     */
    @Nullable
    public static Vector getIntersectionWithBlock(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector eyeDirection = eyeLocation.getDirection();

        RayTraceResult rayTraceResult = player.getWorld().rayTrace(eyeLocation, eyeDirection, 4, FluidCollisionMode.NEVER, true, 0, entity -> !(entity.equals(player)));
        if (rayTraceResult == null || rayTraceResult.getHitBlock() == null) return null;
        return rayTraceResult.getHitPosition();
    }

    /**
     * 获取玩家视线与实体的碰撞点
     *
     * @param player 玩家
     * @return 碰撞实体
     */
    @Nullable
    public static Entity getIntersectionWithEntity(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector eyeDirection = eyeLocation.getDirection();

        RayTraceResult rayTraceResult = player.getWorld().rayTrace(eyeLocation, eyeDirection, 4, FluidCollisionMode.NEVER, true, 0, entity -> !(entity.equals(player)));
        if (rayTraceResult == null || rayTraceResult.getHitEntity() == null) return null;
        return rayTraceResult.getHitEntity();
    }
}
