package org.dylangraham.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "org.dylangraham.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "org.dylangraham.geoquiz.answer_shown";
    private static final String KEY_CHEATED = "cheated";
    private TextView answerTextView;
    private Button showAnswer;
    private boolean answerShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(KEY_CHEATED)) {
                setAnswerShownResult(true);
            }
        }

        setContentView(R.layout.activity_cheat);

        final boolean answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        answerTextView = (TextView) findViewById(R.id.answer_text_view);
        showAnswer = (Button) findViewById(R.id.show_answer_button);

        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerIsTrue) {
                    answerTextView.setText(R.string.true_button);
                } else {
                    answerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = showAnswer.getWidth() / 2;
                    int cy = showAnswer.getHeight() / 2;
                    float radius = showAnswer.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(showAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            answerTextView.setVisibility(View.VISIBLE);
                            showAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    answerTextView.setVisibility(View.VISIBLE);
                    showAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CHEATED, answerShown);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        answerShown = true;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}