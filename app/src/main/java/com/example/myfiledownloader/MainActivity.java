package com.example.myfiledownloader;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static String file_url = "http://androstock.com/wp-content/uploads/2015/10/MaterialSlidingTab.zip";
    public ArcProgress arc_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
        new DownloadFile().execute(file_url);
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {super.onPreExecute();}

        protected String doInBackground(String... args) {
            int count;
            try {
                URL url = new URL(file_url);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                // Output stream to write file
                final String fileName = file_url.substring(file_url.lastIndexOf('/') + 1);
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + fileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            Log.d("PROG", progress[0]);
            arc_progress.setProgress(Integer.parseInt(progress[0]));

        }

        @Override
        protected void onPostExecute(String xml) {
            Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_LONG).show();
        }

    }


}
