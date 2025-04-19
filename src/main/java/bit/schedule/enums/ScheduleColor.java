package bit.schedule.enums;

public enum ScheduleColor {
    LAVENDER("#9a9cff"),
    RED("#dc2227"),
    LIGHT_PURPLE("#daadfe"),
    LIGHT_BLUE("#a4bdfd"),
    BLUE("#5485ee"),
    TEAL("#47d6dc"),
    MINT("#7ae7be"),
    GREEN("#51b749"),
    LIGHT_ORANGE("#ffb878"),
    ORANGE("#ffb878"),  // Duplicate value with LIGHT_ORANGE
    LIGHT_GRAY("#e1e1e1");

    private final String hexCode;

    ScheduleColor(String hexCode) {
        this.hexCode = hexCode;
    }

    public static ScheduleColor fromHexCode(String hexCode) {
        for (ScheduleColor color : ScheduleColor.values()) {
            if (color.hexCode.equalsIgnoreCase(hexCode)) {
                return color;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 색상 코드: " + hexCode);
    }

    public String getHexCode() {
        return hexCode;
    }
}
