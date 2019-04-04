package com.grupomarostica.cloure.Modules.messages;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_messages implements CloureModule {

    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new messages_fragment());
    }
}
