package com.advantal.utils;

public class Constant {

//	 ---------------------------- THIRD PARTY API KEY ---------------------------------
//	public static final String API_KEY_VALUE = "dec5774dc4ea4b21a425d1ddb0a99ff2";//
	public static final String API_KEY_VALUE = "67e8091a7e1419da6d82f2d1869ef63c";//
	public static final String TS_API_KEY_VALUE = "0562240a5d7244aa8261b7c09a9e475c";
	public static final String NEWS_API_TOKEN_VALUE = "AMqEE0xhOHaq67zFfySEE8a0geEMQWbP5rIBaejt";

//	 ---------------------------- THIRD PARTY API BASE URL ---------------------------------
//	public static final String BASE_URL = "https://api.twelvedata.com";
	public static final String BASE_URL = "https://financialmodelingprep.com/api/v3";
	public static final String FMPP_BASE_URL_V4 = "https://financialmodelingprep.com/api/v4";
	public static final String TS_BASE_URL = "https://api.tokeninsight.com";
	public static final String NEWS_BASE_URL = "https://api.marketaux.com";

//	 ---------------------------- TWELVE DATA THIRD PARTY API END-POINTS ---------------------------------
	public static final String PROFILE_ENDPOINT = "/profile/";
	public static final String STOCK_LIST_ENDPOINT = "/stock/list?";
//	public static final String STOCK_LIST_SAUDI_ENDPOINT = "/quotes/sau?";//
	public static final String STOCK_LIST_SAUDI_ENDPOINT = "/private/saudi-details/full?";//
	public static final String EXCHANGE_LIST_ENDPOINT = "/exchanges?";
	public static final String INDICES_LIST_ENDPOINT = "/quotes/index?";
	public static final String QUOTE_ENDPOINT = "/quote/";
	public static final String STATISTICS_ENDPOINT = "/company-outlook?";
	public static final String LOGO_ENDPOINT = "/logo?";
//	public static final String MARKET_MOVERS_ENDPOINT = "/market_movers/stocks?";
	public static final String GAINERS_ENDPOINT = "/stock_market/gainers?";//
	public static final String LOOSERS_ENDPOINT = "/stock_market/losers?";//
//	public static final String TIME_SERIES_ENDPOINT = "/time_series?";
	public static final String TIME_SERIES_ENDPOINT = "/historical-chart/";
	public static final String EOD_ENDPOINT = "/eod?";
	public static final String SYMBOL_SEARCH = "/symbol_search?";
//	public static final String MARKET_STATE_ENDPOINT = "/market_state?";
	public static final String MARKET_STATE_ENDPOINT = "/is-the-market-open?";
	public static final String FIRST_AVAILABLE_STOCK_DATE_ENDPOINT = "/earliest_timestamp?";
	public static final String TS_NEWLY_LISTED_ENDPOINT = "/api/v1/coins/list/newly-listed?";
	public static final String API_USAGE_ENDPOINT = "/api_usage?";
	public static final String PRICE_TARGET_ENDPOINT = "/price-target-consensus?";// key_executives?
//	public static final String KEY_EXECUTIVE_ENDPOINT = "/key_executives?";
	public static final String KEY_EXECUTIVE_ENDPOINT = "/key-executives/";
	public static final String DIVIDENDS_ENDPOINT = "/dividends?";
	public static final String RECOMMENDATIONS_ENDPOINT = "/analyst-stock-recommendations/";
	public static final String RATING_ENDPOINT = "/rating/";
	public static final String SHARE_HOLDERS_ENDPOINT = "/institutional-holder/";
	public static final String SEC_FILING_ENDPOINT = "/sec_filings/";

//	 ---------------------------- TOKEN INSIGHT THIRD PARTY API END-POINTS ---------------------------------
	public static final String CRYPTO_LIST_ENDPOINT = "/v1/cryptocurrency/listings/latest";
	public static final String TS_CRYPTO_LIST_ENDPOINT = "/api/v1/simple/price?";
	public static final String TS_CRYPTO_DETAIL_ENDPOINT = "/api/v1/coins/";
	public static final String TS_CRYPTO_RATING_ENDPOINT = "/api/v1/rating/coin/";
	public static final String TS_CRYPTO_SAVE_LIST_ENDPOINT = "/api/v1/coins/";
	public static final String TD_QUOTE_ENDPOINT = "/quote?";
	public static final String TS_CRYPTO_GLOBAL_STATUS = "/api/v1/global?";
	public static final String TS_GLOBAL_CRYPTO_NEWS_END = "/api/v1/news/list?";
	public static final String TS_CRYPTO_TOP_GAINERS_END = "/api/v1/coins/top-gainers";
	public static final String TS_CRYPTO_TOP_LOSERS_END = "/api/v1/coins/top-losers";
	public static final Object INVALID_REQUEST = "Invalid Request !!";
	public static final String TS_CRYPTO_EXCHANGE_LIST_ENDPOINT = "/api/v1/exchanges/list?cex=false&dex=false&spot=false&derivatives=false";

//	 ---------------------------- MARKET AUX THIRD PARTY API END-POINTS ---------------------------------
	public static final String NEWS_END_POINT = "/v1/news/all?";

//	 ---------------------------- IMPORTANT PARAMS ---------------------------------
	public static final String UNITED_STATES = "united states";
	public static final String SAUDI_ARABIA = "saudi arabia";
	public static final String SOURCE = "docs";
	public static final String TS_LIMIT = "limit=";
	public static final String TS_OFFSET = "offset=";
	public static final String TS_LIMIT_VALUE = "1500";
	public static final String TS_OFFSET_VALUE = "3000";
	public static final String MARKET_OPEN_CLOSE_DATETIME = "";
	public static final String X_CMC_PRO_API_KEY = "X-CMC_PRO_API_KEY";
	public static final String TS_API_KEY = "TI_API_KEY";
	public static final String TS_IDS = "ids=";
	public static final String TS_PRICE_CHANGE_PERCENTAGE = "&include_price_change_percentage_24h=true";
	public static final String GAINERS = "gainers";
	public static final String LOSERS = "losers";
	public static final String AL_RAJHI_COMPLIANCE = "Al Rajhi Compliance";
	public static final String SAUDI_AAOIFI_COMPLIANCE = "Saudi AAOIFI Compliance";
	public static final String USA_AAOIFI_COMPLIANCE = "USA AAOIFI Compliance";

//	 ---------------------------- TIME SERIES INTERVALS ---------------------------------
	public static final String ONE_MINUTE = "1min";
	public static final String FIVE_MINUTE = "5min";
	public static final String FIFTEEN_MINUTE = "15min";
	public static final String THIRTY_MINUTE = "30min";
	public static final String ONE_HOUR = "1hour";
	public static final String FOUR_HOUR = "4hour";
	public static final String ONE_DAY = "1day";
	public static final String ONE_WEEK = "1week";
	public static final String ONE_MONTH = "1month";
	public static final String THREE_MONTH = "3month";
	public static final String SIX_MONTH = "6month";
	public static final String ONE_YEAR = "1year";
	public static final String FIVE_YEAR = "5year";
	public static final String ALL = "all";

