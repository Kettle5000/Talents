package com.seniors.talents.network.packet.client;

import com.seniors.talents.handler.HandlerCommonConfig;
import com.seniors.talents.handler.HandlerConvergenceItemsConfig;
import com.seniors.talents.network.ServerNetworking;
import com.seniors.talents.utils.ResourceLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

/**
 * Client packet to update static config options that needs to match server.
 * Must only be sent on player join.
 */
public class CommonConfigSyncCP {

    private final int aptitudeFirstCostLevel;
    private final boolean dropLockedItems;
    private final boolean displayTitlesAsPrefix;
    private final boolean learnByDoing;
    private final int learnByDoingMaxBoughtLevel;
    private final float learnByDoingBuyableCostMultiplier;

    // Passive
    private final float attackDamageValue;
    private final float movementSpeedValue;
    private final float projectileSpeedValue;
    private final float blockReachValue;
    private final float breakSpeedValue;
    private final float criticalChanceValue;
    private final float luckValue;
    private final float damagereduction;
    private final float shieldproficiency;
    private final float stealthvalue;
    private final float cookingspeedvalue;
    private final float smeltingspeedvalue;
    private final float mineralefficiencyvalue;
    private final float greenthumbchance;
    private final float greenthumbrange;
    private final float fishingproficiencyvalue;
    private final float manaRegenvalue;
    private final float woodChoppingValue;

    // Skils
    private final float oneHandedAmplifier;
    private final int fightingSpiritBoost;
    private final int fightingSpiritDuration;
    private final int berserkerPercent;
    private final float athleticsModifier;
    private final int quickRepositionBoost;
    private final int quickRepositionDuration;
    private final int hagglerPercent;
    private final float obsidianSmasherModifier;
    private final float bountyHunterchance;
    private final int bountyHunterMaxEmeralds;
    private final int treasureHunterProbability;
    private final List<String> treasureHunterItemList;
    private final int convergenceProbability;
    private final List<String> convergenceItemList;
    private final float survivalinstinctschance;
    private final float bountifulharvestchance;
    private final boolean bountifulharvestfix;
    private final float fossilDiggerchance;
    private final List<String> hungryfarmerBlackList;
    private final String transmutationItem;
    private final List<String> transmutationItemList;
    private final int sidestepCooldown;
    private final float sidestepPower;
    private final String dodgeDenyEffect;

    public CommonConfigSyncCP() {
    	var config = HandlerCommonConfig.HANDLER.instance();
        // General
        aptitudeFirstCostLevel = config.aptitudeFirstCostLevel;
        dropLockedItems = config.dropLockedItems;
        displayTitlesAsPrefix = config.displayTitlesAsPrefix;
        learnByDoing = config.learnByDoing;
        learnByDoingMaxBoughtLevel = config.learnByDoingMaxBoughtLevel;
        learnByDoingBuyableCostMultiplier = config.learnByDoingBuyableCostMultiplier;

        // Passives
        attackDamageValue = config.attackDamageValue;
        movementSpeedValue = config.movementSpeedValue;
        projectileSpeedValue = config.projectileSpeedValue;
        blockReachValue = config.blockReachValue;
        breakSpeedValue = config.breakSpeedValue;
        criticalChanceValue = config.criticalChanceValue;
        luckValue = config.luckValue;
        damagereduction = config.damagereduction;
        shieldproficiency = config.shieldproficiency;
        stealthvalue = config.stealthvalue;
        cookingspeedvalue = config.cookingspeedvalue;
        smeltingspeedvalue = config.smeltingspeedvalue;
        mineralefficiencyvalue = config.mineralefficiencyvalue;
        greenthumbchance = config.greenthumbchance;
        greenthumbrange = config.greenthumbrange;
        fishingproficiencyvalue = config.fishingproficiencyvalue;
        manaRegenvalue = config.manaRegenvalue;
        woodChoppingValue = config.woodChoppingValue;

        // Skills
        oneHandedAmplifier = config.oneHandedAmplifier;
        fightingSpiritBoost = config.fightingSpiritBoost;
        fightingSpiritDuration = config.fightingSpiritDuration;
        berserkerPercent = config.berserkerPercent;
        athleticsModifier = config.athleticsModifier;
        quickRepositionBoost = config.quickRepositionBoost;
        quickRepositionDuration = config.quickRepositionDuration;
        hagglerPercent = config.hagglerPercent;
        obsidianSmasherModifier = config.obsidianSmasherModifier;

        bountyHunterchance = config.bountyHunterchance;
        bountyHunterMaxEmeralds = config.bountyHunterMaxEmeralds;

        treasureHunterProbability = config.treasureHunterProbability;
        treasureHunterItemList = config.treasureHunterItemList;

        convergenceProbability = config.convergenceProbability;
        convergenceItemList = HandlerConvergenceItemsConfig.HANDLER.instance().convergenceItemList;

        // Extra skills
        survivalinstinctschance = config.survivalinstinctschance;
        bountifulharvestchance = config.bountifulharvestchance;
        bountifulharvestfix = config.bountifulharvestfix;
        fossilDiggerchance = config.fossilDiggerchance;

        // Lists / misc
        hungryfarmerBlackList = config.hungryfarmerBlackList;
        transmutationItem = config.transmutationItem;
        transmutationItemList = config.transmutationItemList;
        sidestepCooldown = config.sidestepCooldown;
        sidestepPower = config.sidestepPower;
        dodgeDenyEffect = config.dodgeDenyEffect;
    }

