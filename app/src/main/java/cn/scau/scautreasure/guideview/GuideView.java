package cn.scau.scautreasure.guideview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;

@EActivity
public class GuideView extends Activity {
    @App
    AppContext app;
    private JazzyViewPager mJazzy;
    private int drawables[] = {R.drawable.guideview1, R.drawable.guidview2, R.drawable.guidview3, R.drawable.guidview4};

    //String imageUri = "drawable://" + R.drawable.image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_view);
        setupJazziness(JazzyViewPager.TransitionEffect.Standard);
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add("Toggle Fade");
//		String[] effects = this.getResources().getStringArray(R.array.jazzy_effects);
//		for (String effect : effects)
//			menu.add(effect);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getTitle().toString().equals("Toggle Fade")) {
//			mJazzy.setFadeEnabled(!mJazzy.getFadeEnabled());
//		} else {
//			JazzyViewPager.TransitionEffect effect = JazzyViewPager.TransitionEffect.valueOf(item.getTitle().toString());
//			setupJazziness(effect);
//		}
//		return true;
//	}

    private void setupJazziness(JazzyViewPager.TransitionEffect effect) {
        mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);
        mJazzy.setTransitionEffect(effect);
        mJazzy.setAdapter(new MainAdapter());
        mJazzy.setPageMargin(30);
    }

    private class MainAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView image = new ImageView(GuideView.this);
            image.setBackgroundColor(getResources().getColor(R.color.unvisistable));

            image.setPadding(30, 30, 30, 30);
//            image.setImageResource(guidImage[position]);
            AppContext.loadImage("drawable://" + drawables[position], image, null);
            if (position == 3) {
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        app.config.isFirstStartApp().put(false);
                        finish();
                    }
                });
            }
            container.addView(image, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mJazzy.setObjectForPosition(image, position);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(mJazzy.findViewFromObject(position));
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("引导退出");
    }
}
