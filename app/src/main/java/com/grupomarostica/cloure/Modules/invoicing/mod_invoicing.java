package com.grupomarostica.cloure.Modules.invoicing;

import android.support.v4.app.Fragment;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.ModuleInfo;

public class mod_invoicing implements CloureModule {
    @Override
    public void onModuleCreated() {
        Fragment fragment = new invoicing_fragment();
        CloureManager.Navigate(fragment);
    }
}
