package Cssey.macacus.urbanus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Cssey.macacus.urbanus.entity.MonkeyEntity;
import Cssey.macacus.urbanus.foods.CigarreteFoodComponent;
import Cssey.macacus.urbanus.spawns.SpawnInit;

public class MacacusUrbanus implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("macacusurbanus");

	//Items
	public static final Item CIGARRETE = new Item(new Item.Settings().group(ItemGroup.MISC));
	public static final Item CIGARRETE_TOBACCO = new Item(new Item.Settings().group(ItemGroup.MISC).food(CigarreteFoodComponent.CIGARRETE_TOBACCO));
	public static final Item CIGARRETE_WEED = new Item(new Item.Settings().group(ItemGroup.MISC).food(CigarreteFoodComponent.CIGARRETE_WEED));
	public static final Item BELIAL_FRAGMENT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Item BELIAL_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));

	
	//Blocks
	public static final Block ATIZAP = new Block(FabricBlockSettings.of(Material.METAL).strength(2,30).sounds(BlockSoundGroup.METAL).breakByTool(FabricToolTags.AXES));

	//Ores
	public static final Block BELIAL_ORE = new BelialOreBlock(FabricBlockSettings.copy(Blocks.STONE));
	private static ConfiguredFeature<?,?> BELIAL_ORE_OVERWORLD = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,BELIAL_ORE.getDefaultState(),6))
	.decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0,0,80))).spreadHorizontally().repeat(9);


	//Entitys
	public static final EntityType<MonkeyEntity> MONKEY = Registry.register(Registry.ENTITY_TYPE, new Identifier("macacusurbanus","monkey"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MonkeyEntity:: new).dimensions(EntityDimensions.fixed(0.6f, 1.45f)).build());

	//Sounds
	public static final Identifier MONKEY_SOUND_HURT = new Identifier("macacusurbanus:monkey_sound_hurt");
	public static SoundEvent MONKEY_SOUND_HURT_EVENT = new SoundEvent(MONKEY_SOUND_HURT);

	public static final Identifier MONKEY_SOUND_AMBIENT = new Identifier("macacusurbanus:monkey_sound_ambient");
	public static SoundEvent MONKEY_SOUND_AMBIENT_EVENT = new SoundEvent(MONKEY_SOUND_AMBIENT);
	

	@Override
	public void onInitialize() {
		//Registry Items
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus","cigarrete"), CIGARRETE);
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus","cigarrete_tobacco"), CIGARRETE_TOBACCO);
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus","cigarrete_weed"), CIGARRETE_WEED);
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus", "belial_fragment"), BELIAL_FRAGMENT);
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus", "belial_ingot"), BELIAL_INGOT);


		//Registry Blocks
		Registry.register(Registry.BLOCK, new Identifier("macacusurbanus", "atizap"), ATIZAP);
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus", "atizap"), new BlockItem(ATIZAP, new Item.Settings().group(ItemGroup.MISC)));

		Registry.register(Registry.BLOCK, new Identifier("macacusurbanus", "belial_ore"), BELIAL_ORE);
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus", "belial_ore"), new BlockItem(BELIAL_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		RegistryKey<ConfiguredFeature<?,?>> belialOreOverworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("macacusurbanus", "belial_ore"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, belialOreOverworld.getValue(), BELIAL_ORE_OVERWORLD);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,belialOreOverworld);


		//Tools
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus", "belial_pickaxe"), new PickaxeBase( new ToolMaterialBelial()));
		Registry.register(Registry.ITEM, new Identifier("macacusurbanus", "belial_sword"), new SwordBase(new ToolMaterialBelial()));


		//Entityes
		FabricDefaultAttributeRegistry.register(MONKEY, MonkeyEntity.createMobAttributes());
		SpawnInit.init();

		//Sounds
		Registry.register(Registry.SOUND_EVENT, MacacusUrbanus.MONKEY_SOUND_HURT, MONKEY_SOUND_HURT_EVENT);
		Registry.register(Registry.SOUND_EVENT, MacacusUrbanus.MONKEY_SOUND_AMBIENT, MONKEY_SOUND_AMBIENT_EVENT);
		
	}
}
