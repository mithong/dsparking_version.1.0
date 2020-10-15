package com.example.dtuparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragmentBV extends Fragment {
    private DatabaseReference mDatabase;
    Button btn10,btn20,btn50,btnnaptien,btnQr;
    TextView txttien,txtmasv;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate(R.layout.fragmenthomebv, container, false);

        anhxa();

        home hom = (home) getActivity();
        final String id = hom.getData();

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttien.setText("10000");
            }
        });
        btn20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttien.setText("20000");
            }
        });
        btn50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttien.setText("50000");
            }
        });

        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scancode();
            }
        });
        return view;
    }

    private void anhxa() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txttien = view.findViewById(R.id.txttiennap);
        txtmasv = view.findViewById(R.id.txtnhapmasv);
        btn10 = view.findViewById(R.id.btn10);
        btn20 = view.findViewById(R.id.btn20);
        btn50 = view.findViewById(R.id.btn50);
        btnnaptien = view.findViewById(R.id.btnnaptien);
        btnQr = view.findViewById(R.id.btnQr);
    }

    private void scancode() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(captureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scanning code");
        integrator.initiateScan();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Toast.makeText(getActivity(), "aaaaaaaaaaa", Toast.LENGTH_SHORT).show();
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() != null){
                try {
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    txtmasv.setText(jsonObject.getString("id"));
                    System.out.println("KKK: "+jsonObject.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
            }
        }
        else
        super.onActivityResult(requestCode, resultCode, data);
    }

}
