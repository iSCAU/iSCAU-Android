package cn.scau.scautreasure.impl;

/**
 * Created by stcdasqy on 14-3-13.

public class MyStringHttpMessageConverter extends GsonHttpMessageConverter {
    public MyStringHttpMessageConverter(Charset defaultCharset) {
        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(new MediaType("text", "plain", defaultCharset));
        mediaTypeList.add(new MediaType("text", "html", defaultCharset));
        mediaTypeList.add(MediaType.ALL);
        super.setSupportedMediaTypes(mediaTypeList);
    }
    public MyStringHttpMessageConverter(){
        this(Charset.forName("utf-8"));
    }

}
 */