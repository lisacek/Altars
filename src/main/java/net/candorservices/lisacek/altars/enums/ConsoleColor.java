package net.candorservices.lisacek.altars.enums;

public enum ConsoleColor {
    BLACK("\u001b[30m"),
    RED("\u001b[31m"),
    GREEN("\u001b[32m"),
    YELLOW("\u001b[33m"),
    BLUE("\u001b[34m"),
    PURPLE("\u001b[35m"),
    CYAN("\u001b[36m"),
    WHITE("\u001b[37m"),
    GRAY("\u001B[37m"),
    DARK_GRAY_BOLD("\u001B[1;30m"),
    RESET("\u001b[0m"),
    BOLD("\u001b[1m"),
    ITALICS("\u001b[2m"),
    UNDERLINE("\u001b[4m"),
    WHITE_BOLD("\033[1;37m"),
    CHECK_MARK("✓"),
    ERROR_MARK("✗");

    private final String ansiColor;

    ConsoleColor(String ansiColor) {
        this.ansiColor = ansiColor;
    }

    public String getAnsiColor() {
        return this.ansiColor;
    }

    public String toString() {
        return this.ansiColor;
    }

}