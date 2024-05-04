public class Constraint {
    public static final String AlwaysClosed = "0000";

    public static final String XOpenAndYOpen = "1000"; // ~a -> a, ~b -> b
    public static final String XOpenAndYClosed = "0100"; // ~a -> a, b -> ~b
    public static final String XClosedAndYOpen = "0010"; // a -> ~a, ~b -> b
    public static final String XClosedAndYClosed = "0001"; // a -> ~a, b -> ~b

    public static final String XOpen = "1100"; // a -> a
    public static final String XClosed = "0011"; // ~a -> ~a
    public static final String YOpen = "1010"; // b -> b
    public static final String YClosed = "0101"; // ~b -> ~b

    public static final String XOpenOrYOpen = "1110"; // ~a -> b, ~b -> a
    public static final String XOpenOrYClosed = "1101"; // ~a ->~b, b -> a
    public static final String XClosedOrYOpen = "1011";  // ~b -> ~a, a -> b
    public static final String XClosedOrYClosed = "0111"; // a -> ~b, b -> ~a

    public static final String Different = "0110"; // ~a -> b, ~b -> a
    public static final String Equal = "1001";
    public static final String AlwaysOpen = "1111";
}
