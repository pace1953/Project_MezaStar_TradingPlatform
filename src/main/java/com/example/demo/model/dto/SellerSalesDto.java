package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerSalesDto {
	// 總訂單數
	private Integer totalOrdersAmount;
	// 總售出物品數量
	private Integer totalItemsSold;
	// 總售出金額
	private int totalSoldPrice;
	
}
