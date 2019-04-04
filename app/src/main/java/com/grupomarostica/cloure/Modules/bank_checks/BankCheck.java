package com.grupomarostica.cloure.Modules.bank_checks;

import com.grupomarostica.cloure.Core.AvailableCommand;

import java.util.List;

public class BankCheck {
    public int Id = 0;
    public int BancoId =0;
    public String Banco = "";
    public String FechaCobroStr = "";
    public int ClienteId = 0;
    public String Cliente = "";
    public String Descripcion = "";

    public List<AvailableCommand> AvailableCommands;
}
