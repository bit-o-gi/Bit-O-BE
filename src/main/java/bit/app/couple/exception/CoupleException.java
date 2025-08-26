package bit.app.couple.exception;

public class CoupleException extends RuntimeException {
    public CoupleException(String message) {
        super(message);
    }

    public static class CoupleNotFoundException extends CoupleException {
        public CoupleNotFoundException() {
            super("커플을 찾지 못하였습니다.");
        }
    }

    public static class CodeNotFoundException extends CoupleException {
        public CodeNotFoundException() {
            super("알 수 없는 코드입니다.");
        }
    }

    public static class CoupleAlreadyExistsException extends CoupleException {
        public CoupleAlreadyExistsException() {
            super("이미 발급된 코드가 있습니다.");
        }
    }

    public static class CouplePermissionException extends CoupleException {
        public CouplePermissionException() {
            super("해당 커플에 대한 수정 권한이 없습니다.");
        }
    }

    public static class CannotPairWithYourselfException extends CoupleException {
        public CannotPairWithYourselfException() {
            super("나자신을 커플로 만들수 없습니다.");
        }
    }
}
