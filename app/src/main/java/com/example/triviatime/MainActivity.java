 package com.example.triviatime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            Bundle bundle = new Bundle();
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, SplashScreenFragment.class, bundle)
                    .commit();
        }
    }

    // pops up a quit dialog on back pressed
     @Override
     public void onBackPressed(){
         QuitDialogFragment quitDialogFragment = new QuitDialogFragment();
         quitDialogFragment.show(getSupportFragmentManager(),"quit?");
     }
 }