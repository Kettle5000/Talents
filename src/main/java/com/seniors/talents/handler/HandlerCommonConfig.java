package com.seniors.talents.handler;

import com.google.gson.GsonBuilder;
import com.seniors.talents.config.Configuration;
import com.seniors.talents.config.StringListGroup;
import com.seniors.talents.Talents;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandlerCommonConfig {
    @SuppressWarnings("removal")
	public static ConfigClassHandler<HandlerCommonConfig> HANDLER = ConfigClassHandler.createBuilder(HandlerCommonConfig.class)
            .id(new ResourceLocation(Talents.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(Configuration.getAbsoluteDirectory().resolve("talents.common.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();
    // General options
    @SerialEntry(comment = "Aptitudes Max Level")
    @AutoGen(category = "common", group = "general")
    @IntField(min = 2, max = 1000)
    public int aptitudeMaxLevel = 32;

    @SerialEntry(comment = "Global max level, the global level is calculated summing all aptitudes level, so if this is set to 32 players will be able to only maximize 1 skill.")
    @AutoGen(category = "common", group = "general")
    @IntField(min = 32, max = 99999)
    public int playersMaxGlobalLevel = 256;

    @SerialEntry(comment = "First aptitudes level cost")
    @AutoGen(category = "common", group = "general")
    @IntField(min = 1, max = 1000)
    public int aptitudeFirstCostLevel = 1;

    @SerialEntry(comment = "Learn by doing: aptitudes also level up from what you do (killing, mining, farming, crafting...). Rates are in talents.learn-by-doing.json5. Passives still cost XP.")
    @AutoGen(category = "common", group = "general")
    @Boolean(formatter = Boolean.Formatter.ON_OFF)
    public boolean learnByDoing = false;

    @SerialEntry(comment = "With learn by doing on: aptitude levels up to this one can still be bought with XP like before (what you do counts toward them too). Levels above it only come from what you do. 0 = no levels can be bought.")
    @AutoGen(category = "common", group = "general")
    @IntField(min = 0, max = 1000)
    public int learnByDoingMaxBoughtLevel = 16;

    @SerialEntry(comment = "With learn by doing on: levels that can still be bought (up to learnByDoingMaxBoughtLevel) need this many times their usual points from what you do, since the XP orbs from the same things can buy them too. 1.0 = the same as the levels above.")
    @AutoGen(category = "common", group = "general")
    @FloatField(min = 1.0f, max = 100.0f)
    public float learnByDoingBuyableCostMultiplier = 1.0f;

    @SerialEntry(comment = "Show potions overlay over skills")
    @AutoGen(category = "common", group = "general")
    @Boolean(formatter = Boolean.Formatter.ON_OFF)
    public boolean showPotionsHud = true;

    @SerialEntry(comment = "If true, locked items will be automatically dropped from player hands")
    @AutoGen(category = "common", group = "general")
    @Boolean(formatter = Boolean.Formatter.ON_OFF)
    public boolean dropLockedItems = false;

    @SerialEntry(comment = "If true this will display the player titles as prefixes when a player chat.")
    @AutoGen(category = "common", group = "general")
    @Boolean(formatter = Boolean.Formatter.TRUE_FALSE)
    public boolean displayTitlesAsPrefix = true;

    // Passive options
    @SerialEntry(comment = "Attack Damage passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float attackDamageValue = 0.5f;

    @SerialEntry(comment = "Attack damage passive levels. Don't modify the length of the array!")
    public int[] attackPassiveLevels = new int[]{ 28, 28, 29, 29, 30, 30, 31, 31, 32, 32};

	@SerialEntry(comment = "Attack Damage passive costs. DO NOT CHANGE THE LENGTH unless you know what you are doing")
	public int[] attackdamagecosts = new int[] {11, 17, 23, 29, 37, 41, 43, 47, 51, 53};
    //move critical chance here

    @SerialEntry(comment = "Movement Speed passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float movementSpeedValue = 0.05f;

    @SerialEntry(comment = "Movement speed passive levels. Don't modify the length of the array!")
    public int[] movementSpeedPassiveLevels = new int[]{28, 29, 30, 31, 32};
    
    @SerialEntry(comment = "Movement Speed passive costs. DO NOT CHANGE THE LENGTH unless you know what you are doing")
	public int[] movementspeedcosts = new int[]{11, 23, 37, 43, 51};

    @SerialEntry(comment = "Projectile speed passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float projectileSpeedValue = 0.5f;

    @SerialEntry(comment = "Projectile speed passive levels. Don't modify the length of the array!")
    public int[] projectileSpeedPassiveLevels = new int[]{24, 26, 28, 30, 32};

    @SerialEntry(comment = "Projectile speed passive levels cost. MUST be same length that the array above")
	public int[] projectilespeedcosts = new int[]{11, 17, 29, 37, 41};
    
    @SerialEntry(comment = "Block Reach passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float blockReachValue = 1.5f;

    @SerialEntry(comment = "Block reach passive levels. Don't modify the length of the array!")
    public int[] blockReachPassiveLevels = new int[]{8, 14, 20, 26, 32};
    
    @SerialEntry(comment = "Block Reach passive costs. DO NOT CHANGE THE LENGTH unless you know what you are doing")
    public int[] blockreachcosts = new int[]{11, 17, 29, 37, 41};
    

    @SerialEntry(comment = "Break Speed passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float breakSpeedValue = 0.5f;

    @SerialEntry(comment = "Break speed passive levels. Don't modify the length of the array!")
    public int[] breakSpeedPassiveLevels = new int[]{8, 14, 20, 26, 32};
    
    @SerialEntry(comment = "Break Speed passive costs. DO NOT CHANGE THE LENGTH unless you know what you are doing")
    public int[] breakSpeedcosts = new int[]{11, 23, 37, 43, 51};

    @SerialEntry(comment = "Critical Chance passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 1.0f)
    public float criticalChanceValue = 0.25f;

    @SerialEntry(comment = "Critical Chance passive levels. Don't modify the length of the array!")
    public int[] criticalChancePassiveLevels = new int[]{5, 8, 11, 14, 17, 20, 23, 26, 29, 32};
    
    @SerialEntry(comment = "Critical damage passive costs. DO NOT CHANGE THE LENGTH unless you know what you are doing")
    public int[] criticalChancecosts = new int[]{5, 10, 15, 8, 16, 24, 32, 40, 48, 56};

    @SerialEntry(comment = "Luck passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float luckValue = 2.0f;

    @SerialEntry(comment = "Luck passive levels. Don't modify the length of the array!")
    public int[] luckPassiveLevels = new int[]{28, 28, 29, 29, 30, 30, 31, 31, 32, 32};
    
    @SerialEntry(comment = "Luck passive costs. DO NOT CHANGE THE LENGTH unless you know what you are doing")
    public int[] luckpassivecosts = new int[]{16, 20, 24, 28, 32, 36, 40, 44, 48, 52};

    @SerialEntry(comment = "Damage reduction passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 0.99f)
    public float damagereduction = 0.25f;

    @SerialEntry(comment = "Damage reduction passive levels. Careful when modifying the length of the array!")
    public int[] damagereductionlevels = new int[]{28, 29, 30, 31, 32};
    
    @SerialEntry(comment = "Damage reduction passive levels cost. MUST be same length that the array above")
    public int[] damagereductioncosts = new int[]{11, 23, 37, 43, 51};
    
    @SerialEntry(comment = "Shield Proficiency passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 1f)
    public float shieldproficiency = 0.1f;

    @SerialEntry(comment = "Shield Proficiency passive levels. Careful when modifying the length of the array!")
    public int[] shieldproficiencylevels = new int[]{24, 26, 28, 30, 32};
    
    @SerialEntry(comment = "Shield Proficiency passive levels cost. MUST be same length that the array above")
	public int[] shieldproficiencycosts = new int[] {11, 17, 29, 37, 41};
    
    @SerialEntry(comment = "Stealth range detection passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 0.99f)
    public float stealthvalue = 0.75f;

    @SerialEntry(comment = "Stealth range passive levels. Careful when modifying the length of the array!")
    public int[] stealthLevels = new int[]{28, 28, 29, 29, 30, 30, 31, 31, 32, 32};
    
	@SerialEntry(comment = "Stealth range costs. MUST be same length that the array above")
	public int[] stealthcosts = new int[] {5, 7, 11, 13, 17, 23, 29, 31, 37, 41};

    @SerialEntry(comment = "Cooking speed passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 0.99f)
    public float cookingspeedvalue = 0.5f;

    @SerialEntry(comment = "Cooking speed passive levels. Careful when modifying the length of the array!")
    public int[] cookingspeedLevels = new int[]{28, 29, 30, 31, 32};
    
    @SerialEntry(comment = "Cooking speed costs. MUST be same length that the array above")
    public int[] cookingspeedcosts = new int[]{5, 10, 15, 20, 25};
    
    @SerialEntry(comment = "Smelting speed passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 0.99f)
    public float smeltingspeedvalue = 0.5f;
    
    @SerialEntry(comment = "Smelting speed passive levels. Careful when modifying the length of the array!")
    public int[] smeltingspeedLevels = new int[]{28, 29, 30, 31, 32};
    
    @SerialEntry(comment = "Smelting speed costs. MUST be same length that the array above")
    public int[] smeltingspeedcosts = new int[]{5, 10, 15, 20, 25};
    
    @SerialEntry(comment = "Mineral Efficiency passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 0.99f)
    public float mineralefficiencyvalue = 0.1f;
    
    @SerialEntry(comment = "Mineral Efficiency passive levels. Careful when modifying the length of the array!")
    public int[] mineralefficiencyLevels = new int[]{28, 29, 30, 31, 32};
    
    @SerialEntry(comment = "Mineral Efficiency costs. MUST be same length that the array above")
    public int[] mineralefficiencycosts = new int[]{5, 10, 15, 20, 25};
    
    @SerialEntry(comment = "Green Thumb passive value at max level, its a chance for each second, by default is 0.5%.")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 100f)
    public float greenthumbchance = 10.0f;
    
    @SerialEntry(comment = "Green Thumb passive range at max level, by default is 8.")
    @FloatField(min = 0.0f, max = 32f)
    public float greenthumbrange = 8f;
    
    @SerialEntry(comment = "Mineral Efficiency passive levels. Careful when modifying the length of the array!")
    public int[] greenthumbLevels = new int[]{23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
    
    @SerialEntry(comment = "Mineral Efficiency costs. MUST be same length that the array above")
	public int[] greenthumbcosts = new int[] {5, 7, 11, 13, 17, 23, 29, 31, 37, 41};
    
    @SerialEntry(comment = "Fishing Proficiency passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 1.0f)
    public float fishingproficiencyvalue = 0.1f;

    @SerialEntry(comment = "Flint finder passive levels. Careful when modifying the length of the array!")
    public int[] fishingproficiencyLevels = new int[]{6, 8, 10, 12, 14, 16, 18, 20, 22, 24};
    
    @SerialEntry(comment = "Flint finder costs. MUST be same length that the array above")
    public int[] fishingproficiencycosts = new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    
    @SerialEntry(comment = "Mana Regen passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 16f)
    public float manaRegenvalue = 1.0f;

    @SerialEntry(comment = "Mana Regen passive levels. Careful when modifying the length of the array!")
    public int[] manaRegenLevels = new int[]{28, 28, 29, 29, 30, 30, 31, 31, 32, 32};
    
	@SerialEntry(comment = "Mana Regen costs. MUST be same length that the array above")
	public int[] manaRegencosts = new int[] {5, 7, 11, 13, 17, 23, 29, 31, 37, 41};
   
	@SerialEntry(comment = "Wood Chopping passive value at max level")
    @AutoGen(category = "common", group = "passives")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float woodChoppingValue = 0.5f;

    @SerialEntry(comment = "Wood Chopping passive levels. Don't modify the length of the array!")
    public int[] woodChoppingPassiveLevels = new int[]{8, 14, 20, 26, 32};
    
    @SerialEntry(comment = "Wood Chopping passive costs. DO NOT CHANGE THE LENGTH unless you know what you are doing")
    public int[] woodChoppingcosts = new int[]{11, 23, 37, 43, 51};
	
    // Skills options
    @SerialEntry(comment = "One Handed skill attack speed amplifier increase")
    @AutoGen(category = "common", group = "skills")
    @FloatField(min = 0.0f, max = 10000.0f)
    public float oneHandedAmplifier = 0.5f;

    @SerialEntry(comment = "Fighting Spirit skill strength potion effect boost")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 1, max = 255)
    public int fightingSpiritBoost = 0;

    @SerialEntry(comment = "Fighting Spirit skill strength potion effect duration")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 1, max = 3600)
    public int fightingSpiritDuration = 3;

    @SerialEntry(comment = "Berserker skill health percent")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 0, max = 100)
    public int berserkerPercent = 30;

    @SerialEntry(comment = "Athletics skill air modifier multiply")
    @AutoGen(category = "common", group = "skills")
    @FloatField(min = 0.0f, max = 500.0f)
    public float athleticsModifier = 1.5f;

    @SerialEntry(comment = "Quick Reposition skill speed potion effect boost")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 1, max = 255)
    public int quickRepositionBoost = 2;

    @SerialEntry(comment = "Quick Reposition skill speed potion effect duration")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 1, max = 3600)
    public int quickRepositionDuration = 3;

    @SerialEntry(comment = "Haggler skill villager trades cost percent reduced")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 0, max = 100)
    public int hagglerPercent = 20;

    @SerialEntry(comment = "Bounty Hunter skill chance")
    @AutoGen(category = "common", group = "skills")
    @FloatField(min = 0.0f, max = 1.0f)
    public float bountyHunterchance = 0.02f;
    
    @SerialEntry(comment = "Bounty Hunter max emeralds per trigger")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 0, max = 8)
    public int bountyHunterMaxEmeralds = 4;
    
    @SerialEntry(comment = "This allows Bounty hunter to work only and only on infernal mobs, blights (simple difficulty) and champions")
    @Boolean
    public boolean bountyhunterCompat = false;

    @SerialEntry(comment = "Obsidian Smasher skill obsidian breaking speed modifier multiply")
    @AutoGen(category = "common", group = "skills")
    @FloatField(min = 0.0f, max = 100.0f)
    public float obsidianSmasherModifier = 10.0f;

    @SerialEntry(comment = "Treasure Hunter skill probability chance to get a treasure in dirt")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 1, max = 10000)
    public int treasureHunterProbability = 500;

    @SerialEntry(comment = "Treasure Hunter skill treasures item list")
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class)
    public List<String> treasureHunterItemList = Arrays.asList("minecraft:flint", "minecraft:clay_ball", "trashList[minecraft:feather;minecraft:bone_meal]", "lostToolList[minecraft:stick;minecraft:wooden_pickaxe{Damage:59};minecraft:wooden_shovel{Damage:59};minecraft:wooden_axe{Damage:59}]", "discList[minecraft:music_disc_13;minecraft:music_disc_cat;minecraft:music_disc_blocks;minecraft:music_disc_chirp;minecraft:music_disc_far;minecraft:music_disc_mall;minecraft:music_disc_mellohi;minecraft:music_disc_stal;minecraft:music_disc_strad;minecraft:music_disc_ward;minecraft:music_disc_11;minecraft:music_disc_wait]", "seedList[minecraft:beetroot_seeds;minecraft:wheat_seeds;minecraft:pumpkin_seeds;minecraft:melon_seeds;minecraft:brown_mushroom;minecraft:red_mushroom]", "mineralList[minecraft:raw_iron;minecraft:raw_gold;minecraft:raw_copper;minecraft:coal;minecraft:charcoal]");

    @SerialEntry(comment = "Convergence skill probability chance to obtain part of the spent material")
    @AutoGen(category = "common", group = "skills")
    @IntField(min = 1, max = 10000)
    public int convergenceProbability = 8;
    
    // Skill Levels
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int oneHandedRequiredLevel = 8;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int fightingSpiritRequiredLevel = 16;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int berserkerRequiredLevel = 32;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int athleticsRequiredLevel = 24;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int quickRepositionRequiredLevel = 16;
    
    @SerialEntry(comment = "Required level to unlock Undershirt skill")
    @IntField(min = 1)
    public int undershirtRequiredLevel = 16; 

    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int snowWalkerRequiredLevel = 8;

    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int scholarRequiredLevel = 8;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int hagglerRequiredLevel = 16;

    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int obsidianSmasherRequiredLevel = 16;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int treasureHunterRequiredLevel = 32;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int convergenceRequiredLevel = 32;
    @SerialEntry(comment = "Required level to unlock skill")
    @IntField(min = 1)
    public int safePortRequiredLevel = 16;

    @SerialEntry(comment = "Required level to unlock Survival Instincts skill")
    @IntField(min = 1)
    public int survivalinstinctsLevel = 32;
    
    @SerialEntry(comment = "Survival Instincts chance to deviate damage.")
    @FloatField(min = 0.0f, max = 1.0f)
    public float survivalinstinctschance = 0.10f;
    
    @SerialEntry(comment = "Required level to unlock Ledge grab skill")
    @IntField(min = 1)
    public int ledgeGrabRequiredLevel = 8;
    
    @SerialEntry(comment = "Required level to unlock sprinter skill")
    @IntField(min = 1)
    public int sprinterRequiredLevel = 32;
    
    @SerialEntry(comment = "Required level to unlock dodge skill.")
    @IntField(min = 1)
    public int sidestepRequiredLevel = 4;
    
    @SerialEntry(comment = "Dodge skill cooldown in ticks.")
    @IntField(min = 1)
    public int sidestepCooldown = 20;
    
    @SerialEntry(comment = "Dodge skill push power.")
    @FloatField(min = 0.0f, max = 1.0f)
    public float sidestepPower = 1.25f;

    @SerialEntry(comment = "Required level to unlock Lucky Fisherman skill")
    @IntField(min = 1)
    public int luckyfishermanRequiredLevel = 8;
    
    @SerialEntry(comment = "Required level to unlock Bountiful Harvest skill")
    @IntField(min = 1)
    public int bountifulharvestRequiredLevel = 8;
    
    @SerialEntry(comment = "Bountiful Harvest chance of getting 1 extra crops.")
    @FloatField(min = 0.0f, max = 0.99f)
    public float bountifulharvestchance = 0.25f;
    
    @SerialEntry(comment = "This fixes an issue related with quark mod and harvesting blocks with right click. If on, right clicking will work with Bountiful Harvest skill.")
    @Boolean
    public boolean bountifulharvestfix = false;
    
    @SerialEntry(comment = "Required level to unlock Hungry Farmer skill")
    @IntField(min = 1)
    public int hungryfarmerRequiredLevel = 32;
    
    @SerialEntry(comment = "Blacklisted Items from instantly eating.")
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class)
    public List<String> hungryfarmerBlackList = Arrays.asList("");
    
    @SerialEntry(comment = "Required level to unlock Drop Guaranteed skill")
    @IntField(min = 1)
    public int dropGuaranteedRequiredLevel = 24;
    
    @SerialEntry(comment = "Required level to unlock Bounty Hunter skill")
    @IntField(min = 1)
    public int bountyHunterRequiredLevel = 32;
    
    @SerialEntry(comment = "Required level to unlock Fossil Digger skill")
    @IntField(min = 1)
    public int fossilDiggerRequiredLevel = 8;
    
    @SerialEntry(comment = "Fossil Digger chance to mine 1 extra coal.")
    @FloatField(min = 0.0f, max = 1.0f)
    public float fossilDiggerchance = 0.1f;
    
    @SerialEntry(comment = "Required level to unlock Perfect Recovery skill")
    @IntField(min = 1)
    public int perfectRecoveryRequiredLevel = 8;
    
    @SerialEntry(comment = "Required level to unlock Golden Osmosis skill")
    @IntField(min = 1)
    public int goldenosmosisRequiredLevel = 24;

    @SerialEntry(comment = "Extra items Golden Osmosis treats as golden (vanilla gold tools and armor always count). Entries: \"modid:item\", \"modid:prefix*\" (every item of that mod whose name starts with prefix) or \"#modid:tag\" (an item tag).")
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class)
    public List<String> goldenOsmosisExtraItems = new ArrayList<>();
    
    @SerialEntry(comment = "Required level to unlock Teleport Disruption skill")
    @IntField(min = 1)
    public int teleportDisruptionRequiredLevel = 32;
    
    @SerialEntry(comment = "Required level to unlock Transmutation skill")
    @IntField(min = 1)
    public int transmutationRequiredLevel = 24;
    
    @SerialEntry(comment = "Transmutation skill item")
    @StringField
    public String transmutationItem = "minecraft:chorus_fruit";
    
    @SerialEntry(comment = "Transmutation skill list of transmutations.")
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class)
    public List<String> transmutationItemList = Arrays.asList(
    	    // Basic pairs
    	    "minecraft:stone;minecraft:end_stone",
    	    "minecraft:end_stone;minecraft:stone",
    	    "minecraft:pumpkin;minecraft:melon",
    	    "minecraft:melon;minecraft:pumpkin",
    	    "minecraft:prismarine;minecraft:dark_prismarine",
    	    "minecraft:dark_prismarine;minecraft:prismarine",
    	    "minecraft:wet_sponge;minecraft:sponge",
    	    "minecraft:hardened_clay;minecraft:clay",
    	    "minecraft:magma_block;minecraft:ice",

    	    // Sapling cycle
    	    "minecraft:oak_sapling;minecraft:spruce_sapling",
    	    "minecraft:spruce_sapling;minecraft:birch_sapling",
    	    "minecraft:birch_sapling;minecraft:jungle_sapling",
    	    "minecraft:jungle_sapling;minecraft:acacia_sapling",
    	    "minecraft:acacia_sapling;minecraft:dark_oak_sapling",
    	    "minecraft:dark_oak_sapling;minecraft:oak_sapling",

    	    // Wool color cycle
    	    "minecraft:white_wool;minecraft:orange_wool",
    	    "minecraft:orange_wool;minecraft:magenta_wool",
    	    "minecraft:magenta_wool;minecraft:light_blue_wool",
    	    "minecraft:light_blue_wool;minecraft:yellow_wool",
    	    "minecraft:yellow_wool;minecraft:lime_wool",
    	    "minecraft:lime_wool;minecraft:pink_wool",
    	    "minecraft:pink_wool;minecraft:gray_wool",
    	    "minecraft:gray_wool;minecraft:light_gray_wool",
    	    "minecraft:light_gray_wool;minecraft:cyan_wool",
    	    "minecraft:cyan_wool;minecraft:purple_wool",
    	    "minecraft:purple_wool;minecraft:blue_wool",
    	    "minecraft:blue_wool;minecraft:brown_wool",
    	    "minecraft:brown_wool;minecraft:green_wool",
    	    "minecraft:green_wool;minecraft:red_wool",
    	    "minecraft:red_wool;minecraft:black_wool",
    	    "minecraft:black_wool;minecraft:white_wool",

    	    // Carpet color cycle
    	    "minecraft:white_carpet;minecraft:orange_carpet",
    	    "minecraft:orange_carpet;minecraft:magenta_carpet",
    	    "minecraft:magenta_carpet;minecraft:light_blue_carpet",
    	    "minecraft:light_blue_carpet;minecraft:yellow_carpet",
    	    "minecraft:yellow_carpet;minecraft:lime_carpet",
    	    "minecraft:lime_carpet;minecraft:pink_carpet",
    	    "minecraft:pink_carpet;minecraft:gray_carpet",
    	    "minecraft:gray_carpet;minecraft:light_gray_carpet",
    	    "minecraft:light_gray_carpet;minecraft:cyan_carpet",
    	    "minecraft:cyan_carpet;minecraft:purple_carpet",
    	    "minecraft:purple_carpet;minecraft:blue_carpet",
    	    "minecraft:blue_carpet;minecraft:brown_carpet",
    	    "minecraft:brown_carpet;minecraft:green_carpet",
    	    "minecraft:green_carpet;minecraft:red_carpet",
    	    "minecraft:red_carpet;minecraft:black_carpet",
    	    "minecraft:black_carpet;minecraft:white_carpet",

    	    // Stained glass color cycle
    	    "minecraft:white_stained_glass;minecraft:orange_stained_glass",
    	    "minecraft:orange_stained_glass;minecraft:magenta_stained_glass",
    	    "minecraft:magenta_stained_glass;minecraft:light_blue_stained_glass",
    	    "minecraft:light_blue_stained_glass;minecraft:yellow_stained_glass",
    	    "minecraft:yellow_stained_glass;minecraft:lime_stained_glass",
    	    "minecraft:lime_stained_glass;minecraft:pink_stained_glass",
    	    "minecraft:pink_stained_glass;minecraft:gray_stained_glass",
    	    "minecraft:gray_stained_glass;minecraft:light_gray_stained_glass",
    	    "minecraft:light_gray_stained_glass;minecraft:cyan_stained_glass",
    	    "minecraft:cyan_stained_glass;minecraft:purple_stained_glass",
    	    "minecraft:purple_stained_glass;minecraft:blue_stained_glass",
    	    "minecraft:blue_stained_glass;minecraft:brown_stained_glass",
    	    "minecraft:brown_stained_glass;minecraft:green_stained_glass",
    	    "minecraft:green_stained_glass;minecraft:red_stained_glass",
    	    "minecraft:red_stained_glass;minecraft:black_stained_glass",
    	    "minecraft:black_stained_glass;minecraft:white_stained_glass",

    	    // Stained glass pane color cycle
    	    "minecraft:white_stained_glass_pane;minecraft:orange_stained_glass_pane",
    	    "minecraft:orange_stained_glass_pane;minecraft:magenta_stained_glass_pane",
    	    "minecraft:magenta_stained_glass_pane;minecraft:light_blue_stained_glass_pane",
    	    "minecraft:light_blue_stained_glass_pane;minecraft:yellow_stained_glass_pane",
    	    "minecraft:yellow_stained_glass_pane;minecraft:lime_stained_glass_pane",
    	    "minecraft:lime_stained_glass_pane;minecraft:pink_stained_glass_pane",
    	    "minecraft:pink_stained_glass_pane;minecraft:gray_stained_glass_pane",
    	    "minecraft:gray_stained_glass_pane;minecraft:light_gray_stained_glass_pane",
    	    "minecraft:light_gray_stained_glass_pane;minecraft:cyan_stained_glass_pane",
    	    "minecraft:cyan_stained_glass_pane;minecraft:purple_stained_glass_pane",
    	    "minecraft:purple_stained_glass_pane;minecraft:blue_stained_glass_pane",
    	    "minecraft:blue_stained_glass_pane;minecraft:brown_stained_glass_pane",
    	    "minecraft:brown_stained_glass_pane;minecraft:green_stained_glass_pane",
    	    "minecraft:green_stained_glass_pane;minecraft:red_stained_glass_pane",
    	    "minecraft:red_stained_glass_pane;minecraft:black_stained_glass_pane",
    	    "minecraft:black_stained_glass_pane;minecraft:white_stained_glass_pane",

    	    // Terracotta color cycle (stained clay)
    	    "minecraft:white_terracotta;minecraft:orange_terracotta",
    	    "minecraft:orange_terracotta;minecraft:magenta_terracotta",
    	    "minecraft:magenta_terracotta;minecraft:light_blue_terracotta",
    	    "minecraft:light_blue_terracotta;minecraft:yellow_terracotta",
    	    "minecraft:yellow_terracotta;minecraft:lime_terracotta",
    	    "minecraft:lime_terracotta;minecraft:pink_terracotta",
    	    "minecraft:pink_terracotta;minecraft:gray_terracotta",
    	    "minecraft:gray_terracotta;minecraft:light_gray_terracotta",
    	    "minecraft:light_gray_terracotta;minecraft:cyan_terracotta",
    	    "minecraft:cyan_terracotta;minecraft:purple_terracotta",
    	    "minecraft:purple_terracotta;minecraft:blue_terracotta",
    	    "minecraft:blue_terracotta;minecraft:brown_terracotta",
    	    "minecraft:brown_terracotta;minecraft:green_terracotta",
    	    "minecraft:green_terracotta;minecraft:red_terracotta",
    	    "minecraft:red_terracotta;minecraft:black_terracotta",
    	    "minecraft:black_terracotta;minecraft:white_terracotta"
    	);
    
    	@SerialEntry(comment = "Mob Effect that denies dodge.")
    	@StringField
    	public String dodgeDenyEffect = "";
    
}
