package com.supercoding.hanyipman.service;

import com.supercoding.hanyipman.advice.annotation.TimeTrace;
import com.supercoding.hanyipman.dto.address.request.AddressRegisterRequest;
import com.supercoding.hanyipman.dto.address.response.AddressListResponse;
import com.supercoding.hanyipman.entity.Address;
import com.supercoding.hanyipman.entity.Buyer;
import com.supercoding.hanyipman.entity.User;
import com.supercoding.hanyipman.error.CustomException;
import com.supercoding.hanyipman.error.domain.AddressErrorCode;
import com.supercoding.hanyipman.error.domain.BuyerErrorCode;
import com.supercoding.hanyipman.repository.AddressRepository;
import com.supercoding.hanyipman.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final BuyerRepository buyerRepository;

    //TODO 주소등록
    @Transactional
    public String registerAddress(User user, AddressRegisterRequest request) {
        Integer limitAddress = 3; // 개인당 가질 수 있는 주소 갯수
        //Todo 주소 3개초과 등록 못함, 주소 빈값 검사
        if (request.getAddress() == null || request.getAddressDetail() == null || request.getLatitude() == null || request.getLongitude() == null)
            throw new CustomException(AddressErrorCode.EMPTY_ADDRESS_DATA);
        Buyer buyerId = buyerRepository.findByUser(user);
        if (buyerId.getAddresses() == null) throw new CustomException(BuyerErrorCode.INVALID_BUYER);
        if (addressRepository.existsAllByAddress(request.getAddress()) >= 1)
            throw new CustomException(AddressErrorCode.DUPLICATE_ADDRESS);
        Integer allByUserCountId = addressRepository.findAllByUserCountId(buyerId.getId());
        if (allByUserCountId >= limitAddress) throw new CustomException(AddressErrorCode.ADDRESS_DATA_EXCEED_LIMIT);

        Buyer buyer = buyerRepository.findById(buyerId.getId()).orElseThrow(() -> new CustomException(BuyerErrorCode.INVALID_BUYER));
        Address address = Address.toAddAddress(buyer, request);

        Address save = addressRepository.save(address);
        return save.getAddress();
    }

    @TimeTrace//9월7일 Total time = 0.083849917s
    @Transactional
    public List<AddressListResponse> getAddressList(User user) {
//        buyerRepository.findByUser(user).getId();
//        addressRepository.findAllByGetAddressList(user);
        Buyer byUser = buyerRepository.findByUser(user);

        addressRepository.findAllByBuyer(byUser);

        return addressRepository.findAllByBuyer(byUser)
                .stream()
                .map(AddressListResponse::toaAddressListResponse)
                .collect(Collectors.toList());
    }
    // Todo 주소 목록 조회
    // Todo 주소 수정
    // Todo 주소 삭제
}








