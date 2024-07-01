package com.gpbitfactory.middle.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TransferDTO(@NotBlank @Size(min = 1,max = 64) String from,
                          @NotBlank @Size(min = 1,max = 64) String to,
                          @NotBlank @Pattern(regexp = "\\d{1,64}\\.\\d{2}") String amount) {
}
