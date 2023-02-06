package ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.gong0019.databinding.ActivityMainBinding;
import data.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        EditText myedit = variableBinding.edittext;
        String editString = myedit.getText().toString();

        model.editString.observe(
                this, s -> variableBinding.textview.setText("Your edit text has: " + s));

        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(myedit.getText().toString());
        });

        model.isSelected.observe(this, selected -> {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.switch1.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            Toast.makeText(this, "The boxes are now selected: " + selected, Toast.LENGTH_SHORT).show();
        });

        variableBinding.checkBox.setOnCheckedChangeListener((button, isSelected) -> {
            model.isSelected.postValue(isSelected);
        });

        variableBinding.switch1.setOnCheckedChangeListener((button, isSelected) -> {
            model.isSelected.postValue(isSelected);
        });

        variableBinding.radioButton.setOnCheckedChangeListener((button, isSelected) -> {
            model.isSelected.postValue(isSelected);
        });

        variableBinding.myimagebutton.setOnClickListener(click ->
        {
            Toast.makeText(this, "The width = " + variableBinding.myimagebutton.getWidth()
                    + " and height = " + variableBinding.myimagebutton.getHeight(), Toast.LENGTH_SHORT).show();
        });

    }

}
