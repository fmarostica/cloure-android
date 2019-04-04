package com.grupomarostica.cloure.Modules.maintenance_warranties;

import com.grupomarostica.cloure.Core.AvailableCommand;

import java.util.ArrayList;

public class MaintenanceWarranty {
    public int Id=0;
    public int MaintenanceShiftId=0;
    public String DateFrom="";
    public String DateUntil="";
    public int Days=0;
    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();
}
