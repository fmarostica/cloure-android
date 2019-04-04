package com.grupomarostica.cloure.Modules.maintenance_shifts;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Modules.receipts.CartItem;

import java.util.ArrayList;
import java.util.Date;

public class MaintenanceShift {
    public int Id=0;
    public Date Fecha;
    public String FechaCompletaStr="";
    public String FechaStr="";
    public String HoraStr="";
    public String IssueDescription="";
    public int CustomerId = 0;
    public String Customer="";
    public String CustomerAddress="";
    public String CustomerPhone="";
    public int EquipmentTypeId = 0;
    public int StatusId = 0;
    public String Status = "";
    public ArrayList<AvailableCommand> AvailableCommands=new ArrayList<>();
    public ArrayList<CartItem> Items = new ArrayList<>();
}

