package tech.qiuweihong.enums;

public enum AddressStatusEnum {
    DEFAULT_STATUS(1),
    COMMON_STATUS(0);
    private int status;

    public int getStatus() {
        return status;
    }

    private AddressStatusEnum(int status){
        this.status = status;
    }
}
