package com.example.dtuparking;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    TextView txttien,txttrangthai,txtsoluot,txtlanguixe;
    private DatabaseReference mDatabase;
    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragmenthome,container,false);

        txttien = (TextView) view.findViewById(R.id.txtsotien);
        txttrangthai = (TextView) view.findViewById(R.id.txtluotguixe);
        txtsoluot = (TextView) view.findViewById(R.id.txttrangthai);
        txtlanguixe = (TextView) view.findViewById(R.id.txtlanguixe);

        home hom = (home) getActivity();
        final String id = hom.getData();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("User/parkingMan/information/").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                String tien = snapshot.child("treasury").child("money").getValue().toString();
                txttien.setText(tien);

            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });




        return view;
    }
}
