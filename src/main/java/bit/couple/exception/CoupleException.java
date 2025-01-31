package bit.couple.exception;

public class CoupleException {
    public static class CoupleNotFoundException extends IllegalArgumentException {
        public CoupleNotFoundException() {
            super("커플를 찾지 못하였습니다.");
        }
    }
    public static class CodeNotFoundException extends IllegalArgumentException {
        public CodeNotFoundException() {
            super("알수없는 코드입니다.");
        }
    }
}
