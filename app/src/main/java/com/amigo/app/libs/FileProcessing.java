package com.amigo.app.libs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileProcessing {

    public static final int REQUEST_OPEN_CAMERA  = 1001;
    public static final int REQUEST_OPEN_GALLERY = 1002;
    public static final int RESULT_OK = -1;
    public static final String ROOT = "Serbada/";
    public static final String DOWNLOAD = "download/";
    public static final String TRANSACTION = "transaction/";
    private OnSavedListener mOnSavedListener;

    public static File getMainPath(Context context){
        File mediaPath = Environment.getExternalStorageDirectory();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mediaPath = context.getExternalFilesDir("");
        }
        return mediaPath;
    }

    public static Uri getUriFormFile(Context context,File file){
        return FileProvider.getUriForFile(context, context.getPackageName()+".provider", file);
    }


    public static String getPathDefault(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    public  void saveToTmp(Context context, Bitmap bitmap, String path, String name){
        Log.d("FileProcessing","saveToTmp "+path+name );
        String mediaPath = getMainPath(context)+path+name;
        File media = new File(mediaPath);

        if (media.exists()){
            media.delete();
        }

        File file = null,f = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            File root = new File(getMainPath(context), "/");
            if (!root.exists()){
                if(root.mkdirs()){
                    Log.d("FileProcessing","Create "+ROOT+" Success");
                }
                else {
                    Log.d("FileProcessing","Create "+ROOT+" Failed");
                }
            }

            file = new File(getMainPath(context), path);
            if(!file.exists())
            {
                if(file.mkdirs()){
                    Log.d("FileProcessing","Create "+path+" Success");
                }
                else {
                    Log.d("FileProcessing","Create "+path+" Failed");
                }
            }
            f = new File(file.getAbsolutePath()+"/"+name);
        }

        assert file != null;
        Log.d("FileProcessing",file.getAbsolutePath()+"/"+name);

        try {
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ostream);
            ostream.flush();
            ostream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        bundle.putString("PATH", path);
        message.what =1 ;
        message.setData(bundle);
        handler.sendMessageDelayed(message,500);
    }

    public static boolean createFolder(Context context, String path){

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
           String rootPath=  FileProcessing.getMainPath(context).getAbsolutePath();

            File root = new File(rootPath, "/"+path);
            if (!root.exists()){
                if(root.mkdirs()){
                    Log.d("FileProcessing","Create root "+path+" Success");
                  return create(context,path);
                }
                else {
                    Log.d("FileProcessing","Create root "+path+" Failed");
                    return false;
                }
            }
            else {
                return create(context,path);
            }
        }
        else {
            Log.d("FileProcessing","MEDIA_MOUNTED NOT ACCESS");
            return false;
        }
    }

    private static boolean create(Context context, String path){
        File file;
        file = new File(getMainPath(context), path);
        if(!file.exists()) {
            if(file.mkdirs()){
                Log.d("FileProcessing","createFolder "+path+" Success");
                return true;
            }
            else {
                Log.d("FileProcessing","createFolder "+path+" Failed");
                return false;
            }
        }
        else {
            Log.d("FileProcessing","Folder exist "+path+"");
            return true;
        }
    }

    public static Bitmap openImage(String path, String name){
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(sd.getAbsolutePath()+path+name);
        Log.d("FileProcessing",sd.getAbsolutePath()+path+name);
        if (!image.exists()){
            return null;
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        return bitmap;
    }
    public static Bitmap openImage(String url){
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(sd.getAbsolutePath()+url);
        Log.d("FileProcessing",sd.getAbsolutePath()+url);
        if (!image.exists()){
            Log.d("FileProcessing","IMAGE NOT FOUND");
            return null;
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
    }

    public int folderFilqQTY(String url){
        File sd = Environment.getExternalStorageDirectory();
        File file = new File(sd.getAbsolutePath()+url);
        Log.d("FileProcessing",sd.getAbsolutePath()+url);
        if (!file.exists()){
            file.mkdirs();
        }

        return file.listFiles().length;
    }

    public File[] getAllfiles(String url){
        File sd = Environment.getExternalStorageDirectory();
        File file = new File(sd.getAbsolutePath()+url);
        Log.d("FileProcessing","getAllfiles : " +file.listFiles().length);
        return file.listFiles();
    }

    public static Bitmap openImageWthPath(String url){
        File image = new File(url);
        Log.d("FileProcessing",url);
        if (!image.exists()){
            Log.d("FileProcessing","IMAGE NOT FOUND");
            return null;
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        return bitmap;
    }

    public static Bitmap openImageReal(String url){
        File image = new File(url);
        Log.d("FileProcessing",url);
        if (!image.exists()){
            Log.d("FileProcessing","IMAGE NOT FOUND");
            return null;
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
    }
    public static boolean deleteImage(Context context, String path, String name){
        String mediaPath = getMainPath(context)+path+name;
        File media = new File(mediaPath);
        Log.d("FileProcessing","Deleted : "+ media.getPath()+" -> "+media.exists());
        if (media.exists()){
           return media.delete();
        }
        else {
            return false;
        }
    }
    public static boolean deleteImage(String mediaPath){
        File media = new File(mediaPath);
        Log.d("FileProcessing","Deleted : "+ media.getPath()+" -> "+media.exists());
        if (media.exists()){
            return media.delete();
        }
        else {
            return false;
        }
    }
    public static boolean deleteImage(File file){
        Log.d("FileProcessing","Deleted : "+ file.getPath()+" -> "+file.exists());
        if (file.exists()){
            return file.delete();
        }
        else {
            return false;
        }
    }
    public static void clearImage(Context context, String path){
        String mediaPath = getMainPath(context)+path;
        File media = new File(mediaPath);
        String[] children = media.list();
        if (children != null){
            for (String child : children) {
               boolean delete = new File(media, child).delete();
               Log.d("FileProcessing","Deleted "+ media+" : "+ delete);
            }
        }
    }



    public void savefile(Context context, String fromPtah,Uri fromUri, String folder, String fileName) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String destinationFilename = getMainPath(context)+"/"+ROOT+""+folder+"/"+fileName;
        String root = getMainPath(context)+"/"+ROOT;
        String root1 = getMainPath(context)+"/"+ROOT+""+folder;

        File fileRoot = new File(root);
        File fileRoot1 = new File(root1);
        if (!fileRoot.exists()){
            fileRoot.mkdir();
        }
        if (!fileRoot1.exists()){
            fileRoot1.mkdir();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                copyFileFromUri(context,fromUri,new File(destinationFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mOnSavedListener != null){
                mOnSavedListener.onSave(destinationFilename,fileName);
            }
            return;
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(fromPtah));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOnSavedListener != null){
            mOnSavedListener.onSave(destinationFilename,fileName);
        }
    }

    public void copyFileFromUri(Context context, Uri srcUri, File dstFile) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
        OutputStream outputStream = new FileOutputStream(dstFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public void savefileInternal(String fromPtah, String folder, String fileName) {
        String destinationFilename = folder+fileName;


        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(fromPtah));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            int status = bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOnSavedListener != null){
            mOnSavedListener.onSave(destinationFilename,fileName);
        }
    }

    public static void openFile(Activity activity, String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String fileName = file.getName();
        if (fileName.contains(".pdf")){
            pdfOpenintent.setDataAndType(uri, "application/pdf");
        }
        else if (fileName.contains(".jpg") || fileName.contains(".jpeg")){
            pdfOpenintent.setDataAndType(uri, "image/jpeg");
        }
        else if (fileName.contains(".txt")){
            pdfOpenintent.setDataAndType(uri, "text/plain");
        }
        else if (fileName.contains(".zip")){
            pdfOpenintent.setDataAndType(uri, "application/zip");
        }
        else if (fileName.contains(".rar")){
            pdfOpenintent.setDataAndType(uri, "application/x-rar-compressed");
        }
        else if (fileName.contains(".xls") || fileName.contains(".xlsx")){
            pdfOpenintent.setDataAndType(uri, "application/vnd.ms-excel");
        }
        else if (fileName.contains(".pptx") || fileName.contains(".ppt")){
            pdfOpenintent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        }
        else if (fileName.contains(".doc") || fileName.contains(".docx")){
            pdfOpenintent.setDataAndType(uri, "application/msword");
        }

        try {
            activity.startActivity(pdfOpenintent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler(msg -> {
        if (msg.what == 1) {
            Log.d("FileProcessing", "RESULT " + msg.getData().getString("NAME") + "" + msg.getData().getString("PATH"));
            if (mOnSavedListener != null) {
                mOnSavedListener.onSave(msg.getData().getString("PATH"), msg.getData().getString("NAME"));
            }
        }
        return false;
    } );


    public void setOnSavedListener(OnSavedListener onSavedListener){
        mOnSavedListener = onSavedListener;
    }
    public interface OnSavedListener{
        void onSave(String path, String name);
    }

}