	/* 1=active/unblocked, 0=deactive/delete, 2=blocked */
	public static final Short ZERO = 0;
	public static final Short ONE = 1;
	public static final Short TWO = 2;
	public static final Boolean FALSE = false;
	public static final Short THREE = 3;
	public static final Boolean TRUE = true;
	public static final String ACTIVATE = "activate";
	public static final String TYPE_ACTIVATE = "kHJIqVoRke6QPOT/nSXKrw==";
	public static final String SEND_MAIL_TIME = "2023-03-06 22:21:25";

//	------------------------------------------ End OTHER CONSTANT -------------------------------------------

//------------------------------------------ STATUS CODE -------------------------------------
	public static final String CREATE = "201";
	public static final String OK = "200";
	public static final String BAD_REQUEST = "400";
	public static final String NOT_AUTHORIZED = "401";
	public static final String FORBIDDEN = "403";
	public static final String WRONGEMAILPASSWORD = "402";
	public static final String NOT_FOUND = "404";
	public static final String SERVER_ERROR = "500";
	public static final String DB_CONNECTION_ERROR = "502";
	public static final String ENCRYPTION_DECRYPTION_ERROR = "503";
	public static final String NOT_EXIST = "405";
	public static final String CONFLICT = "409";

//--------------------------------------END STATUS CODE---------------------------------------

//--------------------------------------- RESPONSE KEY ---------------------------------------
	public static final String RESPONSE_CODE = "responseCode";
	public static final String OBJECT = "object";
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String AUTH_KEY = "authKey";
	public static final String API_KEY = "apikey=";
	public static final String TOKEN_INSIGHT_API_KEY = "TI_API_KEY";
	public static final String MESSAGE = "message";
	public static final String DATA = "data";
	public static final String TOKEN = "token";

//-------------------------------------- END RESPONSE KEY ---------------------------------------	

//--------------------------------------- RESPONSE MESSAGES ----------------------------------------
// ----------------------- Common message ------------------------
	public static final String BAD_REQUEST_MESSAGE = "Bad request!!";
	public static final String ERROR_MESSAGE = "Please try again!!";
	public static final String RECORD_NOT_FOUND_MESSAGE = "Record not found!!";
	public static final String RECORD_FOUND_MESSAGE = "Record found!!";
	public static final String RECORD_SAVED_MESSAGE = "Record saved successfully!!";
	public static final String SERVER_MESSAGE = "Technical issue";
	public static final String PAGE_SIZE_MESSAGE = "Page size can't be zero, it should be more then zero!!";
	public static final String PAGE_LIMIT_MESSAGE = "limit and page can't be zero, it should be more then zero!!";
	public static final String ALREADY_DELETED_MESSAGE = "Already deleted!!";
	public static final String DELETED_MESSAGE = "Deleted successfully!!";
	public static final String ID_NOT_FOUND_MESSAGE = "Given id not found into the database!!";
	public static final String RECORD_BLOCKED_OR_DELETED_MESSAGE = "Record not found, because it may be blocked or deleted!!";
	public static final String PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE = "Page size and Page index can't be null!!";
	public static final String DATA_FOUND = "Data found";
	public static final String RECORD_NOT_UPDATED_MESSAGE = "Record not updated because, given id not found into the database!!";
	public static final String ID_CAN_NOT_NULL_MESSAGE = "Id can not null, it should be valid!!";
	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "There is an error on the server-side. Try again later";
	public static final String DATA_FOUND_MESSAGE = "Data found!!";
	public static final String NO_DB_SERVER_CONNECTION = "The server was found but the connection to its local database was not possible.";
	public static final String DATA_NOT_FOUND_MESSAGE = "Data not found!!";
	public static final String STATUS_VALUE_INVALID_MESSAGE = "Status value should be either 0 for make favorite or 1 for remove from favorite !!";
	public static final String SYMBOL_NOT_BLANK = "Symbol can't be blank or empty!!";
	public static final String STATUS_INVALID_MESSAGE = "Invalid status !!";
	public static final String CRYPTO_ID_NOT_EMPTY = "Crypto id can't be null";
	public static final String INVALID_COUNTRY = "Invalid country please enter the only US and SA country";
	public static final String NO_SERVER_CONNECTION = "The server was found but the connection to its local database was not possible.";
	public static final String SYMBOL_AND_COUNTRY_NOT_BLANK = "Symbol and Country can't be blank or empty!!";
	public static final String COUNTRY_NOT_BLANK = "Country can't be blank or empty!!";
	public static final String THIRD_PARTY_SERVER_ERROR_MESSAGE = "Error on the third party server-side.";
	public static final String CRYPTO_EXCHANGE_LIST_SAVE_SUCCESSFULLY = "Crypto exchange list save successfully !!";
	public static final String INVALID_TRANSACTION_TYPE = "Invalid credentials !!";
	public static final String TRANSACTION_SUCESSFULLY = "Transaction saved seccussfully !!";
	public static final String TRANSACTION_UPDATED_SUCESSFULLY = "Transaction updated successfully";
	public static final String TRANSACTION_DELETED_SUCESSFULLY = "Transaction deleted successfully";
	public static final String INVALID_USER_REQUEST_MESSAGE = "Invalid user request !!";
	public static final String DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE = "Data not available on the third party server !!";
	public static final String MARKET_WEEKOFF_DATA_NOT_AVAILABLE_MESSAGE = "Market weekly off data not available into the data base!!";
	public static final String BY = "By";

// ------------------------ Stock message ------------------------
	public static final String JSON_PARSE_EXCEPTION_MESSAGE = "JSON parse exception!!";
	public static final String RECORD_NOTSAVED_MESSAGE = "Record not saved because, not getting data from third party service!!";
	public static final String COUNTRY_NAME_NOT_BLANK_MESSAGE = "Country name can't be blank!!";
	public static final String SAVED_FAVORITE_MESSAGE = "Added into your favorite list!!";
	public static final String ALREADY_FAVORITE_MESSAGE = "Already added into your favorite list!!";
	public static final String REMOVED_FAVORITE_MESSAGE = "Removed from your favorite list!!";
	public static final String BOTH_CANT_NULL_MESSAGE = "Either CryptoId or StockId can be null both can't be null at same time!!";
	public static final String INSTRUMENT_TYPE_INVALID_MESSAGE = "Instrument type should be valid, it may stock or crypto!!";
	public static final String TS_NEWS_ENDPOINT = "/api/v1/rating/coin/";
	public static final String TS_CRYPTO_TIME_SERIES_ENDPOINT = "/api/v1/history/coins/";
	public static final String TS_CRYPTO_LENGTH = "&length=";
	public static final String TS_CRYPTO_INTERVAL = "interval=";
	public static final String UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE = "Unable to remove because, given instrument not found into the database !!";
	public static final String IRRELEVANT_INSTRUMENT_ID_AND_CRYPTO_ID_MESSAGE = "Given instrumentId and cryptoId are irrelevant !!";
	public static final String IRRELEVANT_INSTRUMENT_ID_AND_SYMBOL_MESSAGE = "Given instrumentId and symbol are irrelevant !!";
	public static final String INCORRECT_COMPLIANCE_VALUE_MESSAGE = "filterByShariahCompliance value should be 1 or 0 !!";

//	public static final String USER_NOT_FOUND_MESSAGE = "Given userId not found into the database !!";
	public static final String USER_ID_NOT_FOUND_MESSAGE = "Given user id not found into the database!!";
	public static final String LANGAUGE = "&language=";
	public static final String COUNTRY = "&country=";
	public static final String NEWS_API_TOKEN_KEY = "&api_token";
	public static final String PUBLISHED_BEFORE = "&published_before=";
	public static final String PUBLISHED_AFTER = "&published_after=";
	public static final String INVALID_DATE_FORMAT = "Invalid date format !!";

