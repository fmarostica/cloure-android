package com.grupomarostica.cloure.Modules.maintenance_equipments_brands;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_maintenance_equipments_brands implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new maintenance_equipments_brands_fragment());
    }
}
