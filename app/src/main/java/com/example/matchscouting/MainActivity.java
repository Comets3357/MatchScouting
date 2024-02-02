package com.example.matchscouting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.matchscouting.common.MatchSchedule;
import com.example.matchscouting.common.Team;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    AppDatabase db;
    Button saveEventKeyButton;
    Button openQrCodeButton;
    Button beginScoutingButton;
    Button downloadMatchesButton;
    ToggleButton enableMatchScheduleButton;
    EditText ipAddressEditText;
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

        if (db.activeEventKeyDao().countMatchScheduleKey() < 1) {
            db.activeEventKeyDao().initMatchScheduleKey();
        }

        this.enableMatchScheduleButton = (ToggleButton) findViewById(R.id.toggleBtnUseMatchSchedule);
        this.downloadMatchesButton = (Button) findViewById(R.id.buttonLoadMatchSchedule);

        checkToggleMatchSchedule();

        this.enableMatchScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMatchSchedule();
            }
        });

        this.downloadMatchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMatchSchedule();
            }
        });

    }

    public void setEventKey() {
        String newEventKey = this.eventKeyEditText.getText().toString();
        db.activeEventKeyDao().setActiveEventKey(newEventKey);
    }

    public void toggleMatchSchedule() {
        if (db.activeEventKeyDao().getMatchScheduleKey().equals("0")) {
            db.activeEventKeyDao().setMatchScheduleKey("1");
        } else {
            db.activeEventKeyDao().setMatchScheduleKey("0");
        }
        checkToggleMatchSchedule();
    }

    public void checkToggleMatchSchedule() {
        if (db.matchScheduleDao().eventHasLoadedMatches(db.activeEventKeyDao().getActiveEventKey()) > 0) {
            enableMatchScheduleButton.setVisibility(View.VISIBLE);
            if (db.activeEventKeyDao().getMatchScheduleKey().equals("1")) {
                enableMatchScheduleButton.setChecked(true);
            }
        } else {
            enableMatchScheduleButton.setVisibility(View.INVISIBLE);
        }
    }

    public void loadMatchSchedule() {
        try(InputStream resource = getResources().openRawResource(R.raw.match_schedule)) {
            Scanner sc = new Scanner(resource);
            while(sc.hasNextLine()) {
                MatchSchedule matchSchedule = new MatchSchedule();
                String[] vals = sc.nextLine().split(",");
                if (vals.length >= 8) {
                    matchSchedule.eventKey = vals[0];
                    matchSchedule.matchNumber = vals[1];
                    matchSchedule.red1 = vals[2];
                    matchSchedule.red2 = vals[3];
                    matchSchedule.red3 = vals[4];
                    matchSchedule.blue1 = vals[5];
                    matchSchedule.blue2 = vals[6];
                    matchSchedule.blue3 = vals[7];
                    db.matchScheduleDao().insertAll(matchSchedule);
                }
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        checkToggleMatchSchedule();
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