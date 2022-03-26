package net.htlgkr.gopost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    public static MainActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        INSTANCE = this;
    }
}