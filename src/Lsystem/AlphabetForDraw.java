package Lsystem;

public class AlphabetForDraw {
    //distinguish what is the operation doing
    private OperationType type;
    //most operations use 3 arguments
    private int intX, intY, intZ;

    public AlphabetForDraw(OperationType type, int intX, int intY, int intZ) {
        this.type = type;
        this.intX = intX;
        this.intY = intY;
        this.intZ = intZ;
    }

    public OperationType getType() {
        return type;
    }

    public int getIntX() {
        return intX;
    }

    public int getIntY() {
        return intY;
    }

    public int getIntZ() {
        return intZ;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public void setIntX(int intX) {
        this.intX = intX;
    }

    public void setIntY(int intY) {
        this.intY = intY;
    }

    public void setIntZ(int intZ) {
        this.intZ = intZ;
    }
}
