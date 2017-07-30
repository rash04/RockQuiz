package rhabib8.kaunbanegakrorepati;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private int mScore = 0;
    private int mAnswered = 0;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_SCORE = "score";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_IS_CHEATER = "isCheater";
    private static final String KEY_CHEATER_RECORD = "cheaterRecord";
    private static final String KEY_CHEATS_REMAINING = "cheatsRemaining";
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private static final int REQUEST_CODE_CHEAT = 0;
    //private boolean mIsCheater;
    private int mCheatsRemaining = 3;
    private TextView mCheatsRemainingText;
    private static final String KEY_CHEAT_BUTTON_STATUS = "cheatButtonStatus";
    private static final String KEY_TRUE_FALSE_BUTTON_STATUS = "trueFalseButtonStatus";
    private boolean mCheatButtonEnabled = true;
    private boolean mTrueFalseButtonEnabled = true;


    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_1, true),
            new Question(R.string.question_2, false),
            new Question(R.string.question_3, false),
            new Question(R.string.question_4, true),
            new Question(R.string.question_5, true),
            new Question(R.string.question_6, false)
    };

    //Stores which questions have been cheated on
    private boolean[] mCheaterRecord = new boolean[mQuestionBank.length];

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mScore = savedInstanceState.getInt(KEY_SCORE);
            mAnswered = savedInstanceState.getInt(KEY_ANSWER);
            //mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER);
            mCheaterRecord = savedInstanceState.getBooleanArray(KEY_CHEATER_RECORD);
            mCheatsRemaining = savedInstanceState.getInt(KEY_CHEATS_REMAINING);
            mCheatButtonEnabled = savedInstanceState.getBoolean(KEY_CHEAT_BUTTON_STATUS);
            mTrueFalseButtonEnabled = savedInstanceState.getBoolean(KEY_TRUE_FALSE_BUTTON_STATUS);
            Log.i(TAG, "status retrieved");
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        //TRUE/FALSE BUTTONS
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswered++;
                checkAnswer(true);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
                mTrueFalseButtonEnabled = false;
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAnswered++;
                checkAnswer(false);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
                mTrueFalseButtonEnabled = false;
            }
        });

        //PREV/NEXT BUTTONS
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                //mIsCheater = false;
                updateQuestion();
                mTrueFalseButtonEnabled = true;
            }
        });
        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mQuestionBank.length + (mCurrentIndex-1)) % mQuestionBank.length;
                //mIsCheater = false;
                updateQuestion();
                mTrueFalseButtonEnabled = true;
            }
        });

        //CHEAT BUTTON
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        if (!mCheatButtonEnabled){
            mCheatButton.setEnabled(false);
        }
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        mCheatsRemainingText = (TextView)findViewById(R.id.cheats_remaining);
        mCheatsRemainingText.setText(getResources().getString(R.string.cheats_remaining, mCheatsRemaining));

        updateQuestion();
        if (!mTrueFalseButtonEnabled) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }

    }

    //on return from CheatActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;
            //mIsCheater = CheatActivity.wasAnswerShown(data);
            if (CheatActivity.wasAnswerShown(data)) {
                mCheaterRecord[mCurrentIndex] = true;
                mCheatsRemaining--;
                if (mCheatsRemaining < 1) {
                    mCheatButton.setEnabled(false);
                    mCheatButtonEnabled = false;
                }
                mCheatsRemainingText.setText(getResources().getString(R.string.cheats_remaining, mCheatsRemaining));

            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_SCORE, mScore);
        savedInstanceState.putInt(KEY_ANSWER, mAnswered);
        //savedInstanceState.putBoolean(KEY_IS_CHEATER, mIsCheater);
        savedInstanceState.putBooleanArray(KEY_CHEATER_RECORD, mCheaterRecord);
        savedInstanceState.putInt(KEY_CHEATS_REMAINING, mCheatsRemaining);
        savedInstanceState.putBoolean(KEY_CHEAT_BUTTON_STATUS, mCheatButtonEnabled);
        savedInstanceState.putBoolean(KEY_TRUE_FALSE_BUTTON_STATUS, mTrueFalseButtonEnabled);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void checkAnswer(boolean userPressedTrue) {
        int messageResId;

        if (mCheaterRecord[mCurrentIndex]) {
            messageResId = R.string.judgement_toast;
        }
        else {
            if (userPressedTrue && mQuestionBank[mCurrentIndex].isAnswerTrue()) {
                messageResId = R.string.correct_toast;
                mScore++;
            } else if (!userPressedTrue && !mQuestionBank[mCurrentIndex].isAnswerTrue()) {
                messageResId = R.string.correct_toast;
                mScore++;
            } else
                messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();

        if (mAnswered == mQuestionBank.length) {
            double percentage = ((double)mScore/(double)mAnswered) *100;
            Toast.makeText(QuizActivity.this, getResources().getString(R.string.score, percentage),Toast.LENGTH_LONG).show();
        }
    }

}
