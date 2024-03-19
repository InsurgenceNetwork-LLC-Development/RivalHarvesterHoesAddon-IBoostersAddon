package com.insurgencedev.rivalhoesaddon.listeners;

import com.google.common.util.concurrent.AtomicDouble;
import me.rivaldev.harvesterhoes.api.events.HoeEssenceReceivePreEnchantEvent;
import me.rivaldev.harvesterhoes.api.events.HoeMoneyReceiveEnchant;
import me.rivaldev.harvesterhoes.api.events.HoeXPGainEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.data.BoosterFindResult;
import org.insurgencedev.insurgenceboosters.data.PermanentBoosterData;

import java.util.Optional;

public final class RivalHoesEventListeners implements Listener {

    @EventHandler
    private void onReceive(HoeEssenceReceivePreEnchantEvent event) {
        final String TYPE = "Essence";
        final String NAMESPACE = "RIVAL_HOES";
        Player player = event.getPlayer();
        AtomicDouble totalMulti = new AtomicDouble(getPersonalPermMulti(player, TYPE) + getGlobalPermMulti(TYPE));

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(event.getPlayer()).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            totalMulti.getAndAdd(boosterResult.getBoosterData().getMultiplier());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            totalMulti.getAndAdd(globalBooster.getMultiplier());
            return null;
        }, () -> null);

        if (totalMulti.get() > 0) {
            event.setEssence(calculateAmount(event.getEssence(), totalMulti.get()));
        }
    }

    @EventHandler
    private void onReceive(HoeMoneyReceiveEnchant event) {
        final String TYPE = "Money";
        final String NAMESPACE = "RIVAL_HOES";
        Player player = event.getPlayer();
        AtomicDouble totalMulti = new AtomicDouble(getPersonalPermMulti(player, TYPE) + getGlobalPermMulti(TYPE));

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(event.getPlayer()).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            totalMulti.getAndAdd(boosterResult.getBoosterData().getMultiplier());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            totalMulti.getAndAdd(globalBooster.getMultiplier());
            return null;
        }, () -> null);

        if (totalMulti.get() > 0) {
            event.setMoney(calculateAmount(event.getMoney(), totalMulti.get()));
        }
    }

    @EventHandler
    private void onGain(HoeXPGainEvent event) {
        final String TYPE = "XP";
        final String NAMESPACE = "RIVAL_HOES";
        Player player = event.getPlayer();
        AtomicDouble totalMulti = new AtomicDouble(getPersonalPermMulti(player, TYPE) + getGlobalPermMulti(TYPE));

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(event.getPlayer()).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            totalMulti.getAndAdd(boosterResult.getBoosterData().getMultiplier());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            totalMulti.getAndAdd(globalBooster.getMultiplier());
            return null;
        }, () -> null);

        if (totalMulti.get() > 0) {
            event.setXP(calculateAmount(event.getXP(), totalMulti.get()));
        }
    }

    private double getPersonalPermMulti(Player uuid, String type) {
        Optional<PermanentBoosterData> foundMulti = Optional.ofNullable(IBoosterAPI.INSTANCE.getCache(uuid).getPermanentBoosts().getPermanentBooster(type, "RIVAL_HOES"));
        return foundMulti.map(PermanentBoosterData::getMulti).orElse(0d);
    }

    private double getGlobalPermMulti(String type) {
        AtomicDouble multi = new AtomicDouble(0d);

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findPermanentBooster(type, "RIVAL_HOES", data -> {
            multi.set(data.getMulti());
            return null;
        }, () -> null);

        return multi.get();
    }

    private double calculateAmount(double amount, double multi) {
        return amount * (multi < 1 ? 1 + multi : multi);
    }

}
