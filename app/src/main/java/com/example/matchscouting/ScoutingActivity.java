package com.example.matchscouting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.matchscouting.common.ScoringPosition;
import com.example.matchscouting.common.TeamMatchScout;

public class ScoutingActivity extends AppCompatActivity {

    AppDatabase db;

    ToggleButton toggleAutoButton;
    Button buttonCoralL4;
    Button buttonCoralL3;
    Button buttonCoralL2;
    Button buttonCoralL1;
    Button buttonAlgaeL3;
    Button buttonAlgaeL2;
    Button buttonNetPlus;
    Button buttonNetMinus;
    Button buttonProcPlus;
    Button buttonProcMinus;
    Button submitMatch;
    Button refreshTeamNumber;
    ToggleButton toggleBtnNoClimb;
    ToggleButton toggleBtnShallowClimb;
    ToggleButton toggleBtnDeepClimb;
    TextView textViewClimbing;
    EditText textTeamNumber;
    EditText textMatchNumber;
    EditText textScouter;
    EditText textTablet;

    int[] al4c;
    int[] al3c;
    int[] al2c;
    int[] al1c;
    int[] tl4c;
    int[] tl3c;
    int[] tl2c;
    int[] tl1c;
    int autoProc;
    int teleProc;
    int autoNet;
    int teleNet;
    int[] al3a;
    int[] al2a;
    int[] tl3a;
    int[] tl2a;
    int currLevel;
    boolean isAuto;
    boolean isRed;
    boolean scoringTable;

