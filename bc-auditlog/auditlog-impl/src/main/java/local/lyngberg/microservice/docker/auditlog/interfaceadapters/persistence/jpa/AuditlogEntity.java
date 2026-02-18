package local.lyngberg.microservice.docker.auditlog.interfaceadapters.persistence.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditlogEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    
    @NotNull
    @Size(max = 100)
    @Column(name = "service_name", nullable = false)
    private String serviceName;
    
    @NotNull
    @Size(max = 50)
    @Column(name = "action", nullable = false)
    private String action;
    
    @NotNull
    @Size(max = 255)
    @Column(name = "entity_type", nullable = false)
    private String entityType;
    
    @Column(name = "entity_id", columnDefinition = "BINARY(16)")
    private UUID entityId;
    
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    
    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;
    
    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;
    
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    
    @NotNull
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
    
    @Size(max = 500)
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @NotNull
    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;
    
    @Size(max = 20)
    @Column(name = "status", nullable = false)
    private String status = "SUCCESS"; // SUCCESS, FAILED, ERROR
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "request_id")
    private String requestId;
    
    // Constructors
    protected AuditlogEntity() {} // JPA requires a default constructor
    
    public AuditlogEntity(String serviceName, String action, String entityType, String userEmail, String ipAddress) {
        this.serviceName = serviceName;
        this.action = action;
        this.entityType = entityType;
        this.userEmail = userEmail;
        this.ipAddress = ipAddress;
    }
    
    public AuditlogEntity(String serviceName, String action, String entityType, UUID entityId, 
                   UUID userId, String userEmail, String oldValue, String newValue, 
                   String description, String ipAddress, String userAgent) {
        this.serviceName = serviceName;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.description = description;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    
    public UUID getEntityId() { return entityId; }
    public void setEntityId(UUID entityId) { this.entityId = entityId; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
}
