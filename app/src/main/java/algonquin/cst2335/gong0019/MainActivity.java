package algonquin.cst2335.gong0019;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**This is a page that simulates a login page
 * @author Zoe Gong
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     *  This holds the text at the center of the screen
     */
    private TextView tv =null;
    /**
     * This holds the input password at the center of the screen
     */
    private EditText et =null;
    /**
     * This holds the button at the center of the screen
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.sendButton);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            if(verifyPassword(password)) {
                tv.setText("Your password meets the requirements");
            }else {
                tv.setText("You shall not pass!");
            }
        });
    }
    /** This function should check if this string has an Upper Case letter,
     *  a lower case letter, a number, and a special symbol.
     *
     * @param pw The String object that we are checking
     * @return Return true if the password is complex enough, and false if it is not complex enough.
     */

    Boolean verifyPassword(String pw) {

        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;

        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;
        if (!foundUpperCase) {

            Toast.makeText( this, "I found an upper case!", Toast.LENGTH_SHORT).show();// Say that they are missing an upper case letter;

            return false;

        } else if (!foundLowerCase) {
            Toast.makeText(this, "I found an lower case!", Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;

            return false;

        } else if (!foundNumber) {
            Toast.makeText(this, "I found an number!", Toast.LENGTH_SHORT).show();
        } else if (!foundSpecial) {
            Toast.makeText(this, "I found an special character !", Toast.LENGTH_SHORT).show();
        } else

            return true; //only get here if they're all true
        return false;
    }
    //return true if c is one of: #$%^&*!@?
//return false otherwise
    private boolean isSpecialCharacter(char c) {
        //return true if c is one of: #$%^&*!@?
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}
