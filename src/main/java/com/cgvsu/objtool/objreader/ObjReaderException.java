package com.cgvsu.objtool.objreader;

public class    ObjReaderException extends RuntimeException {
    public ObjReaderException(String message, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + message);
    }
}