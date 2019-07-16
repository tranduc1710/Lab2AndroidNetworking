package com.tranduc.lab2androidnetworking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.tranduc.lab2androidnetworking.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public ObservableField<String> username, password, name;

    public static String chao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        username = new ObservableField<>();
        password = new ObservableField<>();
        name = new ObservableField<>();

        binding.setMain(this);
    }

    public void login() {
        PostTask postTask = new PostTask();
        postTask.execute("http://dotplays.com/android/login.php");
    }

    private class PostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");

                // khoi tao param
                StringBuilder params = new StringBuilder();

                params.append("&");
                params.append("username=" + username.get());
                params.append("&");
                params.append("password=" + password.get());
                params.append("&");
                params.append("name=" + name.get());

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter
                        (new OutputStreamWriter(os, "UTF-8"));

                // dua param vao body cua request
                writer.append(params);

                // giai phong bo nho
                writer.flush();
                // ket thuc truyen du lieu vao output
                writer.close();
                os.close();


                // lay du lieu tra ve
                StringBuilder response = new StringBuilder();

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }

                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                finish();
                startActivity(new Intent(getApplicationContext(), Home.class));
                chao = s;
                Log.e("status", s);
            }
        }
    }
}
