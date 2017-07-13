package com.bignerdranch.android.geoquiz;

/**
 * Created by xzp on 2017-07-09 21:47.
 */

class Question {

    private final int mTextResId;
    private final boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

}
