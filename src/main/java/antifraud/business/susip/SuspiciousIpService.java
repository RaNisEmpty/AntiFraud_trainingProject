package antifraud.business.susip;

import antifraud.business.susip.dto.IpRequest;
import antifraud.exception.EntityExistsException;
import antifraud.exception.NotFoundException;
import antifraud.exception.WrongFormatException;
import antifraud.persistence.SuspiciousIpRepository;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuspiciousIpService {


    private final InetAddressValidator validator = InetAddressValidator.getInstance();
    SuspiciousIpRepository ipRepository;

    public SuspiciousIpService(SuspiciousIpRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    public SuspiciousIp findByIp(String ip) throws WrongFormatException, NotFoundException{
        if (!validator.isValid(ip)) {
            throw new WrongFormatException();
        }
        return ipRepository.findByIp(ip).orElseThrow(NotFoundException::new);
    }

    public boolean existsByIp(String ip) {
        if (!validator.isValid(ip)) {
            throw new WrongFormatException();
        }
        return ipRepository.findByIp(ip).isPresent();
    }

    public SuspiciousIp delete(String ipRequest) {
        SuspiciousIp ip = findByIp(ipRequest);
        ipRepository.delete(ip);
        return ip;
    }

    public List<SuspiciousIp> findAll() {
        return (List<SuspiciousIp>) ipRepository.findAll();
    }

    public SuspiciousIp save(IpRequest ipRequest) throws EntityExistsException, WrongFormatException{

        SuspiciousIp ip = new SuspiciousIp();

        if (ipRepository.findByIp(ipRequest.getIp()).isPresent()) {
            throw new EntityExistsException();
        }

        if (!validator.isValid(ipRequest.getIp())) {
            throw new WrongFormatException();
        }

        ip.setIp(ipRequest.getIp());
        return ipRepository.save(ip);

    }

}
