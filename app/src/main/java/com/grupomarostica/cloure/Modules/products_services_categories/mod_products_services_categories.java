package com.grupomarostica.cloure.Modules.products_services_categories;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_products_services_categories implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new products_services_categories_fragment());
    }
}
