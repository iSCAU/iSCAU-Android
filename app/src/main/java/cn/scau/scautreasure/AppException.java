package cn.scau.scautreasure;

import android.content.Context;

/**
 * User: special
 * Date: 13-9-12
 * Time: 下午11:44
 * Mail: specialcyci@gmail.com
 */
public class AppException extends Exception {

    private Context ctx;

    public void parseException(int HtppRequestCode, Context ctx) throws AppException {
        this.ctx = ctx;
        if (HtppRequestCode == 405) {
            throw new UserNameException();
        } else if (HtppRequestCode == 406) {
            throw new PassWordException();
        } else if (HtppRequestCode == 407) {
            throw new LibraryPassportException();
        } else if (HtppRequestCode == 408) {
            throw new RenewFailedException();
        } else if (HtppRequestCode == 409) {
            throw new CardPassportException();
        } else if (HtppRequestCode == 500) {
            throw new SeverErrorException();
        } else {
            throw new UnknowException();
        }
    }

    public class UserNameException extends AppException {
        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_username_error);
        }
    }

    public class PassWordException extends AppException {
        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_password_error);
        }
    }

    public class LibraryPassportException extends AppException {
        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_library_passport_error);
        }
    }

    public class CardPassportException extends AppException {
        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_card_passport_error);
        }
    }

    public class SeverErrorException extends AppException {
        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_server_error);
        }
    }

    public class RenewFailedException extends AppException {
        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_renew_failed);
        }
    }

    public class UnknowException extends AppException {
        @Override
        public String getMessage() {
            return ctx.getString(R.string.tips_unknow_error);
        }
    }
}
