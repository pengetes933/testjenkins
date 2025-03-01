package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.BankRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.BankResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Bank;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.BankRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.BankService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void init() {
        saveBanks(bumnBanks);
        saveBanks(bpdBanks);
        saveBanks(privateBanks);
        saveBanks(syariahBanks);
    }

    private void saveBanks(List<Bank> banks) {
        for (Bank bank : banks) {
            if (!bankRepository.existsById(bank.getId())) {
                bankRepository.saveAndFlush(bank);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankResponse create(BankRequest request) {
        Bank bank = Bank.builder()
                .id(request.getCode())
                .name(request.getName())
                .build();
        return toResponse(createOne(bank));
    }

    private Bank createOne(Bank bank) {
        if (bankRepository.existsById(bank.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank already registered!");
        }
        return bankRepository.saveAndFlush(bank);
    }

    @Transactional(readOnly = true)
    @Override
    public BankResponse getById(String id) {
        return toResponse(getOne(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankResponse updateById(String id, BankRequest request) {
        Bank bank = getOne(id);
        bank.setId(request.getCode());
        bank.setName(request.getName());
        bank.setUpdatedAt(LocalDateTime.now());
        return toResponse(bankRepository.save(bank));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BankResponse> getAll() {
        return bankRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Bank getOne(String id) {
        return bankRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Bank bank = getOne(id);
        bank.setDeletedAt(LocalDateTime.now());
        bankRepository.save(bank);
    }

    private BankResponse toResponse (Bank bank){
        return BankResponse.builder()
                .code(bank.getId())
                .name(bank.getName())
                .build();
    }

    private final List<Bank> bumnBanks = Arrays.asList(
            new Bank("002", "Bank BRI"),
            new Bank("008", "Bank Mandiri"),
            new Bank("009", "Bank BNI"),
            new Bank("200", "Bank BTN")
    );

    private final List<Bank> bpdBanks = Arrays.asList(
            new Bank("110", "Bank Jabar Banten (BJB)"),
            new Bank("111", "Bank DKI"),
            new Bank("112", "Bank DIY"),
            new Bank("113", "Bank Jateng"),
            new Bank("114", "Bank Jatim"),
            new Bank("115", "Bank Jambi"),
            new Bank("116", "Bank Aceh"),
            new Bank("117", "Bank Sumut"),
            new Bank("118", "Bank Nagari Sumbar"),
            new Bank("119", "Bank Riau Kepri"),
            new Bank("120", "Bank Sumsel Babel"),
            new Bank("121", "Bank Lampung"),
            new Bank("122", "Bank Kalsel"),
            new Bank("123", "Bank Kalimantan Barat"),
            new Bank("124", "Bank Kaltim"),
            new Bank("125", "Bank Kalteng"),
            new Bank("126", "Bank Sulsel"),
            new Bank("127", "Bank Sulut"),
            new Bank("128", "Bank NTB"),
            new Bank("129", "Bank Bali"),
            new Bank("130", "Bank NTT"),
            new Bank("131", "Bank Maluku"),
            new Bank("132", "Bank Papua"),
            new Bank("133", "Bank Bengkulu"),
            new Bank("134", "Bank Sulawesi Tengah"),
            new Bank("135", "Bank Sultra"),
            new Bank("137", "Bank Banten")
    );

    private final List<Bank> privateBanks = Arrays.asList(
            new Bank("014", "Bank BCA"),
            new Bank("022", "Bank CIMB Niaga"),
            new Bank("013", "Bank Permata"),
            new Bank("011", "Bank Danamon"),
            new Bank("016", "Bank Maybank"),
            new Bank("426", "Bank Mega"),
            new Bank("153", "Bank Sinarmas"),
            new Bank("003", "Bank Ekspor Indonesia"),
            new Bank("950", "Bank Commonwealth"),
            new Bank("028", "Bank OCBC NISP"),
            new Bank("441", "Bank Bukopin"),
            new Bank("213", "Bank BTPN"),
            new Bank("213", "Bank Jenius BTPN"),
            new Bank("003", "Bank Ekspor Indonesia"),
            new Bank("019", "Bank Panin"),
            new Bank("020", "Bank Arta Niaga Kencana"),
            new Bank("023", "Bank UOB Indonesia"),
            new Bank("036", "Bank Multicor"),
            new Bank("037", "Bank Artha Graha"),
            new Bank("047", "Bank Pesona Perdania"),
            new Bank("052", "Bank ABN Amro"),
            new Bank("026", "Bank Lippo"),
            new Bank("053", "Bank Keppel Tatlee Buana"),
            new Bank("057", "Bank BNP Paribas Indonesia"),
            new Bank("023", "Bank UOB Indonesia"),
            new Bank("068", "Bank Woori Indonesia"),
            new Bank("076", "Bank Bumi Artha"),
            new Bank("087", "Bank Ekonomi"),
            new Bank("089", "Bank Haga"),
            new Bank("093", "Bank IFI"),
            new Bank("095", "Bank Century/Bank J Trust Indonesia"),
            new Bank("097", "Bank Mayapada"),
            new Bank("494", "Bank BNI Agro"),
            new Bank("145", "Bank Nusantara Parahyangan"),
            new Bank("146", "Bank Swadesi"),
            new Bank("151", "Bank Mestika"),
            new Bank("152", "Bank Metro Express"),
            new Bank("157", "Bank Maspion"),
            new Bank("159", "Bank Hagakita"),
            new Bank("161", "Bank Ganesha"),
            new Bank("162", "Bank Windu Kentjana"),
            new Bank("164", "Bank ICBC Indonesia"),
            new Bank("166", "Bank Harmoni Internasional"),
            new Bank("167", "Bank QNB"),
            new Bank("405", "Bank Swaguna"),
            new Bank("459", "Bank Bisnis Internasional"),
            new Bank("466", "Bank Sri Partha"),
            new Bank("494", "Bank Raya"),
            new Bank("523", "Bank Sahabat Sampoerna"),
            new Bank("535", "Bank SeaBank"),
            new Bank("567", "Bank AlloBank"),
            new Bank("513", "Bank Ina Perdana"),
            new Bank("555", "Bank Index Selindo"),
            new Bank("542", "Bank Jago"),
            new Bank("484", "Bank KEB Hana Indonesia"),
            new Bank("553", "Bank Mayora"),
            new Bank("485", "Bank MNC Internasional"),
            new Bank("503", "Bank Nobu"),
            new Bank("490", "Bank Neo"),
            new Bank("526", "Bank Oke Indonesia")
    );

    private final List<Bank> syariahBanks = Arrays.asList(
            new Bank("451", "Bank Syariah Indonesia (BSI)"),
            new Bank("022", "Bank CIMB Niaga Syariah"),
            new Bank("147", "Bank Muamalat"),
            new Bank("947", "Bank Aladin Syariah"),
            new Bank("536", "Bank BCA Syariah"),
            new Bank("521", "Bank Bukopin Syariah"),
            new Bank("425", "Bank BJB Syariah"),
            new Bank("517", "Bank Panin Dubai Syariah"),
            new Bank("506", "Bank Mega Syariah"),
            new Bank("547", "Bank BTPN Syariah")
    );
}
