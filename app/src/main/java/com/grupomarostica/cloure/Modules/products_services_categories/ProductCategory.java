package com.grupomarostica.cloure.Modules.products_services_categories;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.Bitmap64;

import java.util.ArrayList;

public class ProductCategory {
    public int Id=0;
    public String Name="";
    public Bitmap64 ImageToUpload = null;
    public String Type = "";

    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();
}
