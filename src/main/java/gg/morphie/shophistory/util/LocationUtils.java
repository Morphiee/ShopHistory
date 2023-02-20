package gg.morphie.shophistory.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.maxgamer.quickshop.api.QuickShopAPI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationUtils {

    private static final List<Material> damagingBlocks = new ArrayList<>();
    private static final List<Material> nonSuffocatingBlocks = new ArrayList<>();
    private static final int BELOW_SAFE_BLOCK_CHECK_LIMIT = 20;

    static {
        // Initializing Damaging blocks
        damagingBlocks.add(Material.LAVA);
        damagingBlocks.add(Material.CACTUS);
        damagingBlocks.add(Material.CAMPFIRE);
        damagingBlocks.add(Material.SOUL_CAMPFIRE);
        damagingBlocks.add(Material.MAGMA_BLOCK);
        damagingBlocks.add(Material.FIRE);
        damagingBlocks.add(Material.SOUL_FIRE);
        damagingBlocks.add(Material.SWEET_BERRY_BUSH);
        damagingBlocks.add(Material.WITHER_ROSE);
        damagingBlocks.add(Material.END_PORTAL);

        // Initializing Non Suffocating blocks
        nonSuffocatingBlocks.add(Material.AIR);
        // Glass
        nonSuffocatingBlocks.add(Material.GLASS);
        nonSuffocatingBlocks.addAll(
                new ArrayList<>(Arrays.asList(Material.values()).stream().filter(p -> p.toString().toLowerCase().contains("_glass")).toList())
        );
        // Glass Panes
        nonSuffocatingBlocks.addAll(
                new ArrayList<>(Arrays.asList(Material.values()).stream().filter(p -> p.toString().toLowerCase().contains("glass_pane")).toList())
        );
        // Leaves
        nonSuffocatingBlocks.add(Material.ACACIA_LEAVES);
        nonSuffocatingBlocks.add(Material.BIRCH_LEAVES);
        nonSuffocatingBlocks.add(Material.DARK_OAK_LEAVES);
        nonSuffocatingBlocks.add(Material.JUNGLE_LEAVES);
        nonSuffocatingBlocks.add(Material.OAK_LEAVES);
        nonSuffocatingBlocks.add(Material.SPRUCE_LEAVES);
        if(Bukkit.getServer().getVersion().contains("1.17")) {
            nonSuffocatingBlocks.add(Material.AZALEA_LEAVES);
            nonSuffocatingBlocks.add(Material.FLOWERING_AZALEA_LEAVES);
        }
        // Slabs
        nonSuffocatingBlocks.addAll(
                new ArrayList<>(Arrays.asList(Material.values()).stream().filter(p -> p.toString().toLowerCase().contains("_slab")).toList())
        );
        // Walled Signs
        nonSuffocatingBlocks.addAll(
                new ArrayList<>(Arrays.asList(Material.values()).stream().filter(p -> p.toString().toLowerCase().contains("_sign")).toList())
        );
        // Stairs
        nonSuffocatingBlocks.addAll(
                new ArrayList<>(Arrays.asList(Material.values()).stream().filter(p -> p.toString().toLowerCase().contains("_stairs")).toList())
        );
        // MISC
        nonSuffocatingBlocks.add(Material.HONEY_BLOCK);
        nonSuffocatingBlocks.add(Material.BELL);
        nonSuffocatingBlocks.add(Material.CHEST);
        nonSuffocatingBlocks.add(Material.TRAPPED_CHEST);
        nonSuffocatingBlocks.add(Material.HOPPER);
        nonSuffocatingBlocks.add(Material.COMPOSTER);
        nonSuffocatingBlocks.add(Material.GRINDSTONE);
        nonSuffocatingBlocks.add(Material.STONECUTTER);
        nonSuffocatingBlocks.add(Material.IRON_BARS);
        nonSuffocatingBlocks.add(Material.END_PORTAL_FRAME);
        nonSuffocatingBlocks.add(Material.PISTON_HEAD);

    }

    @Nullable
    public static Location findSafeLocationAroundShop(Location shopLocation) {
        Location roundedShopLoc = getRoundedDestination(shopLocation);

        // Creating a list of four block locations in 4 sides of the shop
        List<Location> possibleSafeLocList = new ArrayList<>();
        possibleSafeLocList.add(new Location(
                roundedShopLoc.getWorld(),
                roundedShopLoc.getX() + 1,
                roundedShopLoc.getY(),
                roundedShopLoc.getZ()
        ));
        possibleSafeLocList.add(new Location(
                roundedShopLoc.getWorld(),
                roundedShopLoc.getX() - 1,
                roundedShopLoc.getY(),
                roundedShopLoc.getZ()
        ));
        possibleSafeLocList.add(new Location(
                roundedShopLoc.getWorld(),
                roundedShopLoc.getX(),
                roundedShopLoc.getY(),
                roundedShopLoc.getZ() + 1
        ));
        possibleSafeLocList.add(new Location(
                roundedShopLoc.getWorld(),
                roundedShopLoc.getX(),
                roundedShopLoc.getY(),
                roundedShopLoc.getZ() - 1
        ));
        for(Location loc_i : possibleSafeLocList) {
            if(loc_i.getBlock().getType().equals(new GetQuickShop().getShopSignMaterial())) {
                // check if the block above is suffocating
                Location blockAbove = new Location(
                        loc_i.getWorld(),
                        loc_i.getBlockX(),
                        loc_i.getBlockY() + 1,
                        loc_i.getBlockZ());
                if(!isBlockSuffocating(blockAbove)) {
                    Location blockBelow = null;
                    boolean safeLocFound = false;
                    for(int i = 1; i <= BELOW_SAFE_BLOCK_CHECK_LIMIT; i++) {
                        blockBelow = new Location(
                                loc_i.getWorld(),
                                loc_i.getBlockX(),
                                loc_i.getBlockY() - i,
                                loc_i.getBlockZ()
                        );
                        if(blockBelow.getBlock().getType().equals(Material.AIR)
                                || blockBelow.getBlock().getType().equals(Material.CAVE_AIR)
                                || blockBelow.getBlock().getType().equals(Material.VOID_AIR)
                                || blockBelow.getBlock().getType().equals(new GetQuickShop().getShopSignMaterial())) {
                            // do nothing and let the loop run
                        }
                        else if(!isBlockDamaging(blockBelow)) {
                            safeLocFound = true;
                            break;
                        }
                        else {
                            break;
                        }
                    }
                    if(safeLocFound) {
                        loc_i = lookAt(getRoundedDestination(new Location(
                                blockBelow.getWorld(),
                                blockBelow.getX(),
                                blockBelow.getY() + 1,
                                blockBelow.getZ()
                        )), roundedShopLoc);
                        return loc_i;
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return null;
                }
            }
            else {
            }
        }
        return null;
    }

    // Found the below function from this thread: https://bukkit.org/threads/lookat-and-move-functions.26768/
    private static Location lookAt(Location loc, Location lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }

    private static boolean isBlockDamaging(Location loc) {
        return damagingBlocks.contains(loc.getBlock().getType());
    }

    private static boolean isBlockSuffocating(Location loc) {
        return !nonSuffocatingBlocks.contains(loc.getBlock().getType());
    }

    private static Location getRoundedDestination(final Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int)Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, (double)x + 0.5D, y, (double)z + 0.5D, loc.getYaw(), loc.getPitch());
    }
}
