package com.grupomarostica.cloure.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CloureMenuGroup implements Serializable {
    public String Name = "";
    public String Title = "";
    public String IconSVG = "";
    public List<CloureMenuItem> cloureMenuItems = new ArrayList<>();

    public CloureMenuGroup(String Name, String Title, String IconSVG){
        this.Name = Name;
        this.Title = Title;
        this.IconSVG = IconSVG;
    }

    public List<CloureMenuItem> getItemList(){
        return cloureMenuItems;
    }
}
