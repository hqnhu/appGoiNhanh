package com.example.appgoinhanh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.Random;


public class Main_ThongTin_ChiTiet_DanhBa extends AppCompatActivity {

    private String id = "";
    private String name = "";
    private String phoneNumber = "";
    String selected_ImageGetPath = "";

    RelativeLayout re_back, re_done, re_call, re_sms, re_action1, re_action2;

    private TextView tv_name, tv_numberphone, tv_name_sim;

    private ImageView image_anhdaidien, img_camera;


    // Camera and gallery
    // Thông báo
    File file;
    private Uri selectedImageUri;

    Bitmap bm = null;

    String Url_duongdananh = "";

    // Content
    private Context context = Main_ThongTin_ChiTiet_DanhBa.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thong_tin_chi_tiet_danh_ba);

        initPermission();

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {
            id = b.getString("Id_danhba");
            name = b.getString("Name_danhba");
            phoneNumber = b.getString("Phone_number");
            // selected_ImageGetPath = b.getString("Image_Path"); // đường dẫn Uri kiểu String
        }

        //nut tro ve tren menu
        re_back = (RelativeLayout) findViewById(R.id.re_back);
        re_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (Exception po) {
                }
            }
        });

        //tao short cut tren home
        re_done = (RelativeLayout) findViewById(R.id.re_done);
        re_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //lay hinh anh hien crop
                    Bitmap scaledBitmap = null;
                    Bitmap circular_mIcon11 = null;
                    bm = ((BitmapDrawable) image_anhdaidien.getDrawable()).getBitmap();
                    scaledBitmap = Bitmap.createScaledBitmap(bm, 120, 120, false);
                    circular_mIcon11 = Url_config.getRoundedCornerBitmap(scaledBitmap, 60);

                    // Code tạo ra Shortcut icon màn hình home, nếu thiết bị có hỗ trợ
                    if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {

                        //Cach tạo tùy theo SDK version
                        if (Build.VERSION.SDK_INT >= 24) { //android 7.0 trở lên
                            ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(context, id)
                                    .setIntent(new Intent(context, MainActivity.class).setAction(Intent.ACTION_MAIN)) // !!! intent's action must be set on oreo
                                    .setShortLabel(name)
                                    .setIcon(IconCompat.createWithBitmap(circular_mIcon11))
                                    .setIntent(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)))
                                    .build();
                            ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, null);
                        } else {

                            Intent shortcutIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //shortcutIntent.setAction(Intent.ACTION_MAIN);

                            Intent addIntent = new Intent();
                            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
                            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, circular_mIcon11);

                            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                            getApplicationContext().sendBroadcast(addIntent);
                            Toast.makeText(getApplicationContext(), R.string.shortcut_contact, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.shortcut_not_supported, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception po) {

                }
            }
        });

        //gui tin nhan truc tiep
        re_sms = (RelativeLayout) findViewById(R.id.re_sms);
        re_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri smsUri = Uri.parse("tel:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                intent.putExtra("address", phoneNumber);
                intent.putExtra("sms_body", getString(R.string.send_sms));
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
            }
        });

        // whatsApp
        re_action1 = (RelativeLayout) findViewById(R.id.re_action1);
        re_action1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                try {
                    PackageManager packageManager = getApplicationContext().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode("", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    } else {
                        final Dialog dialog = new Dialog(Main_ThongTin_ChiTiet_DanhBa.this, R.style.FullHeightDialog); //this is a reference to the style above
                        dialog.setContentView(R.layout.custom_layout); //I saved the xml file above as yesnomessage.xml
                        dialog.setCancelable(true);

                        TextView message = (TextView) dialog.findViewById(R.id.tvmessagedialogtext);
                        message.setText(getString(R.string.no_whatsapp));

                        Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                        no.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                } catch (Exception e) {
                }

            }
        });

        //Telegram
        re_action2 = (RelativeLayout) findViewById(R.id.re_action2);
        re_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager packageManager = getApplicationContext().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("https://telegram.me");
                    i.setPackage("org.telegram.messenger");
                    i.setData(uri);
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    } else {
                        final Dialog dialog = new Dialog(Main_ThongTin_ChiTiet_DanhBa.this, R.style.FullHeightDialog); //this is a reference to the style above
                        dialog.setContentView(R.layout.custom_layout); //I saved the xml file above as yesnomessage.xml
                        dialog.setCancelable(true);

                        TextView message = (TextView) dialog.findViewById(R.id.tvmessagedialogtext);
                        message.setText(getString(R.string.no_telegram));
                        Button no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
                        no.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                } catch (Exception oi) {

                }
            }
        });

        // Gọi điện thoại
        re_call = (RelativeLayout) findViewById(R.id.re_call);
        re_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });


        // hiển thị yêu cầu chọn ảnh
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GalIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

            }
        });


        //hiển thị thông tin của liên hệ
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_numberphone = (TextView) findViewById(R.id.tv_numberphone);
        tv_name_sim = (TextView) findViewById(R.id.tv_name_sim);

        tv_name.setText(name);
        tv_numberphone.setText(phoneNumber);

        //kiểm tra và đặt dạng liên hệ
        if (id.length() == 5) {
            tv_name_sim.setText(R.string.sim);
        } else if (id.length() == 4) {
            tv_name_sim.setText(R.string.mobile);
        } else {
            tv_name_sim.setText(R.string.other);
        }

        image_anhdaidien = (ImageView) findViewById(R.id.image_Viewshow);

        // lấy đường dẫn lưu ảnh tạm thời ở SharedPreferences ra chuỗi String rùi convert thành Uri
        // Xong thì cho đọc File và gắn vào bitmap , giai đoạn cuối thì gắn vào image.setImageBitmap
        bm = null;
