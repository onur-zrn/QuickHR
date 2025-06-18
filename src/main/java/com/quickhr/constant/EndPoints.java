package com.quickhr.constant;

public class EndPoints {
	public static final String VERSION = "/v1";
	public static final String API = "/api";
	public static final String DEV = "/dev";
	public static final String TEST = "/test";
	public static final String PROD = "/prod";
	public static final String AUTH = "/auth";
	public static final String ROOT = VERSION + DEV;

	// Classes - Controller
	public static final String ADMIN = API + "/admin";
	public static final String COMPANY = API + "/company";
	public static final String EMPLOYEE = API + "/employee";
	public static final String USER = API + "/users";
	public static final String PUBLIC_API = API + "/public-api";
	public static final String AUTHENTICATION = API + AUTH;

	// Auth
	public static final String REGISTER = AUTHENTICATION + "/register"; // api/auth/register
	public static final String LOGIN = AUTHENTICATION + "/login"; // api/auth/login
	public static final String VERIFY_MAIL = AUTHENTICATION + "/verify-mail"; // api/auth/verify-mail
	public static final String FORGOT_PASSWORD = AUTHENTICATION + "/forgot-password"; // api/auth/forgot-password
	public static final String RESET_PASSWORD = AUTHENTICATION + "/reset-password"; // api/auth/reset-password
	public static final String LOGOUT = AUTHENTICATION + "/logout"; // api/auth/logout
	public static final String REFRESH_ACCESS_TOKEN  = AUTHENTICATION + "/refresh_access_token"; // api/auth/refresh_authentication_token

	// Public Api
	public static final String HOMEPAGE_CONTENT =  PUBLIC_API + "/homepage-content"; // api/public-api/homepage-content
	public static final String PLATFORM_FEATURES = PUBLIC_API + "/platform-features"; // api/public-api/platform-features
	public static final String HOW_IT_WORKS = PUBLIC_API + "/how-it-works"; // api/public-api/how-it-works
	public static final String HOLIDAYS = PUBLIC_API + "/holidays"; // api/public-api/holidays
	public static final String CURRENT_YEAR_HOLIDAYS = PUBLIC_API + "/current-year-holidays"; // api/public-api/current-year-holidays
	public static final String YEAR_HOLIDAYS = PUBLIC_API + "/year-holidays"; // api/public-api/year-holidays

	// Admin
	public static final String ADMIN_DASHBOARD = ADMIN + "/dashboard"; // api/admin/dashboard
	public static final String ADMIN_LOGIN = ADMIN + "/admin-login"; // api/admin/admin-login
	public static final String PENDING_COMPANY = ADMIN + "/pending-company"; // api/admin/pending-company
	public static final String ACCEPTED_COMPANY = ADMIN + "/accepted-company"; // api/admin/accepted-company
	public static final String CHANGE_COMPANY_STATUS = ADMIN + "/change-company-status"; // api/admin/change-company-status
	public static final String ADMIN_DEACTIVATE = ADMIN + "/admin-deactivate"; // api/admin/deactivate
	public static final String DENIED_COMPANY = ADMIN + "/denied-company"; // api/admin/denied-company
	public static final String DELETED_COMPANY = ADMIN + "/deleted-company"; // api/admin/deleted-company
	public static final String FIND_ALL_COMPANY = ADMIN + "/find-all-company"; // api/admin/find-all-company
	public static final String IS_ACCEPTED_COMPANY = ADMIN + "/is-accepted-company"; // api/admin/is-accepted-company

	// Company
	public static final String COMPANY_DASHBOARD = COMPANY + "/dashboard"; // api/company/dashboard

	// Employee
	public static final String EMPLOYEE_DASHBOARD = EMPLOYEE + "/dashboard"; // api/employee/dashboard

	// User
	public static final String USER_PROFILE = USER + "/profile"; // GET /api/users/profile, PUT /api/users/profile
	public static final String USER_CHANGE_EMAIL = USER + "/change-email"; // PUT /api/users/change-email
	public static final String USER_VERIFY_EMAIL = USER + "/verify-email"; // PUT /api/users/change-email
	public static final String USER_CHANGE_PASSWORD = USER + "/change-password"; // PUT /api/users/change-password
	public static final String USER_DEACTIVATE = USER + "/deactivate"; // PUT /api/users/deactivate


	public static final String PERSONALS = COMPANY + "/personals"; // GET api/company/personals
	public static final String PERSONAL_DETAILS = COMPANY + "/personal-details/{id}"; // GET api/company/personals-details/{id}
	public static final String ADD_PERSONAL = COMPANY + "/add-personal"; // POST api/company/add-personal
	public static final String UPDATE_PERSONAL = COMPANY + "/update-personal/{id}"; // PUT api/company/update-personal/{id}
	public static final String DELETED_PERSONAL = COMPANY + "/deleted-personal"; // GET /api/company/deleted-personal
	public static final String ACTIVE_PERSONAL = COMPANY + "/active-personal"; // GET /api/company/active-personal
	public static final String PASSIVE_PERSONAL = COMPANY + "/inactive-personal"; // GET /api/company/inactive-personal
	public static final String PENDING_PERSONAL = COMPANY + "/pending-personal"; // GET /api/company/pending-personal
	public static final String MAKE_DELETED_PERSONAL = COMPANY + "/employee/{id}/deleted"; // DELETE /api/company/employee/{id}/deleted
	public static final String MAKE_PERSONAL_STATUS_IN_ACTIVE = COMPANY + "/employee/{id}/active"; // PUT /api/company/employee/{id}/active
	public static final String MAKE_PERSONAL_STATUS_IN_PASSIVE = COMPANY + "/employee/{id}/passive"; // PUT /api/company/employee/{id}/passive
	public static final String CHANGE_PERSONAL_STATUS = COMPANY + "/employee/{id}/change-personal-status"; // PUT /api/company/employee/{id}/change-personal-status

	public static final String UPDATE_PERSONAL_PROFILE = EMPLOYEE + "/update"; // PUT api/company/update

	//IZINLER
	public static final String LEAVES = EMPLOYEE + "/leaves"; //GET /api/employee/leaves , POST /api/employee/leaves
	public static final String LEAVES_DETAIL = EMPLOYEE + "/leaves/{id}"; //GET /api/employee/leaves/{id}
	public static final String LEAVES_BALANCE = EMPLOYEE + "/leaves/balance"; //GET /api/employee/leaves/balance

	public static final String ANNUAL_LEAVE_DETAILS = EMPLOYEE + "/annual-leave/details"; // GET /api/employee/annual-leave/details  - Yıllık izin kullanım detayları


}
