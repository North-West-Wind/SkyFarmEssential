package ml.northwestwind.skyfarm.common.world.generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ml.northwestwind.skyfarm.SkyFarm;
import ml.northwestwind.skyfarm.common.world.SeedHolder;
import ml.northwestwind.skyfarm.misc.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.function.Supplier;

public class SkyblockMiningChunkGenerator extends ChunkGenerator {
    public static final Codec<SkyblockMiningChunkGenerator> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
                    BiomeProvider.CODEC.fieldOf("biome_source")
                            .forGetter((chunkGenerator) -> chunkGenerator.biomeSource),
                    Codec.LONG.fieldOf("seed")
                            .orElseGet(SeedHolder::getSeed)
                            .forGetter((chunkGenerator) -> chunkGenerator.seed),
                    DimensionSettings.CODEC.fieldOf("settings")
                            .forGetter((chunkGenerator) -> chunkGenerator.settings))
                    .apply(instance, instance.stable(SkyblockMiningChunkGenerator::new)));

    private final long seed;
    private final Supplier<DimensionSettings> settings;

    public static void init() {
        Registry.register(Registry.CHUNK_GENERATOR, Utils.prefix("mining"), CODEC);
    }

    public SkyblockMiningChunkGenerator(BiomeProvider provider, long seed, Supplier<DimensionSettings> settings) {
        super(provider, provider, settings.get().structureSettings(), seed);
        this.seed = seed;
        this.settings = settings;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long newSeed) {
        return new SkyblockMiningChunkGenerator(this.biomeSource.withSeed(newSeed), newSeed, this.settings);
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {}

    @Override
    public void fillFromNoise(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {}

    @Override
    public void applyCarvers(long p_230350_1_, BiomeManager p_230350_3_, IChunk p_230350_4_, GenerationStage.Carving p_230350_5_) {}

    @Override
    public int getBaseHeight(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_) {
        return 0;
    }

    @Override
    public IBlockReader getBaseColumn(int p_230348_1_, int p_230348_2_) {
        return new Blockreader(new BlockState[0]);
    }
}
