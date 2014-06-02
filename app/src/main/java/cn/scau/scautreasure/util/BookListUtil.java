package cn.scau.scautreasure.util;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.ui.CommonActivity;

public class BookListUtil extends CommonActivity {

    public void setCacheKey(String cacheKey){
        this.cacheKey = app.userName + "_" + cacheKey;
    }

    public String getCacheKey(){
         return cacheKey;
    }

    public ActionBarActivity getSherlockActivity(){
         return this;
    }
}
