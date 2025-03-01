package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.AddressRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.ResponsiblePerson;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.ResponsiblePersonAddress;

public interface ResponsiblePersonAddressService {
    ResponsiblePersonAddress create(AddressRequest request, ResponsiblePerson responsiblePerson);
    ResponsiblePersonAddress update(AddressRequest request, ResponsiblePerson responsiblePerson);
}
