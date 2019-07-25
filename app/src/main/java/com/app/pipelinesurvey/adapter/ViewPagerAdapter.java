package com.app.pipelinesurvey.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Kevin on 2018-05-15.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<ImageView> m_views;
    public ViewPagerAdapter(List<ImageView> views) {
        this.m_views = views;
    }
    @Override
    public int getCount() {
        return m_views == null ? 0 : m_views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(m_views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(m_views.get(position));
        return m_views.get(position);
    }
}
