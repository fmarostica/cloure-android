package com.grupomarostica.cloure.Modules.banks;

import com.grupomarostica.cloure.Core.AvailableCommand;

import java.util.ArrayList;

public class Bank {
    public int Id = 0;
    public String Name = "";
    public String Web = "";
    public String OnlineBankingURL = "";

    public ArrayList<AvailableCommand> AvailableCommands = new ArrayList<>();
}
