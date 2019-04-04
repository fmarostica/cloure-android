package com.grupomarostica.cloure.Modules.users_groups;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_users_groups implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new users_groups_fragment());
    }
}
