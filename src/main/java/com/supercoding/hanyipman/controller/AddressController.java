package com.supercoding.hanyipman.controller;

import com.supercoding.hanyipman.dto.address.request.AddressRegisterRequest;
import com.supercoding.hanyipman.dto.address.response.AddressListResponse;
import com.supercoding.hanyipman.dto.shop.buyer.response.ViewShopListResponse;
import com.supercoding.hanyipman.dto.vo.Response;
import com.supercoding.hanyipman.security.JwtToken;
import com.supercoding.hanyipman.service.AddressService;
import com.supercoding.hanyipman.utils.ApiUtils;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "회원 주소 API")
@RequestMapping("/api/addresses")
public class AddressController {


    private final AddressService addressService;

    //TODO 주소등록
    @PostMapping("/register")
    @Operation(summary = "주소 등록", description = "주소를 입력 추가를 하면 위도경도를 포함하여 저장한다")
    public Response<String> registerAddress(AddressRegisterRequest addressRegisterRequest) {
        String saveAddress = addressService.registerAddress(JwtToken.user(), addressRegisterRequest);

        return ApiUtils.success(HttpStatus.OK, saveAddress + " 주소가 추가되었습니다.", null);
    }

    // Todo 주소 목록 조회
    @GetMapping()
    @Operation(summary = "주소 조회", description = "최대 3까지 보유할 수 있다.")
    public Response<List<AddressListResponse>> getAddressList() {
        return ApiUtils.success(HttpStatus.OK, JwtToken.user().getEmail() + " 유저의 등록된 주소입니다.", addressService.getAddressList(JwtToken.user()));
    }

    // Todo 주소 수정
    @PatchMapping("/{address_id}")
    public Response<String> patchAddress(@PathVariable("address_id") Long addressId, AddressRegisterRequest addressRegisterRequest) {
        Long patchId = addressService.patchAddress(JwtToken.user(), addressId, addressRegisterRequest);
        return ApiUtils.success(HttpStatus.OK, patchId+"번 주소가 수정되었습니다.", null);
    }

    // Todo 주소 삭제
}
