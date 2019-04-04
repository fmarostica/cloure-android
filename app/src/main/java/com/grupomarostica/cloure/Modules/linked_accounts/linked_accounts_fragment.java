package com.grupomarostica.cloure.Modules.linked_accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

public class linked_accounts_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private TextView tvNoResults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);
        tvNoResults = rootView.findViewById(R.id.fragment_scroll_tv_no_results);

        try{
            moduleInfo = CloureManager.getModuleInfo();
            tvNoResults.setText(moduleInfo.locales.getString("no_results"));
            RelativeLayout loaderLayout = rootView.findViewById(R.id.loader);
            ConstraintLayout emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);
            emptyLayout.setVisibility(View.VISIBLE);
            loaderLayout.setVisibility(View.INVISIBLE);
            setHasOptionsMenu(true);
        } catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }
}
