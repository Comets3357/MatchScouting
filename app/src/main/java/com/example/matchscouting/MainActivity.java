package com.example.matchscouting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matchscouting.common.Team;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    AppDatabase db;
    Button saveEventKeyButton;
    Button openQrCodeButton;
    Button beginScoutingButton;
    EditText eventKeyEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getDatabase(getApplicationContext());

        importTeamNames();
        saveEventKeyButton = (Button) findViewById(R.id.buttonSaveKey);
        saveEventKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setEventKey(); }
        });

        openQrCodeButton = (Button) findViewById(R.id.buttonQrTransfer);
        openQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQRCode();
            }
        });

        eventKeyEditText = (EditText) findViewById(R.id.editTextEventKey);
        beginScoutingButton = (Button) findViewById(R.id.buttonBeginScouting);
        beginScoutingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginMatchScouting();
            }
        });

        if (db.activeEventKeyDao().countEventKey() < 1) {
            db.activeEventKeyDao().initEventKey();
        } else {
            eventKeyEditText.setText(db.activeEventKeyDao().getActiveEventKey());
        }

    }

    public void setEventKey() {
        String newEventKey = this.eventKeyEditText.getText().toString();
        db.activeEventKeyDao().setActiveEventKey(newEventKey);
    }

    public void openQRCode() {
        if (db.teamMatchScoutDao().getAllMatchesAtEvent(db.activeEventKeyDao().getActiveEventKey()).size() > 0) {
            Intent qrCodeIntent = new Intent(this, QRTransferActivity.class);
            startActivity(qrCodeIntent);
        } else {
            Toast errorToast = Toast.makeText(this, "No matches recorded to transfer QR code!", Toast.LENGTH_LONG);
            errorToast.show();
        }
    }

    private void importTeamNames() {
        try(InputStream resource = getResources().openRawResource(R.raw.team_names)) {
            Scanner sc = new Scanner(resource);
            while(sc.hasNextLine()) {
                Team team = new Team();
                String[] vals = sc.nextLine().split(",");
                if (vals.length >= 2) {
                    team.teamNumber = vals[0];
                    team.teamName = vals[1];
                    db.teamDao().insertAll(team);
                }
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public void beginMatchScouting() {
        Intent scoutingIntent = new Intent(this, ScoutingActivity.class);
        startActivity(scoutingIntent);
    }


}