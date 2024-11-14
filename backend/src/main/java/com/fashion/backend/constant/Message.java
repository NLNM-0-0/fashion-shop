package com.fashion.backend.constant;

public class Message {
	public static final String PASSWORD_VALIDATE = "Password must be between 6 to 20 characters";
	public static final String EMAIL_VALIDATE = "Invalid email format";

	public static final String PHONE_VALIDATE = "User's phone number must be between 10 to 11 digits";
	public static final String USER_NOT_LOGIN = "Please log in to use this feature";
	public static final String USER_NOT_HAVE_FEATURE = "You do not have permission to use this feature";
	public static final String TOKEN_NOT_EXIST = "Token does not exist. Please check your email";
	public static final String TOKEN_EXPIRED = "Token has expired. Please fill out the forgot password form again";
	public static final String COMMON_ERR = "An error occurred. Please try again later";
	public static final String JSON_ERR = "Invalid JSON format";
	public static final String OTP_OVER_LIMIT
			= "Abnormal behavior detected. Please contact us for further assistance";
	public static final String OTP_VALIDATE = "OTP must consist of 6 digits";
	public static final String OTP_NOT_EXIST
			= "The OTP you entered is incorrect. Please try again";
	public static final String OTP_EXPIRED
			= "The OTP has expired. Please enter your phone number again to receive a new OTP";
	public static final String TIME_FROM_TIME_TO_VALIDATE = "Time is not within the valid range";

	public static class ItemQuantity {
		public static String ITEM_QUANTITY_NOT_EXIST = "Product with that color and size does not exist in the system";
	}

	public static class Order {
		public static final String ORDER_NOT_EXIST = "Order does not exist in the system";
		public static final String ORDER_JUST_CAN_BE_REACHED_BY_OWNER_CUSTOMER
				= "You cannot access another customer's order";
		public static final String CAN_NOT_BE_REACHED_CLOSED_ORDER = "You cannot access a closed order";
		public static final String ORDER_CAN_NOT_HAVE_NO_ITEM = "Order cannot be empty";
		public static final String ORDER_ITEM_MUST_HAVE_COLOR = "Order information must include color";
		public static final String ORDER_ITEM_MUST_HAVE_SIZE = "Order information must include size";
		public static final String ORDER_CAN_NOT_HAVE_SAME_ITEM = "Order cannot have two identical products";
	}

	public static class StockReport {
		public static final String FUTURE_DATE_INVALID = "Report date is in the future";
	}

	public static class SaleReport {
		public static final String NO_ORDER_BETWEEN_TIME = "No orders found in this time period";
	}

	public static class Like {
		public static final String ITEM_NOT_IN_LIKED_LIST = "Item is not in the liked products list";
	}

	public static class Cart {
		public static final String QUANTITY_MIN_VALIDATE = "Cart item quantity must be at least 1";
		public static final String ITEM_NOT_IN_CART = "Item is not in the cart";
		public static final String CART_UPDATE_NEED_TO_CHANGE_QUANTITY
				= "Update card request must have changed quantity";
		public static final String CART_CAN_NOT_HAVE_NO_ITEM = "Cart item cannot be empty";
		public static final String CART_REQUEST_MUST_HAVE_CART_ITEM = "Add to card request must have item";
		public static final String CART_ITEM_MUST_HAVE_COLOR = "Cart item must include color";
		public static final String CART_ITEM_MUST_HAVE_SIZE = "Cart item must include size";
		public static final String CAN_NOT_ADD_OVER_CURRENT_QUANTITY = "Can not buy over current item's quantity";
	}

	public static class Category {
		public static final String NAME_VALIDATE
				= "Category name cannot be empty and must be a maximum of 200 characters";
		public static final String CAN_NOT_DELETE_CATEGORY_THAT_EXIST_ITEM_CONTAIN_ONLY_IT
				= "There are products that only belong to this category. Cannot delete";
		public static final String CATEGORY_NOT_EXIST = "Category does not exist in the system";
	}

