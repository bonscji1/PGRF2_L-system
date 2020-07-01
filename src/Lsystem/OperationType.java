package Lsystem;

public enum OperationType {
    //ENUM FOR SUPPORTED OPERATIONS ON ALPHABET
    DRAW("DRAW"), DRAW_INNER("DRAW_INNER"), CHANGE_INNER_VECTOR("CHANGE_INNER_VECTOR"), CHANGE_INNER_ANGLE("CHANGE_INNER_ANGLE"), CHANGE_DRAW_LENGTH("CHANGE_DRAW_LENGTH"), MOVE("MOVE");

    private String type;

    OperationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static OperationType fromString(String type) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.type.equalsIgnoreCase(type)) {
                return operationType;
            }
        }
        return null;
    }
}
