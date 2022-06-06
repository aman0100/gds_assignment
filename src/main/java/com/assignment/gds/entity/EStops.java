package com.assignment.gds.entity;

public enum EStops {
    STOP_1(1),
    STOP_2(2),
    STOP_3(3),
    STOP_4(4),
    STOP_5(5),
    STOP_6(6),
    STOP_7(7),
    STOP_8(8),
    STOP_9(9),
    STOP_10(10),
    STOP_11(11),
    STOP_12(12),
    STOP_13(13),
    STOP_14(14),
    STOP_15(15);

    EStops(Integer stopNumber) {
        this.stopNumber = stopNumber;
    }

    private final Integer stopNumber;

    public Integer getStopNumber() {
        return this.stopNumber;
    }
}
