public class StringPadder {

    private static String padString(String s, int length, boolean right) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(' ');
        }
        String padding = sb.substring(0, length - s.length());

        if (right) { return s + padding; }
        else { return padding + s; }
    }

    public static String padStringRight(String s, int length) {
        return padString(s, length, true);
    }

    public static String padStringLeft(String s, int length) {
        return padString(s, length, false);
    }
}
