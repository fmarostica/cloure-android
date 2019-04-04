package com.grupomarostica.cloure.Core;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class MenuGroup extends ConstraintLayout {
    private TextView tvTitle;
    private ArrayList<MenuGroupItem> items = new ArrayList<>();
    private LinearLayout layout_childs;

    public MenuGroup(Context context) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.menu_group, this, true);
        tvTitle = v.findViewById(R.id.menu_group_txt_title);
        layout_childs = v.findViewById(R.id.menu_group_lst_childs);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void add(MenuGroupItem item){
        this.items.add(item);
        layout_childs.addView(item);
    }
}
