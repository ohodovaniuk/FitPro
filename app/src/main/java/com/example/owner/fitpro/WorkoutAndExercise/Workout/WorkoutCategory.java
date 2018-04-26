package com.example.owner.fitpro.WorkoutAndExercise.Workout;

/**Created by Oleh Hodovaniuk**/

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.example.owner.fitpro.WorkoutAndExercise.DietPlan.DietPlan;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.AddNewExerciseCustomDialog;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseDetails;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseListItem;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseListManager;
import com.example.owner.fitpro.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutCategory extends AppCompatActivity {
    public static String categoryName;
    public static ExerciseListManager listManager;
    public static WorkoutAdapter adapter;
    private RecyclerView recyclerView;
    public ItemTouchHelperExtension workoutRoutineItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_category_list_items);

        //Customise workout category toolbar
        Intent intent = getIntent();
        categoryName = intent.getStringExtra(categoryName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(""); //hide default tool bar title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TextView toolbarTitle = (TextView) findViewById(R.id.tool_bar_title);
        toolbarTitle.setText(categoryName);

        //Create workout category list view of workout routines, add divider under each list item
        recyclerView = (RecyclerView) findViewById(R.id.exercise_list_item);
        listManager = new ExerciseListManager(getApplicationContext());
        adapter = new WorkoutAdapter(listManager.getListOfWorkoutCategoryExercises(categoryName), this);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CustomRecyclerViewDivider(this, 16));
        recyclerView.setAdapter(adapter);

        //Enable recycler view swipe, each list view workout routine or exercise can be swiped to the right to show swipe menu/options
        ItemTouchHelperExtension.Callback workoutRoutineCallback = new ItemTouchHelperCallback();
        workoutRoutineItemTouchHelper = new ItemTouchHelperExtension(workoutRoutineCallback);
        workoutRoutineItemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setItemTouchHelperExtension(workoutRoutineItemTouchHelper);

        //Create workout category boom menu with options to either add new workout routine, begin workout or view diet plan
        BoomMenuButton workoutCategoryBoomMenu = (BoomMenuButton) findViewById(R.id.boom_menu_button);
        assert workoutCategoryBoomMenu != null;
        workoutCategoryBoomMenu.setButtonEnum(ButtonEnum.Ham);
        workoutCategoryBoomMenu.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        workoutCategoryBoomMenu.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);

        HamButton.Builder newWorkoutRoutineBuilder = new HamButton.Builder()
            .normalImageRes(R.drawable.ic_add_routine)
            .normalText("New Workout Routine")
            .subNormalText("Adds new workout routine to the category list")
            .textSize(16)
            .subTextSize(10)
            .normalColorRes(R.color.holo_purple_dark)
            .highlightedColorRes(R.color.holo_purple_light)
            .textPadding(new Rect(0, 0, 0, 25))
            .subTextPadding(new Rect(0, 0, 0, 20))
            .listener(index -> {
                AddNewWorkoutRoutineCustomDialog NewWorkoutRoutineCustomDialog = new AddNewWorkoutRoutineCustomDialog(this);
                //Half a second delay, so that the boom menu has enough time to close with animation
                new CountDownTimer(500, 1000) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        NewWorkoutRoutineCustomDialog.show();
                    }
                }.start();
            });

        HamButton.Builder beginWorkoutBuilder = new HamButton.Builder()
            .normalImageRes(R.drawable.ic_begin_workout)
            .normalText("Begin Workout")
            .subNormalText("Starts workout based on the routine selected")
            .textSize(16)
            .subTextSize(10)
            .normalColorRes(R.color.holo_blue_dark)
            .highlightedColorRes(R.color.holo_blue_light)
            .textPadding(new Rect(0, 0, 0, 25))
            .subTextPadding(new Rect(0, 0, 0, 20))
            .listener(index -> {
                BeginWorkoutCustomDialog BeginWorkoutCustomDialog = new BeginWorkoutCustomDialog(this, categoryName);
                //Half a second delay, so that the boom menu has enough time to close with animation
                new CountDownTimer(500, 1000) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        BeginWorkoutCustomDialog.show();
                    }
                }.start();
            });

        HamButton.Builder dietPlanBuilder = new HamButton.Builder()
            .normalImageRes(R.drawable.ic_diet_plan)
            .normalText("Diet Plan")
            .subNormalText("Displays your current nutrition plan")
            .textSize(16)
            .subTextSize(10)
            .normalColorRes(R.color.holo_yellow_dark)
            .highlightedColorRes(R.color.holo_yellow_light)
            .textPadding(new Rect(0, 0, 0, 25))
            .subTextPadding(new Rect(0, 0, 0, 20))
            .listener(index -> {
                Intent intent2 = new Intent(this, DietPlan.class);
                Bundle extras = new Bundle();
                extras.putString(DietPlan.categoryName, categoryName);
                intent2.putExtras(extras);
                startActivity(intent2);
            });

        //Add ham buttons to the workout category boom menu
        workoutCategoryBoomMenu.addBuilder(newWorkoutRoutineBuilder);
        workoutCategoryBoomMenu.addBuilder(beginWorkoutBuilder);
        workoutCategoryBoomMenu.addBuilder(dietPlanBuilder);
    }

    //Workout Recycler View Adapter
    public class WorkoutAdapter extends ExpandableRecyclerAdapter<WorkoutAdapter.WorkoutListItem> {
        private Context context;
        private List<ExerciseListItem> exercise;
        private ItemTouchHelperExtension mItemTouchHelperExtension;
        List<ArrayList<ExerciseListItem>> workoutRoutineGroups = new ArrayList<>(); //holds all existing routines and its details as a group

        //Initialise current workout routine exercise and context
        WorkoutAdapter(List<ExerciseListItem> exercise, Context context) {
            super(context);
            this.exercise = exercise;
            this.context = context;
            setItems(getWorkoutRoutineExercises());
        }

        //Set individual workout routine exercise content
        public class WorkoutListItem extends ExpandableRecyclerAdapter.ListItem {
            private String workoutRoutineName, exerciseName, description;
            private Integer id, numberOfSets, repsPerSet, setEstimateTime;

            //Initialise workout routine name
            WorkoutListItem(String workoutRoutineName) {
                super(TYPE_ROUTINE_HEADER);
                this.workoutRoutineName = workoutRoutineName;
            }

            //Initialise workout routine name and exercise object
            WorkoutListItem(Integer id, String workoutRoutineName, String exerciseName, Integer numberOfSets, Integer repsPerSet, Integer setEstimateTime, String description) {
                super(TYPE_ROUTINE_EXERCISES);
                this.id = id;
                this.workoutRoutineName = workoutRoutineName;
                this.exerciseName = exerciseName;
                this.numberOfSets = numberOfSets;
                this.repsPerSet = repsPerSet;
                this.setEstimateTime = setEstimateTime;
                this.description = description;
            }
        }

        //Link workout routine header variables and logic with XML layout attributes
        public class RoutineHeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
            View workoutRoutineView;
            View mViewContent;
            View mActionContainer;
            TextView routineName;
            View removeRoutineButton;
            View editRoutineButton;

            RoutineHeaderViewHolder(View view) {
                super(view, view.findViewById(R.id.routine_arrow));
                workoutRoutineView = view;
                mViewContent = view.findViewById(R.id.view_list_main_content);
                mActionContainer = view.findViewById(R.id.view_list_repo_action_container);
                routineName = view.findViewById(R.id.routine_header_name);
                removeRoutineButton = view.findViewById(R.id.swipe_delete);
                editRoutineButton = view.findViewById(R.id.swipe_edit);
            }

            //Populate workout routine recycler view with exercise object data
            //Set listeners on workout routine recycler views to respond to user actions
            public void bind(int position) {
                super.bind(position);
                //Set workout routine header name
                routineName.setText(visibleItems.get(position).workoutRoutineName);

                //If swipe menu is open and edit button is clicked - edit the workout routine name, otherwise perform normal click
                editRoutineButton.setOnClickListener(view -> {
                    if (mItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        handleClick();
                        mItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        AddNewWorkoutRoutineCustomDialog EditWorkoutRoutineCustomDialog = new AddNewWorkoutRoutineCustomDialog(context);
                        EditWorkoutRoutineCustomDialog.editWorkoutRoutine(
                            visibleItems.get(position).workoutRoutineName,
                            categoryName
                        );
                        EditWorkoutRoutineCustomDialog.show();
                        mItemTouchHelperExtension.closeOpened();
                    }
                });

                //If swipe menu is open and delete button is clicked - delete workout routine and its content/exercises, otherwise perform normal click
                removeRoutineButton.setOnClickListener(view -> {
                    if (mItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        handleClick();
                        mItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        if (WorkoutCategory.listManager.deleteWorkoutRoutine(visibleItems.get(position).workoutRoutineName)) {
                            mItemTouchHelperExtension.closeOpened();
                            visibleItems.remove(visibleItems.get(position));
                        }
                        else {
                            Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                        }
                        //Half a second delay
                        new CountDownTimer(500, 1000) {
                            public void onTick(long millisUntilFinished) {}
                            public void onFinish() {
                                refreshWorkoutCategoryView();
                                notifyDataSetChanged();
                            }
                        }.start();
                    }
                });
            }
        }

        //Link workout routine exercise variables and logic with XML layout attributes
        class ExerciseViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
            View exerciseView;
            View mViewContent;
            View mActionContainer;
            TextView exerciseName;
            View removeExerciseButton;
            View editExerciseButton;
            ImageButton moveButton;

            ExerciseViewHolder(View view) {
                super(view);
                exerciseView = view;
                mViewContent = view.findViewById(R.id.view_list_main_content);
                mActionContainer = view.findViewById(R.id.view_list_repo_action_container);
                exerciseName = view.findViewById(R.id.name);
                removeExerciseButton = view.findViewById(R.id.swipe_delete);
                editExerciseButton = view.findViewById(R.id.swipe_edit);
                moveButton = view.findViewById(R.id.move_button);
            }

            //Populate workout routine exercise recycler view with exercise object data
            //Set listeners on workout routine exercise recycler views to respond to user actions
            void bind(int position) {
                //Set workout routine exercise name
                exerciseName.setText(visibleItems.get(position).exerciseName);
                //Hide the image button from the view to reuse the list item layout
                moveButton.setVisibility(View.GONE);

                //Display workout routine exercise details
                exerciseView.setOnClickListener(view -> {
                    mItemTouchHelperExtension.closeOpened();
                    showExerciseDetails(view, position);
                });

                //If swipe menu is open and edit button is clicked - edit the workout routine exercise, otherwise display exercise details
                editExerciseButton.setOnClickListener(view -> {
                    AddNewExerciseCustomDialog EditExerciseCustomDialog = new AddNewExerciseCustomDialog(WorkoutCategory.this);
                    EditExerciseCustomDialog.editWorkoutRoutineExercise(
                        position,
                        visibleItems,
                        visibleItems.get(position).id,
                        visibleItems.get(position).workoutRoutineName,
                        visibleItems.get(position).exerciseName,
                        visibleItems.get(position).numberOfSets,
                        visibleItems.get(position).repsPerSet,
                        visibleItems.get(position).setEstimateTime,
                        visibleItems.get(position).description
                    );

                    if (mItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        showExerciseDetails(view, position);
                        mItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        EditExerciseCustomDialog.show();
                        mItemTouchHelperExtension.closeOpened();
                    }
                });

                //If swipe menu is open and delete button is clicked - delete the workout routine exercise, otherwise display exercise details
                removeExerciseButton.setOnClickListener(view -> {
                    if (mItemTouchHelperExtension.isSwipeMenuClosed() == 0) {
                        showExerciseDetails(view, position);
                        mItemTouchHelperExtension.closeOpened();
                    }
                    else {
                        if (WorkoutCategory.listManager.deleteExercise(visibleItems.get(position).id)) {
                            mItemTouchHelperExtension.closeOpened();
                            visibleItems.remove(visibleItems.get(position));
                        }
                        else {
                            Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                        }
                        //Half a second delay
                        new CountDownTimer(500, 1000) {
                            public void onTick(long millisUntilFinished) {}
                            public void onFinish() {
                                refreshWorkoutCategoryView();
                                notifyDataSetChanged();
                            }
                        }.start();
                    }
                });
            }
        }

        //Inflate appropriate recycler view holder (either workout routine or routine exercise)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch(viewType) {
                case TYPE_ROUTINE_HEADER:
                    return new RoutineHeaderViewHolder(inflate(R.layout.workout_routine_header, parent));
                case TYPE_ROUTINE_EXERCISES:
                default:
                    return new ExerciseViewHolder(inflate(R.layout.list_item, parent));
            }
        }

        //Populate workout routine or routine exercise recycler view with exercise object data
        @Override
        public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
            switch(getItemViewType(position)) {
                case TYPE_ROUTINE_HEADER:
                    ((RoutineHeaderViewHolder) holder).bind(position);
                    break;
                case TYPE_ROUTINE_EXERCISES:
                default:
                    ((ExerciseViewHolder) holder).bind(position);
                    break;
            }
        }

        //Retrieve and return unique workout routine exercises and its content/data
        private List<WorkoutListItem> getWorkoutRoutineExercises() {
            //All routines and its content to be further populated into the view
            List<WorkoutListItem> items = new ArrayList<>();
            //Workout routine exercise object content
            List<ExerciseListItem> listOfRoutines;
            //All workout routine names
            String[] routineNames = new String[exercise.size()];
            //Unique workout routine names only
            String[] uniqueRoutineNames = new String[exercise.size()];

            //Retrieve unique workout routine names
            for (int j = 0; j < exercise.size(); j++) {
                routineNames[j] = exercise.get(j).getWorkoutRoutineName();
                uniqueRoutineNames = Arrays.stream(routineNames).distinct().toArray(String[]::new);
            }

            //Generate workout routine groups
            for (int i = 0; i < uniqueRoutineNames.length; i++) {
                listOfRoutines = listManager.getListOfWorkoutRoutineExercises(categoryName, uniqueRoutineNames[i]);
                workoutRoutineGroups.add((ArrayList<ExerciseListItem>) listOfRoutines);
            }

            //Retrieve exercises from unique workout routines, store workout routine headers and exercises
            for (int i = 0; i < workoutRoutineGroups.size(); i++) {
                items.add(new WorkoutListItem(uniqueRoutineNames[i]));
                for (int j = 0; j < workoutRoutineGroups.get(i).size(); j++) {
                    if (!workoutRoutineGroups.get(i).get(j).getExerciseName().equals("") && !workoutRoutineGroups.get(i).get(j).getDescription().equals("")) {
                        items.add(new WorkoutListItem(
                            workoutRoutineGroups.get(i).get(j).getId(),
                            workoutRoutineGroups.get(i).get(j).getWorkoutRoutineName(),
                            workoutRoutineGroups.get(i).get(j).getExerciseName(),
                            workoutRoutineGroups.get(i).get(j).getNumberOfSets(),
                            workoutRoutineGroups.get(i).get(j).getRepsPerSet(),
                            workoutRoutineGroups.get(i).get(j).getSetEstimateTime(),
                            workoutRoutineGroups.get(i).get(j).getDescription()
                        ));
                    }
                }
            }

            return items;
        }

        //Display exercise details view, send the workout routine exercise content/data to the details view
        void showExerciseDetails(View view, Integer position) {
            Context context = view.getContext();
            Intent intent = new Intent(context, ExerciseDetails.class);
            Bundle extras = new Bundle();
            extras.putString(ExerciseDetails.itemName, visibleItems.get(position).exerciseName);
            extras.putString(ExerciseDetails.itemNumberOfSets, String.valueOf(visibleItems.get(position).numberOfSets));
            extras.putString(ExerciseDetails.itemRepsPerSet, String.valueOf(visibleItems.get(position).repsPerSet));
            extras.putString(ExerciseDetails.itemSetEstimateTime, String.valueOf(visibleItems.get(position).setEstimateTime));
            extras.putString(ExerciseDetails.itemDescription, visibleItems.get(position).description);
            intent.putExtras(extras);
            context.startActivity(intent);
        }

        //Refresh workout routine list item and its content
        public void refreshWorkoutCategoryView() {
            adapter = new WorkoutAdapter(listManager.getListOfWorkoutCategoryExercises(categoryName), context);
            adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            adapter.setItemTouchHelperExtension(workoutRoutineItemTouchHelper);
        }

        //Return current workout routine or routine exercise swipe menu state
        public ItemTouchHelperExtension getItemTouchHelperExtension() {
            return mItemTouchHelperExtension;
        }

        //Reset current workout routine or routine exercise recycler view swipe menu state
        void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
            mItemTouchHelperExtension = itemTouchHelperExtension;
        }
    }

    //Animate workout category routine and routine exercise recycler view swipe
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

            if (viewHolder instanceof WorkoutAdapter.ExerciseViewHolder) {
                WorkoutAdapter.ExerciseViewHolder exerciseHolder = (WorkoutAdapter.ExerciseViewHolder) viewHolder;
                if (dX < -exerciseHolder.mActionContainer.getWidth()) {
                    dX = -exerciseHolder.mActionContainer.getWidth();
                }
                exerciseHolder.mViewContent.setTranslationX(dX);
                return;
            }

            if (viewHolder instanceof WorkoutAdapter.RoutineHeaderViewHolder) {
                WorkoutAdapter.RoutineHeaderViewHolder routineHolder = (WorkoutAdapter.RoutineHeaderViewHolder) viewHolder;
                if (dX < -routineHolder.mActionContainer.getWidth()) {
                    dX = -routineHolder.mActionContainer.getWidth();
                }
                routineHolder.mViewContent.setTranslationX(dX);
            }
        }
    }
}