//        Cursor cursorInfo = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

        SharedPreferences prefs = getSharedPreferences("save_image", Context.MODE_PRIVATE);
        selected_ImageGetPath = prefs.getString(id, "");
        selectedImageUri = Uri.parse(selected_ImageGetPath); // Convert duong dan string thành uri

        try {
            File imgFile = new File(selected_ImageGetPath);

            if (imgFile.exists()) {
                bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                image_anhdaidien.setImageBitmap(bm);
            }

        } catch (Exception e) {
            bm = null;
        }


    }// end onCreate

    // Cấp quyền Permession
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), R.string.file_recording, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.file_write, Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //xin quyền cho lưu hình ảnh
    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(), R.string.permission_isn_granted, Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(getApplicationContext(), R.string.permission_dont_show_dialog, Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
    }


    //nhận kết quả mà activity crop editor của máy trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2) {
                if (data != null) {
                    // Crop ảnh
                    selectedImageUri = data.getData();
                    ImageCropFunction();
                }
            } else if (requestCode == 1) {
                if (data != null) {
                    // Code này dùng cho Crop ảnh xong hiện ra sử dụng cho cả 2 requestCode dùng cho 2 đối tượng .
                    Bundle bundle = data.getExtras();
                    bm = bundle.getParcelable("data");
                    image_anhdaidien.setImageBitmap(bm);
                    bm = ((BitmapDrawable) image_anhdaidien.getDrawable()).getBitmap();
                    SaveIamge(bm);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(selectedImageUri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outpuY", 180);
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    //lưu ảnh vào máy
    private void SaveIamge(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + getApplicationContext().getPackageName() + "/Images"); //thư mục chứa ảnh crop
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + name + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Url_duongdananh = root + "/" + getApplicationContext().getPackageName() + "/Images" + "/" + fname;

        //tao sharedPrefenrences để truyền dữ liệu với Main activity
        SharedPreferences userDetails = getSharedPreferences("save_image", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString(id, Url_duongdananh);
        editor.apply();
        editor.commit();

    }

    //Dự định dùng cho uber
//    public String getPathFromURI(Uri contentUri) {
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }

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

    //nút quay lại trên điện thoại.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
