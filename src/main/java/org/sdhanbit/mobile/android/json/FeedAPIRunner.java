package org.sdhanbit.mobile.android.json;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.os.Bundle;

public class FeedAPIRunner {

	FeedAPI fa;
	
    public FeedAPIRunner(FeedAPI fa) {
    	this.fa = fa;
    }
    
    class WatchDog extends Thread
    {
    	RequestListener listener;
    	public boolean timedOut;
    	public WatchDog(RequestListener listener_p)
    	{
    		listener = listener_p;
    		timedOut = false;
    	}
    	public void run()
		{
			try
			{ 
				Thread.sleep(160000);
				listener.onComplete("timedout");
				timedOut = true;
			}catch(InterruptedException e)
			{
			}
		}
    }
    public void feed(final Bundle parameters, final String httpMethod, final RequestListener listener) {
        new Thread() {
            @Override public void run() {
                try {              	
                	WatchDog watchdog = new WatchDog(listener);
                	watchdog.start();
                	String resp = fa.feed(parameters, httpMethod);
                	if(!watchdog.timedOut)
                	{
                		watchdog.interrupt();
                		listener.onComplete(resp);
                	}
                } catch (FileNotFoundException e) {
                    listener.onComplete("exception");
                } catch (MalformedURLException e) {
                	listener.onComplete("exception");
                } catch (IOException e) {
                	listener.onComplete("exception");
                }
            }
        }.start();
    }

    public static interface RequestListener {

        public void onComplete(String response);

        public void onIOException(IOException e);
        
        public void onFileNotFoundException(FileNotFoundException e);
        
        public void onMalformedURLException(MalformedURLException e);
        
        public void onFacebookError(FeedAPIError e);
        
    }
    
}
