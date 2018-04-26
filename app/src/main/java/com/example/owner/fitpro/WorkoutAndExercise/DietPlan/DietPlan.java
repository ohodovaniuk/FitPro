package com.example.owner.fitpro.WorkoutAndExercise.DietPlan;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.owner.fitpro.WorkoutAndExercise.Animation.CustomRecyclerViewDivider;
import com.example.owner.fitpro.WorkoutAndExercise.Animation.ExpandableRecyclerAdapter;
import com.example.owner.fitpro.WorkoutAndExercise.Animation.ItemTouchHelperExtension;
import com.example.owner.fitpro.WorkoutAndExercise.DietPlan.MealDetails;
import com.example.owner.fitpro.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DietPlan extends AppCompatActivity {
    public static String categoryName;
    public static MealListManager listManager;
    public static DietPlanAdapter adapter;
    private RecyclerView recyclerView;
    public ItemTouchHelperExtension mealItemTouchHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_category_list_items);

        //Customise diet plan toolbar
        Intent intent = getIntent();
        categoryName = intent.getStringExtra(categoryName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TextView toolbarTitle = (TextView) findViewById(R.id.tool_bar_title);
        toolbarTitle.setText(categoryName + " Diet Plan");

        //Create diet plan list view of meal category items, add divider under each list item
        recyclerView = (RecyclerView) findViewById(R.id.meal_list_item);
        listManager = new MealListManager(getApplicationContext());
        adapter = new DietPlanAdapter(this, listManager.getListOfDietPlanMeals(categoryName));
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CustomRecyclerViewDivider(this, 16));
        recyclerView.setAdapter(adapter);

        //Enable recycler view swipe, each list view meal can be swiped to the right to show swipe menu/options
        ItemTouchHelperExtension.Callback mealCallback = new ItemTouchHelperCallback();
        mealItemTouchHelper = new ItemTouchHelperExtension(mealCallback);
        mealItemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setItemTouchHelperExtension(mealItemTouchHelper);

        //Trigger Add New Meal Category Custom Dialog, add new meal category to diet plan list view
        FloatingActionButton addNewMealCategory = (FloatingActionButton) findViewById(R.id.add_new_meal_category);
        addNewMealCategory.setOnClickListener(view -> {
            AddNewMealCategoryCustomDialog NewMealCategoryCustomDialog = new AddNewMealCategoryCustomDialog(this, categoryName);
            NewMealCategoryCustomDialog.show();
        });
    }

    //Diet Plan Recycler View Adapter
    public class DietPlanAdapter extends ExpandableRecyclerAdapter<DietPlanAdapter.DietPlanListItem> {
        private Context context;
        private List<MealListItem> meal;
        private List<ArrayList<MealListItem>> mealGroups = new ArrayList<>();
        private ItemTouchHelperExtension mealItemTouchHelperExtension;

        //Initialise current application context, meal instance and retrieve meals
        DietPlanAdapter(Context context, List<MealListItem> meal) {
            super(context);
            this.meal = meal;
            this.context = context;
            setItems(getDietPlanMeals());
        }

        //Set individual meal category content
        class DietPlanListItem extends ExpandableRecyclerAdapter.ListItem {
            private String mealCategoryName, mealName, description;
            private Integer id, proteins, carbs, fats, calories;

            //Initialise meal category name
            DietPlanListItem(String mealCategoryName) {
                super(TYPE_ROUTINE_HEADER);
                this.mealCategoryName = mealCategoryName;
            }

            //Initialise meal category name and meal object
            DietPlanListItem(Integer id, String mealCategoryName, String mealName, Integer proteins, Integer carbs, Integer fats, Integer calories, String description) {
                super(TYPE_ROUTINE_EXERCISES);
                this.id = id;
                this.mealCategoryName = mealCategoryName;
                this.mealName = mealName;
                this.proteins = proteins;
                this.carbs = carbs;
                this.fats = fats;
                this.calories = calories;
                this.description = description;
            }
        }

        //Link meal category variables and logic with XML layout attributes
        public class MealCategoryHeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
            View mealCategoryView;
            View mealViewContent;
            View mealActionContainer;
            TextView mealCategoryName;
            View removeMealCategoryButton;
            View editMealCategoryButton;
            View addNewMealButton;

            MealCategoryHeaderViewHolder(View view) {
                super(view, view.findViewById(R.id.routine_arrow));
                mealCategoryView = view;
                mealCategoryName = view.findViewById(R.id.routine_header_name);
                mealViewContent = view.findViewById(R.id.view_list_main_content);
                mealActionContainer = view.findViewById(R.id.view_list_repo_action_container);
                removeMealCategoryButton = view.findViewById(R.id.swipe_delete);
                editMealCategoryButton = view.findViewById(R.id.swipe_edit);
                addNewMealButton = view.findViewById(R.id.swipe_new_meal);
            }

            //Populate meal category recycler view with meal object data
            //Set listeners on meal category recycler views to respond to user actions
            public void bind(int position) {
                super.bind(position);
                //Set meal category header name
                mealCategoryName.setText(visibleItems.get(position).mealCategoryName);

                //If swipe menu is open and edit button is clicked - edit the meal category name, otherwise perform normal click
                editMealCategoryButton.setOnClickListener(view -> {
                    if (mealItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        handleClick();
                        mealItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        AddNewMealCategoryCustomDialog EditMealCategoryCustomDialog = new AddNewMealCategoryCustomDialog(context, categoryName);
                        EditMealCategoryCustomDialog.editMealCategory(visibleItems.get(position).mealCategoryName);
                        EditMealCategoryCustomDialog.show();
                        mealItemTouchHelperExtension.closeOpened();
                    }
                });

                //If swipe menu is open and delete button is clicked - delete meal category and its content/meals, otherwise perform normal click
                removeMealCategoryButton.setOnClickListener(view -> {
                    if (mealItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        handleClick();
                        mealItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        if (DietPlan.listManager.deleteMealCategory(visibleItems.get(position).mealCategoryName)) {
                            mealItemTouchHelperExtension.closeOpened();
                            visibleItems.remove(visibleItems.get(position));
                        }
                        else {
                            Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                        }
                        //Half a second delay
                        new CountDownTimer(500, 1000) {
                            public void onTick(long millisUntilFinished) {}
                            public void onFinish() {
                                refreshMealCategoryView();
                                notifyDataSetChanged();
                            }
                        }.start();
                    }
                });

                //If add button is clicked - add new meal to meal category, otherwise perform normal click
                addNewMealButton.setOnClickListener(view -> {
                    AddNewMealCustomDialog AddNewMealCustomDialog = new AddNewMealCustomDialog(context, visibleItems.get(position).mealCategoryName);
                    if (mealItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        handleClick();
                        mealItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        AddNewMealCustomDialog.show();
                        mealItemTouchHelperExtension.closeOpened();
                    }
                });
            }
        }

        //Link meal category meal variables and logic with XML layout attributes
        class MealViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
            View mealView;
            View mealViewContent;
            View mealActionContainer;
            TextView mealName;
            View removeMealButton;
            View editMealButton;
            ImageButton moveButton;

            MealViewHolder(View view) {
                super(view);
                mealView = view;
                mealViewContent = view.findViewById(R.id.view_list_main_content);
                mealActionContainer = view.findViewById(R.id.view_list_repo_action_container);
                mealName = view.findViewById(R.id.name);
                removeMealButton = view.findViewById(R.id.swipe_delete);
                editMealButton = view.findViewById(R.id.swipe_edit);
                moveButton = view.findViewById(R.id.move_button);
            }

            //Populate meal category meal recycler view with meal object data
            //Set listeners on meal category meal recycler views to respond to user actions
            void bind(int position) {
                //Set meal name
                mealName.setText(visibleItems.get(position).mealName);
                //Hide the image button from the view to reuse the list item layout
                moveButton.setVisibility(View.GONE);

                //Display meal details
                mealView.setOnClickListener(view -> {
                    mealItemTouchHelperExtension.closeOpened();
                    showMealDetails(view, position);
                });

                //If swipe menu is open and edit button is clicked - edit the meal, otherwise display meal details
                editMealButton.setOnClickListener(view -> {
                    AddNewMealCustomDialog EditMealCustomDialog = new AddNewMealCustomDialog(context, visibleItems.get(position).mealCategoryName);
                    EditMealCustomDialog.editMeal(
                        position,
                        visibleItems,
                        visibleItems.get(position).id,
                        visibleItems.get(position).mealCategoryName,
                        visibleItems.get(position).mealName,
                        visibleItems.get(position).proteins,
                        visibleItems.get(position).carbs,
                        visibleItems.get(position).fats,
                        visibleItems.get(position).calories,
                        visibleItems.get(position).description
                    );

                    if (mealItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        showMealDetails(view, position);
                        mealItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        EditMealCustomDialog.show();
                        mealItemTouchHelperExtension.closeOpened();
                    }
                });

                //If swipe menu is open and delete button is clicked - delete the meal, otherwise display meal details
                removeMealButton.setOnClickListener(view -> {
                    if (mealItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        showMealDetails(view, position);
                        mealItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        if (DietPlan.listManager.deleteMeal(visibleItems.get(position).id)) {
                            mealItemTouchHelperExtension.closeOpened();
                            visibleItems.remove(visibleItems.get(position));
                        }
                        else {
                            Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                        }
                        //Half a second delay
                        new CountDownTimer(500, 1000) {
                            public void onTick(long millisUntilFinished) {}
                            public void onFinish() {
                                refreshMealCategoryView();
                                notifyDataSetChanged();
                            }
                        }.start();
                    }
                });
            }
        }

        //Inflate appropriate recycler view holder (either meal category or meal)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch(viewType) {
                case TYPE_ROUTINE_HEADER:
                    return new MealCategoryHeaderViewHolder(inflate(R.layout.meal_category_header, parent));
                case TYPE_ROUTINE_EXERCISES:
                default:
                    return new MealViewHolder(inflate(R.layout.list_item, parent));
            }
        }

        //Populate meal category or meal recycler view with meal object data
        @Override
        public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
            switch(getItemViewType(position)) {
                case TYPE_ROUTINE_HEADER:
                    ((MealCategoryHeaderViewHolder) holder).bind(position);
                    break;
                case TYPE_ROUTINE_EXERCISES:
                default:
                    ((MealViewHolder) holder).bind(position);
                    break;
            }
        }

        //Retrieve and return unique meal category meals and its content/data
        private List<DietPlanListItem> getDietPlanMeals() {
            //All meal categories and its meals to be further populated into the view
            List<DietPlanListItem> items = new ArrayList<>();
            //Meal category object content
            List<MealListItem> listOfCategories;
            //All meal category names
            String[] mealCategoryNames = new String[meal.size()];
            //Unique meal category names only
            String[] uniqueMealCategoryNames  = new String[meal.size()];

            //Retrieve unique meal category names
            for (int j = 0; j < meal.size(); j++) {
                mealCategoryNames[j] = meal.get(j).getMealCategoryName();
                uniqueMealCategoryNames = Arrays.stream(mealCategoryNames).distinct().toArray(String[]::new);
            }

            //Generate meal category groups
            for (int i = 0; i < uniqueMealCategoryNames.length; i++) {
                listOfCategories = listManager.getListOfDietPlanMeals(categoryName, uniqueMealCategoryNames[i]);
                mealGroups.add((ArrayList<MealListItem>) listOfCategories);
            }

            //Retrieve meals from unique meal category, store meal category headers and meals
            for (int i = 0; i < mealGroups.size(); i++) {
                items.add(new DietPlanListItem(uniqueMealCategoryNames[i]));
                for (int j = 0; j < mealGroups.get(i).size(); j++) {
                    if (!mealGroups.get(i).get(j).getMealName().equals("") && !mealGroups.get(i).get(j).getDescription().equals("")) {
                        items.add(new DietPlanListItem(
                            mealGroups.get(i).get(j).getId(),
                            mealGroups.get(i).get(j).getMealCategoryName(),
                            mealGroups.get(i).get(j).getMealName(),
                            mealGroups.get(i).get(j).getProteins(),
                            mealGroups.get(i).get(j).getCarbs(),
                            mealGroups.get(i).get(j).getFats(),
                            mealGroups.get(i).get(j).getCalories(),
                            mealGroups.get(i).get(j).getDescription()
                        ));
                    }
                }
            }

            return items;
        }

        //Display exercise details view, send the workout routine exercise content/data to the details view
        void showMealDetails(View view, Integer position) {
            Context context = view.getContext();
            Intent intent = new Intent(context, MealDetails.class);
            Bundle extras = new Bundle();
            extras.putString(MealDetails.itemName, visibleItems.get(position).mealName);
            extras.putString(MealDetails.itemProteins, String.valueOf(visibleItems.get(position).proteins));
            extras.putString(MealDetails.itemCarbs, String.valueOf(visibleItems.get(position).carbs));
            extras.putString(MealDetails.itemFats, String.valueOf(visibleItems.get(position).fats));
            extras.putString(MealDetails.itemCalories, String.valueOf(visibleItems.get(position).calories));
            extras.putString(MealDetails.itemDescription, visibleItems.get(position).description);
            intent.putExtras(extras);
            context.startActivity(intent);
        }

        //Refresh meal category list item and its content
        void refreshMealCategoryView() {
            adapter = new DietPlanAdapter(context, listManager.getListOfDietPlanMeals(categoryName));
            adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            adapter.setItemTouchHelperExtension(mealItemTouchHelper);
        }

        //Return current meal category or meal swipe menu state
        void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
            mealItemTouchHelperExtension = itemTouchHelperExtension;
        }
    }

    //Animate meal category and meal recycler view swipe
    class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (dY != 0 && dX == 0)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            if (viewHolder instanceof DietPlanAdapter.MealViewHolder) {
                DietPlanAdapter.MealViewHolder exerciseHolder = (DietPlanAdapter.MealViewHolder) viewHolder;
                if (dX < -exerciseHolder.mealActionContainer.getWidth()) {
                    dX = -exerciseHolder.mealActionContainer.getWidth();
                }
                exerciseHolder.mealViewContent.setTranslationX(dX);
                return;
            }

            if (viewHolder instanceof DietPlanAdapter.MealCategoryHeaderViewHolder) {
                DietPlanAdapter.MealCategoryHeaderViewHolder routineHolder = (DietPlanAdapter.MealCategoryHeaderViewHolder) viewHolder;
                if (dX < -routineHolder.mealActionContainer.getWidth()) {
                    dX = -routineHolder.mealActionContainer.getWidth();
                }
                routineHolder.mealViewContent.setTranslationX(dX);
            }
        }
    }
}
