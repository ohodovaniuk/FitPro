package com.example.owner.fitpro.WorkoutAndExercise.DietPlan;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.owner.fitpro.R;
import java.util.List;

public class AddNewMealCustomDialog extends Dialog implements View.OnClickListener {
    //Diet plan application context
    private Context context;
    //Diet plan meal object
    private List<DietPlan.DietPlanAdapter.DietPlanListItem> meal;
    //Meal object category name, meal name, description
    private String mealCategoryName, mealName, description;
    //Meal object meal id, proteins, carbs, fats, calories, meal position
    private Integer mealId, proteins, carbs, fats, calories, position;
    //Meal edit text input fields
    private EditText inputMealName, inputProteins, inputCarbs, inputFats, inputCalories, inputDescription;
    //Meal edit mode status
    private boolean editModeOn = false;
    //Input validation status
    private boolean inputIsValid = false;

    //Initialise current application context
    AddNewMealCustomDialog(Context context, String mealCategoryName) {
        super(context);
        this.context = context;
        this.mealCategoryName = mealCategoryName;
    }

    //Set local diet plan meal instance variables with appropriate data (used to populate the edit text fields)
    void editMeal(int position, List<DietPlan.DietPlanAdapter.DietPlanListItem> meal, int id, String mealCategoryName, String mealName, int proteins, int carbs, int fats, int calories, String description) {
        this.position = position;
        this.meal = meal;
        this.mealId = id;
        this.mealCategoryName = mealCategoryName;
        this.mealName = mealName;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fats = fats;
        this.calories = calories;
        this.description = description;
        editModeOn = true;
    }

