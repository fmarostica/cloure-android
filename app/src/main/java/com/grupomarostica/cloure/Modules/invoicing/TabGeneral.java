package com.grupomarostica.cloure.Modules.invoicing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.grupomarostica.cloure.Modules.company_branches_receipts.CompanyBranchReceipt;
import com.grupomarostica.cloure.Modules.company_branches_receipts.CompanyBranchReceiptAdapter;
import com.grupomarostica.cloure.Modules.company_branches_receipts.CompanyBranchesReceipts;
import com.grupomarostica.cloure.Modules.users.User;
import com.grupomarostica.cloure.Modules.users.UserSelection;
import com.grupomarostica.cloure.Modules.users.Users;
import com.grupomarostica.cloure.R;

import java.util.ArrayList;

public class TabGeneral extends Fragment {
    private Button btnSelectCustomer;
    private ArrayList<CompanyBranchReceipt> companyBranchReceipts = new ArrayList<>();
    private CompanyBranchReceiptAdapter adapter;
    public Spinner txtReceiptTypes;
    public User user = new User();
    private Handler handler = new Handler();
    private invoicing_fragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoicing_tab_customer,container, false);

        btnSelectCustomer = rootView.findViewById(R.id.fragment_invoicing_tabCustomer_btnSelectCustomer);
        txtReceiptTypes = rootView.findViewById(R.id.fragment_invoicing_tabCustomer_txtReceiptType);

        btnSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(getActivity(), UserSelection.class);
                    startActivityForResult(intent, 1);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        LoadReceiptTypes();

        int pos = 0;
        for (int i=0;i<companyBranchReceipts.size(); i++){
            CompanyBranchReceipt companyBranchReceipt = companyBranchReceipts.get(i);
            if(companyBranchReceipt.Id==2){
                pos = i;
                break;
            }
        }
        txtReceiptTypes.setSelection(pos);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (invoicing_fragment)getParentFragment();
    }


    private void LoadReceiptTypes(){
        companyBranchReceipts = CompanyBranchesReceipts.getList();
        adapter = new CompanyBranchReceiptAdapter(getContext(), companyBranchReceipts);
        txtReceiptTypes.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==1){
                int user_id = data.getIntExtra("user_id", 0);
                UserBackgroundLoader userBackgroundLoader = new UserBackgroundLoader(user_id);
                new Thread(userBackgroundLoader).start();
            }
        }
    }

    class UserBackgroundLoader implements Runnable{
        int user_id = 0;
        public UserBackgroundLoader(int user_id){
            this.user_id = user_id;
        }
        @Override
        public void run() {
            user = Users.getById(this.user_id);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    btnSelectCustomer.setText(user.apellido + ", " + user.nombre);
                }
            });
        }
    }
}
