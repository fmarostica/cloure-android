package com.grupomarostica.cloure.Modules.products_services;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_products_services implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new products_services_fragment());
    }
}
