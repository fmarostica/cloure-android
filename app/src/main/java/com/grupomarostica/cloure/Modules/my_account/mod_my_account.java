package com.grupomarostica.cloure.Modules.my_account;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_my_account implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new my_account_fragment());
    }
}
