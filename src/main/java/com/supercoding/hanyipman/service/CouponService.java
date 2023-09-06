package com.supercoding.hanyipman.service;

import com.supercoding.hanyipman.dto.coupon.request.RegisterCouponRequest;
import com.supercoding.hanyipman.dto.user.CustomUserDetail;
import com.supercoding.hanyipman.entity.Buyer;
import com.supercoding.hanyipman.entity.BuyerCoupon;
import com.supercoding.hanyipman.entity.Coupon;
import com.supercoding.hanyipman.entity.User;
import com.supercoding.hanyipman.error.CustomException;
import com.supercoding.hanyipman.error.domain.CouponErrorCode;
import com.supercoding.hanyipman.repository.BuyerCouponRepository;
import com.supercoding.hanyipman.repository.BuyerRepository;
import com.supercoding.hanyipman.repository.CouponRepository;
import com.supercoding.hanyipman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final BuyerRepository buyerRepository;
    private final BuyerCouponRepository buyerCouponRepository;

    public void registerCoupon(RegisterCouponRequest request, CustomUserDetail userDetail) {

        Long userId = userDetail.getUserId();
        String couponCode = request.getCouponCode();

        User user = validateUser(userId);
        Buyer buyer = buyerRepository.findByUser(user);

        //코드에 해당하는 쿠폰이 존재하는가?
        Coupon coupon = validateCoupon(couponCode);

        // 해당 쿠폰 코드가 등록 된 적이 있나? 없으면 새로운 쿠폰 저장, 있으면 에러 발생
        if(!buyerCouponRepository.existsBuyerCouponByCoupon(coupon)){

            buyerCouponRepository.save(BuyerCoupon.from(coupon, buyer));

        } else throw new CustomException(CouponErrorCode.REGISTERED_BEFORE);

    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CouponErrorCode.USER_NOT_FOUND));
    }

    private Coupon validateCoupon(String couponCode) {
        return couponRepository.findCouponByCode(couponCode)
                .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));
    }
}