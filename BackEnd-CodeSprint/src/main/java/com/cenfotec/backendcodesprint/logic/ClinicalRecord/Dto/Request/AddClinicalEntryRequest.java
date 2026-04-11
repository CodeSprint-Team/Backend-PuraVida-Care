package com.cenfotec.backendcodesprint.logic.ClinicalRecord.Dto.Request;

import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Enums.EntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddClinicalEntryRequest {

    @NotNull
    private Long clinicalRecordId;

    @NotNull
    private Long providerProfileId;

    @NotNull
    private EntryType entryType;

    @NotBlank
    private String content;

}
