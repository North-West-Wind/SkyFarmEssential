package ml.northwestwind.skyfarm.events;


import ml.northwestwind.skyfarm.SkyFarm;
import ml.northwestwind.skyfarm.world.generators.SkyblockChunkGenerator;
import ml.northwestwind.skyfarm.world.data.SkyblockData;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyFarm.MOD_ID)
public class SkyblockEvents {
    @SubscribeEvent
    public static void playerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = player.getCommandSenderWorld();
        if (SkyblockChunkGenerator.isWorldSkyblock(world)) {
            SkyblockData data = SkyblockData.get((ServerWorld) world);
            if (!data.isWorldGenerated()) {
                generateIsland(world);
                data.setWorldGenerated(true);
            }
            if (data.isFirstSpawn(player.getUUID())) {
                player.teleportTo(0.5, 64, 0.5);
                player.setSleepingPos(new BlockPos(0.5, 64, 0.5));
                if (!world.isClientSide()) {
                    MinecraftServer server = ((ServerWorld) world).getServer();
                    Advancement advancement = server.getAdvancements().getAdvancement(new ResourceLocation(SkyFarm.MOD_ID, "skyfarm/root"));
                    if (advancement != null) {
                        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                        serverPlayer.getAdvancements().award(advancement, "JoinGame");
                    }
                }
                data.playerJoin(player);
            }
            data.setDirty();
            ((ServerWorld) world).getDataStorage().set(data);
        }
    }

    @SubscribeEvent
    public static void playerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = player.getCommandSenderWorld();
        if (SkyblockChunkGenerator.isWorldSkyblock(world)) {
            if (player.getSleepingPos().isPresent()) return;
            player.teleportTo(0.5, 64, 0.5);
            player.setSleepingPos(new BlockPos(0.5, 64, 0.5));
        }
    }

    private static void generateIsland(World world) {
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                BlockPos pos = new BlockPos(i, 63, j);
                world.setBlockAndUpdate(pos, Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos pos = new BlockPos(i, 62, j);
                world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            }
        }
        world.setBlockAndUpdate(new BlockPos(0, 63, 0), Blocks.WATER.defaultBlockState());
        world.setBlockAndUpdate(new BlockPos(1, 63, 0), Blocks.FARMLAND.defaultBlockState());
        world.setBlockAndUpdate(new BlockPos(1, 63, 1), Blocks.FARMLAND.defaultBlockState());
        world.setBlockAndUpdate(new BlockPos(0, 63, 1), Blocks.FARMLAND.defaultBlockState());
        world.setBlockAndUpdate(new BlockPos(-1, 64, -1), Blocks.OAK_SAPLING.defaultBlockState());
    }

    @SubscribeEvent
    public static void playerTick(final TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player != null && player.getY() <= -60 && !player.level.isClientSide) player.teleportTo(player.getX(), 320, player.getZ());
    }

    @SubscribeEvent
    public static void playerFall(final LivingFallEvent event) {
        if (event.getDistance() <= 4) event.setDamageMultiplier(0);
    }
}
