package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.owner.fitpro.WorkoutAndExercise.Animation.CustomRecyclerViewDivider;
import com.example.owner.fitpro.WorkoutAndExercise.Animation.ItemTouchHelperExtension;
import com.example.owner.fitpro.R;
import java.util.List;

public class ExerciseCategory extends AppCompatActivity {
    public static String categoryName;
    public static ExerciseListManager listManager;
    public static ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_category_list_items);

        //Customise exercise category toolbar
        Intent intent = getIntent();
        categoryName = intent.getStringExtra(categoryName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TextView toolbarTitle = (TextView) findViewById(R.id.tool_bar_title);
        toolbarTitle.setText(categoryName);

        //Create exercise category list view of exercise items, add divider under each list item
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exercise_list_item);
        listManager = new ExerciseListManager(getApplicationContext());
        adapter = new ExerciseAdapter(getApplicationContext(), listManager.getListOfExerciseCategoryExercises(categoryName));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new CustomRecyclerViewDivider(this, 16));
        recyclerView.setAdapter(adapter);

        //Enable recycler view swipe, each list view exercise can be swiped to the right to show swipe menu/options
        ItemTouchHelperExtension.Callback exerciseCallback = new ItemTouchHelperCallback();
        ItemTouchHelperExtension exerciseTouchHelper = new ItemTouchHelperExtension(exerciseCallback);
        exerciseTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setItemTouchHelperExtension(exerciseTouchHelper);

        //Hide Add New Exercise floating button during the scroll down, display the button again on scroll up
        NestedScrollView scroll = (NestedScrollView) findViewById(R.id.exercise_nested_scroll);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_new_exercise);
        if (scroll != null) {
            scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                //Scroll down
                if (scrollY > oldScrollY || scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    addButton.hide();
                }
                //Scroll Up
                else if (scrollY < oldScrollY || scrollY == 0) {
                    addButton.show();
                }
            });
        }

        //Trigger Add New Exercise Custom Dialog, add new exercise to exercise category list view
        AddNewExerciseCustomDialog NewExerciseCustomDialog = new AddNewExerciseCustomDialog(this);
        addButton.setOnClickListener(view ->
            NewExerciseCustomDialog.show()
        );
     }

    //Exercise Recycler View Adapter
    class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
        private Context context;
        private List<ExerciseListItem> exercise;
        private ItemTouchHelperExtension exerciseTouchHelperExtension;

        //Initialise current application context and exercise instance
        ExerciseAdapter(Context context, List<ExerciseListItem> exercise) {
            this.context = context;
            this.exercise = exercise;
        }

        //Link exercise category variables and logic with XML layout attributes
        class ViewHolder extends RecyclerView.ViewHolder {
            private View exerciseCategoryView;
            private View exerciseViewContent;
            private View exerciseActionContainer;
            private TextView exerciseName;
            private ImageButton addExerciseToWorkoutRoutineButton;
            private View removeExerciseButton;
            private View editExerciseButton;

            ViewHolder(View view) {
                super(view);
                exerciseCategoryView = view;
                exerciseViewContent = view.findViewById(R.id.view_list_main_content);
                exerciseActionContainer = view.findViewById(R.id.view_list_repo_action_container);
                exerciseName = view.findViewById(R.id.name);
                addExerciseToWorkoutRoutineButton = view.findViewById(R.id.move_button);
                removeExerciseButton = view.findViewById(R.id.swipe_delete);
                editExerciseButton = view.findViewById(R.id.swipe_edit);
            }
        }

        //Inflate exercise category recycler view holder
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(itemView);
        }

        //Populate exercise category recycler view with exercise object data
        //Set listeners on exercise category list item recycler view to respond to user actions
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //Set exercise name
            holder.exerciseName.setText(exercise.get(position).getExerciseName());

            //Display exercise details
            holder.exerciseCategoryView.setOnClickListener(view -> {
                exerciseTouchHelperExtension.closeOpened();
                showExerciseDetails(view, position);
            });

            //If swipe menu is open and edit button is clicked - edit the exercise, otherwise display exercise details
            holder.editExerciseButton.setOnClickListener(view -> {
                AddNewExerciseCustomDialog EditExerciseCustomDialog = new AddNewExerciseCustomDialog(ExerciseCategory.this);
                EditExerciseCustomDialog.editExercise(
                    categoryName,
                    position,
                    exercise,
                    exercise.get(position).getId(),
                    exercise.get(position).getExerciseName(),
                    exercise.get(position).getNumberOfSets(),
                    exercise.get(position).getRepsPerSet(),
                    exercise.get(position).getSetEstimateTime(),
                    exercise.get(position).getDescription()
                );

                if (exerciseTouchHelperExtension.isSwipeMenuClosed() == 0) {
                    showExerciseDetails(view, position);
                    exerciseTouchHelperExtension.closeOpened();
                }
                else {
                    EditExerciseCustomDialog.show();
                    exerciseTouchHelperExtension.closeOpened();
                }
            });

            //If swipe menu is open and delete button is clicked - delete the exercise, otherwise display exercise details
            holder.removeExerciseButton.setOnClickListener(view -> {
                if (exerciseTouchHelperExtension.isSwipeMenuClosed() == 0) {
                    showExerciseDetails(view, position);
                    exerciseTouchHelperExtension.closeOpened();
                }
                else {
                    if (ExerciseCategory.listManager.deleteExercise(exercise.get(position).getId())) {
                        exerciseTouchHelperExtension.closeOpened();
                        exercise.remove(exercise.get(position));
                    }
                    else {
                        Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                    }
                    //Half a second delay
                    new CountDownTimer(500, 1000) {
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            notifyDataSetChanged();
                        }
                    }.start();
                }
            });

            //If add exercise to workout routine button is clicked - add exercise from exercise category list view to workout routine view
            holder.addExerciseToWorkoutRoutineButton.setOnClickListener(view -> {
                AddExerciseToWorkoutRoutineCustomDialog AddExerciseToWorkoutCustomDialog = new AddExerciseToWorkoutRoutineCustomDialog(view.getContext(), categoryName);
                AddExerciseToWorkoutCustomDialog.setExerciseName(exercise.get(position).getExerciseName());
                AddExerciseToWorkoutCustomDialog.show();
                notifyDataSetChanged();
            });
        }

        //Return number of exercises inside the exercise category list view
        @Override
        public int getItemCount() {
            return exercise.size();
        }

        //Display exercise details view, send the exercise content/data to the details view
        void showExerciseDetails(View view, Integer position) {
            Context context = view.getContext();
            Intent intent = new Intent(context, ExerciseDetails.class);
            Bundle extras = new Bundle();
            extras.putString(ExerciseDetails.itemName, exercise.get(position).getExerciseName());
            extras.putString(ExerciseDetails.itemNumberOfSets, exercise.get(position).getNumberOfSets() + "");
            extras.putString(ExerciseDetails.itemRepsPerSet, exercise.get(position).getRepsPerSet() + "");
            extras.putString(ExerciseDetails.itemSetEstimateTime, exercise.get(position).getSetEstimateTime() + "");
            extras.putString(ExerciseDetails.itemDescription, exercise.get(position).getDescription() + "");
            intent.putExtras(extras);
            context.startActivity(intent);
        }

        //Refresh exercise category list item and its content
        void refreshExerciseItem(List<ExerciseListItem> exercise) {
            this.exercise = exercise;
            notifyDataSetChanged();
        }

        //Reset current exercise recycler view swipe menu state
        void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
            exerciseTouchHelperExtension = itemTouchHelperExtension;
        }
    }

    //Animate exercise category list item swipe
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

            ExerciseAdapter.ViewHolder holder = (ExerciseAdapter.ViewHolder) viewHolder;
            if (viewHolder != null) {
                if (dX < -holder.exerciseActionContainer.getWidth()) {
                    dX = -holder.exerciseActionContainer.getWidth();
                }
                holder.exerciseViewContent.setTranslationX(dX);
            }
        }
    }
}


