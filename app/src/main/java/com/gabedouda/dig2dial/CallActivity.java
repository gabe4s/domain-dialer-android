package com.gabedouda.dig2dial;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        final TextView domainTextBox = (TextView) findViewById(R.id.domainTextBox);

        Button dialButton = (Button) findViewById(R.id.dialButton);
        dialButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String domain = domainTextBox.getText().toString();
                if(domain != null && !domain.trim().isEmpty()) {
                    hideSoftKeyboard();
                    // Test value for now
                    showCallDialogBox("1234567890");
                } else {
                    Toast.makeText(CallActivity.this, "Please enter a domain.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showCallDialogBox(String number) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Number found: " + number + "\nWould you like to dial?");
        //adb.setIcon(android.R.drawable.ic_dialog_alert);

        adb.setPositiveButton("Dial", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Dial number
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing - close
            }
        });

        adb.show();
    }

}
