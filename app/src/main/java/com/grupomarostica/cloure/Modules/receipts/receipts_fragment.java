package com.grupomarostica.cloure.Modules.receipts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.AvailableCommand;
import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureModule;
import com.grupomarostica.cloure.Core.GlobalCommand;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.R;

import java.io.File;
import java.util.ArrayList;

public class receipts_fragment extends Fragment {
    private ListView listView;
    private TextView tvNoResults;
    private ReceiptsAdapter adapter;
    private ArrayList<Receipt> registers;
    private RelativeLayout loaderLayout;
    private ConstraintLayout emptyLayout;
    private Handler handler = new Handler();
    private ModuleInfo moduleInfo;
    private boolean ModuleInitializing = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_panel_scroll,container, false);

        try{
            moduleInfo = CloureManager.getModuleInfo();

            listView = rootView.findViewById(R.id.fragment_panel_scroll_items);
            loaderLayout = rootView.findViewById(R.id.loader);
            emptyLayout = rootView.findViewById(R.id.fragment_scroll_no_results);
            tvNoResults = rootView.findViewById(R.id.fragment_scroll_tv_no_results);

            tvNoResults.setText(moduleInfo.locales.getString("no_results"));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Receipt item = (Receipt) parent.getAdapter().getItem(position);
                    edit(item);
                }
            });

            registerForContextMenu(listView);
            setHasOptionsMenu(true);
        } catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(moduleInfo.Title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*
        for(int i=0;i<moduleInfo.globalCommands.size(); i++){
            GlobalCommand gc = moduleInfo.globalCommands.get(i);
            menu.add(Menu.NONE, gc.Id, Menu.NONE, gc.Title);
        }
        */
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                //Agregar
                //Intent intent = new Intent(getActivity(), UserGroupAddActivity.class);
                //startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        Receipt item = (Receipt)listView.getItemAtPosition(info.position);
        for (int i=0;i<item.AvailableCommands.size();i++){
            AvailableCommand availableCommand = item.AvailableCommands.get(i);
            menu.add(Menu.NONE, availableCommand.Id, Menu.NONE, availableCommand.Title);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        Receipt selectedItem = adapter.getItem(index);

        switch(item.getItemId()) {
            case 1:
                edit(selectedItem);
                break;
            case 2:
                delete(selectedItem.Id);
                break;
            case 3:
                exportPDF(selectedItem.Id);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        load_data();
    }

    private void exportPDF(int id){
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        BackgroundExportPDF backgroundExportPDF = new BackgroundExportPDF(id);
        new Thread(backgroundExportPDF).start();
    }

    private class BackgroundExportPDF implements Runnable{
        int ReceiptId = 0;

        public BackgroundExportPDF(int ReceiptId){
            this.ReceiptId = ReceiptId;
        }

        @Override
        public void run() {
            final File file = Receipts.ExportPDF(this.ReceiptId);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(file!=null){
                        //Toast.makeText(getActivity(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(Uri.fromFile(file),"application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Intent intent = Intent.createChooser(target, "Open File");
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void edit(Receipt item){
        Intent intent = new Intent(getActivity(), ReceiptsDetailsActivity.class);
        intent.putExtra("id", item.Id);
        startActivity(intent);
    }

    private void delete(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Borrar");
        builder.setMessage("¿Está seguro que desea eliminar este registro?");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                        ArrayList<CloureParam> params = new ArrayList<>();
                        params.add(new CloureParam("module", "countries"));
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
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                        */
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
        try{
            BackgroundLoad backgroundLoad = new BackgroundLoad();
            new Thread(backgroundLoad).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    class BackgroundLoad implements Runnable{
        @Override
        public void run() {
            registers = Receipts.get_list("");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        adapter = new ReceiptsAdapter(getActivity(), registers);
                        if(registers.size()>0){
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.INVISIBLE);
                        } else {
                            listView.setVisibility(View.INVISIBLE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        loaderLayout.setVisibility(View.INVISIBLE);
                        ModuleInitializing=false;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
