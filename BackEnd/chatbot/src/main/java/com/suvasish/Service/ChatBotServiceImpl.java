package com.suvasish.Service;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.suvasish.Dto.CoinDto;
import com.suvasish.Response.ApiResponse;
import com.suvasish.Response.FunctionResponse;
@Service
public class ChatBotServiceImpl implements ChatBotService{
	String gemini_api_key="##########################################";
	private double convertToDouble(Object value) {
		if(value instanceof Integer) {
			return ((Integer)value).doubleValue();
		}
		else if(value instanceof Long) {
			return ((Long)value).doubleValue();
		}
		else if(value instanceof Double) {
			return (Double)value;
		}
		else throw new IllegalArgumentException("Uspported Type " + value.getClass().getName());
	}
	
	//the below method will fetch the market data from the coingecko api
	//and will return in the form of coindto form.
	public CoinDto makeApiRequest(String currencyName) throws Exception{
		String url="https://api.coingecko.com/api/v3/coins/"+currencyName;
		//we will create restTemplate object fire an http request to our url.
		RestTemplate restTemplate=new RestTemplate();
		HttpHeaders httpHeaders=new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Map> responseEntity=restTemplate.getForEntity(url, Map.class);
		Map<String,Object> responseBody=responseEntity.getBody();
		if(responseBody!=null) {
			//if our responseBody is not null we will map all our api data to dto object 
			Map<String,Object> image=(Map<String,Object>)responseBody.get("image");
			Map<String,Object> marketData=(Map<String,Object>)responseBody.get("market_data");
			CoinDto coinDto=new CoinDto();
			coinDto.setId((String)responseBody.get("id"));
			coinDto.setName((String)responseBody.get("name"));
			coinDto.setSymbol((String)responseBody.get("symbol"));
			coinDto.setImage((String)image.get("large"));
			//fetching data related to market by the help of market data object.
			coinDto.setCurrentPrice(convertToDouble(((Map<String,Object>)marketData.get("current_price")).get("inr")));
			coinDto.setMarketCap(convertToDouble(((Map<String,Object>)marketData.get("market_cap")).get("inr")));
			coinDto.setMarketCapRank(convertToDouble(((int)marketData.get("market_cap_rank"))));
			coinDto.setTotalVolume(convertToDouble(((Map<String,Object>)marketData.get("total_volume")).get("inr")));
			coinDto.setHigh24h(convertToDouble(((Map<String,Object>)marketData.get("high_24h")).get("inr")));
			coinDto.setLow24h(convertToDouble(((Map<String,Object>)marketData.get("low_24h")).get("inr")));
			coinDto.setPriceChange24h(convertToDouble(((double)marketData.get("price_change_24h"))));
			coinDto.setPriceChangePercentage24h(convertToDouble((marketData.get("price_change_percentage_24h"))));
			coinDto.setMarketCapChange24h(convertToDouble((marketData.get("market_cap_change_24h"))));
			coinDto.setMarketCapChangePercentage24h(convertToDouble((marketData.get("market_cap_change_percentage_24h"))));
			coinDto.setCirculatingSupply(convertToDouble((marketData.get("circulating_supply"))));
			coinDto.setTotalSupply(convertToDouble((marketData.get("total_supply"))));
			return coinDto;
		}
		else {
			throw new Exception("Coin Not Found");
		}
	}
//	@Override
//	public ApiResponse getCoinDetails(String prompt) throws Exception {
//		CoinDto coinDto=makeApiRequest(prompt);
//		System.out.println("Coin dto " + coinDto);
//		return null;
//	}
	public FunctionResponse getFunctionResponse(String prompt) {
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + gemini_api_key;

        // Create JSON request body using method chaining
        JSONObject requestBodyJson = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)
                                        )
                                )
                        )
                )
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "getCoinDetails")
                                                .put("description", "Get the coin details from given currency object")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyName", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put(
                                                                                "description",
                                                                                "The currency name, " +
                                                                                        "id, symbol.")
                                                                )
                                                                .put("currencyData", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description",
                                                                                "Currency Data id, " +
                                                                                        "symbol, " +
                                                                                        "name, " +
                                                                                        "image, " +
                                                                                        "current_price, " +
                                                                                        "market_cap, " +
                                                                                        "market_cap_rank, " +
                                                                                        "fully_diluted_valuation, " +
                                                                                        "total_volume, high_24h, " +
                                                                                        "low_24h, price_change_24h, " +
                                                                                        "price_change_percentage_24h, " +
                                                                                        "market_cap_change_24h, " +
                                                                                        "market_cap_change_percentage_24h, " +
                                                                                        "circulating_supply, " +
                                                                                        "total_supply, " +
                                                                                        "max_supply, " +
                                                                                        "ath, " +
                                                                                        "ath_change_percentage, " +
                                                                                        "ath_date, " +
                                                                                        "atl, " +
                                                                                        "atl_change_percentage, " +
                                                                                        "atl_date, last_updated.")
                                                                )
                                                        )
                                                        .put("required", new JSONArray()
                                                                .put("currencyName")
                                                                .put("currencyData")
                                                        )
                                                )
                                        )
                                )
                        )
                );

        // Create HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);

        // Make the POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

        String responseBody = response.getBody();

        JSONObject jsonObject = new JSONObject(responseBody);

        // Extract the first candidate
        JSONArray candidates = jsonObject.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);

        // Extract the function call details
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        JSONObject firstPart = parts.getJSONObject(0);
        JSONObject functionCall = firstPart.getJSONObject("functionCall");

        String functionName = functionCall.getString("name");
        JSONObject args = functionCall.getJSONObject("args");
        String currencyName = args.getString("currencyName");
        String currencyData = args.getString("currencyData");

        // Print or use the extracted values
        System.out.println("Function Name: " + functionName);
        System.out.println("Currency Name: " + currencyName);
        System.out.println("Currency Data: " + currencyData);

       FunctionResponse res=new FunctionResponse();
       res.setFunctionName(functionName);
       res.setCurrencyName(currencyName);
       res.setCurrencyData(currencyData);

        return res;
    }

    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {
        FunctionResponse res = getFunctionResponse(prompt);
        CoinDto apiResponse=makeApiRequest(res.getCurrencyName().toLowerCase());


        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
                + gemini_api_key;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create JSON body using method chaining
        String body = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)
                                        )
                                )
                        )
                        .put(new JSONObject()
                                .put("role", "model")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionCall", new JSONObject()
                                                        .put("name", "getCoinDetails")
                                                        .put("args", new JSONObject()
                                                                .put("currencyName", res.getCurrencyName())
                                                                .put("currencyData", res.getCurrencyData())
                                                        )
                                                )
                                        )
                                )
                        )
                        .put(new JSONObject()
                                .put("role", "function")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionResponse", new JSONObject()
                                                        .put("name", "getCoinDetails")
                                                        .put("response", new JSONObject()
                                                                .put("name", "getCoinDetails")
                                                                .put("content", apiResponse)
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "getCoinDetails")
                                                .put("description", "Get crypto currency data from given currency object.")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyName", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description",
                                                                                "The currency Name, " +
                                                                                        "id, " +
                                                                                        "symbol.")
                                                                )
                                                                .put("currencyData", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description",
                                                                                "The currency data id, " +
                                                                                        "symbol, current price, " +
                                                                                        "image, " +
                                                                                        "market cap rank"+
                                                                                        "market cap extra...")
                                                                )
                                                        )
                                                        .put("required", new JSONArray()
                                                                .put("currencyName")
                                                                .put("currencyData")
                                                        )
                                                )
                                        )

                                )
                        )
                )
                .toString();

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);

        String responseBody = response.getBody();

        System.out.println("------ " + responseBody);

        JSONObject jsonObject = new JSONObject(responseBody);

        // Extract the first candidate
        JSONArray candidates = jsonObject.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);

        // Extract the text
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        JSONObject firstPart = parts.getJSONObject(0);
        String text = firstPart.getString("text");

        ApiResponse ans =new ApiResponse();

        ans.setMessage(text);
        
        return ans;
    }

	
	//simple chat logic
	@Override
	public String simpleChart(String prompt) {
		String gemini_api_url="https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key="+gemini_api_key;
		
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestBody=new JSONObject().put("contents", new JSONArray()
				.put(new JSONObject().
				 put("parts", new JSONArray().
				 put(new JSONObject()
			    .put("text",prompt))))).toString();
		HttpEntity<String> requestEntity=new HttpEntity<>(requestBody,headers);
		RestTemplate restTemplate=new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(gemini_api_url, requestEntity, String.class);
		
		return response.getBody();
	}

}
