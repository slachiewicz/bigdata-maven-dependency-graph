package nl.ordina.jtech.mavendependencygraph.model;

public enum Scope {
    Compile,
    Provided,
    Runtime,
    Test,
    System,
    Import,
    Unknown;

    public static Scope parseFromString(String scope) {
        switch (scope.toLowerCase()) {
            case "compile":
                return Scope.Compile;
            case "provided":
                return Scope.Provided;
            case "import":
                return Scope.Import;
            case "runtime":
                return Scope.Runtime;
            case "system":
                return Scope.System;
            case "test":
                return Scope.Test;
            default:
                return Scope.Unknown;
        }
    }
}
