package com.example.dtuparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView rcv_catagory;
    category_adapter cataAdapter;
    List<LSGIAODICH> listGD1,listGD2,listGD3,listGD4;
    private DatabaseReference mDatabase;
    Spinner spinner1;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.fragmenthistory,container,false);

        // ánh xạ
        rcv_catagory = view.findViewById(R.id.rcv_catalogy);
        cataAdapter = new category_adapter(getActivity());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        spinner1 = view.findViewById(R.id.spinner1);


        // quản lí các item thành hàng dọc
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rcv_catagory.setLayoutManager(linearLayoutManager);

        // đưa dữ liệu vào list
        final List<Category> listcata = new ArrayList<>();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.date, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        // combobox chọn lọc theo tuần hoặc tháng
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, final long idsv) {
                if(position==0){
                    lstheoThang();
                }
                if(position==1){
                    lstheoTuan();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    // sắp xếp các dữ liệu theo giày và giờ
    private void sortArray (List<LSGIAODICH> listgd){

        Collections.sort(listgd, new Comparator<LSGIAODICH>() {
            @Override
            public int compare( LSGIAODICH lsgiaodich, LSGIAODICH t1 ) {
                if(t1.getNgay().compareTo(lsgiaodich.getNgay())>0){
                    return 1;
                }
                else if(t1.getNgay().compareTo(lsgiaodich.getNgay())<0){
                    return -1;
                }
                else
                    return 0;
            }
        });


    }

    // lọc theo tháng
    private void lstheoThang(){
        // khai báo hàm ngày
        final Calendar cal = Calendar.getInstance();

        // lấy giá trị id từ home
        home hom = (home) getActivity();
        final String id = hom.getData();

        if(CheckInternet.isConnect(getContext())){
            final List<Category> listcata = new ArrayList<>();
            listGD1 = new ArrayList<>();
            listGD2 = new ArrayList<>();
            listGD3 = new ArrayList<>();
            listGD4 = new ArrayList<>();

            // tạo biến i cố định để không tự động tăng dữ lieeuju firebase
            final int[] i = {0};

            final String bandaguixe = getResources().getString(R.string.bandaguixe);
            final String bandaduocnaptien = getResources().getString(R.string.bandaduocnaptien);
            final String bandaguitiencho = getResources().getString(R.string.bandaguitiencho);

            Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();

            // truy vấn tới thông tin để xem là sinh viên hay giáo viên
            mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listcata.removeAll(listcata);
                    final DecimalFormat formatter = new DecimalFormat("###,###,###");
                    try {
                        // xét là sinh viên hay giảng viên
                        if(snapshot.child("position").getValue().toString().equals("3")){
                            mDatabase.child("History/parkingMan/").child("moneyOut").child(id).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    String magd = snapshot.getKey().toString();
                                    String dateGet = snapshot.child("dateGet").getValue().toString();
                                    String payMoney = snapshot.child("payMoney").getValue().toString();
                                    String place = snapshot.child("place").getValue().toString();

                                    Integer tien = Integer.parseInt(payMoney);

                                    String coso = "";
                                    if(place.equals("0")){
                                        coso = "Quang Trung";
                                    }
                                    else if(place.equals("1")){
                                        coso = "Hòa Khánh";
                                    }
                                    else if(place.equals("2")){
                                        coso = "Nguyễn Văn Linh";
                                    }

                                    // chuyển string thành date
                                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM");
                                    Date date1 = null;
                                    Date date2 = null;
                                    Date date3 = null;
                                    Date date4 = null;
                                    Date date5 = null;
                                    Date datengay = null;
                                    try {
                                        date1 = sdfgoc.parse(dateGet);
                                        date2 = sdf.parse(""+(cal.get(Calendar.MONTH)+1));
                                        date3 = sdf.parse(""+(cal.get(Calendar.MONTH)));
                                        date4 = sdf.parse(""+(cal.get(Calendar.MONTH)-1));
                                        date5 = sdf.parse(""+(cal.get(Calendar.MONTH)-2));
                                        datengay = sdfgoc.parse(dateGet);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

//                                    String guixe = getResources().getString(R.string.bandaguixe)+" "+ coso;
                                    if(sdf.format(date1).compareTo(sdf.format(date2))==0){

                                        listGD1.add(new LSGIAODICH(R.drawable.ttienra, bandaguixe, coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                        listGD2.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                        listGD3.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                        listGD4.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }


                                    if(i[0] ==0){
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)+1),listGD1));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)),listGD2));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-1),listGD3));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-2),listGD4));

                                        cataAdapter.setData(listcata);
                                        rcv_catagory.setAdapter(cataAdapter);
                                        i[0]++;
                                    }

                                    sortArray(listGD1);
                                    sortArray(listGD2);
                                    sortArray(listGD3);
                                    sortArray(listGD4);




                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            mDatabase.child("History/parkingMan/").child("moneyIn").child(id).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    String magd = snapshot.getKey().toString();
                                    String datePay = snapshot.child("dateSend").getValue().toString();
                                    String payMoney = snapshot.child("payMoney").getValue().toString();
                                    String IdSender = snapshot.child("idSender").getValue().toString();

                                    Integer tien = Integer.parseInt(payMoney);

                                    // chuyển string thành date
                                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM");
                                    Date date1 = null;
                                    Date date2 = null;
                                    Date date3 = null;
                                    Date date4 = null;
                                    Date date5 = null;
                                    Date datengay = null;
                                    try {
                                        date1 = sdfgoc.parse(datePay);
                                        date2 = sdf.parse(""+(cal.get(Calendar.MONTH)+1));
                                        date3 = sdf.parse(""+(cal.get(Calendar.MONTH)));
                                        date4 = sdf.parse(""+(cal.get(Calendar.MONTH)-1));
                                        date5 = sdf.parse(""+(cal.get(Calendar.MONTH)-2));
                                        datengay = sdfgoc.parse(datePay);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                                        listGD1.add(new LSGIAODICH(R.drawable.tienvao, bandaduocnaptien,"", datengay, "+ "+formatter.format(tien)+" VNĐ", magd, id, "#7C007C"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                        listGD2.add(new LSGIAODICH(R.drawable.tienvao, bandaduocnaptien,"", datengay, "+ "+formatter.format(tien)+" VNĐ", magd, id, "#7C007C"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                        listGD3.add(new LSGIAODICH(R.drawable.tienvao, bandaduocnaptien,"", datengay, "+ "+formatter.format(tien)+" VNĐ", magd, id, "#7C007C"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                        listGD4.add(new LSGIAODICH(R.drawable.tienvao, bandaduocnaptien,"", datengay, "+ "+formatter.format(tien)+" VNĐ", magd, id, "#7C007C"));
                                    }


                                    if(i[0] ==0){
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)+1),listGD1));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)),listGD2));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-1),listGD3));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-2),listGD4));

                                        cataAdapter.setData(listcata);
                                        rcv_catagory.setAdapter(cataAdapter);
                                        i[0]++;
                                    }

                                    sortArray(listGD1);
                                    sortArray(listGD2);
                                    sortArray(listGD3);
                                    sortArray(listGD4);

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else if(snapshot.child("position").getValue().toString().equals("2")){
                            mDatabase.child("History/parkingMan/").child("moneyOut").child(id).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    String magd = snapshot.getKey().toString();
                                    String dateGet = snapshot.child("dateGet").getValue().toString();
                                    String place = snapshot.child("place").getValue().toString();

                                    String coso = "";
                                    if(place.equals("0")){
                                        coso = "Quang Trung";
                                    }
                                    else if(place.equals("1")){
                                        coso = "Hòa Khánh";
                                    }
                                    else if(place.equals("2")){
                                        coso = "Nguyễn Văn Linh";
                                    }

                                    // chuyển string thành date
                                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM");
                                    Date date1 = null;
                                    Date date2 = null;
                                    Date date3 = null;
                                    Date date4 = null;
                                    Date date5 = null;
                                    Date datengay = null;
                                    try {
                                        date1 = sdfgoc.parse(dateGet);
                                        date2 = sdf.parse(""+(cal.get(Calendar.MONTH)+1));
                                        date3 = sdf.parse(""+(cal.get(Calendar.MONTH)));
                                        date4 = sdf.parse(""+(cal.get(Calendar.MONTH)-1));
                                        date5 = sdf.parse(""+(cal.get(Calendar.MONTH)-2));
                                        datengay = sdfgoc.parse(dateGet);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                                        listGD1.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                        listGD2.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                        listGD3.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                        listGD4.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }

                                    sortArray(listGD1);
                                    sortArray(listGD2);
                                    sortArray(listGD3);
                                    sortArray(listGD4);

                                    if(i[0] ==0){
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)+1),listGD1));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)),listGD2));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-1),listGD3));
                                        listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-2),listGD4));

                                        cataAdapter.setData(listcata);
                                        rcv_catagory.setAdapter(cataAdapter);
                                        i[0]++;
                                    }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }catch (Exception e){
                        mDatabase.child("History/guard/").child(id).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String magd = snapshot.getKey().toString();
                                String price = snapshot.child("payMoney").getValue().toString();
                                String idStudent = snapshot.child("idStudent").getValue().toString();
                                String dated = snapshot.child("dateSend").getValue().toString();

                                Integer tien = Integer.parseInt(price);

                                // chuyển string thành date
                                SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat sdf = new SimpleDateFormat("MM");
                                Date date1 = null;
                                Date date2 = null;
                                Date date3 = null;
                                Date date4 = null;
                                Date date5 = null;
                                Date datengay = null;
                                try {
                                    date1 = sdfgoc.parse(dated);
                                    date2 = sdf.parse(""+(cal.get(Calendar.MONTH)+1));
                                    date3 = sdf.parse(""+(cal.get(Calendar.MONTH)));
                                    date4 = sdf.parse(""+(cal.get(Calendar.MONTH)-1));
                                    date5 = sdf.parse(""+(cal.get(Calendar.MONTH)-2));
                                    datengay = sdfgoc.parse(dated);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                                    listGD1.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }
                                else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                    listGD2.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }
                                else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                    listGD3.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }
                                else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                    listGD4.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }

                                sortArray(listGD1);
                                sortArray(listGD2);
                                sortArray(listGD3);
                                sortArray(listGD4);

                                if(i[0] ==0){
                                    listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)+1),listGD1));
                                    listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)),listGD2));
                                    listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-1),listGD3));
                                    listcata.add(new Category(getResources().getString(R.string.thang)+" "+(cal.get(Calendar.MONTH)-2),listGD4));

                                    cataAdapter.setData(listcata);
                                    rcv_catagory.setAdapter(cataAdapter);
                                    i[0]++;
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.ktramang), Toast.LENGTH_SHORT).show();
        }

    }

    // lọc theo tuần
    private void lstheoTuan(){
        // khai báo hàm ngày
        final Calendar cal = Calendar.getInstance();

        // lấy giá trị id từ home
        home hom = (home) getActivity();
        final String id = hom.getData();

        if(CheckInternet.isConnect(getContext())){
            final List<Category> listcata = new ArrayList<>();
            listGD1 = new ArrayList<>();
            listGD2 = new ArrayList<>();
            listGD3 = new ArrayList<>();
            listGD4 = new ArrayList<>();

            // tạo biến i cố định để không tự động tăng dữ lieeuju firebase
            final int[] i = {0};
            final DecimalFormat formatter = new DecimalFormat("###,###,###");

            final String bandaguixe = getResources().getString(R.string.bandaguixe);
            final String bandaduocnaptien = getResources().getString(R.string.bandaduocnaptien);
            final String bandaguitiencho = getResources().getString(R.string.bandaguitiencho);

            // truy vấn tới thông tin để xem là sinh viên hay giáo viên
            mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listcata.removeAll(listcata);
                    try {
                        // xét là sinh viên hay giảng viên
                        if(snapshot.child("position").getValue().toString().equals("3")){
                            mDatabase.child("History/parkingMan/").child("moneyOut").child(id).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    String magd = snapshot.getKey().toString();
                                    String dateGet = snapshot.child("dateGet").getValue().toString();
                                    String payMoney = snapshot.child("payMoney").getValue().toString();
                                    String place = snapshot.child("place").getValue().toString();

                                    Integer tien = Integer.parseInt(payMoney);

                                    String coso = "";
                                    if(place.equals("0")){
                                        coso = "Quang Trung";
                                    }
                                    else if(place.equals("1")){
                                        coso = "Hòa Khánh";
                                    }
                                    else if(place.equals("2")){
                                        coso = "Nguyễn Văn Linh";
                                    }

                                    // chuyển string thành date
                                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat sdf = new SimpleDateFormat("ww");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM");
                                    Date date1 = null;
                                    Date date2 = null;
                                    Date date3 = null;
                                    Date date4 = null;
                                    Date date5 = null;
                                    Date datengay = null;
                                    try {
                                        date1 = sdfgoc.parse(dateGet);
                                        date2 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)));
                                        date3 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-1));
                                        date4 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-2));
                                        date5 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-3));
                                        datengay = sdfgoc.parse(dateGet);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                                        listGD1.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe,coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                        listGD2.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe,coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                        listGD3.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe,coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                        listGD4.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe,coso,datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#C63131"));
                                    }

                                    sortArray(listGD1);
                                    sortArray(listGD2);
                                    sortArray(listGD3);
                                    sortArray(listGD4);

                                    if(i[0] ==0){
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)),listGD1));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-1),listGD2));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-2),listGD3));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-3),listGD4));

                                        cataAdapter.setData(listcata);
                                        rcv_catagory.setAdapter(cataAdapter);
                                        i[0]++;
                                    }


                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            mDatabase.child("History/parkingMan/").child("moneyIn").child(id).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    String magd = snapshot.getKey().toString();
                                    String datePay = snapshot.child("dateSend").getValue().toString();
                                    String payMoney = snapshot.child("payMoney").getValue().toString();
                                    String IdSender = snapshot.child("idSender").getValue().toString();

                                    Integer tien = Integer.parseInt(payMoney);

                                    // chuyển string thành date
                                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat sdf = new SimpleDateFormat("ww");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM");
                                    Date date1 = null;
                                    Date date2 = null;
                                    Date date3 = null;
                                    Date date4 = null;
                                    Date date5 = null;
                                    Date datengay = null;
                                    try {
                                        date1 = sdfgoc.parse(datePay);
                                        date2 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)));
                                        date3 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-1));
                                        date4 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-2));
                                        date5 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-3));
                                        datengay = sdfgoc.parse(datePay);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                                        listGD1.add(new LSGIAODICH(R.drawable.tienvao,bandaduocnaptien,"",datengay,"+ "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                        listGD2.add(new LSGIAODICH(R.drawable.tienvao,bandaduocnaptien,"",datengay,"+ "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                        listGD3.add(new LSGIAODICH(R.drawable.tienvao,bandaduocnaptien,"",datengay,"+ "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                        listGD4.add(new LSGIAODICH(R.drawable.tienvao,bandaduocnaptien,"",datengay,"+ "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                    }


                                    if(i[0] ==0){
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)),listGD1));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-1),listGD2));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-2),listGD3));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-3),listGD4));

                                        cataAdapter.setData(listcata);
                                        rcv_catagory.setAdapter(cataAdapter);
                                        i[0]++;
                                    }

                                    sortArray(listGD1);
                                    sortArray(listGD2);
                                    sortArray(listGD3);
                                    sortArray(listGD4);

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else if(snapshot.child("position").getValue().toString().equals("2")){
                            mDatabase.child("History/parkingMan/").child("moneyOut").child(id).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    String magd = snapshot.getKey().toString();
                                    String dateGet = snapshot.child("dateGet").getValue().toString();
                                    String place = snapshot.child("place").getValue().toString();


                                    String coso = "";
                                    if(place.equals("0")){
                                        coso = "Quang Trung";
                                    }
                                    else if(place.equals("1")){
                                        coso = "Hòa Khánh";
                                    }
                                    else if(place.equals("2")){
                                        coso = "Nguyễn Văn Linh";
                                    }


                                    // chuyển string thành date
                                    SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat sdf = new SimpleDateFormat("w");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM");
                                    Date date1 = null;
                                    Date date2 = null;
                                    Date date3 = null;
                                    Date date4 = null;
                                    Date date5 = null;
                                    Date datengay = null;
                                    try {
                                        date1 = sdfgoc.parse(dateGet);
                                        date2 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)));
                                        date3 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-1));
                                        date4 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-2));
                                        date5 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-3));
                                        datengay = sdfgoc.parse(dateGet);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                                        listGD1.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                        listGD2.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                        listGD3.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }
                                    else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                        listGD4.add(new LSGIAODICH(R.drawable.ttienra,bandaguixe, coso,datengay,"null",magd,id,"#C63131"));
                                    }

                                    sortArray(listGD1);
                                    sortArray(listGD2);
                                    sortArray(listGD3);
                                    sortArray(listGD4);

                                    if(i[0] ==0){
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)),listGD1));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-1),listGD2));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-2),listGD3));
                                        listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-3),listGD4));

                                        cataAdapter.setData(listcata);
                                        rcv_catagory.setAdapter(cataAdapter);
                                        i[0]++;
                                    }


                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }catch (Exception e){
                        mDatabase.child("History/guard/").child(id).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String magd = snapshot.getKey().toString();
                                String price = snapshot.child("payMoney").getValue().toString();
                                String idStudent = snapshot.child("idStudent").getValue().toString();
                                String dated = snapshot.child("dateSend").getValue().toString();

                                Integer tien = Integer.parseInt(price);

                                // chuyển string thành date
                                SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat sdf = new SimpleDateFormat("ww");
                                Date date1 = null;
                                Date date2 = null;
                                Date date3 = null;
                                Date date4 = null;
                                Date date5 = null;
                                Date datengay = null;
                                try {
                                    date1 = sdfgoc.parse(dated);
                                    date2 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)));
                                    date3 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-1));
                                    date4 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-2));
                                    date5 = sdf.parse(""+(cal.get(Calendar.WEEK_OF_YEAR)-3));
                                    datengay = sdfgoc.parse(dated);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if(sdf.format(date1).compareTo(sdf.format(date2))==0){
                                    listGD1.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }
                                else if(sdf.format(date1).compareTo(sdf.format(date3))==0){
                                    listGD2.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }
                                else if(sdf.format(date1).compareTo(sdf.format(date4))==0){
                                    listGD3.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }
                                else if(sdf.format(date1).compareTo(sdf.format(date5))==0){
                                    listGD4.add(new LSGIAODICH(R.drawable.tienvao,bandaguitiencho,"",datengay,"- "+formatter.format(tien)+" VNĐ",magd,id,"#7C007C"));
                                }

                                sortArray(listGD1);
                                sortArray(listGD2);
                                sortArray(listGD3);
                                sortArray(listGD4);

                                if(i[0] ==0){
                                    listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)),listGD1));
                                    listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-1),listGD2));
                                    listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-2),listGD3));
                                    listcata.add(new Category(getResources().getString(R.string.tuan)+" "+(cal.get(Calendar.WEEK_OF_YEAR)-3),listGD4));

                                    cataAdapter.setData(listcata);
                                    rcv_catagory.setAdapter(cataAdapter);
                                    i[0]++;
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.ktramang), Toast.LENGTH_SHORT).show();
        }

    }
}