    public CommonConfigSyncCP(FriendlyByteBuf buffer) {
        // General
        aptitudeFirstCostLevel = buffer.readInt();
        dropLockedItems = buffer.readBoolean();
        displayTitlesAsPrefix = buffer.readBoolean();
        learnByDoing = buffer.readBoolean();
        learnByDoingMaxBoughtLevel = buffer.readInt();
        learnByDoingBuyableCostMultiplier = buffer.readFloat();

        // Passives
        attackDamageValue = buffer.readFloat();
        movementSpeedValue = buffer.readFloat();
        projectileSpeedValue = buffer.readFloat();
        blockReachValue = buffer.readFloat();
        breakSpeedValue = buffer.readFloat();
        criticalChanceValue = buffer.readFloat();
        luckValue = buffer.readFloat();
        damagereduction = buffer.readFloat();
        shieldproficiency = buffer.readFloat();
        stealthvalue = buffer.readFloat();
        cookingspeedvalue = buffer.readFloat();
        smeltingspeedvalue = buffer.readFloat();
        mineralefficiencyvalue = buffer.readFloat();
        greenthumbchance = buffer.readFloat();
        greenthumbrange = buffer.readFloat();
        fishingproficiencyvalue = buffer.readFloat();
        manaRegenvalue = buffer.readFloat();
        woodChoppingValue = buffer.readFloat();

        // Skills
        oneHandedAmplifier = buffer.readFloat();
        fightingSpiritBoost = buffer.readInt();
        fightingSpiritDuration = buffer.readInt();
        berserkerPercent = buffer.readInt();
        athleticsModifier = buffer.readFloat();
        quickRepositionBoost = buffer.readInt();
        quickRepositionDuration = buffer.readInt();
        hagglerPercent = buffer.readInt();
        obsidianSmasherModifier = buffer.readFloat();

        bountyHunterchance = buffer.readFloat();
        bountyHunterMaxEmeralds = buffer.readInt();

        treasureHunterProbability = buffer.readInt();
        treasureHunterItemList = buffer.readList(FriendlyByteBuf::readUtf);

        convergenceProbability = buffer.readInt();
        convergenceItemList = buffer.readList(FriendlyByteBuf::readUtf);

        // Extra skills
        survivalinstinctschance = buffer.readFloat();
        bountifulharvestchance = buffer.readFloat();
        bountifulharvestfix = buffer.readBoolean();
        fossilDiggerchance = buffer.readFloat();

        // Lists / misc
        hungryfarmerBlackList = buffer.readList(FriendlyByteBuf::readUtf);
        transmutationItem = buffer.readUtf();
        transmutationItemList = buffer.readList(FriendlyByteBuf::readUtf);
        sidestepCooldown = buffer.readInt();
        sidestepPower = buffer.readFloat();
        dodgeDenyEffect = buffer.readUtf();
    }

    public void toBytes(FriendlyByteBuf buffer) {
    	// General
        buffer.writeInt(this.aptitudeFirstCostLevel);
        buffer.writeBoolean(this.dropLockedItems);
        buffer.writeBoolean(this.displayTitlesAsPrefix);
        buffer.writeBoolean(this.learnByDoing);
        buffer.writeInt(this.learnByDoingMaxBoughtLevel);
        buffer.writeFloat(this.learnByDoingBuyableCostMultiplier);

        // Passives
        buffer.writeFloat(this.attackDamageValue);
        buffer.writeFloat(this.movementSpeedValue);
        buffer.writeFloat(this.projectileSpeedValue);
        buffer.writeFloat(this.blockReachValue);
        buffer.writeFloat(this.breakSpeedValue);
        buffer.writeFloat(this.criticalChanceValue);
        buffer.writeFloat(this.luckValue);
        buffer.writeFloat(this.damagereduction);
        buffer.writeFloat(this.shieldproficiency);
        buffer.writeFloat(this.stealthvalue);
        buffer.writeFloat(this.cookingspeedvalue);
        buffer.writeFloat(this.smeltingspeedvalue);
        buffer.writeFloat(this.mineralefficiencyvalue);
        buffer.writeFloat(this.greenthumbchance);
        buffer.writeFloat(this.greenthumbrange);
        buffer.writeFloat(this.fishingproficiencyvalue);
        buffer.writeFloat(this.manaRegenvalue);
        buffer.writeFloat(this.woodChoppingValue);

        // Skills
        buffer.writeFloat(this.oneHandedAmplifier);
        buffer.writeInt(this.fightingSpiritBoost);
        buffer.writeInt(this.fightingSpiritDuration);
        buffer.writeInt(this.berserkerPercent);
        buffer.writeFloat(this.athleticsModifier);
        buffer.writeInt(this.quickRepositionBoost);
        buffer.writeInt(this.quickRepositionDuration);
        buffer.writeInt(this.hagglerPercent);
        buffer.writeFloat(this.obsidianSmasherModifier);

        buffer.writeFloat(this.bountyHunterchance);
        buffer.writeInt(this.bountyHunterMaxEmeralds);

        buffer.writeInt(this.treasureHunterProbability);
        buffer.writeCollection(this.treasureHunterItemList, FriendlyByteBuf::writeUtf);

        buffer.writeInt(this.convergenceProbability);
        buffer.writeCollection(this.convergenceItemList, FriendlyByteBuf::writeUtf);

        // Extra skills
        buffer.writeFloat(this.survivalinstinctschance);
        buffer.writeFloat(this.bountifulharvestchance);
        buffer.writeBoolean(this.bountifulharvestfix);
        buffer.writeFloat(this.fossilDiggerchance);

        // Lists / misc
        buffer.writeCollection(this.hungryfarmerBlackList, FriendlyByteBuf::writeUtf);
        buffer.writeUtf(this.transmutationItem);
        buffer.writeCollection(this.transmutationItemList, FriendlyByteBuf::writeUtf);
        buffer.writeInt(this.sidestepCooldown);
        buffer.writeFloat(this.sidestepPower);
        buffer.writeUtf(this.dodgeDenyEffect);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
        	Minecraft instance = Minecraft.getInstance();
            LocalPlayer localPlayer = instance.player;
            if(localPlayer != null){

                var config = HandlerCommonConfig.HANDLER.instance();

                // General
                config.aptitudeFirstCostLevel = this.aptitudeFirstCostLevel;
                config.dropLockedItems = this.dropLockedItems;
                config.displayTitlesAsPrefix = this.displayTitlesAsPrefix;
                config.learnByDoing = this.learnByDoing;
                config.learnByDoingMaxBoughtLevel = this.learnByDoingMaxBoughtLevel;
                config.learnByDoingBuyableCostMultiplier = this.learnByDoingBuyableCostMultiplier;

                // Passives
                config.attackDamageValue = this.attackDamageValue;
                config.movementSpeedValue = this.movementSpeedValue;
                config.projectileSpeedValue = this.projectileSpeedValue;
                config.blockReachValue = this.blockReachValue;
                config.breakSpeedValue = this.breakSpeedValue;
                config.criticalChanceValue = this.criticalChanceValue;
                config.luckValue = this.luckValue;
                config.damagereduction = this.damagereduction;
                config.shieldproficiency = this.shieldproficiency;
                config.stealthvalue = this.stealthvalue;
                config.cookingspeedvalue = this.cookingspeedvalue;
                config.smeltingspeedvalue = this.smeltingspeedvalue;
                config.mineralefficiencyvalue = this.mineralefficiencyvalue;
                config.greenthumbchance = this.greenthumbchance;
                config.greenthumbrange = this.greenthumbrange;
                config.fishingproficiencyvalue = this.fishingproficiencyvalue;
                config.manaRegenvalue = this.manaRegenvalue;
                config.woodChoppingValue = this.woodChoppingValue;

                // Skills
                config.oneHandedAmplifier = this.oneHandedAmplifier;
                config.fightingSpiritBoost = this.fightingSpiritBoost;
                config.fightingSpiritDuration = this.fightingSpiritDuration;
                config.berserkerPercent = this.berserkerPercent;
                config.athleticsModifier = this.athleticsModifier;
                config.quickRepositionBoost = this.quickRepositionBoost;
                config.quickRepositionDuration = this.quickRepositionDuration;
                config.hagglerPercent = this.hagglerPercent;
                config.obsidianSmasherModifier = this.obsidianSmasherModifier;

                config.bountyHunterchance = this.bountyHunterchance;
                config.bountyHunterMaxEmeralds = this.bountyHunterMaxEmeralds;

                config.treasureHunterProbability = this.treasureHunterProbability;
                config.treasureHunterItemList = this.treasureHunterItemList;

                config.convergenceProbability = this.convergenceProbability;
                HandlerConvergenceItemsConfig.HANDLER.instance().convergenceItemList = this.convergenceItemList;

                // Extra skills
                config.survivalinstinctschance = this.survivalinstinctschance;
                config.bountifulharvestchance = this.bountifulharvestchance;
                config.bountifulharvestfix = this.bountifulharvestfix;
                config.fossilDiggerchance = this.fossilDiggerchance;

                // Lists / misc
                config.hungryfarmerBlackList = this.hungryfarmerBlackList;
                config.transmutationItem = this.transmutationItem;
                config.transmutationItemList = this.transmutationItemList;
                config.sidestepCooldown = this.sidestepCooldown;
                config.sidestepPower = this.sidestepPower;
                config.dodgeDenyEffect = this.dodgeDenyEffect;
                
                //we calculate here anyways
                @SuppressWarnings("removal")
        		ResourceLocation dodgedenyloc = new ResourceLocation(this.dodgeDenyEffect);
        		if (ForgeRegistries.MOB_EFFECTS.containsKey(dodgedenyloc)) {
        			ResourceLoader.DodgeDenyEffect = ForgeRegistries.MOB_EFFECTS.getValue(dodgedenyloc);		
        		}
                
                // Save client config
                HandlerCommonConfig.HANDLER.save();
            }
        });
        context.setPacketHandled(true);
    }

    public static void sendToPlayer(Player player) {
        ServerNetworking.sendToPlayer(new CommonConfigSyncCP(), (ServerPlayer) player);
    }
}
