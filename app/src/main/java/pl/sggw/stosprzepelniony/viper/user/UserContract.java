package pl.sggw.stosprzepelniony.viper.user;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.mateuszkoslacz.moviper.iface.interactor.ViperRxInteractor;
import com.mateuszkoslacz.moviper.iface.routing.ViperRxRouting;

import java.util.List;

import io.reactivex.Observable;
import pl.sggw.stosprzepelniony.data.entity.Ad;
import pl.sggw.stosprzepelniony.data.entity.User;

interface UserContract {


    interface View extends MvpView {
        void showEmptyState();
        void showLoading();
        void showContent(List<Ad> userAds);
        void showError(Throwable throwable);
        void showUserInfo(User user);
        Observable<Ad> getListItemClicks();
    }

    interface Interactor extends ViperRxInteractor {
        Observable<User> getUserInfoById(int userdId);
        Observable<List<Ad>> getAdsByUserId(int userId);
    }

    interface Routing extends ViperRxRouting<Activity> {
        void startAdDetailsActivity(Ad ad);
    }
}
