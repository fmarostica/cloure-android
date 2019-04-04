package com.grupomarostica.cloure.Modules.products_services;

import android.graphics.Bitmap;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.Bitmap64;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductService implements Serializable {
    public Integer Id=0;
    public String Title="";
    public String Description="";

    public int CategoriaN1Id = 0;
    public int CategoriaN2Id = 0;

    public String CategoriaN1="";
    public String ImagePath="";
    public Bitmap ImageBitmap = null;

    public Double IVA = 0.0;
    public Double PrecioCosto = 0.0;
    public Double ImporteCosto = 0.0;
    public Double PrecioVenta = 0.0;
    public Double ImporteVenta = 0.0;
    public Double Importe=0.0;
    public int MeasureUnitId=0;

    public ArrayList<StockItem> Stock = new ArrayList<>();
    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();
    public ArrayList<Bitmap64> imagesToUpload = new ArrayList<>();
}
