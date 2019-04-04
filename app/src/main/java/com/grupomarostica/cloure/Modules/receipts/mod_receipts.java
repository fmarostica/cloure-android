package com.grupomarostica.cloure.Modules.receipts;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.ModuleInfo;

public class mod_receipts implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new receipts_fragment());
    }
}
