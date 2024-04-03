package com.polaris.papiclientsdk.common.execption;

/**
 * @Author polaris
 * @Create 2024-04-02 11:10
 * @Version 1.0
 * ClassName papiClientSDKExecption
 * Package com.polaris.papiclientsdk.common.execption
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
    private String errorCode;

    public PapiClientSDKException (String message, Throwable cause) {
        super(message, cause);
    }

    public PapiClientSDKException (String message) {
        this(message, "");
    }

    public PapiClientSDKException (String message, String requestId) {
        this(message, requestId, "");
    }

    public PapiClientSDKException (String message, String requestId, String errorCode) {
        super(message);
        this.requestId = requestId;
        this.errorCode = errorCode;
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
    public String getErrorCode() {
        return errorCode;
    }

    public String toString() {
        String msg = "[TencentCloudSDKException]"
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
