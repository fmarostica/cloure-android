package com.grupomarostica.cloure.Modules.support;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.ModuleInfo;

public class mod_support implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new support_fragment());
    }
}
