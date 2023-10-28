package plugway.mc.music.disc.dj.gui.color;

public enum Colors {
    White(0xFFFFFFFF),
    LightGray78(0xFFC6C6C6),
    Gray50(0xFF808080),
    Black4(0xFF0A0A0A),
    LightGray94(0xFFF0F0F0),
    RedYT(0xFFc90000),
    BrownSC(0xFFc94200),
    PastelBlue(0xFFcbd4e9),
    Gray63(0xFFa0a0a0);

    private final int color;

    Colors(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
