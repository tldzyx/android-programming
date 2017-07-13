package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private static final String KEY_CURRENT_INDEX = "current_index";
    private static final String KEY_IS_CHEATER = "is_cheater";
    private static final String KEY_REMAIN_CHEATE_TIMES = "remain_cheate_times";
    private static final int REQUEST_CODE_CHEAT = 0;

    private TextView mQuestionTextView;
    private Button mCheatButton;
    private TextView mRemainCheateTimesTextView;
    private Toast mToast;

    private Question[] mQuestionBank = new Question[]{
      new Question(R.string.question_oceans, true),
      new Question(R.string.question_mideast, false),
      new Question(R.string.question_africa, false),
      new Question(R.string.question_americas, true),
      new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater = false;
    private byte mRemainCheateTimes = 3;

    private void updateQuestion() {
        Question question = mQuestionBank[mCurrentIndex];
        int questionTextResId = question.getTextResId();
        mQuestionTextView.setText(questionTextResId);

        if(mToast != null)
        {
            mToast.cancel();
        }
    }

    private void checkAnswer(boolean userPressedTrue){

        Question question = mQuestionBank[mCurrentIndex];
        boolean answerIsTrue = question.isAnswerTrue();

        int messageResId = 0;
        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        } else if(userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 15);

        if(mToast != null)
        {
            mToast.cancel();
        }
        mToast = toast;
        mToast.show();
    }

    private void updateRemainCheateTimes(){
        mRemainCheateTimesTextView.setText(String.format("You remain %d times to cheat.", mRemainCheateTimes));
        if(mRemainCheateTimes <= 0) {
            mCheatButton.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
            mRemainCheateTimes = savedInstanceState.getByte(KEY_REMAIN_CHEATE_TIMES, (byte)0);
        }

        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);

        Button trueButton = (Button) findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        Button falseButton = (Button) findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mRemainCheateTimesTextView = (TextView)findViewById(R.id.remain_cheate_times_text_view);
        updateRemainCheateTimes();

        Button prevButton = (Button) findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult(int,int,Intent) called");

        switch (requestCode){
            case REQUEST_CODE_CHEAT:{
                switch (resultCode){
                    case RESULT_OK:{
                        if(data != null){
                            mIsCheater = CheatActivity.wasAnswerShown(data);

                            --mRemainCheateTimes;
                            updateRemainCheateTimes();
                        }
                    }break;
                }
            }break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState(Bundle) called");
        outState.putInt(KEY_CURRENT_INDEX, mCurrentIndex);
        outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
        outState.putByte(KEY_REMAIN_CHEATE_TIMES, mRemainCheateTimes);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
