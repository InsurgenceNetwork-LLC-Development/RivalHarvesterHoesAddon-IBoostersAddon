package com.insurgencedev.rivalhoesaddon;

import com.insurgencedev.rivalhoesaddon.listeners.RivalHoesEventListeners;
import org.insurgencedev.insurgenceboosters.api.addon.IBoostersAddon;
import org.insurgencedev.insurgenceboosters.api.addon.InsurgenceBoostersAddon;
import org.insurgencedev.insurgenceboosters.libs.fo.Common;

@IBoostersAddon(name = "RivalHoesAddon", version = "1.0.2", author = "InsurgenceDev", description = "RivalHarvesterHoes Support")
public class RivalHoesAddon extends InsurgenceBoostersAddon {

    @Override
    public void onAddonReloadAblesStart() {
        if (Common.doesPluginExist("RivalHarvesterHoes")) {
            registerEvent(new RivalHoesEventListeners());
        }
    }
}
