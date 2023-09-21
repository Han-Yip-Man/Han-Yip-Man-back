package com.supercoding.hanyipman.service;

import com.supercoding.hanyipman.dto.myInfo.request.SellerUpdateInfoRequest;
import com.supercoding.hanyipman.dto.myInfo.response.MyInfoAddressResponse;
import com.supercoding.hanyipman.dto.myInfo.response.MyInfoResponse;
import com.supercoding.hanyipman.entity.Address;
import com.supercoding.hanyipman.entity.Buyer;
import com.supercoding.hanyipman.entity.Seller;
import com.supercoding.hanyipman.entity.User;
import com.supercoding.hanyipman.error.CustomException;
import com.supercoding.hanyipman.error.domain.BuyerErrorCode;
import com.supercoding.hanyipman.error.domain.SellerErrorCode;
import com.supercoding.hanyipman.error.domain.UserErrorCode;
import com.supercoding.hanyipman.repository.AddressRepository;
import com.supercoding.hanyipman.repository.BuyerRepository;
import com.supercoding.hanyipman.repository.SellerRepository;
import com.supercoding.hanyipman.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.supercoding.hanyipman.entity.User.sellerUpdateMyInfo;

@Slf4j
@Service
@AllArgsConstructor
public class MyInfoService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public MyInfoResponse getUserInfoForMyPage(User user) {
        if (Objects.equals(user.getRole(), "SELLER")) {
            Optional<Seller> seller = sellerRepository.findByUser(user);
            if (seller.isEmpty()) {
                throw new CustomException(SellerErrorCode.NOT_SELLER);
            }
            return MyInfoResponse.toSellerInfoResponse(user, seller.get());

        }
        Buyer buyer = buyerRepository.findByUser(user).orElseThrow(() -> new CustomException(BuyerErrorCode.NOT_BUYER));

        List<Address> addressList = addressRepository.findAllByBuyer(buyer);
        List<MyInfoAddressResponse> myInfoAddressResponses = addressList.stream().map(MyInfoAddressResponse::toMyAddressResponse).collect(Collectors.toList());

        return MyInfoResponse.toBuyerInfoResponse(user, buyer, myInfoAddressResponses);
    }

    @Transactional
    public void sellerUpdateInfo(User user, SellerUpdateInfoRequest request) {
        String password = request.getPassword();
        String passwordCheck = request.getPasswordCheck();

        if (!(user.getId().equals(request.getUserId())))
            throw new CustomException(UserErrorCode.ONLY_OWN_PROFILE_EDITABLE);

        validatePasswordConfirmation(password, passwordCheck, request);
        Seller seller = sellerRepository.findByUser(user).orElseThrow(() -> new CustomException(SellerErrorCode.NOT_SELLER));

        sellerRepository.save(seller.updateBusinessNum(seller, request));
        userRepository.save(sellerUpdateMyInfo(user, seller, request));
    }

    private void validatePasswordConfirmation(String password, String passwordCheck, SellerUpdateInfoRequest request) {
        if (password != null && passwordCheck != null) {
            if (!password.equals(passwordCheck)) {
                throw new CustomException(UserErrorCode.INVALID_PASSWORD_CONFIRMATION);
            }
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }
}
