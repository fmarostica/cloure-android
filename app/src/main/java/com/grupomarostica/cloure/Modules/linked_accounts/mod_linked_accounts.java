package com.grupomarostica.cloure.Modules.linked_accounts;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.ModuleInfo;

public class mod_linked_accounts implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new linked_accounts_fragment());
    }
}
