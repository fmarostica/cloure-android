package com.grupomarostica.cloure.Modules.countries;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_countries implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new countries_fragment());
    }
}
