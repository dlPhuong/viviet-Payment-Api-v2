import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String LPBPublicKey = "<RSAKeyValue><Modulus>t031qq/TIfO+BFNiGAd2RpwSrwsiXSH5gw4tSG0rCR6Rsv4WUrDZ/oTMFuGLFaGGX6L7Ah3ghggdK5y4C8J2Lda7Ba/FytNLotfzRXU8frIZeFMpwC7cO0SMx9G4Xr6Rgs9XCIuHZCO7uTCh8jFyejkD/bXNWUVw6RobJZ5Iyvk=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>"; //public Key Lien viet

        String Merchant_site = "lacviet_7134";
//linkSanbox
        String Linkurl = "https://sandbox.viviet.vn/vipay_v2/initpay";
        String MerchantPrivateKey = "<RSAKeyValue><Modulus>hnIA/ZWImwE3D/f6ymgqkyruC5TdYOpYiAYoX0GgFzouBJk8Q2oESmgqJy9/fzddwsy46AF+yntnCl9Ct5tprYFUefExrHeo7azwEUE6TnuTlYW96fhmkmvnapNUVGwBdRAkNqUGeoAsd7HQxpWsKogHxYlUceyRM1FFLfFktUk=</Modulus><Exponent>AQAB</Exponent><P>0Q0mJn7BxCGhrFcHXTCrwOPsnPGjrhOn+OzIbJrOOrr0RQCEeBaMUsQa7PZjMimzaJwpFHko7bDxiCYYD3Sg4Q==</P><Q>pKOUZHPg9PFMsumL3U5x35UV9GSNmkHVQ3dtN5DieddHPCFOVW35DlPrJrnYiSJ7g9fLrOeZoZOw5AiGFDDZaQ==</Q><DP>t/XRN5jd3EEYKzceZNQ6n/AyPF2rgrkLXFLJgECt8CKqZ7ov2BlHfGHTq+iZm//0P78Dq19/M8M8mGmlTJx+IQ==</DP><DQ>OU9Lz2/a5ci9NyY+7olo8Dg4TTtXY1P5RaPj28zOWTVL8a1yVCCU6pzhpThaHNpo/mr21jhEbU846xfgfFTIyQ==</DQ><InverseQ>y/+yBE5mWX3lVivjhHCNSk83N/vpktA7VJi5qp1m0i8f14k9JYklWIZmWEsoVDFW1zRldOPR0jIvUP7LRW7LoA==</InverseQ><D>DdnxAC9Hw7B8W8jQrcnNmRvzKn5L0JBUTq9TuXypYbs8wnyHGOrzRjc1IJRkLlcl8cvlwQFmbrYIB6mI1iHM4UzRsgZ2uCpKieMXEYTgBEiOlFVZ2k1JdUVP30REi5l6fBOfy2mImhh5orsH30ZgXSvKLthq4sZZ/ROdH3OzIAE=</D></RSAKeyValue>";

        paymentService paymentService = new paymentService();
        ViVietProduct viVietProduct = new ViVietProduct();
        viVietProduct.setAccess_code("3685"); //
        viVietProduct.setVersion("2.0");
        viVietProduct.setReturn_url("https://ebh-tsc.xti.vn//ketquagiaodich/");
        viVietProduct.setMerch_txn_ref("" + new Date().getTime());
        viVietProduct.setMethods("");
        viVietProduct.setOrder_no("ORDER_1550215482761");
        viVietProduct.setOrder_desc("mua chó");
        viVietProduct.setTotal_amount("20000000");
        viVietProduct.setBranch_code("00001");
        viVietProduct.setClient_ip("127.0.0.1");

        System.out.println(paymentService.CreateUrlPayment_ViViet(viVietProduct, LPBPublicKey, MerchantPrivateKey, Merchant_site, Linkurl));

    }

}
