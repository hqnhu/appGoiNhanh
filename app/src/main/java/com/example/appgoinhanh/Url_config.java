package com.example.appgoinhanh;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Url_config {


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        //Setting the width and height of the Bitmap that will be returned
        // equal to the original Bitmap that needs round corners.
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Creating canvas with output Bitmap for drawing
        Canvas canvas = new Canvas(output);

        //Setting paint and rectangles.
        final int color = Color.BLACK;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        //SetXfermode applies PorterDuffXfermode to paint.
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

//    // show popup load
//    public static AlertDialog showpop_up_Load(Context content) {
//        final AlertDialog.Builder popDialog = new AlertDialog.Builder(content);
//
//        final LayoutInflater inflater = (LayoutInflater) content.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        final View Viewlayout = inflater.inflate(R.layout.bg_load, null);
//
//        popDialog.setView(Viewlayout);
//
//        popDialog.create();
//        final AlertDialog dg = popDialog.show();
//        dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        //
//        return  dg;
//    }

}
