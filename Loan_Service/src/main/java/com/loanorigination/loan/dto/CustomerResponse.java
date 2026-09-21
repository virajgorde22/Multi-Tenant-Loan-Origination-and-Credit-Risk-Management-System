package com.loanorigination.loan.dto;

public class CustomerResponse {

    private Long id;

    private Long tenantId;

    private String customerCode;

    private String firstName;

    private String lastName;

    private String email;

    private String employmentType;

    public CustomerResponse() {
    }

    public CustomerResponse(
            Long id,
            Long tenantId,
            String customerCode,
            String firstName,
            String lastName,
            String email,
            String employmentType
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.customerCode = customerCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.employmentType = employmentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }
}