	public static class Item {
		public static final String ITEM_NOT_EXIST = "Product does not exist in the system";
		public static final String ITEM_IS_DELETED = "Product has been deleted from the system";
		public static final String GENDER_VALIDATE = "Item must be identified for a specific gender";
		public static final String NAME_VALIDATE
				= "Product name cannot be empty and must be a maximum of 200 characters";
		public static final String CATEGORY_VALIDATE = "Product must have a category";
		public static final String SEASON_VALIDATE = "Product must have a season";
		public static final String SIZE_NAME_VALIDATE
				= "Size name cannot be empty and must be a maximum of 20 characters";
		public static final String COLOR_VALIDATE = "Product must have a color";
		public static final String COLOR_NAME_VALIDATE
				= "Color name cannot be empty and must be a maximum of 20 characters";
		public static final String COLOR_HEX_VALIDATE = "Hex code cannot be empty and must be exactly 6 characters";
		public static final String NAME_FILTER_VALIDATE = "Product name cannot exceed 200 characters";
		public static final String UNIT_PRICE_VALIDATE = "Product price cannot be empty and must be at least 0";
		public static final String QUANTITY_VALIDATE = "Product quantity cannot be empty and must be at least 0";
		public static final String QUANTITY_MIN = "Product quantity must be at least 0";
		public static final String CAN_NOT_FILTER_2_TYPE_PRICE_FILTER = "Cannot filter by two price types";
	}

	public static class User {
		public static final String USER_IS_NOT_VERIFIED = "User account is not verified";
		public static final String USER_IS_DELETED = "User has been deleted from the system";
		public static final String USER_EXIST = "User already exists in the system";
		public static final String USER_NOT_EXIST = "User does not exist in the system";
		public static final String NAME_VALIDATE = "User name cannot be empty and must be a maximum of 200 characters";
		public static final String DOB_VALIDATE = "Invalid date of birth";
		public static final String ADDRESS_VALIDATE = "Address cannot be empty and must be a maximum of 50 characters";
		public static final String GENDER_VALIDATE = "User's gender cannot be empty";
		public static final String NAME_FILTER_VALIDATE = "User name cannot exceed 200 characters";
		public static final String PHONE_FILTER_VALIDATE = "User's phone number cannot exceed 11 characters";
		public static final String OLD_PASSWORD_NOT_CORRECT = "Old password is incorrect";
		public static final String CAN_NOT_DELETE_YOURSELF = "You cannot delete yourself";
		public static final String CAN_NOT_UPDATE_YOURSELF = "You cannot update yourself";
		public static final String CAN_NOT_SEE_DETAIL_YOURSELF = "You cannot view your own details";
		public static final String CAN_NOT_REACH_CUSTOMER
				= "You are trying to access a customer, not a staff member. Please check again";
		public static final String CAN_NOT_REACH_STAFF
				= "You are trying to access a staff member, not a customer. Please check again";
	}

	public static class Page {
		public static final String PAGE_VALIDATE = "Page number must be greater than 0";
		public static final String PAGE_LIMIT = "Display limit must be greater than 0";
	}

	public static class SMS {
		public static final String SMS_SEND_FAIL
				= "An error occurred while sending the message. Please try again later";
	}

	public static class File {
		public static final String FILE_UPLOAD_FAIL
				= "An error occurred while uploading the file. Please try again later";
		public static final String FILE_INVALID_FORMAT = "Invalid file format. Please try a different file";
	}

	public static class Auth {
		public static final String USER_NOT_CORRECT = "Username or password is incorrect. Please try again";
	}

	public static class Notification {
		public static final String TITLE_VALIDATE
				= "Notification title cannot be empty and must be a maximum of 100 characters";
		public static final String DESCRIPTION_VALIDATE
				= "Notification description must be a maximum of 200 characters";
		public static final String NOTIFICATION_NOT_EXIST = "Notification does not exist";
		public static final String CAN_NOT_READ_OTHER_S_NOTIFICATION = "Cannot read another user's notifications";
	}
}
