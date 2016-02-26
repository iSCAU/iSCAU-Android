package cn.scau.scautreasure.helper;

/**
 * Created by zzb on 2016-2-27.
 */
import java.util.Random;

import cn.scau.scautreasure.R;
public class ColorHelper {

    public static int[] colors = new int[] { R.drawable.colorone,
            R.drawable.colortwo, R.drawable.colorthree, R.drawable.colorfour,
            R.drawable.colorfive };





    public static int  randomColor(){
        Random random=new Random();
        return colors[random.nextInt(4)];
    }


}