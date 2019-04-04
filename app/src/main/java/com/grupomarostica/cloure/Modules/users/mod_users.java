package com.grupomarostica.cloure.Modules.users;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_users implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new users_fragment());
    }
}
