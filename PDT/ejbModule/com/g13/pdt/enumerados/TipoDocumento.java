package com.g13.pdt.enumerados;

//Si se desea agregar otro TipoDocumento requiere recomplilar y republicar
//Pero seleccionamos esta opci�n intencionadamente para aplicar el uso de @Enumerated

public enum TipoDocumento {
	CI("CI"), Pasaporte("Pasaporte"), DNI("DNI");

    private String label;

    TipoDocumento(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
