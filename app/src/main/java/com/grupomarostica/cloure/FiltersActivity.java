package com.grupomarostica.cloure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.ModuleFilter;
import com.grupomarostica.cloure.Core.ModuleFilterItem;
import com.grupomarostica.cloure.Core.ModuleFilterItemAdapter;

import java.util.ArrayList;

public class FiltersActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ImageButton btnApply;
    ArrayList<ModuleFilter> filters;
    ArrayList<Object> controls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        btnApply = findViewById(R.id.activity_activity_filters_btn_apply);
        linearLayout = findViewById(R.id.activity_filter_main_content);

        Bundle bundle = getIntent().getExtras();
        filters = (ArrayList<ModuleFilter>)bundle.getSerializable("filters");

        for (int i=0; i<filters.size(); i++){
            ModuleFilter moduleFilter = filters.get(i);
            if(moduleFilter.Type.equals("combo")){
                Spinner spinner = new Spinner(this);
                spinner.setTag(moduleFilter.Name);
                spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                ModuleFilterItemAdapter adapter = new ModuleFilterItemAdapter(this, moduleFilter.moduleFilterItems);
                spinner.setAdapter(adapter);

                linearLayout.addView(spinner);
                controls.add(spinner);
            }

        }

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent resultIntent = new Intent();
                    for (int i=0; i<controls.size(); i++){
                        if(controls.get(i) instanceof Spinner){
                            Spinner spinner = (Spinner)controls.get(i);
                            String name = (String) spinner.getTag();
                            ModuleFilterItem value = (ModuleFilterItem)spinner.getSelectedItem();
                            resultIntent.putExtra(name, value.Id);
                        }
                    }

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
