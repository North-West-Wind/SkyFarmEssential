package ml.northwestwind.skyfarm.events;

import ml.northwestwind.skyfarm.SkyFarm;
import ml.northwestwind.skyfarm.block.NaturalEvaporatorBlock;
import ml.northwestwind.skyfarm.entity.CompactBrickEntity;
import ml.northwestwind.skyfarm.item.CompactBrickItem;
import ml.northwestwind.skyfarm.item.WaterBowlItem;
import ml.northwestwind.skyfarm.recipes.AbstractEvaporatingRecipe;
import ml.northwestwind.skyfarm.recipes.serializer.EvaporatingRecipeSerializer;
import ml.northwestwind.skyfarm.tile.NaturalEvaporatorTileEntity;
import ml.northwestwind.skyfarm.world.SkyblockWorldType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = SkyFarm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(SkyFarm.MOD_ID)
public class RegistryEvents {

    @SubscribeEvent
    public static void registerWorldType(final RegistryEvent.Register<ForgeWorldType> event) {
        event.getRegistry().register(SkyblockWorldType.INSTANCE.setRegistryName("skyfarm"));
    }

    @SubscribeEvent
    public static void registerBlock(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(Blocks.NATURAL_EVAPORATOR);
    }

    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new BlockItem(Blocks.NATURAL_EVAPORATOR, new Item.Properties().tab(SkyFarm.SkyFarmItemGroup.INSTANCE).stacksTo(64)).setRegistryName("natural_evaporator"),
                Items.COMPACT_BRICK,
                Items.WATER_BOWL,
                Items.BOWL
        );
    }

    @SubscribeEvent
    public static void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityTypes.NATURAL_EVAPORATOR);
    }

    @SubscribeEvent
    public static void registerRecipeSerializer(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        Recipes.EVAPORATING.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerEntityType(final RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(EntityTypes.COMPACT_BRICK);
    }

    public static class Blocks {
        public static final Block NATURAL_EVAPORATOR = new NaturalEvaporatorBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()).setRegistryName("natural_evaporator");
    }

    public static class TileEntityTypes {
        public static final TileEntityType<NaturalEvaporatorTileEntity> NATURAL_EVAPORATOR = (TileEntityType<NaturalEvaporatorTileEntity>) TileEntityType.Builder.of(() -> {
            return new NaturalEvaporatorTileEntity();
        }, Blocks.NATURAL_EVAPORATOR).build(null).setRegistryName("natural_evaporator");
    }

    public static class Recipes<S extends IRecipeSerializer<? extends IRecipe<?>>> {
        public static final Recipes<EvaporatingRecipeSerializer> EVAPORATING = new Recipes<>(new EvaporatingRecipeSerializer(), AbstractEvaporatingRecipe.RECIPE_TYPE_ID);

        private static <T extends IRecipe<?>> IRecipeType<T> customType(ResourceLocation rl) {
            return Registry.register(Registry.RECIPE_TYPE, rl, new IRecipeType<T>() {
                public String toString() {
                    return rl.toString();
                }
            });
        }

        final ResourceLocation rl;
        IRecipeType<? extends IRecipe<?>> type = null;
        S serializer;

        private Recipes(S serializer, ResourceLocation rl) {
            this.serializer = serializer;
            this.rl = rl;
        }

        public S getSerializer() {
            return serializer;
        }

        @SuppressWarnings("unchecked")
        public <T extends IRecipeType<?>> T getType() {
            return (T) type;
        }

        public void register(IForgeRegistry<IRecipeSerializer<?>> registry) {
            if (type == null) type = customType(rl);

            registry.register(serializer.setRegistryName(rl));
        }
    }

    public static class EntityTypes {
        public static final EntityType<CompactBrickEntity> COMPACT_BRICK = (EntityType<CompactBrickEntity>) EntityType.Builder.<CompactBrickEntity>of((type, world) -> {
            return new CompactBrickEntity(type, world);
        }, EntityClassification.MISC).sized(0.25f, 0.25f).build("compact_brick_entity").setRegistryName("compact_brick_entity");
    }

    public static class Items {
        public static final Item COMPACT_BRICK = new CompactBrickItem(new Item.Properties().tab(SkyFarm.SkyFarmItemGroup.INSTANCE).stacksTo(16)).setRegistryName("compact_brick");
        public static final Item WATER_BOWL = new Item(new Item.Properties().stacksTo(1).tab(SkyFarm.SkyFarmItemGroup.INSTANCE)).setRegistryName("water_bowl");
        public static final Item BOWL = new WaterBowlItem(new Item.Properties().stacksTo(64).tab(SkyFarm.SkyFarmItemGroup.INSTANCE), true).setRegistryName("minecraft", "bowl");
    }
}
