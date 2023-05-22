package com.crio.shorturl;
import java.util.Random;
import java.util.HashMap;

public class XUrlImpl implements XUrl
{
    
    HashMap<String,String> map=new HashMap<String,String>();
    HashMap<String,Integer> map1=new HashMap<String,Integer>();
    String createShortUrl(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomString = "http://short.url/";
        int length = 9;

        Random rand = new Random();
        char[] shortUrl = new char[length];

        for(int i = 0; i < length; i++){
            shortUrl[i] = characters.charAt(rand.nextInt(characters.length()));
        }

        for(int i = 0; i < shortUrl.length; i++){
            randomString += shortUrl[i];
        }
        
        return randomString;
    }
// If longUrl already has a corresponding shortUrl, return that shortUrl
  // If longUrl is new, create a new shortUrl for the longUrl and return it

    public String registerNewUrl(String longUrl)
    {
        for(String url1:map.keySet())
        {       
             if(url1.equals(longUrl))
            {
               return  map.get(url1);
            }
           }
      
        String str=createShortUrl();
         map.put(longUrl,str);
      
       return str;
    }

    // If shortUrl is already present, return null
    // Else, register the specified shortUrl for the given longUrl
    // Note: You don't need to validate if longUrl is already present, 
    //       assume it is always new i.e. it hasn't been seen before 
   public String registerNewUrl(String longUrl, String shortUrl)
    {
        for(String str:map.values())
        {
            if(shortUrl.equals(str))
            {
                return null;
            }
        }
       
        map.put(longUrl, shortUrl);
        return shortUrl;
    }
  
    // If shortUrl doesn't have a corresponding longUrl, return null
    // Else, return the corresponding longUrl
  public  String getUrl(String shortUrl)
    {
        for(String url:map.keySet())
        {
    if(shortUrl.equals(map.get(url)))
    {
        map1.put(url,map1.getOrDefault(url, 0) + 1);
        return url;
    }
        }
        return null;
    }
  
    // Return the number of times the longUrl has been looked up using getUrl()
   public Integer getHitCount(String longUrl)
    {
      if(map1.containsKey(longUrl))
      {
        return map1.get(longUrl);
      }
      return 0;
    }
  
    // Delete the mapping between this longUrl and its corresponding shortUrl
    // Do not zero the Hit Count for this longUrl
 public   String delete(String longUrl)
    {
   map.remove(longUrl);
   return longUrl;
    }
}