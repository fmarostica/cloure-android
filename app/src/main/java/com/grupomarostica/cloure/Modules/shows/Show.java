package com.grupomarostica.cloure.Modules.shows;

import com.grupomarostica.cloure.Core.AvailableCommand;

import java.util.ArrayList;

public class Show {
    public int Id=0;
    public int BandArtistId=0;
    public int PlaceId=0;

    public String DateStr="";
    public String BandArtist="";
    public String Place="";
    public String Photographers = ""; //Photographers concatenated
    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();
}
