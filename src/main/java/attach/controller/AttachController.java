package attach.controller;


import attach.dto.AttachDTO;
import attach.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/attach")
@RestController
public class AttachController {

    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    @Operation(summary = "Api for attach", description = "This api is used for uploading video and photo to server")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(attachService.upload(file));
    }
    @GetMapping("/open/{id}")
    @Operation(summary = "Api for attach", description = "This api is used for getting video and photo by (id) from server")

    public ResponseEntity<Resource> open(@PathVariable String id){
        return attachService.open(id);
    }
    /* */

    @GetMapping("/download/{id}")
    @Operation(summary = "Api for attach", description = "This api is used to download video and photo from server (id)")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        return attachService.download(id);
    }

    @GetMapping("/all")
    @Operation(summary = "getting attaches", description = "This api is used to get all videos and photos from server")
    public ResponseEntity<?> all(@RequestParam int page,
                                 @RequestParam int size) {
        page = Math.max(page - 1, 0);
        return ResponseEntity.ok(attachService.getAll(page, size));
    }


    }

