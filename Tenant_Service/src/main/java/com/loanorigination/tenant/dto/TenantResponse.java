package com.loanorigination.tenant.dto;

import com.loanorigination.tenant.entity.TenantStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantResponse {

    private Long id;

    private String tenantCode;

    private String tenantName;

    private String email;

    private String phone;

    private String website;

    private TenantStatus status;

}
