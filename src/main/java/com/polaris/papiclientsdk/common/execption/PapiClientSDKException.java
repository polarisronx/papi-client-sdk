package com.polaris.papiclientsdk.common.execption;

/**
 * @author polaris
 * @data 2024-04-02 11:10
 * @version 1.0
 * ClassName papiClientSDKExecption
 * Package com.polaris.papiclientsdk.utils.execption
 * Description
 */
public class PapiClientSDKException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * UUID of the request, it will be empty if request is not fulfilled.
     */
    private String requestId;

    /**
     * Error code, When API returns a failure, it must have an error code.
     */
    private int errorCode;

    public PapiClientSDKException (String message, Throwable cause) {
        super(message, cause);
    }


    public PapiClientSDKException(int code, String message) {
        super(message);
        this.errorCode = code;
    }

    public PapiClientSDKException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }

    public PapiClientSDKException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode.getCode();
    }



    public PapiClientSDKException (String message, String requestId, ErrorCode errorCode) {
        super(message);
        this.requestId = requestId;
        this.errorCode = errorCode.getCode();
    }
    public PapiClientSDKException (String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode.getCode();
    }

    public String getRequestId() {
        return requestId;
    }

    /**
     * Get error code
     *
     * @return A string represents error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    public String toString() {
        String msg = "[PapiClientSDKException]"
                + "code: "
                + this.getErrorCode()
                + " message:"
                + this.getMessage()
                + " requestId:"
                + this.getRequestId();
        if (getCause() != null) {
            msg += " cause:" + getCause().toString();
        }
        return msg;
    }
}
