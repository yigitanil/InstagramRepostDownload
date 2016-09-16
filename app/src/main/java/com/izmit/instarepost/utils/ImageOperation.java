package com.izmit.instarepost.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;

import com.izmit.instarepost.R;

/**
 * Created by t41203 on 14.03.2016.
 */
public class ImageOperation {

    private Activity activity;

    public ImageOperation setActivity(Activity activity){
        this.activity=activity;
        return this;
    }


    public Bitmap createBitmapForRepost(Bitmap bitmap,String username,Bitmap userThumb,int orientation){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;
        Bitmap footer = BitmapFactory.decodeResource(activity.getResources(), R.drawable.footer,options);
        footer = Bitmap.createScaledBitmap(footer, 250, 37, false);
        Bitmap th=userThumb;
        Canvas canvas = new Canvas(footer);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Typeface typeFace=Typeface.createFromAsset(activity.getAssets(),"vag-rounded-bold.ttf");
        if(th!=null){
            th = Bitmap.createScaledBitmap(th, 33, 33, false);
            Bitmap circleBitmap=createCircleBitmap(th);
            canvas.drawBitmap(circleBitmap, 50,3, paint);
        }
        paint.setTypeface(typeFace);
        paint.setTextSize(23);
        paint.setColor(Color.BLACK);
        canvas.drawText("@" + username, 90, 25, paint);
        paint.setTextSize(16);


        Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bmp);
        if (orientation==1){
            canvas.drawBitmap(footer, 0, bitmap.getHeight()-37, paint);
        }else if (orientation==2){
            canvas.drawBitmap(footer, 0, 0, paint);
        }else if(orientation==3){
            canvas.drawBitmap(footer, bitmap.getWidth()-250, bitmap.getHeight()-37, paint);

        }else {
            canvas.drawBitmap(footer, bitmap.getWidth()-250, 0, paint);
        }

        return bmp;
    }


    // Creates a circle bitmap with user's profile photo
    public Bitmap createCircleBitmap(Bitmap bmp){
        Bitmap circleBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader (bmp,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paints = new Paint();
        paints.setShader(shader);
        paints.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bmp.getWidth()/2, bmp.getHeight()/2, bmp.getWidth()/2, paints);
        return circleBitmap;
    }
}
