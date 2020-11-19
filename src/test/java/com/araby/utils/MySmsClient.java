package com.araby.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import com.arabydeals.messages.MessagesResponse;
import com.arabydeals.messages.MobileMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MySmsClient {

	private static String MY_SMS_API_KEY = "pcervE-HEopWcVhQiXaNZQ";
	public static final String MYSMS_USERNAME = "+201553823792";
	public static final String MYSMS_PASSWORD = "1993";

	private static String loginToMySmsApi() {
		try {

			JSONObject payload = new JSONObject();
			// inside JSON object, declare the following properties apikey, msisdn, password
			payload.put("apiKey", MY_SMS_API_KEY);
			payload.put("msisdn", MYSMS_USERNAME);
			payload.put("password", MYSMS_PASSWORD);

			HttpEntity httpEntity = new StringEntity(payload.toString());
			String response = Request.Post("https://api.mysms.com/rest/user/login").body(httpEntity)
					.addHeader("Content-Type", "application/json").addHeader("accept", "application/json").execute()
					.returnContent().asString();

			return new JSONObject(response).getString("authToken");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Get all messages from the mentioned mobile no.
	public List<MobileMessage> getMessages(final String sender, final String query) throws IOException {
		// https://api.mysms.com/rest/user/message/conversations/get

		String authToken = loginToMySmsApi();

		JSONObject payload = new JSONObject();

		payload.put("apiKey", MY_SMS_API_KEY);
		payload.put("authToken", authToken);
		if (query != null) {
			payload.put("query", query);
		}

		HttpEntity httpEntity = new StringEntity(payload.toString());
		// Save all SMSes in the response variable
		String response = Request.Post("https://api.mysms.com/rest/user/message/search").connectTimeout(10000)
				.socketTimeout(10000).addHeader("Content-Type", "application/json")
				.addHeader("accept", "application/json").body(httpEntity).execute().returnContent().asString();
		// Create objectMapper
		ObjectMapper mapper = new ObjectMapper();
		MessagesResponse messagesResponse = mapper.readValue(response, MessagesResponse.class);
		if (messagesResponse.getErrorCode() != 0) {
			throw new RuntimeException(
					"Error when fetching conversations, with code " + messagesResponse.getErrorCode());
		}
		return messagesResponse.getMessages().stream().filter(msg -> msg.getAddress().equals(sender))
				.filter(msg -> msg.getIncoming().equals(Boolean.TRUE)).collect(Collectors.toList());
	}

//Get the
	public static String getCurrentPinCode(String sender) throws IOException {
		MySmsClient client = new MySmsClient();
		List<MobileMessage> messages = client.getMessages(sender, "Araby");

		// assume test case register
		List<MobileMessage> msgs = messages.stream().filter(msg -> msg.getMessage().startsWith("أدخل الكود التالي")
				|| msg.getMessage().startsWith("لقد تم اشتراكك بنجاح")
				|| msg.getMessage().startsWith("استخدم الكود التالي") || msg.getMessage().contains("ArabyDeals"))
				.filter(msg -> LocalDateTime.ofInstant(msg.getDateSent().toInstant(), ZoneId.systemDefault())
						.isAfter(LocalDateTime.now().minusMinutes(11)))
				.collect(Collectors.toList());

		// LocalDateTime.ofEpochSecond();
		// [Filter by date: whitin last 10 minutes], [Filter ]
		if (msgs.isEmpty()) {
			return null;
		}

		MobileMessage message = (MobileMessage) msgs.get(0);

		Matcher m = Pattern.compile("[0-9]{4}").matcher(message.getMessage());
		if (!m.find())
			return null;

		// get pin code
		return m.group(0);

	}

	@Nullable
	public static String getLoginUrlFromWelcomeMessage(String sender) throws IOException {
		MySmsClient client = new MySmsClient();
		List<MobileMessage> welcomeMessages = client.getMessages(sender, "Araby");

		List<MobileMessage> welcomeMsg = welcomeMessages.stream()
				.filter(welcome -> welcome.getMessage().contains("araby.deals"))
				// .filter(welcome -> LocalDateTime.ofInstant(welcome.getDateSent().toInstant(),
				// ZoneId.systemDefault())
				// .isAfter(LocalDateTime.now().minusMinutes(5)))
				.collect(Collectors.toList());
		MobileMessage message = welcomeMsg.get(0);
//To create a pattern, you must first invoke one of its public static compile() methods, which will then return a Pattern object
		Pattern pattern = Pattern
				.compile("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.," + "]*[\\w@?^=%&/~+#-])?\n");
		// Finds the next expression that matches the pattern
		Matcher m = Pattern.compile("https://araby.deals(.*)").matcher(message.getMessage());
		if (m.find()) {
			return m.group(0);
		}
		return null;
	}

	public static String getPasswordFromWelcomeMessage(String sender) throws IOException {

		MySmsClient client = new MySmsClient();
		List<MobileMessage> welcomeMessages = client.getMessages(sender, "Adeals");

		List<MobileMessage> welcomeMsg = welcomeMessages.stream()
				.filter(welcome -> welcome.getMessage().contains("araby.deals")).collect(Collectors.toList());
		MobileMessage message = welcomeMsg.get(0);

		Matcher m = Pattern.compile("([0-9]{4})").matcher(message.getMessage());
		if (m.find()) {
			return m.group(1);
		}
		return null;

	}
	/*
	 * public static void unsub() throws IOException { final String
	 * sendMessageApiUrlParamtereized =
	 * "https://api.mysms.com/json/message/send?api_key='%s'&msisdn='%s'&password='%s'&recipient='%s'&message='%s'";
	 * String sendMessageApiUrl = String.format(sendMessageApiUrlParamtereized,
	 * MY_SMS_API_KEY, MYSMS_USERNAME,MYSMS_PASSWORD, Constant.BR_STC_SENDER_UNSUB,
	 * "UNSUB%20Agames");
	 * 
	 * String authToken = loginToMySmsApi();
	 * 
	 * JSONObject payload = new JSONObject();
	 * 
	 * payload.put("apiKey", MY_SMS_API_KEY); payload.put("authToken", authToken);
	 * Request.Get(sendMessageApiUrl).execute();
	 * 
	 * }
	 */
}
