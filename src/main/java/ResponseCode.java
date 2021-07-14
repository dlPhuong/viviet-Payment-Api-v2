    class ResponseCode
    {
        public String RspCode;

        public String Message;

        public ResponseCode(String rspCode, String message) {
            RspCode = rspCode;
            Message = message;
        }

        public String getRspCode() {
            return RspCode;
        }

        public void setRspCode(String rspCode) {
            RspCode = rspCode;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }
    }
