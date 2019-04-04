package com.grupomarostica.cloure.Modules.credit_cards;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_credit_cards implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new credit_cards_fragment());
    }
}
