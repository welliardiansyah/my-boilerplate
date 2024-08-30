package com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "w_permission", indexes = {
        @Index(name = "idx_permission_name", columnList = "name"),
        @Index(name = "idx_created_by", columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private int sequence;

    @CreatedBy
    @Column(updatable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @JsonFormat(timezone = "Asia/Jakarta", pattern = "dd MMM yyyy HH:mm:ss", locale = "id")
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedBy
    private UUID updatedBy;

    @UpdateTimestamp
    @Temporal(TIMESTAMP)
    @JsonFormat(timezone = "Asia/Jakarta", pattern = "dd MMM yyyy HH:mm:ss", locale = "id")
    private Date updatedDate;
}
