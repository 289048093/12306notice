package lizhao.util;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author lizhao
 * @since 2014/11/9.
 */
public class MyX509TrustManager implements X509TrustManager {

    /*
     * The default X509TrustManager returned by SunX509.  We'll delegate
     * decisions to it, and fall back to the logic in this class if the
     * default X509TrustManager doesn't trust it.
     */
    X509TrustManager sunJSSEX509TrustManager;
    MyX509TrustManager() throws Exception {
        // create a "default" JSSE X509TrustManager.
        KeyStore ks = KeyStore.getInstance("JKS");
        String trustStore = "jre/lib/security/cacerts";
        String javaHome =System.getenv("JAVA_HOME");

        ks.load(new FileInputStream(javaHome+ File.separator+trustStore),
               "changeit".toCharArray());
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance("SunX509", "SunJSSE");
        tmf.init(ks);
        TrustManager tms [] = tmf.getTrustManagers();
        /*
         * Iterate over the returned trustmanagers, look
         * for an instance of X509TrustManager.  If found,
         * use that as our "default" trust manager.
         */
        for (TrustManager tm : tms) {
            if (tm instanceof X509TrustManager) {
                sunJSSEX509TrustManager = (X509TrustManager) tm;
                return;
            }
        }
        /*
         * Find some other way to initialize, or else we have to fail the
         * constructor.
         */
        throw new Exception("Couldn't initialize");
    }
    /*
     * Delegate to the default trust manager.
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
        } catch (CertificateException excep) {
            // do any special handling here, or rethrow exception.
            excep.printStackTrace();
        }
    }
    private static byte[] $12306 = {(byte)0x30,(byte)0x81,(byte)0x89,(byte)0x02,(byte)0x81,(byte)0x81,(byte)0x00,(byte)0xcc,(byte)0xa5,(byte)0xb3,(byte)0x5e,(byte)0x6f,(byte)0x7e,(byte)0x29,(byte)0xd0,(byte)0x6b,(byte)0xcb,(byte)0x91,(byte)0x9e,(byte)0xad,(byte)0xef,(byte)0x6f,(byte)0xce,(byte)0x39,(byte)0xb6,(byte)0xb8,(byte)0x99,(byte)0x7d,(byte)0x8a,(byte)0xfd,(byte)0xe6,(byte)0x51,(byte)0x58,(byte)0x57,(byte)0xe7,(byte)0xba,(byte)0x4f,(byte)0x1e,(byte)0xe6,(byte)0x32,(byte)0x83,(byte)0x1f,(byte)0xef,(byte)0x41,(byte)0x80,(byte)0x26,(byte)0xf6,(byte)0xf5,(byte)0xb4,(byte)0xa8,(byte)0xb6,(byte)0xa3,(byte)0xea,(byte)0x4d,(byte)0xbc,(byte)0x57,(byte)0x58,(byte)0xfc,(byte)0x48,(byte)0x3b,(byte)0x3f,(byte)0x04,(byte)0x8f,(byte)0x25,(byte)0x0e,(byte)0xdd,(byte)0xb7,(byte)0xd1,(byte)0xe6,(byte)0x94,(byte)0x3c,(byte)0xa2,(byte)0xf3,(byte)0x53,(byte)0x3a,(byte)0x59,(byte)0x2c,(byte)0xda,(byte)0xb4,(byte)0xb9,(byte)0x05,(byte)0xd3,(byte)0x21,(byte)0xa2,(byte)0x2e,(byte)0x2c,(byte)0x52,(byte)0x30,(byte)0x8a,(byte)0x8b,(byte)0x91,(byte)0x0b,(byte)0x8a,(byte)0xab,(byte)0x72,(byte)0xc3,(byte)0x0f,(byte)0xd7,(byte)0x81,(byte)0x8e,(byte)0x62,(byte)0x16,(byte)0xab,(byte)0xfa,(byte)0x0f,(byte)0x83,(byte)0xf0,(byte)0xf5,(byte)0x0b,(byte)0xce,(byte)0x51,(byte)0xc7,(byte)0x51,(byte)0x55,(byte)0x26,(byte)0xcf,(byte)0x5a,(byte)0x8d,(byte)0x43,(byte)0xc0,(byte)0xc9,(byte)0x87,(byte)0xef,(byte)0x92,(byte)0x2d,(byte)0xea,(byte)0x99,(byte)0x3f,(byte)0xac,(byte)0x04,(byte)0x48,(byte)0x14,(byte)0x71,(byte)0x23,(byte)0x5b,(byte)0x02,(byte)0x03,(byte)0x01,(byte)0x00,(byte)0x01 };
    /*
     * Delegate to the default trust manager.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException excep) {

            excep.printStackTrace();
            /*
             * Possibly pop up a dialog box asking whether to trust the
             * cert chain.
             */
        }
    }

    /*
     * Merely pass this through.
     */
    public X509Certificate[] getAcceptedIssuers() {
        return sunJSSEX509TrustManager.getAcceptedIssuers();
    }
}
