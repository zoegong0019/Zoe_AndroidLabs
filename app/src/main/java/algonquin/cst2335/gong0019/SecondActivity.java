package algonquin.cst2335.gong0019;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.gong0019.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Password", "");
        Log.d("SecondActivity", "Email: " + email + " Password: " + password);

        binding.secondPageButton.setOnClickListener(v -> {
            String phoneNumber = binding.textPhone.getText().toString();
            if (!TextUtils.isEmpty(phoneNumber)) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);

                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("phone_number", binding.textPhone.getText().toString());
                editor.apply();
            }
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data =result.getData();
                        assert data != null;
                        Bitmap imageBitmap = data.getParcelableExtra("data");
                        FileOutputStream fOut;

                        try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                            fOut.flush();

                            fOut.close();

                        }
                        catch(IOException ioe) {
                            throw new RuntimeException(ioe);
                        }

                        binding.imageView.setImageBitmap(imageBitmap);
                    }
                });
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        binding.imageView.setOnClickListener(v -> cameraResult.launch(cameraIntent));
        binding.button.setOnClickListener(v -> cameraResult.launch(cameraIntent));

    welcomeTextView = findViewById(R.id.title);

    Intent intent = getIntent();
    String emailAddress = intent.getStringExtra("Email");
        welcomeTextView.setText("Welcome back " + emailAddress);
    }
}




