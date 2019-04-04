package com.grupomarostica.cloure.Modules.maintenance_shifts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.grupomarostica.cloure.Modules.maintanance_equipments_types.MaintenanceEquipmentType;
import com.grupomarostica.cloure.Modules.maintanance_equipments_types.MaintenanceEquipmentsTypes;
import com.grupomarostica.cloure.Modules.maintanance_equipments_types.MaintenanceEquipmentsTypesAdapter;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class TabGeneral extends Fragment {
    public Spinner txtEquipmentType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_maintenance_shifts_finish_tab_general, container, false);
        txtEquipmentType = rootView.findViewById(R.id.activity_finish_maintenance_shift_txt_etype);
        load_equipment_types();
        return rootView;
    }

    public void load_equipment_types(){
        try{
            ArrayList<MaintenanceEquipmentType> equipmentTypes = new MaintenanceEquipmentsTypes().get_list();
            MaintenanceEquipmentsTypesAdapter equipmentsTypesAdapter = new MaintenanceEquipmentsTypesAdapter(getActivity(), equipmentTypes);
            txtEquipmentType.setAdapter(equipmentsTypesAdapter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
