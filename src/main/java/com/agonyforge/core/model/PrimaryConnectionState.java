package com.agonyforge.core.model;

import com.agonyforge.core.model.util.BaseEnumSetConverter;
import com.agonyforge.core.model.util.PersistentEnum;

public enum PrimaryConnectionState implements PersistentEnum {
    LOGIN(0),
    CREATION(1),
    IN_GAME(2),
    MENU(3);

    private int index;

    PrimaryConnectionState(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public static class Converter extends BaseEnumSetConverter<PrimaryConnectionState> {
        public Converter() {
            super(PrimaryConnectionState.class);
        }
    }
}
