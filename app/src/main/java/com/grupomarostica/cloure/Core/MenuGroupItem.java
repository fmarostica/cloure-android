package com.grupomarostica.cloure.Core;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

public class MenuGroupItem extends ConstraintLayout {
    private TextView tvTitle;

    public MenuGroupItem(Context context) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, this, true);
        tvTitle = v.findViewById(R.id.list_item_title);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }
}
