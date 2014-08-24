package org.sdhanbit.mobile.android.json;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.os.Bundle;

public class FeedAPI {

	public static String BASE_URL = "http://www.sdhanbit.org/wordpress/index.php/wp_api/";
	
    protected static String SERMON_GET_PATH = BASE_URL+"v1/posts";
    
    public String feed(Bundle parameters, String httpMethod) throws FileNotFoundException, MalformedURLException, IOException 
    {

    	String url = SERMON_GET_PATH;
    	return Util.openUrl(url, httpMethod, parameters);
    }
}
