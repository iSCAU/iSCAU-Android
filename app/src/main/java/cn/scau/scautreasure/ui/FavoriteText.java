package cn.scau.scautreasure.ui;

import android.media.TimedText;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.FavoriteHelper;
import cn.scau.scautreasure.model.FavoriteModel;
import cn.scau.scautreasure.widget.AppToast;

/**
 * Created by macroyep on 2/9/15.
 * Time:17:22
 */
@EActivity(R.layout.favorite_text)
public class FavoriteText extends BaseActivity {
    @Bean
    FavoriteHelper favoriteHelper;

    @ViewById(R.id.title)
    EditText titleEdit;

    @ViewById(R.id.content)
    EditText contentEdit;

    @ViewById(R.id.time)
    TextView timeText;

    @Extra
    String newText = "";


    @Extra
    boolean edit = false;

    @Extra
    FavoriteModel model;

    @AfterViews
    void init() {
        setTitleText("文字");
        setMoreButtonText("保存");
        if (edit) {
            titleEdit.setText(model.getTitle());
            contentEdit.setText(model.getContent());
            timeText.setText(model.getDate());
        }
        if (!newText.equals("")) {
            contentEdit.setText(newText);
        }
    }

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        if (!titleEdit.getText().toString().trim().equals("")) {
            if (edit) {
                model.setContent(contentEdit.getText().toString().trim());
                model.setTitle(titleEdit.getText().toString().trim());
                favoriteHelper.updateOneText(model);
            } else {

                int result = favoriteHelper.insertOneFavorite(
                        new FavoriteModel(titleEdit.getText().toString().trim(), "",
                                "", contentEdit.getText().toString().trim(), FavoriteModel.TEXT, app.getStringDate()));
            }
            setResult(FavoriteActivity.RESULT_OK);
            finish();
        } else {
            AppToast.show(this, "请输入标题", 0);
        }

    }
}
