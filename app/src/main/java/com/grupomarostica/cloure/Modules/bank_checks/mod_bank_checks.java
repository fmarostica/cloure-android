package com.grupomarostica.cloure.Modules.bank_checks;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_bank_checks implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new fragment_bank_checks());
    }
}
