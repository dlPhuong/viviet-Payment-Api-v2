
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import org.xml.sax.InputSource;


public class paymentService { // service của bản v2
    static ObjectMapper mapper = new ObjectMapper();

    public static String CreateUrlPayment_ViViet(ViVietProduct objectViVietProduct, String LPBPublicKey, String MerchantPrivateKey, String Merchant_site, String Linkurl) {
        try {
            byte[] TripleDESalg = encryptTripleDESalg("tungnd123qeewvbvb");// mã vớ vẩn thôi đủ 17 ký tự là được
            //object to json
            String RequestData = mapper.writeValueAsString(objectViVietProduct);
            //Mã hoá TripleDESalg
            String key = encrypt(TripleDESalg, LPBPublicKey);
            //Mã hoá dữ liệu
            String data = encrypt(RequestData, TripleDESalg);
            //SignRSA
            String secure_hash = createSignRSA(RequestData, MerchantPrivateKey);
            return Linkurl + "?merchant_site=" + Merchant_site + "&data=" + data + "&key=" + key + "&secure_hash=" + secure_hash + "";
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }

    }

    public static String validateDataResponse(String keyReturn, String dataReturn, String MerchantPrivateKey) {
        String jsonData = "";
        try {
            byte[] TripleDESalgTest = decryptToByte(keyReturn, MerchantPrivateKey);
            jsonData = decrypt(dataReturn, TripleDESalgTest);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    public static byte[] encryptTripleDESalg(String message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                .getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8; ) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        final byte[] plainTextBytes = message.getBytes("utf-8");
        final byte[] cipherText = cipher.doFinal(plainTextBytes);
        // final String encodedCipherText = new sun.misc.BASE64Encoder()
        // .encode(cipherText);

        return cipherText;
    }


    public static String createSignRSA(String data, String xmlPrivateKey) throws Exception {
        PrivateKey privKey = getPrivateKeyFromXML(xmlPrivateKey);

        Signature instance = Signature.getInstance("SHA256withRSA");
        instance.initSign(privKey);
        instance.update(data.getBytes("UTF-8"));
        byte[] signature = instance.sign();
// convert Byte to Base64
        return Base64.encodeBase64URLSafeString(signature);
    }

    public static boolean checkSignRSA(String data, String signature,
                                       String xmlPublicKey) throws Exception {
        PublicKey pubKey = getPublicKeyFromXML(xmlPublicKey);
        Signature instance = Signature.getInstance("SHA256withRSA");
        instance.initVerify(pubKey);
        instance.update(data.getBytes("UTF-8"));
        return instance.verify(Base64.decodeBase64(signature));
    }

    public static String encrypt(byte[] byteData, String xmlPublicKey)
            throws Exception {
        Cipher cipher = createCipherEncrypt(xmlPublicKey);
        byte[] encrypted = cipher.doFinal(byteData);
        return Base64.encodeBase64URLSafeString(encrypted);
    }

    public static byte[] decryptToByte(String encrypted,
                                       String xmlPrivateKey) throws Exception {
        Cipher cipher = createCipherDecrypt(xmlPrivateKey);
        byte[] bts = Base64.decodeBase64(encrypted);
        byte[] decrypted = cipher.doFinal(bts);
        return decrypted;
    }

    public static String encrypt(String data, byte[] key)
            throws Exception {
// Create and initialize the encryption engine
        Cipher cipher = Cipher.getInstance("DESede");
        SecretKeySpec keyspec = new SecretKeySpec(key, "DESede");

        cipher.init(Cipher.ENCRYPT_MODE, keyspec);
        byte[] encBytes = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.encodeBase64URLSafeString(encBytes);
    }

    public static String decrypt(String data, byte[] key)
            throws Exception {
// Create and initialize the encryption engine
        Cipher cipher = Cipher.getInstance("DESede");
        SecretKeySpec keyspec = new SecretKeySpec(key, "DESede");
        cipher.init(Cipher.DECRYPT_MODE, keyspec);
        byte[] decBytes = cipher.doFinal(Base64.decodeBase64(data));
        return new String(decBytes, "UTF-8");
    }

    public static byte[] generateKey() throws Exception {
// Get a key generator for Triple DES (a.k.a DESede)
        KeyGenerator keygen = KeyGenerator.getInstance("DESede");
// Use it to generate a key
        SecretKey secretKey = keygen.generateKey();
        SecretKeyFactory keyfactory = SecretKeyFactory
                .getInstance("DESede");
        DESedeKeySpec keyspec = (DESedeKeySpec) keyfactory.getKeySpec(
                secretKey, DESedeKeySpec.class);
        byte[] rawkey = keyspec.getKey();
        return rawkey;
    }

    public static PublicKey getPublicKeyFromXML(String xml) throws Exception {
        RSAPublicKeySpec pkeyspec = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);

        String modulus = doc.getElementsByTagName("Modulus").item(0).getTextContent();
        byte[] modulusBytes =
                DatatypeConverter.parseBase64Binary(modulus);
        BigInteger bigModulus = new BigInteger(1, modulusBytes);
        String exponent =
                doc.getElementsByTagName("Exponent").item(0).getTextContent();
        byte[] exponentBytes =
                DatatypeConverter.parseBase64Binary(exponent);
        BigInteger bigExponent = new BigInteger(1, exponentBytes);
        pkeyspec = new RSAPublicKeySpec(bigModulus, bigExponent);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(pkeyspec);
        return pubKey;
    }

    public static PrivateKey getPrivateKeyFromXML(String xml)
            throws Exception {
        RSAPrivateCrtKeySpec pkeyspec = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);
        BigInteger modulus = new
                BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("Modulus").item(0).getTextContent()));
        BigInteger publicExponent = new
                BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("Exponent").item(0).getTextContent()));
        BigInteger privateExponent = new
                BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("D").item(0).getTextContent()));
        BigInteger primeP = new BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("P").item(0).getTextContent()));
        BigInteger primeQ = new BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("Q").item(0).getTextContent()));
        BigInteger primeExponentP = new
                BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("DP").item(0).getTextContent()));
        BigInteger primeExponentQ = new
                BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("DQ").item(0).getTextContent()));
        BigInteger crtCoefficient = new
                BigInteger(1, DatatypeConverter.parseBase64Binary(
                doc.getElementsByTagName("InverseQ").item(0).getTextContent()));
        pkeyspec = new
                RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privKey = fact.generatePrivate(pkeyspec);
        return privKey;
    }

    private static Cipher createCipherEncrypt(String xmlPublicKey) throws Exception {

        PublicKey publicKey = getPublicKeyFromXML(xmlPublicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher;
    }

    private static Cipher createCipherDecrypt(String xmlPrivateKey)
            throws Exception {
        PrivateKey privateKey = getPrivateKeyFromXML(xmlPrivateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher;
    }


}
