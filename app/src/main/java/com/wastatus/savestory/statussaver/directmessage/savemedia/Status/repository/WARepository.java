package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.repository;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.CalendarContract;
import android.view.View;

import androidx.documentfile.provider.DocumentFile;

import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters.WAStatusAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.SharedPrefs;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class WARepository {

    private Context context;

    public WARepository(Context context){
        this.context = context;
        new loadDataAsync();

    }

    class loadDataAsync extends AsyncTask<Void, Void, List<DataModel>> {
        DocumentFile[] allFiles;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<DataModel> doInBackground(Void... voids) {
            allFiles = null;
            allFiles = getFromSdcard();
            Utils.waList.clear();
//            Arrays.sort(allFiles, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
            for (int i = 0; i < allFiles.length; i++) {
                if (!allFiles[i].getUri().toString().contains(".nomedia")) {

                    Utils.waList.add(new DataModel(allFiles[i].getUri().toString(),
                            allFiles[i].getName(), false));


                }
            }


            return Utils.waList;
        }

        @Override
        protected void onPostExecute(List<DataModel> list) {
            super.onPostExecute(list);

            if (SharedPrefs.getAutoSave(context)) {
                Utils.saveWAData(context);
            }

            compareImage();
            Collections.reverse(Utils.waList);


        }
    }


    private DocumentFile[] getFromSdcard() {
        String treeUri = SharedPrefs.getWATree(context);
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context.getApplicationContext(), Uri.parse(treeUri));
        if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory()
                && fromTreeUri.canRead() && fromTreeUri.canWrite()) {

            return fromTreeUri.listFiles();
        } else {
            return null;
        }
    }

    public String getWAFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
        } else {
            return "WhatsApp%2FMedia%2F.Statuses";
        }
    }

    private void compareImage() {

        for (int i = 0; i < Utils.waList.size(); i++) {

            for (int j = 0; j < Utils.downloadedList.size(); j++) {


                if (Utils.waList.get(i).getFilename().equals(Utils.downloadedList.get(j).getFilename())) {
                    Utils.waList.get(i).setSaved(true);

                }
            }
        }
    }
}
