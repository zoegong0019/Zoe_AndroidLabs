package algonquin.cst2335.gong0019;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.gong0019.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Password", "");

        Log.d("SecondActivity", "Email: " + email + " Password: " + password);

        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.secondPageButton.setOnClickListener(clk -> {

            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:8193297373"));
            startActivity(call);
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        binding.imageView.setImageBitmap(imageBitmap);
                    }
                });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        binding.imageView.setOnClickListener(clk -> cameraResult.launch(cameraIntent));
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }
}


