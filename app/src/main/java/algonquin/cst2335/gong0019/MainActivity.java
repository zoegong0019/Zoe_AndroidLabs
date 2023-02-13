package algonquin.cst2335.gong0019;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override //Application starts
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("Email",emailEditText.getText().toString());

            nextPage.putExtra("Password",passwordEditText.getText().toString());
            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("Email", emailEditText.getText().toString());
            editor.putString("Password", passwordEditText.getText().toString());
            editor.apply();

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