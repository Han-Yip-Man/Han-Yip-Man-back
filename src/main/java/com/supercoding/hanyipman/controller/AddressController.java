package com.supercoding.hanyipman.controller;

import com.supercoding.hanyipman.dto.address.request.AddressRegisterRequest;
import com.supercoding.hanyipman.dto.address.response.AddressListResponse;
import com.supercoding.hanyipman.dto.shop.buyer.response.ViewShopListResponse;
import com.supercoding.hanyipman.dto.vo.Response;
import com.supercoding.hanyipman.security.JwtToken;
import com.supercoding.hanyipman.service.AddressService;
import com.supercoding.hanyipman.utils.ApiUtils;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "회원 주소 CRUD")
@RequestMapping("/api/addresses")
public class AddressController {


    private final AddressService addressService;

    //TODO 주소등록
    @PostMapping("/register")
    public Response<String> registerAddress(AddressRegisterRequest addressRegisterRequest) {
        String saveAddress = addressService.registerAddress(JwtToken.user(), addressRegisterRequest);

        return ApiUtils.success(HttpStatus.OK, saveAddress + " 주소가 추가되었습니다.", null);
    }

    // Todo 주소 목록 조회
    @GetMapping()
    public Response<List<AddressListResponse>> getAddressList() {
        return ApiUtils.success(HttpStatus.OK, "", addressService.getAddressList(JwtToken.user()));

    }

    // Todo 주소 수정
    // Todo 주소 삭제
}
