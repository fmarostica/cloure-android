package com.grupomarostica.cloure.Modules.receipts;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Modules.users.User;

import java.util.ArrayList;

public class Receipt {
    public int Id = 0;
    public String Description = "";
    public User Customer;
    public int CustomerId = 0;
    public String CustomerName = "";
    public int ReceiptType = 0;

    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();
    public ArrayList<CartItem> Items = new ArrayList<>();

}
