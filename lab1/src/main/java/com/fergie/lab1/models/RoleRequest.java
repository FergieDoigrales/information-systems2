package com.fergie.lab1.models;

import com.fergie.lab1.models.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class RoleRequest {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @JoinColumn(name = "username")
    private String username;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date requestDate;


}
