package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.AddressRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.ResponsiblePerson;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.ResponsiblePersonAddress;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.ResponsiblePersonAddressRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.ResponsiblePersonAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponsiblePersonAddressServiceImpl implements ResponsiblePersonAddressService {
    private final ResponsiblePersonAddressRepository responsiblePersonAddressRepository;
    @Override
    public ResponsiblePersonAddress create(AddressRequest request, ResponsiblePerson responsiblePerson) {
        ResponsiblePersonAddress responsiblePersonAddress = ResponsiblePersonAddress.builder()
                .responsiblePerson(responsiblePerson)
                .street(request.getStreet())
                .city(request.getCity())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .build();
        return responsiblePersonAddressRepository.saveAndFlush(responsiblePersonAddress);
    }

    @Override
    public ResponsiblePersonAddress update(AddressRequest request, ResponsiblePerson responsiblePerson) {
        ResponsiblePersonAddress responsiblePersonAddress = responsiblePerson.getAddress();
        responsiblePersonAddress.setStreet(request.getStreet());
        responsiblePersonAddress.setCity(request.getCity());
        responsiblePersonAddress.setProvince(request.getProvince());
        responsiblePersonAddress.setPostalCode(request.getPostalCode());
        return responsiblePersonAddressRepository.save(responsiblePersonAddress);
    }
}
