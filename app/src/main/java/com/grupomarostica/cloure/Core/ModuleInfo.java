package com.grupomarostica.cloure.Core;

import org.json.JSONObject;

import java.util.ArrayList;

public class ModuleInfo {
    public String Name = "";
    public String Title = "";
    public ArrayList<GlobalCommand> globalCommands = new ArrayList<>();
    public ArrayList<ModuleFilter> moduleFilters = new ArrayList<>();
    public JSONObject locales = null;
}
