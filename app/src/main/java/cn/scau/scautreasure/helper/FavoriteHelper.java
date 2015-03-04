package cn.scau.scautreasure.helper;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;

import cn.scau.scautreasure.model.FavoriteModel;

/**
 * Created by macroyep on 2/9/15.
 * Time:11:20
 */
@EBean
public class FavoriteHelper {

    @OrmLiteDao(helper = DatabaseHelper.class, model = FavoriteModel.class)
    RuntimeExceptionDao<FavoriteModel, Integer> dao;

    /**
     * 插入一条收藏*
     *
     * @param model
     *
     * @return int 当前影响行数
     */
    public int insertOneFavorite(FavoriteModel model) {
        return dao.create(model);
    }


    /**
     * 更新*
     *
     * @param model
     */
    public void updateOneText(FavoriteModel model) {
        dao.update(model);

    }


    /**
     * 删除一条*
     *
     * @param id
     *
     * @return
     */
    public int removeOneFavorite(int id) {
        return dao.deleteById(id);
    }


    public List<FavoriteModel> loadAll() {

        try {
            return dao.queryBuilder().orderBy("id", false).query();
        } catch (SQLException e) {

        }
        return null;
    }

    public List<FavoriteModel> searchOneFavorite(String text) {

        try {
            return dao.queryBuilder().where().like("title", "%" + text + "%").
                    or().like("subtitle", "%" + text + "%").
                    or().like("content", "%" + text + "%")
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


}
