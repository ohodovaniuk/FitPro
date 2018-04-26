package com.example.owner.fitpro.WorkoutAndExercise.Workout;

/*** Created by Hongcheng Zhang**/

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.owner.fitpro.R;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WorkoutTimeManager extends AppCompatActivity {

    //itemTime
    int[] TimerexerciseTimes = {10, 20, 30, 40, 0, 0, 0, 0, 0, 0};

    //itemNumberOfSet
    int[] TimerexerciseNumberOfSets = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    //itemName
    String[] TimerexerciseName = {"Exercise 1", "Exercise 2", "Exercise 3", "Excise 4", "Excise 5", "Excise 6", "Excise 7", "Excise 8", "Excise 9", "Excise 10"};

    //total time of every single item (exerviseTime = itemtime * sets)
    int[] TotalexerciseTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private long time;
    private long timeCountInMilliSeconds;
    long startTime;
    long difference;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    //make the timer start in the beginning
    private TimerStatus timerStatus = TimerStatus.STARTED;


    private ProgressBar progressBarCircle;
    private Button textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;
    private TextView exerciseTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin_workout_manager);

        //so the below three variables hold the all the exercises names times and number of set.
        //the actuall data inside each workout routine.
        Intent intent = getIntent();
        ArrayList<String> exerciseNames = (ArrayList<String>) intent.getSerializableExtra("exerciseNames");
        ArrayList<Integer> exerciseNumberOfSets = (ArrayList<Integer>) intent.getSerializableExtra("exerciseNumberOfSets");
        ArrayList<Integer> exerciseTimes = (ArrayList<Integer>) intent.getSerializableExtra("exerciseTimes");

        for (int i = 0; i < exerciseNames.size(); i++) {
            //Log.d("Exercise Names", exerciseNames.get(i));
            TimerexerciseName[i] = exerciseNames.get(i);
        }
        for (int i = 0; i < exerciseNumberOfSets.size(); i++) {
            //Log.d("Exercise number of sets sets", String.valueOf(exerciseNumberOfSets.get(i)));
            TimerexerciseNumberOfSets[i] = exerciseNumberOfSets.get(i);
        }
        for (int i = 0; i < exerciseTimes.size(); i++) {
            //Log.d("Exercise times", String.valueOf(exerciseTimes.get(i)));
            TimerexerciseTimes[i] = exerciseTimes.get(i);
        }

        //calculate total time
        time = 0;
        for (int i = 0; i < exerciseTimes.size(); i++) {

            time += exerciseTimes.get(i) * exerciseNumberOfSets.get(i);
        }

        //calculate TotalExerciseTime (TotalExerciseTime = single exerciseTimes * sets )
        for (int j = 0; j < exerciseTimes.size(); j++) {
            TotalexerciseTime[j] = exerciseTimes.get(j) * exerciseNumberOfSets.get(j);

        }

        //to set the SupportActionBar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Workout Timer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (Button) findViewById(R.id.textViewTime);
        imageViewReset = (ImageView) findViewById(R.id.imageViewReset);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);
        exerciseTextView = (TextView) findViewById(R.id.exerciseTitle);

        //converting to milliseconds
        timeCountInMilliSeconds = time * 1000;
        progressBarCircle.setMax((int) (timeCountInMilliSeconds) / 1000);
        progressBarCircle.setProgress((int) (timeCountInMilliSeconds) / 1000);
        ;
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));

        imageViewStartStop.setImageResource(R.drawable.ic_stop_countdown_icon);


        imageViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });


        imageViewStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseResume();
            }
        });


        startCountDownTimer(false, timeCountInMilliSeconds - difference);
        startTime = System.currentTimeMillis();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Function for resetting to the beginning of current exercise
    private void reset() {

        stopCountDownTimer();
        difference += (((int) (System.currentTimeMillis() - startTime) / 1000) + 1) * 1000;

        if (timerStatus == TimerStatus.STARTED) {
            if ((difference / 1000) <= TotalexerciseTime[0]) {
                difference = 0;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else if ((difference / 1000) > TotalexerciseTime[0] && (difference / 1000) <= (TotalexerciseTime[0] + TotalexerciseTime[1])) {
                difference = TotalexerciseTime[0] * 1000;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else if ((difference / 1000) > (TotalexerciseTime[0] + TotalexerciseTime[1]) && (difference / 1000) <= (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2])) {
                difference = (TotalexerciseTime[0] + TotalexerciseTime[1]) * 1000;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else if ((difference / 1000) > (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2]) && (difference / 1000) <= (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2] + TotalexerciseTime[3])) {
                difference = (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2]) * 1000;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else {
            }
            // Here is timerStatus == TimerStatus.STOPED
        } else {
            timerStatus = TimerStatus.STARTED;
            imageViewStartStop.setImageResource(R.drawable.ic_stop_countdown_icon);
            if ((difference / 1000) <= TotalexerciseTime[0]) {
                difference = 0;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else if ((difference / 1000) > TotalexerciseTime[0] && (difference / 1000) <= (TotalexerciseTime[0] + TotalexerciseTime[1])) {
                difference = TotalexerciseTime[0] * 1000;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else if ((difference / 1000) > (TotalexerciseTime[0] + TotalexerciseTime[1]) && (difference / 1000) <= (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2])) {
                difference = (TotalexerciseTime[0] + TotalexerciseTime[1]) * 1000;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else if ((difference / 1000) > (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2]) && (difference / 1000) <= (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2] + TotalexerciseTime[3])) {
                difference = (TotalexerciseTime[0] + TotalexerciseTime[1] + TotalexerciseTime[2]) * 1000;
                startCountDownTimer(false, timeCountInMilliSeconds - difference);

            } else {
            }

        }

        startTime = System.currentTimeMillis();
    }


    //This function to start and stop timer for timer
    private void pauseResume() {

        if (timerStatus == TimerStatus.STARTED) {
            imageViewStartStop.setImageResource(R.drawable.ic_start_countdown_icon);
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
            difference += (((int) (System.currentTimeMillis() - startTime) / 1000) + 1) * 1000;

        } else {
            imageViewStartStop.setImageResource(R.drawable.ic_stop_countdown_icon);
            timerStatus = TimerStatus.STARTED;
            //new
            startTime = System.currentTimeMillis();
            startCountDownTimer(false, timeCountInMilliSeconds - difference);
        }

    }


    private void startCountDownTimer(boolean restart, final long time) {
        if (restart == false) {
            countDownTimer(time);
        } else {
            difference = 0;
            countDownTimer(time);
        }
    }

    //this function used to schedule the countdown timer until a time down
    private void countDownTimer(final long time) {
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
                //change exercise name with time changed. the timer gonna call onTick every single second until time done.
                changeExerciseName((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(time));
                progressBarCircle.setMax((int) (time) / 1000);
                progressBarCircle.setProgress((int) (time) / 1000);
                imageViewReset.setVisibility(View.VISIBLE);
                timerStatus = TimerStatus.STOPPED;
            }
        }.start();
        //start the timer
        countDownTimer.start();
    }


    //stop the timer
    private void stopCountDownTimer() {
        countDownTimer.cancel();

    }


    private void ChangeColor() {
        progressBarCircle.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
    }


    //this function for changing the name with time change
    private void changeExerciseName(long remainingTime) {

        if (remainingTime >= (time - TotalexerciseTime[0])) {
            exerciseTextView.setText(TimerexerciseName[0]);
        } else if (remainingTime < (time - TotalexerciseTime[0]) && remainingTime >= (time - TotalexerciseTime[0] - TotalexerciseTime[1])) {
            exerciseTextView.setText(TimerexerciseName[1]);
        } else if (remainingTime < (time - TotalexerciseTime[0] - TotalexerciseTime[1]) && remainingTime >= (time - TotalexerciseTime[0] - TotalexerciseTime[1] - TotalexerciseTime[2])) {
            exerciseTextView.setText(TimerexerciseName[2]);
        } else if (remainingTime < (time - TotalexerciseTime[0] - TotalexerciseTime[1] - TotalexerciseTime[2]) && remainingTime > (time - TotalexerciseTime[0] - TotalexerciseTime[1] - TotalexerciseTime[2] - TotalexerciseTime[3])) {
            exerciseTextView.setText(TimerexerciseName[3]);
        } else {
            exerciseTextView.setText("Done !");
        }

    }

    //this function coverts the millisecond to HH:MM:SS
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }
}
