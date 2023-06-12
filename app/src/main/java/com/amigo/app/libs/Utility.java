package com.amigo.app.libs;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.amigo.app.R;
import com.amigo.app.ui.component.MyToast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class Utility {

    public static void LogDbug(String tag, String message){
        Log.d(tag, message);
    }

    public static Typeface getFontFamily(Context context,int font ){
        return   ResourcesCompat.getFont(context, font);
    }

    public static void showToastError(Context context, String message){
        MyToast myToast = new MyToast(context, null);
        myToast.setIcon(R.drawable.ic_error, Color.parseColor("#B71C1C"));
        myToast.setBackground(Color.parseColor("#FFCEC9"));
        myToast.setTextColor(Color.parseColor("#7F0000"));
        myToast.setMessage(message);
        myToast.show(Toast.LENGTH_LONG, Gravity.CENTER, 0,0);
    }
    public static void showToastSuccess(Context context, String message){
        MyToast myToast = new MyToast(context, null);
        myToast.setIcon(R.drawable.ic_success, Color.parseColor("#1BA13E"));
        myToast.setBackground(Color.parseColor("#E5F9E5"));
        myToast.setTextColor(Color.parseColor("#1BA13E"));
        myToast.setMessage(message);
        myToast.show(Toast.LENGTH_LONG, Gravity.CENTER, 0,0);
    }

    public static void showAlertError(Context context,String message){
//        AlertDialog dialog = new AlertDialog(context);
//        dialog.showFailed(message);
    }

    public static Calendar getLocalTime(Date date){
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            String dateStr = readDate.format(date);
            readDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            Date date2 = readDate.parse(dateStr);
            readDate.setTimeZone(TimeZone.getDefault());
            if (date2 == null){
                date2 = new Date();
            }
            String mDate = readDate.format(date2);
            Log.d("Utility","TIME "+ mDate);

            String h = mDate.split(" ")[1].split(":")[0];
            String m = mDate.split(" ")[1].split(":")[1];
            String s = mDate.split(" ")[1].split(":")[2];
            calendar.setTime(date2);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h));
            calendar.set(Calendar.MINUTE, Integer.parseInt(m));
            calendar.set(Calendar.SECOND, Integer.parseInt(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String GET_UUID(Context ctx) {
        String androidId = "";
       try {
           final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
           String tmDevice, tmSerial;
           PackageManager pm = ctx.getPackageManager();
           int hasPerm = pm.checkPermission(android.Manifest.permission.READ_PHONE_STATE, ctx.getPackageName());

           if (hasPerm == PackageManager.PERMISSION_GRANTED) {
               try {
                   tmDevice = "" + tm.getDeviceId();
                   tmSerial = "" + tm.getSimSerialNumber();
               }catch (Exception e){
                   tmDevice =  Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
                   tmSerial = Build.SERIAL;
               }

           } else {
               tmDevice = Settings.Secure.ANDROID_ID;
               tmSerial = Build.SERIAL;

           }
           Log.d("Utility","Device ID "+Build.MODEL+" : "+tmDevice);
           androidId = "" + Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
           UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
           androidId = deviceUuid.toString();
       }catch (Exception e){
//           e.printStackTrace();
       }

        if (Build.MODEL.contains("sdk")
                || Build.MODEL.contains("emulator")
//                || Build.MODEL.contains("ASUS_I006D")
        ) {
//            androidId = "ffffffff-aab6-f82d-ffff-ffffef05ac4a";
        }
        return androidId;
    }

    public static String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error MD5 NoSuchAlgorithmException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        boolean isValid  = true;
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    isValid = false;
                    Log.e("TAGRZ","Permission failed "+permission);
                }
            }
        }
        return isValid;
    }

    public static String getStringFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static void showKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static SpannableString BoldText(Context pContext, String pText, int start, int end, String colorCode){
        SpannableString content = new SpannableString(pText);
        Typeface font =  ResourcesCompat.getFont(pContext, R.font.roboto_bold);
        content.setSpan(new CustomTypefaceSpan("", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setSpan(new ForegroundColorSpan(Color.parseColor(colorCode)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return content;
    }
    public static SpannableString BoldText(Context pContext, String pText, int start, int end, int color){
        SpannableString content = new SpannableString(pText);
        Typeface font =  ResourcesCompat.getFont(pContext, R.font.roboto_bold);
        content.setSpan(new CustomTypefaceSpan("", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return content;
    }


    public static SpannableString BoldText(Context pContext, String pText, int start, int end){
        SpannableString content = new SpannableString(pText);
        Typeface font =  ResourcesCompat.getFont(pContext, R.font.roboto_bold);
        content.setSpan(new CustomTypefaceSpan("", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return content;
    }

    public static void copyTextToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    public static ShapeDrawable getOvalBackground(String color_code){
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(Color.parseColor("#"+color_code));
        return shapeDrawable;
    }
    public static ShapeDrawable getOvalBackground(int color){
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public static int toUnitDP(Context context, int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }
    public static int toUnitSP(Context context, int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }

    public static Date convert2Date(String date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format,new Locale("id"));
        Date mDate = new Date();
        try {
            mDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public static String getDateString(Date pDate, String format){
        DateFormat format1 = new SimpleDateFormat(format,new Locale("id"));
        return format1.format(pDate);
    }

    public static String UpperAfterSpace(String text){
        try {
            if (text.isEmpty()){
                return "-";
            }
            text = text.toLowerCase();
            String[] x = text.split(" ");
            StringBuilder sb = new StringBuilder();
            if (x.length > 1){
                for (String x1 : x) {
                    String s1 = x1.substring(0, 1).toUpperCase();
                    String textCapitalized = s1 + x1.substring(1);
                    sb.append(textCapitalized).append(" ");
                }
            }
            else {
                sb.append(text.toUpperCase());
            }
            return sb.toString();
        }catch (Exception e){
            return text;
        }
    }

}
