public class ViVietProduct
    {
        public String Version ;

        public String Access_code ;

        public String Return_url ;

        public String Merch_txn_ref ;

        public String Methods ;

        public String Order_no ;

        public String Order_desc ;

        public String Total_amount ;

        public String Branch_code ;

        public String Client_ip ;

        public ViVietProduct() {
        }

        public ViVietProduct(String version, String access_code, String return_url, String merch_txn_ref, String methods, String order_no, String order_desc, String total_amount, String branch_code, String client_ip) {
            Version = version;
            Access_code = access_code;
            Return_url = return_url;
            Merch_txn_ref = merch_txn_ref;
            Methods = methods;
            Order_no = order_no;
            Order_desc = order_desc;
            Total_amount = total_amount;
            Branch_code = branch_code;
            Client_ip = client_ip;
        }

        public String getVersion() {
            return Version;
        }

        public void setVersion(String version) {
            Version = version;
        }

        public String getAccess_code() {
            return Access_code;
        }

        public void setAccess_code(String access_code) {
            Access_code = access_code;
        }

        public String getReturn_url() {
            return Return_url;
        }

        public void setReturn_url(String return_url) {
            Return_url = return_url;
        }

        public String getMerch_txn_ref() {
            return Merch_txn_ref;
        }

        public void setMerch_txn_ref(String merch_txn_ref) {
            Merch_txn_ref = merch_txn_ref;
        }

        public String getMethods() {
            return Methods;
        }

        public void setMethods(String methods) {
            Methods = methods;
        }

        public String getOrder_no() {
            return Order_no;
        }

        public void setOrder_no(String order_no) {
            Order_no = order_no;
        }

        public String getOrder_desc() {
            return Order_desc;
        }

        public void setOrder_desc(String order_desc) {
            Order_desc = order_desc;
        }

        public String getTotal_amount() {
            return Total_amount;
        }

        public void setTotal_amount(String total_amount) {
            Total_amount = total_amount;
        }

        public String getBranch_code() {
            return Branch_code;
        }

        public void setBranch_code(String branch_code) {
            Branch_code = branch_code;
        }

        public String getClient_ip() {
            return Client_ip;
        }

        public void setClient_ip(String client_ip) {
            Client_ip = client_ip;
        }
    }


