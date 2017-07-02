package pl.sggw.stosprzepelniony.viper.ad_details;


import com.google.android.flexbox.FlexboxLayout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mateuszkoslacz.moviper.base.view.activity.autoinject.passive.butterknife.ViperButterKnifePassiveActivity;
import com.mateuszkoslacz.moviper.iface.presenter.ViperPresenter;

import pl.sggw.stosprzepelniony.R;
import pl.sggw.stosprzepelniony.data.entity.Ad;
import pl.sggw.stosprzepelniony.exception.BaseException;
import pl.sggw.stosprzepelniony.util.constant.Irrelevant;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class AdDetailsActivity
        extends ViperButterKnifePassiveActivity
        <AdDetailsContract.View>
        implements AdDetailsContract.View {

    @BindView(R.id.loading_view)
    ProgressBar loadingView;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    @BindView(R.id.ad_view)
    LinearLayout adView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.categories_container)
    FlexboxLayout categoriesContainer;

    private PublishSubject<Object> backButtonClicks = PublishSubject.create();
    private static final double ZERO = 0.0;

    public static void start(Context context, int adId) {
        Intent starter = new Intent(context, AdDetailsActivity.class);
        starter.putExtra(AD_ID_EXTRA, adId);
        context.startActivity(starter);
    }

    @Override
    public Observable<Object> getBackButtonClicks() {
        return backButtonClicks;
    }

    @Override
    public void showContent(Ad ad) {
        title.setText(ad.getTitle());
        date.setText(ad.getDate().toString());
        cost.setText(createProperCostInformationForAd(ad));
        content.setText(ad.getContent());
        loadingView.setVisibility(View.GONE);
        adView.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable throwable) {
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        if (throwable instanceof BaseException) {
            Toasty.error(this, ((BaseException) throwable).getUserMessage(this), Toast.LENGTH_LONG, true).show();
        } else {
            Toasty.error(this, getString(R.string.error_default), Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    protected void injectViews() {
        super.injectViews();
        setUpToolbar();
    }

    private void setUpToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Advertisement");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backButtonClicks.onNext(Irrelevant.EVENT);
        }
        return super.onOptionsItemSelected(item);
    }

    private String createProperCostInformationForAd(Ad ad) {
        if (ad.getTotalCost() != 0 && ad.getTotalCost() != 1) {
            return String.format("%d$ - total cost", ((int) ad.getTotalCost()));
        } else if (ad.getHourCost() != 0 && ad.getHourCost() != 1) {
            return String.format("%d$/per hour", ((int) ad.getHourCost()));
        } else {
            return "Missing cost information";
        }
    }

    @NonNull
    @Override
    public ViperPresenter<AdDetailsContract.View> createPresenter() {
        return new AdDetailsPresenter(getArgs());
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ad_details;
    }
}
