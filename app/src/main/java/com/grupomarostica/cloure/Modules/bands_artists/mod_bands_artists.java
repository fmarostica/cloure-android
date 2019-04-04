package com.grupomarostica.cloure.Modules.bands_artists;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_bands_artists implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new bands_artists_fragment());
    }
}
