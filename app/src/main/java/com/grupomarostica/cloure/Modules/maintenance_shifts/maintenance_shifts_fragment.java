package com.grupomarostica.cloure.Modules.maintenance_shifts;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureLocale;
import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.FiltersActivity;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class maintenance_shifts_fragment extends Fragment {
    private ModuleInfo moduleInfo;
    private ListView listView;
    private LinearLayout buttonsBar;
    private ArrayList<MaintenanceShift> registers;
    private RelativeLayout loaderLayout;
    private ImageView btnAdd;
    private CloureSDK cloureSDK;
    private MaintenanceShiftAdapter adapter;
    private Handler handler = new Handler();
    private ConstraintLayout emptyLayout;

    //Filters
    private String FilterStateId = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);

        moduleInfo = CloureManager.getModuleInfo();

        listView = rootView.findViewById(R.id.fragment_panel_scroll_items);
        loaderLayout = rootView.findViewById(R.id.loader);
        emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MaintenanceShift met = (MaintenanceShift)adapter.getItem(position);
                edit(met.Id);
            }
        });

        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(moduleInfo.Title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            if(gc.Name.equals("add")) menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
            if(gc.Name.equals("filters")) menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                Intent intent = new Intent(getContext(), MaintenanceShiftsAddActivity.class);
                startActivity(intent);
                break;
            case 2:
                //Filtros
                Intent intentFilters = new Intent(getContext(), FiltersActivity.class);
                intentFilters.putExtra("filters", moduleInfo.moduleFilters);
                startActivityForResult(intentFilters, 2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MaintenanceShift maintenanceShift = (MaintenanceShift)listView.getItemAtPosition(info.position);
        for (int i=0;i<maintenanceShift.AvailableCommands.size();i++){
            AvailableCommand availableCommand = maintenanceShift.AvailableCommands.get(i);
            menu.add(Menu.NONE, availableCommand.Id, Menu.NONE, availableCommand.Title);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        MaintenanceShift register = adapter.getItem(index);

        switch(item.getItemId()) {
            case 1:
                //Edit
                edit(register.Id);
                break;
            case 2:
                //Delete
                delete(register.Id);
                break;
            case 3:
                Intent finish_intent = new Intent(getContext(), MaintenanceShiftsFinishActivity.class);
                finish_intent.putExtra("id", register.Id);
                startActivity(finish_intent);
                break;
            case 4:
                //Send SMS
                Intent sms_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+register.CustomerPhone));
                startActivity(sms_intent);
            case 5:
                //Make call
                Intent call_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+register.CustomerPhone));
                startActivity(call_intent);
                break;
            case 6:
                //Send Whatsapp
                try{
                    Intent wp_intent = new Intent(Intent.ACTION_MAIN, Uri.parse("whatsapp:"+register.CustomerPhone));
                    wp_intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    wp_intent.setAction(Intent.ACTION_SEND);
                    wp_intent.setType("text/plain");
                    wp_intent.putExtra(Intent.EXTRA_TEXT, "");
                    String telefono = register.CustomerPhone.replace("+","");
                    telefono = telefono.replace(" ","");
                    telefono = telefono.replace("-","");
                    wp_intent.putExtra("jid", telefono);
                    wp_intent.setPackage("com.whatsapp");
                    startActivity(wp_intent);
                } catch(Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:
                //View in map
                String uri = "geo:0,0?q="+register.CustomerAddress;
                Intent geo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(geo_intent);
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }

    public void edit(int id){
        Intent intent = new Intent(getActivity(), MaintenanceShiftsAddActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        //startActivityForResult(intent, 1);
    }

    private void delete(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Borrar");
        builder.setMessage("¿Está seguro que desea eliminar este registro?");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<CloureParam> params = new ArrayList<>();
                        params.add(new CloureParam("module", "maintenance_shifts"));
                        params.add(new CloureParam("topic", "borrar"));
                        params.add(new CloureParam("id", Integer.toString(id)));

                        CloureSDK cloureSDK = new CloureSDK();
                        try{
                            String res = cloureSDK.execute(params);
                            JSONObject object = new JSONObject(res);
                            String error = object.getString("Error");
                            if(error.equals("")){
                                load_data();
                            } else {
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void load_data(){
        BackgroundLoad backgroundLoad = new BackgroundLoad();
        new Thread(backgroundLoad).start();
    }

    class BackgroundLoad implements Runnable{
        String res = "";
        String error = "";

        @Override
        public void run() {
            registers = MaintenanceShifts.GetList(FilterStateId);
            adapter = new MaintenanceShiftAdapter(getContext(), registers);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(registers.size()>0){
                        listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.INVISIBLE);
                    } else {
                        listView.setVisibility(View.INVISIBLE);
                        emptyLayout.setVisibility(View.VISIBLE);
                    }
                    loaderLayout.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null)
        {
            if(requestCode==2){
                try{
                    FilterStateId = data.getStringExtra("state");
                    load_data();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
