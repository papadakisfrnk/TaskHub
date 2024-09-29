package com.hub.contextawaretaskmanagement.Fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hub.contextawaretaskmanagement.R;

public class PhoneFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int REQUEST_CODE = 1;
    private Button button_call;
    private EditText mobileno, message;
    private Button sendsms;
    private String number = new String();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_phone, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Phone Task");
        }

        button_call = view.findViewById(R.id.callButton);
        mobileno = view.findViewById(R.id.mobile_no);
        message = view.findViewById(R.id.message);
        sendsms = view.findViewById(R.id.messageButton);


        button_call.setOnClickListener(v -> {
            number = mobileno.getText().toString();
            if (number.length() > 9) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCallPermission();
                } else {
                    startActivity(callIntent);
                }
            } else {
                Toast.makeText(requireContext(), "Phone number must have at least 10 digits", Toast.LENGTH_SHORT).show();
            }
        });


        sendsms.setOnClickListener(arg0 -> {
            number = mobileno.getText().toString();
            String msg = message.getText().toString();
            if (!(number.length() > 9)) {
                Toast.makeText(requireContext(), "Phone number must have at least 10 digits", Toast.LENGTH_SHORT).show();
            } else if (msg.isEmpty()) {

                Toast.makeText(requireContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    SmsManager sms = SmsManager.getDefault();
                    Intent intent = new Intent(requireContext(), PhoneFragment.class);
                    PendingIntent pi = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    sms.sendTextMessage(number, null, msg, pi, null);
                    Toast.makeText(requireContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS) +
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS))
                != PackageManager.PERMISSION_GRANTED) {

            requestSmsPermissions();
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                requireActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                number = mobileno.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            } else {
                Toast.makeText(requireContext(), "Call Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission for SMS granted
                Toast.makeText(requireContext(), "SMS Permissions Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "SMS Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestCallPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }

    private void requestSmsPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS},
                REQUEST_CODE);
    }
}
