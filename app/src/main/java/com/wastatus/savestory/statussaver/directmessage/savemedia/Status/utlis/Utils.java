package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    private static final String TAG = "Utils";
    public static File downloadWhatsAppDir = new File(Environment.getExternalStorageDirectory() + "/Download/WAStatus");
    public static ArrayList<DataModel> waList = new ArrayList<>();
    public static ArrayList<DataModel> wabList = new ArrayList<>();
    public static ArrayList<DataModel> downloadedList = new ArrayList<>();
    public static AlertDialog alertDialog = null;


    public static void loadMedia() {
        File file = downloadWhatsAppDir;


        if (!file.isDirectory()) {
            return;
        }

        File[] listMediaFiles = dirListByAscendingDate(file);

        if (listMediaFiles != null) {
            downloadedList.clear();
            for (File listMediaFile : listMediaFiles) {
                downloadedList.add(new DataModel(listMediaFile.getAbsolutePath(), listMediaFile.getName(), true));
            }
        }
    }

    public static File[] dirListByAscendingDate(File folder) {
        if (!folder.isDirectory()) {
            return null;
        }
        File[] sortedByDate = folder.listFiles();
        if (sortedByDate == null || sortedByDate.length <= 1) {
            return sortedByDate;
        }
        Arrays.sort(sortedByDate, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        return sortedByDate;
    }

    public static void saveWAData(Context context) {
        for (int i = 0; i < Utils.waList.size(); i++) {
            Utils.copyFileInSavedDir(context, Utils.waList.get(i).getFilepath(), Utils.waList.get(i).getFilename());
        }
    }

    public static void saveWBData(Context context) {
        for (int i = 0; i < Utils.wabList.size(); i++) {
            Utils.copyFileInSavedDir(context, Utils.wabList.get(i).getFilepath(), Utils.wabList.get(i).getFilename());
        }

    }

    private static boolean doesPackageExist(String targetPackage, Context context) {
        try {
            context.getPackageManager().getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getBack(String paramString1, String paramString2) {
        Matcher localMatcher = Pattern.compile(paramString2).matcher(paramString1);
        if (localMatcher.find()) {
            return localMatcher.group(1);
        }
        return "";
    }

    public static void mShare(String filepath, Activity activity) {
        File fileToShare = new File(filepath);
        if (isImageFile(filepath)) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setType("image/*");
            Uri photoURI = FileProvider.getUriForFile(
                    activity.getApplicationContext(), activity.getApplicationContext()
                            .getPackageName() + ".provider", fileToShare);
            share.putExtra(Intent.EXTRA_STREAM,
                    photoURI);
            activity.startActivity(Intent.createChooser(share, "Share via"));

        } else if (isVideoFile(filepath)) {

            Uri videoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext()
                    .getPackageName() + ".provider", fileToShare);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, videoURI);

            activity.startActivity(intent);
        }

    }

    public static void shareFile(Context context, boolean isVideo, String path) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        if (isVideo)
            share.setType("Video/*");
        else
            share.setType("image/*");

        Uri uri;
        if (path.startsWith("content")) {
            uri = Uri.parse(path);
        } else {
            uri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", new File(path));
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(share);
    }

    public static void repostOnWhatsapp(Context context, boolean isVideo, String path, String pakage) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        if (isVideo)
            share.setType("Video/*");
        else
            share.setType("image/*");

        Uri uri;
        if (path.startsWith("content")) {
            uri = Uri.parse(path);
        } else {
            uri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", new File(path));
        }
        share.setPackage(pakage);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(share);
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void mediaScanner(Context context, String filePath, String fileName) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + filePath + fileName).getAbsolutePath()},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayLoader(Context context) {
        if (alertDialog == null) {

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View view = layoutInflaterAndroid.inflate(R.layout.dialog_loading, null);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(view);

            alertDialog = alert.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }

    }

    public static void dismissLoader() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public static void downloader(Context context, String downloadURL, String path, String fileName) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "" + context.getString(R.string.dl_started), Toast.LENGTH_SHORT).show();
            }
        });

        String desc = context.getString(R.string.downloading);
        Uri Download_Uri = Uri.parse(downloadURL);


        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        request.setTitle(context.getString(R.string.app_name));
        request.setVisibleInDownloadsUi(true);
        request.setDescription(desc);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, path + fileName);
        dm.enqueue(request);

        Utils.mediaScanner(context, path, fileName);
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static boolean isVideoFile(Context context, String path) {
        if (path.startsWith("content")) {
            DocumentFile fromTreeUri = DocumentFile.fromSingleUri(context, Uri.parse(path));
            String mimeType = fromTreeUri.getType();
            return mimeType != null && mimeType.startsWith("video");
        } else {
            String mimeType = URLConnection.guessContentTypeFromName(path);
            return mimeType != null && mimeType.startsWith("video");
        }
    }

    public static boolean copyFileInSavedDir(Context context, String sourceFile, String fileName) {

        String finalPath = getDir().getAbsolutePath();
//        String lastFourChars = StringUtils.left(input, 4);
        String pathWithName = finalPath + File.separator + fileName;
        Uri destUri = Uri.fromFile(new File(pathWithName));
        InputStream is;
        OutputStream os;
        try {
            Uri uri = Uri.parse(sourceFile);
            is = context.getContentResolver().openInputStream(uri);
            os = context.getContentResolver().openOutputStream(destUri, "w");

            byte[] buffer = new byte[1024];

            int length;
            while ((length = is.read(buffer)) > 0)
                os.write(buffer, 0, length);

            is.close();
            os.flush();
            os.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(destUri);
            context.sendBroadcast(intent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    static File getDir() {

        File rootFile = downloadWhatsAppDir;

        rootFile.mkdirs();

        return rootFile;

    }

    public static boolean download(Context context, String sourceFile, String fileName) {
        return copyFileInSavedDir(context, sourceFile, fileName);
    }

    public void save(final String fileName, final Context context) {
        String IMAGE,VIDEO;




        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            VIDEO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video";
        }
        else {
            VIDEO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Video";

        }

        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            IMAGE  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images";
        }
        else {
            IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Images";

        }


        final String[] ArrayFiles = {IMAGE, VIDEO};

        new AsyncTask<Void, Void, Void>() {

            public Void doInBackground(Void... voidArr) {

                int index = 0;
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg")) {
                    index = 0; // for image
                } else if (fileName.endsWith(".mp4") || fileName.endsWith(".3gp") || fileName.endsWith(".mkv")) {
                    index = 1;// for video
                } else if (fileName.endsWith(".webp")) {
                    index = 2;// for stickers
                } else if (fileName.endsWith(".mp3")) {
                    index = 4;// for audio
                } else if (fileName.endsWith(".opus")) {
                    index = 5;// for voice notes
                } else {
                    index = 3;// for documents
                }

                String str = "";
                String str2 = "/";
                String str3 = ".lock";
                String str4 = "savefileslog";
                try {
                    if (ArrayFiles.length >= 0) {
                        if (str.endsWith(str3)) {
                            String substring = str.substring(1);
                            str = substring.substring(0, substring.indexOf(str3));
                        } else {
                            str = fileName;
                        }
                        File externalStorageDirectory;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            externalStorageDirectory =context.getExternalFilesDir(null) ;
                        }else {
                            externalStorageDirectory = Environment.getExternalStorageDirectory();
                        }
                        StringBuilder sb = new StringBuilder();
                        if (index == 5)// for voice notes only
                        {
                            Calendar calender = Calendar.getInstance();
                            String weekNumber = String.format("%02d", calender.get(Calendar.WEEK_OF_YEAR));
                            String Year = String.valueOf(calender.get(Calendar.YEAR));
                            sb.append(ArrayFiles[index] + "/" + Year + weekNumber); // to get voice votes sub folder

                        } else {
                            sb.append(ArrayFiles[index]);
                        }
//                        sb.append(SaveFiles.ArrayFiles[0]);
                        sb.append(str2);
                        sb.append(str);
                        File file = new File(sb.toString());
                        if (file.exists()) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(context.getResources().getString(R.string.app_name));
                            sb2.append("/.Cached Files");
                            File file2 = new File(externalStorageDirectory, sb2.toString());
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(file2.getAbsolutePath());
                            sb3.append(str2);
                            sb3.append(str);
                            sb3.append(".cached");
                            File file3 = new File(sb3.toString());
                            if (!file3.exists()) {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                                byte[] bArr = new byte[1024];
                                while (true) {
                                    int read = fileInputStream.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                fileInputStream.close();
                                fileOutputStream.close();
                            }
                        } else {
                            Log.d(TAG, "wa file not exists");
                        }
                    }
                } catch (Exception e) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("copy error: ");
                    sb4.append(e.toString());
                    Log.d(TAG, sb4.toString());
                }
                return null;
            }
        }.execute();
    }
}
