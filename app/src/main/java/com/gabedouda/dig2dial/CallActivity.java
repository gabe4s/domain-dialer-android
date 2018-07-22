package com.gabedouda.dig2dial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CallActivity extends AppCompatActivity implements OnDomainProcessed {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        final TextView domainTextBox = (TextView) findViewById(R.id.domainTextBox);

        Button dialButton = (Button) findViewById(R.id.dialButton);
        dialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String domain = domainTextBox.getText().toString();
                if (domain != null && !domain.trim().isEmpty()) {
                    domainTextBox.clearFocus();
                    hideSoftKeyboard();
                    new DomainUtil(CallActivity.this).execute(domain);
                } else {
                    Toast.makeText(CallActivity.this, "Please enter a domain",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showCallDialogBox(final String tn) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Number found: " + tn + "\nWould you like to dial?");
        //adb.setIcon(android.R.drawable.ic_dialog_alert);

        adb.setPositiveButton("Dial", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Dial number
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tn));
                startActivity(callIntent);
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing - close
            }
        });

        adb.show();
    }

    @Override
    public void onDomainProcessed(String tn, Exception e) {
        if(e != null) {
            Toast.makeText(CallActivity.this, "Error retrieving domain records",
                    Toast.LENGTH_LONG).show();
        } else {
            if(tn != null) {
                showCallDialogBox(tn);
            } else {
                Toast.makeText(CallActivity.this, "No phone number found for domain",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
