package com.grupomarostica.cloure.Modules.places;

import com.grupomarostica.cloure.Core.AvailableCommand;

import java.util.ArrayList;

public class Place {
    public int Id=0;
    public String Name="";
    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();

    @Override
    public String toString() {
        return this.Name;
    }
}