	public static final String INSTRUMENT_NOT_FOUND_MESSAGE = "Given instrument not found into the database !!";
	public static final String COUNTRY_NOT_FOUND_MESSAGE = "Country not found into the database !!";
	public static final String INSTRUMENT_NOT_FOUND_COUNTRY_NOT_MATCHED_MESSAGE = "Instrument not found because country not matched from database !!";
	public static final String STOCK_LIST_EMPTY_MESSAGE = "Stock list is empty !!";
	public static final String COUNTRY_LIST_EMPTY_MESSAGE = "Country list is empty !!";
	public static final String TOP_GAINERS_LOSERS = "cryptoResponseList";
	public static final String EXCHANGE_LIST_EMPTY_MESSAGE = "Exchange list is empty !!";
	public static final String SYMBOL_NOT_FOUND = "Given symbol not found !!";
	public static final String STOCK_NEWS_ENDPOINT = "https://financialmodelingprep.com/api/v3/stock_news?";
	public static final String PAGE = "page=";
	public static final String INDICES_ADDED = "Indices added successfully !!";
	public static final String INDICES_DISABLE = "Indices disable successfully !!";
	public static final String INDICES_ENABLE = "Indices enable successfully !!";
	public static final String INDICES_UPDATED = "Indices updated successfully !!";
	public static final String COUNTRY_AND_EXCHANGE_ALREADY = "Given country & exchange already present in our database !!";
	public static final String COUNTRY_ADDED = "country added successfully !!";
	public static final String COUNTRY_UPDATE = "Country updated successfully !!";
	public static final String EXCHNAGE_ADDED = "Exchange added successfully !!";
	public static final String EXCHANGE_UPDATE = "Exchange updated successfully !!";
	public static final String EXCHANGE_DISABLE = "Exchange disable successfully !!";
	public static final String EXCHANGE_ENABLE = "Exchange enable successfully !!";
	public static final String INVALID_TYPE = "Invalid type. Please enter the valid type";
	public static final String DIVIDENDS_CALENDAR_ENDPOINT = "/stock_dividend_calendar?";
	public static final String EARNING_CALENDAR_ENDPOINT = "/earning_calendar?";
	public static final String ECONOMIC_CALENDAR_ENDPOINT = "/economic_calendar?";
	public static final String IPOS_CALENDAR_ENDPOINT = "/ipo_calendar?";
	public static final String STATISTICS_SECOND_ENDPOINT = "/key-metrics/";
	public static final String STATISTICS_THIRD_ENDPOINT = "/ratios/";
	public static final String TS_CRYPTO_GRAPH_DATA_ENDPOINT = "/api/v1/history/coins/";
	public static final String CRYPTO_GRAPH_DATA_FOUND_SUCESSFULLY = "Crypto graph data found successfully !!";
	public static final String SAUDI_PROFILE_ENDPOINT = "/private/saudi-details/full?";

