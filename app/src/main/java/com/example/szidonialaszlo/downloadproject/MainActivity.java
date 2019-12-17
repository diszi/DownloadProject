package com.example.szidonialaszlo.downloadproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private MainActivity context;
    Button download, view, upload, installBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        download = (Button)findViewById(R.id.btnDownload);
        view = (Button) findViewById(R.id.btnView);
        upload = (Button) findViewById(R.id.btnUpload);
        installBtn  = (Button) findViewById(R.id.installApp);

        installBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(" INSTALL ");
                String applicationAPKname ="/app-debug-1.apk";
                String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+applicationAPKname;

                System.out.println("INSTALL APP directory= "+dir);
                Intent intentInstall = new Intent(Intent.ACTION_VIEW);
                //intentInstall.setDataAndType(Uri.fromFile(new File(dir)),"application/vnd.android.package-archive");
               // intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentInstall);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadFile().execute("http://www.tutorialspoint.com/java/java_tutorial.pdf","java_tutorial.pdf");
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "java_tutorial.pdf");  // -> filename = maven.pdf
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  + "/java_tutorial.pdf");
                System.out.println("pdfFile = "+pdfFile);
                //Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                Uri apkUri = FileProvider.getUriForFile(MainActivity.this,MainActivity.this.getApplicationContext().getPackageName()+".provider",pdfFile);
                pdfIntent.setDataAndType(apkUri,"application/pdf");
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                System.out.println("   pdfIntent = "+pdfIntent);
                System.out.println("  apkUri = "+apkUri);
                MainActivity.this.startActivity(pdfIntent);
               /* pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                try{
                    startActivity(pdfIntent);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(MainActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"choose"),1);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedFileUri = data.getData();
        String path = FileUtils.getPath(this, selectedFileUri);

        File file = new File(path);
        String fileName = file.getName();
        System.out.println("selectedFileUri ="+selectedFileUri+"   path="+path+"   file="+file);

    }

    private class DownloadFile extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString()+"/Download";
            //File folder = new File(extStorageDirectory, "testthreepdf");


//            System.out.println("folder = "+folder+"   dir = "+extStorageDirectory);
            //folder.mkdir();

            File pdfFile = new File(extStorageDirectory, fileName);
            System.out.println("pdfFile DownloadFile = "+pdfFile+"  extDir="+extStorageDirectory);
            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }
}
