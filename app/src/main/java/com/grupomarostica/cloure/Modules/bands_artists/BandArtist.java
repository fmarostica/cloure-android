package com.grupomarostica.cloure.Modules.bands_artists;

import com.grupomarostica.cloure.Core.AvailableCommand;

import java.util.ArrayList;

public class BandArtist {
    public int Id=0;
    public String Name="";
    public String ImageName = "";

    public ArrayList<AvailableCommand> AvailableCommands=new ArrayList<>();

    @Override
    public String toString(){
        return this.Name;
    }
}
