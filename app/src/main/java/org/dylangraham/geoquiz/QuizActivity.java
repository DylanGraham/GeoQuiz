package org.dylangraham.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = QuizActivity.class.getCanonicalName();
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATED = "cheated";
    private static final int REQUEST_CODE_CHEAT = 0;

    @Bind(R.id.question_text_view)
    TextView questionTextView;
    @Bind(R.id.next_button)
    Button nextButton;
    @Bind(R.id.cheat_button)
    Button cheatButton;
    @Bind(R.id.prev_button)
    Button prevButton;
    @Bind(R.id.true_button)
    Button trueButton;
    @Bind(R.id.false_button)
    Button falseButton;

    private int currentIndex = 0;
    private boolean isCheater;

    private Question[] questionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, true),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX);
            isCheater = savedInstanceState.getBoolean(KEY_CHEATED);
        }

        nextQuestion();

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkAnswer(true); }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { checkAnswer(false); }
        });
    }

    @OnClick(R.id.question_text_view)
    public void clickQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.length;
        nextQuestion();
    }

    @OnClick(R.id.next_button)
    public void clickNext() {
        currentIndex = (currentIndex + 1) % questionBank.length;
        isCheater = false;
        nextQuestion();
    }

    @OnClick(R.id.cheat_button)
    public void clickCheat() {
        boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();
        Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
        startActivityForResult(i, REQUEST_CODE_CHEAT);
    }

    @OnClick(R.id.prev_button)
    public void clickPrev() {
        currentIndex = (currentIndex - 1 + questionBank.length) % questionBank.length;
        nextQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) return;
            isCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, currentIndex);
        if (isCheater) {
            outState.putBoolean(KEY_CHEATED, true);
        }
    }

    private void nextQuestion() {
        int question = questionBank[currentIndex].getTextResID();
        questionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        int messageResID;

        if (isCheater) {
            messageResID = R.string.judgment_toast;
        } else {
            if (userPressedTrue && questionBank[currentIndex].isAnswerTrue()
                    || !userPressedTrue && !questionBank[currentIndex].isAnswerTrue()) {
                messageResID = R.string.correct_toast;
            } else {
                messageResID = R.string.incorrect_toast;
            }
        }

        Toast.makeText(QuizActivity.this, messageResID, Toast.LENGTH_SHORT).show();
    }
}
