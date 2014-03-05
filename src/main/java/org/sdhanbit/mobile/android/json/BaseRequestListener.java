package org.sdhanbit.mobile.android.json;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import org.sdhanbit.mobile.android.json.FeedAPIRunner.RequestListener;

public abstract class BaseRequestListener implements RequestListener {

    public void onFacebookError(FeedAPIError e) {
        e.printStackTrace();
    }

    public void onFileNotFoundException(FileNotFoundException e) {
        e.printStackTrace();
    }

    public void onIOException(IOException e) {
        e.printStackTrace();
    }

    public void onMalformedURLException(MalformedURLException e) {
        e.printStackTrace();
    }
    
}
