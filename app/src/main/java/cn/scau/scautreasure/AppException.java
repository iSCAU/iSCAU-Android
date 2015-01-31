package cn.scau.scautreasure;

import android.content.Context;

/**
 * 异常
 */
public class AppException extends Exception {

    private Context ctx;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type = -1;

    /**
     * 405:学号不存在
     * 406:密码错误
     * 407:图书馆密码错误
     * 408:图书馆续借失败,续借失败，已达到续借上限
     * 409:校园卡密码错误
     * 500:正方挂了
     * 0:未知错误
     *
     * @param HtppRequestCode
     * @param ctx
     *
     * @throws AppException
     */
    public void parseException(int HtppRequestCode, Context ctx) throws AppException {
        this.ctx = ctx;
        if (HtppRequestCode == 405) {

            throw new UserNameException(405);
        } else if (HtppRequestCode == 406) {

            throw new PassWordException(406);
        } else if (HtppRequestCode == 407) {

            throw new LibraryPassportException(407);
        } else if (HtppRequestCode == 408) {

            throw new RenewFailedException(408);
        } else if (HtppRequestCode == 409) {

            throw new CardPassportException(409);
        } else if (HtppRequestCode == 500) {

            throw new SeverErrorException(500);
        } else {

            throw new UnknowException(0);
        }
    }

    public class UserNameException extends AppException {

        public UserNameException(int type) {
            super.type = type;
        }

        @Override
        public int getType() {

            return super.getType();
        }

        @Override
        public String getMessage() {
            return "这个学号不存在";
        }

    }

    public class PassWordException extends AppException {
        public PassWordException(int type) {
            super.type = type;
        }

        @Override
        public int getType() {
            return super.getType();
        }

        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_password_error);
        }
    }

    public class LibraryPassportException extends AppException {
        @Override
        public int getType() {
            return super.getType();
        }

        public LibraryPassportException(int type) {
            super.type = type;
        }

        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_library_passport_error);
        }
    }

    public class CardPassportException extends AppException {
        public CardPassportException(int type) {
            super.type = type;
        }

        @Override
        public int getType() {
            return super.getType();
        }

        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_card_passport_error);
        }
    }

    public class SeverErrorException extends AppException {
        @Override
        public int getType() {
            return super.getType();
        }

        public SeverErrorException(int type) {
            super.type = type;
        }

        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_server_error);
        }
    }

    public class RenewFailedException extends AppException {
        public RenewFailedException(int type) {
            super.type = type;
        }

        @Override
        public int getType() {
            return super.getType();
        }

        @Override
        public String getMessage() {
            return "续借失败，已达到续借上限";
        }
    }

    public class UnknowException extends AppException {
        public UnknowException(int type) {
            super.type = type;
        }

        @Override
        public int getType() {
            return super.getType();
        }

        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_unknow_error);
        }
    }

}
