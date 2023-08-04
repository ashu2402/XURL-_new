//package com.crio.xurl;
package com.crio.shorturl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Iterator;
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


    Map<String,LocalDate> url_hit_time_stamp = new HashMap<>();


    //this function could be run after every 24hrs or depending as per requirement.
    private void delete_expired_urls(){
        LocalDate current_time_stamp = LocalDate.now();

        Iterator<Map.Entry<String, LocalDate>>iterator = url_hit_time_stamp.entrySet().iterator();

        //url_hit_time_stamp.entrySet().removeIf(entry->ChronoUnit.MONTHS.between(entry.getValue(), current_time_stamp)>=1);

        // Iterate over the HashMap
        while (iterator.hasNext()) {
            // Get the entry at this iteration
            Map.Entry<String, LocalDate> entry = iterator.next();
            // Check if this key is the required key
            //Duration of expiry here is set to 1 month or above
            if(ChronoUnit.MONTHS.between(entry.getValue(), current_time_stamp)>=1)
            {
                delete(entry.getKey());
                // Remove this entry from HashMap
                iterator.remove();
            }
        }

    }




    //In this method we will firstly generate a short url for corresponding long url and then save the mapping into our 
    //hashmap ...here we are returning the short url which is generated.
    private final String generateShortUrl(){
        String alphaNumericpool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder("");
        
        for(int i=0; i<9; i++){
            int index = random.nextInt(alphaNumericpool.length()-1);
            sb.append(alphaNumericpool.charAt(index));
        }
        return "http://short.url/"+sb.toString();
    }


    @Override
    public String registerNewUrl(String longUrl){

        if(urlHashMap.containsKey(longUrl)){
            return urlHashMap.get(longUrl);
        }

        else{       //longurl entry not present in urlhashmap
                String shortUrl = generateShortUrl();
                urlHashMap.put(longUrl, shortUrl);
                inverseMap.put(shortUrl, longUrl);
        }

        //try updating the time_stamp as there was a lookup to register a new url
        url_hit_time_stamp.put(longUrl, LocalDate.now());


        return urlHashMap.get(longUrl);
    }



    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        if(urlHashMap.containsValue(shortUrl))
            return null;

        else{
            urlHashMap.put(longUrl, shortUrl);
            inverseMap.put(shortUrl, longUrl);

            //try updating the time_stamp as there was a lookup to register a new url
            url_hit_time_stamp.put(longUrl, LocalDate.now());
        }

        return urlHashMap.get(longUrl);
    }



    @Override
    public String getUrl(String shortUrl) {
        //urlHashMap.forEach((key, value) -> inverseMap.put(value, key));

        String str = inverseMap.get(shortUrl);
        longUrlHitCount.put(str, longUrlHitCount.getOrDefault(str,0)+1);

        //try updating the time_stamp as there was a lookup to register a new url
        url_hit_time_stamp.put(str, LocalDate.now());


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
