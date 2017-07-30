package rhabib8.kaunbanegakrorepati;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "rhabib8.android.kaunbanegakrorepati.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "rhabib8.android.kaunbanegakrorepati.answer_shown";
    private boolean mAnswerIsTrue;
    private Button mShowAnswerButton;
    private TextView mAnswerTextView;
    private static final String KEY_CHEATER = "cheater";
    private static boolean cheater = false;
    private TextView mApiLevel;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //Cheater used for rotations/activity destructions
        if (savedInstanceState != null)
            cheater = savedInstanceState.getBoolean(KEY_CHEATER);

        if (cheater)
            setAnswerShownResult(true);


        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);

        //SHOW ANSWER BUTTON
        mShowAnswerButton = (Button)findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               if (mAnswerIsTrue)
                    mAnswerTextView.setText(R.string.true_button);
               else
                   mAnswerTextView.setText(R.string.false_button);
               setAnswerShownResult(true);
               cheater = true;

               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   int cx = mShowAnswerButton.getWidth() / 2;
                   int cy = mShowAnswerButton.getHeight() / 2;
                   float radius = mShowAnswerButton.getWidth();
                   Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                   anim.addListener(new AnimatorListenerAdapter() {
                       @Override
                       public void onAnimationEnd(Animator animation) {
                           super.onAnimationEnd(animation);
                           mShowAnswerButton.setVisibility(View.INVISIBLE);
                       }
                   });
                   anim.start();
               }
               else {
                   mShowAnswerButton.setVisibility(View.INVISIBLE);
               }
           }
        });
        mApiLevel = (TextView) findViewById(R.id.api_level);
        mApiLevel.setText("API Level " + Build.VERSION.SDK_INT);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_CHEATER, cheater);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK,data);
    }



}
