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

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;

public class PhoneFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int REQUEST_CODE = 1;
    private EditText editText1;
    private Button button1;
    private EditText mobileno, message;
    private Button sendsms;

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

        editText1 = view.findViewById(R.id.callText);
        button1 = view.findViewById(R.id.callButton);
        mobileno = view.findViewById(R.id.messageMobile);
        message = view.findViewById(R.id.message);
        sendsms = view.findViewById(R.id.messageButton);

        button1.setOnClickListener(v -> {
            String number = editText1.getText().toString();
            if (number.length() > 9) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCallPermission();
                } else {
                    startActivity(callIntent);
                }
            }
            else{
                Toast.makeText(requireContext(), "Phone number must have at least 10 digits", Toast.LENGTH_SHORT).show();
            }
        });


        sendsms.setOnClickListener(arg0 -> {
            String no = mobileno.getText().toString();
            String msg = message.getText().toString();

            if (no.length() < 10) {

                Toast.makeText(requireContext(), "Phone number must have at least 10 digits", Toast.LENGTH_SHORT).show();
            } else if (msg.isEmpty()) {

                Toast.makeText(requireContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                SmsManager sms = SmsManager.getDefault();
                Intent intent = new Intent(requireContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                sms.sendTextMessage(no, null, msg, pi, null);
                Toast.makeText(requireContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String number = editText1.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
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
