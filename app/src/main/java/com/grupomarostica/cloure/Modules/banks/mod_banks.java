package com.grupomarostica.cloure.Modules.banks;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_banks implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new banks_fragment());
    }
}
