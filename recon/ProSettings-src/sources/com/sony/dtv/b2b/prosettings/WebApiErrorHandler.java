package com.sony.dtv.b2b.prosettings;

import java.util.Arrays;
import java.util.Collection;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
public class WebApiErrorHandler {
    private final String TAG = getClass().getSimpleName();
    public static JSONArray ERROR_UNAUTHORIZED = new JSONArray((Collection) Arrays.asList(401, "Unauthorized"));
    public static JSONArray ERROR_FORBIDDEN = new JSONArray((Collection) Arrays.asList(403, "Forbidden"));
    public static JSONArray ERROR_NOT_FOUND = new JSONArray((Collection) Arrays.asList(404, "Not Found"));
    public static JSONArray ERROR_REQUEST_ENTITY_TOO_LARGE = new JSONArray((Collection) Arrays.asList(413, "Request Entity Too Large"));
    public static JSONArray ERROR_REQUEST_URI_TOO_LONG = new JSONArray((Collection) Arrays.asList(414, "Request-URI Too Long"));
    public static JSONArray ERROR_NOT_IMPLEMENTED = new JSONArray((Collection) Arrays.asList(501, "Not Implemented"));
    public static JSONArray ERROR_SERVICE_UNAVAILABLE = new JSONArray((Collection) Arrays.asList(503, "Service Unavailable"));
    public static JSONArray ERROR_ANY = new JSONArray((Collection) Arrays.asList(1, "Any"));
    public static JSONArray ERROR_TIMEOUT = new JSONArray((Collection) Arrays.asList(2, "Timeout"));
    public static JSONArray ERROR_ILLEGAL_ARGUMENT = new JSONArray((Collection) Arrays.asList(3, "Illegal Argument"));
    public static JSONArray ERROR_ILLEGAL_JSON = new JSONArray((Collection) Arrays.asList(5, "Illegal JSON"));
    public static JSONArray ERROR_ILLEGAL_STATE = new JSONArray((Collection) Arrays.asList(7, "Illegal State"));
    public static JSONArray ERROR_NO_SUCH_METHOD = new JSONArray((Collection) Arrays.asList(12, "No Such Method"));
    public static JSONArray ERROR_UNSUPPORTED_VERSION = new JSONArray((Collection) Arrays.asList(14, "Unsupported Version"));
    public static JSONArray ERROR_UNSUPPORTED_OPERATION = new JSONArray((Collection) Arrays.asList(15, "Unsupported Operation"));
    public static JSONArray ERROR_REQUEST_RETRY = new JSONArray((Collection) Arrays.asList(40000, "Request Retry"));
    public static JSONArray ERROR_CLIENT_OVER_MAXIMUM = new JSONArray((Collection) Arrays.asList(40001, "Clients Over Maximum"));
    public static JSONArray ERROR_ENCRYPTION_FAILED = new JSONArray((Collection) Arrays.asList(40002, "Encryption Failed"));
    public static JSONArray ERROR_REQUEST_DUPLICATED = new JSONArray((Collection) Arrays.asList(40003, "Request Duplicated"));
    public static JSONArray ERROR_MULTIPLE_SETTINGS_FAILED = new JSONArray((Collection) Arrays.asList(40004, "Multiple Settings Failed"));
    public static JSONArray ERROR_DISPLAY_IS_TURNED_OFF = new JSONArray((Collection) Arrays.asList(40005, "Display is turned off"));
    public static JSONArray ERROR_PLEASE_CONTACT_INQUIRY_COUNTER = new JSONArray((Collection) Arrays.asList(40006, "Exxxx"));
    public static JSONArray ERROR_AVAILABLE_POST_ONLY = new JSONArray((Collection) Arrays.asList(43000, "Available POST only"));
    public static JSONArray ERROR_IO_EXCEPTION = new JSONArray((Collection) Arrays.asList(43001, "IO Exception"));
    public static JSONArray ERROR_RESPONSE_EXCEPTION = new JSONArray((Collection) Arrays.asList(43002, "Response Exception"));
    public static JSONArray ERROR_JSON_EXCEPTION = new JSONArray((Collection) Arrays.asList(43003, "JSON Exception"));
    public static JSONArray ERROR_INTERNAL_ERROR = new JSONArray((Collection) Arrays.asList(43004, "Internal Error"));
    public static JSONArray ERROR_INTERRUPTED_EXCEPTION = new JSONArray((Collection) Arrays.asList(43004, "Interrupted Exception"));
}
