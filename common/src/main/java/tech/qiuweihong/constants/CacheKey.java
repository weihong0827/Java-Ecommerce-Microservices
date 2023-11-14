package tech.qiuweihong.constants;

public class CacheKey {
    /***
     * Verification code
     * first is code type
     * second is recipient address
     */
    public static final String CacheCodeKey = "code:%s:%s";
    /***
     * cart hash
     * first is user unique identifier
     */
    public static final String CartCodeKey = "code:%s";

}
