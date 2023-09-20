package com.supercoding.hanyipman.dto.order.response;

import com.supercoding.hanyipman.entity.Order;
import com.supercoding.hanyipman.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사장님 가게 주문 조회")
public class OrderNoticeSellerResponse {
    @ApiModelProperty(value="주문 식별값 필드", dataType = "Long")
    private Long orderId;
    @ApiModelProperty(value="주문 UID 필드", dataType = "String")
    private String orderUId;
    @ApiModelProperty(value="주문 상태 필드", dataType = "String")
    private String orderStatus;
    @ApiModelProperty(value="가게 이름", dataType = "String")
    private String shopName;
    @ApiModelProperty(value="주문 메뉴 필드", dataType = "String")
    private String menuNames;
    @ApiModelProperty(value="총 가격 필드", dataType = "Integer")
    private Integer totalAmount;
    @ApiModelProperty(value = "주소", dataType = "String")
    private String address;
    @ApiModelProperty(value="주문시간", dataType = "Instant")
    private String orderedTime;




    public static OrderNoticeSellerResponse from(Order order) {

        return OrderNoticeSellerResponse.builder()
                .orderId(order.getId())
                .orderUId(order.getOrderUid())
                .orderStatus(order.getOrderStatus().name())
                .shopName(order.getShop().getName())
                .menuNames(setMenuNames(order))
                .totalAmount(order.getTotalPrice())
                .address(order.getAddress().getAddress())
                .orderedTime(DateUtils.convertToString(order.getCreatedAt()))
                .build();
    }
    public static String setMenuNames(Order order) {
        String menuNames = order.getCarts().get(0).getMenu().getName();
        if(order.getCarts().size() >= 2) {
            return menuNames + " 외 " + (order.getCarts().size()-1) + "개";
        }
        return menuNames;
    }

}
