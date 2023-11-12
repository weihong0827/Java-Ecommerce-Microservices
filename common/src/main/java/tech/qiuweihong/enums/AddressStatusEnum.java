package tech.qiuweihong.enums;

public enum AddressStatusEnum {
    DEFAULT_STATUS(1),
    COMMON_STATUS(0);
    private final int status;

    public int getStatus() {
        return status;
    }

    AddressStatusEnum(int status){
        this.status = status;
    }
}
