package com.example.owner.fitpro.RegistrationLogin.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.owner.fitpro.Chat.model.User;
import com.example.owner.fitpro.R;
import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.example.owner.fitpro.RegistrationLogin.services.DataServices;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class EditProfileActivity extends AppCompatActivity {

    private DisplayMetrics dM;
    private EditText ageInput, heightInput, weightInput, expInput, bodyInput, goalInput;
    private Button ageButton, heightButton, weightButton, expButton, bodyButton, cancelButton, saveButton;
    private APIs apis;
    private DataServices dataServices;
    private String[] expArray = new String[] {"Beginner", "Mediocre", "Advanced"};
    private String[] bodyArray = new String[] {"Slim", "Moderate", "Over Weight"};
    private String age, height, weight, exp, body, goal;
    private int selectedExp, selectedBody;
    private Context context = this;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        this.dM = new DisplayMetrics();

        this.heightInput = (EditText) findViewById(R.id.epHeightET);
        this.weightInput = (EditText) findViewById(R.id.epWeightET);
        this.ageInput = (EditText) findViewById(R.id.epAgeET);
        this.expInput = (EditText) findViewById(R.id.epExpET);
        this.bodyInput = (EditText) findViewById(R.id.epBodyET);
        this.goalInput = (EditText) findViewById(R.id.epGoalET);

        this.ageButton = (Button) findViewById(R.id.epAgeButton);
        this.heightButton = (Button) findViewById(R.id.epHeightButton);
        this.weightButton = (Button) findViewById(R.id.epWeightButton);
        this.expButton = (Button) findViewById(R.id.epExpButton);
        this.bodyButton = (Button) findViewById(R.id.epBodyButton);

        this.cancelButton = (Button) findViewById(R.id.epCancelButton);
        this.saveButton = (Button) findViewById(R.id.epSaveButton);
        this.apis = new APIs();
        this.dataServices = new DataServices();

        this.apis.getUsersAPI().observeCurrentUser(
                new Function<User, Void>() {
                    @Override
                    public Void apply(User user) {
                        age = user.age;
                        height = user.height;
                        weight = user.weight;
                        exp = user.exp;
                        body = user.bodyType;
                        goal = user.goal;

                        ageInput.setText(user.age);
                        heightInput.setText(user.height);
                        weightInput.setText(user.weight);
                        expInput.setText(user.exp);
                        bodyInput.setText(user.bodyType);
                        goalInput.setText(user.goal);
                        return null;
                    }
                }, new Function<String, Void>() {
                    @Override
                    public Void apply(String s) {
                        return null;
                    }
                });

        this.ageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO FIX THIS?
                age = "0";

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                FrameLayout parent = new FrameLayout(getApplicationContext());
                NumberPicker picker = new NumberPicker(getApplicationContext());

                picker.setMinValue(18);
                picker.setMaxValue(100);
                picker.setValue(Integer.valueOf(age));
                picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        if (Integer.valueOf(age) != newVal) {
                            ageInput.setText(String.valueOf(newVal));
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }
                });

                parent.addView(picker, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER));
                builder
                        .setTitle("Age")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setView(parent);


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        this.heightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                FrameLayout parent = new FrameLayout(getApplicationContext());
                NumberPicker picker = new NumberPicker(getApplicationContext());
                //TODO FIX THIS?
                height = "0";
                picker.setMinValue(50);
                picker.setMaxValue(500);
                picker.setValue(Integer.valueOf(height));
                picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        if (Integer.valueOf(height) != newVal) {
                            heightInput.setText(String.valueOf(newVal));
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }
                });

                parent.addView(picker, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER));
                builder
                        .setTitle("Height")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setView(parent);


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        this.weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                FrameLayout parent = new FrameLayout(getApplicationContext());
                NumberPicker picker = new NumberPicker(getApplicationContext());
                //TODO FIX THIS?
                weight = "0";
                picker.setMinValue(60);
                picker.setMaxValue(500);
                picker.setValue(Integer.valueOf(weight));
                picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        if (Integer.valueOf(weight) != newVal) {
                            weightInput.setText(String.valueOf(newVal));
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }
                });

                parent.addView(picker, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER));
                builder
                        .setTitle("Weight")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setView(parent);


                AlertDialog dialog = builder.create();
                dialog.show();            }
        });

        this.expButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Experience")
                        .setSingleChoiceItems(expArray, 0, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedExp = which;
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                expInput.setText(expArray[selectedExp]);
                                if(exp != null)
                                    if (!exp.equals(expArray[selectedExp])) {
                                        saveButton.setEnabled(true);
                                    } else {
                                        saveButton.setEnabled(false);
                                    }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        this.bodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Body Type")
                        .setSingleChoiceItems(bodyArray, 0, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedBody = which;
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                bodyInput.setText(bodyArray[selectedBody]);
                                if(body != null)
                                    if (!body.equals(bodyArray[selectedBody])) {
                                        saveButton.setEnabled(true);
                                    } else {
                                        saveButton.setEnabled(false);
                                    }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        this.goalInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(goal != null)
                    if (goal.equals(s.toString())) {
                        saveButton.setEnabled(false);
                    } else {
                        saveButton.setEnabled(true);
                    }
            }
        });

        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.saveButton.setEnabled(false);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> profileMap = new HashMap<>();

                if (!ageInput.getText().toString().equals(age)) {
                    age = ageInput.getText().toString();
                    profileMap.put("age", ageInput.getText().toString());
                }
                if (!heightInput.getText().toString().equals(height)) {
                    height = heightInput.getText().toString();
                    profileMap.put("height", heightInput.getText().toString());
                }
                if (!weightInput.getText().toString().equals(weight)) {
                    weight = weightInput.getText().toString();
                    profileMap.put("weight", weightInput.getText().toString());
                }
                if (!expInput.getText().toString().equals(exp)) {
                    exp = expInput.getText().toString();
                    profileMap.put("exp", expInput.getText().toString());
                }
                if (!bodyInput.getText().toString().equals(body)) {
                    body = bodyInput.getText().toString();
                    profileMap.put("bodyType", bodyInput.getText().toString());
                }
                if (!goalInput.getText().toString().equals(goal)) {
                    goal = goalInput.getText().toString();
                    profileMap.put("goal", goalInput.getText().toString());
                }

                dataServices.writeUserProfile(
                        profileMap,
                        new Function<Void, Void>() {
                            @Override
                            public Void apply(Void aVoid) {
                                return null;
                            }
                        },
                        new Function<String, Void>() {
                            @Override
                            public Void apply(String s) {
                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                return null;
                            }
                        }
                );

                finish();
            }
        });

        getWindowManager().getDefaultDisplay().getMetrics(this.dM);
        getWindow().setLayout((int) (this.dM.widthPixels * 0.75), (int) (this.dM.heightPixels * 0.75));
    }

}
