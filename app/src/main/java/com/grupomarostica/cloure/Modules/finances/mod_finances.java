package com.grupomarostica.cloure.Modules.finances;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.ModuleInfo;

public class mod_finances implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new finances_fragment());
    }
}
