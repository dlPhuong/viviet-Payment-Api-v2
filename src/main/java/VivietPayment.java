import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VivietPayment { // đây là code của bản v1

    // URL viviet payment
    private static final String IPG_URL = "https://sandbox.viviet.vn/vipay/ecommerce";

    // Mã merchante site
    private static final String MERCHANT_SITE_ID = "1";

    // Mật khẩu bảo mật
    private static final String ACCESS_CODE = "xxxxx";
    // Mã dùng để hash
    private static final String SECURITY_SECRET = "xxxxxx";

    /**
     * Hàm thực hiện tạo mã Hash để check tính toàn vẹn của dữ liệu
     *
     * @param messages
     * @return
     */
    public String getSecureHash(String message) {
        return hash(SECURITY_SECRET + "|" + message, "SHA-256");
    }

    /**
     * Hàm hash dữ liệu
     *
     * @param message
     *            dữ liệu hash
     * @param algorithm
     *            thuật toán hash
     * @return
     */
    public String hash(String message, String algorithm) {
        MessageDigest md;
        byte byteData[];
        try {
            md = MessageDigest.getInstance(algorithm);
            md.update(message.getBytes("UTF-8"));
            byteData = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return sb.toString();
    }

    /**
     * Hàm thực hiện tạo URL request yêu cầu thanh toán trên Ví Việt
     *
     * @param parameters
     *            tham số request
     * @return
     */
    public String createRequestUrl(Map<String, String> parameters) {
        StringBuilder requestUrl = new StringBuilder(IPG_URL);

        boolean firstEntry = true;
        for (Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (firstEntry) {
                requestUrl.append("?");
                firstEntry = false;
            } else {
                requestUrl.append("&");
            }
            requestUrl.append(String.format("%s=%s", key, value));
        }

        String secureHash = getSecureHash(createValueFormated(parameters));
        requestUrl.append(String.format("&%s=%s", "secure_hash", secureHash));
        return requestUrl.toString();
    }

    /**
     * Hàm tạo chuỗi String có value ngăn cách nhau bằng '|' và sắp xếp theo thứ
     * tự tên tham số
     *
     * @param parameters
     * @return
     */
    private String createValueFormated(Map<String, String> parameters) {
        List<String> keyList = new ArrayList<String>(parameters.keySet());
        Collections.sort(keyList);
        StringBuilder valueFormated = new StringBuilder();
        for (String key : keyList) {
            valueFormated.append(parameters.get(key));
            valueFormated.append("|");
        }
        return valueFormated.toString()
                .substring(0, valueFormated.length() - 1);
    }

    /**
     * Hàm valid response từ Ví Việt
     *
     * @param parameters
     *            Tham số trả về
     * @return
     */
    public boolean validResponseUrl(Map<String, String> parameters) {
        String secure_hash_response = parameters.get("secure_hash");
        String secureHash_client = getSecureHash(createValueFormated(parameters));
        return secureHash_client.equals(secure_hash_response);

    }
}
