package com.example.matchscouting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    Button buttonSubCoralL1;
    Button buttonAddAlgaeL3;
    Button buttonSubAlgaeL3;
    Button buttonAddAlgaeL2;
    Button buttonSubAlgaeL2;
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
    int al1c;
    int[] tl4c;
    int[] tl3c;
    int[] tl2c;
    int tl1c;
    int autoProc;
    int teleProc;
    int autoNet;
    int teleNet;
    int al3a;
    int al2a;
    int tl3a;
    int tl2a;
    int currLevel;
    boolean isAuto;
    boolean isRed;
    boolean scoringTable;

    final String processorText = "Processor +\n";
    final String netText = "Net +\n";
    final String l1CoralText = "L1 Coral +\n";
    final String l3AlgaeText = "L3 Algae +\n";
    final String l2AlgaeText = "L2 Algae +\n";
    final String l4CoralText = "L4 Coral \n";
    final String l3CoralText = "L3 Coral \n";
    final String l2CoralText = "L2 Coral \n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scouting);
        this.db = AppDatabase.getDatabase(getApplicationContext());
        this.al4c = new int[6];
        this.al3c = new int[6];
        this.al2c = new int[6];
        this.al1c = 0;
        this.tl4c = new int[6];
        this.tl3c = new int[6];
        this.tl2c = new int[6];
        this.tl1c = 0;
        this.al3a = 0;
        this.al2a = 0;
        this.tl3a = 0;
        this.tl2a = 0;
        this.autoProc = 0;
        this.teleProc = 0;
        this.autoNet = 0;
        this.teleNet = 0;
        this.currLevel = 0;
        this.isAuto = true;
        this.scoringTable = db.activeEventKeyDao().getScoringTableKey().equals("1");
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
        buttonSubCoralL1 = (Button) findViewById(R.id.btnSubCoralL1);

        buttonAddAlgaeL3 = (Button) findViewById(R.id.btnAlgaePlusL3);
        buttonAddAlgaeL2 = (Button) findViewById(R.id.btnAlgaePlusL2);

        buttonSubAlgaeL3 = (Button) findViewById(R.id.btnSubAlgaeL3);
        buttonSubAlgaeL2 = (Button) findViewById(R.id.btnSubAlgaeL2);

        buttonNetPlus = (Button) findViewById(R.id.btnNetPlus);
        buttonNetMinus = (Button) findViewById(R.id.btnNetMinus);

        buttonProcPlus = (Button) findViewById(R.id.btnProcPlus);
        buttonProcMinus = (Button) findViewById(R.id.btnProcMinus);

        buttonAddAlgaeL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    addAutoAlgaeL3();
                } else {
                    addTeleAlgaeL3();
                }
            }
        });

        buttonAddAlgaeL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    addAutoAlgaeL2();
                } else {
                    addTeleAlgaeL2();
                }
            }
        });

        buttonSubAlgaeL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    subAutoAlgaeL3();
                } else {
                    subTeleAlgaeL3();
                }
            }
        });

        buttonSubAlgaeL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    subAutoAlgaeL2();
                } else {
                    subTeleAlgaeL2();
                }
            }
        });

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
                    if (!(charSequence + "").isEmpty() && textTablet.getText().toString().length() == 1) {
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
                if (isAuto) {
                    addAutoCoralL1();
                } else {
                    addTeleCoralL1();
                }
            }
        });

        this.buttonSubCoralL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuto) {
                    subAutoCoralL1();
                } else {
                    subTeleCoralL1();
                }
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
        if (tablettext.isEmpty()) {
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
        ImageView redDsLeft = (ImageView) dialog.findViewById(R.id.imgRedDsLeft);
        ImageView redDsRight = (ImageView) dialog.findViewById(R.id.imgRedDsRight);
        ImageView blueDsLeft = (ImageView) dialog.findViewById(R.id.imgBlueDsLeft);
        ImageView blueDsRight = (ImageView) dialog.findViewById(R.id.imgBlueDsRight);
        ImageView bargeLeft = (ImageView) dialog.findViewById(R.id.imgBargeLeft);
        ImageView bargeRight = (ImageView) dialog.findViewById(R.id.imgBargeRight);
        cancelDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });

        final boolean isRed = Integer.parseInt(tablettext) < 4;
        final String buttonText = "+ (%d)";

        buttonCoralTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.TOP_LEFT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralTL.setText(String.format(buttonText,getTextForCoralPopup(ScoringPosition.TOP_LEFT, isRed, level)));
        buttonCoralML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.MIDDLE_LEFT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralML.setText(String.format(buttonText,getTextForCoralPopup(ScoringPosition.MIDDLE_LEFT, isRed, level)));
        buttonCoralBL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.BOTTOM_LEFT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralBL.setText(String.format(buttonText,getTextForCoralPopup(ScoringPosition.BOTTOM_LEFT, isRed, level)));
        buttonCoralBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.BOTTOM_RIGHT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralBR.setText(String.format(buttonText,getTextForCoralPopup(ScoringPosition.BOTTOM_RIGHT, isRed, level)));
        buttonCoralMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.MIDDLE_RIGHT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralMR.setText(String.format(buttonText,getTextForCoralPopup(ScoringPosition.MIDDLE_RIGHT, isRed, level)));
        buttonCoralTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoral(ScoringPosition.TOP_RIGHT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralTR.setText(String.format(buttonText,getTextForCoralPopup(ScoringPosition.TOP_RIGHT, isRed, level)));
        buttonCoralSubTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.TOP_LEFT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.MIDDLE_LEFT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubBL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.BOTTOM_LEFT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.BOTTOM_RIGHT, isRed, level);
                dialog.dismiss();
            }
        });
        buttonCoralSubMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.MIDDLE_RIGHT, isRed, level);
                dialog.dismiss();

            }
        });
        buttonCoralSubTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCoral(ScoringPosition.TOP_RIGHT, isRed, level);
                dialog.dismiss();
            }
        });

        if (ScoringPosition.TOP_LEFT.getArrayPos(isRed, scoringTable) == 0) {
            bargeLeft.setVisibility(View.GONE);
            bargeRight.setVisibility(View.VISIBLE);
            blueDsRight.setVisibility(View.GONE);
            redDsRight.setVisibility(View.GONE);
            if (isRed) {
                redDsLeft.setVisibility(View.VISIBLE);
                blueDsLeft.setVisibility(View.GONE);
            } else {
                redDsLeft.setVisibility(View.GONE);
                blueDsLeft.setVisibility(View.VISIBLE);
            }
        } else {
            bargeRight.setVisibility(View.GONE);
            bargeLeft.setVisibility(View.VISIBLE);
            blueDsLeft.setVisibility(View.GONE);
            redDsLeft.setVisibility(View.GONE);
            if (isRed) {
                redDsRight.setVisibility(View.VISIBLE);
                blueDsRight.setVisibility(View.GONE);
            } else {
                redDsRight.setVisibility(View.GONE);
                blueDsRight.setVisibility(View.VISIBLE);
            }
        }

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
            String coralL1Text = this.l1CoralText + this.tl1c;
            String algaeL3Text = this.l3AlgaeText + this.tl3a;
            String algaeL2Text = this.l2AlgaeText + this.tl2a;
            int l2C = 0;
            int l3C = 0;
            int l4C = 0;
            for (int i = 0; i < 6; i++) {
                l2C+=this.tl2c[i];
                l3C+=this.tl3c[i];
                l4C+=this.tl4c[i];
            }
            String coralL2Text = this.l2CoralText + l2C;
            String coralL3Text = this.l3CoralText + l3C;
            String coralL4Text = this.l4CoralText + l4C;
            buttonNetPlus.setText(netText);
            buttonProcPlus.setText(processorText);
            buttonCoralL1.setText(coralL1Text);
            buttonAddAlgaeL3.setText(algaeL3Text);
            buttonAddAlgaeL2.setText(algaeL2Text);
            buttonCoralL2.setText(coralL2Text);
            buttonCoralL3.setText(coralL3Text);
            buttonCoralL4.setText(coralL4Text);
        } else {
            this.isAuto = true;
            toggleBtnNoClimb.setVisibility(View.GONE);
            toggleBtnShallowClimb.setVisibility(View.GONE);
            toggleBtnDeepClimb.setVisibility(View.GONE);
            textViewClimbing.setVisibility(View.GONE);
            String processorText = this.processorText +this.autoProc;
            String netText = this.netText +this.autoNet;
            String coralL1Text = this.l1CoralText + this.al1c;
            String algaeL3Text = this.l3AlgaeText + this.al3a;
            String algaeL2Text = this.l2AlgaeText + this.al2a;
            int l2C = 0;
            int l3C = 0;
            int l4C = 0;
            for (int i = 0; i < 6; i++) {
                l2C+=this.al2c[i];
                l3C+=this.al3c[i];
                l4C+=this.al4c[i];
            }
            String coralL2Text = this.l2CoralText + l2C;
            String coralL3Text = this.l3CoralText + l3C;
            String coralL4Text = this.l4CoralText + l4C;
            buttonNetPlus.setText(netText);
            buttonProcPlus.setText(processorText);
            buttonCoralL1.setText(coralL1Text);
            buttonAddAlgaeL3.setText(algaeL3Text);
            buttonAddAlgaeL2.setText(algaeL2Text);
            buttonCoralL2.setText(coralL2Text);
            buttonCoralL3.setText(coralL3Text);
            buttonCoralL4.setText(coralL4Text);
        }
    }


    public void toggleClimbs(int buttonPressed) {
        toggleBtnNoClimb.setChecked(buttonPressed == 0);
        toggleBtnShallowClimb.setChecked(buttonPressed == 1);
        toggleBtnDeepClimb.setChecked(buttonPressed == 2);
    }

    public void addAutoNet() {
        if (this.autoNet < 9) {
            this.autoNet++;
            refreshScoutingTable();
        }
    }

    public void addAutoCoralL1() {
        if (this.al1c < 10) {
            this.al1c++;
            refreshScoutingTable();
        }
    }

    public void subAutoCoralL1() {
        if (this.al1c > 0) {
            this.al1c--;
            refreshScoutingTable();
        }
    }

    public void addTeleCoralL1() {
        if (this.tl1c < 50) {
            this.tl1c++;
            refreshScoutingTable();
        }
    }

    public void subTeleCoralL1() {
        if (this.tl1c > 0) {
            this.tl1c--;
            refreshScoutingTable();
        }
    }

    public void addAutoAlgaeL3() {
        if (this.al3a < 3) {
            this.al3a++;
            refreshScoutingTable();
        }
    }

    public void subAutoAlgaeL3() {
        if (this.al3a > 0) {
            this.al3a--;
            refreshScoutingTable();
        }
    }

    public void addTeleAlgaeL3() {
        if (this.tl3a < 3) {
            this.tl3a++;
            refreshScoutingTable();
        }
    }

    public void subTeleAlgaeL3() {
        if (this.tl3a > 0) {
            this.tl3a--;
            refreshScoutingTable();
        }
    }

    public void addAutoAlgaeL2() {
        if (this.al2a < 3) {
            this.al2a++;
            refreshScoutingTable();
        }
    }

    public void subAutoAlgaeL2() {
        if (this.al2a > 0) {
            this.al2a--;
            refreshScoutingTable();
        }
    }

    public void addTeleAlgaeL2() {
        if (this.tl2a < 3) {
            this.tl2a++;
            refreshScoutingTable();
        }
    }

    public void subTeleAlgaeL2() {
        if (this.tl2a > 0) {
            this.tl2a--;
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
     * @param currLevel Integer indicating the level being scored on.
     */
    private void addCoral(ScoringPosition scoringPosition, boolean isRed, int currLevel) {
        int index = scoringPosition.getArrayPos(isRed, scoringTable);
        if (isAuto) {
            switch(currLevel) {
                case 2:
                    if (this.al2c[index] < 2) {
                        al2c[index]++;
                    }
                    refreshScoutingTable();
                    break;
                case 3:
                    if (this.al3c[index] < 2) {
                        al3c[index]++;
                    }
                    refreshScoutingTable();
                    break;
                case 4:
                    if (this.al4c[index] < 2) {
                        al4c[index]++;
                    }
                    refreshScoutingTable();
                    break;
                default:
                    break;
            }
        } else {
            switch(currLevel) {
                case 2:
                    if (this.tl2c[index] < 2) {
                        tl2c[index]++;
                    }
                    refreshScoutingTable();
                    break;
                case 3:
                    if (this.tl3c[index] < 2) {
                        tl3c[index]++;
                    }
                    refreshScoutingTable();
                    break;
                case 4:
                    if (this.tl4c[index] < 2) {
                        tl4c[index]++;
                    }
                    refreshScoutingTable();
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
     * @param currLevel Integer indicating the level being scored on.
     */
    private void subCoral(ScoringPosition scoringPosition, boolean isRed, int currLevel) {
        int index = scoringPosition.getArrayPos(isRed, scoringTable);
        if (isAuto) {
            switch(currLevel) {
                case 2:
                    if (this.al2c[index] > 0) {
                        al2c[index]--;
                    }
                    refreshScoutingTable();
                    break;
                case 3:
                    if (this.al3c[index] > 0) {
                        al3c[index]--;
                    }
                    refreshScoutingTable();
                    break;
                case 4:
                    if (this.al4c[index] > 0) {
                        al4c[index]--;
                    }
                    refreshScoutingTable();
                    break;
                default:
                    break;
            }
        } else {
            switch(currLevel) {
                case 2:
                    if (this.tl2c[index] > 0) {
                        tl2c[index]--;
                    }
                    refreshScoutingTable();
                    break;
                case 3:
                    if (this.tl3c[index] > 0) {
                        tl3c[index]--;
                    }
                    refreshScoutingTable();
                    break;
                case 4:
                    if (this.tl4c[index] > 0) {
                        tl4c[index]--;
                    }
                    refreshScoutingTable();
                    break;
                default:
                    break;
            }
        }
    }

    private int getTextForCoralPopup(ScoringPosition scoringPosition, boolean isRed, int currLevel) {
        int index = scoringPosition.getArrayPos(isRed, scoringTable);
        if (isAuto) {
            switch(currLevel) {
                case 2:
                    return al2c[index];
                case 3:
                    return al3c[index];
                case 4:
                    return al4c[index];
                default:
                    return 0;
            }
        } else {
            switch(currLevel) {
                case 2:
                    return tl2c[index];
                case 3:
                    return tl3c[index];
                case 4:
                    return tl4c[index];
                default:
                    return 0;
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
        matchData.setAutoL1Coral(this.al1c);
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
        matchData.setTeleL1Coral(this.tl1c);
        matchData.setAutoL3Algae(this.al3a);
        matchData.setAutoL2Algae(this.al2a);
        matchData.setTeleL3Algae(this.tl3a);
        matchData.setTeleL2Algae(this.tl2a);
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
        this.al1c = 0;
        this.tl4c = new int[6];
        this.tl3c = new int[6];
        this.tl2c = new int[6];
        this.tl1c = 0;
        this.al3a = 0;
        this.al2a = 0;
        this.tl3a = 0;
        this.tl2a = 0;
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