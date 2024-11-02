package com.fashion.backend.constant;

public class Message {
	public static final String PASSWORD_VALIDATE = "Mật khẩu phải từ 6 đến 20 ký tự";
	public static final String EMAIL_VALIDATE = "Email không đúng định dạng";

	public static final String PHONE_VALIDATE = "Số điện thoại người dùng từ 10 đến 11 chữ số";
	public static final String USER_NOT_LOGIN = "Xin vui lòng đăng nhập để sử dụng chức năng";
	public static final String USER_NOT_HAVE_FEATURE = "Bạn không có quyền sử dụng chức năng này";
	public static final String USER_EXISTED = "Người dùng đã tồn tại.";
	public static final String TOKEN_NOT_EXIST = "Token không tồn tại. Xin vui lòng kiểm tra lại email";
	public static final String TOKEN_EXPIRED = "Token đã hết hạn. Xin vui lòng điền lại form quên mật khẩu";
	public static final String COMMON_ERR = "Đã có lỗi xảy ra. Xin hãy thử lại sau";
	public static final String JSON_ERR = "JSON không đúng định dạng";
	public static final String TIME_INVALID_FORMAT_DD_MM_YYYY = "Thời gian cần có định dạng dd/MM/yyyy";
	public static final String OTP_OVER_LIMIT
			= "Nhận thấy hành vi bất thường. Xin hãy liên hệ chúng tôi để được hỗ trợ thêm";
	public static final String OTP_VALIDATE = "OTP cần phải gồm 6 số";
	public static final String OTP_NOT_EXIST
			= "Số điện thoại này không được sử dụng để gửi OTP. Xin vui lòng kiểm tra lại";
	public static final String OTP_EXPIRED = "Mã OTP đã hết hạn. Xin hãy nhập lại số điện thoại để nhận mã OTP mới";
	public static final String DATE_VALIDATE = "Ngày đặt cần theo định dạng dd/MM/yyyy";
	public static final String TIME_FROM_TIME_TO_VALIDATE = "Thời gian không đúng qui định";

	public static class Order {
		public static final String ORDER_NOT_EXIST = "Đơn hàng không tồn tại trong hệ thống";
		public static final String ORDER_JUST_CAN_BE_REACHED_BY_OWNER_CUSTOMER
				= "Bạn không thể truy cập đến đơn hàng của khách hàng khác";
		public static final String CAN_NOT_BE_REACHED_CLOSED_ORDER = "Bạn không thể truy cập đến đơn hàng đã đóng";
		public static final String ORDER_CAN_NOT_HAVE_NO_ITEM = "Đơn hàng không được rỗng";
		public static final String ORDER_CAN_NOT_HAVE_SAME_ITEM = "Đơn hàng không được có 2 sản phẩm giống nhau";
		public static final String NORMAL_USER_CAN_NOT_CREATE_INVOICE_DIRECTLY
				= "Khách hàng không thể tự thanh toán hóa đơn";
		public static final String STAFF_CAN_NOT_BUY_ITEM = "Nhân viên không thể mua hàng";
	}

	public static class StockReport {
		public static final String FUTURE_DATE_INVALID = "Chưa tới thời điểm báo cáo";
	}

	public static class SaleReport {
		public static final String NO_ORDER_BETWEEN_TIME = "Không có đơn hàng nào trong khoảng thời gian này";
	}

	public static class Like {
		public static final String ITEM_NOT_IN_LIKED_LIST = "Sản phẩm không có trong danh sách sản phẩm yêu thích";
	}

	public static class Cart {
		public static final String QUANTITY_MIN_VALIDATE = "Số lượng sản phẩm được thêm ít nhất phải là 1";
		public static final String ITEM_NOT_IN_CART = "Sản phẩm không có trong giỏ hàng";
	}

	public static class Item {
		public static final String ITEM_NOT_EXIST = "Sản phẩm không tồn tại trong hệ thống";
		public static final String ITEM_IS_DELETED = "Sản phẩm đã bị xóa khỏi hệ thống";
	}

	public static class User {
		public static final String USER_IS_NOT_VERIFIED = "Người dùng chưa xác thực tài khoản";
		public static final String USER_IS_DELETED = "Người dùng đã bị xóa khỏi hệ thống";
		public static final String USER_NOT_EXIST = "Người dùng không tồn tại trong hệ thống";
		public static final String NAME_VALIDATE = "Tên người dùng không được trống và tối đa 200 ký tự";
		public static final String DOB_VALIDATE = "Ngày sinh không hợp lệ";
		public static final String ADDRESS_VALIDATE = "Địa chỉ không được để trống và có tối đa 50 ký tự";
		public static final String GENDER_VALIDATE = "Giới tính người dùng không được để trống";
		public static final String NAME_FILTER_VALIDATE = "Tên người dùng không được quá 200 ký tự";
		public static final String PHONE_FILTER_VALIDATE = "Số điện thoại người dùng không được quá 11 ký tự";
		public static final String OLD_PASSWORD_NOT_CORRECT = "Mật khẩu cũ không khớp";
		public static final String CAN_NOT_DELETE_YOURSELF = "Bạn không thể xóa chính bạn";
		public static final String CAN_NOT_UPDATE_YOURSELF = "Bạn không thể chỉnh sửa chính bạn";
		public static final String CAN_NOT_SEE_DETAIL_YOURSELF = "Bạn không thể xem chính bạn";
		public static final String CAN_NOT_REACH_CUSTOMER
				= "Bạn đang cố truy cập khách hàng chứ không phải nhân viên. Xin hãy kiểm tra lại";
		public static final String CAN_NOT_BE_LIKE_STAFF
				= "Bạn đang là khách hàng không phải nhân viên. Xin hãy dừng lại";
		public static final String CAN_NOT_BE_LIKE_CUSTOMER
				= "Bạn đang là nhân viên không phải khách hàng. Xin hãy dừng lại";
	}

	public static class Page {
		public static final String PAGE_VALIDATE = "Số trang phải lớn hơn 0";
		public static final String PAGE_LIMIT = "Số lượng hiển thị phải lớn hơn 0";
	}

	public static class SMS {
		public static final String SMS_SEND_FAIL = "Đã có lỗi xảy ra đối với việc gửi tin nhắn. Xin hãy thử lại sau";
	}

	public static class File {
		public static final String FILE_UPLOAD_FAIL = "Đã có lỗi xảy ra đối với việc upload file. Xin hãy thử lại sau";
		public static final String FILE_INVALID_FORMAT = "File không đúng định dạng. Xin hãy thử file khác";
	}

	public static class Auth {
		public static final String USER_NOT_CORRECT = "Tên đăng nhập hoặc mật khẩu chưa đúng. Xin hãy thử lại";
	}


	public static class Notification {
		public static final String TITLE_VALIDATE = "Tiêu đề bài viết không được để trống và có tối đa 100 ký tự";
		public static final String DESCRIPTION_VALIDATE = "Mô tả bài viết có tối đa 200 ký tự";
		public static final String NOTIFICATION_NOT_EXIST = "Thông báo không tồn tại";
		public static final String CAN_NOT_READ_OTHER_S_NOTIFICATION = "Không thể đọc thông báo của người khác";
	}
}
