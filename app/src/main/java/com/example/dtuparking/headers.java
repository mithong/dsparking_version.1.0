package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class headers extends AppCompatActivity {

    ImageView  imgavatamenu;
    TextView txtmasvmenu,txttensvmenu;
    DatabaseReference mDatabase;

    DrawerLayout drawerLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_headers);
        System.out.println("KKK: "+"kkkkkkkkk");


    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_headers);
//
//        imgavatamenu = (ImageView) findViewById(R.id.imgavatamenu2) ;
//        txttensvmenu = (TextView) findViewById(R.id.txttensvmenu2);
//        txtmasvmenu = (TextView) findViewById(R.id.txtmasvmenu2);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        home hom = new home() ;
//        final String id = hom.getData();
//        Log.d("KKKK",id);
//        mDatabase.child("User/parkingMan/information/").child(id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                System.out.println("KKK: "+snapshot.child("name").getValue().toString());
//                System.out.println("KKK1: "+snapshot.child("idStudent").getValue().toString());
//                txttensvmenu.setText(snapshot.child("name").getValue().toString());
//                txtmasvmenu.setText(snapshot.child("idStudent").getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }