package com.colonylabs.callmotionsample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.colonylabs.callmotionsample.adapter.ListContactAdapter;
import com.colonylabs.callmotionsample.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvContact;
    ListContactAdapter listContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();
        askForContactPermission();

    }

    private void initRecycler() {
        rvContact = (RecyclerView) findViewById(R.id.rv_contact);
        rvContact.setHasFixedSize(true);
        rvContact.setLayoutManager(new LinearLayoutManager(this));
        listContactAdapter = new ListContactAdapter(this);
        rvContact.setAdapter(listContactAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    void getContact() {
        List<Contact> contacts = new ArrayList<>();
        ContentResolver cr = this.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String hasNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(hasNumber) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+") ASC");
                    while (pCur.moveToNext()) {
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.add(new Contact(contactName,contactNumber));
                        break;
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }
        listContactAdapter.setListContact(contacts);
    }
    private static final int PERMISSION_REQUEST_CONTACT = 100;
    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);
                }
            }else{
                getContact();
            }
        }
        else{
            getContact();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT :{

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();

                } else {
                    Toast.makeText(this,"No permission for contacs",Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

}
