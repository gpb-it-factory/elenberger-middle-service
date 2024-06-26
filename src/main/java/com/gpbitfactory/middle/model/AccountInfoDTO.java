package com.gpbitfactory.middle.model;

import java.util.Objects;

public record AccountInfoDTO(String accountId, String accountName, String amount) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AccountInfoDTO that = (AccountInfoDTO) object;
        return Objects.equals(accountId, this.accountId) &&
                Objects.equals(accountName, accountName) &&
                Objects.equals(amount, this.amount);
    }
}
