package com.wastatus.savestory.statussaver.directmessage.savemedia.Status.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.adapters.WAStatusAdapter;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.model.DataModel;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.SharedPrefs;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.FragmentWabusinessBinding;

import java.io.File;
import java.util.Collections;
import java.util.Objects;


public class WAStatusFragment extends Fragment {
    private static final String TAG = "WAStatusFragment";
    RecyclerView.LayoutManager mLayoutManager;
    WAStatusAdapter mAdapter;
    int REQUEST_ACTION_OPEN_DOCUMENT_TREE = 1001;
    loadDataAsync async;
    File file;
    private FragmentWabusinessBinding binding;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWabusinessBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        binding.rv.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.rv.setLayoutManager(this.mLayoutManager);

        binding.llAccess.setOnClickListener(v -> {

            if (Utils.appInstalledOrNot(requireContext(), "com.whatsapp")) {
                StorageManager sm = (StorageManager) requireActivity().getSystemService(Context.STORAGE_SERVICE);

                Intent intent;
                String statusDir = getWAFolder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                    Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

                    String scheme = uri.toString();

                    scheme = scheme.replace("/root/", "/document/");

                    scheme += "%3A" + statusDir;

                    uri = Uri.parse(scheme);

                    intent.putExtra("android.provider.extra.INITIAL_URI", uri);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    intent.putExtra("android.provider.extra.INITIAL_URI", statusDir);
                }


                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

                startActivityForResult(intent, REQUEST_ACTION_OPEN_DOCUMENT_TREE);
            } else {
                Toast.makeText(getActivity(), "Please Install WhatsApp For Download Status!!!!!", Toast.LENGTH_SHORT).show();
            }


        });
        if (!SharedPrefs.getWATree(getActivity()).equals("")) {
            populateGrid();
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_ACTION_OPEN_DOCUMENT_TREE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.e("onActivityResult: ", "" + data.getData());
            try {
                requireContext().getContentResolver()
                        .takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPrefs.setWATree(getActivity(), uri.toString());

            populateGrid();
        }
    }

    public void populateGrid() {

        async = new loadDataAsync();
        async.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (async != null) {
            async.cancel(true);
        }
    }

    private DocumentFile[] getFromSdcard() {
        String treeUri = SharedPrefs.getWATree(getActivity());
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(requireContext().getApplicationContext(), Uri.parse(treeUri));
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




    class loadDataAsync extends AsyncTask<Void, Void, Void> {
        DocumentFile[] allFiles;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rv.setVisibility(View.GONE);
            binding.llAccess.setVisibility(View.GONE);
            binding.isEmptyList.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
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


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(() -> {
                if (getActivity() != null) {

                    if (SharedPrefs.getAutoSave(getActivity())) {
                        Utils.saveWAData(requireContext());
                    }

                    compareImage();
                    Collections.reverse(Utils.waList);
                    mAdapter = new WAStatusAdapter(getActivity(), Utils.waList);
                    binding.rv.setAdapter(mAdapter);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rv.setVisibility(View.VISIBLE);


                }

                if (Utils.waList == null || Utils.waList.size() == 0) {
                    binding.isEmptyList.setVisibility(View.VISIBLE);
                } else {
                    binding.isEmptyList.setVisibility(View.GONE);
                }
            }, 100);
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
