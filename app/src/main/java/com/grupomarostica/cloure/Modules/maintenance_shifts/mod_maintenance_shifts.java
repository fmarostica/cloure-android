package com.grupomarostica.cloure.Modules.maintenance_shifts;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_maintenance_shifts implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new maintenance_shifts_fragment());
    }
}