    //Link/connect the custom dialog XML with the class logic
    //Populate the layout with data if edit mode is on
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_new_meal_custom_dialog);

        TextView dialogTitle = findViewById(R.id.add_new_meal_dialog_title);
        dialogTitle.setText(R.string.add_new_meal);
        inputMealName = findViewById(R.id.meal_name);
        inputProteins = findViewById(R.id.proteins);
        inputCarbs = findViewById(R.id.carbs);
        inputFats = findViewById(R.id.fats);
        inputCalories = findViewById(R.id.calories);
        inputDescription = findViewById(R.id.description);
        TextInputLayout descriptionBackground = findViewById(R.id.description_background);

        //Change the description edit text background border color
        inputDescription.setOnFocusChangeListener((view, hasfocus) -> {
            if (hasfocus) {
                descriptionBackground.setBackgroundResource(R.drawable.add_new_exercise_description_border_yellow);
            }
            else {
                descriptionBackground.setBackgroundResource(R.drawable.add_new_exercise_description_border_white);
            }
        });

        //Set listener on dialog buttons
        Button cancel = findViewById(R.id.cancel_button);
        Button add = findViewById(R.id.add_button);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);

        //Populate the dialog when edit mode is on
        if (editModeOn){
            dialogTitle.setText(R.string.edit_meal);
            inputMealName.setText(mealName);
            inputProteins.setText(proteins.toString());
            inputCarbs.setText(carbs.toString());
            inputFats.setText(fats.toString());
            inputCalories.setText(calories.toString());
            inputDescription.setText(description);
        }
    }

    //Listen to the custom dialog fields and buttons, responds to dialog actions with class logic
    //Read from the custom dialog edit fields if normal mode is on
    //Validate the edit text fields according to the rules, stores the input into appropriate exercise object
    //Edit the existing meal information if edit mode is on, saves the modifications
    //Clear input fields junk data after the dialog has done its work
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Clear input fields and close the dialog
            case R.id.cancel_button:
                inputMealName.getText().clear();
                inputProteins.getText().clear();
                inputCarbs.getText().clear();
                inputFats.getText().clear();
                inputCalories.getText().clear();
                inputDescription.getText().clear();
                inputIsValid = false;
                dismiss();
                break;
            //Read, validate and store the new/existing meal object data
            case R.id.add_button:
                if (inputMealName.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Meal name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!inputMealName.getText().toString().matches("[a-zA-Z0-9\\s]+")) {
                    Toast.makeText(context, "Meal name field must contain characters and digits only", Toast.LENGTH_SHORT).show();
                }
                else if (inputMealName.getText().toString().length() > 30) {
                    Toast.makeText(context, "Meal name field must not be more than 30 characters long", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (inputProteins.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Proteins field is empty", Toast.LENGTH_SHORT).show();
                    }
                    else if (!inputProteins.getText().toString().matches("[0-9]+")) {
                        Toast.makeText(context, "Proteins field must contain positive digits only", Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.parseInt(inputProteins.getText().toString()) > 100) {
                        Toast.makeText(context, "Proteins field cannot be more than 100", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (inputCarbs.getText().toString().isEmpty()){
                            Toast.makeText(context, "Carbs field is empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (!inputCarbs.getText().toString().matches("[0-9]+")) {
                            Toast.makeText(context, "Carbs field must contain positive digits only", Toast.LENGTH_SHORT).show();
                        }
                        else if (Integer.parseInt(inputCarbs.getText().toString()) > 1000) {
                            Toast.makeText(context, "Carbs field cannot be more than 1000", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (inputFats.getText().toString().isEmpty()) {
                                Toast.makeText(context, "Fats field is empty", Toast.LENGTH_SHORT).show();
                            }
                            else if (!inputFats.getText().toString().matches("[0-9]+")) {
                                Toast.makeText(context, "Fats field must contain positive digits only", Toast.LENGTH_SHORT).show();
                            }
                            else if (Integer.parseInt(inputFats.getText().toString()) > 3600) {
                                Toast.makeText(context, "Fats field cannot be more than 3600 seconds long", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (inputCalories.getText().toString().isEmpty()) {
                                    Toast.makeText(context, "Calories field is empty", Toast.LENGTH_SHORT).show();
                                }
                                else if (!inputCalories.getText().toString().matches("[0-9]+")) {
                                    Toast.makeText(context, "Calories field must contain positive digits only", Toast.LENGTH_SHORT).show();
                                }
                                else if (Integer.parseInt(inputCalories.getText().toString()) > 3600) {
                                    Toast.makeText(context, "Calories field cannot be more than 3600 seconds long", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if (inputDescription.getText().toString().isEmpty()) {
                                        Toast.makeText(context, "Description field is empty", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (inputDescription.getText().toString().length() >= 500) {
                                        Toast.makeText(context, "Description field must not be more than 500 characters long", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        //Store valid data into temporary local variables
                                        String tempMealName = inputMealName.getText().toString();
                                        int tempProteins = Integer.parseInt(inputProteins.getText().toString());
                                        int tempCarbs = Integer.parseInt(inputCarbs.getText().toString());
                                        int tempFats = Integer.parseInt(inputFats.getText().toString());
                                        int tempCalories = Integer.parseInt(inputCalories.getText().toString());
                                        String tempDescription = inputDescription.getText().toString();

                                        //Update the diet plan meal by recreating the existing meal object (remove, create)
                                        if (editModeOn) {
                                            //No modifications - do nothing
                                            if (tempMealName.equals(mealName) && tempProteins == proteins && tempCarbs == carbs && tempFats == fats && tempCalories == calories && tempDescription.equals(description)) {
                                                dismiss();
                                                Toast.makeText(context, mealName + " is already up to date", Toast.LENGTH_SHORT).show();
                                            }
                                            //Any edit text field changed - update the meal object
                                            else {
                                                //Recreate the workout routine exercise object and store the modifications into the database
                                                //If the meal name has changed then check if it is a duplicate, otherwise just update the meal details
                                                if(!tempMealName.equals(mealName)) {
                                                    if (!DietPlan.listManager.mealIsDuplicate(mealCategoryName, tempMealName)) {
                                                        MealListItem item = new MealListItem(-1, mealCategoryName, tempMealName, tempProteins, tempCarbs, tempFats, tempCalories, tempDescription);
                                                        DietPlan.listManager.addNewMealCategory(item);
                                                        //Delete the old copy of the meal object
                                                        if (DietPlan.listManager.deleteMeal(mealId)) {
                                                            meal.remove(meal.get(position));
                                                            DietPlan.adapter.notifyDataSetChanged();
                                                        } else {
                                                            Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                                        }

                                                        //Refresh the diet plan view
                                                        DietPlan.adapter.refreshMealCategoryView();
                                                        Toast.makeText(context, mealName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                                        //Clear the edit text fields, close the dialog, turn off the edit mode
                                                        inputIsValid = true;
                                                        editModeOn = false;
                                                    } else {
                                                        Toast.makeText(context, tempMealName + " already exists", Toast.LENGTH_SHORT).show();
                                                        inputIsValid = false;
                                                    }
                                                }
                                                else {
                                                    MealListItem item = new MealListItem(-1, mealCategoryName, tempMealName, tempProteins, tempCarbs, tempFats, tempCalories, tempDescription);
                                                    DietPlan.listManager.addNewMealCategory(item);
                                                    //Delete the old copy of the meal object
                                                    if (DietPlan.listManager.deleteMeal(mealId)) {
                                                        meal.remove(meal.get(position));
                                                        DietPlan.adapter.notifyDataSetChanged();
                                                    } else {
                                                        Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                                    }

                                                    //Refresh the diet plan view
                                                    DietPlan.adapter.refreshMealCategoryView();
                                                    Toast.makeText(context, mealName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                                    //Clear the edit text fields, close the dialog, turn off the edit mode
                                                    inputIsValid = true;
                                                    editModeOn = false;
                                                }
                                            }
                                        }
                                        //Create new meal object and save the meal into the database (edit mode is off)
                                        else {
                                            if (!DietPlan.listManager.mealIsDuplicate(mealCategoryName, tempMealName)) {
                                                MealListItem item = new MealListItem(-1, mealCategoryName, tempMealName, tempProteins, tempCarbs, tempFats, tempCalories, tempDescription);
                                                DietPlan.listManager.addNewMeal(item);
                                                //Refresh the diet plan view
                                                DietPlan.adapter.refreshMealCategoryView();
                                                Toast.makeText(context, tempMealName + " is successfully added", Toast.LENGTH_SHORT).show();
                                                inputIsValid = true;
                                            }
                                            else {
                                                Toast.makeText(context, tempMealName + " already exists", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        //Clear input fields and close the dialog
        if (inputIsValid) {
            inputMealName.getText().clear();
            inputProteins.getText().clear();
            inputCarbs.getText().clear();
            inputFats.getText().clear();
            inputCalories.getText().clear();
            inputDescription.getText().clear();
            inputIsValid = false;
            dismiss();
        }
    }
}
