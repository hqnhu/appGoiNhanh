package com.example.appgoinhanh;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appgoinhanh.helper.AllContactsAdapter;
import com.example.appgoinhanh.helper.ContactVO;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    SearchView search;
    TextView view_name;

    private ListView listview;
    private ArrayList<ContactVO> listdata;

    AllContactsAdapter adapter_search ;

    Uri pURI ;
    String url_path = "";

    CustomProgressDialog progressdialog;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
        listdata = new ArrayList<>();

        search = (SearchView) findViewById(R.id.searchView);

        // gia Navigation drawer
        RelativeLayout re_menu = (RelativeLayout) findViewById(R.id.re_menu);
        re_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    final AlertDialog.Builder popDialog = new AlertDialog.Builder(MainActivity.this);

                    final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    final View Viewlayout = inflater.inflate(R.layout.bg_load_menu, null);

                    popDialog.setView(Viewlayout);

                    popDialog.create();

                    final AlertDialog dg = popDialog.show();

                    RelativeLayout re_home_now = (RelativeLayout) Viewlayout.findViewById(R.id.re_home_now);
                    RelativeLayout re_share = (RelativeLayout) Viewlayout.findViewById(R.id.re_share);
                    RelativeLayout re_Cancel = (RelativeLayout) Viewlayout.findViewById(R.id.re_Cancel);



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dg.getWindow().setLayout(650,1200);
                        dg.getWindow().setGravity(Gravity.TOP | Gravity.LEFT | Gravity.BOTTOM);
                    }else {
                        dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dg.getWindow().setLayout(470,1200);
                        dg.getWindow().setGravity(Gravity.TOP |Gravity.LEFT | Gravity.BOTTOM);
                        re_Cancel.setVisibility(View.VISIBLE);
                    }

                    //chức năng trở lại màn hình chính và cập nhật lại danh sách
                    re_home_now.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dg.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });

                    //chức năng chia sẻ
                    re_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //final String appPackageName = context.getPackageName();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/hqnhu/appGoiNhanh");
                            sendIntent.setType("text/plain");
                            context.startActivity(sendIntent);
                        }
                    });

                    re_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dg.dismiss();
                        }
                    });

                }catch (Exception ep){

                }

            }
        });


        //gia nut info
        RelativeLayout re_info = (RelativeLayout) findViewById(R.id.re_info);
        re_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder popDialog = new AlertDialog.Builder(MainActivity.this);

                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View Viewlayout = inflater.inflate(R.layout.custom_popup_info, null);

                popDialog.setView(Viewlayout);
                popDialog.create();

                final AlertDialog dg = popDialog.show();
                TextView txtclose;
//                Button btnfollow;
//                LinearLayout li_more , li_rate , li_share ;

                txtclose =(TextView) Viewlayout.findViewById(R.id.txtclose);

                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dg.dismiss();
                    }
                });

                dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dg.show();
            }
        });



        //==== ArrayAdapter của Listview
//         ArrayAdapter<ContactVO> adapter = new ArrayAdapter<ContactVO>(this, android.R.layout.activity_list_item, listdata);

        new _LoadDulieu().execute();

    }//end of onCreate

    private ArrayList<ContactVO> getContacts(Context ctx) {

        ArrayList<ContactVO> list = new ArrayList<>();
        if(list.size()>0){
            list.clear();
        } // xóa dữ liệu
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    SharedPreferences prefs = getSharedPreferences("save_image", Context.MODE_PRIVATE);

                    while (cursorInfo.moveToNext()) {
                        Bitmap photo = null;
                        Bitmap cr_photo = null;
                        Bitmap circular_mIcon11 = null;
                        pURI = null;
                        ContactVO info = new ContactVO();

                        //if(pURI!= null){
                        // Lấy đường dẫn lưu file từ Main_ChiTiet Qua
                        url_path = prefs.getString(id, "");
                        pURI = Uri.parse(url_path); // Convert duong dan string thành uri

                        // Uri uris = ContentUris.withAppendedId(pURI, Long.parseLong(id));
                        //InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(), uris);
                        try {
                            File imgFile = new File(url_path);

                            if(imgFile.exists()){
                                photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                                cr_photo = Bitmap.createScaledBitmap(photo, 140 , 140 , false);

                                circular_mIcon11 = Url_config.getRoundedCornerBitmap(cr_photo, 20);

                            }

                        } catch (Exception e) {
                            //photo = null;
                        }

                        info.setContactId(id);
                        info.setContactName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        info.setContactNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        info.setContactImage(circular_mIcon11);
                        info.setPhotoURI(pURI);
                        list.add(info);


                    }
                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }


    private class _LoadDulieu extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() { // Hàm này luôn luôn chạy đầu tiên quá trình load bắt đâu "Start"
            progressdialog = new CustomProgressDialog(MainActivity.this); // hiển thị vòng tròn xin đợi
            progressdialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // chạy ẩn = hàm này chạy xong mới tới hàm onPreExecute
            listdata = getContacts(MainActivity.this);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            adapter_search = new AllContactsAdapter(getApplicationContext(), listdata);
            listview.setAdapter(adapter_search);
            //chức năng search khi chọn
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter_search.filter_name(newText);
                    return true;
                }
            });

            if(listdata.size()>0){

                // Thuật toán sấp xếp ds danh bạ theo tên ContactName
                Collections.sort(listdata, new Comparator<ContactVO>() {
                    @Override public int compare(ContactVO p1, ContactVO p2) {
                        return p1.getContactName().toLowerCase().compareTo(p2.getContactName().toLowerCase()); // Ascending
                    }

                });

                listview.setAdapter(new AllContactsAdapter(getApplicationContext(),listdata));
                listview.setDivider(null);
                listview.setDividerHeight(0);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), Main_ThongTin_ChiTiet_DanhBa.class);
                        intent.putExtra("Id_danhba", listdata.get(position).getContactId());
                        intent.putExtra("Name_danhba", listdata.get(position).getContactName()); // string
                        intent.putExtra("Phone_number", listdata.get(position).getContactNumber()); // string
                        startActivity(intent);
                    }
                });
            }
            progressdialog.dismiss();
            //Cuối cùng là hàm này - thực hiện khi tiến trình kết thúc
            super.onPostExecute(aVoid);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //cursor.close();
    }

    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        super.onBackPressed();

    }


}