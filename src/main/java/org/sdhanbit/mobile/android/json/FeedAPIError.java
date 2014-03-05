package org.sdhanbit.mobile.android.json;

public class FeedAPIError extends Throwable {
  
    private int mErrorCode = 0;
    private String mErrorType;
    
    public FeedAPIError(String message) {
        super(message);
    }

    public FeedAPIError(String message, String type, int code) {
        super(message);
        mErrorType = type;
        mErrorCode = code;
    }
    
    public int getErrorCode() {
        return mErrorCode;
    }
    
    public String getErrorType() {
        return mErrorType;
    }
}
