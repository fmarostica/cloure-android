package com.grupomarostica.cloure.Modules.receipts;
import java.io.Serializable;

public class CartItem implements Serializable {
    public double Cantidad = 0;
    public String Descripcion = "";
    public double Precio = 0;
    public double Total = 0;
    public int ProductoId = 0;
    public double Iva = 0;

    public CartItem(double Cantidad, String Descripcion, double Precio, double Total){
        this.Cantidad = Cantidad;
        this.Descripcion = Descripcion;
        this.Precio = Precio;
        this.Total = Total;
    }

    /*
    private CartItem(Parcel in){
        Cantidad = in.readDouble();
        Descripcion = in.readString();
        Precio = in.readDouble();
        Total = in.readDouble();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Descripcion);
        dest.writeDouble(Cantidad);
        dest.writeDouble(Precio);
        dest.writeDouble(Total);
    }
    */
}
