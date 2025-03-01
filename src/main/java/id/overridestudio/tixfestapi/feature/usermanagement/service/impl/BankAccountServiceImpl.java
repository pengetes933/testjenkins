package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.core.service.AesService;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.BankAccountRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.BankAccountResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Bank;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.BankAccount;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.BankAccountRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.BankAccountService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.BankService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerService;
import id.overridestudio.tixfestapi.core.service.impl.AesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final EventOrganizerService eventOrganizerService;
    private final BankService bankService;
    private final AesService aesService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankAccountResponse create(String eventOrganizerId, BankAccountRequest request) {
        EventOrganizer eventOrganizer = eventOrganizerService.getOne(eventOrganizerId);
        Bank bank = bankService.getOne(request.getBankCode());
        String encryptedAccountNumber = aesService.encrypt(request.getAccountNumber());

        BankAccount bankAccount = BankAccount.builder()
                .eventOrganizer(eventOrganizer)
                .bank(bank)
                .accountNumber(encryptedAccountNumber)
                .accountHolderName(request.getAccountHolderName())
                .build();
        return toResponse(createOne(bankAccount));
    }

    private BankAccount createOne(BankAccount bankAccount) {
        return bankAccountRepository.saveAndFlush(bankAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public BankAccountResponse getById(String id) {
        return toResponse(getOne(id));
    }

    private BankAccount getOne(String id){
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankAccountResponse updateById(String id, BankAccountRequest request) {
        BankAccount bankAccount = getOne(id);
        Bank bank = bankService.getOne(request.getBankCode());
        bankAccount.setBank(bank);
        bankAccount.setAccountNumber(aesService.encrypt(request.getAccountNumber()));
        bankAccount.setAccountHolderName(request.getAccountHolderName());
        bankAccount.setUpdatedAt(LocalDateTime.now());
        return toResponse(createOne(bankAccount));
    }

    @Override
    public boolean existByBankAccountIdAndEventOrganizerIdAndUserId(String bankAccountId, String eventOrganizerId, String userId) {
        return bankAccountRepository.existsByIdAndEventOrganizerIdAndEventOrganizer_UserAccount_Id(bankAccountId, eventOrganizerId, userId);
    }


    private BankAccountResponse toResponse(BankAccount bankAccount) {
        return BankAccountResponse.builder()
                .id(bankAccount.getId())
                .eventOrganizerId(bankAccount.getEventOrganizer().getId())
                .bankName(bankAccount.getBank().getName())
                .accountNumber(aesService.decrypt(bankAccount.getAccountNumber()))
                .accountHolderName(bankAccount.getAccountHolderName())
                .build();
    }
}
