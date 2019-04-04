package com.grupomarostica.cloure.Modules.transports;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_transports implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new transports_fragment());
    }
}
