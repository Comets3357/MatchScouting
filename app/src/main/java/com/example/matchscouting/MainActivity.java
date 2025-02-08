package com.example.matchscouting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.matchscouting.common.JsonResponse;
import com.example.matchscouting.common.MatchSchedule;
import com.example.matchscouting.common.ScoutingDataServerResponse;
import com.example.matchscouting.common.Team;
import com.example.matchscouting.dao.ScoutingDataServerRestDao;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    AppDatabase db;
    Button saveEventKeyButton;
    Button openQrCodeButton;
    Button beginScoutingButton;
    Button downloadMatchesButton;
    Button getDataFromServerButton;
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

        if (db.activeEventKeyDao().countScoringTableKey() < 1) {
            db.activeEventKeyDao().initScoringTableKey();
        }

        this.enableMatchScheduleButton = (ToggleButton) findViewById(R.id.toggleBtnUseMatchSchedule);
        this.downloadMatchesButton = (Button) findViewById(R.id.buttonLoadMatchSchedule);

        ToggleButton scoringTableButton = (ToggleButton) findViewById(R.id.toggleButtonScoringTable);
        scoringTableButton.setChecked(db.activeEventKeyDao().getScoringTableKey().equals("1"));

        checkToggleMatchSchedule();

        this.enableMatchScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMatchSchedule();
            }
        });

        scoringTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleScoringTable();
            }
        });

        this.downloadMatchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMatchSchedule();
            }
        });

        this.getDataFromServerButton = (Button) findViewById(R.id.buttonDownloadData);
        this.getDataFromServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromServer();
                checkToggleMatchSchedule();
            }
        });

    }

    public void setEventKey() {
        String newEventKey = this.eventKeyEditText.getText().toString();
        db.activeEventKeyDao().setActiveEventKey(newEventKey);
        checkToggleMatchSchedule();
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
            db.activeEventKeyDao().setMatchScheduleKey("0");
        }
    }

    public void toggleScoringTable() {
        if (db.activeEventKeyDao().getScoringTableKey().equals("0")) {
            db.activeEventKeyDao().setScoringTableKey("1");
        } else {
            db.activeEventKeyDao().setScoringTableKey("0");
        }
        if (db.activeEventKeyDao().getMatchScheduleKey().equals("1")) {
            enableMatchScheduleButton.setChecked(true);
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

    public void getDataFromServer() {
        EditText ipEditText = (EditText) findViewById(R.id.editTextIP);
        ScoutingDataServerRestDao.getApiInterface("http://"+ipEditText.getText().toString()+":5000").getScoutingDataServerData()
                .enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        if (response.body() != null){
                            Log.d("TAG","Successful data retrieval");
                            ScoutingDataServerResponse sdsresponse = response.body().getResponse();
                            db.activeEventKeyDao().setScoringTableKey(sdsresponse.getScoringTable());
                            db.activeEventKeyDao().setActiveEventKey(sdsresponse.getEventKey());
                            eventKeyEditText.setText(sdsresponse.getEventKey());
                            for (MatchSchedule matchSchedule : sdsresponse.getMatchSchedule()) {
                                if (db.matchScheduleDao().matchAlreadyLoaded(matchSchedule.getEventKey(), matchSchedule.getMatchNumber()) < 1) {
                                    db.matchScheduleDao().insertAll(matchSchedule);
                                }
                            }
                            scoringTableButton.setChecked(db.activeEventKeyDao().getScoringTableKey().equals("1"));
                            Toast.makeText(getApplicationContext(),"Successfully downloaded data from server",Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("TAG","Failure");
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                        Log.d("TAG","Failure :-"+t.getMessage());
                        Toast.makeText(getApplicationContext(),"ERROR "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }


}
