package com.example.awalkthrough;

import android.graphics.Typeface;
import android.util.Log;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.awalkthrough.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

public class AWalkthroughAdapter extends FragmentStatePagerAdapter implements ShadowTransformer.CardAdapter {

    private String TAG = AWalkthroughAdapter.class.getSimpleName();
    List<AWalkthroughCard> pages = new ArrayList<AWalkthroughCard>();
    private List<FancyWalkthroughFragment> mFragments = new ArrayList<>();
    private float mBaseElevation;
    private Typeface typeface;

    public AWalkthroughAdapter(List<AWalkthroughCard> pages, FragmentManager fm, float baseElevation, Typeface typeface) {
        super(fm);
        this.pages = pages;
        this.typeface = typeface;
        this.mBaseElevation = baseElevation;

        for (int i = 0; i < pages.size(); i++) {
            addCardFragment(pages.get(i));
        }

        //setTypeface(typeface);

    }

    @Override
    public Fragment getItem(int position) {
        //return FancyWalkthroughFragment.newInstance(pages.get(position));
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (FancyWalkthroughFragment) fragment);
        return fragment;
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        setTypeface(typeface, position);
        return mFragments.get(position).getCardView();
    }

    public void addCardFragment(AWalkthroughCard page) {
        mFragments.add(FancyWalkthroughFragment.newInstance(page));
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        super.destroyItem(container, position, object);

    }

    public void setTypeface(Typeface typeface, int i) {
        if (typeface != null) {

            if (mFragments.get(i) == null) {
                Log.i(TAG, "Fragment is null");
                return;
            }

            if (mFragments.get(i).getTitleView() == null) {
                Log.i(TAG, "TitleView is null");
                return;
            }

            if (mFragments.get(i).getTitleView() == null) {
                Log.i(TAG, "DescriptionView is null");
                return;
            }

            mFragments.get(i).getTitleView().setTypeface(typeface);
            mFragments.get(i).getDescriptionView().setTypeface(typeface);

        }
    }

}
