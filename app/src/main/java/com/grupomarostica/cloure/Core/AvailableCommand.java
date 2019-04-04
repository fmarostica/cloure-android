package com.grupomarostica.cloure.Core;

import java.io.Serializable;

public class AvailableCommand implements Serializable {
    public int Id;
    public String Name;
    public String Title;

    public AvailableCommand(int id, String name, String title){
        this.Id = id;
        this.Name = name;
        this.Title = title;
    }
}
