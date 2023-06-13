package antifraud.presentation;

import antifraud.business.susip.SuspiciousIp;
import antifraud.business.susip.SuspiciousIpService;
import antifraud.business.susip.dto.IpDeleteResponse;
import antifraud.business.susip.dto.IpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IpController {

    SuspiciousIpService ipService;

    @Autowired
    public IpController(SuspiciousIpService ipService) {
        this.ipService = ipService;
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<SuspiciousIp> postIp(@Validated @RequestBody IpRequest ipRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(ipService.save(ipRequest));
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public IpDeleteResponse deleteIp(@PathVariable String ip) {
        return IpDeleteResponse.fromEntity(ipService.delete(ip));
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public List<SuspiciousIp> getIpList() {
        return ipService.findAll();
    }

}
