package com.grupomarostica.cloure.Core;

import java.io.Serializable;

public class ModuleFilterItem implements Serializable {
    public String Id="";
    public String Title = "";

    public ModuleFilterItem(String id, String title){
        this.Id = id;
        this.Title = title;
    }
}
