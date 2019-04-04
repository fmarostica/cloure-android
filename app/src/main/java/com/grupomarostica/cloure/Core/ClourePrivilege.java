package com.grupomarostica.cloure.Core;

public class ClourePrivilege {
    public String Id = "";
    public String Title = "";
    public String Type = "";
    public String Value = "false";

    public ClourePrivilege(String Id, String Title, String Type, String Value){
        this.Id = Id;
        this.Title = Title;
        this.Type = Type;
        this.Value = Value;
    }
}
