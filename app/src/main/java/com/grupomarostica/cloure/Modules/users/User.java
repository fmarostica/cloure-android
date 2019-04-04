package com.grupomarostica.cloure.Modules.users;

import android.graphics.Bitmap;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.Bitmap64;

import java.io.Serializable;
import java.util.ArrayList;

public class User {
    public int id=0;
    public String nombre="";
    public String apellido="";
    public String telefono="";
    public String direccion="";
    public String email="";
    public double saldo=0;
    public String grupo_id="";
    public String ImageURL = "";
    public Bitmap Image;
    public Bitmap64 ImageToUpload = null;

    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();

}
