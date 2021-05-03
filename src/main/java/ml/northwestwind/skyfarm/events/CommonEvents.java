package ml.northwestwind.skyfarm.events;

import ml.northwestwind.skyfarm.SkyFarm;
import ml.northwestwind.skyfarm.packet.SkyFarmPacketHandler;
import ml.northwestwind.skyfarm.world.generators.SkyblockChunkGenerator;
import ml.northwestwind.skyfarm.world.generators.SkyblockNetherChunkGenerator;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = SkyFarm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        SkyblockChunkGenerator.init();
        SkyblockNetherChunkGenerator.init();
        SkyFarmPacketHandler.registerPackets();
    }
}
