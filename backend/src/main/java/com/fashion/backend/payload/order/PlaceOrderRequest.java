package com.fashion.backend.payload.order;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class PlaceOrderRequest {
	@Schema(
			name = "name",
			example = "user name"
	)
	@Length(
			min = 1,
			max = 200,
			message = Message.User.NAME_VALIDATE
	)
	@NotNull(message = Message.User.NAME_VALIDATE)
	private String name;

	@Schema(
			name = "phone",
			example = "0123456789"
	)
	@Pattern(
			regexp = "\\d{10,11}",
			message = Message.PHONE_VALIDATE
	)
	@NotNull(message = Message.PHONE_VALIDATE)
	private String phone;

	@Schema(
			name = "address"
	)
	@Length(
			min = 1,
			max = 50,
			message = Message.User.ADDRESS_VALIDATE
	)
	@NotNull(message = Message.User.ADDRESS_VALIDATE)
	private String address;

	@Schema(
			name = "cardIds"
	)
	@NotNull(message = Message.Order.ORDER_CAN_NOT_HAVE_NO_ITEM)
	private List<Long> cardIds;
}
