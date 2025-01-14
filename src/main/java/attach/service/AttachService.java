package attach.service;

import attach.dto.AttachDTO;
import attach.dto.AttachSimpleDTO;
import attach.entity.AttachEntity;
import attach.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class AttachService {

    @Autowired
    private AttachRepository attachRepository;
    @Value("${server.domain}")
    private String domainName;
    private String folderName = "attaches";


    public AttachDTO upload(MultipartFile file) {
        String pathFolder = getYmDString();
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());
        checkFolder(pathFolder);
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);
            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension);
            entity.setOriginName(file.getOriginalFilename());
            entity.setSize(file.getSize());
            entity.setPath(pathFolder);
            entity.setExtension(extension);
            entity.setVisible(Boolean.TRUE);
            entity.setCreatedDate(LocalDateTime.now());
            attachRepository.save(entity);
            return toDTO(entity);

        } catch (IOException e) {
            throw new RuntimeException("something.went.wrong");
        }

    }


    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    private void checkFolder(String pathFolder) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File folder1 = new File(folderName + "/" + pathFolder);
        if (!folder1.exists()) {
            folder1.mkdirs();
        }
    }

    public Page<AttachDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttachEntity> entityPage = attachRepository.getAll(pageable);
        List<AttachDTO> dtoList = new LinkedList<>();
        for (AttachEntity entity : entityPage) {
            dtoList.add(toDTO(entity));
        }
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalPages());
    }

    public AttachSimpleDTO getDTO(String id) {
        if (id == null) {
            return null;
        }
        AttachSimpleDTO dto = new AttachSimpleDTO();
        dto.setId(id);
        dto.setUrl(getUrl(id));
        return dto;
    }

    public String getOriginName(String id) {
        return attachRepository.getOriginName(id);
    }

    public ResponseEntity<Resource> open(String id) {
        AttachEntity entity = getById(id);
        Path filePath = Paths.get(folderName + "/" + entity.getPath() + "/" + entity.getId()).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("file.not.found");
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    public ResponseEntity<Resource> download(String id) {
        AttachEntity entity = getById(id);
        Path path = Paths.get(folderName + "/" + entity.getPath() + "/" + entity.getId()).normalize();
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginName() + "\"").body(resource);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AttachDTO toDTO(AttachEntity entity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getId());
        attachDTO.setOriginName(entity.getOriginName());
        attachDTO.setSize(entity.getSize());
        attachDTO.setExtension(entity.getExtension());
        attachDTO.setCreatedData(entity.getCreatedDate());
        attachDTO.setUrl(getUrl(entity.getId()));
        attachDTO.setDuration(entity.getDuration());
        attachDTO.setCreatedData(entity.getCreatedDate());

        return attachDTO;
    }

    public String getUrl(String id) {
        return domainName + "/attach/open/" + id;
    }

    private AttachEntity getById(String id) {
        return attachRepository.findByIdAndVisibleTrue(id).orElseThrow(() ->
                new RuntimeException(("file.not.found") + id));
    }
}

