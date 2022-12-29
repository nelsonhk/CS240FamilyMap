package Handler;

import java.io.*;

/**
 * All handlers extend this BaseHandler class. The base class allows them to call common methods.
 */
public class BaseHandler {

    /*
		The readString method shows how to read a String from an InputStream.
	*/
    protected String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }


    protected void writeString (String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
