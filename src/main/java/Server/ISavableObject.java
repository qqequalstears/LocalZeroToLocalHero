package Server.Model;

/**
 * The ISavableObject interface defines a contract for objects that can be
 * serialized into a savable string format. Implementing classes should provide
 * a mechanism to convert their state into a string representation that can be
 * stored or transmitted.
 * 
 * @author Jansson Anton
 * @Date 2025-04-16
 */
public interface ISavableObject {

    /**
     * Converts the current state of the object into a string representation.
     * This string can be used to save the object's state or transmit it to
     * another system.
     *
     * @return A string representation of the object's state.
     * @author Jansson Anton
     * @Date 2025-04-16
     */
    String getSavableString();

    // String methodName(String data);//TODO method for extracting data from a String for the object construction? @Jansson 16/4 2025
}

