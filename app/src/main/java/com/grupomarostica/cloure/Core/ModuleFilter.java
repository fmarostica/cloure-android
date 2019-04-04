package com.grupomarostica.cloure.Core;

import java.io.Serializable;
import java.util.ArrayList;

public class ModuleFilter implements Serializable {
    public String Name;
    public String Title;
    public String Type;

    public ArrayList<ModuleFilterItem> moduleFilterItems = new ArrayList<>();

    public ModuleFilter(String name, String title, String type){
        this.Name = name;
        this.Title = title;
        this.Type = type;
    }
}
