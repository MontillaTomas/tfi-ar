package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.ClientInteraction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientInteractionResponse {
    private Integer id;
    private String date;
    private String details;

    public ClientInteractionResponse(ClientInteraction interaction){
        this.id = interaction.getId();
        this.date = interaction.getDate().toString();
        this.details = interaction.getDetails();
    }
}
