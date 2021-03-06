package opengrave;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.List;

import static net.minecraft.util.MathHelper.floor_double;

public class PlayerDeathHandler {

    @SubscribeEvent
    public void onDeath(LivingDropsEvent event) {
        Entity entity = event.entity;
        if (entity instanceof EntityPlayer) {
            World world = entity.worldObj;
            int x = floor_double(entity.posX);
            int y = floor_double(entity.posY);
            y = y < 0 ? 1 : y;
            int z = floor_double(entity.posZ);

            while (y < world.getActualHeight() - 1 && !world.isAirBlock(x, y, z)) // Float up to the top of liquids/walls
                y++;

            while (y > 1 && (!world.isAirBlock(x, y, z) || world.isAirBlock(x, y - 1, z))) // Descend to the first valid position
                y--;

            List<ItemStack> itemStacks = ItemUtil.getItemStacks(event.drops);
            if (BlockGrave.spawnGraveBlock(world, x, y, z, itemStacks))
                event.setCanceled(true);
        }
    }
}
