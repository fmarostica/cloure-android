package com.grupomarostica.cloure.Modules.maintanance_equipments_types;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_maintanance_equipments_types implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new maintanance_equipments_types_fragment());
    }
}
