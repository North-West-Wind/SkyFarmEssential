package ml.northwestwind.skyfarm.events;

import ml.northwestwind.skyfarm.SkyFarm;
import ml.northwestwind.skyfarm.discord.Discord;
import ml.northwestwind.skyfarm.entity.CompactBrickEntity;
import ml.northwestwind.skyfarm.tile.renderer.NaturalEvaporatorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SkyFarm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(RegistryEvents.TileEntityTypes.NATURAL_EVAPORATOR, NaturalEvaporatorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RegistryEvents.EntityTypes.COMPACT_BRICK, manager -> {
            return new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer());
        });
        Discord.startup();
        Discord.updateRichPresence("Starting game...", "Mods are loading...", new Discord.DiscordImage("sky_farm", ""), null);
    }
}
