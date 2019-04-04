package com.grupomarostica.cloure.Modules.company;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_company implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new company_fragment());
    }
}
