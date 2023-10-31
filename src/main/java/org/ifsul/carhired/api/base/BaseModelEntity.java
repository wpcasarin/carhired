package org.ifsul.carhired.api.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModelEntity {
    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("deletedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @PrePersist
    protected void onCreate() {
        var now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