	// ========================== File message ============================
	public static final String FILE_UPLOAD_SUCCESS_MESSAGE = "New file uploaded sucessfully!!";
	public static final String UPDATED_FILE_UPLOAD_SUCCESS_MESSAGE = "Updated file uploaded sucessfully!!";
	public static final String FILE_UPLOAD_FAILED_MESSAGE = "File uploading failed! please try again!!";
	public static final String NO_CHANGES_FOUND_IN_FILE_MESSAGE = "No Changes found in local file! so you can't upload the file!!";
	public static final String NOT_A_EXCEL_FILE_MESSAGE = "This file is not an excel file!!";
	public static final String NOT_A_CSV_FILE_MESSAGE = "The file Formate should be CSV type(Ex: .csv)!!";
	public static final String NOT_IN_FORMATE_MESSAGE = "This file is not in proper formate!!";
	public static final String DATA_IMPORT_SUCCESS_MESSAGE = "Data imported sucessfully!!";
	public static final String ALREADY_EXIT_MESSAGE = "Already exit !!";
	public static final String NOT_DOWNLOAD_FILE_MESSAGE = "Not able to download the file, because no record found on the database!";


	// ========================== Holiday message ============================
	public static final Object HOLIDAY_SAVED_MESSAGE = "Holiday data saved successfully !!";
	public static final Object HOLIDAY_UPDATED_MESSAGE = "Holiday data updated successfully !!";
	public static final Object COUNTRY_ID_CANT_BE_ZERO_MESSAGE = "Country id should be greater than zero !!";
	public static final Object HOLIDAY_ALREADY_EXIST_MESSAGE = "Given holiday already exist into the database !!";
	
	
	public static final String PORTFOLIO_ID_NOT_FOUND_MESSAGE = "Given portfolio id not found into the database!!";
	
//----------------------------------------- END RESPONSE MESSAGES ----------------------------------------	

}
