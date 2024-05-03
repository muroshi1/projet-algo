public class Constraint {
    public static final int AlwaysClosed = 0b0000;

    public static final int XOpenAndYOpen = 0b1000;
    public static final int XOpenAndYClosed = 0b0100;
    public static final int XClosedAndYOpen = 0b0010;
    public static final int XClosedAndYClosed = 0b0001;

    public static final int XOpen = 0b1100;
    public static final int XClosed = 0b0011;
    public static final int YOpen = 0b1010;
    public static final int YClosed = 0b0101;

    public static final int XOpenOrYOpen = 0b1110;
    public static final int XOpenOrYClosed = 0b1101;
    public static final int XClosedOrYOpen = 0b1011;
    public static final int XClosedOrYClosed = 0b0111;

    public static final int Different = 0b0110;
    public static final int Equal = 0b1001;
    public static final int AlwaysOpen = 0b1111;
}
