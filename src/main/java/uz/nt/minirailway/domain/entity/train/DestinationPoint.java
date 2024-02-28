package uz.nt.minirailway.domain.entity.train;

import lombok.Getter;

@Getter
public enum DestinationPoint {
    ANDIJAN(1),
    FERGANA(2),
    NAMANGAN(3),
    TASHKENT(4),
    SIRDARYA(5),
    JIZZAKH(6),
    SAMARKAND(7),
    KASHKADARYA(8),
    SURKHANDARYA(9),
    BUKHARA(10),
    NAVOI(11),
    KHOREZM(12),
    NUKUS(13);

    private final int value;

    DestinationPoint(int value){
        this.value = value;
    }
}
