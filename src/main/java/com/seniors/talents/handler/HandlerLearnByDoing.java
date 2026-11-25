package com.seniors.talents.handler;

import com.seniors.talents.common.capability.AptitudeCapability;
import com.seniors.talents.network.packet.client.SyncAptitudeCapabilityCP;
import com.seniors.talents.network.packet.common.AptitudeLevelUpSP;
import com.seniors.talents.registry.RegistryAptitudes;
import com.seniors.talents.registry.aptitude.Aptitude;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Learn by doing: when learnByDoing is on, aptitudes level up from what the player does. Levels up to
 * learnByDoingMaxBoughtLevel can still be bought with XP as well ({@link #canBuyLevel}); above it, doing is the only way. Every source is measured in vanilla XP points (anything that drops XP in vanilla feeds the matching
 * aptitude with that much, times the rate in talents.learn-by-doing.json5), and a level needs as many points as
 * buying it with XP would cost ({@link AptitudeLevelUpSP#requiredPoints}), so the pace matches the old one.
 * <ul>
 *   <li>kills: the XP the mob actually drops (after Simple Difficulty blights, Champions...), split between
 *       attack, defense and agility depending on melee or ranged; Infernal Mobs elites add a bonus</li>
 *   <li>damage taken or blocked with a shield -> defense; magic damage dealt -> magic</li>
 *   <li>block XP (ores) and stone -> mining, logs -> gathering, grown crops -> farming, placing -> building</li>
 *   <li>fishing -> gathering, breeding -> farming, smelting -> farming/mining/building by what came out</li>
 *   <li>XP spent on enchanting, anvils and Golden Osmosis, brewing, and casting Wizardry spells from a wand
 *       ({@link com.seniors.talents.integration.WizardryLearnByDoing}) -> magic; sprinting and jumping -> agility</li>
 *   <li>crafting and workstations (anvil, smithing table, ... see {@link StationXp}) -> building</li>
 * </ul>
 * Every handler runs at LOWEST priority, so other mods' changes to the event (and cancels) come first.
 */
public final class HandlerLearnByDoing {

    public static final String ATTACK = "attack", DEFENSE = "defense", AGILITY = "agility", MINING = "mining",
            BUILDING = "building", GATHERING = "gathering", FARMING = "farming", MAGIC = "magic";

    // persistent-data key on a killed mob: true when the killing blow was a projectile
    private static final String RANGED_KILL = "talents_ranged_kill";
    private static final TagKey<Block> FORGE_STONE = BlockTags.create(new ResourceLocation("forge", "stone"));
    private static final TagKey<Block> FORGE_ORES = BlockTags.create(new ResourceLocation("forge", "ores"));
    private static final TagKey<Block> FORGE_NETHERRACK = BlockTags.create(new ResourceLocation("forge", "netherrack"));
    // smelting outputs that count as mining (anything else that isn't food counts as building)
    private static final TagKey<Item> FORGE_INGOTS = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "ingots"));
    private static final TagKey<Item> FORGE_GEMS = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "gems"));
    private static final TagKey<Item> FORGE_NUGGETS = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "nuggets"));

    // set while Talents itself spends XP (passives, Golden Osmosis), so that spend doesn't count as magic XP
    private static boolean spendingOnTalents;
    // players whose aptitude XP changed since the last sync (synced together every SYNC_TICKS)
    private static final Set<UUID> DIRTY = new HashSet<>();
    private static final int SYNC_TICKS = 10;
    // player -> position last tick, for measuring sprint distance
    private static final Map<UUID, Vec3> LAST_POS = new HashMap<>();

    private HandlerLearnByDoing() {}

    /** Learn by doing is on (synced to clients, so the screen can use this too). */
    public static boolean enabled() {
        return HandlerCommonConfig.HANDLER.instance().learnByDoing;
    }

    /**
     * Whether an aptitude at this level can still be bought up one level with XP: always with learn by doing off,
     * otherwise only while the level being bought is at most learnByDoingMaxBoughtLevel. Used by the level-up
     * packet (server) and the screen (client), so both agree.
     */
    public static boolean canBuyLevel(int currentLevel) {
        return !enabled() || currentLevel + 1 <= HandlerCommonConfig.HANDLER.instance().learnByDoingMaxBoughtLevel;
    }

    /**
     * Points of learn-by-doing XP an aptitude at this level needs for the next one. While the level can still be
     * bought, that's the buy cost times learnByDoingBuyableCostMultiplier: the XP orbs from the same actions can buy
     * it too, so those levels came about twice as fast as the ones above. Used by give() and the screen.
     */
    public static int doingPoints(int currentLevel) {
        int points = AptitudeLevelUpSP.requiredPoints(currentLevel);
        if (!canBuyLevel(currentLevel)) return points;
        return Math.round(points * Math.max(1.0F, HandlerCommonConfig.HANDLER.instance().learnByDoingBuyableCostMultiplier));
    }

    private static HandlerLearnByDoingConfig rates() {
        return HandlerLearnByDoingConfig.HANDLER.instance();
    }

    // ---------------------------------------------------------------- giving XP and levelling up

    /**
     * Gives {@code vanillaXp} vanilla XP points' worth of XP (times the config scale) toward an aptitude, levelling
     * it up as many times as the XP covers. Stops at the max aptitude level, and holds the XP at the next level's
     * cost while the player is at the global level limit (so it levels as soon as the limit is raised).
     */
    public static void give(ServerPlayer player, String aptitudeName, double vanillaXp) {
        if (!enabled() || vanillaXp <= 0 || player instanceof FakePlayer || player.isCreative() || player.isSpectator()) return;
        Aptitude aptitude = RegistryAptitudes.getAptitude(aptitudeName);
        AptitudeCapability capability = AptitudeCapability.get(player);
        if (aptitude == null || capability == null) return;

        HandlerCommonConfig common = HandlerCommonConfig.HANDLER.instance();
        int level = capability.getAptitudeLevel(aptitude);
        if (level >= common.aptitudeMaxLevel) return;

        double xp = capability.getAptitudeXp(aptitude) + vanillaXp * rates().scale;
        boolean leveled = false;
        while (level < common.aptitudeMaxLevel) {
            int needed = doingPoints(level);
            if (xp < needed) break;
            if (capability.getGlobalLevel() >= common.playersMaxGlobalLevel) {
                xp = needed;                 // can't level now: keep exactly enough for when the limit goes up
                break;
            }
            xp -= needed;
            level++;
            capability.setAptitudeLevel(aptitude, level);
            leveled = true;
            player.displayClientMessage(Component.translatable("message.talents.aptitude.level_up",
                    Component.translatable(aptitude.getKey()).withStyle(ChatFormatting.GOLD), level).withStyle(ChatFormatting.GREEN), false);
        }
        capability.setAptitudeXp(aptitude, level >= common.aptitudeMaxLevel ? 0 : xp);

        if (leveled) {
            // a level changes which skills and passives are available, so sync right away
            SyncAptitudeCapabilityCP.send(player);
            DIRTY.remove(player.getUUID());
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.6F, 1.2F);
        } else {
            DIRTY.add(player.getUUID());     // plain progress only needs to reach the screen eventually
        }
    }

    /** Sends the aptitude XP of players who gained some, every SYNC_TICKS instead of on every jump or block. */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || DIRTY.isEmpty()) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null || server.getTickCount() % SYNC_TICKS != 0) return;
        for (UUID id : DIRTY) {
            ServerPlayer player = server.getPlayerList().getPlayer(id);
            // A dead player stays in the player list until they respawn, but once removed (20 ticks after
            // dying) Forge has invalidated their capabilities. Skip them: the respawn sync sends everything.
            if (player != null && !player.isRemoved()) SyncAptitudeCapabilityCP.send(player);
        }
        DIRTY.clear();
    }

    // ---------------------------------------------------------------- kills

    /** Remembers whether the killing blow was ranged; the XP drop event that follows doesn't know the damage source. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event) {
        if (!enabled() || !(event.getSource().getEntity() instanceof ServerPlayer)) return;
        event.getEntity().getPersistentData().putBoolean(RANGED_KILL, event.getSource().is(DamageTypeTags.IS_PROJECTILE));
    }

    /**
     * The mob's final XP drop feeds attack/defense/agility. Simple Difficulty blights and Champions change the
     * drop through this same event at normal priority, so it's already in the amount here. Infernal Mobs instead
     * spawns its own extra orbs, so that is added on top.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onXpDrop(LivingExperienceDropEvent event) {
        if (!enabled() || !(event.getAttackingPlayer() instanceof ServerPlayer player)) return;
        LivingEntity mob = event.getEntity();
        if (mob instanceof Player) return;                          // no aptitude XP for killing players
        HandlerLearnByDoingConfig rates = rates();
        double xp = event.getDroppedExperience();
        if (isInfernal(mob)) xp += rates.infernalBonusXp;
        if (mob.getPersistentData().getBoolean(RANGED_KILL)) {
            give(player, AGILITY, xp * rates.rangedKillAgility);
            give(player, ATTACK, xp * rates.rangedKillAttack);
            give(player, DEFENSE, xp * rates.rangedKillDefense);
        } else {
            give(player, ATTACK, xp * rates.meleeKillAttack);
            give(player, DEFENSE, xp * rates.meleeKillDefense);
        }
    }

    // Infernal Mobs' "is this an elite" check, looked up once by reflection (null when Infernal Mobs isn't installed)
    private static Method infernalCheck;
    private static boolean infernalLooked;

    private static boolean isInfernal(LivingEntity mob) {
        if (!infernalLooked) {
            infernalLooked = true;
            try {
                infernalCheck = Class.forName("atomicstryker.infernalmobs.common.InfernalMobsCore")
                        .getMethod("getIsRareEntityOnline", LivingEntity.class);
            } catch (ReflectiveOperationException | LinkageError e) {
                infernalCheck = null;
            }
        }
        if (infernalCheck == null) return false;
        try {
            return (Boolean) infernalCheck.invoke(null, mob);
        } catch (ReflectiveOperationException | RuntimeException e) {
            return false;
        }
    }

    // ---------------------------------------------------------------- damage

    /** Final damage (after armor) taken from a mob or player -> defense; magic damage dealt by a player -> magic. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDamage(LivingDamageEvent event) {
        if (!enabled()) return;
        float amount = Math.min(event.getAmount(), event.getEntity().getHealth());   // overkill doesn't count
        Entity source = event.getSource().getEntity();
        if (event.getEntity() instanceof ServerPlayer player && source != null && source != player)
            give(player, DEFENSE, amount * rates().damageTakenDefense);
        if (source instanceof ServerPlayer attacker && event.getEntity() != attacker
                && event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO))   // vanilla's list of magic damage types
            give(attacker, MAGIC, amount * rates().magicDamageMagic);
    }

    /** Damage stopped by a shield -> defense. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (enabled() && event.getEntity() instanceof ServerPlayer player)
            give(player, DEFENSE, event.getBlockedDamage() * rates().shieldBlockDefense);
    }

    // ---------------------------------------------------------------- blocks

    /**
     * Block XP (ores, spawners; 0 with silk touch like vanilla) and stone -> mining, logs -> gathering, fully grown
     * crops -> farming. XP a crop drops goes to farming instead of mining.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!enabled() || !(event.getPlayer() instanceof ServerPlayer player)) return;
        HandlerLearnByDoingConfig rates = rates();
        BlockState state = event.getState();
        boolean grownCrop = isGrownCrop(state);
        give(player, grownCrop ? FARMING : MINING, event.getExpToDrop() * rates.blockXp);
        if (state.is(FORGE_STONE) || state.is(FORGE_ORES) || state.is(FORGE_NETHERRACK)
                || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.BASE_STONE_NETHER))
            give(player, MINING, rates.stoneMining);
        else if (state.is(BlockTags.LOGS))
            give(player, GATHERING, rates.logGathering);
        else if (grownCrop)
            give(player, FARMING, rates.cropFarming);
    }

    /** A crop at its last growth stage: vanilla-style crops, or anything in minecraft:crops with a maxed "age". */
    private static boolean isGrownCrop(BlockState state) {
        if (state.getBlock() instanceof CropBlock crop) return crop.isMaxAge(state);
        if (!state.is(BlockTags.CROPS)) return false;
        for (Property<?> property : state.getProperties()) {
            if (property instanceof IntegerProperty age && property.getName().equals("age"))
                return state.getValue(age) == age.getPossibleValues().stream().mapToInt(Integer::intValue).max().orElse(0);
        }
        return false;
    }

    /** Placing a block -> building. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (enabled() && event.getEntity() instanceof ServerPlayer player)
            give(player, BUILDING, rates().placeBuilding);
    }

    // ---------------------------------------------------------------- fishing, breeding, smelting, brewing

    /** A catch -> gathering, at vanilla's fishing XP (1 to 6, rolled here since the event doesn't carry it). */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFished(ItemFishedEvent event) {
        if (enabled() && event.getEntity() instanceof ServerPlayer player)
            give(player, GATHERING, (1 + player.getRandom().nextInt(6)) * rates().fishingGathering);
    }

    /** Breeding two animals -> farming, at vanilla's breeding XP (1 to 7). */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreed(BabyEntitySpawnEvent event) {
        if (enabled() && event.getCausedByPlayer() instanceof ServerPlayer player)
            give(player, FARMING, (1 + player.getRandom().nextInt(7)) * rates().breedingFarming);
    }

    /** Taking furnace output: the recipe's XP goes to farming (food), mining (ingots, gems, nuggets) or building. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (!enabled() || !(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack out = event.getSmelting();
        double xp = recipeXp(player, out) * out.getCount() * rates().smelting;
        if (out.isEdible()) give(player, FARMING, xp);
        else if (out.is(FORGE_INGOTS) || out.is(FORGE_GEMS) || out.is(FORGE_NUGGETS)) give(player, MINING, xp);
        else give(player, BUILDING, xp);
    }

    /** XP per item of the furnace, blast furnace or smoker recipe that makes this item (the highest if several). */
    private static float recipeXp(ServerPlayer player, ItemStack out) {
        var recipes = player.server.getRecipeManager();
        var access = player.server.registryAccess();
        float best = 0;
        for (RecipeType<? extends AbstractCookingRecipe> type : new RecipeType[]{RecipeType.SMELTING, RecipeType.BLASTING, RecipeType.SMOKING}) {
            for (AbstractCookingRecipe recipe : recipes.getAllRecipesFor(type)) {
                if (recipe.getResultItem(access).is(out.getItem())) best = Math.max(best, recipe.getExperience());
            }
        }
        return best;
    }

    /** Taking a potion from a brewing stand -> magic. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBrewed(PlayerBrewedPotionEvent event) {
        if (enabled() && event.getEntity() instanceof ServerPlayer player)
            give(player, MAGIC, rates().brewingMagic);
    }

    /** Crafting in a crafting table or the inventory grid -> building, per craft. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (enabled() && event.getEntity() instanceof ServerPlayer player)
            give(player, BUILDING, rates().craftBuilding);
    }

    // ---------------------------------------------------------------- levels spent

    /**
     * Levels spent on the enchanting table or an anvil (both go through giveExperienceLevels with a negative
     * amount, which fires this before the levels change) -> magic, as the vanilla XP points those levels were worth.
     * Ignored while Talents itself spends XP.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLevelChange(PlayerXpEvent.LevelChange event) {
        if (!enabled() || spendingOnTalents || event.getLevels() >= 0 || !(event.getEntity() instanceof ServerPlayer player)) return;
        int from = player.experienceLevel;
        int to = Math.max(0, from + event.getLevels());
        give(player, MAGIC, (totalXpForLevel(from) - totalXpForLevel(to)) * rates().levelsSpentMagic);
    }

    /**
     * Takes XP points for something Talents sells (passive levels). The levels this costs don't count as magic XP;
     * only enchanting, anvils and Golden Osmosis do.
     */
    public static void spendXpPoints(Player player, int points) {
        boolean was = spendingOnTalents;
        spendingOnTalents = true;
        try {
            player.giveExperiencePoints(-points);
        } finally {
            spendingOnTalents = was;
        }
    }

    /**
     * Takes XP points for a Golden Osmosis repair and counts exactly those points as magic XP (at the enchanting/
     * anvil rate). Golden Osmosis spends a few points at a time, which only rarely crosses a whole level, so the
     * level-change event alone would miss almost all of it; the spend itself is kept out of that event so it isn't
     * counted twice.
     */
    public static void spendXpPointsAsMagic(Player player, int points) {
        spendXpPoints(player, points);
        if (player instanceof ServerPlayer serverPlayer)
            give(serverPlayer, MAGIC, points * rates().levelsSpentMagic);
    }

    /** Total vanilla XP points needed to reach an XP level (Minecraft's own formula). */
    private static int totalXpForLevel(int level) {
        if (level <= 16) return level * level + 6 * level;
        if (level <= 31) return (int) (2.5 * level * level - 40.5 * level + 360);
        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }

    // ---------------------------------------------------------------- movement

    /** Distance sprinted on the ground -> agility (teleports and fast travel over 2 blocks a tick don't count). */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) return;
        Vec3 pos = player.position();
        Vec3 last = LAST_POS.put(player.getUUID(), pos);
        if (!enabled() || last == null || !player.isSprinting() || !player.onGround() || player.isPassenger()) return;
        double dx = pos.x - last.x, dz = pos.z - last.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        if (distance < 2) give(player, AGILITY, distance * rates().sprintAgility);
    }

    /** Each jump -> agility. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (enabled() && event.getEntity() instanceof ServerPlayer player)
            give(player, AGILITY, rates().jumpAgility);
    }

    /** Drops per-player bookkeeping on logout (the XP itself is saved in the capability). */
    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_POS.remove(event.getEntity().getUUID());
        DIRTY.remove(event.getEntity().getUUID());
    }
}
