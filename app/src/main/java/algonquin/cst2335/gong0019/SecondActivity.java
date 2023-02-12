package algonquin.cst2335.gong0019;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.gong0019.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Password", "");

        Log.d("SecondActivity", "Email: " + email + " Password: " + password);

        binding.secondPageButton.setOnClickListener(clk -> {

            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:8193297373"));
            startActivity(call);
        });

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data =result.getData();
                        Bitmap imageBitmap = data.getParcelableExtra("data");
                        FileOutputStream fOut = null;

                        File sandbox = getFilesDir();

                        try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                            fOut.flush();

                            fOut.close();

                        }
                        catch(IOException ioe) {}

                        int i= 0;
                        binding.imageView.setImageBitmap(imageBitmap);
                    }
                });
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        binding.imageView.setOnClickListener(v -> cameraResult.launch(cameraIntent));
        binding.button.setOnClickListener(v -> cameraResult.launch(cameraIntent));
    }

    private void saveImage(Bitmap imageBitmap) {
        // Save the image

        }
    }




