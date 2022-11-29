//package com.crio.xurl;
package com.crio.shorturl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class XUrlImpl implements XUrl{

    public String longUrl , shortUrl;

    public XUrlImpl(String orgUrl){
        this.longUrl = orgUrl;
    }

    public XUrlImpl(String longU,String shortU){
        this.longUrl = longU;
        this.shortUrl = shortU;
    }

    HashMap<String,String> urlHashMap = new HashMap<String,String>();
    Map<String, String> inverseMap = new HashMap<>();
    Map<String, Integer> longUrlHitCount = new HashMap<>();


    //In this method we will firstly generate a short url for corresponding long url and then save the mapping into our 
    //hashmap 
    private final String generateShortUrl(){
        String alphaNumericpool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder("");
        
        for(int i=0; i<9; i++){
            int index = random.nextInt(alphaNumericpool.length()-1);
            char c = alphaNumericpool.charAt(index);
            sb.append(c);
        }
        String shortUrlgenerated = "http://short.url/" + sb.toString();
        return shortUrlgenerated;
    }


    @Override
    public String registerNewUrl(String longUrl){

        if(urlHashMap.containsKey(longUrl)){
            return urlHashMap.get(longUrl);
        }

        else{
            if(!(urlHashMap.containsKey(longUrl))){
                String shortUrl = generateShortUrl();
                urlHashMap.put(longUrl, shortUrl);
                inverseMap.put(shortUrl, longUrl);
            }
        }

        return urlHashMap.get(longUrl);
    }


    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        if(urlHashMap.containsValue(shortUrl))
            return null;

        else
            urlHashMap.put(longUrl, shortUrl);
            inverseMap.put(shortUrl, longUrl);

        return urlHashMap.get(longUrl);
    }

    @Override
    public String getUrl(String shortUrl) {
        //urlHashMap.forEach((key, value) -> inverseMap.put(value, key));

        String str = inverseMap.get(shortUrl);
        longUrlHitCount.put(str, longUrlHitCount.getOrDefault(str,0)+1);

        if(!(inverseMap.containsKey(shortUrl)))
            return null;

        else{
            return inverseMap.get(shortUrl);
        }
    }


    @Override
    public Integer getHitCount(String longUrl) {

        if(longUrlHitCount.containsKey(longUrl))
            return longUrlHitCount.get(longUrl);

        else
            return 0;
    }


    @Override
    public String delete(String longUrl) {
        inverseMap.remove(urlHashMap.get(longUrl));
        urlHashMap.remove(longUrl);
        return shortUrl;
    }
}
