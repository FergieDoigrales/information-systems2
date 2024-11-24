package com.fergie.lab1.services;
import com.fergie.lab1.models.RoleRequest;
import com.fergie.lab1.models.User;
import com.fergie.lab1.models.enums.AccessRole;
import com.fergie.lab1.models.enums.RequestStatus;
import com.fergie.lab1.repositories.RequestsRepository;
import com.fergie.lab1.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class RequestsService {
    private final RequestsRepository requestsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public RequestsService(RequestsRepository requestsRepository, UsersRepository usersRepository) {
        this.requestsRepository = requestsRepository;
        this.usersRepository = usersRepository;
    }

    public Page<RoleRequest> findAll(Pageable pageable) {
        return requestsRepository.findAll(pageable);
    }

    public RoleRequest findById(Long Id){
        return requestsRepository.findById(Id).orElse(null);
    }

    @Transactional
    public void addRequest(RoleRequest request){
        request.setRequestDate(new java.util.Date());
        requestsRepository.save(request);
    }

    @Transactional
    public void considerRequest(Long Id,  RequestStatus status){
        RoleRequest roleRequest = requestsRepository.findById(Id).orElse(null);
        assert roleRequest != null;
        User user = usersRepository.findByUsername(roleRequest.getUsername()).orElse(null);
        if (user != null){
            roleRequest.setStatus(status);
            if (status.equals(RequestStatus.ACCEPTED)){
                user.setRole(AccessRole.ADMIN);
            }
            requestsRepository.save(roleRequest);
        }
    }
}
