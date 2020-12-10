package com.example.dtuparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    ImageView imgavata,imgmenu, imgavatamenu;
    TextView txtten,txtmasv, txtmasvmenu,txttensvmenu;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton fab;
    DatabaseReference mDatabase;

    int id1 = 0;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        anhxa();

        // nhận dữ liệu từ login
        final Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");



          //đọc tên sinh viên mã sinh viên từ firebase
        mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if(snapshot.child("position").getValue().toString().equals("3")){
                        txtten.setText(snapshot.child("name").getValue().toString());
                        txtmasv.setText(snapshot.child("idStudent").getValue().toString());
                        new LoadImage().execute(snapshot.child("avatar").getValue().toString());
                    }
                    else if(snapshot.child("position").getValue().toString().equals("2")){
                        txtten.setText(snapshot.child("name").getValue().toString());
                        txtmasv.setText(snapshot.child("idLecturers").getValue().toString());
                        new LoadImage().execute(snapshot.child("avatar").getValue().toString());
                    }
                }catch (Exception e){
                    mDatabase.child("User/information/guard/").child(id).addValueEventListener(new ValueEventListener() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            txtten.setText(snapshot.child("name").getValue().toString());
                            txtmasv.setText(snapshot.child("idGuard").getValue().toString());
                            new LoadImage().execute(snapshot.child("avatar").getValue().toString());
                            fab.setVisibility(View.GONE);
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

        // bắt sự kiện khi nhấn vào menu và mở menu
        imgmenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick( View view ) {
                drawerLayout.openDrawer(Gravity.END);
            }
        });
        // khi nhấn vào item thì có hiệu ứng
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        // bắt sự kiện khi nhấn vào nút QR
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(home.this,MaQRCode.class);
                        intent.putExtra("idSinhVien",id);
                        Pair[] pairs = new Pair[1];
                        pairs[0] = new Pair<View,String>(fab,"button_QR");

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(home.this,pairs);
                            startActivity(intent,options.toBundle());
                        }
                        else {
                            startActivity(intent);
                        }

                    }
                },100);
            }
        });

        mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    if(snapshot.child("position").getValue().toString().equals("3")){
                        setUpViewPager();
                    }
                    else if(snapshot.child("position").getValue().toString().equals("2")){
                        setUpViewPager();
                    }
                }catch (Exception e){
                    setUpViewPagerBV();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // bắt sự kiện chuyển trang
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.trangchu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.lichsu:
                        viewPager.setCurrentItem(1);
                        break;

                }
                return true;
            }
        });
        UpdateHeader();

        //  bắt sự kiện thêm lịch sử giao dịch thì sẽ có thông báo
        mDatabase.child("History/parkingMan/").child("moneyOut").child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey().toString();
                String dateGet = snapshot.child("dateGet").getValue().toString();
                SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datengay = null;
                try {
                    datengay = sdfgoc.parse(dateGet);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Boolean isNoti = (Boolean) snapshot.child("isNoti").getValue();
                // nếu bằng false thì sẻ có thông báo sau khi thông báo sẽ chuyển thành true
                if(!isNoti){
                    noficationGuiXe(sdfgoc.format(datengay));
                    mDatabase.child("History/parkingMan/").child("moneyOut").child(id).child(key).child("isNoti").setValue(true);
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

        //  bắt sự kiện thêm lịch sử giao dịch thì sẽ có thông báo
        mDatabase.child("History/parkingMan/").child("moneyIn").child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey().toString();
                String dateGet = snapshot.child("dateSend").getValue().toString();
                String payMoney = snapshot.child("payMoney").getValue().toString();
                SimpleDateFormat sdfgoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datengay = null;
                try {
                    datengay = sdfgoc.parse(dateGet);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Integer tien = Integer.parseInt(payMoney);
                Boolean isNoti = (Boolean) snapshot.child("isNoti").getValue();
                // nếu bằng false thì sẻ có thông báo sau khi thông báo sẽ chuyển thành true
                if(!isNoti){
                    noficationNaptien(tien/1000+".000 đ",sdfgoc.format(datengay));
                    mDatabase.child("History/parkingMan/").child("moneyIn").child(id).child(key).child("isNoti").setValue(true);
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

    // hiển thị thông báo nofication khi có lịch sử gửi xe thêm vào
    private void noficationGuiXe(String ngay) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentText("DTU PARKING")
                .setSmallIcon(R.drawable.android_logo)
                .setAutoCancel(true)
                .setContentText(getResources().getString(R.string.bandathanhtoanluc)+" "+ ngay);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Random random = new Random();
        int value = random.nextInt(999-1) + 1;
        managerCompat.notify(value,builder.build());
    }

    // hiển thị thông báo nofication khi được nạp tiền
    private void noficationNaptien(String tien,String ngay) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentText("DTU PARKING")
                .setSmallIcon(R.drawable.android_logo)
                .setAutoCancel(true)
                .setContentText(getResources().getString(R.string.bandaduocnap)+" "+tien+" "+getResources().getString(R.string.tubaoveluc)+" "+ngay);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Random random = new Random();
        int value = random.nextInt(9999-1001) + 1;
        managerCompat.notify(value,builder.build());
    }

    // ánh xạ các đối tượng
    private void anhxa() {
        // ánh xạ
        imgmenu = (ImageView) findViewById(R.id.imgmenu) ;
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        imgavata = (ImageView) findViewById(R.id.imgavata) ;
        txtten = (TextView) findViewById(R.id.txtten);
        txtmasv = (TextView) findViewById(R.id.txtmasv);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // viết hàm tạo class để đọc link hình
    private class LoadImage extends AsyncTask<String, Void , Bitmap> {
        Bitmap bitmaphinh = null;
        @Override
        protected Bitmap doInBackground( String... strings ) {
            try{
                URL url = new URL(strings[0]);

                InputStream inputStream = url.openConnection().getInputStream();

                bitmaphinh = BitmapFactory.decodeStream(inputStream);



            }catch (Exception e){
                e.printStackTrace();
            }
            return bitmaphinh;
        }

        @Override
        protected void onPostExecute( Bitmap bitmap ) {
            super.onPostExecute(bitmap);
            imgavata.setImageBitmap(bitmap);
        }
    }

    // viết hàm tạo class để đọc link hình
    private class LoadImagemenu extends AsyncTask<String, Void , Bitmap> {
        Bitmap bitmaphinh = null;
        @Override
        protected Bitmap doInBackground( String... strings ) {
            try{
                URL url = new URL(strings[0]);

                InputStream inputStream = url.openConnection().getInputStream();

                bitmaphinh = BitmapFactory.decodeStream(inputStream);



            }catch (Exception e){
                e.printStackTrace();
            }
            return bitmaphinh;
        }

        @Override
        protected void onPostExecute( Bitmap bitmap ) {
            super.onPostExecute(bitmap);
            imgavatamenu.setImageBitmap(bitmap);
        }
    }

    // bắt sự kiện chọn và chuyển đến trang khác của sinh viên
    private void setUpViewPager() {

        ViewAdapter viewAdapter = new ViewAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {

            }

            @Override
            public void onPageSelected( int position ) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.trangchu).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.lichsu).setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged( int state ) {

            }
        });
    }

    // bắt sự kiện chọn và chuyển đến trang khác của bảo vệ
    private void setUpViewPagerBV() {
        ViewAdapterBV viewAdapterbv = new ViewAdapterBV(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewAdapterbv);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {

            }

            @Override
            public void onPageSelected( int position ) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.trangchu).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.lichsu).setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged( int state ) {

            }
        });
    }

    // tạo hàm để lưu dữ liệu cố định
    public String getData(){
        // nhận dữ liệu từ mainactivity
        Intent intent = getIntent();
        String id = intent.getStringExtra("idSinhVien");
        return id;
    }

    // bắt sự kiện khi click vào cái item và chuyển đến các trang khác
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // nhận dữ liệu từ login
        Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");

        switch(menuItem.getItemId()){
            case R.id.navthongtin:
                Intent intent2 = new Intent(home.this,ThongTinSV.class);
                intent2.putExtra("idSinhVien",id);
                startActivity(intent2);
                break;
            case R.id.navphanhoi:
                Intent intent3 = new Intent(home.this,PhanHoi.class);
                intent3.putExtra("idSinhVien",id);
                startActivity(intent3);
                break;
            case R.id.navlienlac:
                Intent intent4 = new Intent(home.this,LienLac.class);
                startActivity(intent4);
                break;
            case R.id.navdangxuat:
                XacNhanDangXuat();
                break;
        }

        return true;
    }

    // hỏi khi nhấn vào đăng xuất
    private  void XacNhanDangXuat(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(home.this);
        alertDialog.setTitle(getResources().getString(R.string.thongbao));
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage(getResources().getString(R.string.Bancomuondangxuat));

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {
                Intent intent = new Intent(home.this,login.class);
                intent.putExtra("ktra",false);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {

            }
        });
        alertDialog.show();
    }

    // cập nhật phần header
    private void UpdateHeader(){
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.navigationview);
        View view = navigationView1.getHeaderView(0);
        imgavatamenu = (ImageView) view.findViewById(R.id.imgavatamenu2) ;
        txttensvmenu = (TextView) view.findViewById(R.id.txttensvmenu2);
        txtmasvmenu = (TextView) view.findViewById(R.id.txtmasvmenu2);

        // nhận dữ liệu từ login
        Intent intent = getIntent();
        final String id = intent.getStringExtra("idSinhVien");
        // đọc tên sinh viên mã sinh viên từ firebase
        mDatabase.child("User/information/parkingMan/").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if(snapshot.child("position").getValue().toString().equals("3")){
                        txttensvmenu.setText(snapshot.child("name").getValue().toString());
                        txtmasvmenu.setText(snapshot.child("idStudent").getValue().toString());
                        new LoadImagemenu().execute(snapshot.child("avatar").getValue().toString());
                    }
                    else if(snapshot.child("position").getValue().toString().equals("2")){
                        txttensvmenu.setText(snapshot.child("name").getValue().toString());
                        txtmasvmenu.setText(snapshot.child("idLecturers").getValue().toString());
                        new LoadImagemenu().execute(snapshot.child("avatar").getValue().toString());
                    }
                }catch (Exception e){
                    mDatabase.child("User/information/guard/").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            txttensvmenu.setText(snapshot.child("name").getValue().toString());
                            txtmasvmenu.setText(snapshot.child("idGuard").getValue().toString());
                            new LoadImagemenu().execute(snapshot.child("avatar").getValue().toString());
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

    // xử lí cho phép việc xử cho phép dùng camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    // thoát ứng dụng
    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder b =new androidx.appcompat.app.AlertDialog.Builder(home.this);
        b.setTitle("Question");
        b.setMessage("Are you sure you want to exit?");
        b.setPositiveButton("Yes", new DialogInterface. OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
                System.exit(0);
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        b.create().show();
    }
}