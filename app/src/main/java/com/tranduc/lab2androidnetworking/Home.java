package com.tranduc.lab2androidnetworking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.tranduc.lab2androidnetworking.databinding.ActivityHomeBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Home extends AppCompatActivity {

    public ObservableField<String> menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        menu = new ObservableField<>();

        GetTask getTask = new GetTask();
        getTask.execute("http://dotplays.com/android/bai1.php?food=today");

        binding.setHome(this);
    }


    public class GetTask extends AsyncTask<String, Long, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                Scanner scanner = new Scanner(inputStream);

                String data = "";

                while (scanner.hasNext()) {
                    data = data + scanner.nextLine();
                }
                scanner.close();
                return data;

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
                menu.set(s);
                Log.e("data", s);
            }
        }
    }

    public void exit(){
        finish();
    }
}
