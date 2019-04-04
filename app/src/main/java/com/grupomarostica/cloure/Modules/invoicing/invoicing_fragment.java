package com.grupomarostica.cloure.Modules.invoicing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.grupomarostica.cloure.Core.CloureManager;
import com.grupomarostica.cloure.Core.CloureParam;
import com.grupomarostica.cloure.Core.CloureSDK;
import com.grupomarostica.cloure.Core.ModuleInfo;
import com.grupomarostica.cloure.Modules.company_branches_receipts.CompanyBranchReceipt;
import com.grupomarostica.cloure.Modules.receipts.CartItem;
import com.grupomarostica.cloure.Modules.receipts.CartItemEditableAdapter;
import com.grupomarostica.cloure.Modules.receipts.Receipt;
import com.grupomarostica.cloure.Modules.receipts.Receipts;
import com.grupomarostica.cloure.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class invoicing_fragment extends Fragment {
    ModuleInfo moduleInfo;
    private TabGeneral tabGeneral = new TabGeneral();
    private TabProducts tabProducts = new TabProducts();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private TextView txtTotal;
    private ImageButton btnSave;
    private Receipt receipt = new Receipt();
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoicing,container, false);

        TabLayout tabLayout = rootView.findViewById(R.id.fragment_invoicing_tabLayout);
        txtTotal = rootView.findViewById(R.id.fragment_invoicing_txtTotal);
        viewPager = rootView.findViewById(R.id.fragment_invoicing_viewPager);
        btnSave = rootView.findViewById(R.id.fragment_invoicing_btnSave);

        moduleInfo = CloureManager.getModuleInfo();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mSectionsPagerAdapter.AddFragment(tabGeneral);
        mSectionsPagerAdapter.AddFragment(tabProducts);
        viewPager.setAdapter(mSectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });

        return rootView;
    }

    public void setTxtTotal(double Total){
        txtTotal.setText(String.format(Locale.US, "%.2f", Total));
    }

    private void Save(){
        try{
            receipt.CustomerId = tabGeneral.user.id;

            CompanyBranchReceipt companyBranchReceipt = (CompanyBranchReceipt) tabGeneral.txtReceiptTypes.getSelectedItem();
            receipt.ReceiptType = companyBranchReceipt.Id;
            receipt.Items = tabProducts.items;

            JSONObject response = Receipts.Save(receipt);
            String error = response.getString("Error");
            if(error.equals("")){
                JSONObject receiptResponse = response.getJSONObject("Response");
                int receiptId = receiptResponse.getInt("ComprobanteId");
                exportPDF(receiptId);
            } else {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
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


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void AddFragment(Fragment fragment){
            fragmentList.add(fragment);
        }
    }

}
