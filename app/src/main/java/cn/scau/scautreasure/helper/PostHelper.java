package cn.scau.scautreasure.helper;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.StatusModel;

//import cn.scau.scautreasure.impl.MyStringHttpMessageConverter;

/**
 * Created by stcdasqy on 14-3-14.
 */

public class PostHelper {
    public interface PostCallBack{
        void onSuccess();
        void onFail(int extraMsgId);
    }
    public static void PostData(String URL, MultiValueMap params,PostCallBack callback){
        RestTemplate restTemplate = new RestTemplate();

        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        HttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        restTemplate.getMessageConverters().add(formHttpMessageConverter);
        restTemplate.getMessageConverters().add(stringHttpMessageConverter);
        restTemplate.getMessageConverters().add(gsonHttpMessageConverter);

        StatusModel result = null;
        String extraMsg = null;
        boolean bSuccess = true;
        try {
            result = restTemplate.postForObject(URL, params, StatusModel.class);
        }catch(RestClientException e){
            e.printStackTrace();
            bSuccess = false;
        }
        //Log.e("result",result.getResult());
        if (bSuccess && result != null && (
                (result.getResult() != null && result.getResult().equals("success"))
                        || ((extraMsg = result.getStatus()) != null && extraMsg.equals("success"))
        )) {
            callback.onSuccess();
        } else {
            int extraMsgId = R.string.comments_fail;
            if(extraMsg != null){
                if(extraMsg.equals("repeat operate"))
                    extraMsgId = R.string.comments_repeat_operate;
            }
            callback.onFail(extraMsgId);
        }
    }
}
