public class Encrypt {

    public static void encrypt () {

        String msg = "abc";
        int shift = 3;

        String s = "";
        int len = msg.length();
        for(int x = 0; x < len; x++){
            char c = (char)(msg.charAt(x) + shift);
            if (c > 'z')
                s += (char)(msg.charAt(x) - (26-shift));
            else
                s += (char)(msg.charAt(x) + shift);
        }

        System.out.println(s);
    }


    }

