package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by xzp on 2017-07-17.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private final List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes  = new ArrayList<>();
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {

        for (int i = 0; i < mCrimes.size(); i++) {
            Crime crime = mCrimes.get(i);
            if(crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }

    public void deleteCrime(UUID id) {

        for (int i = mCrimes.size() - 1; i >= 0; i--) {
            Crime crime = mCrimes.get(i);
            if(crime.getId().equals(id)) {
                mCrimes.remove(i);
                break;
            }
        }
    }

}
