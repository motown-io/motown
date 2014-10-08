package io.motown.domain.utils;

/**
 * Keys of non-critical attributes that are received by Motown and may want to be preserved in a AttributeMap. For
 * example see usage of {@code io.motown.domain.api.chargingstation.StatusNotification}.
 */
public class AttributeMapKeys {

    public static final String STATUS_NOTIFICATION_ERROR_CODE_KEY = "errorCode";

    public static final String STATUS_NOTIFICATION_INFO_KEY = "info";

    public static final String STATUS_NOTIFICATION_VENDOR_ID_KEY = "vendorId";

    public static final String STATUS_NOTIFICATION_VENDOR_ERROR_CODE_KEY = "vendorErrorCode";

}
