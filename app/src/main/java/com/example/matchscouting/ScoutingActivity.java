package com.example.matchscouting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.matchscouting.common.TeamMatchScout;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoutingActivity extends AppCompatActivity {

    AppDatabase db;
    ToggleButton toggleAutoButton;
    Button btnTrapPlus;
    Button btnTrapMinus;
    Button buttonSpeakerPlus;
    Button buttonSpeakerMinus;
    Button buttonAmpPlus;
    Button buttonAmpMinus;
    Button submitMatch;
    Button refreshTeamNumber;
    ToggleButton toggleBtnNoClimb;
    ToggleButton toggleBtnFailClimb;
    ToggleButton toggleBtnClimb1;
    ToggleButton toggleBtnClimb2;
    ToggleButton toggleBtnClimb3;
    ToggleButton toggleBtnDefenseNone;
    ToggleButton toggleButtonDefenseMeh;
    ToggleButton toggleButtonDefenseGood;
    ToggleButton toggleButtonDefenseEpic;
    Switch switchLeaveAuto;
    TextView textViewClimbing;
    TextView textViewDefense;
    EditText textTeamNumber;
    EditText textMatchNumber;
    EditText textScouter;
    EditText textTablet;
    int autoSpeaker;
    int autoAmp;
    int teleSpeaker;
    int teleAmp;
    int trap;
    boolean isAuto;

    final String speakerText = "Speaker +\n";
    final String ampText = "Amp +\n";
    final String trapText = "Trap +\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouting);
        this.db = AppDatabase.getDatabase(getApplicationContext());
        this.autoSpeaker = 0;
        this.teleSpeaker = 0;
        this.autoAmp = 0;
        this.teleAmp = 0;
        this.trap = 0;
        this.isAuto = true;
        toggleAutoButton = (ToggleButton) findViewById(R.id.toggleAutoButton);
        toggleAutoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshScoutingTable();
            }
        });
        btnTrapPlus = (Button) findViewById(R.id.btnTrapPlus);
        btnTrapPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTrap();
            }
        });
        btnTrapMinus = (Button) findViewById(R.id.btnTrapMinus);
        btnTrapMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subTrap();
            }
        });

        buttonSpeakerPlus = (Button) findViewById(R.id.btnSpeakerPlus);
        buttonSpeakerMinus = (Button) findViewById(R.id.btnSpeakerMinus);
        buttonAmpPlus = (Button) findViewById(R.id.btnAmpPlus);
        buttonAmpMinus = (Button) findViewById(R.id.btnAmpMinus);

        buttonSpeakerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    addAutoSpeaker();
                } else {
                    addTeleSpeaker();
                }
            }
        });

        refreshTeamNumber = (Button) findViewById(R.id.btnRefreshTeamNumber);
        refreshTeamNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTeamNumber.setText(getTeamNumber(textMatchNumber.getText().toString(), textTablet.getText().toString()));
            }
        });

        buttonSpeakerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    subAutoSpeaker();
                } else {
                    subTeleSpeaker();
                }
            }
        });

        buttonAmpPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    addAutoAmp();
                } else {
                    addTeleAmp();
                }
            }
        });

        buttonAmpMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    subAutoAmp();
                } else {
                    subTeleAmp();
                }
            }
        });

        toggleBtnNoClimb = (ToggleButton) findViewById(R.id.toggleBtnNoClimb);
        toggleBtnNoClimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleClimbs(0);
            }
        });
        toggleBtnFailClimb = (ToggleButton) findViewById(R.id.toggleBtnFailClimb);
        toggleBtnFailClimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleClimbs(1);
            }
        });
        toggleBtnClimb1 = (ToggleButton) findViewById(R.id.toggleBtnClimbOne);
        toggleBtnClimb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleClimbs(2);
            }
        });
        toggleBtnClimb2 = (ToggleButton) findViewById(R.id.toggleBtnClimbTwo);
        toggleBtnClimb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleClimbs(3);
            }
        });
        toggleBtnClimb3 = (ToggleButton) findViewById(R.id.toggleBtnClimbThree);
        toggleBtnClimb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleClimbs(4);
            }
        });
        switchLeaveAuto = (Switch) findViewById(R.id.switchLeaveAuto);

        toggleBtnDefenseNone = (ToggleButton) findViewById(R.id.toggleBtnDefNone);
        toggleBtnDefenseNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDefense(0);
            }
        });
        toggleButtonDefenseMeh = (ToggleButton) findViewById(R.id.toggleBtnDefMeh);
        toggleButtonDefenseMeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDefense(1);
            }
        });
        toggleButtonDefenseGood = (ToggleButton) findViewById(R.id.toggleBtnDefGood);
        toggleButtonDefenseGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDefense(2);
            }
        });
        toggleButtonDefenseEpic = (ToggleButton) findViewById(R.id.toggleBtnDefEpic);
        toggleButtonDefenseEpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDefense(3);
            }
        });
        textViewClimbing = (TextView) findViewById(R.id.textViewClimbing);
        textViewDefense = (TextView) findViewById(R.id.textViewDefense);

        textTeamNumber = (EditText) findViewById(R.id.editTextTeamNumber);
        textMatchNumber = (EditText) findViewById(R.id.editTextMatchNumber);
        textTablet = (EditText) findViewById(R.id.editTextTablet);
        if (db.activeEventKeyDao().getMatchScheduleKey().equals("1")) {
            refreshTeamNumber.setVisibility(View.VISIBLE);
            textTeamNumber.setEnabled(false);
            this.textMatchNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //Logger.getLogger("ScoutingLogger").log(Level.INFO, "Match Num: " + charSequence + " Tablet Num:" + textTablet.getText().toString());
                    if ((charSequence+"").length() > 0 && textTablet.getText().toString().length() == 1) {
                        textTeamNumber.setText(getTeamNumber(charSequence+"", textTablet.getText().toString()));
                    } else {
                        textTeamNumber.setText("");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            refreshTeamNumber.setVisibility(View.GONE);
            textTeamNumber.setEnabled(true);
        }


        textScouter = (EditText) findViewById(R.id.editTextScouter);


        submitMatch = (Button) findViewById(R.id.buttonSubmit);
        submitMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMatch();
            }
        });
        refreshScoutingTable();
    }

    public String getTeamNumber(String matchNumber, String tablet) {
        switch(tablet) {
            case "1":
                return db.matchScheduleDao().getRed1(matchNumber, db.activeEventKeyDao().getActiveEventKey());
            case "2":
                return db.matchScheduleDao().getRed2(matchNumber, db.activeEventKeyDao().getActiveEventKey());
            case "3":
                return db.matchScheduleDao().getRed3(matchNumber, db.activeEventKeyDao().getActiveEventKey());
            case "4":
                return db.matchScheduleDao().getBlue1(matchNumber, db.activeEventKeyDao().getActiveEventKey());
            case "5":
                return db.matchScheduleDao().getBlue2(matchNumber, db.activeEventKeyDao().getActiveEventKey());
            case "6":
                return db.matchScheduleDao().getBlue3(matchNumber, db.activeEventKeyDao().getActiveEventKey());
            default:
                return "";
        }
    }

    public void refreshScoutingTable() {
        if (toggleAutoButton.isChecked()) {
            this.isAuto = false;
            btnTrapPlus.setVisibility(View.VISIBLE);
            btnTrapMinus.setVisibility(View.VISIBLE);
            toggleBtnNoClimb.setVisibility(View.VISIBLE);
            toggleBtnFailClimb.setVisibility(View.VISIBLE);
            toggleBtnClimb1.setVisibility(View.VISIBLE);
            toggleBtnClimb2.setVisibility(View.VISIBLE);
            toggleBtnClimb3.setVisibility(View.VISIBLE);
            switchLeaveAuto.setVisibility(View.GONE);
            textViewDefense.setVisibility(View.VISIBLE);
            textViewClimbing.setVisibility(View.VISIBLE);
            toggleBtnDefenseNone.setVisibility(View.VISIBLE);
            toggleButtonDefenseMeh.setVisibility(View.VISIBLE);
            toggleButtonDefenseGood.setVisibility(View.VISIBLE);
            toggleButtonDefenseEpic.setVisibility(View.VISIBLE);
            String speakerText = this.speakerText+this.teleSpeaker;
            String ampText = this.ampText+this.teleAmp;
            buttonSpeakerPlus.setText(speakerText);
            buttonAmpPlus.setText(ampText);

        } else {
            this.isAuto = true;
            btnTrapPlus.setVisibility(View.GONE);
            btnTrapMinus.setVisibility(View.GONE);
            toggleBtnNoClimb.setVisibility(View.GONE);
            toggleBtnFailClimb.setVisibility(View.GONE);
            toggleBtnClimb1.setVisibility(View.GONE);
            toggleBtnClimb2.setVisibility(View.GONE);
            toggleBtnClimb3.setVisibility(View.GONE);
            textViewDefense.setVisibility(View.GONE);
            textViewClimbing.setVisibility(View.GONE);
            toggleBtnDefenseNone.setVisibility(View.GONE);
            toggleButtonDefenseMeh.setVisibility(View.GONE);
            toggleButtonDefenseGood.setVisibility(View.GONE);
            toggleButtonDefenseEpic.setVisibility(View.GONE);
            switchLeaveAuto.setVisibility(View.VISIBLE);
            String speakerText = this.speakerText+this.autoSpeaker;
            String ampText = this.ampText+this.autoAmp;
            buttonSpeakerPlus.setText(speakerText);
            buttonAmpPlus.setText(ampText);
        }
        String trapText = this.trapText+this.trap;
        this.btnTrapPlus.setText(trapText);
    }
    public void toggleClimbs(int buttonPressed) {
        if (buttonPressed != 0) {
            toggleBtnNoClimb.setChecked(false);
        } else {
            toggleBtnNoClimb.setChecked(true);
        }
        if (buttonPressed != 1) {
            toggleBtnFailClimb.setChecked(false);
        } else {
            toggleBtnFailClimb.setChecked(true);
        }
        if (buttonPressed != 2) {
            toggleBtnClimb1.setChecked(false);
        } else {
            toggleBtnClimb1.setChecked(true);
        }
        if (buttonPressed != 3) {
            toggleBtnClimb2.setChecked(false);
        } else {
            toggleBtnClimb2.setChecked(true);
        }
        if (buttonPressed != 4) {
            toggleBtnClimb3.setChecked(false);
        } else {
            toggleBtnClimb3.setChecked(true);
        }
    }

    public void toggleDefense(int defenseScore) {
        toggleBtnDefenseNone.setChecked(defenseScore == 0);
        toggleButtonDefenseMeh.setChecked(defenseScore == 1);
        toggleButtonDefenseGood.setChecked(defenseScore == 2);
        toggleButtonDefenseEpic.setChecked(defenseScore == 3);
    }

    public void addAutoSpeaker() {
        if (this.autoSpeaker < 10) {
            this.autoSpeaker++;
            refreshScoutingTable();
        }
    }

    public void addAutoAmp() {
        if (this.autoAmp < 10) {
            this.autoAmp++;
            refreshScoutingTable();
        }
    }

    public void subAutoSpeaker() {
        if (this.autoSpeaker > 0) {
            this.autoSpeaker--;
            refreshScoutingTable();
        }
    }

    public void subAutoAmp() {
        if (this.autoAmp > 0) {
            this.autoAmp--;
            refreshScoutingTable();
        }
    }

    public void addTeleSpeaker() {
        if (this.teleSpeaker < 30) {
            this.teleSpeaker++;
            refreshScoutingTable();
        }
    }

    public void subTeleSpeaker() {
        if (this.teleSpeaker > 0) {
            this.teleSpeaker--;
            refreshScoutingTable();
        }
    }

    public void addTeleAmp() {
        if (this.teleAmp < 30) {
            this.teleAmp++;
            refreshScoutingTable();
        }
    }

    public void subTeleAmp() {
        if (this.teleAmp > 0) {
            this.teleAmp--;
            refreshScoutingTable();
        }
    }

    public void addTrap() {
        if (this.trap < 3) {
            this.trap++;
            refreshScoutingTable();
        }
    }

    public void subTrap() {
        if (this.trap > 0) {
            this.trap--;
            refreshScoutingTable();
        }
    }

    public int getClimbNumber() {
        if (this.toggleBtnNoClimb.isChecked()) {
            return 0;
        }
        if (this.toggleBtnFailClimb.isChecked()) {
            return -1;
        }
        if (this.toggleBtnClimb1.isChecked()) {
            return 1;
        }
        if (this.toggleBtnClimb2.isChecked()) {
            return 2;
        }
        if (this.toggleBtnClimb3.isChecked()) {
            return 3;
        }
        return 0;
    }

    public int getDefenseNumber() {
        if (this.toggleBtnDefenseNone.isChecked()) {
            return 0;
        } else if (this.toggleButtonDefenseMeh.isChecked()) {
            return 1;
        } else if (this.toggleButtonDefenseGood.isChecked()) {
            return 2;
        } else if (this.toggleButtonDefenseEpic.isChecked()) {
            return 3;
        }
        return -1;
    }

    public void submitMatch() {
        int errors = 0;
        if (this.textTeamNumber.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please include a team number!", Toast.LENGTH_LONG).show();
            errors = 1;
        }
        if (this.textMatchNumber.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please include a match number!", Toast.LENGTH_LONG).show();
            errors = 1;
        }
        if (this.textTablet.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please include a tablet number!", Toast.LENGTH_LONG).show();
            errors = 1;
        }
        if (this.textScouter.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please include a scouter name!", Toast.LENGTH_LONG).show();
            errors = 1;
        }
        if (errors != 0) {
            return;
        }
        TeamMatchScout matchData = new TeamMatchScout();
        matchData.setTeamNumber(this.textTeamNumber.getText().toString());
        matchData.setMatchNumber(this.textMatchNumber.getText().toString());
        matchData.setTablet(this.textTablet.getText().toString());
        matchData.setScouter(this.textScouter.getText().toString());
        matchData.setTrap(this.trap);
        matchData.setAutoSpeaker(this.autoSpeaker);
        matchData.setTeleSpeaker(this.teleSpeaker);
        matchData.setAutoAmp(this.autoAmp);
        matchData.setTeleAmp(this.teleAmp);
        matchData.setAutoLeave(this.switchLeaveAuto.isChecked() ? 1 : 0);
        matchData.setEvent(this.db.activeEventKeyDao().getActiveEventKey());
        matchData.setClimbStatus(getClimbNumber());
        matchData.setDefense(getDefenseNumber());
        if (db.teamMatchScoutDao().getAlreadySubmitted(matchData.getTeamNumber(), matchData.getEvent(), matchData.getMatchNumber()) > 0) {
            db.teamMatchScoutDao().update(matchData);
        } else {
            db.teamMatchScoutDao().insertAll(matchData);
        }
        resetMatch();
        Intent qrIntent = new Intent(this, QRTransferActivity.class);
        qrIntent.putExtra("teamNumber", matchData.getTeamNumber());
        qrIntent.putExtra("matchNumber", matchData.getMatchNumber());
        startActivity(qrIntent);
    }

    public void resetMatch() {
        this.autoAmp = 0;
        this.autoSpeaker = 0;
        this.teleSpeaker = 0;
        this.teleAmp = 0;
        this.trap = 0;
        toggleClimbs(0);
        toggleDefense(0);
        this.switchLeaveAuto.setChecked(false);
        this.textTeamNumber.setText("");
        int oldMatch = Integer.parseInt(this.textMatchNumber.getText().toString());
        String newMatchText = (oldMatch + 1) + "";
        this.textMatchNumber.setText(newMatchText);
        this.toggleAutoButton.setChecked(false);
        refreshScoutingTable();
    }
}