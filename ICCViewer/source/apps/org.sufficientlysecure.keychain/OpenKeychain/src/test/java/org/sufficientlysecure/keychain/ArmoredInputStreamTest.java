package org.sufficientlysecure.keychain;


import java.io.InputStream;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sufficientlysecure.keychain.pgp.UncachedKeyringTest;
import org.sufficientlysecure.keychain.support.TestDataUtil;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


@RunWith(KeychainTestRunner.class)
public class ArmoredInputStreamTest {

    @Test
    public void armoredInputStream__withDifferentLineEndings() throws Exception {
        // these are generated by GPG4USB, see https://github.com/gpg4usb/gpg4usb/issues/25
        ArmoredInputStream stream1 = new ArmoredInputStream(ArmoredInputStreamTest.class.getResourceAsStream("/armored_crcrlf.asc"));
        ArmoredInputStream stream2 = new ArmoredInputStream(ArmoredInputStreamTest.class.getResourceAsStream("/armored_crlf.asc"));
        ArmoredInputStream stream3 = new ArmoredInputStream(ArmoredInputStreamTest.class.getResourceAsStream("/armored_lf.asc"));

        assertEquals(1, stream1.getArmorHeaders().length);
        assertEquals("Comment: comment content", stream1.getArmorHeaders()[0]);

        assertEquals(1, stream2.getArmorHeaders().length);
        assertEquals("Comment: comment content", stream2.getArmorHeaders()[0]);

        assertEquals(1, stream3.getArmorHeaders().length);
        assertEquals("Comment: comment content", stream3.getArmorHeaders()[0]);

        byte[] data1 = TestDataUtil.readFully(stream1);
        byte[] data2 = TestDataUtil.readFully(stream2);
        byte[] data3 = TestDataUtil.readFully(stream3);

        assertArrayEquals(data1, data2);
        assertArrayEquals(data1, data3);
    }

}