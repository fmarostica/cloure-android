package com.grupomarostica.cloure.Modules.company_branches;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_company_branches implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new company_branches_fragment());
    }
}
