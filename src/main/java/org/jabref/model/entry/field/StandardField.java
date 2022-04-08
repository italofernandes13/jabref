package org.jabref.model.entry.field;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardFieldTest {

    @Test
    void fieldsConsideredEqualIfSame() {
        assertEquals(StandardField.TITLE, StandardField.TITLE);
    }

    @Test
    void fieldVolumeNaoAceitaLetra() {
        String teste = String.valueOf(StandardField.VOLUME.validaVolume("T"))
        assertFalse(teste, Boolean.parseBoolean("T"));
    }
    @Test
    void fieldVolumeNaoAceitaSimbolo() {
        String teste = String.valueOf(StandardField.VOLUME.validaVolume("@"))
        assertFalse(teste, Boolean.parseBoolean("@"));
    }
    @Test
    void fieldVolumeAceitaNumero() {
        String teste = String.valueOf(StandardField.VOLUME.validaVolume("8"))
        assertEquals(teste, "8");
    }
    @Test
    void fieldVolumeNaoAceitaLetraComNumero() {
        String teste = String.valueOf(StandardField.VOLUME.validaVolume("2a28b"))
        assertFalse(teste, Boolean.parseBoolean("2a28b"));
    }
    @Test
    void fieldVolumeNaoAceitaSimboloComNumero() {
        String teste = String.valueOf(StandardField.VOLUME.validaVolume("2@28#"))
        assertFalse(teste, Boolean.parseBoolean("2@28#"));
    }
    @Test
    void fieldVolumeAceitaVariosNumero() {
        String teste = String.valueOf(StandardField.VOLUME.validaVolume("84620"))
        assertEquals(teste, "84620");
    }
}
