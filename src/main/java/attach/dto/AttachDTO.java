package attach.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {
    @NotNull(message = "ID cannot be null")
    @Size(min = 36, max = 36, message = "ID must be a valid UUID")
    private String id;

    @NotNull(message = "Origin name cannot be null")
    @Size(min = 1, max = 255, message = "Origin name must be between 1 and 255 characters")
    private String originName;

    @NotNull(message = "Size cannot be null")
    @PositiveOrZero(message = "Size must be a positive number or zero")
    private Long size;

    @NotNull(message = "File extension cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Extension must only contain alphanumeric characters")
    private String extension;

    @NotNull(message = "Creation date cannot be null")
    private LocalDateTime createdData;

    @NotNull(message = "URL cannot be null")
    @URL(message = "URL must be a valid URL")
    private String url;

    @NotNull(message = "Size cannot be null")
    @PositiveOrZero(message = "Size must be a positive number or zero")
    private long duration;

    public @NotNull(message = "ID cannot be null") @Size(min = 36, max = 36, message = "ID must be a valid UUID") String getId() {
        return id;
    }

    public void setId(@NotNull(message = "ID cannot be null") @Size(min = 36, max = 36, message = "ID must be a valid UUID") String id) {
        this.id = id;
    }

    public @NotNull(message = "Origin name cannot be null") @Size(min = 1, max = 255, message = "Origin name must be between 1 and 255 characters") String getOriginName() {
        return originName;
    }

    public void setOriginName(@NotNull(message = "Origin name cannot be null") @Size(min = 1, max = 255, message = "Origin name must be between 1 and 255 characters") String originName) {
        this.originName = originName;
    }

    public @NotNull(message = "Size cannot be null") @PositiveOrZero(message = "Size must be a positive number or zero") Long getSize() {
        return size;
    }

    public void setSize(@NotNull(message = "Size cannot be null") @PositiveOrZero(message = "Size must be a positive number or zero") Long size) {
        this.size = size;
    }

    public @NotNull(message = "File extension cannot be null") @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Extension must only contain alphanumeric characters") String getExtension() {
        return extension;
    }

    public void setExtension(@NotNull(message = "File extension cannot be null") @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Extension must only contain alphanumeric characters") String extension) {
        this.extension = extension;
    }

    public @NotNull(message = "Creation date cannot be null") LocalDateTime getCreatedData() {
        return createdData;
    }

    public void setCreatedData(@NotNull(message = "Creation date cannot be null") LocalDateTime createdData) {
        this.createdData = createdData;
    }

    public @NotNull(message = "URL cannot be null") @URL(message = "URL must be a valid URL") String getUrl() {
        return url;
    }

    public void setUrl(@NotNull(message = "URL cannot be null") @URL(message = "URL must be a valid URL") String url) {
        this.url = url;
    }

    @NotNull(message = "Size cannot be null")
    @PositiveOrZero(message = "Size must be a positive number or zero")
    public long getDuration() {
        return duration;
    }

    public void setDuration(@NotNull(message = "Size cannot be null") @PositiveOrZero(message = "Size must be a positive number or zero") long duration) {
        this.duration = duration;
    }
}
