package net.ralf2oo2.mipmaps.config;

public enum MipmapLevel {
    OFF(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4);

    private final int level;

    MipmapLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isEnabled() {
        return this.level > 0;
    }
}
