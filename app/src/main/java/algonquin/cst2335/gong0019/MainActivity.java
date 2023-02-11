package algonquin.cst2335.gong0019;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

private static String TAG = "MainActivity";


    @Override //Application starts
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(nextPage);
        });
    }

    @Override // Activity is now visible
    protected void onStart() {
        super.onStart();

        Log.w(TAG, "Now visible");

    }

    @Override //Now responds to touch input
    protected void onResume() {
        super.onResume();

        Log.w(TAG, "Now listen for touch");
    }

    @Override // no longer listening to touches
    protected void onPause() {
        super.onPause();


    }

    @Override  //no longer visible
    protected void onStop() {
        super.onStop();
    }

    @Override //memory is garbage collected
    protected void onDestroy() {
        super.onDestroy();
    }
}