    final String processorText = "Processor +\n";
    final String netText = "Net +\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouting);
        this.db = AppDatabase.getDatabase(getApplicationContext());
        this.al4c = new int[6];
        this.al3c = new int[6];
        this.al2c = new int[6];
        this.al1c = new int[6];
        this.tl4c = new int[6];
        this.tl3c = new int[6];
        this.tl2c = new int[6];
        this.tl1c = new int[6];
        this.al3a = new int[3];
        this.al2a = new int[3];
        this.tl3a = new int[3];
        this.tl2a = new int[3];
        this.autoProc = 0;
        this.teleProc = 0;
        this.autoNet = 0;
        this.teleNet = 0;
        this.currLevel = 0;
        this.isAuto = true;
        toggleAutoButton = (ToggleButton) findViewById(R.id.toggleAutoButton);
        toggleAutoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshScoutingTable();
            }
        });

        buttonCoralL4 = (Button) findViewById(R.id.btnCoralL4);
        buttonCoralL3 = (Button) findViewById(R.id.btnCoralL3);
        buttonCoralL2 = (Button) findViewById(R.id.btnCoralL2);
        buttonCoralL1 = (Button) findViewById(R.id.btnCoralL1);

        buttonNetPlus = (Button) findViewById(R.id.btnNetPlus);
        buttonNetMinus = (Button) findViewById(R.id.btnNetMinus);

        buttonProcPlus = (Button) findViewById(R.id.btnProcPlus);
        buttonProcMinus = (Button) findViewById(R.id.btnProcMinus);

        buttonNetPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    addAutoNet();
                } else {
                    addTeleNet();
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

        buttonNetMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    subAutoNet();
                } else {
                    subTeleNet();
                }
            }
        });

        buttonProcPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    addAutoProc();
                } else {
                    addTeleProc();
                }
            }
        });

        buttonProcMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    subAutoProc();
                } else {
                    subTeleProc();
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
        toggleBtnShallowClimb = (ToggleButton) findViewById(R.id.toggleBtnShallowClimb);
        toggleBtnShallowClimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleClimbs(1);
            }
        });
        toggleBtnDeepClimb = (ToggleButton) findViewById(R.id.toggleBtnDeepClimb);
        toggleBtnDeepClimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleClimbs(2);
            }
        });

        textViewClimbing = (TextView) findViewById(R.id.textViewClimbing);

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

        this.buttonCoralL4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCoralPopup(4);
            }
        });
        this.buttonCoralL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCoralPopup(3);
            }
        });
        this.buttonCoralL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCoralPopup(2);
            }
        });
        this.buttonCoralL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCoralPopup(1);
            }
        });

        textScouter = (EditText) findViewById(R.id.editTextScouter);


        submitMatch = (Button) findViewById(R.id.buttonSubmit);
        submitMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
        refreshScoutingTable();
    }

    private void doCoralPopup(int level) {
        String tablettext = this.textTablet.getText().toString();
        if (tablettext.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please include a tablet number!", Toast.LENGTH_LONG).show();
            return;
        } else if (Integer.parseInt(tablettext) > 6 || Integer.parseInt(tablettext) < 1) {
            Toast.makeText(getApplicationContext(), "Please include a tablet number between 1 and 6!", Toast.LENGTH_LONG).show();
            return;
        }
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_coral);

        Button buttonCoralTL = (Button) dialog.findViewById(R.id.buttonAddTLCoral);
        Button buttonCoralTR = (Button) dialog.findViewById(R.id.buttonAddTRCoral);
        Button buttonCoralML = (Button) dialog.findViewById(R.id.buttonAddMLCoral);
        Button buttonCoralMR = (Button) dialog.findViewById(R.id.buttonAddMRCoral);
        Button buttonCoralBL = (Button) dialog.findViewById(R.id.buttonAddBLCoral);
        Button buttonCoralBR = (Button) dialog.findViewById(R.id.buttonAddBRCoral);
        Button buttonCoralSubTL = (Button) dialog.findViewById(R.id.buttonSubTLCoral);
        Button buttonCoralSubML = (Button) dialog.findViewById(R.id.buttonSubMLCoral);
        Button buttonCoralSubBL = (Button) dialog.findViewById(R.id.buttonSubBLCoral);
        Button buttonCoralSubBR = (Button) dialog.findViewById(R.id.buttonSubBRCoral);
        Button buttonCoralSubMR = (Button) dialog.findViewById(R.id.buttonSubMRCoral);
        Button buttonCoralSubTR = (Button) dialog.findViewById(R.id.buttonSubTRCoral);
        Button cancelDialog = (Button) dialog.findViewById(R.id.buttonCancelCoral);
        cancelDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });

        buttonCoralTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.TOP_LEFT,Integer.parseInt(tablettext) < 4, false, level); //TODO: add scoring table
                dialog.dismiss();
            }
        });
        buttonCoralML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.MIDDLE_LEFT,Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralBL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.BOTTOM_LEFT,Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.BOTTOM_RIGHT,Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.MIDDLE_RIGHT,Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.TOP_RIGHT,Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.TOP_LEFT, Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.MIDDLE_LEFT, Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubBL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.BOTTOM_LEFT, Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.BOTTOM_RIGHT, Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.MIDDLE_RIGHT, Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();

            }
        });
        buttonCoralSubTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.TOP_RIGHT, Integer.parseInt(tablettext) < 4, false, level);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void doAlgaePopup(int level) {
        String tablettext = this.textTablet.getText().toString();
        if (tablettext.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please include a tablet number!", Toast.LENGTH_LONG).show();
            return;
        } else if (Integer.parseInt(tablettext) > 6 || Integer.parseInt(tablettext) < 1) {
            Toast.makeText(getApplicationContext(), "Please include a tablet number between 1 and 6!", Toast.LENGTH_LONG).show();
            return;
        }
        Dialog dialog = new Dialog(this);
        if (level == 3 && ((this.scoringTable && Integer.parseInt(tablettext) > 3)
                || (!this.scoringTable && Integer.parseInt(tablettext) < 4))) {
            dialog.setContentView(R.layout.popup_l3_algae_left);
        } else if (level == 3) {
            dialog.setContentView(R.layout.popup_l3_algae_right);
        } else if ((this.scoringTable && Integer.parseInt(tablettext) > 3)
                || (!this.scoringTable && Integer.parseInt(tablettext) < 4)) {
            dialog.setContentView(R.layout.popup_l2_algae_left);
        } else {
            dialog.setContentView(R.layout.popup_l2_algae_right);
        }

        //TODO: insert content rendering

        dialog.show();
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
            toggleBtnNoClimb.setVisibility(View.VISIBLE);
            toggleBtnShallowClimb.setVisibility(View.VISIBLE);
            toggleBtnDeepClimb.setVisibility(View.VISIBLE);
            textViewClimbing.setVisibility(View.VISIBLE);
            String processorText = this.processorText +this.teleProc;
            String netText = this.netText +this.teleNet;
            buttonNetPlus.setText(netText);
            buttonProcPlus.setText(processorText);
        } else {
            this.isAuto = true;
            toggleBtnNoClimb.setVisibility(View.GONE);
            toggleBtnShallowClimb.setVisibility(View.GONE);
            toggleBtnDeepClimb.setVisibility(View.GONE);
            textViewClimbing.setVisibility(View.GONE);
            String processorText = this.processorText +this.autoProc;
            String netText = this.netText +this.autoNet;

            buttonNetPlus.setText(netText);
            buttonProcPlus.setText(processorText);
        }
    }


    public void toggleClimbs(int buttonPressed) {
        if (buttonPressed != 0) {
            toggleBtnNoClimb.setChecked(false);
        } else {
            toggleBtnNoClimb.setChecked(true);
        }
        if (buttonPressed != 1) {
            toggleBtnShallowClimb.setChecked(false);
        } else {
            toggleBtnShallowClimb.setChecked(true);
        }
        if (buttonPressed != 2) {
            toggleBtnDeepClimb.setChecked(false);
        } else {
            toggleBtnDeepClimb.setChecked(true);
        }
    }

    public void addAutoNet() {
        if (this.autoNet < 9) {
            this.autoNet++;
            refreshScoutingTable();
        }
    }

    public void addAutoProc() {
        if (this.autoProc < 9) {
            this.autoProc++;
            refreshScoutingTable();
        }
    }

    public void subAutoNet() {
        if (this.autoNet > 0) {
            this.autoNet--;
            refreshScoutingTable();
        }
    }

    public void subAutoProc() {
        if (this.autoProc > 0) {
            this.autoProc--;
            refreshScoutingTable();
        }
    }

    public void addTeleNet() {
        if (this.teleNet < 18) {
            this.teleNet++;
            refreshScoutingTable();
        }
    }

    public void subTeleNet() {
        if (this.teleNet > 0) {
            this.teleNet--;
            refreshScoutingTable();
        }
    }

    public void addTeleProc() {
        if (this.teleProc < 18) {
            this.teleProc++;
            refreshScoutingTable();
        }
    }

    public void subTeleProc() {
        if (this.teleProc > 0) {
            this.teleProc--;
            refreshScoutingTable();
        }
    }

    /**
     * Method to add a coral to the scoring tally.
     *
     * @param scoringPosition The {@link ScoringPosition} used to indicate where on the reef the robot scored relative to the scout.
     * @param isRed Boolean indicating whether the team scoring is on the red alliance.
     * @param scoringTable Boolean indicating whether the scout is behind the scoring table.
     * @param currLevel Integer indicating the level being scored on.
     */
    public void addCoral(ScoringPosition scoringPosition, boolean isRed, boolean scoringTable, int currLevel) {
        int index = scoringPosition.getArrayPos(isRed, scoringTable);
        if (isAuto) {
            switch(currLevel) {
                case 1:
                    if (this.al1c[index] < 8) {
                        al1c[index]++;
                    }
                    break;
                case 2:
                    if (this.al2c[index] < 2) {
                        al2c[index]++;
                    }
                    break;
                case 3:
                    if (this.al3c[index] < 2) {
                        al3c[index]++;
                    }
                    break;
                case 4:
                    if (this.al4c[index] < 2) {
                        al4c[index]++;
                    }
                    break;
                default:
                    break;
            }
        } else {
            switch(currLevel) {
                case 1:
                    if (this.tl1c[index] < 8) {
                        tl1c[index]++;
                    }
                    break;
                case 2:
                    if (this.tl2c[index] < 2) {
                        tl2c[index]++;
                    }
                    break;
                case 3:
                    if (this.tl3c[index] < 2) {
                        tl3c[index]++;
                    }
                    break;
                case 4:
                    if (this.tl4c[index] < 2) {
                        tl4c[index]++;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Method to remove a coral from the score tally.
     *
     * @param scoringPosition The {@link ScoringPosition} used to indicate where on the reef the robot scored relative to the scout.
     * @param isRed Boolean indicating whether the team scoring is on the red alliance.
     * @param scoringTable Boolean indicating whether the scout is behind the scoring table.
     * @param currLevel Integer indicating the level being scored on.
     */
    public void subCoral(ScoringPosition scoringPosition, boolean isRed, boolean scoringTable, int currLevel) {
        int index = scoringPosition.getArrayPos(isRed, scoringTable);
        if (isAuto) {
            switch(currLevel) {
                case 1:
                    if (this.al1c[index] > 0) {
                        al1c[index]--;
                    }
                    break;
                case 2:
                    if (this.al2c[index] > 0) {
                        al2c[index]--;
                    }
                    break;
                case 3:
                    if (this.al3c[index] > 0) {
                        al3c[index]--;
                    }
                    break;
                case 4:
                    if (this.al4c[index] > 0) {
                        al4c[index]--;
                    }
                    break;
                default:
                    break;
            }
        } else {
            switch(currLevel) {
                case 1:
                    if (this.tl1c[index] > 0) {
                        tl1c[index]--;
                    }
                    break;
                case 2:
                    if (this.tl2c[index] > 0) {
                        tl2c[index]--;
                    }
                    break;
                case 3:
                    if (this.tl3c[index] > 0) {
                        tl3c[index]--;
                    }
                    break;
                case 4:
                    if (this.tl4c[index] > 0) {
                        tl4c[index]--;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public int getClimbNumber() {
        if (this.toggleBtnNoClimb.isChecked()) {
            return 0;
        }
        if (this.toggleBtnShallowClimb.isChecked()) {
            return 1;
        }
        if (this.toggleBtnDeepClimb.isChecked()) {
            return 2;
        }
        return 0;
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
        //todo: add gamespec
        StringBuilder sb = new StringBuilder();
        for (int i : this.al4c) {
            sb.append(i);
        }
        matchData.setAutoL4Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.al3c) {
            sb.append(i);
        }
        matchData.setAutoL3Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.al2c) {
            sb.append(i);
        }
        matchData.setAutoL2Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.al1c) {
            sb.append(i);
        }
        matchData.setAutoL1Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.tl4c) {
            sb.append(i);
        }
        matchData.setTeleL4Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.tl3c) {
            sb.append(i);
        }
        matchData.setTeleL3Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.tl2c) {
            sb.append(i);
        }
        matchData.setTeleL2Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.tl1c) {
            sb.append(i);
        }
        matchData.setTeleL1Coral(sb.toString());
        sb = new StringBuilder();
        for (int i : this.al3a) {
            sb.append(i);
        }
        matchData.setAutoL3Algae(sb.toString());
        sb = new StringBuilder();
        for (int i : this.al2a) {
            sb.append(i);
        }
        matchData.setAutoL2Algae(sb.toString());
        sb = new StringBuilder();
        for (int i : this.tl3a) {
            sb.append(i);
        }
        matchData.setTeleL3Algae(sb.toString());
        sb = new StringBuilder();
        for (int i : this.tl2a) {
            sb.append(i);
        }
        matchData.setTeleL2Algae(sb.toString());
        matchData.setAutoNet(this.autoNet);
        matchData.setTeleNet(this.teleNet);
        matchData.setAutoProcessor(this.autoProc);
        matchData.setTeleProcessor(this.teleProc);
        matchData.setEndgame(""+getClimbNumber());
        matchData.setEvent(this.db.activeEventKeyDao().getActiveEventKey());
        matchData.setEndgame(""+getClimbNumber());
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
        this.al4c = new int[6];
        this.al3c = new int[6];
        this.al2c = new int[6];
        this.al1c = new int[6];
        this.tl4c = new int[6];
        this.tl3c = new int[6];
        this.tl2c = new int[6];
        this.tl1c = new int[6];
        this.al3a = new int[3];
        this.al2a = new int[3];
        this.tl3a = new int[3];
        this.tl2a = new int[3];
        this.autoProc = 0;
        this.teleProc = 0;
        this.autoNet = 0;
        this.teleNet = 0;
        toggleClimbs(0);
        this.textTeamNumber.setText("");
        int oldMatch = Integer.parseInt(this.textMatchNumber.getText().toString());
        String newMatchText = (oldMatch + 1) + "";
        this.textMatchNumber.setText(newMatchText);
        this.toggleAutoButton.setChecked(false);
        refreshScoutingTable();
    }

    public void onSubmit() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(ScoutingActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to submit this match?");

        // Set Alert Title
        builder.setTitle("Confirm Submission");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Submit", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            submitMatch();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}