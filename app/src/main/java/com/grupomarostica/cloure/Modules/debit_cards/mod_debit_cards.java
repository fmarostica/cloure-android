package com.grupomarostica.cloure.Modules.debit_cards;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;

public class mod_debit_cards implements CloureModule {
    @Override
    public void onModuleCreated() {
        CloureManager.Navigate(new debit_cards_fragment());
    }
}
